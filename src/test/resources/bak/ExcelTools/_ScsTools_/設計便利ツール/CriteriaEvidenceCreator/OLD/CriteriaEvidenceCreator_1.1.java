import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import murata.co.jointest.EvidenceGeneratorImpl;
import murata.co.jointest.common.ExcelUtils;
import murata.co.jointest.common.JoinTestConstants;
import murata.co.jointest.database.exp.excel.ExcelSheetCreater;
import murata.co.jointest.database.imp.excel.ExcelSheetReader;
import murata.co.jointest.database.schema.SchemaLoader;
import murata.co.jointest.database.schema.SchemaWriter;
import murata.co.jointest.manager.DataBaseManager;
import murata.co.jointest.manager.JoinTestManager;
import murata.co.jointest.table.TrialInformation;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

/**
 * Criteriaエビデンスまわりを扱うツール。<BR>
 * <PRE>
 * コマンドラインからの操作でエビデンス形式のDBデータを指定スキーマに投入したり、
 * DBデータのダンプ、差分を取得したりします。
 *
 * ※※ 注意 ※※
 * Excelファイルを生成する方（BEFORE、AFTER、TEMPLATE）は成果物（Criteria記述書）を壊す可能性があります。
 * 壊さないまでも、そもそもこのAPIはExcelのオブジェクト（吹き出しやテキストボックスなど）を取り扱えないため、
 * これらは消えてしまいます。
 *
 * 不測の自体を避けるため、DBデータの取得は一旦別のExcelファイル（新規作成したExcelブックでOK）に行う、
 * エビデンス作成時は一旦別に取得したものをCriteria記述書に手で移動する、が無難です。
 *
 *
 * ▼使い方
 * １．正しく動作するプロダクトのsrc/test/java 直下に保存してください。
 *
 *   ※これはこのツールは寄生を前提としています。Mavenから取得するライブラリ（murata-co-test）、
 *     データソース設定なんかに依存しているためです。
 *
 * ２．Eclipseの「実行」からJAVAアプリケーションとして実行します。
 *
 *     メインクラスにはこのpublicクラス名(CriteriaEvidenceCreator)を指定してください。
 *     引数の指定は後述します。次項記載の入力を全て手入力する場合は必要ありません。
 *
 *
 * ３．コマンドラインから入力する内容は以下。順番に入力を問われるので都度入力します。
 *
 *     - スキーマ名：対象スキーマを入力します。（"A1B5MEM37"とか）
 *                   これはapplicationContext-datasource-local.xml の設定と一致しているはずです。
 *
 *     - ファイル名：処理対象としたいExcelファイルをフルパスで入力します。
 *                   Criteria記述書のエビデンスからデータ投入を行う場合はCriteria記述書、
 *                   DBダンプを取得したい場合は保存したいExcelファイルになるかと思います。
 *                   ちなみに、寄生先プロダクト直下であればファイル名のみの指定でOKです、
 *                   フルパスである必要はありません。
 *
 *     - テーブルID：DBダンプ取得したいテーブルのIDをカンマ区切りで入力します。
 *                   データ投入時は入力不要です。そのままEnterでOK。
 *
 *     - 対象シート：このツールはいくつかシートをシート名決め打ちで取り扱いますが、
 *                   そのシートの内の処理前データが記載されているシートの名前を指定します。
 *                   データ投入の元ねた、処理後データ取得時の差分チェックに利用します。
 *                   未入力のままEnterとした場合は初期値「BEFORE」となります。
 *
 *     - 処理タイプ：ツールに何をさせるのかを入力します。以下のコマンドがあります。
 *
 *                   PREPARE - 「投入データ」シートのデータを投入します。
 *                   BEFORE  - 処理前データの取得。DBデータのダンプを行います。（「BEFORE」シートへ出力）
 *                   AFTER   - 処理後データの取得、処理前データとの差分取得を行います。（「AFTER」「DIFF」シートへ出力）
 *                   TEMPLATE- テーブルメタデータの取得を行います。
 *                   RESTORE - 処理前データのデータ投入を行います。
 *
 *                   未入力のままEnterとした場合は初期値「RESTORE」となります。
 *
 *
 * ４．INPUTファイルの指定が可能です。
 *
 *     上記入力項目のうち、「対象シート」「処理タイプ」以外はテキストファイルに記述しておき、
 *     それを利用することで都度入力する手間を省くことができます。
 *     （「対象シート」「処理タイプ」については手入力強制です）
 *
 *     テキストファイルに行毎（記述順は前項の入力順です）に各設定記述を行い保存、
 *     保存したテキストファイルをフルパスで指定してください。
 *     前項記述の「ファイル名」同様、寄生先プロダクト直下であればファイル名のみの指定でOKです。
 *
 * </PRE>
 *
 * <P>2007/12/17 1.0 新規作成。</P>
 * <P>2007/12/20 1.1 PREPAREの実装。シート名初期値変更。</P>
 *
 * @author T.Ueda
 * @version $Id$
 * @since JDK5.0
 */
