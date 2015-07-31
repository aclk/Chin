package test.xsv.csv;

public class CSVException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CSVException() {
        super();
    }

    public CSVException(Throwable cause) {
        super(cause);
    }

    public CSVException(String message) {
        super(message);
    }

    public CSVException(String message, Throwable cause) {
        super(message, cause);
    }
}