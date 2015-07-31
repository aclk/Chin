package jp.gr.java_conf.boj.app.regex;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkListener;

class ExplanationArea extends JScrollPane {
	
	//enum Type {SET, SHIFT, REPLACE}
	
	private static final String EMPTY_HTML = Main.EMPTY_HTML;
	
	private final JEditorPane editorPane;
	
	private final Navigator navigator = new Navigator();
	
	ExplanationArea(HyperlinkListener listener) {
		super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editorPane = new JEditorPane();
		editorPane.setBackground(Main.getColorProperty(
		                            "explanationarea.color.background"));
		editorPane.setContentType("text/html; charset=UTF8");
		editorPane.setText(EMPTY_HTML);
		editorPane.setEditable(false);
		editorPane.addHyperlinkListener(listener);
		getViewport().setView(editorPane);
	}
	
	/*
	 * 現在表示されているHTMLと戻るボタンや進むボタンで
	 * 表示可能なHTMLを破棄し、引数の内容のHTMLを表示する。
	 * 引数がnullの場合はclearメソッドと等価。
	 */
	void set(String html, boolean isTutorial) {
		if ((html == null) && navigator.isEmpty()) {
			return;
		}
		navigator.clear();
		navigator.shift(html, isTutorial);
		updateEditorPane();
	}
	
	/*
	 * editorPaneの内容を引数の内容にする。
	 * 現在表示されているHTMLがあればそれが１つ前のHTMLとなり、
	 * 戻るボタンで表示される。
	 * 進むボタンで表示されるHTMLはなくなる。
	 * isTutorialがtrueの場合はチュートリアル以外からチュートリアルに
	 * 遷移する場合となる。
	 * 引数がnullの場合はclearメソッドと等価。
	 */
	void shift(String html, boolean isTutorial) {
		if ((html == null) && navigator.isEmpty()) {
			return;
		}
		navigator.shift(html, isTutorial);
		updateEditorPane();
	}
	
	/*
	 * editorPaneの内容を引数の内容に置き換える。
	 * 戻るボタンや進むボタンを押した場合に表示されるHTMLは
	 * 変更されない。
	 * チュートリアルからチュートリアルに遷移する場合にこのメソッドを
	 * 使用する。
	 * 引数がnullの場合はclearメソッドと等価。
	 */
	void replace(String html) {
		navigator.replace(html);
		updateEditorPane();
	}
	
	/*
	 * editorPaneの内容をクリアする。
	 * 戻るボタンや進むボタンで表示可能なHTMLもすべてなくなる。
	 */
	void clear() {
		navigator.clear();
		updateEditorPane();
	}
	
	/*
	 * 進むボタンで表示するHTMLがあるならtrueを返す。
	 */
	boolean hasNext() {
		return navigator.hasNext();
	}
	
	/*
	 * 戻るボタンで表示するHTMLがあるならtrueを返す。
	 */
	boolean hasPrevious() {
		return navigator.hasPrevious();
	}
	
	/*
	 * 進むボタンを押された場合のHTMLを表示する。
	 * 進むボタンを押された場合のHTMLが無い場合には例外をスローするので
	 * 呼び出しもとでhasNextを使ってチェックしてから呼び出す。
	 */
	void next() {
		navigator.next();
		updateEditorPane();
	}
	
	/*
	 * 戻るボタンを押された場合のHTMLを表示する。
	 * 戻るボタンを押された場合のHTMLが無い場合には例外をスローするので
	 * 呼び出しもとでhasNextを使ってチェックしてから呼び出す。
	 */
	void previous() {
		navigator.previous();
		updateEditorPane();
	}
	
