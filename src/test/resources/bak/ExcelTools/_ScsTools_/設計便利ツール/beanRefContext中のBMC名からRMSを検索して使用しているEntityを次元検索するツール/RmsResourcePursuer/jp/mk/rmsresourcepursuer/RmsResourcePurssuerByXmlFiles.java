/**
 *
 */
package jp.mk.rmsresourcepursuer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import murata.rms.batch.dmrimport.business.container.RmsResourceContainer;
import murata.rms.batch.dmrimport.vo.Bmc;
import murata.rms.batch.dmrimport.vo.BmcMethod;
import murata.rms.batch.dmrimport.vo.Entity;
import murata.rms.batch.dmrimport.vo.LinkedParenthoods;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author K.Miura
 * @version $Id$
 * @since JDK5.0
 */
public class RmsResourcePurssuerByXmlFiles extends SqlExecuter {

    /**
     * 対象となるSQLファイルがあるフォルダ
     */
    private String sqlFilesDirectory = "";

    /**
     * @param args
     */
    public static void main(String[] arg) {
        RmsResourcePurssuerByXmlFiles executer =
            new RmsResourcePurssuerByXmlFiles();
        executer.setLog(LogFactory.getLog(executer.getClass()));
        executer.readSettingResource();
        executer.execute();
    }

    /**
     * sqlFilesDirectoryを戻す。
     * <br>
     * @return  sqlFilesDirectory
     */
    public String getSqlFilesDirectory() {
        return sqlFilesDirectory;
    }

    /**
     * sqlFilesDirectoryを設定する。
     * <br>
     * @param sqlFilesDirectory String
     */
    public void setSqlFilesDirectory(String sqlFilesDirectory) {
        this.sqlFilesDirectory = sqlFilesDirectory;
    }

    @Override
    protected void executeSql(Connection con, BufferedWriter resultWriter)
        throws Exception {

        File dir = new File(sqlFilesDirectory);

        writeLogAndResult(log, resultWriter, "解析開始");

        // RMS情報の読み込み
        RmsResourceContainer rmsConainer = new RmsResourceContainer();
        rmsConainer.setConnection(con);
        rmsConainer.setLog(log);
        if (!loadRmsInfo(rmsConainer)) {
            return;
        }

        // フォルダにあるすべてのxmlを読み込む
        for (File xmlFile : dir.listFiles()) {
            // 解析＆結果出力
            if (xmlFile.isFile()) {
                purssuering(log, resultWriter, xmlFile, rmsConainer);
            }
        }
        writeLogAndResult(log, resultWriter, "解析終了");

    }

    public void purssuering(Log log, BufferedWriter resultWriter, File xmlFile,
        RmsResourceContainer rmsConainer) throws ParserConfigurationException,
        FileNotFoundException, SAXException, IOException {

        // XMLをパース
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new FileInputStream(xmlFile));

        // XMLから、BMC名の一覧取得
        ArrayList<String> beanIds = new ArrayList<String>();
        getBeanList(doc, beanIds);
        // 取得できなければ、処理を抜ける
        if (beanIds.size() == 0) return;

        StringBuilder sb = new StringBuilder();

        sb.append("ファイル名:");
        sb.append(xmlFile.getName());
        sb.append("\r\n");

