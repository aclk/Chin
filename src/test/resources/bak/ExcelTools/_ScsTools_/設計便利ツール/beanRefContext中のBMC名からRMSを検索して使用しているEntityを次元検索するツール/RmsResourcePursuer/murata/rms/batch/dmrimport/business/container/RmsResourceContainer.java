package murata.rms.batch.dmrimport.business.container;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import murata.rms.batch.dmrimport.util.Converter;
import murata.rms.batch.dmrimport.util.ResourceStore;
import murata.rms.batch.dmrimport.vo.Bmc;
import murata.rms.batch.dmrimport.vo.BmcMethod;
import murata.rms.batch.dmrimport.vo.Criteria;
import murata.rms.batch.dmrimport.vo.DataField;
import murata.rms.batch.dmrimport.vo.Entity;
import murata.rms.batch.dmrimport.vo.LinkedParenthoods;
import murata.rms.batch.dmrimport.vo.Parenthood;
import murata.rms.batch.dmrimport.vo.Resource;

/**
 * RMS側のデータを取ってくるための、ResourceContainer
 * @author K.Miura
 * @author H.Hayata
 */
public class RmsResourceContainer extends ResourceContainer {

    private final String TARGET_SYSTEM = "RMS";

    /**
     * RMSからのリソースタイプ「Entity」データの読出
     * <p>
     * CriteriaMergerからEntity情報を呼び出すため、可視性を変更
     * </p>
     */
    public boolean loadEntity() {

        boolean result = false;

        try {

            // SQL発行
            log.info(TARGET_SYSTEM + "からのEntityデータの読込を開始。");

            ResultSet rs = getResultSetResource(61);

            // 全件まわす。
            Entity ent = null;
            int i = 0;
            while (rs.next()) {
                // コントロールブレイク的な処理。IDが前のと変わっていたら。
                if (ent == null
                    || ent.getResourceId() != rs.getInt("resource_id")) {
                    if (ent != null) {
                        addEntity(ent);
                        ++i;
                    }
                    ent = new Entity();;

                    // 共通項目をセットしておく
                    ent.setResourceId(rs.getInt("resource_id"));
                    ent.setEntityNameJp(rs.getString("resource_name"));
                    ent.setUpdateLog(rs.getString("update_log"));
                }
                // 明細からの値を構造体に入れる。
                switch (rs.getInt("specification_type_code")) {
                    case Entity.ENT_NAME_EN:
                        ent
                            .setEntityNameEnSpecId(rs
                                .getInt("specification_id"));
                        ent.setEntityNameEn(rs.getString("content"));
                        break;
                    case Entity.ENT_DESK:
                        ent.setDescSpecId(rs.getInt("specification_id"));
                        ent.setDesc(rs.getString("content"));
                        break;
                    case Entity.ENT_ENTITY_ID:
                        ent.setEntityIdSpecId(rs.getInt("specification_id"));
                        ent.setEntityId(rs.getString("content"));
                        break;
                    case Entity.ENT_COVER_KEY:
                        ent.setCoverKeySpecId(rs.getInt("specification_id"));
                        ent.setCoverKey(rs.getString("content"));
                        break;
                    case Entity.ENT_SUBJECT:
                        ent.setSubjectSpecId(rs.getInt("specification_id"));
                        ent.setSubject(rs.getString("content"));
                        break;
                }

            }
            // 最後の場合も登録
            if (ent != null) {
                addEntity(ent);
                ++i;
            }

            result = true;

            log.info(TARGET_SYSTEM + "からのEntityデータの読込終了。" + i + " 件のロード。");

        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのEntityデータの読込失敗終了。");
        }

        return result;
    }

