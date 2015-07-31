package main;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import bean.SummaryResult;

/**
 * Coverage ReportÇ‹Ç≈ÇÃHTMLÇâêÕÇ∑ÇÈCall Back
 *
 * @author H.Uchimoto
 * @version $Id$
 * @since JDK5.0
 */
public class SummaryParserCallback extends ParserCallback {

    private boolean titleFlag = false;

    private boolean summaryFlag = false;

    private boolean testsFlag = false;

    private boolean errorsFlag = false;

    private boolean failuresFlag = false;

    private boolean skippedFlag = false;

    private boolean successRateFlag = false;

    private boolean timeFlag = false;

    private SummaryResult summaryResult;

    //private boolean packageListFlag = false;

    SummaryParserCallback() {
        summaryResult = new SummaryResult();
    }

    public void handleText(char[] data, int pos) {
        if (titleFlag) {
            summaryResult.setTitle(String.valueOf(data));
        }

        if (summaryFlag) {

            if (timeFlag) {
                summaryResult.setTime(String.valueOf(data));
                return;
            }

            if (successRateFlag) {
                summaryResult.setSuccessRate(String.valueOf(data));
                return;
            }

            if (skippedFlag) {
                summaryResult.setSkipped(String.valueOf(data));
                return;
            }

            if (failuresFlag) {
                summaryResult.setFailures(String.valueOf(data));
                return;
            }

            if (errorsFlag) {
                summaryResult.setErrors(String.valueOf(data));
                return;
            }

            if (testsFlag) {
                summaryResult.setTests(String.valueOf(data));
                return;
            }
        }
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        if (t.equals(HTML.Tag.TITLE)) {
            titleFlag = true;
        }

        if (t.equals(HTML.Tag.A)) {
            // SummaryÇ™àÍî‘ç≈èâÇ…Ç≠ÇÈÇÃÇëOíÒÇ…ÇµÇƒÇ‹Ç¬
            if ("Summary".equals(a.getAttribute(HTML.Attribute.NAME))) {

                summaryFlag = true;

            } else if ("Package_List".equals(a
                .getAttribute(HTML.Attribute.NAME))) {

                summaryFlag = false;

                //packageListFlag = true;

            } else if (!"".equals(a.getAttribute(HTML.Attribute.NAME))) {

                //packageListFlag = false;
            }
        }

        if (summaryFlag) {
            if (t.equals(HTML.Tag.TD)) {

                if (!testsFlag) {
                    testsFlag = true;
                } else if (!errorsFlag) {
                    errorsFlag = true;
                } else if (!failuresFlag) {
                    failuresFlag = true;
                } else if (!skippedFlag) {
                    skippedFlag = true;
                } else if (!successRateFlag) {
                    successRateFlag = true;
                } else if (!timeFlag) {
                    timeFlag = true;
                }
            }
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        if (timeFlag) {
            if (t.equals(HTML.Tag.TD)) {
                summaryFlag = false;
            }
        }

        if (t.equals(HTML.Tag.TITLE)) {
            titleFlag = false;
        }

        if (t.equals(HTML.Tag.HTML)) {
            summaryFlag = false;
        }
    }

    /**
     * summaryResultÇñﬂÇ∑ÅB
     * <br>
     * @return  summaryResult
     */
    public SummaryResult getSummaryResult() {
        return summaryResult;
    }
}