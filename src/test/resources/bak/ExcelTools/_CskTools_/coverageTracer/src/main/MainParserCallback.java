package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

/**
 * Coverage Report��HTML����͂���Call Back
 * ������������₱����
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class MainParserCallback extends ParserCallback {

    // �J�o���b�W�ԗ����i�[�p

    // �J�o���b�W�ԗ���(����)
    private String lineCoverageMax;

    // �J�o���b�W�ԗ���(���q)
    private String lineCoverage;

    // �ԗ����o�����m�p�t���O
    private boolean greenbarFlag = false;

    // �s��
    private String lineNumber;

    // �s���o�����m�p�t���O
    private boolean lineNumberFlag = false;

    private List<Map<String, String>> uncovList =
        new ArrayList<Map<String, String>>();

    private boolean unCoveredFlag = false;

    public void handleText(char[] data, int pos) {
        // �J�o���b�W�ԗ���
        if (greenbarFlag) {
            String lineCoverage = String.valueOf(data);
            String[] lineCoverageS = lineCoverage.split("/");

            this.lineCoverage = lineCoverageS[0];
            this.lineCoverageMax = lineCoverageS[1];
        }

        // nbHitsUncovered
        if (lineNumberFlag) {
            // �悭�킩���X�y�[�X�Ή�
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
            // �悭�킩���X�y�[�X�Ή�
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
     * lineCoverage��߂��B
     * <br>
     * @return  lineCoverage
     */
    public String getLineCoverage() {
        return lineCoverage;
    }

    /**
     * lineCoverageMax��߂��B
     * <br>
     * @return  lineCoverageMax
     */
    public String getLineCoverageMax() {
        return lineCoverageMax;
    }

    /**
     * uncovList��߂��B
     * <br>
     * @return  uncovList
     */
    public List<Map<String, String>> getUncovList() {
        return uncovList;
    }

}
