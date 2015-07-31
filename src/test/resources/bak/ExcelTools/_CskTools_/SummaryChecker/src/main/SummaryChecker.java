package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import bean.SummaryResult;

public class SummaryChecker {

    public static void main(String args[]) {

        // こいつが基礎のURL
        String targetUrl;

        try {

            FileOutputStream fos =
                new FileOutputStream("SummaryChecker.log", false);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "MS932");
            BufferedWriter bw = new BufferedWriter(osw);

            bw.write("ProductID" + "\t");
            bw.write("Tests" + "\t");
            bw.write("Errors" + "\t");
            bw.write("Failures" + "\t");
            bw.write("Skipped" + "\t");
            bw.write("Success Rate" + "\t");
            bw.write("Time");
            bw.newLine();

            bw.close();
            osw.close();
            fos.close();

            String target = null;
            if (args.length == 0) {
                target = "target.txt";
            } else {
                target = args[0];
            }
            System.out.println("target = " + target);

            if (target.endsWith(".txt")) {
                // テキスト IN

                FileInputStream fis = new FileInputStream(target);
                InputStreamReader ir = new InputStreamReader(fis, "MS932");
                BufferedReader br = new BufferedReader(ir);

                while ((targetUrl = br.readLine()) != null) {
                    callParseMethod(targetUrl);
                }
            } else {
                // URL IN
                targetUrl = target;
                callParseMethod(targetUrl);

            }

            System.out.println("SummaryCheck done.");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static void callParseMethod(String targetUrl) throws Exception {

        if (targetUrl.startsWith("//")) {
            return;
        }
        // プロジェクト名取得
        String projetctId = getProjectId(targetUrl);

        System.out.println("[" + projetctId + "]parsing...");

        targetUrl =
            targetUrl.substring(0, targetUrl.lastIndexOf("/"))
                + "/surefire-report.html";

        SummaryParserCallback cb = new SummaryParserCallback();

        // mainページをパース
        ParseForHtml.parse(targetUrl, cb);

        SummaryResult sr = cb.getSummaryResult();

        FileOutputStream fos = new FileOutputStream("SummaryChecker.log", true);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "MS932");
        BufferedWriter bw = new BufferedWriter(osw);

        bw.write(projetctId + "\t");
        if (sr.getTime() != null) {
            bw.write(sr.getTests() + "\t");
            bw.write(sr.getErrors() + "\t");
            bw.write(sr.getFailures() + "\t");
            bw.write(sr.getSkipped() + "\t");
            bw.write(sr.getSuccessRate() + "\t");
            bw.write(sr.getTime());
        }
        bw.newLine();

        bw.close();
        osw.close();
        fos.close();
    }

    /**
     * プロジェクト名取得
     * @param targetUrl 取得先URL
     * @return プロジェクト名
     */
    private static String getProjectId(String targetUrl) {
        int startIndex = 0;
        int targetIndex = targetUrl.lastIndexOf("target") - 1;
        // ファイル形式？
        if (0 >= targetIndex) {
            // URL
            targetIndex = targetUrl.lastIndexOf("/");
        }
        do {
            if (targetUrl.indexOf("/", startIndex + 1) != targetIndex) {
                startIndex = targetUrl.indexOf("/", startIndex + 1);
            } else {
                break;
            }

        } while (true);
        String projetctId = targetUrl.substring(startIndex + 1, targetIndex);
        return projetctId;
    }
}