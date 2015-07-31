package jp.mk.rmsresourcepursuer;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;

/**
 * ＤＢ辞書システムからデータをデータチェックするためのツール
 * (使い捨て)
 * @author K.Miura@SCS
 */
public abstract class SqlExecuter {

    static final String SETTING_FILE = "setting.xml";

	// 汎用ログ
	protected Log log = null;


	// 書出ｄｂ(リソース管理システム)の設定
	protected String driverClass = "";
	protected String jdbcUrl    = "";
	protected String user   = "";
	protected String password	  = "";

	protected String outputDirectory = "";
	protected String caption = "";
    protected boolean expandMethod = false;

	/**
	 * 初期化
	 */
	public SqlExecuter() {
//		log = LogFactory.getLog(this.getClass());
//		// デフォルト読込み
//		readSettingResource();
	}

	/**
	 * デフォルト情報を設定ファイルから取る
	 */
	protected void readSettingResource() {
		try {
			// 自身リソースのプロパティファイルから、自身Beanに対し値を読み込む
			Properties settings = new Properties();
			settings.loadFromXML(new FileInputStream(SETTING_FILE));
			// 面倒くさいから全自動コピー
            copyProperty4Properties(settings, this);
		} catch (Exception e) {
			// 読めなくても別によし
			e.printStackTrace();
		}
	}
    /**
     * 設定情報を設定ファイルへ保存
     * @param importer
     */
    protected void saveSettingResource() {
        try {
            // 自身リソースのプロパティファイルから、自身Beanに対し値を読み込む
            Properties settings = new Properties();
            // 面倒くさいから全自動コピー
            copyProperty4Properties(this , settings);
            String comment = this.getClass().getName() + " setting. Stored at " + (new Date()).toString();
            settings.storeToXML(new FileOutputStream(SETTING_FILE) , comment);
        } catch (Exception e) {
            // 読めなくても別によし
            e.printStackTrace();
        }
    }

    /**
     * Propertiesにコピーする共通メソッド
     * @param src
     * @param dest
     */
    public static void copyProperty4Properties(Object src , Object dest) {
        try {
            if (src instanceof Properties && dest instanceof Properties) {
                // プロパティ同士
                Properties destProperties = (Properties)dest;
                Properties srcProperties = (Properties)src;
                destProperties.clear();
                for (Enumeration e = srcProperties.propertyNames() ; e.hasMoreElements() ;) {
                    String name = (String)e.nextElement();
                    destProperties.setProperty(name, srcProperties.getProperty(name));
                }
            } else if (src instanceof Properties) {
                // 元側がプロパティ
                Properties srcProperties = (Properties)src;
                for (Enumeration e = srcProperties.propertyNames() ; e.hasMoreElements() ;) {
                    String name = (String)e.nextElement();
                    BeanUtils.setProperty(dest, name, srcProperties.getProperty(name));
                }
            } else if (dest instanceof Properties) {
                // 先がわがプロパティ
                BeanInfo info;
                Properties destProperties = (Properties)dest;
                destProperties.clear();
                info = Introspector.getBeanInfo(src.getClass());
                PropertyDescriptor pds[] = info.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    Object value = PropertyUtils.getProperty(src, pd.getName());
                    if (value == null || value instanceof Number
                        || value instanceof Boolean
                        || value instanceof String
                        || value instanceof Character) {
                        destProperties.setProperty(pd.getName(), BeanUtils.getProperty(src, pd.getName()));
                    }
                }
            } else {
                // オブジェクト同士
                BeanUtils.copyProperties(dest, src);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 実行
	 */
	public boolean execute() {

		boolean isSuccessed = false;

		Connection targetCon = null;
        BufferedWriter bw = null;

		try {
			// コネクションを空ける。
			targetCon = getConnect(driverClass , jdbcUrl , user , password);

			// オートコミットモードの解除
			targetCon.setAutoCommit(false);

            // フォルダをオブジェクトで渡す
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            File outTarget = new File(this.outputDirectory + "/" + sdf.format(new Date()) + ".log");
            FileWriter fw = new FileWriter(outTarget);
            bw = new BufferedWriter(fw);

			// 編集操作を行う。
            executeSql(targetCon, bw);

			// 確定する。
			targetCon.commit();

			// 閉じる。
			targetCon.close();

			isSuccessed = true;

		} catch (Exception e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();
			for (StackTraceElement elem : e.getStackTrace()) {
				if (sb.length() > 0) sb.append("\n");
				sb.append(elem.toString());
			}
			log.error(sb.toString());
			// 状態を戻す。
			try {
                if (targetCon != null) {
                    if (!targetCon.isClosed()) {
                        targetCon.rollback();
                        targetCon.close();
                    }
                }
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return isSuccessed;
	}

	/**
     * 個々のSQL実装を書く。
     * @param bw
	 */
	protected abstract void executeSql(Connection con, BufferedWriter resultWriter) throws Exception;

    /**
	 * 引数を元に、コネクションをあけ、返す。
	 * @param driver
	 * @param uri
	 * @param user
	 * @param pass
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnect(String driver , String uri , String user , String pass)
		throws ClassNotFoundException, SQLException
	{
		if (log != null) log.info("ドライバ: " + driver + " をロード。");
	    Class.forName(driver);
        if (log != null) log.info("データベース: " + uri + " を " + user + "で接続。");
	    Connection con = DriverManager.getConnection(uri, user, pass);
	    // 返す
	    return con;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String targetDbDriver) {
		this.driverClass = targetDbDriver;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String targetDbPass) {
		this.password = targetDbPass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String targetDbUri) {
		this.jdbcUrl = targetDbUri;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String targetDbUser) {
		this.user = targetDbUser;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}


    /**
     * logを戻す。
     * <br>
     * @return  log
     */
    public Log getLog() {
        return log;
    }


    /**
     * expandMethodを戻す。
     * <br>
     * @return  expandMethod
     */
    public boolean isExpandMethod() {
        return expandMethod;
    }


    /**
     * expandMethodを設定する。
     * <br>
     * @param expandMethod boolean
     */
    public void setExpandMethod(boolean expandMethod) {
        this.expandMethod = expandMethod;
    }

}
