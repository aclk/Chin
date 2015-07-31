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

    private static final String OUTPUT_WARNINGS = "warningsPOMXML.txt";

    public POMXMLReWriter(String baseline, String version) {
        this.baseline = baseline;
        this.version = version;
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
                if (baseline.equals(dependency.elementText("artifactId"))) {
                    if (!version.equals(dependency.elementText("version"))) {
                        dependency.element("version").setText(version);
                        results
                            .add((new StringBuilder())
                                .append(file.getAbsolutePath())
                                .append(
                                    ":\u696D\u52D9\u4E2D\u5206\u985E\u30D9\u30FC\u30B9\u30E9\u30A4\u30F3\u306E\u30D0\u30FC\u30B8\u30E7\u30F3\u304C\u6B63\u3057\u304F\u8A2D\u5B9A\u3055\u308C\u3066\u3044\u307E\u305B\u3093\u3002")
                                .toString());
                        isFileReWritten = true;
                    }
                    if (dependency.element("optional") == null) {
                        dependency.addElement("optional");
                        dependency.element("optional").setText("true");
                        results
                            .add((new StringBuilder())
                                .append(file.getAbsolutePath())
                                .append(
                                    ":optional\u8A2D\u5B9A\u3092\u8FFD\u52A0\u3057\u307E\u3057\u305F\u3002")
                                .toString());
                        isFileReWritten = true;
                    }
                } else {
                    results
                        .add((new StringBuilder())
                            .append(file.getAbsolutePath())
                            .append(
                                ":\u696D\u52D9\u4E2D\u5206\u985E\u30D9\u30FC\u30B9\u30E9\u30A4\u30F3\u4EE5\u5916\u306E\u4F9D\u5B58\u8A18\u8FF0\u304C\u3042\u308A\u307E\u3059\u3002[")
                            .append(dependency.elementText("artifactId"))
                            .append("]").toString());
                }
                if (Character.isUpperCase(baseline.charAt(0))) {
                    String artifactId = dependency.elementText("artifactId");
                    if (artifactId.equals("murata-baseline")
                        || artifactId.matches("murata-ss-s[ezsax]{1}-baseline")) {
                        dependency.element("artifactId").setText(
                            baseline.toLowerCase());
                        dependency.element("version").setText(version);
                        results
                            .add((new StringBuilder())
                                .append(file.getAbsolutePath())
                                .append(
                                    ":\u5F37\u5236\u7684\u306B\u30D9\u30FC\u30B9\u30E9\u30A4\u30F3\u3001\u30D0\u30FC\u30B8\u30E7\u30F3\u3092\u7F6E\u63DB\u3057\u307E\u3057\u305F\u3002[")
                                .append(artifactId).append("\u2192").append(
                                    baseline.toLowerCase()).append("]")
                                .toString());
                        if (dependency.element("optional") == null) {
                            dependency.addElement("optional");
                            dependency.element("optional").setText("true");
                        }
                        isFileReWritten = true;
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
