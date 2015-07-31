package jp.gr.java_conf.boj.app.regex;

import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;

class ReplacementField extends WhitespaceVisibleField {
//	b t n f r " ' \
	//n t " ' \ r b f
	private static final char[] escapeChars = 
	        new char[] {'n', 't', '"', '\'', '\\', 'r', 'b', 'f'};
	private static final char[] literalChars = 
		new char[] {'\n', '\t', '"', '\'', '\\', '\r', '\b', '\f'};
	
	ReplacementField() {
		this(null);
	}
	
	ReplacementField(WhitespaceVisibleFieldInfo info) {
		super(info);
		setText("\"\"");
		setCaretPosition(1);
		AbstractDocument document = (AbstractDocument)getDocument();
		document.setDocumentFilter(new QuotFilter());
		setNavigationFilter(new DotFilter());
	}
	
	String getTextAsEscapedChars() throws IllegalInputException {
		String text = getText();
		int length = text.length();
		assert (length >= 2) : "illegal state (textLength < 2)";
		int lastIndex = length - 1;
		StringBuilder buf = new StringBuilder(length);
		boolean escape = false;

		cotinueLabel:
		for (int i = 1; i < lastIndex; i++) {
			char c = text.charAt(i);
			if (escape) {
				for (int j = 0; j < escapeChars.length; j++) {
					if (c == escapeChars[j]) {
						buf.append(literalChars[j]);
						escape = false;
						continue cotinueLabel;
					}
				}
				throw new IllegalInputException(
						text.substring(1,lastIndex), i - 1, false);
			} else {
				if (c == '\\') {
					escape = true;
				} else if (c == '"') {
					throw new IllegalInputException(
							text.substring(1,lastIndex), i - 1, true);
				} else {
					buf.append(c);
				}
			}
		}
		if (escape) {
			throw new IllegalInputException(
					text.substring(1,lastIndex), lastIndex - 2, false);
		}
		return buf.toString();
	}
	
	// 最初と最後のダブルクォーテーションの前後にはキャレットが
	// こないように制御
	private class DotFilter extends NavigationFilter {
		
		public void setDot(NavigationFilter.FilterBypass fb,
                           int dot,
                           Position.Bias bias) {
			fb.setDot(adjustPosition(dot), bias);
		}
		
		
		
		public void moveDot(NavigationFilter.FilterBypass fb,
                            int dot,
                            Position.Bias bias) {
			fb.moveDot(adjustPosition(dot), bias);
		}
		
		public int getNextVisualPositionFrom(JTextComponent text,
                                             int pos,
                                             Position.Bias bias,
                                             int direction,
                                             Position.Bias[] biasRet)
		                                     throws BadLocationException {
			
			if ((pos == 1) && (direction != SwingConstants.EAST)) {
				return 1;
			}
			int lastIndex = getDocument().getLength() - 1;
			if ((pos == lastIndex) && (direction != SwingConstants.WEST)) {
				return lastIndex;
			}
			return super.getNextVisualPositionFrom(
			                     text, pos, bias, direction, biasRet);
		}
		
		private int adjustPosition(int dot) {
			if (dot == 0) {
				return 1;
			}
			int lastIndex = getDocument().getLength() - 1;
			if (dot > lastIndex) {
				return lastIndex;
			}
			return dot;
		}
	}
	
	/*
	 * DocumentFilterを実装したRegexField.TabFilterを継承。
	 * タブ \t の２文字に変換する。
	 * またテキストフィールドの最初と最後のダブルクォーテーションを
	 * 削除させない。
	 * イベントディスパッチスレッドのみで使用する。
	 */
	private class QuotFilter extends TabFilter {
		private int adjustedOffset;
		private int adjustedLength;
		            
		/**
		 * 最初と最後にあるダブルクォーテーションを削除させない。
		 */
		public void remove(FilterBypass fb,
                           int offset,
                           int length) throws BadLocationException {
			adjustPosition(offset, length);
			super.remove(fb, adjustedOffset, adjustedLength);
		}
		
		/**
		 * クリップボードからのペーストなどでタブが入力された際には
		 * 見えるように \t の２文字に変換する。<br>
		 * 改行は半角スペースに変換する。<br>
		 * 最初と最後のダブルクォーテーションを削除させない。
		 */	
		public void replace(FilterBypass fb,
	                        int offset,
	                        int length,
	                        String text,
	                        AttributeSet attrs)
			                throws BadLocationException {
			adjustPosition(offset, length);
			super.replace(fb, adjustedOffset, adjustedLength, text, attrs);
		}
		
		private void adjustPosition(int offset,
                                     int length) {
			adjustedOffset = offset;
			adjustedLength = length;
			if (adjustedLength == 0) {
				return;
			}
			if (adjustedOffset == 0) {
				adjustedOffset = 1;
				adjustedLength--;
			}
			if (adjustedLength == 0) {
				return;
			}
			
			int lastIndex = getDocument().getLength() - 1;
			if (adjustedOffset + adjustedLength - 1 >= lastIndex) {
				adjustedLength = lastIndex - adjustedOffset;
			}
		}
	}
	
	static class IllegalInputException extends Exception {
		private final String text;
		private final int position;
		private final boolean quotCaused;
		IllegalInputException(String text, 
		                      int position, 
		                      boolean quotCaused) {
			this.text = text;
			this.position = position;
			this.quotCaused = quotCaused;
		}
		int getPosition() {
			return position;
		}
		String getText() {
			return text;
		}
		boolean isQuotCaused() {
			return quotCaused;
		}
	}
}
