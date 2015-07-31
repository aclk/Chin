package demo.beans;

import java.sql.Timestamp;

/**
 * Bar クラス.
 *
 * @author Kentaro Ohkouchi
 * @version $Revision$ $Date$
 */
public class Bar {

    private String example;
    private Timestamp created;

    public Bar() {
        this.example = "example";
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public Bar(String example) {
        this.example = example;
        this.created = new Timestamp(System.currentTimeMillis());
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
