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
 * RMS�ɓo�^����Ă��郁�b�Z�[�W���\�[�X���e�l�X�L�[�}�ɓo�^����N���X�B
 * <pre>
 * ���b�Z�[�WID���X�V�������R�[�h�́Adiff(����)�����s�����ۂɂ͓��e�����f����܂���B
 * ����Ƀf�[�^�𔽉f���������ꍇ��all(�S��)���w�肵�Ă��������B
 *
 * SQL���͈ȉ��̐ݒ�t�@�C���ɋL�q����Ă��邽�߁A�\�[�X���C������ۂ͒��ӂ��Ă��������B
 * murata-co/src/main/resources/murata/co/message/MassageCopySql.properties
 *
 * DB�ڑ����͈ȉ��̐ݒ�t�@�C���ɋL�q����Ă��܂��B
 * %PROJECT%/src/main/resources/jdbc.properties
 * </pre>
 * @author H.Hayata
 * @version $Id: MessageCopy.java 72321 2008-04-25 04:03:25Z A1M4MEM108 $
 */
public class MessageCopy {

    /** Log instance */
    private org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(this.getClass());

    /** ���s��ʁF���� */
    private static final String EXEC_DIFF = "diff";

    /** ���s��ʁF�S�� */
    private static final String EXEC_ALL = "all";

    /** ���s��ʁF�X�L�b�v */
    private static final String EXEC_SKIP = "skip";

    /** ���b�Z�[�WID�ő�o�^�\���� */
    private static final int MESSAGEID_MAX_LENGTH = 256;

    /** JDBC�ڑ��ݒ�t�@�C�� */
    private Properties jdbcPops = null;

    /** SQL�ݒ�t�@�C�� */
    private Properties sqlPops = null;

    /** ���ݓ��� */
    private java.sql.Timestamp now = null;

    /** JDBC�h���C�o */
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

    /** RMS���烁�b�Z�[�W���\�[�X���擾���� */
    private static final String SQLKEY_RMS_S_MESSAGE = "rms.select.message";

    /** RMS���烁�b�Z�[�W���\�[�X���擾����(���t�����t��) */
    private static final String SQLKEY_RMS_S_MESSAGEDATE =
        "rms.select.messagedate";

    /** �ʃX�L�[�}�Ƀf�[�^��ǉ����� */
    private static final String SQLKEY_PRIVATE_I_MESSAGE =
        "private.insert.message";

    /** �ʃX�L�[�}�̃f�[�^���X�V���� */
    private static final String SQLKEY_PRIVATE_U_MESSAGE =
        "private.update.message";

    /** �ʃX�L�[�}����f�[�^���폜���� */
    private static final String SQLKEY_PRIVATE_D_MESSAGE =
        "private.delete.message";

    /** �ʃX�L�[�}�ɓo�^�σf�[�^����ő���t���擾���� */
    private static final String SQLKEY_PRIVATE_S_MAXDATE =
        "private.select.maxdate";

    /**
     * �A�v���P�[�V�������s���\�b�h�B
     * @param args ���s����
     */
    public static void main(String[] args) {
        if (args.length != 1
            || !(args[0].equals(EXEC_DIFF) || args[0].equals(EXEC_ALL))) {
            throw new RuntimeException("�����ɂ́udiff�v���uall�v���w�肵�Ă��������B");
        }
        new MessageCopy().rms2Db(args[0]);
    }

    /**
     * �A�v���P�[�V�������s���\�b�h�{�́B
     * @param type ���s��ʁFdiff(����) or all(�S��)
     */
    public void rms2Db(String type) {

    	// ���������ɏI��
    	if(type.equals(EXEC_SKIP)) {
    		return;
    	}

        long start = System.currentTimeMillis();

        // �p�����[�^�̏�����
        initParam();

        //
        Connection conn = null;
        Connection connRms = null;
        try {
            // �R�l�N�V�������擾���܂�
            conn = getConnection(CONNECTION_TYPE.PRIVATE);
            connRms = getConnection(CONNECTION_TYPE.RMS);

            // SQL���s
            int resultCount = 0;
            if (type.equals(EXEC_ALL)) {
                resultCount = executeQueryAll(conn, connRms);
            } else if (type.equals(EXEC_DIFF)) {
                resultCount = executeQueryDiff(conn, connRms);
            }

            // �f�[�^�x�[�X���f
            conn.commit();

            // �X�V����
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
     * �������w�肳�ꂽ�ꍇ�̔��sSQL���擾����B
     * <p>
     * ���sSQL�́ARMS����擾�������׃f�[�^�����ɐ������܂��B<br/>
     * ���b�Z�[�WID�����l�łȂ����׃f�[�^�͑ΏۊO�Ƃ���B
     * </p>
     * @param conn �R�l�N�V����
     * @param connRms RMS�R�l�N�V����
     * @return �X�V����
     * @throws SQLException SQLException
     */
    private int executeQueryDiff(Connection conn, Connection connRms)
        throws SQLException {

        //
        PreparedStatement psSel = null;
        ResultSet rsSel = null;

        // �����f�[�^������ő���t���擾����
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

        // RMS����f�[�^���擾����
        int resultCount = 0;
        PreparedStatement psIns = null;
        PreparedStatement psUpd = null;
        try {

            // ���sSQL
            psUpd = conn.prepareStatement(getSql(SQLKEY_PRIVATE_U_MESSAGE));
            psIns = conn.prepareStatement(getSql(SQLKEY_PRIVATE_I_MESSAGE));

            // ���b�Z�[�W�X�V�^�ǉ�
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
                // Update���������݂��Ȃ��ꍇ�AInsert�����s����
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
     * �S�����w�肳�ꂽ�ꍇ�̔��sSQL���擾����B
     * <p>
     * ���sSQL�́ARMS����擾�������׃f�[�^�����ɐ������܂��B<br/>
     * ���b�Z�[�WID�����l�łȂ����׃f�[�^�͑ΏۊO�Ƃ���B
     * </p>
     * @param conn �R�l�N�V����
     * @param connRms RMS�R�l�N�V����
     * @return �X�V����
     * @throws SQLException SQLException
     */
    private int executeQueryAll(Connection conn, Connection connRms)
        throws SQLException {
        int resultCount = 0;
        PreparedStatement psIns = null;
        PreparedStatement psSel = null;
        ResultSet rsSel = null;
        try {

            // ���b�Z�[�W�폜
            PreparedStatement psDel = null;
            try {
                psDel = conn.prepareStatement(getSql(SQLKEY_PRIVATE_D_MESSAGE));
                psDel.executeUpdate();
            } finally {
                if (psDel != null) {
                    psDel.close();
                }
            }

            // ���sSQL
            psIns = conn.prepareStatement(getSql(SQLKEY_PRIVATE_I_MESSAGE));

            // ���b�Z�[�W�ǉ�
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
     * CR9020�ɑ΂���UPDATE�������s���܂��B
     * @param ps PreparedStatement
     * @param messageId ���b�Z�[�WID
     * @param message ���b�Z�[�W
     * @param message ���b�Z�[�W�p��
     * @return ���s����
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
     * CR9020�ɑ΂���INSERT�������s���܂��B
     * @param ps PreparedStatement
     * @param messageId ���b�Z�[�WID
     * @param message ���b�Z�[�W
     * @return ���s����
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
     * SQL�ݒ�t�@�C��������SQL�𐶐����܂��B
     * @see this{@link #getSql(String, Object[])}
     */
    private String getSql(String id) {
        // delegate
        return getSql(id, null);
    }

    /**
     * SQL�ݒ�t�@�C��������SQL�𐶐����܂��B
     * @param id SQL�����ʂ���ID
     * @param args �t�H�[�}�b�g���邩�܂��͒u��������I�u�W�F�N�g����Ȃ�z��
     */
    private String getSql(String id, Object[] args) {
        String sql = MessageFormat.format((String)sqlPops.get(id), args);
        if (log.isDebugEnabled()) {
            log.debug(sql);
        }
        return sql;
    }

    /**
     * arg���o�^�\�����𒴂��Ă��Ȃ������`�F�b�N���܂��B
     * <p>
     * RMS����擾�������b�Z�[�WID�����b�Z�[�WID(CR9020.ID90400)�o�^�\������
     * �����Ă���ꍇ�͑ΏۊO�Ƃ���B
     * </p>
     * @param arg �`�F�b�N����
     * @return null �܂��� �o�^�\�����𒴂��Ă���ꍇ��false
     */
    private boolean checkLength(String arg) {
        if (arg == null || arg.length() > MESSAGEID_MAX_LENGTH) {
            return false;
        }
        return true;
    }

    /**
     * �R�l�N�V�������擾���܂��B
     * @param type �R�l�N�V�����ڑ���
     */
    private Connection getConnection(CONNECTION_TYPE type) {

        // �R�l�N�V�����ڑ����
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
            throw new RuntimeException("�R�l�N�V�����ڑ���񂪑��݂��܂���ł����B");
        }

        // �R�l�N�V��������
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
     * Oracle��TO_DATE�֐��`���̕������ԋp���܂��B
     * <p>
     * YYYYMMDD�`���̃t�H�[�}�b�g��ԋp���܂��B
     * </p>
     */
    private String createToDateMethod(String date) {
        return MessageFormat.format("TO_DATE(''{0}'', ''YYYYMMDDHH24MISS'')",
            new Object[]{date});
    }

    /**
     * ���̃A�v���P�[�V�����ŗ��p����p�����[�^�����������܂��B
     */
    private void initParam() {
        // �o�^�������擾
        if (now == null) {
            now = new Timestamp(System.currentTimeMillis());
        }
        // �v���p�e�B����ǂݍ���
        loadProperties();
    }

    /**
     * �v���p�e�B����ǂݍ��݂܂��B
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
     * �v���p�e�B����ǂݍ��݂܂��B
     * @param name �v���p�e�B�t�@�C����
     * @return �v���p�e�B���
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
     * �R�l�N�V�����ڑ���B
     * @author H.Hayata
     * @version $Id: MessageCopy.java 72321 2008-04-25 04:03:25Z A1M4MEM108 $
     * @since JDK5.0
     */
    private enum CONNECTION_TYPE {
        /**
         * �l�X�L�[�}�B
         */
        PRIVATE,
        /**
         * RMS�B
         */
        RMS
    }

}
