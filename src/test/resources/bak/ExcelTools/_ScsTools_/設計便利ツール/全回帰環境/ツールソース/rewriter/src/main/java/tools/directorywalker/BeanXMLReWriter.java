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
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

public class BeanXMLReWriter extends ReWriter {

    private static final String OUTPUT_WARNINGS = "warningsBeanXML.txt";

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
            if (TARGET_PRODUCTS.get(file.getName()) == null) return;
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
                Iterator properties = bean.elementIterator("property");
                while (properties.hasNext()) {
                    Element property = (Element)properties.next();
                    Iterator refs = property.elementIterator("ref");
                    while (refs.hasNext()) {
                        Element ref = (Element)refs.next();
                        if (ref.attributeValue("bean").startsWith("mock")) {
                            String before = ref.attributeValue("bean");
                            String after =
                                TARGET_PRODUCTS.getProperty(file.getName());
                            ref.addAttribute("bean", after);
                            results
                                .add((new StringBuilder())
                                    .append(file.getAbsolutePath())
                                    .append(
                                        ":MQES\u306EDI\u3092\u7F6E\u63DB\u3057\u307E\u3057\u305F\u3002[")
                                    .append(before).append("]\u21D2[").append(
                                        after).append("]").toString());
                            isFileReWritten = true;
                        }
                        if ("messageSendPSC".equals(ref.attributeValue("bean"))
                            || "tdStartPSC".equals(ref.attributeValue("bean"))
                            || "delayStartPSC".equals(ref
                                .attributeValue("bean")))
                            results.add((new StringBuilder()).append(
                                file.getAbsolutePath()).append(
                                ":MQES\u306E\u8A2D\u5B9A[").append(
                                ref.attributeValue("bean")).append("]")
                                .toString());
                    }
                }
            }

            if (isFileReWritten) {
                writer = new XMLWriter(new FileWriter(file));
                writer.setOutputStream(new FileOutputStream(file));
                writer.write(document);
                writer.flush();
            }
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
