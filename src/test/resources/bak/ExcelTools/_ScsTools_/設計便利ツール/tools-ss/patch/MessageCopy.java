package p;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Properties;

import murata.co.inspection.LogKeywords;
import murata.co.inspection.LogMessageCreator;
import murata.co.util.StringUtils;

/**
 * RMSに登録されているメッセージリソースを各個人スキーマに登録するクラス。
 * <pre>
 * メッセージIDを更新したレコードは、diff(差分)を実行した際には内容が反映されません。
 * 正常にデータを反映させたい場合はall(全件)を指定してください。
 *
 * SQL情報は以下の設定ファイルに記述されているため、ソースを修正する際は注意してください。
 * murata-co/src/main/resources/murata/co/message/MassageCopySql.properties
 *
 * DB接続情報は以下の設定ファイルに記述されています。
 * %PROJECT%/src/main/resources/jdbc.properties
 * </pre>
 * @author H.Hayata
 * @version $Id: MessageCopy.java 72321 2008-04-25 04:03:25Z A1M4MEM108 $
 */
public class MessageCopy {

    /** Log instance */
    private org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(this.getClass());

    /** 実行種別：差分 */
    private static final String EXEC_DIFF = "diff";

    /** 実行種別：全件 */
    private static final String EXEC_ALL = "all";

    /** 実行種別：スキップ */
    private static final String EXEC_SKIP = "skip";

    /** メッセージID最大登録可能桁数 */
    private static final int MESSAGEID_MAX_LENGTH = 256;

    /** JDBC接続設定ファイル */
    private Properties jdbcPops = null;

    /** SQL設定ファイル */
    private Properties sqlPops = null;

    /** 現在日時 */
    private java.sql.Timestamp now = null;

    /** JDBCドライバ */
    private static final String POPKEY_JDBC_DRIVER = "jdbc.driverClassName";

    /** URL */
    private static final String POPKEY_URL = "jdbc.url";

    /** USERNAME */
    private static final String POPKEY_USERNAME = "jdbc.username";

    /** PASSWORD */
    private static final String POPKEY_PASSWORD = "jdbc.password";

    /** RMS.URL */
    private static final String POPKEY_RMS_URL = "rms.jdbc.url";

    /** RMS.USERNAME */
    private static final String POPKEY_RMS_USERNAME = "rms.jdbc.username";

    /** RMS.PASSWORD */
    private static final String POPKEY_RMS_PASSWORD = "rms.jdbc.password";

    /** RMSからメッセージリソースを取得する */
    private static final String SQLKEY_RMS_S_MESSAGE = "rms.select.message";

    /** RMSからメッセージリソースを取得する(日付条件付き) */
    private static final String SQLKEY_RMS_S_MESSAGEDATE =
        "rms.select.messagedate";

    /** 個別スキーマにデータを追加する */
    private static final String SQLKEY_PRIVATE_I_MESSAGE =
        "private.insert.message";

    /** 個別スキーマのデータを更新する */
    private static final String SQLKEY_PRIVATE_U_MESSAGE =
        "private.update.message";

    /** 個別スキーマからデータを削除する */
    private static final String SQLKEY_PRIVATE_D_MESSAGE =
        "private.delete.message";

    /** 個別スキーマに登録済データから最大日付を取得する */
    private static final String SQLKEY_PRIVATE_S_MAXDATE =
        "private.select.maxdate";

    /**
     * アプリケーション実行メソッド。
     * @param args 実行引数
     */
    public static void main(String[] args) {
        if (args.length != 1
            || !(args[0].equals(EXEC_DIFF) || args[0].equals(EXEC_ALL))) {
            throw new RuntimeException("引数には「diff」か「all」を指定してください。");
        }
        new MessageCopy().rms2Db(args[0]);
    }

