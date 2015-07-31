package output;

import java.io.*;

/** applicationContext-product-test.xmlèëÇ´ä∑Ç¶óp */
public class ProductTestXmlReplaceOutput extends AbstractReplaceOutput {
    static final String XML_FILE = "applicationContext-product-test";
    static final String XML_PATH = "src/test/resources/springconf";
    static final String ENCODE = "UTF8";
    static final String OWNER = "owner";
    static final String VALUE = "value";
    private String targetName;
    protected String getTargetFilePathName() {
        return targetName;
    }
    public void replaceOutput(String prjRootPath,
                              String jdbcUrl,
                              String user,
                              String password,
                              String schema) throws Exception {
        File root = new File(prjRootPath);
        File xmlPath = new File(new File(prjRootPath), XML_PATH);
        File[] files = xmlPath.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) return false;
                String name = pathname.getName();
                if (!".xml".equalsIgnoreCase(name.substring(name.length() - 4))) return false;
                return name.startsWith("applicationContext-product-test");
            }});
        for(File f : files) {
            targetName = XML_PATH + "/" + f.getName();
            try {
                super.replaceOutput(prjRootPath, jdbcUrl, user, password, schema);
            } catch (IllegalArgumentException iae) {}
        }
    }
    protected void replaceOutput(File inputFile,
                                 File outputFile,
                                 String jdbcUrl,
                                 String user,
                                 String password,
                                 String schema) throws Exception {
        BufferedReader fr = null;
        PrintWriter fw = null;
        try {
            fr = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), ENCODE));
            fw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), ENCODE)));
            String line;
            boolean skip = false;
            boolean ownerFlg = false;
            boolean commentFlg = false;
            while((line = fr.readLine()) != null) {
                if (skip) {
                    if (line.indexOf("\"") != -1) skip = false;
                    continue;
                }
                int posComment = line.indexOf("<!--");
                if (posComment != -1) {
                    commentFlg = true;
                }
                posComment = line.indexOf("-->");
                if (posComment != -1) {
                    commentFlg = false;
                }
                if (!commentFlg) {
                    int posOwner = line.indexOf(OWNER);
                    if (posOwner != -1) {
                        ownerFlg = true;
                    }
                    int posValue = line.indexOf(VALUE);
                    if (ownerFlg && posValue != -1) {
                        int stPos = line.indexOf("\"", posValue) + 1;
                        int endPos = line.indexOf("\"", stPos);
                        String str = line.substring(0, stPos);
                        str += user;
                        ownerFlg = false;
                        if (endPos != -1) {
                            str = str  + line.substring(endPos);
                        } else {
                            str = str + "\" />";
                            skip = true;
                        }
                        line = str;
                    }
                }
                fw.println(line);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fr != null) try {fr.close();} catch(Exception e){}
            if (fw != null) fw.close();
        }
    }
}
