package demo.beans;

import java.sql.Timestamp;

/**
 * ExampleClass クラス.
 *
 * @author Kentaro Ohkouchi
 * @version $Revision$ $Date$
 */
public class ExampleClass {

	private String foo;
	private String baz;
	private String example;
	private Bar bar;
	private Timestamp created;

	public String getFoo() {
		return foo;
	}

	public void setFoo(String foo) {
		this.foo = foo;
	}

	public String getBaz() {
		return baz;
	}

	public void setBaz(String baz) {
		this.baz = baz;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public Bar getBar() {
		return bar;
	}

	public void setBar(Bar bar) {
		this.bar = bar;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}
}
