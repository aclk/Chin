package jp.gr.java_conf.boj.app.regex;

class MetaChar {
	static final String METACHARS_TITLE;
	static {
		METACHARS_TITLE = "METACHARS_TITLE";
	}
	final String chars;
	final String summary;
	final String example;
	
	MetaChar(String chars, String summary, String example) {
		this.chars = (chars == null) ? "" : chars;
		this.summary = (summary == null) ? "" : summary;
		this.example = example;
	}
}
