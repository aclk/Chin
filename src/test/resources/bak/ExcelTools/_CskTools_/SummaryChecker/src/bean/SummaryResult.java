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
     * title��߂��B
     * <br>
     * @return  title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * title��ݒ肷��B
     * <br>
     * @param title String
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * errors��߂��B
     * <br>
     * @return  errors
     */
    public String getErrors() {
        return Errors;
    }

    /**
     * errors��ݒ肷��B
     * <br>
     * @param errors String
     */
    public void setErrors(String errors) {
        Errors = errors;
    }

    /**
     * failures��߂��B
     * <br>
     * @return  failures
     */
    public String getFailures() {
        return Failures;
    }

    /**
     * failures��ݒ肷��B
     * <br>
     * @param failures String
     */
    public void setFailures(String failures) {
        Failures = failures;
    }

    /**
     * skipped��߂��B
     * <br>
     * @return  skipped
     */
    public String getSkipped() {
        return Skipped;
    }

    /**
     * skipped��ݒ肷��B
     * <br>
     * @param skipped String
     */
    public void setSkipped(String skipped) {
        Skipped = skipped;
    }

    /**
     * successRate��߂��B
     * <br>
     * @return  successRate
     */
    public String getSuccessRate() {
        return SuccessRate;
    }

    /**
     * successRate��ݒ肷��B
     * <br>
     * @param successRate String
     */
    public void setSuccessRate(String successRate) {
        SuccessRate = successRate;
    }

    /**
     * tests��߂��B
     * <br>
     * @return  tests
     */
    public String getTests() {
        return Tests;
    }

    /**
     * tests��ݒ肷��B
     * <br>
     * @param tests String
     */
    public void setTests(String tests) {
        Tests = tests;
    }

    /**
     * time��߂��B
     * <br>
     * @return  time
     */
    public String getTime() {
        return Time;
    }

    /**
     * time��ݒ肷��B
     * <br>
     * @param time String
     */
    public void setTime(String time) {
        Time = time;
    }
}