public class CriteriaEvidenceCreator extends EvidenceGeneratorImpl {

    // 本来定数とするところですが、実行時に切替ができるよう。
    /** 事前データ */
    private static String SHEET_NAME_PREPARE = "投入データ";

    /** 前シート名 */
    private static String SHEET_NAME_BEF = "BEFORE";

    /** 後シート名 */
    private static String SHEET_NAME_AFT = "AFTER";

    /** DIFFシート名 */
    private static String SHEET_NAME_DIF = "DIFF";

    /** エビデンスマネージャ */
    private CriteriaEvidenceManager manager;

    enum Type {
        PREPARE, BEFORE, AFTER, TEMPLATE, RESTORE
    }

    /**
     *
     */
    private CriteriaEvidenceCreator() {
    }

    /**
     * @param manager
     * @param dataSource
     * @return
     */
    public static CriteriaEvidenceCreator createInstance(
        JoinTestManager manager, DataSource dataSource) {

        CriteriaEvidenceCreator sakakibara = new CriteriaEvidenceCreator();
        sakakibara.setManager(sakakibara.new CriteriaEvidenceManager(manager));
        sakakibara.getManager().setDataSource(dataSource);

        return sakakibara;
    }

    public void setManager(CriteriaEvidenceManager manager) {

        this.manager = manager;
    }

