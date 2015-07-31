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
 * プロジェクト中の書き換えを行うじゃい
 *
 * @author A1B5MEMO
 */
public class ReWriterXai {

    public static String dbConSettingFilePath =
        "tools/SAITO/applicationContext-datasource-local.xml"; // デフォルトはtools/SAITOの下

    /**
     * @param args
     */
    public static void main(String[] args) {
        Options options = new Options();

        Option targetOption =
            OptionBuilder.hasArg(true).withArgName("target").isRequired(true)
                .withDescription("書き換え対象ディレクトリを指定").withLongOpt("target")
                .create("t");
        options.addOption(targetOption);

        Option schemaOption =
            OptionBuilder.hasArg(true).withArgName("schema").isRequired(false)
                .withDescription("スキーマ名を指定").withLongOpt("schema").create("s");
        options.addOption(schemaOption);

        Option typeOption =
            OptionBuilder.hasArg(true).withArgName("type").isRequired(false)
                .withDescription("テストの種類を指定").withLongOpt("type").create("p");
        options.addOption(typeOption);

        Option dsPathOption =
            OptionBuilder.hasArg(true).withArgName("dsPath").isRequired(false)
                .withDescription("データソースファイルパスを指定").withLongOpt("dsPath")
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

        // 元となるappilcationContext-datasource.xmlを取得する。
        DbConnectSetting dbcSetting = new DbConnectSetting();
        if (!dbcSetting.load(dbConSettingFilePath)) {
            dbcSetting = null;
        }

        // schemaNameが引数に指定されていなければ…
        if (schemaName == null || schemaName.trim().length() == 0) {
            // 設定ファイルからの値を正とする
            schemaName = dbcSetting.getUser();
        }

        DirectoryWalker dw = new DirectoryWalker();
        DefaultIgnorePatterns dip;
        // application-context-producttest-xmlを書き換え
        dip = new DefaultIgnorePatterns();
        TestxmlRewriteCommand trc = new TestxmlRewriteCommand();
        trc.setSchemaName(schemaName);
        trc.setTestType(testType);
        dw.walkAround(new File(target),
            ".*applicationcontext-product-test.*.xml", dip, trc);

        // *Test.javaを書き換え
        dip = new DefaultIgnorePatterns();
        dip.add("main");
        dip.add("resources");
        dip.add("springconf");
        dw.walkAround(new File(target), ".+Test\\.java", dip,
            new TestJavaRewriteCommand());

        // pom.xmlを書き換え(ベースラインリリースまで暫定)
        dip = new DefaultIgnorePatterns();
        dip.add("src");
        dw
            .walkAround(new File(target), "pom.xml", dip,
                new PomRewriteCommand());

        // applicationConetxt-datasource.xml(と類似する名前のファイル群)を書き換え
        dip = new DefaultIgnorePatterns();
        dip.add("java");
        dw.walkAround(new File(target),
            ".*applicationContext.*datasource.*\\.xml", dip,
            new DatasourceXmlRewriteCommand(dbcSetting));

        // jdbc.properties(と類似する名前のファイル群)を書き換え
        dip = new DefaultIgnorePatterns();
        dip.add("java");
        dw.walkAround(new File(target), ".*jdbc.*\\.properties", dip,
            new JdbcPropRewriteCommand(dbcSetting));

    }
}

/**
 * application-context-product-test.xmlを書き換え
 * @author A1B5MEMO
 */
class TestxmlRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(TestxmlRewriteCommand.class);

    /** スキーマ名 */
    private String schemaName;

    /** テストの種類(空文字は未指定) */
    private String testType;

    /** スキーマ名設定 */
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

            // スキーマ名置換
            Element e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='owner']");

            if (!this.schemaName.equals(e.attribute("value").getValue())) {
                // 一致しない時は置換する
                e.addAttribute("value", this.schemaName);
                isModify = true;
            }

            // テスト種類置換
            if (testType != null && testType.trim().length() > 0) {
                e =
                    (Element)document
                        .selectSingleNode("/beans/bean/property[@name='advice']");

                if (e != null) {
                    if (!this.testType.equals(e.attribute("ref").getValue())) {
                        // 一致しない時は置換する
                        e.addAttribute("ref", this.testType);
                        isModify = true;
                    }
                }
            }

            // ※2010/07/05 K.Miura Add
            // プロダクトIDを大文字化
            e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='productId']");
            if (e != null) {
                String productId = e.attribute("value").getValue();
                String productIdUpper = productId.toUpperCase();
                if (!productId.equals(productIdUpper)) {
                    // 大文字化してみて、異なるようであれば置換
                    e.addAttribute("value", productIdUpper);
                    isModify = true;
                }
            }

            // 変更があった場合のみ上書き
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
 * テストクラスの書き換え
 * @author A1B5MEMO
 *
 */
class TestJavaRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(TestJavaRewriteCommand.class);

    /**
     * Javaファイル中の大文字になっているXML⇒xmlにする
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
                // 置換があった場合のみファイル書き込み
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
 * pom.xmlの書き換え
 * optional指定で最新のテストFWを参照します
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

            // XPath を用いるとNamespace指定のpom.xmlが読み込めない⇒Namespace消す
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

            // XPath を用いるとNamespace指定のpom.xmlが読み込めない⇒Namespace消す
            Document document = reader.read(filePath);

            // jdbcUrl
            Element e =
                (Element)document
                    .selectSingleNode("/beans/bean/property[@name='jdbcUrl']");
            String jdbcStr = e.attributeValue("value").trim();
            // 改行、空白を削除
            jdbcStr = jdbcStr.replaceAll("\r\n", "");
            jdbcStr = jdbcStr.replaceAll("\n", "");
            jdbcStr = jdbcStr.replaceAll("\n", "");
            jdbcStr = jdbcStr.replaceAll("\t", "");
            jdbcStr = jdbcStr.replaceAll(" ", "");
            // 自身プロパティに設定
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
 * applicationContext-datasource-local.xml(と類似ファイル)を書き換え
 * @author A1B5MEMB
 */
class DatasourceXmlRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(DatasourceXmlRewriteCommand.class);

    /** DBのConnection設定オブジェクト */
    private DbConnectSetting dbcSetting;

    public DatasourceXmlRewriteCommand(DbConnectSetting dbcSetting) {
        this.dbcSetting = dbcSetting;
    }

    /** 設定書換メソッド   */
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

            // beansタグを取得
            Element beans = (Element)document.selectSingleNode("/beans");

            for (Object item : beans.elements()) {
                if (item instanceof Element) {
                    Element pElement = (Element)item;

                    // beanなら
                    if (pElement.getName().equals("bean")) {

                        for (Object innerItem : pElement.elements()) {
                            if (innerItem instanceof Element) {
                                Element cElement = (Element)innerItem;

                                // property なら
                                if (cElement.getName().equals("property")) {

                                    // 接続文字列置換
                                    if ("jdbcUrl".equals(cElement
                                        .attribute("name").getText())) {
                                        String jdbcUrl =
                                            this.dbcSetting.getJdbcUrl();
                                        String value =
                                            cElement.attribute("value")
                                                .getValue();
                                        if (!jdbcUrl.equals(value)) {
                                            // 一致しない時は置換する
                                            cElement.addAttribute("value",
                                                jdbcUrl);
                                            isModify = true;
                                        }
                                    }
                                    // user置換
                                    if ("user".equals(cElement
                                        .attribute("name").getText())) {
                                        String user = this.dbcSetting.getUser();
                                        String value =
                                            cElement.attribute("value")
                                                .getValue();
                                        if (!user.equals(value)) {
                                            // 一致しない時は置換する
                                            cElement
                                                .addAttribute("value", user);
                                            isModify = true;
                                        }
                                    }
                                    // password置換
                                    if ("password".equals(cElement
                                        .attribute(
                                        "name").getText())) {
                                        String password =
                                            this.dbcSetting.getPassword();
                                        String value =
                                            cElement.attribute("value")
                                                .getValue();
                                        if (!password.equals(value)) {
                                            // 一致しない時は置換する
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

            // 変更があった場合のみ上書き
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
 * jdbc.properties.xml(と類似ファイル)を書き換え
 * @author A1B5MEMB
 */
class JdbcPropRewriteCommand implements FileRewriteCommand {

    /** logger */
    Log log = LogFactory.getLog(JdbcPropRewriteCommand.class);

    /** DBのConnection設定オブジェクト */
    private DbConnectSetting dbcSetting;

    public JdbcPropRewriteCommand(DbConnectSetting dbcSetting) {
        this.dbcSetting = dbcSetting;
    }

    /** 設定書換メソッド   */
    public void rewrite(File targetFile) {

        try {

            boolean isModify = false;

            //Propertiesオブジェクトを生成、ファイルを読み込む
            Properties prop = new Properties();
            prop.load(new FileInputStream(targetFile));

            // 接続文字列置換
            String newValue = dbcSetting.getJdbcUrl();
            String key = "jdbc.url";
            String value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // user置換
            newValue = dbcSetting.getUser();
            key = "jdbc.username";
            value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // password置換
            newValue = dbcSetting.getPassword();
            key = "jdbc.password";
            value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // スキーマ名置換(Oracleはuserと一緒)
            newValue = dbcSetting.getUser();
            key = "schema.name";
            value = prop.getProperty(key);
            if (value != null && !value.equals(newValue)) {
                prop.setProperty(key, newValue);
                isModify = true;
            }

            // 変更があった場合のみ上書き
            if (isModify) {
                // プロパティのリストをファイルに保存(上書き)
                prop.store(new FileOutputStream(targetFile), null);
                log.info("rewrite " + targetFile.getAbsolutePath());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
