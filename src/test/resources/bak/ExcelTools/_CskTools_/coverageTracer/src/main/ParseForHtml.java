package main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * Parseとは名ばかりのクラス。
 * ParserDelegator.parseをよんでいるだけ。
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class ParseForHtml {

    public static void parse(String Url, ParserCallback pcb) {

        GetHtmlSource ghs = null;

        try {
            URL url = new URL(Url);
            ghs = new GetHtmlSource(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        ParserDelegator pd = new ParserDelegator();

        try {
            pd.parse(ghs.getBufferedReader(), pcb, true);
            ghs.closeBufferedReader();
        } catch (IOException e) {
            // ParseError
            e.printStackTrace();
            return;
        }
    }
}