        // BMCごとループ
        for (String beanId : beanIds) {

            // BMC名を検索
            String key = beanId.toLowerCase();
            Bmc bmc = rmsConainer.getBmcs().get(key);

            if (bmc != null) {

                sb.append("\tBMC:");
                sb.append(bmc.getBmcNameJp());
                sb.append("\t");
                sb.append(bmc.getBmcNameEn());
                sb.append("\r\n");

                // BMCが見つかったら、今度はBMCが子リソースIDとなる関連を探す
                LinkedParenthoods parenthood =
                    rmsConainer.getAllParenthoods().getChildParenthoods(
                        bmc.getResourceId());

                if (this.expandMethod) {
                    // 「メソッドを展開」モードなら

                    // その子を回して、BMCメソッドかどうかを見る
                    for (LinkedParenthoods child : parenthood.getChildren()
                        .values()) {

                        BmcMethod bmcMethod =
                            rmsConainer.getBmcMethods().get(
                                child.getResourceIdChild());

                        if (bmcMethod != null) {

                            sb.append("\t\tBmcMethod:");
                            sb.append(bmcMethod.getBmcMethodNameJp());
                            sb.append("\t");
                            sb.append(bmcMethod.getBmcMethodNameEn());
                            sb.append("\r\n");

                            // メソッドからつながる、関連上の末端リソース群を取得
                            HashMap<Integer, LinkedParenthoods> endParenthoods =
                                child.getEndParenthoods();

                            // 末端リソースを、Entityに当てながら回す
                            for (LinkedParenthoods curParenthood : endParenthoods
                                .values()) {
                                Entity entity =
                                    rmsConainer.getEntityes().get(
                                        curParenthood.getResourceIdChild());
                                // あれば(Entity以外にあたる場合は無い場合あり)
                                if (entity != null) {
                                    sb.append("\t\t\tEntity:");
                                    sb.append(entity.getEntityNameJp());
                                    sb.append("\t");
                                    sb.append(entity.getEntityNameEn());
                                    sb.append("\r\n");
                                }
                            }
                        }
                    }
                } else {
                    // 「メソッドを展開」モードでないなら

                    // BMCからつながる、関連上の末端リソース群を取得
                    HashMap<Integer, LinkedParenthoods> endParenthoods =
                        parenthood.getEndParenthoods();

                    // 末端リソースを、Entityに当てながら回す
                    for (LinkedParenthoods curParenthood : endParenthoods
                        .values()) {
                        Entity entity =
                            rmsConainer.getEntityes().get(
                                curParenthood.getResourceIdChild());
                        // あれば(Entity以外にあたる場合は無い場合あり)
                        if (entity != null) {
                            sb.append("\t\tEntity:");
                            sb.append(entity.getEntityNameJp());
                            sb.append("\t");
                            sb.append(entity.getEntityNameEn());
                            sb.append("\r\n");
                        }
                    }

                }
            }
        }

        writeLogAndResult(log, resultWriter, sb.toString());

    }

    /**
     * methodCacher.xmlから、対象BMCのBeanIdの一覧を取得
     * @param node
     * @param beanIds
     */
    private void getBeanList(Node node, ArrayList<String> beanIds) {
        if (!(node.getNodeType() == Node.TEXT_NODE && node.getNodeValue()
            .trim().length() == 0)) {
            for (Node child = node.getFirstChild(); child != null; child =
                child.getNextSibling()) {

                if (child.getNodeName().equals("list")) {
                    // listアトリビュートが出てきたら、もうそれを信じる
                    for (Node valueItem = child.getFirstChild(); valueItem != null; valueItem =
                        valueItem.getNextSibling()) {
                        if (valueItem.getNodeType() == Node.ELEMENT_NODE
                            && valueItem.getNodeName().equals("value")) {
                            beanIds.add(valueItem.getTextContent());
                        }
                    }
                } else {
                    // 有ったら探しに行く
                    getBeanList(child, beanIds);
                }
            }
        }
    }

    private boolean loadRmsInfo(RmsResourceContainer rmsConainer) {
        // エンティティの読み出し
        if (!rmsConainer.loadEntity()) return false;

        // BMC読み出し
        if (!rmsConainer.loadBmc()) return false;

        // BMCメソッド読み出し
        if (!rmsConainer.loadBmcMethod()) return false;

        // 関連ツリー読み出し
        if (!rmsConainer.loadParenthoodBmcAndMethodAndEntity()) return false;

        return true;
    }

    private void writeLogAndResult(Log log, BufferedWriter bw, String comment)
        throws IOException {
        bw.write(comment + "\n");
        if (log != null) log.info(comment);
    }
}
