package jp.gr.java_conf.boj.app.regex;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Segment;

/**
 * 半角・全角スペースをイメージとして描画するJTextFieldのサブクラス。<br>
 * クリップボードからコピーなど、タブの入力は \t の２文字に変換する。<br>
 * このクラスの使用(コンパイル・実行)には
 * WhitespaceImageFactoryクラスが必要<br>
 */
class WhitespaceVisibleField extends JTextField {
	
	// getPrefferedSize が返す最大横幅
	private int maxWidth;
	
	// 幅は半角スペースと同じで高さがアセントの半角スペースのイメージ
	private final Image spaceImage;
	
	// 幅は全角スペースと同じで高さがアセントの全角スペースのイメージ
	private final Image wspaceImage;
	
	// ドキュメントから文字列を取得する際に使い回す
	private final Segment segment = new Segment();
	
	/**
	 * ルックアンドフィールデフォルトの色とフォントのまま
	 * インスタンスを生成する。<br>
	 * WhitespaceImageFactory.WHITESPACE_IMAGE_COLORが
	 * ホワイトスペースのイメージの描画色として使用される。<br>
	 */
	WhitespaceVisibleField() {
		this(null);
	}
	
	/**
	 * 色とフォントに関する情報を保持する
	 * WhitespaceVisibleFieldInfoオブジェクトを引数に
	 * インスタンスを生成する。<br>
	 * infoがnullの場合やinfoのフィールドがnullの場合には
	 * ルックアンドフィールのデフォルトの色が使用される。<br>
	 * infoがnullの場合やinfoのwhitespaceColorがnullの場合には
	 * WhitespaceImageFactory.WHITESPACE_IMAGE_COLORが
	 * ホワイトスペースのイメージの描画色として使用される。<br>
	 * 
	 * @param info 色とフォントに関する情報を保持する
	 *             WhitespaceVisibleFieldInfoオブジェクト。
	 *             nullの場合にはデフォルトの色とフォントが使用される。
	 */
	WhitespaceVisibleField(WhitespaceVisibleFieldInfo info) {
		Color whitespaceColor = 
            WhitespaceImageFactory.WHITESPACE_IMAGE_COLOR;
		if (info != null) {
			if (info.bgColor != null) {
				setBackground(info.bgColor);
			}
			if (info.fgColor != null) {
				setForeground(info.fgColor);
			}
			if (info.caretColor != null) {
				setCaretColor(info.caretColor);
			}
			if (info.font != null) {
				setFont(info.font);
			}
			if (info.whitespaceColor != null) {
				whitespaceColor = info.whitespaceColor;
			}
		}

		FontMetrics fm = getFontMetrics(getFont());
		int ascent = fm.getAscent();
		int spaceWidth = fm.charWidth(' ');
		int wspaceWidth = fm.charWidth('　');
		spaceImage = WhitespaceImageFactory.getSpaceImage(
		                       whitespaceColor, spaceWidth, ascent);
		wspaceImage = WhitespaceImageFactory.getWspaceImage(
		                       whitespaceColor, wspaceWidth, ascent);

		Document document = getDocument();
		if (!(document instanceof AbstractDocument)) {
			throw new IllegalStateException(
				"お使いのJAVA環境(JRE あるいはルックアンドフィール)では" +
				"JTextFieldのDocumentがAbstractDocumentを継承していない為" +
				"このプログラムは正しく実行できません。");
		}
		((AbstractDocument) document).setDocumentFilter(new TabFilter());
	}
	
	void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public Dimension getPreferredSize() {
		Dimension dimension = super.getPreferredSize();
		if ((maxWidth == 0) || (maxWidth >= dimension.width)) {
			return dimension;
		}
		
		System.out.println(dimension);//TODO test
		return new Dimension(maxWidth, dimension.height);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawWhitespace(g);
	}
	
	private void drawWhitespace(Graphics g) {
		Document doc = getDocument();
		int textLength = doc.getLength();
		if (textLength == 0) {
			return;
		}
		try {
			doc.getText(0, textLength, segment);
			drawImage(g, segment.first(), 0);
			for (int i = 1; i < textLength; i++) {
				drawImage(g, segment.next(), i);
			}
		} catch (BadLocationException e) {
			// never
		}
		
	}
	
	private void drawImage(Graphics g, char c, int offset) 
	                       throws BadLocationException {
		if (c == ' ') {
			drawImage(modelToView(offset), spaceImage, g);
		} else if (c == '　') {
			drawImage(modelToView(offset), wspaceImage, g);
		}
	}
	
	private void drawImage(Rectangle rect, Image image, Graphics g) {
		g.drawImage(image, rect.x, rect.y, this);
	}
	
	/**
	 * WhitespaceVisibleFieldの色とフォントの情報を保持するクラス
	 */
	static final class WhitespaceVisibleFieldInfo {
		final Color bgColor;
		final Color fgColor;
		final Color caretColor;
		final Color whitespaceColor;
		final Font font;
		
		/**
		 * 引数はnullでもよい。<br>
		 * nullのフィールドの場合には
		 * ルックアンドフィールのデフォルトの色が使用される。<br>
		 * whitespaceColorがnullの場合には
		 * WhitespaceImageFactory.WHITESPACE_IMAGE_COLORが使用される。<br>
		 * 
		 * @param bgColor テキストフィールドの背景色
		 * @param fgColor テキストフィールドのテキストの色
		 * @param caretColor テキストフィールドのキャレットの色
		 * @param whitespaceColor ホワイトスペースのイメージの描画色
		 * @param font テキストフィールドのフォント
		 */
		WhitespaceVisibleFieldInfo(Color bgColor, 
		                           Color fgColor, 
		                           Color caretColor,
		                           Color whitespaceColor,
		                           Font font) {
			
			this.bgColor = bgColor;
			this.fgColor = fgColor;
			this.caretColor = caretColor;
			this.whitespaceColor = whitespaceColor;
			this.font = font;
		}
	}
	
	/*
	 * クリップボードからのペーストなどでタブが入力された際には
	 * 見えるように \t の２文字に変換する。
	 * 改行はJTextFieldに倣って半角スペースに変換する。
	 */
	protected class TabFilter extends DocumentFilter {
		StringBuilder buffer = new StringBuilder(64);
		
		public void replace(FilterBypass fb,
                            int offset,
                            int length,
                            String text,
                            AttributeSet attrs)
		                    throws BadLocationException {
			if (text == null) {
				super.replace(fb, offset, length, text, attrs);
				return;
			}
			
			int textLength = text.length();
			if (textLength == 0) {
				super.replace(fb, offset, length, text, attrs);
				return;
			}
			
			buffer.setLength(0);
			boolean isReplaced = false;
			for (int i = 0; i < textLength; i++) {
				char c = text.charAt(i);
				if (c == '\t') {
					buffer.append("\\t");
					isReplaced = true;
				} else if (c == '\n') {
					// AbstractDocument#insertStringを
					// PlainDocumentがオーバーライドしているが
					// ドキュメントフィルタを付けている場合には
					// PlainDocument#insertStringはフィルタリングの後に
					// 呼び出されそうなものなのに呼び出されない為
					// PlainDocument#insertStringで行っている
					// 改行を半角スペースに変換する処理をここで行う。
					buffer.append(' ');
					isReplaced = true;
				} else {
					buffer.append(c);
				}	
			}	
			
			if (isReplaced) {
				super.replace(fb, offset, length, buffer.toString(), attrs);
			} else {
				super.replace(fb, offset, length, text, attrs);
			}
		}
		
	}
}