    /**
     * RMSからのリソースタイプ「データ項目」データの読出
     */
    protected boolean loadDataField() {

        boolean result = false;

        try {

            // SQL発行
            log.info(TARGET_SYSTEM + "からのデータ項目のデータの読込を開始。");

            ResultSet rs = getResultSetResource(62);

            // 全件まわす。
            DataField field = null;
            int i = 0;
            while (rs.next()) {
                // コントロールブレイク的な処理。IDが前のと変わっていたら。
                if (field == null
                    || field.getResourceId() != rs.getInt("resource_id")) {
                    if (field != null) {
                        addDataField(field);
                        ++i;
                    }
                    field = new DataField();;
                    // 共通項目をセットしておく
                    field.setResourceId(rs.getInt("resource_id"));
                    field.setDataFieldNameJp(rs.getString("resource_name"));
                    field.setUpdateLog(rs.getString("update_log"));

                }
                // 明細からの値を構造体に入れる。
                switch (rs.getInt("specification_type_code")) {
                    case DataField.FLD_NAME_EN:
                        field.setDataFieldNameEnSpecId(rs
                            .getInt("specification_id"));
                        field.setDataFieldNameEn(rs.getString("content"));
                        break;
                    case DataField.FLD_DESK:
                        field.setDescSpecId(rs.getInt("specification_id"));
                        field.setDesc(rs.getString("content"));
                        break;
                    case DataField.FLD_DATA_FIELD_ID:
                        field.setDataFieldIdSpecId(rs
                            .getInt("specification_id"));
                        field.setDataFieldId(rs.getString("content"));
                        break;
                    case DataField.FLD_TYPE:
                        field.setTypeSpecId(rs.getInt("specification_id"));
                        field.setType(rs.getString("content"));
                        break;
                    case DataField.FLD_PRECISION:
                        field.setPrecisionSpecId(rs.getInt("specification_id"));
                        field.setPrecision(rs.getString("content"));
                        break;
                    case DataField.FLD_DECIMAL_POINT:
                        field.setDecimalPointSpecId(rs
                            .getInt("specification_id"));
                        field.setDecimalPoint(rs.getString("content"));
                        break;
                }

            }
            // 最後の場合も登録
            if (field != null) {
                addDataField(field);
                ++i;
            }

            result = true;

            log.info(TARGET_SYSTEM + "からのデータ項目のデータの読込終了。" + i + " 件のロード。");

        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのデータ項目のデータの読込失敗終了。");
        }

        return result;
    }

    /**
     * RMSからの「EntityからEntityへの関連」データの読出
     */
    protected boolean loadParenthoodE2E() {
        boolean result = false;
        try {
            log.info(TARGET_SYSTEM + "からのEntityからEntityへの関連のデータの読込を開始。");

            // 下準備。リソースIDをキーにしたマップを作成
            HashMap<Integer, Resource> entityResourceIdMap =
                createResourceIdMap(this.getEntityes());

            // SQL発行。両方61＝EntityToEntityの関連
            ResultSet rs =
                getResultSetParenthood(ResourceStore.RES_TYPE_ENTITY,
                    ResourceStore.RES_TYPE_ENTITY);

            // 全件まわす。
            Parenthood reration = null;
            int i = 0;
            while (rs.next()) {
                // 専用関数でオブジェクト作成・データ詰め
                reration =
                    mapParenthoodObject(rs, entityResourceIdMap,
                        entityResourceIdMap);
                // 自身が持ってるコレクションに足す。
                addParenthoodE2E(reration);
                i++;
            }
            result = true;
            log.info(TARGET_SYSTEM + "からのEntityからEntityへの関連のデータの読込終了。" + i
                + " 件のロード。");
        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのEntityからEntityへの関連のデータの読込失敗終了。");
        }

        return result;
    }

    /**
     * RMSからの「Entityからデータフィールドへの関連」データの読出
     */
    protected boolean loadParenthoodE2DFd() {
        boolean result = false;
        try {
            log.info(TARGET_SYSTEM + "からのEntityからデータフィールドへの関連のデータの読込を開始。");

            // 下準備。リソースIDをキーにしたマップを作成
            HashMap<Integer, Resource> entityResourceIdMap =
                createResourceIdMap(this.getEntityes());
            HashMap<Integer, Resource> dataFieldResourceIdMap =
                createResourceIdMap(this.getDataFields());

            // SQL発行。61から62＝EntityToデータフィールドの関連
            ResultSet rs =
                getResultSetParenthood(ResourceStore.RES_TYPE_ENTITY,
                    ResourceStore.RES_TYPE_FIELD);

            // 全件まわす。
            Parenthood reration = null;
            int i = 0;
            while (rs.next()) {
                // 専用関数でオブジェクト作成・データ詰め
                reration =
                    mapParenthoodObject(rs, entityResourceIdMap,
                        dataFieldResourceIdMap);
                // 自身が持ってるコレクションに足す。
                addParenthoodE2DFd(reration);
                i++;
            }
            result = true;
            log.info(TARGET_SYSTEM + "からのEntityからデータフィールドへの関連のデータの読込終了。" + i
                + " 件のロード。");
        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのEntityからデータフィールドへの関連のデータの読込失敗終了。");
        }

        return result;
    }

