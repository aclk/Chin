package jp.gr.java_conf.boj.app.regex;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import jp.gr.java_conf.boj.lib.gui.SwingUncaughtExceptionHandler;

public class Main {

	private static final String ALL_PLAIN_FONT_METAL = "ALL_PLAIN_FONT_METAL";
	private static final String CROSS_PLATFORM_LAF = "CROSS_PLATFORM_LAF";
	private static final String OS_STYLE_LAF = "OS_STYLE_LAF";
	private static final String METHOD_INFO_XML = "methodInfo.xml";
	private static final String METHOD_INFO_XSD = "methodInfo.xsd";
	private static final String METACHARS_XML = "metachars.xml";
	private static final String METACHARS_XSD = "metachars.xsd";
	private static final String CHARCLASSES_XML = "charclasses.xml";
	private static final String TUTORIAL_XML = "tutorial.xml";
	private static final String TUTORIAL_XSD = "tutorial.xsd";
	private static final String EXAMPLES_XSD = "examples.xsd";
	private static final String[] examplesXMLFiles = {
		"methodinfo_examples.xml",
		"metachars_examples.xml",
		"charclasses_examples.xml",
		"tutorial_examples.xml"
	};
		
	
	private static ResourceBundle props = ResourceBundle.getBundle(
	                                   "jp.gr.java_conf.boj.app.regex.regextester");
	
	static final String HIGHLIGHT_RRGGBB_1 = getStringProperty(
	                                 "main.string.html.highlightcolor_1");
	
	static final String HIGHLIGHT_RRGGBB_2 = getStringProperty(
	                                 "main.string.html.highlightcolor_2");
	
	static final String EXPLAIN_RRGGBB = getStringProperty(
	                                     "main.string.html.explaincolor");
	
	static final String CODE_RRGGBB = getStringProperty(
	                                      "main.string.html.codecolor");
	
	static final String ERROR_RRGGBB = getStringProperty(
	                                       "main.string.html.errorcolor");
	
	static final String LINK_RRGGBB = getStringProperty(
                                            "main.string.html.linkcolor");
	
	static final String LINKED_URL_PRE = getStringProperty(
	                                        "main.string.linked.url.pre");
	
	static final String NOT_FOUND_IMAGEFILE_MESSAGE = getStringProperty(
			"appmessage.string.illegal.imagefile.path.message");
	
	static final String PARAMETER_LABEL = getStringProperty(
			"explanationarea.string.parameter.label");
	
	static final String RETURN_LABEL = getStringProperty(
	        "explanationarea.string.return.label");
	
	static final String EXCEPTION_LABEL = getStringProperty(
            "explanationarea.string.exception.label");
	
	static final String PARSE_ERROR_MESSAGE = getStringProperty(
			"appmessage.string.parse.error.message");
	
	static final String UNCAUGHT_EXCEPTION_MESSAGE = getStringProperty(
			"appmessage.string.uncaught.exception.message");
	
	static final String ILLEGAL_LOOKANDFEEL_ERROR_MESSAGE = 
		getStringProperty(
				"appmessage.string.illegal.lookandfell.error.message");
	
	static final String UNCAUGHT_EXCEPTION_MESSAGE_2 = getStringProperty(
	"appmessage.string.uncaught.exception.message2");
	
	static final String BGCOLOR;
	static final String TEXT;
	static final String PRE_TAG;
	static final String LAST_TAG;
	static final String EMPTY_HTML;
	
	static {
		StringBuilder sb = new StringBuilder("\"#");
		sb.append(Main.getStringProperty("main.string.html.bgcolor"));
		sb.append("\"");
		BGCOLOR = sb.toString();
		sb.setLength(2);
		sb.append(Main.getStringProperty("main.string.html.textcolor"));
		sb.append("\"");
		TEXT = sb.toString();
		
		// <p style="font-size: 200%; color: white; background: red;">
		sb.setLength(0);
		sb.append("<html><body bgcolor=");
		sb.append(BGCOLOR);
		sb.append(" text=");
		sb.append(TEXT);
		sb.append(" style=\"font-family: ");
		sb.append(Main.getStringProperty(
		             "explanationarea.string.html.font.family"));
		sb.append("\">");
		PRE_TAG = sb.toString();
		
		LAST_TAG = "</body></html>";
		
		sb.setLength(0);
		sb.append(PRE_TAG);
		sb.append(LAST_TAG);
		
		EMPTY_HTML = sb.toString();
	}	
	
