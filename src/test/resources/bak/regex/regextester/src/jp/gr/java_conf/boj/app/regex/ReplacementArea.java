package jp.gr.java_conf.boj.app.regex;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

/*
 * 半角スペース、全角スペース、タブ、改行コード(LFもしくはCRLF)を
 * イメージとして描画する縦スクロールバーを常に表示している
 * テキストエリア。<br>
 * 常に編集不可。<br>
 * このクラスにはWhitespaceImageFactoryクラスが必要<br>
 */
class ReplacementArea extends JScrollPane {
	
	private final ReplacementTextArea textArea;
	private final JViewport viewport;
	private final ReplacementAreaInfo info;
	
	/*
	 *  色とフォントの情報を保持するReplacementAreaInfoの内容で
	 *  インスタンスを生成する。
	 *  
	 * @param info ReplacementAreaInfoオブジェクト
	 */
	ReplacementArea(ReplacementAreaInfo info) {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		if (info == null) {
			throw new IllegalArgumentException("argument(info) is null !");
		}
		this.info = info;
		textArea = new ReplacementTextArea();
		textArea.setEditable(false);
		viewport = getViewport();
		viewport.setView(textArea);
	}
	
	/**
	 * テキストエリアに引数の文字列を設定する。<br>
	 * 引数がnullや空の場合は単に現在のテキストを削除することになる。
	 * 
	 * @param text テキストエリアに表示する文字列。
	 *             nullまたは空の文字列の場合はテキストエリアをクリア。
	 */
	void setText(String text) {
		textArea.setText(text);
	}
	
	/**
	 * テキストエリアに表示している文字列を返す。<br>
	 * 
	 * @return テキストエリアに表示している文字列
	 */
	String getText() {
		return textArea.getText();
	}
	
	/**
	 * 引数の範囲をハイライト表示する。<br>
	 * startは0以上でendはstart以上、なお且つそれらは
	 * ドキュメントの長さ(Document#getLength)以下でなければ
	 * 非検査例外をスローする。<br>
	 * startとendが同じ場合にはその位置を強調する。<br>
	 * 戻り値はclearHighlightメソッドでハイライトを消す場合に
	 * 引数として使用する。<br>
	 * このメソッドを複数回呼び出す事により複数のハイライトを
	 * 設定できる。<br>
	 * 複数のハイライトを設定している際にそれらの領域が重なっている
	 * 場合は後から設定されたハイライトが先に設定された
	 * ハイライトの上から描画する。<br>
	 * このメソッドで強調表示を行うとテキストエリアは編集不可に
	 * 設定されるので再度編集可能な状態にしたい場合には
	 * setEditableメソッドをtrueを引数に呼び出す。<br>
	 * このメソッドはrepaintメソッドで再描画を行う。
	 * 
	 * @param start 強調表示させる最初の文字のインデックス
	 * @param end 強調表示させる最後の文字の次のインデックス
	 * @param color ハイライト(背景色)に使用する色
	 * @exception IllegalArgumentException 引数の値が不正な場合
	 */
	Object highlight(int start, int end, Color color)
	                        throws IllegalArgumentException {
		DefaultHighlightPainter painter = new DefaultHighlightPainter(color);
		try {
			Object highlight = textArea.getHighlighter().addHighlight(
		                                               start, end, painter);
			return highlight;
		} catch (BadLocationException e) {
			throw new IllegalArgumentException(
					"(start < 0) || (start > end) || " +
					"(end > document.getLength())", e);
		}
	}
	
	/**
	 * 引数のオブジェクトに対応するハイライト表示をクリアする。<br>
	 * ハイライトされていない場合には何もしない。<br>
	 * このメソッドはrepaintメソッドで再描画を行う。
	 */
	void clearHighlight(Object highlight) {
		textArea.getHighlighter().removeHighlight(highlight);
	}
	
	/**
	 * すべてのハイライトをクリアする。<br>
	 * このメソッドはrepaintメソッドで再描画を行う。
	 */
	void clearAllHighlights() {
		textArea.getHighlighter().removeAllHighlights();
	}
	
	/**
	 * ReplacementAreaの色とフォントの情報を保持するクラス
	 */
	static final class ReplacementAreaInfo {
		final Color bgColor;
		final Color fgColor;
		final Color endLineColor;
		final Color whitespaceColor;
		final Font font;
		final int tabCount;
		
		ReplacementAreaInfo(Color bgColor, 
		                    Color fgColor, 
		                    Color endLineColor,
		                    Color whitespaceColor,
		                    Font font,
		                    int tabCount) {
			if ((bgColor == null) || (fgColor == null) || 
					(endLineColor == null) || (whitespaceColor == null) || 
					(font == null)) {
				throw new IllegalArgumentException(
						"argument must not be null !");
			}
			if (tabCount <= 0) {
				throw new IllegalArgumentException("(tabCount <= 0)");
			}
			this.bgColor = bgColor;
			this.fgColor = fgColor;
			this.endLineColor = endLineColor;
			this.whitespaceColor = whitespaceColor;
			this.font = font;
			this.tabCount = tabCount;
		}
	}
	
	class ReplacementTextArea extends JTextArea {
		private static final int TOP_LEFT_BOTTOM_MARGIN = 3;
		private static final int CRLF_IMAGE_WIDTH = 
								WhitespaceImageFactory.CR_LF_IMAGE_WIDTH;
		private static final int RIGHT_MARGIN = 
							CRLF_IMAGE_WIDTH * 2 + TOP_LEFT_BOTTOM_MARGIN;
		
