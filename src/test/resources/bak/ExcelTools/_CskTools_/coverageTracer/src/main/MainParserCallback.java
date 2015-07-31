package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

/**
 * Coverage ReportのHTMLを解析するCall Back
 * こいつが正直ややこしい
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class MainParserCallback extends ParserCallback {

    // カバレッジ網羅率格納用

    // カバレッジ網羅率(分母)
    private String lineCoverageMax;

    // カバレッジ網羅率(分子)
    private String lineCoverage;

    // 網羅率出現検知用フラグ
    private boolean greenbarFlag = false;

    // 行数
    private String lineNumber;

    // 行数出現検知用フラグ
    private boolean lineNumberFlag = false;

    private List<Map<String, String>> uncovList =
        new ArrayList<Map<String, String>>();

    private boolean unCoveredFlag = false;

    public void handleText(char[] data, int pos) {
        // カバレッジ網羅率
        if (greenbarFlag) {
            String lineCoverage = String.valueOf(data);
            String[] lineCoverageS = lineCoverage.split("/");

            this.lineCoverage = lineCoverageS[0];
            this.lineCoverageMax = lineCoverageS[1];
        }

        // nbHitsUncovered
        if (lineNumberFlag) {
            // よくわからんスペース対応
            int c = (int)data[0];
            String lineNumber = String.valueOf(data);
            if (c == 160) {
                this.lineNumber = lineNumber.substring(1);
            } else {
                this.lineNumber = lineNumber;
            }
        }

        // unCoveredFlag
        if (unCoveredFlag) {
            // よくわからんスペース対応
            int c = (int)data[0];
            String lineSrc = String.valueOf(data);
            Map<String, String> map = uncovList.get(uncovList.size() - 1);
            if (c == 160) {
                map.put("Value", map.get("Value") + lineSrc.substring(1));
            } else {
                map.put("Value", map.get("Value") + lineSrc);
            }
        }
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        if (t.equals(HTML.Tag.DIV)) {
            if ("greenbar".equals((String)a.getAttribute(HTML.Attribute.CLASS))) {
                greenbarFlag = true;
            }
        }

        if (t.equals(HTML.Tag.TD)) {

            if (!lineNumberFlag) {
                if ("numlinecover".equals((String)a
                    .getAttribute(HTML.Attribute.CLASS))) {
                    lineNumberFlag = true;
                }
            } else {
                if ("nbhitsuncovered".equals((String)a
                    .getAttribute(HTML.Attribute.CLASS))) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("Line", lineNumber);
                    map.put("Value", "");
                    uncovList.add(map);
                    unCoveredFlag = true;

                    this.lineNumberFlag = false;
                    this.lineNumber = "";
                }
            }
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        if (t.equals(HTML.Tag.DIV)) {
            greenbarFlag = false;
        }

        if (t.equals(HTML.Tag.TR)) {
            unCoveredFlag = false;
        }
    }

    /**
     * lineCoverageを戻す。
     * <br>
     * @return  lineCoverage
     */
    public String getLineCoverage() {
        return lineCoverage;
    }

    /**
     * lineCoverageMaxを戻す。
     * <br>
     * @return  lineCoverageMax
     */
    public String getLineCoverageMax() {
        return lineCoverageMax;
    }

    /**
     * uncovListを戻す。
     * <br>
     * @return  uncovList
     */
    public List<Map<String, String>> getUncovList() {
        return uncovList;
    }

}
