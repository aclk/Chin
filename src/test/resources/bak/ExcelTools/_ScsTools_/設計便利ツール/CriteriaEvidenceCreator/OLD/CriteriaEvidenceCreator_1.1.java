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
 * Criteria�G�r�f���X�܂��������c�[���B<BR>
 * <PRE>
 * �R�}���h���C������̑���ŃG�r�f���X�`����DB�f�[�^���w��X�L�[�}�ɓ���������A
 * DB�f�[�^�̃_���v�A�������擾�����肵�܂��B
 *
 * ���� ���� ����
 * Excel�t�@�C���𐶐�������iBEFORE�AAFTER�ATEMPLATE�j�͐��ʕ��iCriteria�L�q���j���󂷉\��������܂��B
 * �󂳂Ȃ��܂ł��A������������API��Excel�̃I�u�W�F�N�g�i�����o����e�L�X�g�{�b�N�X�Ȃǁj����舵���Ȃ����߁A
 * �����͏����Ă��܂��܂��B
 *
 * �s���̎��̂�����邽�߁ADB�f�[�^�̎擾�͈�U�ʂ�Excel�t�@�C���i�V�K�쐬����Excel�u�b�N��OK�j�ɍs���A
 * �G�r�f���X�쐬���͈�U�ʂɎ擾�������̂�Criteria�L�q���Ɏ�ňړ�����A������ł��B
 *
 *
 * ���g����
 * �P�D���������삷��v���_�N�g��src/test/java �����ɕۑ����Ă��������B
 *
 *   ������͂��̃c�[���͊񐶂�O��Ƃ��Ă��܂��BMaven����擾���郉�C�u�����imurata-co-test�j�A
 *     �f�[�^�\�[�X�ݒ�Ȃ񂩂Ɉˑ����Ă��邽�߂ł��B
 *
 * �Q�DEclipse�́u���s�v����JAVA�A�v���P�[�V�����Ƃ��Ď��s���܂��B
 *
 *     ���C���N���X�ɂ͂���public�N���X��(CriteriaEvidenceCreator)���w�肵�Ă��������B
 *     �����̎w��͌�q���܂��B�����L�ڂ̓��͂�S�Ď���͂���ꍇ�͕K�v����܂���B
 *
 *
 * �R�D�R�}���h���C��������͂�����e�͈ȉ��B���Ԃɓ��͂�����̂œs�x���͂��܂��B
 *
 *     - �X�L�[�}���F�ΏۃX�L�[�}����͂��܂��B�i"A1B5MEM37"�Ƃ��j
 *                   �����applicationContext-datasource-local.xml �̐ݒ�ƈ�v���Ă���͂��ł��B
 *
 *     - �t�@�C�����F�����ΏۂƂ�����Excel�t�@�C�����t���p�X�œ��͂��܂��B
 *                   Criteria�L�q���̃G�r�f���X����f�[�^�������s���ꍇ��Criteria�L�q���A
 *                   DB�_���v���擾�������ꍇ�͕ۑ�������Excel�t�@�C���ɂȂ邩�Ǝv���܂��B
 *                   ���Ȃ݂ɁA�񐶐�v���_�N�g�����ł���΃t�@�C�����݂̂̎w���OK�ł��A
 *                   �t���p�X�ł���K�v�͂���܂���B
 *
 *     - �e�[�u��ID�FDB�_���v�擾�������e�[�u����ID���J���}��؂�œ��͂��܂��B
 *                   �f�[�^�������͓��͕s�v�ł��B���̂܂�Enter��OK�B
 *
 *     - �ΏۃV�[�g�F���̃c�[���͂������V�[�g���V�[�g�����ߑł��Ŏ�舵���܂����A
 *                   ���̃V�[�g�̓��̏����O�f�[�^���L�ڂ���Ă���V�[�g�̖��O���w�肵�܂��B
 *                   �f�[�^�����̌��˂��A������f�[�^�擾���̍����`�F�b�N�ɗ��p���܂��B
 *                   �����͂̂܂�Enter�Ƃ����ꍇ�͏����l�uBEFORE�v�ƂȂ�܂��B
 *
 *     - �����^�C�v�F�c�[���ɉ���������̂�����͂��܂��B�ȉ��̃R�}���h������܂��B
 *
 *                   PREPARE - �u�����f�[�^�v�V�[�g�̃f�[�^�𓊓����܂��B
 *                   BEFORE  - �����O�f�[�^�̎擾�BDB�f�[�^�̃_���v���s���܂��B�i�uBEFORE�v�V�[�g�֏o�́j
 *                   AFTER   - ������f�[�^�̎擾�A�����O�f�[�^�Ƃ̍����擾���s���܂��B�i�uAFTER�v�uDIFF�v�V�[�g�֏o�́j
 *                   TEMPLATE- �e�[�u�����^�f�[�^�̎擾���s���܂��B
 *                   RESTORE - �����O�f�[�^�̃f�[�^�������s���܂��B
 *
 *                   �����͂̂܂�Enter�Ƃ����ꍇ�͏����l�uRESTORE�v�ƂȂ�܂��B
 *
 *
 * �S�DINPUT�t�@�C���̎w�肪�\�ł��B
 *
 *     ��L���͍��ڂ̂����A�u�ΏۃV�[�g�v�u�����^�C�v�v�ȊO�̓e�L�X�g�t�@�C���ɋL�q���Ă����A
 *     ����𗘗p���邱�Ƃœs�x���͂����Ԃ��Ȃ����Ƃ��ł��܂��B
 *     �i�u�ΏۃV�[�g�v�u�����^�C�v�v�ɂ��Ă͎���͋����ł��j
 *
 *     �e�L�X�g�t�@�C���ɍs���i�L�q���͑O���̓��͏��ł��j�Ɋe�ݒ�L�q���s���ۑ��A
 *     �ۑ������e�L�X�g�t�@�C�����t���p�X�Ŏw�肵�Ă��������B
 *     �O���L�q�́u�t�@�C�����v���l�A�񐶐�v���_�N�g�����ł���΃t�@�C�����݂̂̎w���OK�ł��B
 *
 * </PRE>
 *
 * <P>2007/12/17 1.0 �V�K�쐬�B</P>
 * <P>2007/12/20 1.1 PREPARE�̎����B�V�[�g�������l�ύX�B</P>
 *
 * @author T.Ueda
 * @version $Id$
 * @since JDK5.0
 */
public class CriteriaEvidenceCreator extends EvidenceGeneratorImpl {

    // �{���萔�Ƃ���Ƃ���ł����A���s���ɐؑւ��ł���悤�B
    /** ���O�f�[�^ */
    private static String SHEET_NAME_PREPARE = "�����f�[�^";

    /** �O�V�[�g�� */
    private static String SHEET_NAME_BEF = "BEFORE";

    /** ��V�[�g�� */
    private static String SHEET_NAME_AFT = "AFTER";

    /** DIFF�V�[�g�� */
    private static String SHEET_NAME_DIF = "DIFF";

    /** �G�r�f���X�}�l�[�W�� */
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
            // �����̎w�肪�������ꍇ�͒ʏ�̏����Ƃ��Ă���
            EvidenceGeneratorImpl.main(args);
            System.exit(0);
        }

        Scanner scanner = null;
        try {
            if (args.length == 1) {
                // �������P��������e�L�X�g�t�@�C���ɂ��w��Ƃ݂Ȃ�
                scanner = new Scanner(new FileInputStream(args[0]));
            } else {

                scanner = new Scanner(System.in);
            }
            // owner
            System.out.println("���p�X�L�[�}����� >");
            String owner = scanner.nextLine();
            System.out.println(owner);

            // Criteria
            System.out.println("Criteria�L�q�����t���p�X�œ��� >");
            String fullFilePath = scanner.nextLine();
            System.out.println(fullFilePath);

            // tableID
            System.out.println("�Ώۃe�[�u��ID���J���}��؂�œ��� >");
            String tableIds = scanner.nextLine();
            System.out.println(tableIds);

            // ��U�I���B�ȍ~�͎���͋���
            scanner.close();
            scanner = new Scanner(System.in);

            // sheet
            System.out.println("�����Ώۃf�[�^�̃V�[�g������́i�����l�u" + SHEET_NAME_BEF + "�v�j>");
            String sheet = scanner.nextLine();
            SHEET_NAME_BEF = "".equals(sheet) ? SHEET_NAME_BEF : sheet;
            System.out.println(SHEET_NAME_BEF);

            // type
            StringBuilder sb = new StringBuilder("�����^�C�v[ ");
            for (Type type : Type.values())
                sb.append(type + " ");
            System.out.println(sb.append("]�i�����l�u" + Type.RESTORE + "�v�j>")
                .toString());

            String strType = scanner.nextLine();
            Type type = null;
            try {
                type =
                    "".equals(strType) ? Type.RESTORE : Type.valueOf(strType
                        .toUpperCase());
                System.out.println(type);
            } catch (IllegalArgumentException e) {

                System.out.println("����ȏ����͂ł��Ȃ��ł��B[" + strType + "]");
                System.out.println("�I���B");
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

                    System.out.println("�������ł��F" + type);
                    break;
            }

            System.out.println("�����B");
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

            // Excel�ɏo��
            createWorkBook(getCurrentTrialInfomation(), null);

            return JoinTestConstants.COMPLETE;
        }

        public String createAfterEvidence() throws Exception {

            TrialInformation before =
                new ExcelImporter().doImport(this.getFullFilePath(), SHEET_NAME_BEF);

            // ���O�f�[�^����e�[�u��ID���Ƃ�
            String tableIds = this.getTableIdsFromBeforeSheet();
            if (tableIds != null) this.setTableIds(tableIds);

            // ����f�[�^�̎擾
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

            // ���O�f�[�^����e�[�u��ID���Ƃꂽ��Ƃ肽��
            String tableIds = this.getTableIdsFromBeforeSheet();
            if (tableIds != null) this.setTableIds(tableIds);

            createWorkBook(null, SchemaLoader.invokeNoData(
                this.getDataSource(), this.getOwner(), this.getTableIds(), this
                    .getWhereQLs()));

            return JoinTestConstants.COMPLETE;
        }

        private String checkDifference(TrialInformation before,
            TrialInformation after) throws Exception {

            // Excel�ɏo��
            createWorkBook(before, after);

            return JoinTestConstants.COMPLETE;
        }

        private TrialInformation getCurrentTrialInfomation() throws Exception {

            // �w�肳�ꂽ�����ɉ������f�[�^���擾����
            return SchemaLoader.invoke(this.getDataSource(), this.getOwner(),
                this.getTableIds(), this.getWhereQLs());

        }

        /*
         * Excel�𐶐�����B
         *
         * @param before �����O�f�[�^
         * @param after  ������f�[�^
         * @throws Exception
         */
        private void createWorkBook(TrialInformation before,
            TrialInformation after) throws Exception {

            File input = new File(this.getFullFilePath());
            if (!input.exists()) {
                throw new Exception("�G�r�f���X�t�@�C����������܂���");
            }

            File copy = new File(this.getFullFilePath() + "_original.xls");
            //// �I�����Ɉڑ���������
            //copy.deleteOnExit();
            input.renameTo(copy);

            // �t�@�C���w��i�t�@�C�����݂̂Ȃ�v���_�N�g�����ɂ���܂��j
            if (!"".equals(this.getOutputPath()))
                checkDirectory(this.getOutputPath());

            File output = new File(this.getFullFilePath());
            FileOutputStream out = null;

            // Excel�o��
            try {
                out = new FileOutputStream(output);
                new ExcelExporter().doExport(out, copy, before, after);

                out.flush();
            } finally {

                if (out != null) out.close();
            }
        }

        /*
         * �����O�V�[�g�𑖍����ċL�ڂ���Ă���e�[�u��ID���J���}��؂�Ŏ擾�B
         * �O�Y1���Ɍ����Ă��������̂́A���̂Ƃ��뗘�p���l�Ȃ��B
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
         * capture��߂��B
         * <br>
         * @return  capture
         */
        public boolean isCapture() {
            return capture;
        }

        /**
         * capture��ݒ肷��B
         * <br>
         * @param capture boolean
         */
        public void setCapture(boolean capture) {
            this.capture = capture;
        }

        /**
         * dataSource��߂��B
         * <br>
         * @return  dataSource
         */
        public DataSource getDataSource() {
            return this.dataSource;
        }

        /**
         * dataSource��ݒ肷��B
         * <br>
         * @param dataSource DataSource
         */
        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        /**
         * inputPath��߂��B
         * <br>
         * @return  inputPath
         */
        public String getInputPath() {
            return inputPath;
        }

        /**
         * inputPath��ݒ肷��B
         * <br>
         * @param inputPath String
         */
        public void setInputPath(String inputPath) {
            this.inputPath = inputPath;
        }

        /**
         * number��߂��B
         * <br>
         * @return  number
         */
        public String getNumber() {
            return number;
        }

        /**
         * number��ݒ肷��B
         * <br>
         * @param number String
         */
        public void setNumber(String number) {
            this.number = number;
        }

        /**
         * outputPath��߂��B
         * <br>
         * @return  outputPath
         */
        public String getOutputPath() {
            return outputPath;
        }

        /**
         * outputPath��ݒ肷��B
         * <br>
         * @param outputPath String
         */
        public void setOutputPath(String outputPath) {
            this.outputPath = outputPath;
        }

        /**
         * owner��߂��B
         * <br>
         * @return  owner
         */
        public String getOwner() {
            return owner;
        }

        /**
         * owner��ݒ肷��B
         * <br>
         * @param owner String
         */
        public void setOwner(String owner) {
            this.owner = owner;
        }

        /**
         * productId��߂��B
         * <br>
         * @return  productId
         */
        public String getProductId() {
            return productId;
        }

        /**
         * productId��ݒ肷��B
         * <br>
         * @param productId String
         */
        public void setProductId(String productId) {
            this.productId = productId;
        }

        /**
         * tableIds��߂��B
         * <br>
         * @return  tableIds
         */
        public String getTableIds() {
            return tableIds;
        }

        /**
         * tableIds��ݒ肷��B
         * <br>
         * @param tableIds String
         */
        public void setTableIds(String tableIds) {
            this.tableIds = tableIds;
        }

        /**
         * template��߂��B
         * <br>
         * @return  template
         */
        public String getTemplate() {
            return template;
        }

        /**
         * template��ݒ肷��B
         * <br>
         * @param template String
         */
        public void setTemplate(String template) {
            this.template = template;
        }

        /**
         * testCaseId��߂��B
         * <br>
         * @return  testCaseId
         */
        public String getTestCaseId() {
            return testCaseId;
        }

        /**
         * testCaseId��ݒ肷��B
         * <br>
         * @param testCaseId String
         */
        public void setTestCaseId(String testCaseId) {
            this.testCaseId = testCaseId;
        }

        /**
         * whereQLs��߂��B
         * <br>
         * @return  whereQLs
         */
        public String getWhereQLs() {
            return whereQLs;
        }

        /**
         * whereQLs��ݒ肷��B
         * <br>
         * @param whereQLs String
         */
        public void setWhereQLs(String whereQLs) {
            this.whereQLs = whereQLs;
        }

        /**
         * fileName��߂��B
         * <br>
         * @return  fileName
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * fileName��ݒ肷��B
         * <br>
         * @param fileName String
         */
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        /**
         * fullFilePath��߂��B
         * <br>
         * @return  fullFilePath
         */
        public String getFullFilePath() {
            return fullFilePath;
        }

        /**
         * fullFilePath��ݒ肷��B
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
                // �t���p�X�łȂ��Ȃ�v���W�F�N�g�����ɂ�����̂Ƃ�����
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

                    // �O�V�[�g�̑��݃`�F�b�N
                    Sheet beforeSheet = book.getSheet(sheetName);
                    TrialInformation before =
                        ExcelSheetReader.readResultSheet(beforeSheet, manager
                            .getOutputPath(), new ArrayList());

                    if (before == null)
                        throw new Exception("����ȃV�[�g�Ȃ��ł��B[" + sheetName + "]");

                    return before;
                } else {

                    throw new Exception("����ȃt�@�C���Ȃ��ł��B[" + fullFilePath + "]");
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
         * before <> null & after <> null ��  ������V�[�g�A�����V�[�g�o��
         * before <> null & after == null ��  �����O�V�[�g�o��
         * before == null & after <> null ��  ���O�V�[�g�o��
         * before == null & after == null ��  �ǂ̃V�[�g���o�͂���Ȃ�
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

                // �ݒ���
                WorkbookSettings ws = ExcelUtils.createWorkbookSettings();

                // �C���v�b�g�t�@�C���������ɕύX
                tmp = Workbook.getWorkbook(input);
                workbook = Workbook.createWorkbook(os, tmp, ws);
                tmp.close();

                // BEFORE�f�[�^�̏�������
                if (before != null) {

                    if (after != null) {

                        // AFTER�f�[�^�̏�������
                        ExcelSheetCreater.createDBExcel(getSheet(workbook,
                            SHEET_NAME_AFT), after);

                        // �����o��
                        ExcelSheetCreater.createDiffDB(getSheet(workbook,
                            SHEET_NAME_DIF), before, after);
                    } else {

                        ExcelSheetCreater.createDBExcel(getSheet(workbook,
                            SHEET_NAME_BEF), before);
                    }
                } else {

                    if (after != null) {

                        // AFTER�f�[�^�̏�������
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
         * �w��̃V�[�g����������������č쐬�B
         * �Ȃ������畁�ʂɍ쐬�B
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
