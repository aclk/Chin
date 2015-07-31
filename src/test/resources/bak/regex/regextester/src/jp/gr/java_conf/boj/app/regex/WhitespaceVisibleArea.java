package jp.gr.java_conf.boj.app.regex;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position;
import javax.swing.text.Segment;

/**
 * 半角スペース、全角スペース、タブ、改行コード(LFもしくはCRLF)を
 * イメージとして描画するテキストエリア。<br>
 * 縦スクロールバーを常に表示し、ラインラップを行い、横スクロールバーは
 * 表示されない。<br>
 * 領域と色を指定して強調表示するhighlightメソッドと
 * 指定した領域以外の背景色を指定した色にするregionメソッドを持つ。<br>
 * highlightやregionメソッドで強調表示をしていなければ
 * 改行モードはLFとCRLFに変更可能(CRのみはサポートしていない)。<br>
 * このクラスの使用(コンパイル・実行)には
 * WhitespaceImageFactoryクラスが必要<br>
 */
class WhitespaceVisibleArea extends JScrollPane {
	
	private final WhitespaceVisibleTextArea textArea;
	
	/**
	 * 引数のWhitespaceVisibleAreaInfoオブジェクトの内容で
	 * インスタンスを生成する。<br>
	 * WhitespaceVisibleAreaInfoオブジェクトは色とフォントの情報を
	 * 保持する。<br>
	 * 引数のWhitespaceVisibleAreaInfoオブジェクトがnullの場合や
	 * WhitespaceVisibleAreaInfoオブジェクトのフィールドがnullの場合は
	 * ルックアンドフィールデフォルトの色とフォントが使用される。<br>
	 * whitespaceColorとendLineColorに関してはデフォルトの色として
	 * WhitespaceImageFactoryクラスのWHITESPACE_IMAGE_COLORと
	 * ENDLINE_COLORが適用される。<br>
	 *  
	 * @param textAreaInfo WhitespaceVisibleAreaInfoオブジェクト
	 */
	WhitespaceVisibleArea(WhitespaceVisibleAreaInfo textAreaInfo) {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textArea = new WhitespaceVisibleTextArea(textAreaInfo);
		getViewport().setView(textArea);
	}
	
	/**
	 * テキストエリアが編集可能ならtrueを返し、編集不可ならfalseを返す<br>
	 * highlightメソッドやregionメソッドで強調表示している場合は
	 * 編集不可となる。<br>
	 * このメソッドがfalseを返す状態ではsetCRLFメソッドは使用できない。
	 * 
	 * @return テキストエリアが編集可能ならtrue編集不可ならfalse
	 */
	boolean isEditable() {
		return textArea.isEditable();
	}
	
	/**
	 * 引数がtrueならテキストエリアを編集可能し、falseなら
	 * 編集不可にする。<br>
	 * 引数がtrueの場合にhighlightメソッドやregionメソッドで
	 * 強調表示している場合にはそれらの強調表示はすべてクリアされる。<br>
	 * 
	 * @param editable テキストエリアを編集可能にするならtrueを
	 * 　　　　　　　　編集不可にするならfalse
	 */
	void setEditable(boolean editable) {
		if (editable) {
			clearHighlightsAndRegion();
		}
		textArea.setEditable(editable);
	}
	
	/**
	 * テキストエリアに引数の文字列を設定する。<br>
	 * 引数がnullや空の場合は単に現在のテキストを削除することになる。
	 * 
	 * @param text テキストエリアに表示する文字列。
	 *             nullまたは空の文字列の場合はテキストエリアをクリア。
	 */
	void setText(String text) {
		setEditable(true);
		textArea.clearPositionList();
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
		Object highlight = textArea.highlight(start, end, color);
		return highlight;
	}
	
	/*
	 * 引数のオブジェクトに対応するハイライト表示をクリアする。<br>
	 * ハイライトされていない場合には何もしない。<br>
	 * このメソッドはrepaintメソッドで再描画を行う。
	 */
	void clearHighlight(Object highlight) {
		textArea.clearHighlight(highlight);
	}
	
	/*
	 * すべてのハイライトをクリアする。<br>
	 * このメソッドはrepaintメソッドで再描画を行う。
	 */
	void clearAllHighlights() {
		textArea.clearAllHighlights();
	}
	
	/**
	 * 改行をCRあるいはCRLFにする。<br>
	 * CRLFにする場合にはtrueを指定し、
	 * CRにする場合にはfalseを指定する。<br>
	 * 現在の設定と同じ場合には何もしない。<br>
	 * テキストエリアが編集可能状態でない場合(強調表示している時)
	 * にこのメソッドを呼び出すとIllegalStateExceptionをスローする。<br>
	 * isEditableメソッドがtrueを返す状態でこのメソッドを使用する。
	 * 
	 * @param isCRLF 改行をCRLFにするならtrue、LFにするならfalse
	 */
	void setCRLF(boolean isCRLF) {
		if (!isEditable()) {
			throw new IllegalStateException(
				"cannot change newline mode when textarea is highlighted");
		}
		textArea.setCRLF(isCRLF);
	}
	
	/**
	 * 改行がCRLFならtrueを返し、LFならfalseを返す。
	 * 
	 * @return 改行がCRLFならtrueを返し、LFならfalseを返す。
	 */
	boolean isCRLF() {
		return textArea.isCRLF;
	}
	
	/**
	 * 引数で指定された領域以外の部分を引数の色を背景色として
	 * 塗りつぶす。<br>
	 * startは0以上でendはstart以上、なお且つそれらは
	 * ドキュメントの範囲内でなければ非検査例外をスローする。<br>
	 * startとendが同じ場合は何も強調しない。<br>
	 * このメソッドによる強調表示は１つの領域のみしか
	 * 出来ないので、このメソッドにより現在領域強調表示している
	 * 場合に、新たにこのメソッドが呼び出されると古い方は
	 * クリアされる。<br>
	 * このメソッドで強調表示を行うとテキストエリアは編集不可に
	 * 設定されるので再度編集可能な状態にしたい場合には
	 * setEditableメソッドをtrueを引数に呼び出す。<br>
	 * このメソッドはrepaintメソッドで再描画を行う。
	 * 
	 * @param start 強調表示する最初の文字のインデックス
	 * @param end 強調表示する最後の文字の次のインデックス
	 * @param color startとendで指定した領域以外を塗りつぶす背景色
	 * @exception IllegalArgumentException 引数の値が不正な場合
	 */
	void region(int start, int end, Color color) {
		if (start == end) {
			return;
		}
		textArea.region(start, end, color);
	}
	