	/*
	 * regextester.propertiesファイルのプロパティを値を返す。
	 * 引数のキーがregextester.propertiesファイルに無い場合は
	 * 非検査例外のMissingResourceExceptionをスローする。
	 */
	static String getStringProperty(String key) {
		return props.getString(key);
	}
	
	/*
	 * regextester.propertiesファイルのプロパティを値をColorで返す。
	 * 引数のキーがregextester.propertiesファイルに無い場合は
	 * 非検査例外のMissingResourceExceptionをスローする。
	 * プロパティの記述が不正な場合にも非検査例外を投げる。
	 */
	static Color getColorProperty(String key) {
		String value = props.getString(key);
		String[] params = value.split(",");
		Color color = new Color(Integer.parseInt(params[0].trim()),
		                        Integer.parseInt(params[1].trim()),
		                        Integer.parseInt(params[2].trim()));
		return color;
	}
	
	/*
	 * regextester.propertiesファイルのプロパティをFontで返す。
	 * 引数のキーがregextester.propertiesファイルに無い場合は
	 * 非検査例外のMissingResourceExceptionをスローする。
	 * プロパティの記述が不正な場合にも非検査例外を投げる。
	 * スタイルの値はPLAIN BOLD ITALIC ITALICBOLD の
	 * どれかを指定し、それ以外の場合にはPLAINにする
	 */
	static Font getFontProperty(String key) {
		String value = props.getString(key);
		String[] params = value.split(",");
		int style = (params[1].equals("PLAIN")) ? Font.PLAIN :
			        (params[1].equals("BOLD")) ? Font.BOLD :
			        (params[1].equals("ITALIC")) ? Font.ITALIC :
			        (params[1].equals("ITALICBOLD")) 
			        ? (Font.BOLD | Font.ITALIC) : Font.PLAIN;
		return new Font(params[0], style, Integer.parseInt(params[2]));
	}
	
	/*
	 * regextester.propertiesファイルのプロパティを値をint型で返す。
	 * 引数のキーがregextester.propertiesファイルに無い場合は
	 * 非検査例外のMissingResourceExceptionをスローする。
	 * プロパティがintに変換出来ないなど、記述が不正な場合にも
	 * 非検査例外を投げる。
	 */
	static int getIntProperty(String key) {
		return Integer.parseInt(props.getString(key));
	}
	
