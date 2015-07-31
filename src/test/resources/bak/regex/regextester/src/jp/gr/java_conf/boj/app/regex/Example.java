package jp.gr.java_conf.boj.app.regex;

/*
 * アプリ起動時にXMLから生成し、マップに格納しておく。
 * リンクのURLにキーを指定しておく。
 * stateがnullでなければGUIを更新する。
 * textをtypeでExplanationAreaに表示する。
 * commandはEventQueue#invokeLaterで対応するメソッドのdoClickを実行する。
 */
class Example {
	
	enum Command {CLEAR_SOURCE, CLEAR_REPLACEMENT_BUF, RESET, 
	              LOOKING_AT, MATCHES, FIND, FIND_INT, APPEND_REPLACEMENT, 
	              APPEND_TAIL, REPLACE_FIRST, REPLACE_ALL, REGION,
	              GROUP0, GROUP1, GROUP2, GROUP3, GROUP4, GROUP5}
	
	// ExplanationAreaに表示するHTMLのbodyタグ内にネストする内容
	final String html;
	
	// コンポーネントの状態を設定する情報を保持するAppStateオブジェクト
	// nullの場合はコンポーネントの状態を変更しない事を意味する。
	final AppState state;
	
	// コンポーネントに対するアクションを表すCommandオブジェクトの配列。
	// アクションがなければnullをしていする。
	// 配列が空の場合もnullと同様アクションが無い事を表す。
	final Command[] commands;
	
	// trueの場合はGUIの戻るボタン、進むボタン、
	// サンプルクリアボタン以外を使用不可にする。
	// stateがnullでCommandがDO_NOTHINGの場合でもチュートリアルの
	// 場合がある。
	final boolean isTutorial;
	
	/*
	 * htmlがnullの場合には空の文字列に置き換える。
	 * stateはnullでもよい。
	 * commandsはnullでも空でもよい。
	 * 
	 */
	Example(String html, 
	        AppState state, 
	        Command[] commands, 
	        boolean isTutorial) {
		
		this.html = (html == null) ? "" : html;
		this.state = state;
		this.commands = commands;
		this.isTutorial = isTutorial;
	}
}