    /**
     * アプリケーション実行メソッド本体。
     * @param type 実行種別：diff(差分) or all(全件)
     */
    public void rms2Db(String type) {

    	// 何もせずに終了
    	if(type.equals(EXEC_SKIP)) {
    		return;
    	}

        long start = System.currentTimeMillis();

        // パラメータの初期化
        initParam();

        //
        Connection conn = null;
        Connection connRms = null;
        try {
            // コネクションを取得します
            conn = getConnection(CONNECTION_TYPE.PRIVATE);
            connRms = getConnection(CONNECTION_TYPE.RMS);

            // SQL実行
            int resultCount = 0;
            if (type.equals(EXEC_ALL)) {
                resultCount = executeQueryAll(conn, connRms);
            } else if (type.equals(EXEC_DIFF)) {
                resultCount = executeQueryDiff(conn, connRms);
            }

            // データベース反映
            conn.commit();

            // 更新結果
            if (log.isInfoEnabled()) {
                log.info(LogMessageCreator.createMessage(LogKeywords.INFO_FRAMEWORK, "\"" + "Copy is complete. Exec Query " + resultCount  + "\""));
                log.info(LogMessageCreator.createMessage(LogKeywords.INFO_FRAMEWORK, "\"" + "Elapsed time is " + (System.currentTimeMillis() - start)  + "ms" + "\""));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connRms != null) {
                try {
                    connRms.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 差分が指定された場合の発行SQLを取得する。
     * <p>
     * 発行SQLは、RMSから取得した明細データを元に生成します。<br/>
     * メッセージIDが数値でない明細データは対象外とする。
     * </p>
     * @param conn コネクション
     * @param connRms RMSコネクション
     * @return 更新件数
     * @throws SQLException SQLException
     */
    private int executeQueryDiff(Connection conn, Connection connRms)
        throws SQLException {

        //
        PreparedStatement psSel = null;
        ResultSet rsSel = null;

        // 既存データ内から最大日付を取得する
        String maxDate = null;
        try {
            psSel = conn.prepareStatement(getSql(SQLKEY_PRIVATE_S_MAXDATE));
            rsSel = psSel.executeQuery();
            if (rsSel.next()) {
                maxDate = createToDateMethod(rsSel.getString(1));
            }
        } finally {
            if (rsSel != null) {
                rsSel.close();
            }
            if (psSel != null) {
                psSel.close();
            }
        }

        // RMSからデータを取得する
        int resultCount = 0;
        PreparedStatement psIns = null;
        PreparedStatement psUpd = null;
        try {

            // 発行SQL
            psUpd = conn.prepareStatement(getSql(SQLKEY_PRIVATE_U_MESSAGE));
            psIns = conn.prepareStatement(getSql(SQLKEY_PRIVATE_I_MESSAGE));

            // メッセージ更新／追加
            String messageId = null;
            String message = null;
            String message_en = null;
            if (maxDate == null) {
                psSel = connRms.prepareStatement(getSql(SQLKEY_RMS_S_MESSAGE));
            } else {
                psSel =
                    connRms.prepareStatement(getSql(SQLKEY_RMS_S_MESSAGEDATE,
                        new Object[]{maxDate}));
            }
            rsSel = psSel.executeQuery();
            while (rsSel.next()) {
                int index = 1;
                messageId = rsSel.getString(index++);
                message = StringUtils.convertNullToHalfSpace(rsSel.getString(index++));
                message_en = StringUtils.convertNullToHalfSpace(rsSel.getString(index++));
                if (!checkLength(messageId)) {
                    continue;
                }
                // Update件数が存在しない場合、Insertを実行する
                int updateCount = executeUpdate(psUpd, messageId, message, message_en);
                if (updateCount == 0) {
                    int insertCount = executeInsert(psIns, messageId, message, message_en);
                    resultCount += insertCount;
                }
                resultCount += updateCount;
            }
        } finally {
            if (rsSel != null) {
                rsSel.close();
            }
            if (psSel != null) {
                psSel.close();
            }
            if (psUpd != null) {
                psUpd.close();
            }
            if (psIns != null) {
                psIns.close();
            }
        }
        return resultCount;
    }

    /**
     * 全件が指定された場合の発行SQLを取得する。
     * <p>
     * 発行SQLは、RMSから取得した明細データを元に生成します。<br/>
     * メッセージIDが数値でない明細データは対象外とする。
     * </p>
     * @param conn コネクション
     * @param connRms RMSコネクション
     * @return 更新件数
     * @throws SQLException SQLException
     */
    private int executeQueryAll(Connection conn, Connection connRms)
        throws SQLException {
        int resultCount = 0;
        PreparedStatement psIns = null;
        PreparedStatement psSel = null;
        ResultSet rsSel = null;
        try {

            // メッセージ削除
            PreparedStatement psDel = null;
            try {
                psDel = conn.prepareStatement(getSql(SQLKEY_PRIVATE_D_MESSAGE));
                psDel.executeUpdate();
            } finally {
                if (psDel != null) {
                    psDel.close();
                }
            }

            // 発行SQL
            psIns = conn.prepareStatement(getSql(SQLKEY_PRIVATE_I_MESSAGE));

            // メッセージ追加
            String messageId = null;
            String message = null;
            String message_en = null;
            psSel = connRms.prepareStatement(getSql(SQLKEY_RMS_S_MESSAGE));
            rsSel = psSel.executeQuery();
            while (rsSel.next()) {
                int index = 1;
                messageId = rsSel.getString(index++);
                message = StringUtils.convertNullToHalfSpace(rsSel.getString(index++));
                message_en = StringUtils.convertNullToHalfSpace(rsSel.getString(index++));
                if (!checkLength(messageId)) {
                    continue;
                }
                resultCount += executeInsert(psIns, messageId, message, message_en);
            }

        } finally {
            if (rsSel != null) {
                rsSel.close();
            }
            if (psSel != null) {
                psSel.close();
            }
            if (psIns != null) {
                psIns.close();
            }
        }
        return resultCount;
    }

    /**
     * CR9020に対してUPDATE文を実行します。
     * @param ps PreparedStatement
     * @param messageId メッセージID
     * @param message メッセージ
     * @param message メッセージ英語
     * @return 実行結果
     * @throws SQLException SQLException
     */
    private int executeUpdate(PreparedStatement ps, String messageId,
        String message, String message_en) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                "update sql messageId:{0} message:{1}", new Object[]{messageId,
                                                                     message}));
        }
        ps.clearParameters();
        ps.setString(1, message);
        ps.setString(2, message_en);
        ps.setTimestamp(3, now);
        ps.setString(4, messageId);
        return ps.executeUpdate();
    }

