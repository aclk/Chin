package demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class Change {

    // private BigDecimal bd;

    // @Test
    public void testReg() {

        String regex = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

        String s = "x.jpg";
        assertTrue(s.matches(regex));

        s = "aaa.PNG";
        assertTrue(s.matches(regex));

        Pattern p = Pattern.compile("a*b");
        Matcher m = p.matcher("bba,ab,aaab,abbb");
        Set<String> values = new HashSet<String>();
        while (m.find()) {
            values.add(m.group());
        }
        for (String out : values) {
            System.err.println(out);
        }
    }

    // @Test
    public void testCase() {
        String str = "This is String ,,,, split by StringTokenizer, created by mkyong";

        // System.out.println("---- Split by comma ',' ------");
        // StringTokenizer tokens = new StringTokenizer(str, ",");
        //
        // while (tokens.hasMoreElements()) {
        // System.out.println("[" + tokens.nextElement() + "]");
        // }

        for (String token : str.split(",")) {
            System.out.println("[" + token + "]");
        }
    }

    // @Test
    public void testStringTokenizer() {
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/tmp/test.csv"));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                StringTokenizer tokens = new StringTokenizer(line, "|");
                while (tokens.hasMoreElements()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\nId : " + tokens.nextElement());
                    sb.append("\nPrice : " + tokens.nextElement());
                    sb.append("\nUsername : " + tokens.nextElement());
                    sb.append("\n*******************\n");
                    System.out.println(sb.toString());
                }
            }
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // @Test
    public void testSplitString() {
        String input = "职务=GM 薪水=50000 , 姓名=职业经理人 ; 性别=男 年龄=45 ";
        String patternStr = "(\\s*,\\s*)|(\\s*;\\s*)|(\\s+)";
        Pattern pattern = Pattern.compile(patternStr);
        String[] dataArr = pattern.split(input);
        for (String str : dataArr) {
            System.out.println("[" + str + "]");
        }

    }

    @Test
    public void testFileWriter() {
        // 文字列を追記したいファイルのディレクトリパスを取得します。
        String dir = System.getProperty("user.dir");

        // 文字列を追記したいファイル名を指定してFileオブジェクトを生成します。
        File file = new File(dir + "/test.txt");

        FileWriter filewriter = null;
        try {
            filewriter = new FileWriter(file, true);

            // ここでファイルに文字を書き込みます。
            filewriter.write("追加その1\r\n");
            filewriter.write("追加その2\r\n");
            System.out.println(file.getPath() + "ファイルの書き込みに成功しました!");
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            // クローズ処理（成功・失敗に関わらず必ずクローズします。）
            // クローズ漏れはバグのもとになります。必ずfinally句でクローズしましょう。
            if (filewriter != null) {
                try {
                    filewriter.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
