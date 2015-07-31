package murata.rms.batch.dmrimport.util;

/**
 * 変換処理クラス。
 * <p>
 * 特定文字列が文字化けを起こすため、変換メソッドを用意している。
 * </p>
 *
 * @author H.Hayata
 * @version $Revision: 2402 $ $Date: 2007-01-30 09:49:14 +0000 (火, 30 1 2007) $
 */
public class Converter {

	/**
	 * 文字コード"CP932"使用判断フラグ。<br/> ツールのためwindows環境で実行される事を前提としている。(外部ファイル化する？)
	 */
	private static final boolean IS_CP932 = true;

	/**
	 * This method convert JIS to Cp932.<br/>
	 * <p>
	 * 「－」全角ハイフンなど同一コードにも関わらず、Unicode変換後のコードが<br/>
	 * SJISとCP932で異なり文字化けが発生するため特定コードを発見した場合変換するメソッド。
	 * </p>
	 *
	 * @param s
	 *            変換前文字列
	 * @return 変換後文字列
	 */
	public static String toCp932(String s) {
		if (!IS_CP932) {
			return s;
		}
		if (s == null) {
			return s;
		}
		StringBuffer sb = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			switch (c) {
			case 0x005c: // REVERSE SOLIDUS ->
				c = 0xff3c; // FULLWIDTH REVERSE SOLIDUS
				break;
			case 0x301c: // WAVE DASH ->
				c = 0xff5e; // FULLWIDTH TILDE
				break;
			case 0x2016: // DOUBLE VERTICAL LINE ->
				c = 0x2225; // PARALLEL TO
				break;
			case 0x2212: // MINUS SIGN ->
				c = 0xff0d; // FULLWIDTH HYPHEN-MINUS
				break;
			case 0x00a2: // CENT SIGN ->
				c = 0xffe0; // FULLWIDTH CENT SIGN
				break;
			case 0x00a3: // POUND SIGN ->
				c = 0xffe1; // FULLWIDTH POUND SIGN
				break;
			case 0x00ac: // NOT SIGN ->
				c = 0xffe2; // FULLWIDTH NOT SIGN
				break;
			}
			sb.append(c);
		}
		return new String(sb);
	}
}
