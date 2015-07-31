package main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 * 指定したURLから、BufferedReaderを取得する。
 * ただ、それだけ。
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class GetHtmlSource {

    private String charset = "UTF-8";

    private BufferedReader bufferedReader;

    // コンストラクタ
    public GetHtmlSource(URL url) {

        try {
            // 接続
            URLConnection uc = url.openConnection();
            // HTMLを読み込む
            BufferedInputStream bis =
                new BufferedInputStream(uc.getInputStream());
            bufferedReader =
                new BufferedReader(new InputStreamReader(bis, charset));

        } catch (MalformedURLException ex) {
            System.out.println("URLが不正です。");
            ex.printStackTrace();
        } catch (UnknownHostException ex) {
            System.out.println("サイトが見つかりません。");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void closeBufferedReader() {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
}