package spiritstoolkit.dbsettings.output;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/** applicationContext-datasource-local.xml‘‚«Š·‚¦—p */
public class XmlReplaceOutput extends AbstractReplaceOutput {

    static final String XML_FILE =
        "src/main/resources/springconf/applicationContext-datasource-local.xml";

    static final String ENCODE = "UTF8";

    static final String JDBC_URL = "\"jdbcUrl\"";

    static final String USER = "\"user\"";

    static final String PASS = "\"password\"";

    static final String VALUE = "value";

    protected String getTargetFilePathName() {
        return XML_FILE;
    }

    protected void replaceOutput(File inputFile, File outputFile,
        String jdbcUrl, String user, String password, String schema)
        throws Exception {
        BufferedReader fr = null;
        PrintWriter fw = null;
        try {
            fr =
                new BufferedReader(new InputStreamReader(new FileInputStream(
                    inputFile), ENCODE));
            fw =
                new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outputFile), ENCODE)));
            String line;
            boolean urlFlg = false;
            boolean userFlg = false;
            boolean passFlg = false;
            boolean skip = false;
            boolean isUrl = (jdbcUrl != null && jdbcUrl.length() > 0);
            boolean commentFlg = false;
            if (isUrl) jdbcUrl = formatUrl(jdbcUrl);
            while ((line = fr.readLine()) != null) {
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
                    int posUser = line.indexOf(USER);
                    int posPass = line.indexOf(PASS);
                    int posUrl = line.indexOf(JDBC_URL);
                    if (posUrl != -1 && isUrl) {
                        urlFlg = true;
                    }
                    if (posUser != -1) {
                        userFlg = true;
                    }
                    if (posPass != -1) {
                        passFlg = true;
                    }
                    int posValue = line.indexOf(VALUE);
                    if ((urlFlg || userFlg || passFlg) && posValue != -1) {
                        int stPos = line.indexOf("\"", posValue) + 1;
                        int endPos = line.indexOf("\"", stPos);
                        String str = line.substring(0, stPos);
                        if (urlFlg) {
                            str += jdbcUrl;
                            urlFlg = false;
                        } else if (userFlg) {
                            str += user;
                            userFlg = false;
                        } else if (passFlg) {
                            str += password;
                            passFlg = false;
                        }
                        if (endPos != -1) {
                            str = str + line.substring(endPos);
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
			ConsoleUnit console = new ConsoleUnit();
			console.outPutStream(e.getMessage());
            throw e;
        } finally {
            if (fr != null) try {
                fr.close();
            } catch (Exception e) {
    			ConsoleUnit console = new ConsoleUnit();
    			console.outPutStream(e.getMessage());
            }
            if (fw != null) fw.close();
        }
    }

    private String formatUrl(String url) {
        url = url.replaceAll(" ", "");
        url = url.replaceAll("\\(ADDRESS", "\n                    (ADDRESS");
        url = url.replaceAll("\\(ADDRESS=", "  (ADDRESS=");
        url = url.replaceAll("\\(LOAD", "\n                      (LOAD");
        url =
            url.replaceAll("\\)\\(CONNECT",
                "\n                    )\n                    (CONNECT");
        url = url.replaceAll("\\(SERVICE", "\n                      (SERVICE");
        url =
            url.replaceAll("\\)\\)\\)",
                ")\n                    )\n                  )");
        url = url.replaceAll("=", " = ");
        return url;
    }
}
