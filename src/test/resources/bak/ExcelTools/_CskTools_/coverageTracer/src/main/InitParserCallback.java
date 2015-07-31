package main;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

/**
 * Coverage ReportまでのHTMLを解析するCall Back
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class InitParserCallback extends ParserCallback {

    // index.htmlがフレームなので、左側のフレームのパスを格納するところ
    private String frameSrc;

    // a hrefタグ内容保持Map
    private Map<String, String> hrefMap = new HashMap<String, String>();

    // a hrefMap's Key
    private String hrefKey;

    // a hrefタグ内容取得用
    private boolean hrefFlag = false;

    public void handleText(char[] data, int pos) {
        if (hrefFlag) {
            hrefMap.put(hrefKey, String.valueOf(data));
        }
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        String frameSrc = "";
        // Frameタグから、フレーム名称を取得
        if (t.equals(HTML.Tag.FRAME)) {
            frameSrc = (String)a.getAttribute(HTML.Attribute.SRC);
        }
        if ("frame-sourcefiles.html".equals(frameSrc)) {
            this.frameSrc = frameSrc;
        }
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        // Aタグから、href名称を取得
        if (!hrefFlag) {
            if (t.equals(HTML.Tag.A)) {
                String href = (String)a.getAttribute(HTML.Attribute.HREF);
                hrefKey = href;
                hrefFlag = true;
            }
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        // Aタグから、href名称を取得
        if (hrefFlag) {
            if (t.equals(HTML.Tag.A)) {
                hrefKey = "";
                hrefFlag = false;
            }
        }
    }

    public String getFrameSrc() {
        return frameSrc;
    }

    public Map<String, String> getHrefMap() {
        return hrefMap;
    }

}
