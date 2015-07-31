package jp.gr.java_conf.boj.app.regex;

/*
 * GUI(正規表現エンジン)の状態を表す。
 */
class AppState {
	/*
	 * nullなら何も設定しない事を表し、それ以外は空の文字列を含めて設定する
	 */ 
	final String regex;    
	
	/*
	 * nullなら何も設定しない事を表し、それ以外は空の文字列を含めて設定する
	 */ 
	final String inputChars; 
	
	/*
	 * nullなら何も設定しない事を表し、それ以外は空の文字列を含めて設定する
	 */ 
	final String replacement;
	
	/*
	 * 負の数の場合には設定を変更しない事を表し、0の場合には
	 * 設定しない事を表し、1以上の場合にはその値を設定する。
	 */
	final int findIndex;  
	
	/*
	 * 負の数の場合には設定を変更しない事を表し、0の場合には
	 * 設定しない事を表し、1以上の場合にはその値を設定する。
	 */
	final int regionStart;   
	
	/*
	 * 負の数の場合には設定を変更しない事を表し、0の場合には
	 * 設定しない事を表し、1以上の場合にはその値を設定する。
	 */
	final int regionEnd;     
	
	/*
	 * 負の数の場合には設定を変更しない事を表し、0の場合には
	 * 設定しない事を表し、1以上の場合にはその値を設定する。
	 */
	final int modemusk;        
	
	/* この値を設定する。 */
	final boolean isAnchoringBounds;
	
	/* この値を設定する。 */
	final boolean isTransparentBounds;
	
	/* この値を設定する。 */
	final boolean isCRLF;
	
	/*
	 * regex, replacement, inputCharsは負の数の場合には設定を
	 * 変更しない事を表し、0の場合には設定しない事を表し、
	 * 1以上の場合にはその値を設定する。
	 */
	AppState(String regex, String inputChars, String replacement) {
		this(regex, inputChars, replacement, 
		     0, 0, 0, 0, 
		     true, false, false);	
	}
	
	/*
	 * regex, replacement, inputCharsは負の数の場合には設定を
	 * 変更しない事を表し、0の場合には設定しない事を表し、
	 * 1以上の場合にはその値を設定する。
	 * regex, replacement, inputChars, modemuskは負の数の場合には設定を
	 * 変更しない事を表し、0の場合には設定しない事を表し、
	 * 1以上の場合にはその値を設定する。
	 */
	AppState(String regex,
			 String inputChars,
			 String replacement,
	         int findIndex,
	         int regionStart,
	         int regionEnd,
	         int modemusk,
	         boolean isAnchoringBounds,
	         boolean isTransparentBounds,
	         boolean isCRLF) {
		
		this.regex = regex;
		this.inputChars = inputChars;
		this.replacement = replacement;
		this.findIndex = findIndex;
		this.regionStart = regionStart;
		this.regionEnd = regionEnd;
		this.modemusk = modemusk;
		this.isAnchoringBounds = isAnchoringBounds;
		this.isTransparentBounds = isTransparentBounds;
		this.isCRLF = isCRLF;
	}
	
	/*
	 * 引数のモードもオンならtrueを返す。
	 * 引数はPatternの定数
	 * UNIX_LINES  CASE_INSENSITIVE  COMMENTS  MULTILINE  LITERAL 
	 * DOTALL  UNICODE_CASE  CANON_EQ
	 * のどれか
	 */
	boolean containsMode(int mode) {
		return ((modemusk & mode) != 0);
	}
}
