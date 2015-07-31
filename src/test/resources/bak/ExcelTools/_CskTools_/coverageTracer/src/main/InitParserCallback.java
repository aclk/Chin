package main;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

/**
 * Coverage Report�܂ł�HTML����͂���Call Back
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class InitParserCallback extends ParserCallback {

    // index.html���t���[���Ȃ̂ŁA�����̃t���[���̃p�X���i�[����Ƃ���
    private String frameSrc;

    // a href�^�O���e�ێ�Map
    private Map<String, String> hrefMap = new HashMap<String, String>();

    // a hrefMap's Key
    private String hrefKey;

    // a href�^�O���e�擾�p
    private boolean hrefFlag = false;

    public void handleText(char[] data, int pos) {
        if (hrefFlag) {
            hrefMap.put(hrefKey, String.valueOf(data));
        }
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        String frameSrc = "";
        // Frame�^�O����A�t���[�����̂��擾
        if (t.equals(HTML.Tag.FRAME)) {
            frameSrc = (String)a.getAttribute(HTML.Attribute.SRC);
        }
        if ("frame-sourcefiles.html".equals(frameSrc)) {
            this.frameSrc = frameSrc;
        }
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        // A�^�O����Ahref���̂��擾
        if (!hrefFlag) {
            if (t.equals(HTML.Tag.A)) {
                String href = (String)a.getAttribute(HTML.Attribute.HREF);
                hrefKey = href;
                hrefFlag = true;
            }
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        // A�^�O����Ahref���̂��擾
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
