package bean;

public class SummaryResult {

    private String Title;

    private String Tests;

    private String Errors;

    private String Failures;

    private String Skipped;

    private String SuccessRate;

    private String Time;

    /**
     * titleを戻す。
     * <br>
     * @return  title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * titleを設定する。
     * <br>
     * @param title String
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * errorsを戻す。
     * <br>
     * @return  errors
     */
    public String getErrors() {
        return Errors;
    }

    /**
     * errorsを設定する。
     * <br>
     * @param errors String
     */
    public void setErrors(String errors) {
        Errors = errors;
    }

    /**
     * failuresを戻す。
     * <br>
     * @return  failures
     */
    public String getFailures() {
        return Failures;
    }

    /**
     * failuresを設定する。
     * <br>
     * @param failures String
     */
    public void setFailures(String failures) {
        Failures = failures;
    }

    /**
     * skippedを戻す。
     * <br>
     * @return  skipped
     */
    public String getSkipped() {
        return Skipped;
    }

    /**
     * skippedを設定する。
     * <br>
     * @param skipped String
     */
    public void setSkipped(String skipped) {
        Skipped = skipped;
    }

    /**
     * successRateを戻す。
     * <br>
     * @return  successRate
     */
    public String getSuccessRate() {
        return SuccessRate;
    }

    /**
     * successRateを設定する。
     * <br>
     * @param successRate String
     */
    public void setSuccessRate(String successRate) {
        SuccessRate = successRate;
    }

    /**
     * testsを戻す。
     * <br>
     * @return  tests
     */
    public String getTests() {
        return Tests;
    }

    /**
     * testsを設定する。
     * <br>
     * @param tests String
     */
    public void setTests(String tests) {
        Tests = tests;
    }

    /**
     * timeを戻す。
     * <br>
     * @return  time
     */
    public String getTime() {
        return Time;
    }

    /**
     * timeを設定する。
     * <br>
     * @param time String
     */
    public void setTime(String time) {
        Time = time;
    }
}