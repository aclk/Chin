package tools.directorywalker;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.DirectoryWalker;

public abstract class ReWriter extends DirectoryWalker {

    protected static Properties TARGET_PRODUCTS;

    protected static Properties AUTOTEST_SETTINGS;

    protected void outputWarnings(List warnings, String path) {

        PrintWriter writer = null;
        try {

            writer = new PrintWriter(new FileWriter(path));
            String warning;
            for (Iterator i$ = warnings.iterator(); i$.hasNext(); writer
                .println(warning)) {
                warning = (String)i$.next();
            }
            writer.flush();

            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        TARGET_PRODUCTS = new Properties();
        AUTOTEST_SETTINGS = new Properties();
        try {
            if ("TRUE".equals(System.getProperty("debug"))) {
                TARGET_PRODUCTS.load(ReWriter.class
                    .getResourceAsStream("external_targets.properties"));
                AUTOTEST_SETTINGS.load(ReWriter.class
                    .getResourceAsStream("autotest_settings.properties"));
            } else {
                TARGET_PRODUCTS.load(ClassLoader
                    .getSystemResourceAsStream("external_targets.properties"));
                AUTOTEST_SETTINGS.load(ClassLoader
                    .getSystemResourceAsStream("autotest_settings.properties"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