		// 幅4高さがフォントのアセントCRのイメージ
		private final Image crImage; 
		
		// 幅4高さがフォントのアセントLFのイメージ
		private final Image lfImage;
		
		// 幅は半角スペースと同じで高さがアセントの半角スペースのイメージ
		private final Image spaceImage;
		
		// 幅は全角スペースと同じで高さがアセントの全角スペースのイメージ
		private final Image wspaceImage;
		
		// 幅は半角スペースと同じで高さがアセントのタブを表すイメージ
		private final Image tabImage;
		
		
		// viewToModelの引数としてフィールドの値を変え使い回す。
		private final Point startPoint = new Point();
		private final Point endPoint = new Point();
		
		// 使い回す
		private final Segment segment = new Segment();
		
		ReplacementTextArea() {
			setMargin(new Insets(TOP_LEFT_BOTTOM_MARGIN,
			                     TOP_LEFT_BOTTOM_MARGIN,
			                     TOP_LEFT_BOTTOM_MARGIN,
			                     RIGHT_MARGIN));
			setLineWrap(true);
			setTabSize(info.tabCount);
			setBackground(info.bgColor);
			setForeground(info.fgColor);
			setFont(info.font);
			
			FontMetrics fm = getFontMetrics(info.font);
			int ascent = fm.getAscent();
			int spaceWidth = fm.charWidth(' ');
			int wspaceWidth = fm.charWidth('　');

			crImage = WhitespaceImageFactory.getCRImage(
			                               info.whitespaceColor, ascent);
			lfImage = WhitespaceImageFactory.getLFImage(
			                               info.whitespaceColor, ascent);
			spaceImage = WhitespaceImageFactory.getSpaceImage(
			                   info.whitespaceColor, spaceWidth, ascent);
			wspaceImage = WhitespaceImageFactory.getWspaceImage(
			                  info.whitespaceColor, wspaceWidth, ascent);
			tabImage = WhitespaceImageFactory.getTabImage(
			                   info.whitespaceColor, spaceWidth, ascent);
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			//行末の区切り線の描画
			int endX = getWidth() - RIGHT_MARGIN;
			int originY = -getY();
			g.setColor(info.endLineColor);
			g.drawLine(endX, originY, 
					   endX, originY + getHeight() - 1);
			
			// 半角スペース 全角スペース タブ CR LF を描画
			g.setColor(info.whitespaceColor);
			drawWhitespace(g);
		}
		
		private void drawWhitespace(Graphics g) {
			Document doc = getDocument();
			int textLength = doc.getLength();
			if (textLength == 0) {
				return;
			}
			
			startPoint.x = getX();
			startPoint.y = -getY();
			endPoint.x = viewport.getWidth() - 1;
			endPoint.y = startPoint.y + viewport.getHeight() - 1;
			int start = viewToModel(startPoint);
			int end = viewToModel(endPoint);
			
			// textLengthの値は正しいが実際にはドキュメントの最後には
			// \nが勝手に加えられていてendがその改行の場合はデクリメント
			if (textLength == end) {
				end--;
			}
			
			try {
				doc.getText(start, end - start + 1, segment);
				
				char pre = '\0';
				char c = segment.first();
				drawImage(g, c, pre, start);
				pre = c;
				for (int i = start + 1; i <= end; i++) {
					c = segment.next();
					drawImage(g, c, pre, i);
					pre = c;
				}
				
			} catch (BadLocationException e) {
				// unknown cause -> not draw
			}
		}
		
		private void drawImage(Graphics g, char c, char pre, int offset) 
		                       throws BadLocationException {
			if (c <= ' ') {
				if (c == ' ') {
					drawImage(modelToView(offset), spaceImage, g);
				} else if (c == '\t') {
					drawImage(modelToView(offset), tabImage, g);
				} else if (c == '\r') {
					drawImage(modelToView(offset), crImage, g);
				} else if (c == '\n') {
					Rectangle rectangle = modelToView(offset);
					if (pre == '\r') {
						rectangle.x += CRLF_IMAGE_WIDTH;
					}
					drawImage(rectangle, lfImage, g);
				}
			} else if (c == '　') {
				drawImage(modelToView(offset), wspaceImage, g);
			}
		}
		
		private void drawImage(Rectangle rect, Image image, Graphics g) {
			g.drawImage(image, rect.x, rect.y, this);
		}
	}
	
	/*
	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame("JTextPane Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ReplacementAreaInfo info = new ReplacementAreaInfo(
				Main.getColorProperty("text.color.bg"),
				Main.getColorProperty("text.color.fg"),
				Main.getColorProperty("text.color.endLine"),
				Main.getColorProperty("text.color.whitespace"),
				Main.getFontProperty("textarea.font"),
				Main.getIntProperty("text.int.tab.count")
			);
		ReplacementArea sa = new ReplacementArea(info);
		
		//String str = "aa\r\nbb\r\n";
		String str = read();
		sa.setText(str);
		
		frame.getContentPane().add(sa);
		frame.setSize(200, 200);
		frame.setVisible(true);
	}
	
	private static String read() {
		String result = "";
		try {
			CharBuffer cs = IOUtil.readTextFile(
					new File("D:/pg/java/test.txt"));
			result = cs.toString();
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}
	*/
}
