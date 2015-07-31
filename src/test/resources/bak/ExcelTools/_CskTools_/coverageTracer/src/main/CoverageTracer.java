package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

public class CoverageTracer {

    public static void main(String args[]) {

        // こいつが基礎のURL
        String targetUrl;

        try {
            String target = null;
            if (args.length == 0) {
                target = "target.txt";
            } else {
                target = args[0];
            }

            System.out.println("CoverageTracer Start...");
            System.out.println("target = " + target);

            if (target.endsWith(".txt")) {

                FileInputStream fis = new FileInputStream(target);
                InputStreamReader ir = new InputStreamReader(fis, "MS932");
                BufferedReader br = new BufferedReader(ir);

                while ((targetUrl = br.readLine()) != null) {

                    if (targetUrl.startsWith("//")) {
                        continue;
                    }

                    getCoverageTraceFile(targetUrl);
                }
            } else {
                targetUrl = target;

                getCoverageTraceFile(targetUrl);

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CoverageTracer Error...");
            return;
        } finally {
            System.out.println("CoverageTracer Finished...");
        }

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
            targetIndex = targetUrl.lastIndexOf("cobertura") - 1;
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

    /**
     *  coverageTraceFileを取得する
     *
     *  @param targetUrl 取得先URL
     */
    private static void getCoverageTraceFile(String targetUrl) throws Exception {

        System.out.println(targetUrl);

        // プロジェクト名取得
        String projetctId = getProjectId(targetUrl);

        InitParserCallback cb = new InitParserCallback();

        // mainページをパース
        ParseForHtml.parse(targetUrl, cb);

        String baseUrl = targetUrl.substring(0, targetUrl.lastIndexOf("/"));

        // フレームページ(左下)部をパース
        ParseForHtml.parse(baseUrl + "/" + cb.getFrameSrc(), cb);

        Map<String, String> hrefMap = cb.getHrefMap();

        // hrefの件数分ループ
        for (Map.Entry<String, String> map : hrefMap.entrySet()) {

            // System.out.println(e.getValue());

            // 対象は、"～Impl"、"ValidationUnit"のみ
            if (map.getValue().endsWith("Impl")
                || map.getValue().endsWith("ValidationUnit")) {

                String implUrl = baseUrl + "/" + map.getKey();

                // coverage解析用Callback
                MainParserCallback mcb = new MainParserCallback();

                // mainページをパース
                ParseForHtml.parse(implUrl, mcb);

                File logfile = new File("traceResult");
                logfile.mkdirs();

                FileOutputStream fos =
                    new FileOutputStream(logfile.getName() + "/" + projetctId
                        + " " + map.getValue() + ".log");
                OutputStreamWriter osw = new OutputStreamWriter(fos, "MS932");
                BufferedWriter bw = new BufferedWriter(osw);

                bw.write(map.getValue());
                bw.newLine();

                for (Map<String, String> aa : mcb.getUncovList()) {
                    bw.write("Line:" + aa.get("Line") + " || "
                        + aa.get("Value"));
                    bw.newLine();
                }

                bw.close();
                osw.close();
                fos.close();

            }
        }
    }
}