	/*
	 * クラス名をキーにMapを格納した読み取り専用Mapを返す。
	 * パッケージを除いたPatternとMatcherというクラス名でMap取り出す。
	 * 戻り値のMapもそこに値として格納されているMapもキーは
	 * 順序づけされている。
	 * 戻り値のMapに格納されているMapの値はMethodInfoオブジェクトで
	 * キーはクラス名とメソッド名をベースにしたXML文書内で
	 * ユニークなIDで以下のように 
	 * &quot;クラス名.メソッド名&quot; 形式
	 * ClassName.methodName
	 * あるいはメソッド名が重複している場合には以下のように
	 * ハイフンで区切って引数も加える。
	 * ClassName.methodName-param-param
	 * 
	 * @return methodInfo.xmlの内容を格納した
	 *         Map<String, Map<String, MethodInfo>>オブジェクト。
	 *         nullは返さない。
	 */
	static Map<String, Map<String, MethodInfo>> getClassMap() 
	                                            throws RuntimeException {
		Map<String, Map<String, MethodInfo>> classMap = null;
		InputStream xmlIn = null;
		try {
			MethodInfoSAXHandler handler = new MethodInfoSAXHandler();
			SchemaFactory schemaFactory = SchemaFactory.newInstance(
			                              XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(
			                    Main.class.getResource(METHOD_INFO_XSD));
			
			xmlIn = Main.class.getResourceAsStream(METHOD_INFO_XML);
			InputSource methodInfoXml = new InputSource(xmlIn);
			handler.parse(methodInfoXml, schema);
			classMap = handler.getMap();
		} catch (SAXParseException e) {
			throwException(e, METHOD_INFO_XML);
		} catch (Exception e) {
			throwUnknownException(e, METHOD_INFO_XML);
		} finally {
			//XMLReaderがクローズするかどうかドキュメントに
			//記述が無い為一応閉じておく。
			if (xmlIn != null) {
				try {
					xmlIn.close();
				} catch (IOException e) {}
			}
		}
		assert (classMap != null);
		return classMap;
	}
	
	/*
	 * Example名をキーにExampleオブジェクトを格納したマップを返す。
	 */
	static Map<String, Example> getExampleMap() throws RuntimeException {
		Map<String, Example> map = new HashMap<String, Example>();
		InputStream xmlIn = null;
		
		for (String file : examplesXMLFiles) {
			try {
				ExampleSAXHandler handler = new ExampleSAXHandler();
				SchemaFactory schemaFactory = SchemaFactory.newInstance(
				                      XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema schema = schemaFactory.newSchema(
				                    Main.class.getResource(EXAMPLES_XSD));
				
				xmlIn = Main.class.getResourceAsStream(file);
				InputSource methodInfoXml = new InputSource(xmlIn);
				handler.parse(methodInfoXml, schema);
				map.putAll(handler.getExampleMap());
			} catch (SAXParseException e) {
				throwException(e, file);
			} catch (Exception e) {
				throwUnknownException(e, file);
			} finally {
				if (xmlIn != null) {
					try {
						xmlIn.close();
					} catch (IOException e) {}
				}
			}
		}
		return map;
	}
	
	
	static List<MetaChar> getMetaCharList(boolean isCharClasses) 
	                                      throws RuntimeException {
		String xml = (isCharClasses) ? CHARCLASSES_XML : METACHARS_XML;
		List<MetaChar> metaCharList = null;
		InputStream xmlIn = null;
		try {
			MetaCharSAXHandler handler = new MetaCharSAXHandler();
			SchemaFactory schemaFactory = SchemaFactory.newInstance(
			                              XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(
			                    Main.class.getResource(METACHARS_XSD));
			xmlIn = Main.class.getResourceAsStream(xml);
			InputSource methodInfoXml = new InputSource(xmlIn);
			handler.parse(methodInfoXml, schema);
			metaCharList = handler.getMetaCharList();
		} catch (SAXParseException e) {
			throwException(e, xml);
		} catch (Exception e) {
			throwUnknownException(e, xml);
		} finally {
			if (xmlIn != null) {
				try {
					xmlIn.close();
				} catch (IOException e) {}
			}
		}
		assert (metaCharList != null);
		return metaCharList;
	}
	
	static List<Tutorial> getTutorialList() {
		List<Tutorial> tutorialList = new ArrayList<Tutorial>();
		InputStream xmlIn = null;
		try {
			TutorialSAXHandler handler = new TutorialSAXHandler();
			SchemaFactory schemaFactory = SchemaFactory.newInstance(
			                              XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(
			                    Main.class.getResource(TUTORIAL_XSD));
			xmlIn = Main.class.getResourceAsStream(TUTORIAL_XML);
			InputSource methodInfoXml = new InputSource(xmlIn);
			handler.parse(methodInfoXml, schema);
			tutorialList = handler.getTutorialList();
		} catch (SAXParseException e) {
			throwException(e, TUTORIAL_XML);
		} catch (Exception e) {
			throwUnknownException(e, TUTORIAL_XML);
		} finally {
			if (xmlIn != null) {
				try {
					xmlIn.close();
				} catch (IOException e) {}
			}
		}
		assert (tutorialList != null);
		return tutorialList;
	}
	
	// ファイルはこのクラスと同じパッケージ内に存在しなければならない。
	// うまくイメージをロードできなかった場合には例外をスローする。
	static ImageIcon getImageIcon(String fileName) {
		URL url = Main.class.getResource(fileName);
		if (url == null) {
			throw new IllegalStateException(
				NOT_FOUND_IMAGEFILE_MESSAGE + fileName);
		} 
		return new ImageIcon(url);
	}
	
	/*
	 * 引数のbodyタグの内容(bodyタグは含めてはいけない)で
	 * HTMLを生成する。
	 * 引数の内容がnullの場合にはbodyタグ内が空のHTMLが出力される。
	 * 色を指定する引数がnullの場合には設定ファイルで指定された
	 * デフォルトのテキストの色が使用される。
	 */
	static String toHTML(String bodyContent) {
		return toHTML(null, null, bodyContent, null, null);
	}
	
	/*
	 * 引数のbodyタグの内容(bodyタグは含めてはいけない)で
	 * HTMLを生成する。
	 * 引数の内容がnullの場合にはbodyタグ内が空のHTMLが出力される。
	 * 色を指定する引数がnullの場合には設定ファイルで指定された
	 * デフォルトのテキストの色が使用される。
	 */
	static String toHTML(String bodyContent, String rrggbb) {
		return toHTML(null, null, bodyContent, rrggbb, null);
	}
	
	/*
	 * 引数のbodyタグの内容(bodyタグは含めてはいけない)で
	 * HTMLを生成する。
	 * 引数の内容がnullの場合にはbodyタグ内が空のHTMLが出力される。
	 * 色を指定する引数がnullの場合には設定ファイルで指定された
	 * デフォルトのテキストの色が使用される。
	 */
	static String toHTML(String result, 
	                     String resultRRGGBB,
	                     String message, 
	                     String messageRRGGBB) {
		return toHTML(result, resultRRGGBB, message, messageRRGGBB);
	}
	
	/*
	 * 引数のbodyタグの内容(bodyタグは含めてはいけない)で
	 * HTMLを生成する。
	 * infoがnullでなければその状態も内容として含める。
	 * 引数の内容がnullの場合にはbodyタグ内が空のHTMLが出力される。
	 * 色を指定する引数がnullの場合には設定ファイルで指定された
	 * デフォルトのテキストの色が使用される。
	 */
	static String toHTML(String result, 
	                     String resultRRGGBB,
	                     String message, 
	                     String messageRRGGBB,
	                     MethodInfo info) {
		StringBuilder buf = new StringBuilder();
		buf.append(PRE_TAG);
		
		if (result != null) {
			if (resultRRGGBB != null) {
				buf.append("<font color=\"#");
				buf.append(resultRRGGBB).append("\">");
			}
			buf.append(result);
			if (resultRRGGBB != null) {
				buf.append("</font>");
			}
		}
		
		if (message != null) {
			if (messageRRGGBB != null) {
				buf.append("<font color=\"#");
				buf.append(messageRRGGBB).append("\">");
			}
			if (result != null) {
				buf.append("<br><br>");
			}
			buf.append(message);
			if (messageRRGGBB != null) {
				buf.append("</font>");
			}
		}
		
		if (info != null) {
			if ((result != null) || (message != null)) {
				buf.append("<hr>");
			}
			writeMethodInfo(buf, info);
		}
		buf.append(LAST_TAG);
		
		return buf.toString();
	}
	
	/*
	 * 引数のMethodInfoオブジェクトの内容を表示する
	 * HTMLを生成する。
	 * infoがnullの場合にはbodyタグ内が空のHTMLが出力される。
	 */
	static String toHTML(MethodInfo info) {
		return toHTML(null, null, null, null, info);
	}
	
	/*
	 * 例外とメッセージを表示するHTMLを生成して返す。
	 * メッセージや例外はnullの場合は表示しない。
	 */
	static String toHTML(Exception e, String message) {
		StringBuilder buf = new StringBuilder();
		buf.append(PRE_TAG);
		if ((message != null) && (message.length() > 0)) {
			addFontColor(buf, ERROR_RRGGBB);
			buf.append(message);
			buf.append("</font><br>");
		}
		if (e != null) {
			addFontColor(buf, CODE_RRGGBB);
			buf.append(e);
			buf.append("</font>");
		}
		buf.append(LAST_TAG);
		
		return buf.toString();
	}
	
	static String color2HexString(Color color) {
		StringBuilder buf = new StringBuilder();
		buf.append('#');
		appendRGB(buf, color.getRed());
		appendRGB(buf, color.getGreen());
		appendRGB(buf, color.getBlue());
		return buf.toString();
	}
	private static void appendRGB(StringBuilder buf, int rgb) {
		if (rgb < 16) {
			buf.append('0');
		}
		buf.append(Integer.toHexString(rgb));
	}
	
	// MethodInfoの内容をHTMLでbufferに書き込む
	private static StringBuilder writeMethodInfo(StringBuilder buf, 
	                                             MethodInfo info) {
		buf.append("<table>");
		buf.append("<tr><td cellpadding=\"0\" cellspacing=\"0\">");
		addFontColor(buf, HIGHLIGHT_RRGGBB_1);
		buf.append(info.getDeclaration()).append("</font></td></tr>");
		
		List<String> paramList = info.getParamList();
		if ((paramList != null) && (!paramList.isEmpty())) {
			buf.append("<tr><td cellpadding=\"0\" cellspacing=\"0\">");
			addFontColor(buf, HIGHLIGHT_RRGGBB_2);
			addSpace(buf, 1).append(PARAMETER_LABEL);
			buf.append("</font><br>");
			buf.append("<table><tr>");
			buf.append("<td cellpadding=\"0\" cellspacing=\"0\">");
			addSpace(buf, 2).append("</td>");
			buf.append("<td cellpadding=\"0\" cellspacing=\"0\">");
			for (String param : paramList) {
				buf.append(param).append("<br>");
			}
			buf.append("</td></tr></table>");
			buf.append("</td></tr>");
		}
		String returnType = info.getReturnType();
		if ((returnType != null) && (returnType.length() > 0)) {
			buf.append("<tr><td cellpadding=\"0\" cellspacing=\"0\">");
			addFontColor(buf, HIGHLIGHT_RRGGBB_2);
			addSpace(buf, 1).append(RETURN_LABEL);
			buf.append("</font><br>");
			buf.append("<table><tr>");
			buf.append("<td cellpadding=\"0\" cellspacing=\"0\">");
			addSpace(buf, 2).append("</td>");
			buf.append("<td cellpadding=\"0\" cellspacing=\"0\">");
			buf.append(returnType);
			buf.append("</td></tr></table>");
			buf.append("</td></tr>");
		}
		
		List<String> exceptionList = info.getExceptionList();
		if ((exceptionList != null) && (!exceptionList.isEmpty())) {
			buf.append("<tr><td cellpadding=\"0\" cellspacing=\"0\">");
			addFontColor(buf, HIGHLIGHT_RRGGBB_2);
			addSpace(buf, 1).append(EXCEPTION_LABEL);
			buf.append("</font><br>");
			buf.append("<table><tr>");
			buf.append("<td cellpadding=\"0\" cellspacing=\"0\">");
			addSpace(buf, 2).append("</td>");
			buf.append("<td cellpadding=\"0\" cellspacing=\"0\">");
			for (String exception : exceptionList) {
				buf.append(exception).append("<br>");
			}
			buf.append("</td></tr></table>");
			buf.append("</td></tr>");
		}
		
		String explain = info.getExplain();
		if ((explain != null) && (explain.length() > 0)) {
			buf.append("<tr><td cellpadding=\"0\" cellspacing=\"0\">");
			addFontColor(buf, EXPLAIN_RRGGBB);
			buf.append(explain).append("</font>");
			buf.append("</td></tr>");
		}
		buf.append("</table>");
		
		return buf;
	}
	
	private static StringBuilder addFontColor(StringBuilder buf, 
                                              String color) {
		buf.append("<font color=\"#");
		buf.append(color).append("\">");
		return buf;
	}

	private static StringBuilder addSpace(StringBuilder buf, int count) {
		for (int i = 0; i < count; i++) {
			buf.append("　");
		}
		return buf;
	}
	
	private static void throwUnknownException(Exception e, 
	                                          String fileName)
	                                          throws RuntimeException {
		String message = fileName + PARSE_ERROR_MESSAGE + e;
		throw new RuntimeException(message);
	}
	
	private static void throwException(SAXParseException e, 
			                           String fileName) 
	                                   throws RuntimeException {
		
		StringBuilder sb = new StringBuilder();
		sb.append(fileName);
		sb.append(PARSE_ERROR_MESSAGE);
		int lineNumber = e.getLineNumber();
		if (lineNumber != -1) {
			sb.append("line number:");
			sb.append(lineNumber);
			int columnNumber = e.getColumnNumber();
			if (columnNumber != -1) {
				sb.append("      column number:");
				sb.append(columnNumber);
			}
			sb.append("\n");
		}
		sb.append("\n");
		sb.append(e);
		throw new RuntimeException(sb.toString());
	}
	
	//グループのコンボボックスがWindowsLookAndFeelでつぶれる
	//TODO 日本語はプロパティファイルに移す
	
	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					createAndShowGUI();
				}
			}
		);
	}
	
	private static void createAndShowGUI() {
		Thread.setDefaultUncaughtExceptionHandler(
				new SwingUncaughtExceptionHandler(
					(Frame)null, UNCAUGHT_EXCEPTION_MESSAGE));
			
		if (ALL_PLAIN_FONT_METAL.equals(
				getStringProperty("main.string.metallookandfeel.font.style"))) {
		    UIManager.put("swing.boldMetal", Boolean.FALSE);
		}
		String laf = getStringProperty("main.string.lookandfeel.class");
		if (!laf.equals(CROSS_PLATFORM_LAF)) {
			try {
				if (laf.equals(OS_STYLE_LAF)) {
					UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
				} else {
					UIManager.setLookAndFeel(laf);
				}
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, 
						ILLEGAL_LOOKANDFEEL_ERROR_MESSAGE + laf);
			}
		}
		RegexTester regexTester = new RegexTester();
		JFrame frame = regexTester.getFrame();
		frame.setVisible(true);
        Thread.setDefaultUncaughtExceptionHandler(
				new SwingUncaughtExceptionHandler(
						frame, UNCAUGHT_EXCEPTION_MESSAGE_2));
	}

}
