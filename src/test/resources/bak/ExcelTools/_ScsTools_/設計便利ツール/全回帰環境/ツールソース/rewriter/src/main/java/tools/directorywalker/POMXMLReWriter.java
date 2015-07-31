package tools.directorywalker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.apache.commons.lang.ArrayUtils;

public class POMXMLReWriter extends ReWriter {

    private String baseline;

    private String version;

    private String murataBaselineVer;

    private static final String OUTPUT_WARNINGS = "warningsPOMXML.txt";

    public POMXMLReWriter(String baseline, String version,
        String murataBaselineVer) {
        this.baseline = baseline;
        this.version = version;
        this.murataBaselineVer = murataBaselineVer;
    }

    public void walk(String root) throws Exception {
        List warnings = new ArrayList();
        walk(new File(root), ((Collection)(warnings)));
        if (!warnings.isEmpty())
            outputWarnings(warnings, (new StringBuilder()).append(root).append(
                OUTPUT_WARNINGS).toString());
    }

    public void handleFile(File file, int depth, Collection results) {
        XMLWriter writer = null;

        try {
            if (file.getAbsolutePath().contains("target")) return;
            if (file.getAbsolutePath().contains(".svn")) return;
            if (file.getAbsolutePath().contains(".settings")) return;
            if (file.getAbsolutePath().contains(".metadata")) return;
            if (!file.getName().equals("pom.xml")) return;
            writer = null;
            System.out.println(file.getAbsolutePath());
            boolean isFileReWritten = false;
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            reader
                .setFeature(
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);
            Document document = reader.read(file);
            Element root = document.getRootElement();
            Element dependencies = root.element("dependencies");
            Iterator dependencyIt = dependencies.elementIterator("dependency");
            do {
                if (!dependencyIt.hasNext()) break;
                Element dependency = (Element)dependencyIt.next();

                String artifactId = dependency.elementText("artifactId");
                // artifactIdがあれば
                if (artifactId != null && artifactId.trim().length() > 0) {
                    // baseline指定の場合で、baseline名が一致していた場合
                    if (baseline.equals(artifactId)) {
                        if (!version.equals(dependency.elementText("version"))) {
                            dependency.element("version").setText(version);
                            results.add((new StringBuilder()).append(
                                file.getAbsolutePath()).append(
                                ":業務中分類ベースラインのバージョンが正しく設定されていません。").toString());
                            isFileReWritten = true;
                        }
                        if (dependency.element("optional") == null) {
                            dependency.addElement("optional");
                            dependency.element("optional").setText("true");
                            results.add((new StringBuilder()).append(
                                file.getAbsolutePath()).append(
                                ":optional設定を追加しました").toString());
                            isFileReWritten = true;
                        }
                    } else {
                        // baseilne名が一致していない場合
                        results.add((new StringBuilder()).append(
                            file.getAbsolutePath()).append(
                            ":業務中分類ベースライン以外の依存記述があります。[").append(
                            dependency.elementText("artifactId")).append("]")
                            .toString());
                    }

                    // 引数指定のベースライン名の１文字目が大文字であり、
                    // かつ"murata-{ss|ms|ps}-(何文字か)baselineのものだった
                    if (artifactId.matches("murata-.s-.*baseline")) {
                        // baseline名とバージョンを変更。
                        dependency.element("artifactId").setText(
                            baseline.toLowerCase());
                        dependency.element("version").setText(version);
                        results.add((new StringBuilder()).append(
                            file.getAbsolutePath()).append(
                            ":強制的にベースライン、バージョンを置換しました。[").append(artifactId)
                            .append("→").append(baseline.toLowerCase()).append(
                                "]").toString());
                        if (dependency.element("optional") == null) {
                            dependency.addElement("optional");
                            dependency.element("optional").setText("true");
                        }
                        isFileReWritten = true;
                    }

                    // murata-baseline の置き換えを指定されていた場合
                    if (murataBaselineVer != null) {
                        // murata-baselineの依存関係箇所なら
                        if (artifactId.toLowerCase().equals("murata-baseline")) {
                            dependency.element("version").setText(
                                murataBaselineVer);
                            results.add((new StringBuilder()).append(
                                file.getAbsolutePath()).append(
                                ":強制的にmurata-baselineのバージョンを置換しました。[").append(
                                artifactId).append("→").append(
                                murataBaselineVer.toLowerCase()).append("]")
                                .toString());
                            if (dependency.element("optional") == null) {
                                dependency.addElement("optional");
                            }
                            dependency.element("optional").setText("true");
                            isFileReWritten = true;
                        }
                    }
                }
            } while (true);
            Element properties = root.element("properties");
            if (isBtpBatchOrRemote(properties.element("productId").getText())
                && properties.element("packaging") != null) {
                properties.remove(properties.element("packaging"));
                isFileReWritten = true;
            }
            if (isFileReWritten) {
                writer = new XMLWriter(new FileWriter(file));
                writer.setOutputStream(new FileOutputStream(file));
                writer.write(document);
                writer.flush();
                writer.close();
            }

            if (writer != null) writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isBtpBatchOrRemote(String text) {
        return ArrayUtils.contains(((String)TARGET_PRODUCTS.get("btp-batch"))
            .split(","), text)
            || ArrayUtils.contains(((String)TARGET_PRODUCTS.get("btp-remote"))
                .split(","), text);
    }

}
