package ss_scs.rewriter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * �v���W�F�N�g���̏����������s�����Ⴂ
 *
 * @author A1B5MEMO
 */
public class ReWriterXai {

    public static String dbConSettingFilePath =
        "tools/SAITO/applicationContext-datasource-local.xml"; // �f�t�H���g��tools/SAITO�̉�

    /**
     * @param args
     */
    public static void main(String[] args) {
        Options options = new Options();

        Option targetOption =
            OptionBuilder.hasArg(true).withArgName("target").isRequired(true)
                .withDescription("���������Ώۃf�B���N�g�����w��").withLongOpt("target")
                .create("t");
        options.addOption(targetOption);

        Option schemaOption =
            OptionBuilder.hasArg(true).withArgName("schema").isRequired(false)
                .withDescription("�X�L�[�}�����w��").withLongOpt("schema").create("s");
        options.addOption(schemaOption);

        Option typeOption =
            OptionBuilder.hasArg(true).withArgName("type").isRequired(false)
                .withDescription("�e�X�g�̎�ނ��w��").withLongOpt("type").create("p");
        options.addOption(typeOption);

        Option dsPathOption =
            OptionBuilder.hasArg(true).withArgName("dsPath").isRequired(false)
                .withDescription("�f�[�^�\�[�X�t�@�C���p�X���w��").withLongOpt("dsPath")
                .create("d");
        options.addOption(dsPathOption);

        CommandLineParser parser = new PosixParser();
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("rewriter", options, true);
            return;
        }
        String target = commandLine.getOptionValue("t");
        String schemaName = commandLine.getOptionValue("s");
        String testType = commandLine.getOptionValue("p");
        String dsPath = commandLine.getOptionValue("d");
        if (dsPath != null) {
            dbConSettingFilePath = dsPath;
        }

        new ReWriterXai().rewrite(target, schemaName, testType);
        //        new ReWriterXai().rewrite();
    }

    /**
     * @param type
     *
     *
     */
    public void rewrite(String target, String schemaName, String testType) {

        // ���ƂȂ�appilcationContext-datasource.xml���擾����B
        DbConnectSetting dbcSetting = new DbConnectSetting();
        if (!dbcSetting.load(dbConSettingFilePath)) {
            dbcSetting = null;
        }

        // schemaName�������Ɏw�肳��Ă��Ȃ���΁c
        if (schemaName == null || schemaName.trim().length() == 0) {
            // �ݒ�t�@�C������̒l�𐳂Ƃ���
            schemaName = dbcSetting.getUser();
        }

        DirectoryWalker dw = new DirectoryWalker();
        DefaultIgnorePatterns dip;
        // application-context-producttest-xml����������
        dip = new DefaultIgnorePatterns();
        TestxmlRewriteCommand trc = new TestxmlRewriteCommand();
        trc.setSchemaName(schemaName);
        trc.setTestType(testType);
        dw.walkAround(new File(target),
            ".*applicationcontext-product-test.*.xml", dip, trc);

        // *Test.java����������
        dip = new DefaultIgnorePatterns();
        dip.add("main");
        dip.add("resources");
        dip.add("springconf");
        dw.walkAround(new File(target), ".+Test\\.java", dip,
            new TestJavaRewriteCommand());

        // pom.xml����������(�x�[�X���C�������[�X�܂Ŏb��)
        dip = new DefaultIgnorePatterns();
        dip.add("src");
        dw
            .walkAround(new File(target), "pom.xml", dip,
                new PomRewriteCommand());

        // applicationConetxt-datasource.xml(�Ɨގ����閼�O�̃t�@�C���Q)����������
        dip = new DefaultIgnorePatterns();
        dip.add("java");
        dw.walkAround(new File(target),
            ".*applicationContext.*datasource.*\\.xml", dip,
            new DatasourceXmlRewriteCommand(dbcSetting));

        // jdbc.properties(�Ɨގ����閼�O�̃t�@�C���Q)����������
        dip = new DefaultIgnorePatterns();
        dip.add("java");
        dw.walkAround(new File(target), ".*jdbc.*\\.properties", dip,
            new JdbcPropRewriteCommand(dbcSetting));

    }
}

/**
 * application-context-product-test.xml����������
 * @author A1B5MEMO
 */
class TestxmlRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(TestxmlRewriteCommand.class);

    /** �X�L�[�}�� */
    private String schemaName;

    /** �e�X�g�̎��(�󕶎��͖��w��) */
    private String testType;

    /** �X�L�[�}���ݒ� */
    public void setSchemaName(String s) {
        this.schemaName = s;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    /**    */
    public void rewrite(File targetFile) {

        try {

            boolean isModify = false;

            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            reader
                .setFeature(
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);
            Document document = reader.read(targetFile);

            // �X�L�[�}���u��
            Element e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='owner']");

            if (!this.schemaName.equals(e.attribute("value").getValue())) {
                // ��v���Ȃ����͒u������
                e.addAttribute("value", this.schemaName);
                isModify = true;
            }

            // �e�X�g��ޒu��
            if (testType != null && testType.trim().length() > 0) {
                e =
                    (Element)document
                        .selectSingleNode("/beans/bean/property[@name='advice']");

                if (e != null) {
                    if (!this.testType.equals(e.attribute("ref").getValue())) {
                        // ��v���Ȃ����͒u������
                        e.addAttribute("ref", this.testType);
                        isModify = true;
                    }
                }
            }

            // ��2010/07/05 K.Miura Add
            // �v���_�N�gID��啶����
            e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='productId']");
            if (e != null) {
                String productId = e.attribute("value").getValue();
                String productIdUpper = productId.toUpperCase();
                if (!productId.equals(productIdUpper)) {
                    // �啶�������Ă݂āA�قȂ�悤�ł���Βu��
                    e.addAttribute("value", productIdUpper);
                    isModify = true;
                }
            }

            // �ύX���������ꍇ�̂ݏ㏑��
            if (isModify) {
                XMLWriter output =
                    new XMLWriter(new OutputStreamWriter(new FileOutputStream(
                        targetFile), "UTF-8"), new OutputFormat("", false,
                        "UTF-8"));
                output.write(document);
                output.flush();
                output.close();
                log.info("rewrite " + targetFile.getAbsolutePath());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * �e�X�g�N���X�̏�������
 * @author A1B5MEMO
 *
 */
class TestJavaRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(TestJavaRewriteCommand.class);

    /**
     * Java�t�@�C�����̑啶���ɂȂ��Ă���XML��xml�ɂ���
     */
    public void rewrite(File targetFile) {
        try {
            BufferedReader reader =
                new BufferedReader(new InputStreamReader(new FileInputStream(
                    targetFile), "Shift_JIS"));
            StringWriter sw = new StringWriter();
            String line;
            boolean beReplaced = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(".XML\"")) {
                    String replaced = line.replace(".XML", ".xml");
                    sw.append(replaced);
                    sw.append("\r\n");
                    beReplaced = true;
                } else {
                    sw.append(line);
                    sw.append("\r\n");
                }
            }
            if (beReplaced) {
                // �u�����������ꍇ�̂݃t�@�C����������
                BufferedWriter bw =
                    new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(targetFile), "Shift_JIS"));
                bw.append(sw.toString());
                bw.flush();
                bw.close();
                log.info("rewrite " + targetFile.getAbsolutePath());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * pom.xml�̏�������
 * optional�w��ōŐV�̃e�X�gFW���Q�Ƃ��܂�
 * @author A1B5MEMO
 */
class PomRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(TestJavaRewriteCommand.class);

    public void rewrite(File targetFile) {
        try {

            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");

            reader
                .setFeature(
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);

            // XPath ��p�����Namespace�w���pom.xml���ǂݍ��߂Ȃ���Namespace����
            Document document = reader.read(targetFile);

            Element rootElement =
                (Element)document.selectSingleNode("/project");
            Element dependencies = rootElement.element("dependencies");

            Namespace ns = rootElement.getNamespace();
            Element dependency =
                DocumentHelper.createElement(new QName("dependency", ns));
            dependencies.add(dependency);
            Element groupId =
                DocumentHelper.createElement(new QName("groupId", ns));
            groupId.setText("murata");
            dependency.add(groupId);
            Element artifactId =
                DocumentHelper.createElement(new QName("artifactId", ns));
            artifactId.setText("murata-co-product-test");
            dependency.add(artifactId);
            Element version =
                DocumentHelper.createElement(new QName("version", ns));
            version.setText("0.4.12");
            dependency.add(version);
            Element optional =
                DocumentHelper.createElement(new QName("optional", ns));
            optional.setText("true");
            dependency.add(optional);

            XMLWriter output =
                new XMLWriter(new OutputStreamWriter(new FileOutputStream(
                    targetFile), "UTF-8"), new OutputFormat("", false, "UTF-8"));
            output.write(document);
            output.flush();
            output.close();
            log.info("rewrite " + targetFile.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 *
 * @author K.Miura
 * @version $Id$
 * @since JDK5.0
 */
class DbConnectSetting {

    /** logger */
    Log log = LogFactory.getLog(DbConnectSetting.class);

    private String jdbcUrl = "";

    private String user = "";

    private String password = "";

    public boolean load(String filePath) {

        try {

            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");

            reader
                .setFeature(
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);

            // XPath ��p�����Namespace�w���pom.xml���ǂݍ��߂Ȃ���Namespace����
            Document document = reader.read(filePath);

            // jdbcUrl
            Element e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='jdbcUrl']");
            String jdbcStr = e.attributeValue("value").trim();
            // ���s�A�󔒂��폜
            jdbcStr = jdbcStr.replaceAll("\r\n", "");
            jdbcStr = jdbcStr.replaceAll("\n", "");
            jdbcStr = jdbcStr.replaceAll("\n", "");
            jdbcStr = jdbcStr.replaceAll("\t", "");
            jdbcStr = jdbcStr.replaceAll(" ", "");
            // ���g�v���p�e�B�ɐݒ�
            this.jdbcUrl = jdbcStr;

            // user
            e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='user']");
            this.user = e.attributeValue("value").trim();

            // password
            e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='password']");
            this.password = e.attributeValue("value").trim();

            log.info("baseDatasourceFile : " + filePath);
            log.info("parameter/user : " + this.getUser() + " , password : " + this.getPassword() + " , jdbcUrl : " + this.getJdbcUrl());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

/**
 * applicationContext-datasource-local.xml(�Ɨގ��t�@�C��)����������
 * @author A1B5MEMB
 */
class DatasourceXmlRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(DatasourceXmlRewriteCommand.class);

    /** DB��Connection�ݒ�I�u�W�F�N�g */
    private DbConnectSetting dbcSetting;

    public DatasourceXmlRewriteCommand(DbConnectSetting dbcSetting) {
        this.dbcSetting = dbcSetting;
    }

    /** �ݒ菑�����\�b�h   */
    public void rewrite(File targetFile) {

        try {

            boolean isModify = false;

            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            reader
                .setFeature(
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);
            Document document = reader.read(targetFile);

            // beans�^�O���擾
            Element beans = (Element)document.selectSingleNode("/beans");

            for (Object item : beans.elements()) {
                if (item instanceof Element) {
                    Element pElement = (Element)item;

                    // bean�Ȃ�
                    if (pElement.getName().equals("bean")) {

                        for (Object innerItem : pElement.elements()) {
                            if (innerItem instanceof Element) {
                                Element cElement = (Element)innerItem;

                                // property �Ȃ�
                                if (cElement.getName().equals("property")) {

                                    // �ڑ�������u��
                                    if ("jdbcUrl".equals(cElement
                                        .attribute("name").getText())) {
                                        String jdbcUrl =
                                            this.dbcSetting.getJdbcUrl();
                                        String value =
                                            cElement.attribute("value")
                                                .getValue();
                                        if (!jdbcUrl.equals(value)) {
                                            // ��v���Ȃ����͒u������
                                            cElement.addAttribute("value",
                                                jdbcUrl);
                                            isModify = true;
                                        }
                                    }
                                    // user�u��
                                    if ("user".equals(cElement
                                        .attribute("name").getText())) {
                                        String user = this.dbcSetting.getUser();
                                        String value =
                                            cElement.attribute("value")
                                                .getValue();
                                        if (!user.equals(value)) {
                                            // ��v���Ȃ����͒u������
                                            cElement
                                                .addAttribute("value", user);
                                            isModify = true;
                                        }
                                    }
                                    // password�u��
                                    if ("password".equals(cElement
                                        .attribute(
                                        "name").getText())) {
                                        String password =
                                            this.dbcSetting.getPassword();
                                        String value =
                                            cElement.attribute("value")
                                                .getValue();
                                        if (!password.equals(value)) {
                                            // ��v���Ȃ����͒u������
                                            cElement.addAttribute("value",
                                                password);
                                            isModify = true;
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

            // �ύX���������ꍇ�̂ݏ㏑��
            if (isModify) {
                XMLWriter output =
                    new XMLWriter(new OutputStreamWriter(new FileOutputStream(
                        targetFile), "UTF-8"), new OutputFormat("", false,
                        "UTF-8"));
                output.write(document);
                output.flush();
                output.close();
                log.info("rewrite " + targetFile.getAbsolutePath());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * jdbc.properties.xml(�Ɨގ��t�@�C��)����������
 * @author A1B5MEMB
 */
class JdbcPropRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(JdbcPropRewriteCommand.class);

    /** DB��Connection�ݒ�I�u�W�F�N�g */
    private DbConnectSetting dbcSetting;

    public JdbcPropRewriteCommand(DbConnectSetting dbcSetting) {
        this.dbcSetting = dbcSetting;
    }

    /** �ݒ菑�����\�b�h   */
    public void rewrite(File targetFile) {

        try {

            boolean isModify = false;

            //Properties�I�u�W�F�N�g�𐶐��A�t�@�C����ǂݍ���
            Properties prop = new Properties();
            prop.load(new FileInputStream(targetFile));

            // �ڑ�������u��
            String newValue = dbcSetting.getJdbcUrl();
            String key = "jdbc.url";
            String value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // user�u��
            newValue = dbcSetting.getUser();
            key = "jdbc.username";
            value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // password�u��
            newValue = dbcSetting.getPassword();
            key = "jdbc.password";
            value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // �X�L�[�}���u��(Oracle��user�ƈꏏ)
            newValue = dbcSetting.getUser();
            key = "schema.name";
            value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // �ύX���������ꍇ�̂ݏ㏑��
            if (isModify) {
                // �v���p�e�B�̃��X�g���t�@�C���ɕۑ�(�㏑��)
                prop.store(new FileOutputStream(targetFile), null);
                log.info("rewrite " + targetFile.getAbsolutePath());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
