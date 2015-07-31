package jp.gr.java_conf.boj.app.regex;

class Tutorial {
	final String caption;
	final String example;
	Tutorial(String caption, String example) {
		if ((caption == null) || (caption.length() == 0) ||
				(example == null) || (example.length() == 0)) {
			throw new IllegalArgumentException(
					"argument is null or empty !");
		}
		this.caption = caption;
		this.example = example;
	}
	
}