	// editorPaneに現在設定されているHTMLを表示する。
	private void updateEditorPane() {
		editorPane.setText(navigator.getCurrentHTML());
		final Point viewPosition = navigator.getCurrentLeftTop();
		// 普通にsetViewPositionを呼び出すと一度位置を設定した後に
		// テキスト末尾にスクロールしてしまう為
		EventQueue.invokeLater(
			new Runnable() {
				public void run() {
					getViewport().setViewPosition(viewPosition);
				}
			}
		);
	}
	
	
	// 表示しているHTMLや戻る、進むをカプセル化
	private class Navigator {
		/*
		 * 何も表示されていない事を示すbodyの内容が空のHTML
		 * currentが持つのリンクのスタートは常にこのemptyHTMLと
		 * なる。
		 * current.preにemptyHTMLを設定したりはせず、
		 * emptyHTML.nextは常にnullとなるようにする。
		 */
		private final HTML emptyHTML = new HTML(EMPTY_HTML, false);
		
		/*
		 * 常に現在表示しているHTMLオブジェクトの参照。
		 * 何も表示しない場合にはemptyHTMLを設定し、nullの状態は
		 * 作らない。
		 */
		private HTML current = emptyHTML;
		
		String getCurrentHTML() {
			return current.html;
		}
		
		Point getCurrentLeftTop() {
			return new Point(current.leftTop.x, current.leftTop.y);
		}
		
		void shift(String html, boolean isTutorial) {
			if (html == null) {
				clear();
				return;
			}
			
			HTML newHtml = new HTML(html, isTutorial);
			if (current != emptyHTML) {
				
				// チュートリアルからチュートリアル以外への
				// shift(setではなく)は想定していないが
				// 対応できるように実装しておく
				if (current.isTutorial) {
					
					// チュートリアルからチュートリアルへのshiftは禁止
					// setでemptyHTMLに初期化するかreplaceを使う
					if (isTutorial) {
						throw new IllegalArgumentException(
						              "example cannot shift example !");
					}
					
					// currentのチュートリアルをとばす
					if (current.pre != null) {
						current.pre.next = newHtml;
						newHtml.pre = current.pre;
					}
				} else {
					current.next = newHtml;
					newHtml.pre = current;
				}
				recordCurrentLeftTop();
			}
			current = newHtml;
		}
		
		void replace(String html) {
			if (html == null) {
				clear();
				return;
			}
			if (isEmpty() || (!current.isTutorial)) {
				throw new IllegalStateException(
						"current is EMPTY_HTML or not Example");
			}
			current.html = html;
		}
		
		void clear() {
			current = emptyHTML;
		}
		
		boolean isEmpty() {
			return (current == emptyHTML);
		}
		
		boolean hasNext() {
			return (!isEmpty() && (current.next != null));
		}
		
		boolean hasPrevious() {
			return (!isEmpty() && (current.pre != null));
		}
		
		void next() {
			if (!hasNext()) {
				throw new IllegalStateException("next html is nothing !");
			}
			recordCurrentLeftTop();
			current = current.next;
		}
		
		void previous() {
			// emptyHTMLには戻らない
			if (!hasPrevious()) {
				throw new IllegalStateException(
				                            "previous html is nothing !");
			}
			
			// 戻る場合にチュートリアルはnextとして記録しない。
			if (current.isTutorial) {
				current.pre.next = null;
			} else {
				recordCurrentLeftTop();
			}
			current = current.pre;
		}
		
		// 現在のビューポートにおけるeditorPaneの左上の位置を記録
		private void recordCurrentLeftTop() {
			Point point = getViewport().getViewPosition();
			current.leftTop.x = point.x;
			current.leftTop.y = point.y;
		}
		
		
		private class HTML {
			// リンクをクリックした際にExplainationAreaに表示されている
			// 左上の位置（画面遷移の際に同じ位置で表示する為）
			// non null 保証の為finalだがフィールドx, yを直接変更する。
			final Point leftTop = new Point();
			HTML pre;
			HTML next;
			String html;
			final boolean isTutorial;
			
			HTML(String html, boolean isTutorial) {
				this.html = html;
				this.isTutorial = isTutorial;
			}
		}
	}
	
}