	/**
	 * regionメソッドで領域外の背景色が変更されていればもとに戻す<br>
	 * regionメソッドで変更されていなければ何もしない。<br>
	 * 変更をもとに戻した場合はこのメソッドはrepaintメソッドで再描画を行う。
	 */
	void clearRegion() {
		textArea.clearRegion();
	}
	
	/**
	 * highlightメソッドやregionメソッドで行った強調表示があれば
	 * すべてクリアする。<br>
	 * 強調表示されていなければ何もしない。<br>
	 * 強調表示をクリアした場合にはrepaintメソッドで再描画を行う。
	 */
	void clearHighlightsAndRegion() {
		clearAllHighlights();
		clearRegion();
	}
	
	/**
	 * WhitespaceVisibleTextAreaの色とフォントの情報を保持するクラス<br>
	 * ルックアンドフィールデフォルトの色やフォントを使用したい場合には
	 * コンストラクタの引数にnullを指定する。<br>
	 */
	static final class WhitespaceVisibleAreaInfo {
		final Color bgColor;
		final Color fgColor;
		final Color caretColor;
		final Color endLineColor;
		final Color whitespaceColor;
		final Font font;
		final int tabCount;
		
		/**
		 * 引数はnullでもよく、その場合には
		 * ルックアンドフィールデフォルトの色やフォントが使用される。<br>
		 * 引数のwhitespaceColorがnullの場合には
		 * WhitespaceImageFactory.WHITESPACE_IMAGE_COLORがデフォルトとして
		 * 使用され、endLineColorがnullの場合には
		 * WhitespaceImageFactory.ENDLINE_COLORが使用される。<br>
		 * tabCountをデフォルトにしたい場合には0より小さい値(-1以下)を
		 * 指定する。
		 * 
		 * @param bgColor WhitespaceVisibleAreaの背景色
		 * @param fgColor WhitespaceVisibleAreaのテキストの色
		 * @param caretColor WhitespaceVisibleAreaのキャレットの色
		 * @param endLineColor WhitespaceVisibleAreaの末尾境界線の色
		 * @param whitespaceColor WhitespaceVisibleAreaの
		 *                        ホワイトスペースのイメージの描画色
		 * @param font WhitespaceVisibleAreaのフォント
		 * @param tabCount WhitespaceVisibleAreaのタブカウント
		 *                 デフォルトを使用するなら-1以下を指定する。
		 */
		WhitespaceVisibleAreaInfo(Color bgColor, 
		                          Color fgColor, 
		                          Color caretColor,
		                          Color endLineColor,
		                          Color whitespaceColor,
		                          Font font,
		                          int tabCount) {
			this.bgColor = bgColor;
			this.fgColor = fgColor;
			this.caretColor = caretColor;
			this.endLineColor = endLineColor;
			this.whitespaceColor = whitespaceColor;
			this.font = font;
			this.tabCount = tabCount;
		}
	}
	
	protected class WhitespaceVisibleTextArea extends JTextArea {
		
		private static final int TRIANGLE_HEIGHT = 3;
		private static final int TOP_LEFT_BOTTOM_MARGIN = TRIANGLE_HEIGHT;
		private static final int CRLF_IMAGE_WIDTH = 
								WhitespaceImageFactory.CR_LF_IMAGE_WIDTH;
		private static final int RIGHT_MARGIN = 
							CRLF_IMAGE_WIDTH * 2 + TOP_LEFT_BOTTOM_MARGIN;
		
		
		// 幅がCRLF_IMAGE_WIDTHで高さがフォントのアセントCRのイメージ
		private final Image crImage; 
		
		// 幅がCRLF_IMAGE_WIDTHで高さがフォントのアセントLFのイメージ
		private final Image lfImage;
		
		// 幅は半角スペースと同じで高さがアセントの半角スペースのイメージ
		private final Image spaceImage;
		
		// 幅は全角スペースと同じで高さがアセントの全角スペースのイメージ
		private final Image wspaceImage;
		
		// 幅は半角スペースと同じで高さがアセントのタブを表すイメージ
		private final Image tabImage;
		
		private final Color bgColor;
		private final Color endLineColor;
		
		private final List<Position> lfList = new ArrayList<Position>();
		private final List<Position> crList = new ArrayList<Position>();
		private final List<Position> spaceList = new ArrayList<Position>();
		private final List<Position> tabList = new ArrayList<Position>();
		private final List<Position> wspaceList = new ArrayList<Position>();
		
		private final Document document;
		
		// Documentから文字を読み込む際に使い回す。
		private final Segment segment = new Segment();
		
		private final PrivateHighlighter highlighter = 
												new PrivateHighlighter();
		
		// 改行を\r\nとするならtrue デフォルトは\nでfalse
		private boolean isCRLF;
		
