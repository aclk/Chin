package tools.directorywalker;

import java.io.*;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class TestXMLForAutotestReWriter extends ReWriter {

    private static final String OUTPUT_WARNINGS = "warningsTESTXML.txt";

    private String baseDir = "C:\\evidence";
    private String inputDir = "input";
    private String outputDir = "output";

    public TestXMLForAutotestReWriter() {
        // �v���p�e�B�t�@�C������㏑��
        if (AUTOTEST_SETTINGS != null && !AUTOTEST_SETTINGS.isEmpty()) {
            baseDir = AUTOTEST_SETTINGS.getProperty("baseDir");
            inputDir = AUTOTEST_SETTINGS.getProperty("inputDir");
            outputDir = AUTOTEST_SETTINGS.getProperty("outputDir");
        }
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

                // �Z�b�e�B���O���̏�������
                if ("settingVO".equals(bean.attributeValue("id"))) {
                    // �܂��A�v���_�N�gID���擾
                    Iterator properties = bean.elementIterator("property");
                    String productId = "other";
                    while (properties.hasNext()) {
                        Element property = (Element)properties.next();
                        if ("productId".equals(property.attributeValue("name"))) {
                            productId =property.attributeValue("value").toLowerCase();
                            // �����R��������ʔԍ��̏ꍇ������̂ŁA�폜
                            if (productId.substring(productId.length() - 3 ).matches("_[0123456789][0123456789]") ) {
                                productId = productId.substring(0 , productId.length() - 3);
                            }
                            break;
                        }
                    };
                    // �v���_�N�gID����ꂽ��A�f�[�^�o�́A���͂���������
                    StringBuilder sb = new StringBuilder();
                    sb.append(baseDir);
                    sb.append(File.separator);
                    sb.append(productId );
                    sb.append(File.separator);

                    String inPath = new StringBuilder(sb).append(inputDir).toString();
                    String outPath =  new StringBuilder(sb).append(outputDir).toString();

                    while (properties.hasNext()) {
                        Element property = (Element)properties.next();
                        if ("inputDir".equals(property.attributeValue("name"))) {
                            property.addAttribute("value", inPath);
                            isFileReWritten = true;
                        } else if ("evidenceDir".equals(property.attributeValue("name"))) {
                            property.addAttribute("value", outPath);
                            isFileReWritten = true;
                        }
                    };
                }

                // �A�h�o�C�U�[��(=�e�X�g�̎��)�̏�������
                if ("advisor".equals(bean.attributeValue("id"))) {
                    Iterator properties = bean.elementIterator("property");
                    while (properties.hasNext()) {
                        Element property = (Element)properties.next();
                        if ("advice".equals(property.attributeValue("name"))
                            && !"autoTestInterceptor".equals(property.attributeValue("ref"))) {
                            property.addAttribute("ref","autoTestInterceptor");
                            results
                                .add((new StringBuilder())
                                    .append(file.getAbsolutePath())
                                    .append(":�������[�h�ݒ肪�s���Ă܂���B")
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
