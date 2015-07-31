package murata.rms.batch.dmrimport.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.commons.logging.Log;

public class ResourceStore {
	// RMSコネクション
	private Connection connect = null;
	// 標準的出力をすべてログに
	private Log log = null;

	// 定数郡
	public static final int RES_TYPE_ENTITY = 61;
	public static final int RES_TYPE_FIELD 	= 62;
	public static final int RES_TYPE_CRITERIA = 41;

	private static final String OPE_USER = "DMR2RMS importer";
	private static final Timestamp OPE_TSTAMP = new Timestamp(System.currentTimeMillis());

	private static final boolean IS_ORACLE = true;

	public ResourceStore(Connection connection , Log log) {
		this.connect = connection;
		this.log = log;
	}

	/**
	 * ID_CR9060の最大(場合によってはシーケンスの値)をとって来る
	 * @throws SQLException
	 */
	private int getNextResourceId() throws SQLException {
		int newId = 1;
		String sql = "";
		if (IS_ORACLE) {
			sql = "SELECT ID_CR9060_SEQ.NEXTVAL FROM DUAL ";
//			sql = "select nvl(max(ID_CR9060) , 0) + 1 from CR9060 where ID_CR9060 < 5000";
		} else {
			sql = "select nvl(max(ID_CR9060) , 0) + 1 from CR9060 where ID_CR9060 < 5000";
		}
		Statement st = connect.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs != null && rs.next()) {
			newId = rs.getInt(1);
		}
		rs.close();
		st.close();
		return newId;
	}
	/**
	 * ID_CR9062の最大(場合によってはシーケンスの値)をとって来る
	 * @throws SQLException
	 */
	private int getNextSpecificationId() throws SQLException {
		int newId = 1;

		String sql = "";
		if (IS_ORACLE) {
			sql = "SELECT ID_CR9062_SEQ.NEXTVAL FROM DUAL ";
		} else {
			sql = "select nextval('ID_CR9062_SEQ');";
		}

		Statement st = connect.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs != null && rs.next()) {
			newId = rs.getInt(1);
		}
		rs.close();
		st.close();
		return newId;
	}

	/**
	 * 引数の情報を元に、リソースを一件登録する。
	 * @param resourceTypeId
	 * @param resourceName
	 * @return
	 * @throws SQLException
	 */
	public int addResource(int resourceTypeId , String resourceName) throws SQLException {
		// ID_CR9060 の次のものを発行。
		int newId = getNextResourceId();
		// SQL作成
		String sql = "INSERT INTO CR9060 (" +
		" ID_CR9060," +
		" ID_CR9061," +
		" DH90301," +
		" NO90300," +
		" KB90300," +
		" DH90304," +
		" HI90300," +
		" DH90303," +
		" PMS_I_USR," +
		" PMS_I_YMD," +
		" PMS_I_CLASS," +
		" INVALID_FLAG," +
		" VALID_FROM," +
		" VALID_TO," +
		" VERSION" +
		") VALUES (" +
		" ?," +
		" ?," +
		" ?," +
		" 1," +
		" 3," +
		" ?," +
		" ?," +
		" ?," +
		" ?," +
		" SYSDATE," +
		" ?," +
		" 0," +
		" SYSDATE," +
		" NULL," +
		" 1" +
		")";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , newId);
		pstmt.setInt(2 , resourceTypeId);
		pstmt.setString(3 , resourceName);
		pstmt.setString(4 , OPE_USER);
		pstmt.setTimestamp(5 , OPE_TSTAMP);
		pstmt.setString(6 , "インポータにより自動作成。 " + OPE_USER + " , " + OPE_TSTAMP.toString());
		pstmt.setString(7 , OPE_USER);
		pstmt.setString(8 , this.getClass().getName());

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		// 開放
		pstmt.close();
		pstmt = null;

		// 使ったリソースIDを保存
		return newId;

	}
	/**
	 * 引数の情報を元に、リソースを一件更新する。
	 * @param resourceId
	 * @param resourceName
	 * @return
	 * @throws SQLException
	 */
	public int updateResource(int resourceId , String resourceName) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql =
			" UPDATE  " +
			"  CR9060 " +
			" SET " +
			"  DH90301 = ? , " +
			"  NO90300 = NO90300 + 1,  " +
			"  DH90303 = DH90303 || ? ,  " +
			"  PMS_U_YMD = SYSDATE , " +
			"  PMS_U_USR = ? , " +
			"  PMS_U_CLASS = ? , " +
			"  VERSION = VERSION + 1 " +
			" WHERE ID_CR9060 = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setString(1 , resourceName);
		pstmt.setString(2 , "\nインポータにより更新。 " + OPE_USER + " , " + OPE_TSTAMP.toString());
		pstmt.setString(3 , OPE_USER);
		pstmt.setString(4 , this.getClass().getName());
		pstmt.setInt(5 , resourceId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}

	/**
	 * 引数の情報を元に、リソースを一件削除する。
	 * @param resourceTypeId
	 * @param resourceName
	 * @return
	 * @throws SQLException
	 */
	public int deleteResource(int resourceId) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql = " DELETE FROM CR9060 WHERE ID_CR9060 = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , resourceId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}
	/**
	 * 引数の情報を元に、明細を一件登録する。
	 * @param resourceId
	 * @param content
	 * @return
	 * @throws SQLException
	 */
	public int addSpecification(int resourceId , int specificationTypeId ,  String content) throws SQLException {
		// ID_CR9062 の次のものを発行。
		int newId = getNextSpecificationId();
		// SQL作成
		String sql = "INSERT INTO CR9062 (" +
			" ID_CR9062," +
			" ID_CR9060," +
			" ID_CR9063," +
			" DH90305," +
			" NO90300," +
			" PMS_I_USR," +
			" PMS_I_YMD," +
			" PMS_I_CLASS," +
			" INVALID_FLAG," +
			" VALID_FROM," +
			" VALID_TO," +
			" VERSION" +
			") VALUES (" +
			" ?," +
			" ?," +
			" ?," +
			" ?," +
			" 1," +
			" ?," +
			" SYSDATE," +
			" ?," +
			" 0," +
			" SYSDATE," +
			" NULL ," +
			" 1" +
			")";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , newId);
		pstmt.setInt(2 , resourceId);
		pstmt.setInt(3 , specificationTypeId);
		pstmt.setString(4 , content);
		pstmt.setString(5 , OPE_USER);
		pstmt.setString(6 , this.getClass().getName());

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		// 開放
		pstmt.close();
		pstmt = null;

		// 使ったリソースIDを保存
		return newId;
	}

	/**
	 * 引数の情報を元に、明細を一件更新する。
	 * @param specificationId
	 * @param content
	 * @return
	 * @throws SQLException
	 */
	public int updateSpecification(int specificationId , String content) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql =
			" UPDATE CR9062 SET " +
			"  DH90305 = ? ,   " +
			"  NO90300 = NO90300,  " +
			"  PMS_U_YMD = SYSDATE , " +
			"  PMS_U_USR = ?  , " +
			"  PMS_U_CLASS = ?  , " +
			"  VERSION = VERSION + 1 " +
			" WHERE ID_CR9062 = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setString(1 , content);
		pstmt.setString(2 , OPE_USER);
		pstmt.setString(3 , this.getClass().getName());
		pstmt.setInt(4 , specificationId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}

	/**
	 * 引数の情報(リソースIDと明細タイプ)を元に、明細を一件更新する。
	 * @param specificationId
	 * @param content
	 * @return
	 * @throws SQLException
	 */
	public int updateSpecification(int resourceId , int specificationType , String content) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql =
			" UPDATE CR9062 SET " +
			"  DH90305 = ? ,   " +
			"  NO90300 = NO90300,  " +
			"  PMS_U_YMD = SYSDATE , " +
			"  PMS_U_USR = ?  , " +
			"  PMS_U_CLASS = ?  , " +
			"  VERSION = VERSION + 1 " +
			" WHERE ID_CR9060 = ? " +
			"  AND ID_CR9063 = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setString(1 , content);
		pstmt.setString(2 , OPE_USER);
		pstmt.setString(3 , this.getClass().getName());
		pstmt.setInt(4 , resourceId);
		pstmt.setInt(5 , specificationType);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}


	/**
	 * 引数の情報を元に、明細を一件削除する。
	 * @param specificationId
	 * @return
	 * @throws SQLException
	 */
	public int deleteSpecification(int specificationId) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql = " DELETE FROM CR9062 WHERE ID_CR9062 = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , specificationId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}

	/**
	 * 引数のリソースIDを元に、明細を削除する(複数件削除)。
	 * @param resourceId
	 * @return
	 * @throws SQLException
	 */
	public int deleteSpecificationByResourceId(int resourceId) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql = " DELETE FROM CR9062 WHERE ID_CR9060 = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , resourceId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;
	}

	/**
	 * 引数の情報を元に、関連を一件登録する。
	 * @return
	 * @throws SQLException
	 */
	public int addParenthood(int parentId , int childId , String description) throws SQLException {
		// SQL作成
		String sql = "INSERT INTO CR9064 (" +
			" ID_CR9060_CHILD  , " +
			" ID_CR9060_PARENT  , " +
			" PMS_I_USR  , " +
			" PMS_I_YMD  , " +
			" PMS_I_CLASS  , " +
			" INVALID_FLAG  , " +
			" VALID_FROM  , " +
			" VALID_TO  , " +
			" VERSION  , " +
			" DH90307  " +
			") VALUES (" +
			" ?," +
			" ?," +
			" ?," +
			" SYSDATE," +
			" ?," +
			" 0," +
			" SYSDATE," +
			" NULL ," +
			" 1," +
			" ? " +
			")";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , childId);
		pstmt.setInt(2 , parentId);
		pstmt.setString(3 , OPE_USER);
		pstmt.setString(4 , this.getClass().getName());
		pstmt.setString(5 , description);

		// デバッグ
		log.debug(sql);

		// 実行
		boolean result = pstmt.execute();
		// 開放
		pstmt.close();
		pstmt = null;

		return (result ? 1 : 0);
	}
	/**
	 * 引数の情報を元に、関連を一件更新する。
	 * @param specificationId
	 * @param content
	 * @return
	 * @throws SQLException
	 */
	public int updateParenthood(int parentId , int childId , String description) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql =
			" UPDATE CR9064 SET " +
			"  DH90307 = ? ,  " +
			"  PMS_U_YMD = SYSDATE , " +
			"  PMS_U_USR = ? , " +
			"  PMS_U_CLASS = ? , " +
			" VERSION = VERSION + 1 " +
			" WHERE " +
			"  ID_CR9060_PARENT = ? " +
			"  AND ID_CR9060_CHILD = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setString(1 , description);
		pstmt.setString(2 , OPE_USER);
		pstmt.setString(3 , this.getClass().getName());
		pstmt.setInt(4 , parentId);
		pstmt.setInt(5 , childId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}

	/**
	 * 引数の情報を元に、関連を一件削除する。
	 * @param specificationId
	 * @return
	 * @throws SQLException
	 */
	public int deleteParenthood(int parentId ,  int childId ) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql =
			" DELETE FROM CR9064 " +
			" WHERE " +
			"  ID_CR9060_PARENT = ? " +
			"  AND ID_CR9060_CHILD = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , parentId);
		pstmt.setInt(2 , childId);

		// デバッグ
		log.debug(sql);
		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}

	/**
	 * 引数のリソースIDを元に、それを親、あるいは子とする関連をすべて削除する。(複数件削除)
	 * @param specificationId
	 * @return
	 * @throws SQLException
	 */
	public int deleteParenthoodByResourceId(int resourceId) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql =
			" DELETE FROM CR9064 " +
			" WHERE " +
			"  ID_CR9060_PARENT = ? " +
			"  OR ID_CR9060_CHILD = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , resourceId);
		pstmt.setInt(2 , resourceId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}

	/**
	 * リソース名でリソースを検索し、あればリソースIDを、無ければ0を返す。
	 * @param resourceNameJp
	 * @return
	 * @throws SQLException
	 */
	public int searchResourceByNameJp(String resourceNameJp) throws SQLException {
		int resourceId = 0;
		String sql = "SELECT ID_CR9060 FROM CR9060 WHERE DH90301 = ? ";
		PreparedStatement st = connect.prepareStatement(sql);
		st.setString(1,resourceNameJp);
		ResultSet rs = st.executeQuery();
		if (rs != null && rs.next()) {
			resourceId = rs.getInt(1);
		}
		rs.close();
		st.close();
		return resourceId;

	}
	/**
	 * 引数のリソースIDを元に、関係者(stakeholder)を削除する。(複数件削除)
	 * @param resourceId
	 * @return
	 * @throws SQLException
	 */
	public int deleteStakeholderByResourceId(int resourceId) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql = " DELETE FROM CR9060 WHERE ID_CR9060 = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , resourceId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}

	/**
	 * 引数のリソースIDを元に、変更通知(notification)を削除する。(複数件削除)
	 * @param resourceId
	 * @return
	 * @throws SQLException
	 */
	public int deleteNotificationByResourceId(int resourceId) throws SQLException {
		int resultCount = 0;
		// SQL作成
		String sql = " DELETE FROM CR9080 " +
					" WHERE ID_CR9060_SOURCE = ? " +
					"    OR ID_CR9060_TARGET = ? ";

		// 穴あきステートメント作成
		PreparedStatement pstmt = connect.prepareStatement(sql);
		// 穴埋め
		pstmt.setInt(1 , resourceId);
		pstmt.setInt(2 , resourceId);

		// デバッグ
		log.debug(sql);

		// 実行
		pstmt.execute();
		resultCount = pstmt.getUpdateCount();
		// 開放
		pstmt.close();
		pstmt = null;

		return resultCount;

	}


}