		protected WhitespaceVisibleTextArea(WhitespaceVisibleAreaInfo info) {
			setHighlighter(highlighter);
			setMargin(new Insets(TOP_LEFT_BOTTOM_MARGIN,
			                     TOP_LEFT_BOTTOM_MARGIN,
			                     TOP_LEFT_BOTTOM_MARGIN,
			                     RIGHT_MARGIN));
			setLineWrap(true);
			
			Color whitespaceColor = 
			              WhitespaceImageFactory.WHITESPACE_IMAGE_COLOR;
			Color tempELC = WhitespaceImageFactory.ENDLINE_COLOR;
			Color tempBGC = getBackground();
			if (info != null) {
				if (info.tabCount > 0) {
					setTabSize(info.tabCount);
				}
				if (info.bgColor != null) {
					tempBGC = info.bgColor;
					setBackground(tempBGC);
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
				if (info.endLineColor != null) {
					tempELC = info.endLineColor;
				}
			}
			endLineColor = tempELC;
			bgColor =tempBGC;
			
			FontMetrics fm = getFontMetrics(getFont());
			int ascent = fm.getAscent();
			int spaceWidth = fm.charWidth(' ');
			int wspaceWidth = fm.charWidth('　');
			

			crImage = WhitespaceImageFactory.getCRImage(whitespaceColor, 
			                                            ascent);
			lfImage = WhitespaceImageFactory.getLFImage(whitespaceColor, 
			                                            ascent);
			spaceImage = WhitespaceImageFactory.getSpaceImage(
			                        whitespaceColor, spaceWidth, ascent);
			wspaceImage = WhitespaceImageFactory.getWspaceImage(
			                       whitespaceColor, wspaceWidth, ascent);
			tabImage = WhitespaceImageFactory.getTabImage(
			                        whitespaceColor, spaceWidth, ascent);
			
			document = getDocument();
			if (!(document instanceof AbstractDocument)) {
				throw new IllegalStateException(
					"お使いのJAVA環境(JRE あるいはルックアンドフィール)" +
					"ではJTextAreaのDocumentがAbstractDocumentを" +
					"継承していない為" +
					"このプログラムは正しく実行できません。");
			}
			setNavigationFilter(new DotFilter());
			((AbstractDocument) document).setDocumentFilter(
											new WhitespaceFilter());
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
		 * 複数のハイライトを設定している際に領域が重なっている
		 * 場合は後から設定したハイライトが先に設定された
		 * ハイライトの上から描画する。<br>
		 * このメソッドはrepaintメソッドで再描画を行う。
		 * 
		 * @param start ハイライトさせる最初の文字のインデックス
		 * @param end ハイライトさせる最後の文字の次のインデックス
		 * @param color ハイライト(背景色)に使用する色
		 * @exception IllegalArgumentException 引数の値が不正な場合
		 */ 
		protected Object highlight(int start, int end, Color color) {
			setEditable(false);
			setCaretPosition(start);
			HighlightInfo info = new HighlightInfo(start, end, color);
			repaintLater(info, false);
			return info; 
		}
		
		/**
		 * 引数のオブジェクトはhighlightメソッドの戻り値で
		 * このオブジェクトに対応するハイライト表示をクリアする。<br>
		 * ハイライトされていない場合には何もしない。<br>
		 * このメソッドはrepaintメソッドで再描画を行う。
		 * 
		 * @param removeKey highlightメソッドの戻り値として得た
		 *                  ハイライトを消す為のキーとなるオブジェクト
		 */
		protected final void clearHighlight(Object removeKey) {
			highlighter.eraseHighlight(removeKey);
			repaint();
		}
		
		/**
		 * すべてのハイライトをクリアする。<br>
		 * このメソッドはrepaintメソッドで再描画を行う。
		 */
		protected final void clearAllHighlights() {
			highlighter.eraseAllHighlight();
			repaint();
		}
		
		/**
		 * 引数で指定された領域以外の部分を引数の色を背景色として
		 * 塗りつぶす。<br>
		 * startは0以上でendはstart以上、なお且つそれらは
		 * ドキュメントの範囲内でなければ非検査例外をスローする。<br>
		 * startとendが同じ場合は何も強調しない。<br>
		 * このメソッドによる強調表示は１つの領域のみしか
		 * 出来ないので、このメソッドにより現在領域強調表示している
		 * 場合に、新たにこのメソッドが呼び出されると古い方は
		 * クリアされる。<br>
		 * このメソッドで強調表示を行うとテキストエリアは編集不可に
		 * 設定されるので再度編集可能な状態にしたい場合には
		 * setEditableメソッドをtrueを引数に呼び出す。<br>
		 * このメソッドはrepaintメソッドで再描画を行う。
		 * 
		 * @param start 強調表示する最初の文字のインデックス
		 * @param end 強調表示する最後の文字の次のインデックス
		 * @param color startとendで指定した領域以外を塗りつぶす背景色
		 * @exception IllegalArgumentException 引数の値が不正な場合
		 */
		protected final void region(int start, int end, Color color) {
			setBackground(color);
			setEditable(false);
			setCaretPosition(start);
			HighlightInfo info = new HighlightInfo(start, end, bgColor);
			repaintLater(info, true);
		}
		
		/**
		 * regionメソッドで領域外の背景色が変更されていればもとに戻す
		 * 変更されていなければ何もしない。<br>
		 * このメソッドはrepaintメソッドで再描画を行う。
		 */
		protected final void clearRegion() {
			highlighter.eraseRegion();
			setBackground(bgColor);
			repaint();
		}
		
		/**
		 * 改行をCRあるいはCRLFにする。<br>
		 * CRLFにする場合にはtrueを指定し、
		 * CRにする場合にはfalseを指定する。<br>
		 * 現在の設定と同じ場合には何もしない。<br>
		 * テキストエリアが編集可能状態でない場合(強調表示している時)
		 * にこのメソッドを呼び出すとIllegalStateExceptionをスローする。<br>
		 * このメソッドは改行の設定が変更される場合にのみ
		 * repaintメソッドで再描画を行う。
		 * 
		 * @param isCRLF 改行をCRLFにするならtrue、LFにするならfalse
		 */
		protected final void setCRLF(boolean isCRLF) {
			if (this.isCRLF == isCRLF) {
				return;
			}
			clearPositionList();
			this.isCRLF = isCRLF;
			this.setText(this.getText());
			repaint();
		}
		
		// 引数の位置の文字を返す。
		// ドキュメントの最後に加えられた\nの場合は空文字を返す。
		private char offset2char(int offset) throws BadLocationException {
			if (offset == document.getLength()) {
				return '\0';
			}
			document.getText(offset, 1, segment);
			return segment.first();
		}
		
		/*
		 * Highlighter#paintメソッド内での
		 * JTextComponent#modelToViewメソッドが不正な値を返す場合がある為。
		 * 例えばドキュメントの内容が \ta の２文字の場合に
		 * Highlighter#paintメソッド内で modelToView(1) を呼び出すと
		 * 戻り値の値がテキストエリアの左マージン分少ない場合がある。
		 * これは計算する行にタブが含まれている場合に発生する。
		 * 再描画の過程でpaintComponentまで行くと modelToView は
		 * 常に正しい値を返しているよう。
		 * 原因は不明だが setEditable など他に再描画を行うメソッドとは
		 * 別に新たに描画させれば正しく描画できるようなので
		 * EventQueue#invokeLater を使う。
		 * DefaultHighlighterでも同じような再描画の方法をとっていたので
		 * あながち間違った方法ではないかも
		 */
		private void repaintLater(final HighlightInfo info, 
		                          final boolean isRegion) {
			EventQueue.invokeLater(
				new Runnable() {
					public void run() {
						try {
							Rectangle r0 = modelToView(info.start);
							Rectangle r1 = modelToView(info.end);
							int width = 
							    WhitespaceVisibleTextArea.this.getWidth();
							if (isRegion) {
								highlighter.setRegion(info);
							} else {
								highlighter.addHighlight(info);
							}
							WhitespaceVisibleTextArea.this.repaint(
								0, r0.y, width, r1.y - r0.y + r1.height);
						} catch (BadLocationException e) {}
					}
				}
			);
		}
		
		private void clearPositionList() {
			lfList.clear();
			crList.clear();
			spaceList.clear();
			wspaceList.clear();
			tabList.clear();
		}
		
		private void addPosition(List<Position> list, int offset) 
		                         throws BadLocationException {
			Position position = document.createPosition(offset);
			list.add(position);
			
		}
		
		private void removePosition(List<Position> list, int offset) {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Position p = list.get(i);
				if (offset == p.getOffset()) {
					list.remove(i);
					break;
				}
			}
		}
		
		// ホワイトスペースのイメージの描画
		private void drawWhitespace(Graphics g) {
			drawWhitespace(g, lfImage, lfList);
			drawWhitespace(g, crImage, crList);
			drawWhitespace(g, spaceImage, spaceList);
			drawWhitespace(g, wspaceImage, wspaceList);
			drawWhitespace(g, tabImage, tabList);
		}
		
		// ホワイトスペースのイメージの描画。
		// 右マージンにはみ出たCR LFのイメージ部分が
		// ハイライトされている場合にはそれも描画
		private void drawWhitespace(Graphics g, 
		                            Image image,
		                            List<Position> list) {
			if (list.isEmpty()) {
				return;
			}
			Iterator<Position> iter = list.iterator();
			try {
				while (iter.hasNext()) {
					Position p = iter.next();
					int offset = p.getOffset();
					Rectangle rect = modelToView(offset);
					
					if (image == crImage) {
						if (rect.x > getWidth() - RIGHT_MARGIN - 
						                          CRLF_IMAGE_WIDTH) {
							highlighter.drawOutOfBorder(g, offset, rect);
						}
					} else if (image == lfImage) {
						if (isCRLF && (offset > 0)) {
							if (offset2char(offset - 1) == '\r') {
								rect.x += CRLF_IMAGE_WIDTH;
							}
						}
						if (rect.x > getWidth() - RIGHT_MARGIN - 
		                                          CRLF_IMAGE_WIDTH) {
							highlighter.drawOutOfBorder(g, offset, rect);
						}
					} 
					
					g.drawImage(image, rect.x, rect.y, this);
				}
			} catch (BadLocationException e) {
				throw new RuntimeException(
					"プログラムのバグにより予想外の例外が発生しました。\n" +
					"ホワイトスペースの位置を保持するPositionが" +
					"不正な値を返しました。", e);
			}
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// 行末の区切り線の描画
			int endX = getWidth() - RIGHT_MARGIN;
			int originY = -getY();
			g.setColor(endLineColor);
			g.drawLine(endX, originY, 
					   endX, originY + getHeight() - 1);
			
			// 半角スペース 全角スペース タブ CR LF を描画
			//g.setColor(whitespaceColor);
			drawWhitespace(g);
			
			// 位置の強調表示
			highlighter.highlightPoint(g);
		}
		
		/*
		 * Documentにフィルタをかけて描画するホワイトスペースの
		 * Positonオブジェクトのリストを管理する。
		 * insertStringはIMEによる確定前の文字(列)の入力の際に
		 * 呼び出されるが、半角スペース、全角スペース、タブ
		 * LF、CR を挿入することはないのでオーバーライドしない。
		 * removeもIMEによる確定前の文字(列)の入力の際の呼び出しで
		 * 前述のホワイトスペースが削除されることはない。
		 * ただし通常の削除とIMEによる削除の区別はできないので
		 * IMEによる削除も無駄ではあるが通常の削除と同様の
		 * 処理を行う。
		 * このフィルタのメソッドはWhitespaceVisibleTextAreaのフィールド
		 * documentに関連してのみ呼び出される。
		 */
		private class WhitespaceFilter extends DocumentFilter {
			// 使い回すStringBuilder
			private StringBuilder charBuf = new StringBuilder(2048);
			
			
			public void insertString(FilterBypass fb, 
			                         int offset, 
			                         String string, 
			                         AttributeSet attr) 
			                         throws BadLocationException {
				super.insertString(fb, offset, string, attr);
				//showPosition();//test
				repaint();
			}


			/*
			 * 削除文字列にホワイトスペースがあればPositionリストから
			 * 削除する。
			 * LFの削除は前の位置にCRがあればそれも削除する。
			 */
			public void remove(FilterBypass fb,
	                           int offset,
	                           int length) throws BadLocationException {
				/*
				System.out.println("remove offset:" +
						offset + " length:" + length);// test
				*/
				if (length == 0) {
					return;
				}
				checkAndRemove(fb, offset, length);
				//showPosition();//test
				repaint();
			}
			
			
			/*
			 * 半角スペース、全角スペース、タブ、LF、CR に関する
			 * 挿入、削除をPositionリストに反映させる。
			 * isCRLFがtrueの場合にはLFの挿入の際に前の位置に
			 * CRがなければCRLFの挿入にし、
			 * LFの削除は前の位置にCRがあればそれも削除する。
			 * offsetの位置からlength分文字を削除してから
			 * offsetの位置にtextを挿入する。
			 * lengthが0の場合は何も削除されない。
			 * textはnullの場合がある。
			 * 
			 * offsetが0の場合に位置0を保持するPositionオブジェクトが
			 * ある場合には挿入する文字列の末尾その
			 * Positionオブジェクトのホワイトスペースを
			 * 加え、そのPositionオブジェクトを削除してから
			 * 文字列を挿入する必要がある。
			 * Positionオブジェクトを削除することによって位置0になる
			 * Positionオブジェクトがある場合には再帰的に
			 * 処理しなければならない。
			 * これは位置が0のPositionオブジェクトは0の位置に文字を
			 * 挿入しても保持する位置はずっと0のままであるから。
			 * これはドキュメントにも記述があるのでPositionの仕様。
			 * 
			 */
			public void replace(FilterBypass fb,
                                int offset,
                                int length,
                                String text,
                                AttributeSet attrs)
                                throws BadLocationException {
				/*
				System.out.println("replace  offset:" +
						offset + " length:" + length + 
						" text:" + text);//test
				*/
				
				// offsetの位置からlength分の文字を削除
				int newOffset = checkAndRemove(fb, offset, length);
				
				// 挿入する文字列が無ければリターン
				if ((text == null) || (text.length() == 0)) {
					return;
				}
				
				// ドキュメントの最初に文字列を挿入する場合に
				// 最初の文字が CR CL タブ 半角スペース 全角スペースの
				// 場合にはそれらを削除し、削除した文字を挿入文字列の
				// 末尾に加えた文字列を挿入する。
				// こうしないとポジションリストが壊れる。
				String newText = text;
				int removedLength = 0;
				int docLength = document.getLength();
				if ((newOffset == 0) && (docLength > 0)) {
					charBuf.setLength(0);
					charBuf.append(newText);
					
					int next = 0;
					while (next < docLength) {
						char c = offset2char(next++);
						if ((c == '\n') || (c == ' ') || (c == '\t') ||
						                   (c == '　') || (c == '\r')) {
							charBuf.append(c);
							removedLength++;
						} else {
							break;
						}
					}
					checkAndRemove(fb, 0, removedLength);
					newText = charBuf.toString();
				}
				// 挿入文字列の改行文字を必要に応じて変更
				newText = (isCRLF) ? appendCRLF(newText, newOffset)
				                   : removeCR(newText);
				
				fb.insertString(newOffset, newText, attrs);
				checkInsertedChars(newOffset, newText);
				
				// 末尾に加えた文字の分だけキャレットを戻す
				if (removedLength > 0) {
					setCaretPosition(getCaretPosition() - removedLength);
				}
				
				//showPosition();//test
				repaint();
			}
			
			
			
			private String removeCR(String text) {
				charBuf.setLength(0);
				int length = text.length();
				for (int i = 0; i < length; i++) {
					char c = text.charAt(i);
					if (c != '\r') {
						charBuf.append(c);
					}
				}
				
				return (charBuf.length() == length) 
				        ? text : charBuf.toString();
			}
			
			// 引数のtextのLFの前にCRが無ければCRを挿入する。
			// CRの後にLFが無い場合はLFを加える。
			// 加える文字がLFの場合、その前にCRが無ければCRを挿入
			// LFがテキストの最後の文字の場合にはドキュメントの
			// その次がLFの場合にはその前にCRを加える
			// 加える文字がCRの場合には次がLFでなければ
			// LFを加える。
			// CRがテキストの最初の場合にドキュメントの前の
			// 文字がCRの場合にはLFを挿入
			private String appendCRLF(String text, int offset) 
			                          throws BadLocationException {
				int length = text.length();
				if (length == 0) {
					return text;
				}
				charBuf.setLength(0);
				char c = text.charAt(0);
				
				if (c == '\n') {
					if (offset == 0) {
						charBuf.append('\r');
					} else {
						if (offset2char(offset - 1) != '\r') {
							charBuf.append('\r');
						} 
					} 
					charBuf.append('\n');
					
				} else if (c == '\r') {
					if (offset > 0) {
						if (offset2char(offset - 1) == '\r') {
							charBuf.append('\n');
						}
					}
					charBuf.append('\r');
					if (length > 1) {
						if (text.charAt(1) != '\n') {
							charBuf.append('\n');
						}
					}
				} else {
					charBuf.append(c);
				}
				
				for (int i = 1; i < length; i++) {
					c = text.charAt(i);
					if (c == '\n') {
						if (text.charAt(i - 1) != '\r') {
							charBuf.append('\r');
						}
						charBuf.append('\n');
					} else if (c == '\r') {
						charBuf.append('\r');
						int nextIndex = i + 1;
						if (nextIndex < length) {
							if (text.charAt(nextIndex) != '\n') {
								charBuf.append('\n');
							}
						}
					} else {
						charBuf.append(c);
					}
				}
				char lastChar = charBuf.charAt(charBuf.length() - 1);
				int docLength = document.getLength();
				if (lastChar == '\n') {
					if ((docLength > 0) && (offset < docLength)) {
						if (offset2char(offset) == '\n') {
							charBuf.append('\r');
						}
					}
				} else if (lastChar == '\r') {
					if (docLength == 0) {
						charBuf.append('\n');
					} else if (offset2char(offset) != '\n') {
						charBuf.append('\n');
					}
				}
				if (charBuf.length() == length) {
					return text;
				}
				String newText = charBuf.toString();
				if (length > 2048) {
					charBuf = new StringBuilder(2048);
				}
				return newText;
			}
			
			// 引数の挿入文字列を走査してホワイトスペースの
			// ポジションリストを更新する。textはnullではない
			private void checkInsertedChars(int offset, String text) 
			                                throws BadLocationException {
				/*
				System.out.println("checkInsertedChars offset:" +
						offset + " text:" + text);//test
				*/		
				int length = text.length();
				for (int i = 0; i < length; i++) {
					char c = text.charAt(i);
					if (c <= ' ') {
						if (c == ' ') {
							addPosition(spaceList, offset + i);
						} else if (c == '\n') {
							addPosition(lfList, offset + i);
						} else if (c == '\t') {
							addPosition(tabList, offset + i);
						} else if (c == '\r') {
							addPosition(crList, offset + i);
						}
					} else if (c == '　') {
						addPosition(wspaceList, offset + i);
					} 
				}
			}
			
			// 戻り値はoffsetでoffset - 1のCRを削除する場合には
			// offsetをデクリメントして返す。
			// 引数のlengthは0の場合がある
			private int checkAndRemove(FilterBypass fb, 
			                           int offset, 
			                           int length)
			                           throws BadLocationException {
				/*
				System.out.print("checkAndRemove offset:" +
						offset + " length:" + length);//test
				*/
				if (length == 0) {
					return offset;
				}
				
				int newOffset = offset;
				int newLength = length;
				char pre = '\0';
				char c;
				if (offset > 0) {
					document.getText(offset - 1, length + 1, segment);
					pre = segment.first();
					c = segment.next();
				} else {
					document.getText(offset, length, segment);
					c = segment.first();
				}
				if (c == '\n') {
					if (pre == '\r') {
						newOffset--;
						newLength++;
						removePosition(crList, offset - 1);
					}
					removePosition(lfList, offset);
				} else {
					checkRemovedList(c, offset);
				}
				for (int i = 1; i < length; i++) {
					c = segment.next();
					checkRemovedList(c, offset + i);
				}
				if (c == '\r') {
					int next = offset + length;
					if (next < document.getLength()) {
						if (offset2char(next) == '\n') {
							newLength++;
							removePosition(lfList, next);
						}
					}
					removePosition(crList, next - 1);
				}
				fb.remove(newOffset, newLength);
				
				return newOffset;
			}
			
			private void checkRemovedList(char c, int offset) {
				if (c <= ' ') {
					if (c == ' ') {
						removePosition(spaceList, offset);
					} else if (c == '\n') {
						removePosition(lfList, offset);
					} else if (c == '\t') {
						removePosition(tabList, offset);
					} else if (c == '\r') {
						removePosition(crList, offset);
					}
				} else if (c == '　') {
					removePosition(wspaceList, offset);
				}
			}
			
			/*
			// for test 
			private void showBytes(String label, String str) {
				byte[] bytes = str.getBytes();
				System.out.print(label);
				System.out.print(":");
				for(byte b : bytes) {
					System.out.print(" " + b);
				}
				System.out.println();
			}
			
			// for test 
			private void showPosition(List<Position> list) {
				for (Position p : list) {
					System.out.print(" " + p.getOffset());
				}
			}
			
			
			// for test 
			private void showPosition() {
				//		lfList, crList, spaceList, wspaceList, tabList
				System.out.print("lfList:");
				showPosition(lfList);
				System.out.println();
				System.out.print("cfList:");
				showPosition(crList);
				System.out.println();
				System.out.print("spaceList:");
				showPosition(spaceList);
				System.out.println();
				System.out.print("wspaceList:");
				showPosition(wspaceList);
				System.out.println();
				System.out.print("tabList:");
				showPosition(tabList);
				System.out.println();
			}
			*/
		}
		
		// 改行がCRLFの場合にキャレットの(見た目ではなくデータ上の)
		// 位置がCRとLFの間にこないようにするフィルタ
		private class DotFilter extends NavigationFilter {
			
			// dotがCRとLFの間にある場合にはCRの前に移動。
			// CRの前にあるキャレットを矢印キーにより
			// CRの後ろに動かした場合はgetNextVisualPositionFromで
			// 制御する。
			private int checkDot(int dot) {
				if (!isCRLF) {
					return dot;
				}
				if ((dot == 0) || (dot >= document.getLength())) {
					return dot;
				}
				
				try {
					char c = offset2char(dot - 1);
					return (c == '\r') ? --dot : dot;
					
				} catch (BadLocationException e) {
					throw new AssertionError(e);
				}
			}
			
			/**
			 * dotがCRとLFの間にある場合にはCRの前に移動
			 */
			public void setDot(NavigationFilter.FilterBypass fb,
	                           int dot,
	                           Position.Bias bias) {
				//System.out.println("setDot dot:" + dot + " bias:" + bias);
				fb.setDot(checkDot(dot), bias);
			}
			
			/**
			 *  dotがCRとLFの間にある場合にはCRの前に移動
			 */
			public void moveDot(NavigationFilter.FilterBypass fb,
                                int dot,
                                Position.Bias bias) {
				//System.out.println("moveDot dot:" + dot + " bias:" + bias);
				fb.moveDot(checkDot(dot), bias);
			}
			
			/**
			 * CRの前にあるキャレットを矢印キーによりCRの後ろに
			 * 動かした場合は次のLFの後ろに１つ移動させる。
			 */
			public int getNextVisualPositionFrom(JTextComponent text,
                                                 int pos,
                                                 Position.Bias bias,
                                                 int direction,
                                                 Position.Bias[] biasRet)
			                                     throws BadLocationException {
				
				if ((!isCRLF) || (direction != SwingConstants.EAST) ||
				                 (pos >= document.getLength())) {
					return super.getNextVisualPositionFrom(
									text, pos, bias, direction, biasRet);
				}
				
				int newPos = (offset2char(pos) == '\r') ? pos + 1 : pos;
				
				return super.getNextVisualPositionFrom(
								text, newPos, bias, direction, biasRet);
			}
		}
		
		private class HighlightInfo {
			final int start;
			final int end;
			final Color color;
			
			HighlightInfo(int start, int end, Color color) { 
				if ((start < 0) || (start > end) || 
						(end > document.getLength())) {
					throw new IllegalArgumentException(
						"(start < 0) || (start > end) || " +
						"(end > document.getLength())");
				}
				if (color == null) {
					throw new IllegalArgumentException(
							"argument(color) is null !");
				}
				this.start = start;
				this.end = end;
				this.color = color;
			}
		}
		
		// デフォルトのハイライタ(javax.swing.plaf.basic.BasicTextUIが
		// 使っているjavax.swing.text.DefaultHighlighter)は複数の
		// ハイライタを設定した際のハイライトの描画順番に関して
		// ドキュメントで何も保証していない(動作としては貼り付け順)為
		// このハイライタを使用する。
		private class PrivateHighlighter implements Highlighter {

			List<HighlightInfo> infoList = new ArrayList<HighlightInfo>();
			HighlightInfo regionInfo;
			
			void addHighlight(HighlightInfo info) {
				infoList.add(info);
			}
			
			void eraseHighlight(Object info) {
				infoList.remove(info);
			}
			
			void eraseAllHighlight() {
				infoList.clear();
			}
			
			// (start == end) の場合は何もしない。
			void setRegion(HighlightInfo info) {
				if (info.start == info.end) {
					return;
				}
				regionInfo = info;
			}
			
			void eraseRegion() {
				regionInfo = null;
			}
			
			public void paint(Graphics g) {
				if (regionInfo != null) {
					paint(g, regionInfo);
				}
				if (!infoList.isEmpty()) {
					for (HighlightInfo info : infoList) {
						paint(g, info);
					}
				}
			}
			
			// マージンにはみ出た分の描画は
			// WhitespaceVisibleTextArea#paintComponent
			// がホワイトスペースのイメージの描画を行う直前に
			// drawOutOfBorderメソッドを呼び出して行う。
			// xはマージンを除いた描画のｘ座標の起点
			// widthはマージンを除いたテキストエリアの幅
			private void paint(Graphics g, HighlightInfo info) { 
				int p0 = info.start;
				int p1 = info.end;
				Color color = info.color;
				
				// 位置の強調は行末のボーダラインの描画の後に行う必要が
				// ある為WhitespaceVisibleTextArea#paintComponentから
				// highlightPointを呼び出して行う。
				if (p0 == p1) {
					return;
				}
				
				////////// 以下 (p0 != p1) の場合の描画 //////////
				Rectangle r0;
				Rectangle r1;
				try {
					r0 = modelToView(p0);
					r1 = modelToView(p1);
					//System.out.println("r0:" + r0 + "  r1:" + r1);
					if (isCRLF && (offset2char(p0) == '\n')) {
						r0.x += (CRLF_IMAGE_WIDTH);
					}
					if (isCRLF && (offset2char(p1) == '\n')) {
						r1.x += CRLF_IMAGE_WIDTH;
					}
				} catch (BadLocationException e) {
					//cannot paint
					return; 
				}
				
				int x = TOP_LEFT_BOTTOM_MARGIN;
				int width = getWidth() - TOP_LEFT_BOTTOM_MARGIN - RIGHT_MARGIN;
				g.setColor(color);
				if (r0.y == r1.y) {
					// 同じ行にp0とp1がある場合
					g.fillRect(r0.x, r0.y, r1.x - r0.x, r0.height);
				} else {
					// 複数行にまたがって強調する場合
					g.fillRect(r0.x, r0.y, 
					           x + width - r0.x, r0.height);
					
					if (r0.y + r0.height != r1.y) {
						g.fillRect(x, r0.y + r0.height,
								   width, r1.y - r0.y - r0.height);
					} 
					g.fillRect(x, r1.y, r1.x - x, r1.height);
				}
			}
			
			// startとendが同じ場合の強調表示の描画
			private void highlightPoint(Graphics g) {
				for (HighlightInfo info : infoList) {
					if (info.start != info.end) {
						continue;
					}
					try {
						Rectangle rect = modelToView(info.start);
						if (isCRLF && (offset2char(info.start) == '\n')) {
							rect.x += (CRLF_IMAGE_WIDTH + 1);
						}
						g.setColor(info.color);
						int x = rect.x - TRIANGLE_HEIGHT;
						g.translate(x, rect.y);
						for (int i = 0; i < TRIANGLE_HEIGHT; i++) {
							int y = TRIANGLE_HEIGHT - 1- i ;
							g.drawLine(y, y, TRIANGLE_HEIGHT - 1 + i, y);
						}
						g.translate(-x, -rect.y);
						
					} catch (BadLocationException e) {
						//cannot render
					}
				}
			                            	
				
			}
			
			// 引数のoffsetはCRかLFの位置
			// rectはmodelToView(offset)が返すRectangle
			// 呼び出しもとは
			// (rect.x > getWidth() - RIGHT_MARGIN - CRLF_IMAGE_WIDTH)
			// である事を確認してからこのメソッドを呼び出さなければならない
			void drawOutOfBorder(Graphics g, int offset, Rectangle rect) {
				if (regionInfo != null) {
					drawOutOfBorder(g, offset, rect, regionInfo);
				}
				for (HighlightInfo info : infoList) {
					drawOutOfBorder(g, offset, rect, info);
				}
			}
			
			private void drawOutOfBorder(Graphics g, 
                                         int offset, 
                                         Rectangle rect, 
                                         HighlightInfo info) {
				if ((offset < info.start) || (offset >= info.end)) {
					return;
				}
				g.setColor(info.color);
				g.fillRect(rect.x, rect.y, CRLF_IMAGE_WIDTH, rect.height);
			}
			
			
			
			public void install(JTextComponent c) {}
			public void deinstall(JTextComponent c) {}
			public void removeHighlight(Object tag) {}
			public void removeAllHighlights() {}
			public void changeHighlight(Object tag, int p0, int p1) 
			                            throws BadLocationException {}
			public Highlight[] getHighlights() {
				return null;
			}
			public Object addHighlight(int p0, int p1, HighlightPainter p) 
			                           throws BadLocationException {
				return null;
			}
		}
	}
	