    /**
     * CR9020に対してINSERT文を実行します。
     * @param ps PreparedStatement
     * @param messageId メッセージID
     * @param message メッセージ
     * @return 実行結果
     * @throws SQLException SQLException
     */
    private int executeInsert(PreparedStatement ps, String messageId,
        String message, String message_en ) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                "insert sql messageId:{0} message:{1}", new Object[]{messageId,
                                                                     message}));
        }
        ps.clearParameters();
        ps.setString(1, messageId);
        ps.setString(2, message);
        ps.setString(3, message_en);
        ps.setTimestamp(4, null);
        ps.setString(5, null);
        ps.setTimestamp(6, now);
        ps.setString(7, MessageCopy.class.getSimpleName());
        ps.setString(8, MessageCopy.class.getName());
        ps.setTimestamp(9, now);
        ps.setString(10, MessageCopy.class.getSimpleName());
        ps.setString(11, MessageCopy.class.getName());
        ps.setInt(12, 0);
        return ps.executeUpdate();
    }

    /**
     * SQL設定ファイルを元にSQLを生成します。
     * @see this{@link #getSql(String, Object[])}
     */
    private String getSql(String id) {
        // delegate
        return getSql(id, null);
    }

    /**
     * SQL設定ファイルを元にSQLを生成します。
     * @param id SQLを識別するID
     * @param args フォーマットするかまたは置き換えるオブジェクトからなる配列
     */
    private String getSql(String id, Object[] args) {
        String sql = MessageFormat.format((String)sqlPops.get(id), args);
        if (log.isDebugEnabled()) {
            log.debug(sql);
        }
        return sql;
    }

    /**
     * argが登録可能桁数を超えていないかをチェックします。
     * <p>
     * RMSから取得したメッセージIDがメッセージID(CR9020.ID90400)登録可能桁数を
     * 超えている場合は対象外とする。
     * </p>
     * @param arg チェック項目
     * @return null または 登録可能桁数を超えている場合はfalse
     */
    private boolean checkLength(String arg) {
        if (arg == null || arg.length() > MESSAGEID_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    /**
     * コネクションを取得します。
     * @param type コネクション接続先
     */
    private Connection getConnection(CONNECTION_TYPE type) {

        // コネクション接続情報
        String driver = (String)jdbcPops.get(POPKEY_JDBC_DRIVER);
        String url = null;
        String username = null;
        String password = null;
        if (type == CONNECTION_TYPE.PRIVATE) {
            url = (String)jdbcPops.get(POPKEY_URL);
            username = (String)jdbcPops.get(POPKEY_USERNAME);
            password = (String)jdbcPops.get(POPKEY_PASSWORD);
        } else if (type == CONNECTION_TYPE.RMS) {
            url = (String)jdbcPops.get(POPKEY_RMS_URL);
            username = (String)jdbcPops.get(POPKEY_RMS_USERNAME);
            password = (String)jdbcPops.get(POPKEY_RMS_PASSWORD);
        } else {
            throw new RuntimeException("コネクション接続情報が存在しませんでした。");
        }

        // コネクション生成
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * OracleのTO_DATE関数形式の文字列を返却します。
     * <p>
     * YYYYMMDD形式のフォーマットを返却します。
     * </p>
     */
    private String createToDateMethod(String date) {
        return MessageFormat.format("TO_DATE(''{0}'', ''YYYYMMDDHH24MISS'')",
            new Object[]{date});
    }

    /**
     * このアプリケーションで利用するパラメータを初期化します。
     */
    private void initParam() {
        // 登録日時を取得
        if (now == null) {
            now = new Timestamp(System.currentTimeMillis());
        }
        // プロパティ情報を読み込み
        loadProperties();
    }

    /**
     * プロパティ情報を読み込みます。
     */
    private void loadProperties() {
        if (jdbcPops == null) {
            jdbcPops = loadProperties("jdbc.properties");
        }
        if (sqlPops == null) {
            sqlPops =
                loadProperties("murata/co/message/MessageCopySql.properties");
        }
    }

    /**
     * プロパティ情報を読み込みます。
     * @param name プロパティファイル名
     * @return プロパティ情報
     */
    private Properties loadProperties(String name) {
        Properties pops = new Properties();
        try {
            pops.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pops;
    }

    /**
     * コネクション接続先。
     * @author H.Hayata
     * @version $Id: MessageCopy.java 72321 2008-04-25 04:03:25Z A1M4MEM108 $
     * @since JDK5.0
     */
    private enum CONNECTION_TYPE {
        /**
         * 個人スキーマ。
         */
        PRIVATE,
        /**
         * RMS。
         */
        RMS
    }

}
