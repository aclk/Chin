import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.DirectoryWalker;

/**
 *
 */

/**
 * @author T.Ueda
 * @version $Id$
 * @since JDK5.0
 */
public class LineCounter extends DirectoryWalker {

    private static final String CRITERIA_PATH = "src/main/resources/";

    private static final String TARGET = "criteria.xml";

    private static final String OUTPUT_FILE = "TotalLinesOfCriteria.txt";

    /**
     * @param args
     */
    public static void main(String[] args) {

        String arg = CRITERIA_PATH;
        if (args.length == 1) arg = args[0];

        System.out.println(arg);
        new LineCounter().countLine(arg);

    }

    public void countLine(String root) {

        PrintWriter writer = null;
        try {
            List<String> results = new ArrayList<String>();
            walk(new File(root), results);

            writer = new PrintWriter(new FileWriter(OUTPUT_FILE));

            for (String result : results)
                writer.println(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        } finally {
            writer.close();
        }
    }

    @Override
    public void handleFile(File file, int depth, Collection results) {

        if (file.getName().endsWith(TARGET)
            && file.getAbsolutePath().indexOf("target") < 0) {

            System.out.println(file.getAbsolutePath());
            this.count(file, results);
        }

    }

    public void count(File file, Collection results) {

        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new FileReader(file));

            int cnt = 0;
            while (reader.readLine() != null)
                cnt++;

            results.add(file.getName() + "," + cnt);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        } finally {

            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.exit(0);
            }
        }

    }
}