	/*
	private static class Test extends JFrame implements ActionListener {
		WhitespaceVisibleAreaInfo info = new WhitespaceVisibleAreaInfo(
			Main.getColorProperty("text.color.bg"),
			Main.getColorProperty("text.color.fg"),
			Main.getColorProperty("text.color.caret"),
			Main.getColorProperty("text.color.endLine"),
			Main.getColorProperty("text.color.whitespace"),
			Main.getFontProperty("textarea.font"),
			Main.getIntProperty("text.int.tab.count")
		);
		WhitespaceVisibleArea sa = new WhitespaceVisibleArea(info);
		ArrayList<Object> highlights = new ArrayList<Object>();
		
		JTextField rTF = new JTextField(2);
		JTextField gTF = new JTextField(2);
		JTextField bTF = new JTextField(2);
		JTextField startTF = new JTextField(3);
		JTextField endTF = new JTextField(3);
		JButton highlightButton = new JButton("highlight");
		JButton regionButton = new JButton("region");
		JButton fgButton = new JButton("FG");
		JButton bgButton = new JButton("BG");
		JButton setTextButton = new JButton("setText");
		JRadioButton editableButton = new JRadioButton("true");
		JRadioButton disEditableButton = new JRadioButton("false");
		JRadioButton lfButton = new JRadioButton("LF");
		JRadioButton crButton = new JRadioButton("CRLF");
		JButton clearHighlightButton = new JButton("clearHighlight");
		JTextField clearedTF = new JTextField(1);
		JButton clearAllHighlightButton = new JButton("clearAllHighlight");
		JButton clearRegionButton = new JButton("clearRegion");
		JButton clearHighlightsAndRegionButton = 
									new JButton("clearHighlightsAndRegion");
		
		
		Test() {
			super("WhitespaceVisibleArea - Test");
			
			Dimension d = new Dimension(295, 200);
			sa.setPreferredSize(d);
			sa.setMinimumSize(d);
			
			highlightButton.addActionListener(this);
			regionButton.addActionListener(this);
			fgButton.addActionListener(this);
			bgButton.addActionListener(this);
			setTextButton.addActionListener(this);
			ButtonGroup group1 = new ButtonGroup();
			editableButton.setSelected(true);
			group1.add(editableButton);
			group1.add(disEditableButton);
			editableButton.addActionListener(this);
			disEditableButton.addActionListener(this);
			ButtonGroup group2 = new ButtonGroup();
			lfButton.setSelected(true);
			group2.add(lfButton);
			group2.add(crButton);
			lfButton.addActionListener(this);
			crButton.addActionListener(this);
			clearHighlightButton.addActionListener(this);
			clearAllHighlightButton.addActionListener(this);
			clearRegionButton.addActionListener(this);
			clearHighlightsAndRegionButton.addActionListener(this);
			
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			Box base = Box.createVerticalBox();
			
			Box saBox = Box.createHorizontalBox();
			saBox.add(Box.createHorizontalStrut(5));
			saBox.add(sa);
			saBox.add(Box.createHorizontalStrut(5));
			saBox.add(Box.createVerticalStrut(200));
			
			base.add(saBox);
			
			Box colorBox = Box.createHorizontalBox();
			colorBox.add(Box.createHorizontalStrut(5));
			colorBox.add(new JLabel("Color     "));
			colorBox.add(new JLabel("r:"));
			colorBox.add(rTF);
			colorBox.add(new JLabel("   g:"));
			colorBox.add(gTF);
			colorBox.add(new JLabel("   b:"));
			colorBox.add(bTF);
			colorBox.add(Box.createHorizontalGlue());
			colorBox.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(colorBox);
			
			Box box2 = Box.createHorizontalBox();
			box2.add(Box.createHorizontalStrut(5));
			box2.add(fgButton);
			box2.add(Box.createHorizontalGlue());
			box2.add(bgButton);
			box2.add(Box.createHorizontalGlue());
			box2.add(highlightButton);
			box2.add(Box.createHorizontalGlue());
			box2.add(regionButton);
			box2.add(Box.createHorizontalStrut(10));
			base.add(Box.createVerticalStrut(5));
			base.add(box2);
			
			Box box3 = Box.createHorizontalBox();
			box3.add(Box.createHorizontalStrut(5));
			box3.add(new JLabel("start:"));
			box3.add(startTF);
			box3.add(new JLabel("    end:"));
			box3.add(endTF);
			box3.add(Box.createHorizontalGlue());
			box3.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box3);
			
			Box box4 = Box.createHorizontalBox();
			box4.add(Box.createHorizontalStrut(5));
			box4.add(new JLabel("setEditable"));
			box4.add(Box.createHorizontalStrut(30));
			box4.add(editableButton);
			box4.add(Box.createHorizontalStrut(30));
			box4.add(disEditableButton);
			box4.add(Box.createHorizontalGlue());
			box4.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box4);
			
			Box box5 = Box.createHorizontalBox();
			box5.add(Box.createHorizontalStrut(5));
			box5.add(new JLabel("setCRLF"));
			box5.add(Box.createHorizontalStrut(30));
			box5.add(lfButton);
			box5.add(Box.createHorizontalStrut(30));
			box5.add(crButton);
			box5.add(Box.createHorizontalGlue());
			box5.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box5);
			
			Box box6 = Box.createHorizontalBox();
			box6.add(Box.createHorizontalStrut(5));
			box6.add(clearHighlightButton);
			box6.add(Box.createHorizontalStrut(5));
			box6.add(clearedTF);
			box6.add(Box.createHorizontalGlue());
			box6.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box6);
			
			Box box7 = Box.createHorizontalBox();
			box7.add(Box.createHorizontalStrut(5));
			box7.add(clearAllHighlightButton);
			box7.add(Box.createHorizontalGlue());
			box7.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box7);
			
			Box box8 = Box.createHorizontalBox();
			box8.add(Box.createHorizontalStrut(5));
			box8.add(clearRegionButton);
			box8.add(Box.createHorizontalGlue());
			box8.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box8);
			
			Box box9 = Box.createHorizontalBox();
			box9.add(Box.createHorizontalStrut(5));
			box9.add(clearHighlightsAndRegionButton);
			box9.add(Box.createHorizontalGlue());
			box9.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box9);
			
			Box box10 = Box.createHorizontalBox();
			box10.add(Box.createHorizontalStrut(5));
			box10.add(setTextButton);
			box10.add(Box.createHorizontalGlue());
			box10.add(Box.createHorizontalStrut(5));
			base.add(Box.createVerticalStrut(5));
			base.add(box10);
			
			base.add(Box.createVerticalStrut(5));
			
			getContentPane().add(base);
			pack();
			setVisible(true);
		}

		private Color getColor() {
			int r = Integer.parseInt(rTF.getText());
			int g = Integer.parseInt(gTF.getText());
			int b = Integer.parseInt(bTF.getText());
			return new Color(r, g, b);
		}
		
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == highlightButton) {
				try {
					int start = Integer.parseInt(startTF.getText());
					int end = Integer.parseInt(endTF.getText());
					Object highlight = sa.highlight(start, end, getColor());
					highlights.add(highlight);
				} catch (Exception e2) {
					System.out.println(e2);
				}
			} else if(source == clearHighlightsAndRegionButton) {
				sa.clearHighlightsAndRegion();
			} else if(source == clearRegionButton) {
				sa.clearRegion();
			} else if(source == clearAllHighlightButton) {
				sa.clearAllHighlights();
			} else if(source == clearHighlightButton) {
				try {
					int index = Integer.parseInt(clearedTF.getText());
					Object highlight = highlights.remove(index);
					sa.clearHighlight(highlight);
				} catch (Exception e2) {
					System.out.println(e2);
				}
			} else if(source == crButton) {
				sa.setCRLF(true);
			} else if(source == lfButton) {
				sa.setCRLF(false);
			} else if(source == disEditableButton) {
				sa.setEditable(false);
			} else if(source == editableButton) {
				sa.setEditable(true);
			} else if(source == setTextButton) {
				sa.setText(TEXT);
			} else if(source == bgButton) {
				try {
					sa.getViewport().getView().setBackground(getColor());
				} catch (Exception e2) {
					System.out.println(e2);
				}
			} else if(source == fgButton) {
				try {
					sa.getViewport().getView().setForeground(getColor());
				} catch (Exception e2) {
					System.out.println(e2);
				}
			} else if(source == regionButton) {
				try {
					int start = Integer.parseInt(startTF.getText());
					int end = Integer.parseInt(endTF.getText());
					sa.region(start, end, getColor());
				} catch (Exception e2) {
					System.out.println(e2);
				}
			}
		}
		private static final String TEXT = 
			"aaa aaa　aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
			"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\r\n" +
			"	bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb" +
			"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\r\n" +
			"ccccccccccccc\r\nddddddddddd";
			
	}
	
	public static void main(String[] args) throws Exception {
		new Test();
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