    public CriteriaEvidenceManager getManager() {

        return manager;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {

        if (args.length > 1) {
            // 引数の指定があった場合は通常の処理としておく
            EvidenceGeneratorImpl.main(args);
            System.exit(0);
        }

        Scanner scanner = null;
        try {
            if (args.length == 1) {
                // 引数が１つだったらテキストファイルによる指定とみなす
                scanner = new Scanner(new FileInputStream(args[0]));
            } else {

                scanner = new Scanner(System.in);
            }
            // owner
            System.out.println("利用スキーマを入力 >");
            String owner = scanner.nextLine();
            System.out.println(owner);

            // Criteria
            System.out.println("Criteria記述書をフルパスで入力 >");
            String fullFilePath = scanner.nextLine();
            System.out.println(fullFilePath);

            // tableID
            System.out.println("対象テーブルIDをカンマ区切りで入力 >");
            String tableIds = scanner.nextLine();
            System.out.println(tableIds);

            // 一旦終了。以降は手入力強制
            scanner.close();
            scanner = new Scanner(System.in);

            // sheet
            System.out.println("処理対象データのシート名を入力（初期値「" + SHEET_NAME_BEF + "」）>");
            String sheet = scanner.nextLine();
            SHEET_NAME_BEF = "".equals(sheet) ? SHEET_NAME_BEF : sheet;
            System.out.println(SHEET_NAME_BEF);

            // type
            StringBuilder sb = new StringBuilder("処理タイプ[ ");
            for (Type type : Type.values())
                sb.append(type + " ");
            System.out.println(sb.append("]（初期値「" + Type.RESTORE + "」）>")
                .toString());

            String strType = scanner.nextLine();
            Type type = null;
            try {
                type =
                    "".equals(strType) ? Type.RESTORE : Type.valueOf(strType
                        .toUpperCase());
                System.out.println(type);
            } catch (IllegalArgumentException e) {

                System.out.println("そんな処理はできないです。[" + strType + "]");
                System.out.println("終了。");
                System.exit(0);
            }

            scanner.close();

            CriteriaEvidenceCreator sakakibara = generate();
            sakakibara.getManager().setFullFilePath(fullFilePath);
            sakakibara.getManager().setTableIds(tableIds);
            sakakibara.getManager().setOwner(owner);

            switch (type) {

                case PREPARE:

                    sakakibara.getManager().prepare();
                    break;

                case BEFORE:

                    sakakibara.getManager().createBeforeEvidence();
                    break;

                case RESTORE:

                    sakakibara.getManager().restore();
                    break;

                case AFTER:

                    sakakibara.getManager().createAfterEvidence();
                    break;

                case TEMPLATE:

                    sakakibara.getManager().createTemplateFile();
                    break;

                default:

                    System.out.println("未実装です：" + type);
                    break;
            }

            System.out.println("完了。");
            System.exit(0);
        } catch (Exception e) {

            e.printStackTrace();
            System.exit(0);
        } finally {

            if (scanner != null) scanner.close();
        }
    }

    private static CriteriaEvidenceCreator generate() throws Exception {

        BeanFactoryLocator locator =
            ContextSingletonBeanFactoryLocator
                .getInstance("springconf/testBeanRefContext.xml");
        BeanFactoryReference beanFactoryReference =
            locator.useBeanFactory("context");
        ApplicationContext cxt =
            (ApplicationContext)beanFactoryReference.getFactory();
        JoinTestManager manager = (JoinTestManager)cxt.getBean("dbManager");
        DataSource dataSource = (DataSource)cxt.getBean("dataSource");

        return CriteriaEvidenceCreator.createInstance(manager, dataSource);
    }

    class CriteriaEvidenceManager extends DataBaseManager {

        protected String fileName;

        protected String fullFilePath;

        CriteriaEvidenceManager(JoinTestManager manager) {

            this.dataSource = null;
            this.owner = null;
            this.tableIds = null;
            this.whereQLs = null;
            this.template = manager.getTemplate();
            this.inputPath = manager.getInputPath();
            this.outputPath = manager.getOutputPath();
            this.productId = manager.getProductId();
            this.testCaseId = manager.getTestCaseId();
            this.number = manager.getNumber();
            this.capture = manager.isCapture();
        }

        public List<String> prepare() throws Exception {

            TrialInformation before =
                new ExcelImporter().doImport(this.getFullFilePath(), SHEET_NAME_PREPARE);

            return SchemaWriter.invoke(this.getDataSource(), before, this
                .getOwner());
        }

        public String createBeforeEvidence() throws Exception {

            // Excelに出力
            createWorkBook(getCurrentTrialInfomation(), null);

            return JoinTestConstants.COMPLETE;
        }

        public String createAfterEvidence() throws Exception {

            TrialInformation before =
                new ExcelImporter().doImport(this.getFullFilePath(), SHEET_NAME_BEF);

            // 事前データからテーブルIDをとる
            String tableIds = this.getTableIdsFromBeforeSheet();
            if (tableIds != null) this.setTableIds(tableIds);

            // 事後データの取得
            TrialInformation afterInfo = getCurrentTrialInfomation();

            return checkDifference(before, afterInfo);
        }

        public List<String> restore() throws Exception {

            TrialInformation before =
                new ExcelImporter().doImport(this.getFullFilePath(), SHEET_NAME_BEF);

            return SchemaWriter.invoke(this.getDataSource(), before, this
                .getOwner());
        }

        public String createTemplateFile() throws Exception {

            // 事前データからテーブルIDがとれたらとりたい
            String tableIds = this.getTableIdsFromBeforeSheet();
            if (tableIds != null) this.setTableIds(tableIds);

            createWorkBook(null, SchemaLoader.invokeNoData(
                this.getDataSource(), this.getOwner(), this.getTableIds(), this
                    .getWhereQLs()));

            return JoinTestConstants.COMPLETE;
        }

        private String checkDifference(TrialInformation before,
            TrialInformation after) throws Exception {

            // Excelに出力
            createWorkBook(before, after);

            return JoinTestConstants.COMPLETE;
        }

        private TrialInformation getCurrentTrialInfomation() throws Exception {

            // 指定された条件に沿ったデータを取得する
            return SchemaLoader.invoke(this.getDataSource(), this.getOwner(),
                this.getTableIds(), this.getWhereQLs());

        }

        /*
         * Excelを生成する。
         *
         * @param before 処理前データ
         * @param after  処理後データ
         * @throws Exception
         */
        private void createWorkBook(TrialInformation before,
            TrialInformation after) throws Exception {

            File input = new File(this.getFullFilePath());
            if (!input.exists()) {
                throw new Exception("エビデンスファイルが見つかりません");
            }

            File copy = new File(this.getFullFilePath() + "_original.xls");
            //// 終了時に移送元を消す
            //copy.deleteOnExit();
            input.renameTo(copy);

            // ファイル指定（ファイル名のみならプロダクト直下にあります）
            if (!"".equals(this.getOutputPath()))
                checkDirectory(this.getOutputPath());

            File output = new File(this.getFullFilePath());
            FileOutputStream out = null;

            // Excel出力
            try {
                out = new FileOutputStream(output);
                new ExcelExporter().doExport(out, copy, before, after);

                out.flush();
            } finally {

                if (out != null) out.close();
            }
        }

        /*
         * 処理前シートを走査して記載されているテーブルIDをカンマ区切りで取得。
         * 三浦1号に言われてつくったものの、今のところ利用価値なし。
         *
         */
        private String getTableIdsFromBeforeSheet() throws Exception {

            StringBuilder sb = new StringBuilder();
            Workbook wb = null;
            try {

                wb = Workbook.getWorkbook(new File(this.getFullFilePath()));
                Sheet sheet = wb.getSheet(SHEET_NAME_BEF);
                if (sheet == null) return null;

                for (int row = 0; !ExcelUtils.END_LINE.equals(sheet.getCell(0,
                    row).getContents()); row++) {

                    if (ExcelUtils.TABLE_LINE.equals(sheet.getCell(0, row)
                        .getContents())) {
                        sb.append(sheet.getCell(1, row).getContents()).append(
                            ",");
                    }
                }

                System.out.println(sb.toString());
                return sb.toString();
            } finally {

                if (wb != null) wb.close();
            }
        }

        /**
         * captureを戻す。
         * <br>
         * @return  capture
         */
        public boolean isCapture() {
            return capture;
        }

        /**
         * captureを設定する。
         * <br>
         * @param capture boolean
         */
        public void setCapture(boolean capture) {
            this.capture = capture;
        }

        /**
         * dataSourceを戻す。
         * <br>
         * @return  dataSource
         */
        public DataSource getDataSource() {
            return this.dataSource;
        }

        /**
         * dataSourceを設定する。
         * <br>
         * @param dataSource DataSource
         */
        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        /**
         * inputPathを戻す。
         * <br>
         * @return  inputPath
         */
        public String getInputPath() {
            return inputPath;
        }

        /**
         * inputPathを設定する。
         * <br>
         * @param inputPath String
         */
        public void setInputPath(String inputPath) {
            this.inputPath = inputPath;
        }

        /**
         * numberを戻す。
         * <br>
         * @return  number
         */
        public String getNumber() {
            return number;
        }

        /**
         * numberを設定する。
         * <br>
         * @param number String
         */
        public void setNumber(String number) {
            this.number = number;
        }

        /**
         * outputPathを戻す。
         * <br>
         * @return  outputPath
         */
        public String getOutputPath() {
            return outputPath;
        }

        /**
         * outputPathを設定する。
         * <br>
         * @param outputPath String
         */
        public void setOutputPath(String outputPath) {
            this.outputPath = outputPath;
        }

        /**
         * ownerを戻す。
         * <br>
         * @return  owner
         */
        public String getOwner() {
            return owner;
        }

        /**
         * ownerを設定する。
         * <br>
         * @param owner String
         */
        public void setOwner(String owner) {
            this.owner = owner;
        }

        /**
         * productIdを戻す。
         * <br>
         * @return  productId
         */
        public String getProductId() {
            return productId;
        }

        /**
         * productIdを設定する。
         * <br>
         * @param productId String
         */
        public void setProductId(String productId) {
            this.productId = productId;
        }

        /**
         * tableIdsを戻す。
         * <br>
         * @return  tableIds
         */
        public String getTableIds() {
            return tableIds;
        }

        /**
         * tableIdsを設定する。
         * <br>
         * @param tableIds String
         */
        public void setTableIds(String tableIds) {
            this.tableIds = tableIds;
        }

        /**
         * templateを戻す。
         * <br>
         * @return  template
         */
        public String getTemplate() {
            return template;
        }

        /**
         * templateを設定する。
         * <br>
         * @param template String
         */
        public void setTemplate(String template) {
            this.template = template;
        }

        /**
         * testCaseIdを戻す。
         * <br>
         * @return  testCaseId
         */
        public String getTestCaseId() {
            return testCaseId;
        }

        /**
         * testCaseIdを設定する。
         * <br>
         * @param testCaseId String
         */
        public void setTestCaseId(String testCaseId) {
            this.testCaseId = testCaseId;
        }

        /**
         * whereQLsを戻す。
         * <br>
         * @return  whereQLs
         */
        public String getWhereQLs() {
            return whereQLs;
        }

        /**
         * whereQLsを設定する。
         * <br>
         * @param whereQLs String
         */
        public void setWhereQLs(String whereQLs) {
            this.whereQLs = whereQLs;
        }

        /**
         * fileNameを戻す。
         * <br>
         * @return  fileName
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * fileNameを設定する。
         * <br>
         * @param fileName String
         */
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        /**
         * fullFilePathを戻す。
         * <br>
         * @return  fullFilePath
         */
        public String getFullFilePath() {
            return fullFilePath;
        }

        /**
         * fullFilePathを設定する。
         * <br>
         * @param fullFilePath String
         */
        public void setFullFilePath(String fullFilePath) {
            this.fullFilePath = fullFilePath;

            int pos = fullFilePath.lastIndexOf(File.separator);
            if (-1 < pos) {

                this.setOutputPath(fullFilePath.substring(0, pos));
                this.setFileName(fullFilePath.substring(pos + 1));
            } else {
                // フルパスでないならプロジェクト直下にあるものとしたい
                this.setOutputPath("");
                this.setFileName(fullFilePath);
            }
        }
    }

    class ExcelImporter {

        ExcelImporter() {
        }

        public TrialInformation doImport(String fullFilePath, String sheetName)
            throws Exception {

            Workbook book = null;

            try {
                File file = new File(fullFilePath);

                if (file != null && file.exists()) {
                    book = Workbook.getWorkbook(file);

                    // 前シートの存在チェック
                    Sheet beforeSheet = book.getSheet(sheetName);
                    TrialInformation before =
                        ExcelSheetReader.readResultSheet(beforeSheet, manager
                            .getOutputPath(), new ArrayList());

                    if (before == null)
                        throw new Exception("そんなシートないです。[" + sheetName + "]");

                    return before;
                } else {

                    throw new Exception("そんなファイルないです。[" + fullFilePath + "]");
                }
            } finally {

                if (book != null) book.close();
            }
        }
    }

    class ExcelExporter {

        ExcelExporter() {
        }

        /**
         *
         * before <> null & after <> null →  処理後シート、差分シート出力
         * before <> null & after == null →  処理前シート出力
         * before == null & after <> null →  事前シート出力
         * before == null & after == null →  どのシートも出力されない
         *
         * @param os
         * @param input
         * @param before
         * @param after
         * @throws Exception
         */
        public void doExport(OutputStream os, File input,
            TrialInformation before, TrialInformation after) throws Exception {

            Workbook tmp = null;
            WritableWorkbook workbook = null;
            try {

                // 設定情報
                WorkbookSettings ws = ExcelUtils.createWorkbookSettings();

                // インプットファイルを引数に変更
                tmp = Workbook.getWorkbook(input);
                workbook = Workbook.createWorkbook(os, tmp, ws);
                tmp.close();

                // BEFOREデータの書き込み
                if (before != null) {

                    if (after != null) {

                        // AFTERデータの書き込み
                        ExcelSheetCreater.createDBExcel(getSheet(workbook,
                            SHEET_NAME_AFT), after);

                        // 差分出力
                        ExcelSheetCreater.createDiffDB(getSheet(workbook,
                            SHEET_NAME_DIF), before, after);
                    } else {

                        ExcelSheetCreater.createDBExcel(getSheet(workbook,
                            SHEET_NAME_BEF), before);
                    }
                } else {

                    if (after != null) {

                        // AFTERデータの書き込み
                        ExcelSheetCreater.createDBExcel(getSheet(workbook,
                            SHEET_NAME_PREPARE), after);
                    }
                }

                workbook.write();
            } finally {

                if (tmp != null) tmp.close();
                if (workbook != null) workbook.close();
            }
        }

        /*
         * 指定のシートがあったら一回消して作成。
         * なかったら普通に作成。
         */
        private WritableSheet getSheet(WritableWorkbook workbook,
            String sheetName) {

            WritableSheet sheet = workbook.getSheet(sheetName);
            if (sheet != null) {
                int idx = 0;
                for (; idx < workbook.getSheets().length; idx++) {
                    if (sheetName.equals(workbook.getSheet(idx).getName()))
                        break;
                }
                workbook.removeSheet(idx);
            }

            return workbook
                .createSheet(sheetName, workbook.getNumberOfSheets());
        }
    }

}
