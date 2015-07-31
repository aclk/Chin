package murata.rms.batch.dmrimport.business.container;

import java.sql.Connection;
import java.util.HashMap;

import murata.rms.batch.dmrimport.vo.Bmc;
import murata.rms.batch.dmrimport.vo.BmcMethod;
import murata.rms.batch.dmrimport.vo.Criteria;
import murata.rms.batch.dmrimport.vo.DataField;
import murata.rms.batch.dmrimport.vo.Entity;
import murata.rms.batch.dmrimport.vo.LinkedParenthoods;
import murata.rms.batch.dmrimport.vo.Parenthood;

import org.apache.commons.logging.Log;

/**
 * Entity、データ項目、それらの関連を溜め込む土台。
 * 対象データを読み出し、形の違うデータを各VOの形に変換することを、
 * このクラスの責務とする。
 * @author K.Miura
 * @author H.Hayata
 */
public abstract class ResourceContainer {

    /**
     * Entityのマップ。キーはEntityのリソースID
     */
    protected HashMap<Integer, Entity> entityes =
        new HashMap<Integer, Entity>();

    /**
     * Bmcのマップ。キーはBmcの英名
     */
    protected HashMap<String, Bmc> bmcs = new HashMap<String, Bmc>();

    /**
     * Bmcメソッドのマップ。キーはBmcの英名
     */
    protected HashMap<Integer, BmcMethod> bmcMethods = new HashMap<Integer, BmcMethod>();

    /**
     * データ項目のマップ。キーはデータ項目の和名
     */
    protected HashMap<String, DataField> dataFields =
        new HashMap<String, DataField>();

    /**
     * Entity同士の関連マップ。キーはEntityの和名＋タブ文字＋Entityの和名
     */
    protected HashMap<String, Parenthood> parenthoodE2Es =
        new HashMap<String, Parenthood>();

    /**
     * Entityとデータ項目の関連マップ。キーはEntityの和名＋タブ文字＋データ項目の和名
     */
    protected HashMap<String, Parenthood> parenthoodE2DFd =
        new HashMap<String, Parenthood>();

    /**
     * Criteriaのマップ。キーはリソース名
     */
    protected HashMap<String, Criteria> criterias =
        new HashMap<String, Criteria>();

    /**
     * CriteriaとEntityの関連マップ。キーはCriteriaの和名＋タブ文字＋Entityの和名
     */
    protected HashMap<String, Parenthood> parenthoodCri2Ent =
        new HashMap<String, Parenthood>();

    protected LinkedParenthoods allParenthoods = new LinkedParenthoods();

    protected Connection con = null;

    protected Log log = null;

    /**
     * コネクションから、自身のプロパティとなる4種のリストへデータを読み込む
     */
    public boolean loadAll() {

        // エンティティの読み出し
        if (!loadEntity()) return false;

        // データ項目の読み出し
        if (!loadDataField()) return false;

        // EntityとEntityの関連を読み出し。
        if (!loadParenthoodE2E()) return false;

        // Entityとデータ項目の関連を読み出し。
        if (!loadParenthoodE2DFd()) return false;

        // クライテリアを読み出し。
        if (!loadCriterias()) return false;

        return true;
    }

    protected abstract boolean loadParenthoodE2E();

    protected abstract boolean loadDataField();

    protected abstract boolean loadEntity();

    protected abstract boolean loadParenthoodE2DFd();

    protected abstract boolean loadCriterias();

    public HashMap<String, DataField> getDataFields() {
        return dataFields;
    }

    public HashMap<Integer, Entity> getEntityes() {
        return entityes;
    }

    public HashMap<String, Parenthood> getParenthoodE2Dfs() {
        return parenthoodE2DFd;
    }

    public HashMap<String, Parenthood> getParenthoodE2Es() {
        return parenthoodE2Es;
    }