    /**
     * 引数のリソースタイプを元に、リソースと明細を連結した結果セットを返す。
     * @param resourceTypeId
     * @return
     * @throws SQLException
     */
    private ResultSet getResultSetResource(int resourceTypeId)
        throws SQLException {
        //        String sql =
        //            " SELECT  "
        //                + "  CR9060.ID_CR9060 as resource_id , "
        //                + "  CR9060.DH90301   as resource_name ,  "
        //                + "  CR9060.DH90303   as update_log ,  "
        //                + "  CR9060.PMS_I_YMD as create_time ,  "
        //                + "  CR9060.PMS_U_YMD as up_time , "
        //                + "  CR9062.ID_CR9062 as specification_id ,  "
        //                + "  CR9062.ID_CR9063 as specification_type_code ,  "
        //                + "  CR9062.DH90305 as content   "
        //                + " FROM CR9060 RIGHT JOIN CR9062 ON CR9060.ID_CR9060 = CR9062.ID_CR9060 "
        //                + " WHERE CR9060.ID_CR9061 = ? " + " ORDER BY  "
        //                + "   resource_id , " + "   specification_id ";

        String sql =
            " SELECT  "
                + "  CR9060.ID_CR9060 as resource_id , "
                + "  CR9060.DH90301   as resource_name ,  "
                + "  CR9060.DH90303   as update_log ,  "
                + "  CR9062.ID_CR9062 as specification_id ,  "
                + "  CR9062.ID_CR9063 as specification_type_code ,  "
                + "  CR9062.DH90305 as content   "
                + " FROM CR9060 INNER JOIN CR9062 ON CR9060.ID_CR9060 = CR9062.ID_CR9060 "
                + " WHERE CR9060.ID_CR9061 = ? AND CR9062.ID_CR9063 = 1 "
                + " ORDER BY  resource_id , specification_id ";

        log.debug(sql);

        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, resourceTypeId);
        ResultSet result = st.executeQuery();
        return result;

    }

    /**
     * 引数の親・子リソースタイプを元に、関連(parenthood)の結果セットを返す。
     * @param resourceTypeId
     * @return
     * @throws SQLException
     */
    private ResultSet getResultSetParenthood(int parentResourceTypeId,
        int childResourceTypeId) throws SQLException {
        String sql =
            " SELECT   "
                + "  parenthood.ID_CR9060_PARENT  as resource_id_parent , "
                + "  parenthood.ID_CR9060_CHILD   as resource_id_child , "
                + "  parenthood.DH90307           as description , "
                + "  parenthood.PMS_U_YMD as up_time "
                + " FROM "
                + "  ( CR9064 parenthood INNER JOIN CR9060 parent_resource "
                + "  ON parenthood.ID_CR9060_PARENT = parent_resource.ID_CR9060 ) "
                + "  INNER JOIN CR9060 child_resource "
                + "  ON parenthood.ID_CR9060_CHILD = child_resource.ID_CR9060"
                + " WHERE "
                + "  parent_resource.ID_CR9061    = ?"
                + "  AND child_resource.ID_CR9061 = ? "
                + " ORDER BY resource_id_parent , resource_id_child , description ";
        log.debug(sql);

        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, parentResourceTypeId);
        st.setInt(2, childResourceTypeId);

        ResultSet result = st.executeQuery();
        return result;

    }

    /**
     * parenthoodを扱ったレコードセットのカレント行情報から、
     * Parenthoodオブジェクトを作成し、返す。
     * @throws SQLException
     */
    private Parenthood mapParenthoodObject(ResultSet rs,
        HashMap<Integer, Resource> parentResources,
        HashMap<Integer, Resource> childResources) throws SQLException {

        Parenthood phood = new Parenthood();

        // リソース群のハッシュマップから、リソースIDで検索し、親・子のリソースオブジェクトをセット
        // (絶対に存在するはず！)
        phood.setParentResource(parentResources.get(rs
            .getInt("resource_id_parent")));
        phood.setChildResource(childResources.get(rs
            .getInt("resource_id_child")));

        phood.setDescription(rs.getString("description"));
        phood.setUpdateTime(rs.getDate("up_time"));
        return phood;
    }

    /**
     * リソースアイテムを溜め込んだマップから、リソースIDをキーにしたマップへ変換する
     * @param resourceMap
     * @return
     */
    private static HashMap<Integer, Resource> createResourceIdMap(
        HashMap resourceMap) {
        HashMap<Integer, Resource> nowKeyMap = new HashMap<Integer, Resource>();
        for (Iterator iter = resourceMap.values().iterator(); iter.hasNext();) {
            Resource item = (Resource)iter.next();
            nowKeyMap.put(item.getResourceId(), item);
        }
        return nowKeyMap;
    }

    /**
     * RMSからのリソースタイプ「クライテリア」データの読出
     */
    protected boolean loadCriterias() {

        try {

            // SQL発行
            log.info(TARGET_SYSTEM + "からのクライテリアのデータの読込を開始。");

            ResultSet rs =
                getResultSetResource(ResourceStore.RES_TYPE_CRITERIA);

            // 全件まわす。
            Criteria cri = null;
            int i = 0;
            while (rs.next()) {
                // コントロールブレイク的な処理。IDが前のと変わっていたら。
                if (cri == null
                    || cri.getResourceId() != rs.getInt("resource_id")) {
                    if (cri != null) {
                        criterias.put(
                            cri.getQlType() + cri.getResourceNameJp(), cri);
                        ++i;
                    }
                    cri = new Criteria();;
                    // 共通項目をセットしておく
                    cri.setResourceId(rs.getInt("resource_id"));
                    cri.setResourceNameJp(Converter.toCp932(rs
                        .getString("resource_name")));
                    cri.setUpdateLog(rs.getString("update_log"));
                    cri.setUpdateTime(rs.getDate("up_time"));

                }
                // 明細からの値を構造体に入れる。
                switch (rs.getInt("specification_type_code")) {
                    case Criteria.CRI_CRITERIA_ID: // クライテリアID
                        cri.setCriteriaIdSpecId(rs.getInt("specification_id"));
                        cri.setCriteriaId(rs.getString("content"));
                        break;
                    case Criteria.CRI_USE_VO_TYPE: // 使用するVOの型
                        cri.setUseVoTypeSpecId(rs.getInt("specification_id"));
                        cri.setUseVoType(rs.getString("content"));
                        break;
                    case Criteria.CRI_CRITERIA_TYPE: // タイプ
                        cri
                            .setCriteriaTypeSpecId(rs
                                .getInt("specification_id"));
                        cri.setCriteriaType(rs.getString("content"));
                        break;
                    case Criteria.CRI_QL_TYPE: // QLタイプ
                        cri.setQlTypeSpecId(rs.getInt("specification_id"));
                        cri.setQlType(rs.getString("content"));
                        break;
                    case Criteria.CRI_ENTITY_OBJECT_TYPE: // ENTITYオブジェクトの型
                        cri.setEntityObjectTypeSpecId(rs
                            .getInt("specification_id"));
                        cri.setEntityObjectType(rs.getString("content"));
                        break;
                    case Criteria.CRI_ENTITY_OBJECT_CREATE: // ENTITYオブジェクトの作成
                        cri.setEntityObjectCreateSpecId(rs
                            .getInt("specification_id"));
                        cri.setEntityObjectCreate(rs.getString("content"));
                        break;
                    case Criteria.CRI_CONTROL_BREAK: // コントロールブレイクの有無
                        cri
                            .setControlBreakSpecId(rs
                                .getInt("specification_id"));
                        cri.setControlBreak(rs.getString("content"));
                        break;
                    case Criteria.CRI_TABLE_LOCK: // テーブルロックの有
                        cri.setTableLockSpecId(rs.getInt("specification_id"));
                        cri.setTableLock(rs.getString("content"));
                        break;
                    case Criteria.CRI_DETAIL_DESC: // 詳細説明の有無
                        cri.setDetailDescSpecId(rs.getInt("specification_id"));
                        cri.setDetailDesc(Converter.toCp932(rs
                            .getString("content")));
                        break;
                    case Criteria.CRI_QL_TEMPLATE: // QLテンプレートの有無
                        cri.setQlTemplateSpecId(rs.getInt("specification_id"));
                        cri.setQlTemplate(rs.getString("content"));
                        break;
                    case Criteria.CRI_ADD_DESC: // 補足説明の有無
                        cri.setAddDescSpecId(rs.getInt("specification_id"));
                        cri.setAddDesc(rs.getString("content"));
                        break;
                }

            }
            // 最後の場合も登録
            if (cri != null) {
                criterias.put(cri.getQlType() + cri.getResourceNameJp(), cri);
                ++i;
            }

            log.info(TARGET_SYSTEM + "からのクライテリアのデータの読込終了。" + i + " 件のロード。");

        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのクライテリアのデータの読込失敗終了。");
        }

        return true;
    }

    /**
     * RMSからのリソースタイプ「BMCクラス」データの読出
     * <p>
     * CriteriaMergerからEntity情報を呼び出すため、可視性を変更
     * </p>
     */
    public boolean loadBmc() {

        boolean result = false;

        try {

            // SQL発行
            log.info(TARGET_SYSTEM + "からのBMCデータの読込を開始。");

            ResultSet rs = getResultSetResource(11);

            // 全件まわす。
            Bmc bmc = null;
            int i = 0;
            while (rs.next()) {
                // コントロールブレイク的な処理。IDが前のと変わっていたら。
                if (bmc == null
                    || bmc.getResourceId() != rs.getInt("resource_id")) {
                    if (bmc != null) {
                        addBmc(bmc);
                        ++i;
                    }
                    bmc = new Bmc();;

                    // 共通項目をセットしておく
                    bmc.setResourceId(rs.getInt("resource_id"));
                    bmc.setBmcNameJp(rs.getString("resource_name"));
                }
                // 明細からの値を構造体に入れる。
                switch (rs.getInt("specification_type_code")) {
                    case Entity.ENT_NAME_EN:
                        bmc.setBmcNameEn(rs.getString("content"));
                        break;
                    case Entity.ENT_DESK:
                        bmc.setDesc(rs.getString("content"));
                        break;
                }

            }
            // 最後の場合も登録
            if (bmc != null) {
                addBmc(bmc);
                ++i;
            }

            result = true;

            log.info(TARGET_SYSTEM + "からのBmcデータの読込終了。" + i + " 件のロード。");

        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのBmcデータの読込失敗終了。");
        }

        return result;
    }

    /**
     * RMSからのリソースタイプ「BMCメソッド」データの読出
     * <p>
     * CriteriaMergerからEntity情報を呼び出すため、可視性を変更
     * </p>
     */
    public boolean loadBmcMethod() {

        boolean result = false;

        try {

            // SQL発行
            log.info(TARGET_SYSTEM + "からのBMCメソッドデータの読込を開始。");

            ResultSet rs = getResultSetResource(12);

            // 全件まわす。
            BmcMethod bmcMethod = null;
            int i = 0;
            while (rs.next()) {
                // コントロールブレイク的な処理。IDが前のと変わっていたら。
                if (bmcMethod == null
                    || bmcMethod.getResourceId() != rs.getInt("resource_id")) {
                    if (bmcMethod != null) {
                        addBmcMethod(bmcMethod);
                        ++i;
                    }
                    bmcMethod = new BmcMethod();;

                    // 共通項目をセットしておく
                    bmcMethod.setResourceId(rs.getInt("resource_id"));
                    bmcMethod.setBmcMethodNameJp(rs.getString("resource_name"));
                }
                // 明細からの値を構造体に入れる。
                switch (rs.getInt("specification_type_code")) {
                    case BmcMethod.BMCMETHOD_NAME_EN:
                        bmcMethod.setBmcMethodNameEn(rs.getString("content"));
                        break;
                    case BmcMethod.BMCMETHOD_ENT_DESK:
                        bmcMethod.setDesc(rs.getString("content"));
                        break;
                }

            }
            // 最後の場合も登録
            if (bmcMethod != null) {
                addBmcMethod(bmcMethod);
                ++i;
            }

            result = true;

            log.info(TARGET_SYSTEM + "からのBmcメソッドデータの読込終了。" + i + " 件のロード。");

        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのBmcメソッドデータの読込失敗終了。");
        }

        return result;
    }

    /**
     * RMSからの「BMC、BMCメソッド、Entityの関連」データの読出
     */
    public boolean loadParenthoodBmcAndMethodAndEntity() {
        boolean result = false;
        try {
            log.info(TARGET_SYSTEM + "からのBMC、BMCメソッド、Entityの関連のデータの読込を開始。");

            int i = 0;
            ArrayList<LinkedParenthoods> relationList =
                new ArrayList<LinkedParenthoods>();

            // 第一段階、関連を読み込む
            for (int exeSwitch = 0; exeSwitch < 3; exeSwitch++) {
                ResultSet rs = null;
                switch (exeSwitch) {
                    case 0:
                        // SQL発行。11 to 12＝BMCからBMCメソッドの関連
                        rs = getResultSetParenthood(11, 12);
                        break;
                    case 1:
                        // SQL発行。両方12＝BMCメソッドからBMCメソッドの関連
                        rs = getResultSetParenthood(12, 12);
                        break;
                    case 2:
                        // SQL発行。12 to 61＝BMCメソッドからEntityの関連
                        rs = getResultSetParenthood(12, 61);
                        break;
                }

                // 全件まわす。
                while (rs.next()) {
                    // 要素作成
                    LinkedParenthoods relation = new LinkedParenthoods();

                    relation.setResourceIdParent(rs
                        .getInt("resource_id_parent"));
                    relation.setResourceIdChild(rs.getInt("resource_id_child"));

                    relationList.add(relation);

                    relation = null;
                    i++;
                }
            }

            // 第2段階、読み込んだ関連からツリーを作成
            allParenthoods = new LinkedParenthoods();
            allParenthoods.setResourceIdParent(9999);

            // 後ろから回す
            for (int j = relationList.size() - 1; j >= 0; j--) {
                LinkedParenthoods currentNode = relationList.get(j);
                boolean isFound = false;

                // 自分をリストから消す
                relationList.remove(j);

                // 今度は、前から最後の一つ前まで
                for (LinkedParenthoods hitTest : relationList) {
                    // 子IDが自分の親IDなら
                    if (hitTest.getResourceIdChild() == currentNode
                        .getResourceIdParent()) {
                        // 親子の契りを交わす
                        hitTest.addChild(currentNode);
                        isFound = true;
                    }
                }

                // 本ちゃんマップもサーチ
                for (LinkedParenthoods hitTest : allParenthoods.getChildren()
                    .values()) {
                    // 子IDが自分の親IDなら
                    if (hitTest.getResourceIdChild() == currentNode
                        .getResourceIdParent()) {
                        // 親子の契りを交わす
                        hitTest.addChild(currentNode);
                        isFound = true;
                    }
                }

                // 最後までサーチして、親が見つからなければ
                if (!isFound) {
                    // トップの関連として、嘘親をつくり...
                    LinkedParenthoods dummyParent = new LinkedParenthoods();
                    dummyParent.setResourceIdChild(currentNode
                        .getResourceIdParent());
                    dummyParent.addChild(currentNode);
                    // 本ちゃんマップに足す
                    allParenthoods.addChild(dummyParent);
                }

            }

            result = true;
            log.info(TARGET_SYSTEM + "からのBMC、BMCメソッド、Entityの関連のデータの読込終了。" + i
                + " 件のロード。");
        } catch (SQLException e) {
            e.printStackTrace();
            log.info(e.getMessage());
            log.info(TARGET_SYSTEM + "からのBMC、BMCメソッド、Entityの関連のデータの読込失敗終了。");
        }

        return result;
    }

}
