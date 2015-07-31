package tools.directorywalker;

import java.io.*;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class TestXMLReWriter extends ReWriter {

    private String schema;

    private static final String OUTPUT_WARNINGS = "warningsTESTXML.txt";

    public TestXMLReWriter(String schema) {
        this.schema = schema;
    }

    public void walk(String root) throws Exception {
        List warnings = new ArrayList();
        walk(new File(root), ((Collection)(warnings)));
        if (!warnings.isEmpty())
            outputWarnings(warnings, (new StringBuilder()).append(root).append(
                OUTPUT_WARNINGS).toString());
    }

    public void handleFile(File file, int depth, Collection results) {
        XMLWriter writer;

        try {

            if (file.getAbsolutePath().contains("target")) return;
            if (file.getAbsolutePath().contains(".svn")) return;
            if (file.getAbsolutePath().contains(".settings")) return;
            if (file.getAbsolutePath().contains(".metadata")) return;
            if (!file.getName().startsWith("applicationContext-product-test"))
                return;
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
            for (Iterator beans = root.elementIterator("bean"); beans.hasNext();) {
                Element bean = (Element)beans.next();
                if ("settingVO".equals(bean.attributeValue("id"))) {
                    Iterator properties = bean.elementIterator("property");
                    do {
                        if (!properties.hasNext()) break;
                        Element property = (Element)properties.next();
                        if ("owner".equals(property.attributeValue("name"))) {
                            property.addAttribute("value", schema);
                            isFileReWritten = true;
                        } else if ("sqlDir".equals(property.attributeValue("name"))) {
                            // sqlDirがバックスラッシュ使うなど、悪さをしているなら。
                            String sqlDir = property.attributeValue("value");
                            if (sqlDir.indexOf("\\") > -1) {
                                sqlDir = sqlDir.replace("\\", "/");
                                property.addAttribute("value", sqlDir);
                                isFileReWritten = true;
                            }
                        }

                    } while (true);
                }
                if ("advisor".equals(bean.attributeValue("id"))) {
                    Iterator properties = bean.elementIterator("property");
                    while (properties.hasNext()) {
                        Element property = (Element)properties.next();
                        if ("advice".equals(property.attributeValue("name"))
                            && !"regressionTestInterceptor".equals(property
                                .attributeValue("ref"))) {
                            property.addAttribute("ref",
                                "regressionTestInterceptor");
                            results
                                .add((new StringBuilder())
                                    .append(file.getAbsolutePath())
                                    .append(
                                        ":\u56DE\u5E30\u30E2\u30FC\u30C9\u8A2D\u5B9A\u304C\u884C\u308F\u308C\u3066\u307E\u305B\u3093\u3002")
                                    .toString());
                            isFileReWritten = true;
                        }
                    }
                }
            }

            if (isFileReWritten) {
                writer = new XMLWriter(new FileWriter(file));
                writer.setOutputStream(new FileOutputStream(file));
                writer.write(document);
                writer.flush();
                writer.close();
            }
            if (writer != null) writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