    public void setConnection(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    /**
     * 自身のEntityコレクションにデータを足す。(キーの確かさを一元化するため)
     * @param ent
     */
    protected void addEntity(Entity ent) {
        // キーはリソースID
        entityes.put(ent.getResourceId(), ent);
    }

    /**
     * 自身のBmcコレクションにデータを足す。(キーの確かさを一元化するため)
     * @param bmc
     */
    protected void addBmc(Bmc bmc) {
        // キーはBMC英名の小文字
        bmcs.put(bmc.getBmcNameEn().toLowerCase(), bmc);
    }

    /**
     * 自身のBmcメソッドコレクションにデータを足す。(キーの確かさを一元化するため)
     * @param ent
     */
    protected void addBmcMethod(BmcMethod bmcMethod) {
        // キーはBMC英名の小文字
        bmcMethods.put(bmcMethod.getResourceId(), bmcMethod);
    }


    /**
     * 自身のEntityコレクションにデータを足す。(キーの確かさを一元化するため)
     * @param ent
     */
    protected void addDataField(DataField dfld) {
        // キーはEntity和名。
        String key = dfld.getDataFieldNameJp();
        dataFields.put(key, dfld);
    }

    /**
     * 自身の関連(EntityからEntityへの)コレクションにデータを足す。(キーの確かさを一元化するため)
     * @param ent
     */
    protected void addParenthoodE2E(Parenthood reration) {
        // キーはEntity和名。
        String key =
            reration.getParentResource().getResourceNameJp() + "\t"
                + reration.getChildResource().getResourceNameJp();
        parenthoodE2Es.put(key, reration);
    }

    /**
     * 自身の関連(Entityからデータフィールドへの)コレクションにデータを足す。(キーの確かさを一元化するため)
     * @param ent
     */
    protected void addParenthoodE2DFd(Parenthood reration) {
        // キーはEntity和名。
        String key =
            reration.getParentResource().getResourceNameJp() + "\t"
                + reration.getChildResource().getResourceNameJp();
        parenthoodE2DFd.put(key, reration);
    }

    /**
     * デバッグ用。ダンプメソッド
     */
    protected void dampParenhood(String systemName) {
        for (Parenthood ph : parenthoodE2DFd.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(systemName);
            sb.append("側:");
            sb.append(ph.getParentResource().getResourceId());
            sb.append(",");
            sb.append(ph.getParentResource().getResourceNameJp());
            sb.append(" - ");
            sb.append(ph.getChildResource().getResourceId());
            sb.append(",");
            sb.append(ph.getChildResource().getResourceNameJp());
            System.out.println(sb.toString());
        }
    }

    public HashMap<String, Criteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(HashMap<String, Criteria> criterias) {
        this.criterias = criterias;
    }


    /**
     * bmcsを戻す。
     * <br>
     * @return  bmcs
     */
    public HashMap<String, Bmc> getBmcs() {
        return bmcs;
    }

    /**
     * bmcsを設定する。
     * <br>
     * @param bmcs HashMap<String,Bmc>
     */
    public void setBmcs(HashMap<String, Bmc> bmcs) {
        this.bmcs = bmcs;
    }


    /**
     * bmcMethodsを戻す。
     * <br>
     * @return  bmcMethods
     */
    public HashMap<Integer, BmcMethod> getBmcMethods() {
        return bmcMethods;
    }


    /**
     * bmcMethodsを設定する。
     * <br>
     * @param bmcMethods HashMap<Integer,BmcMethod>
     */
    public void setBmcMethods(HashMap<Integer, BmcMethod> bmcMethods) {
        this.bmcMethods = bmcMethods;
    }


    /**
     * allParentHoodsを戻す。
     * <br>
     * @return  allParenthoods
     */
    public LinkedParenthoods getAllParenthoods() {
        return allParenthoods;
    }


    /**
     * allParentHoodsを設定する。
     * <br>
     * @param allParenthoods LinkedParenthoods
     */
    public void setAllParenthoods(LinkedParenthoods allParentHoods) {
        this.allParenthoods = allParentHoods;
    }



}
