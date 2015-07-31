package jp.gr.java_conf.boj.app.regex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import jp.gr.java_conf.boj.app.regex.Example.Command;

class ExampleSAXHandler implements ContentHandler,
                                    ErrorHandler {

	private static final String LINKED_URL_PRE;
	private static final String HIGHLIGHT_RRGGBB_1;
	private static final String HIGHLIGHT_RRGGBB_2;
	private static final String EXPLAIN_RRGGBB;
	private static final String CODE_RRGGBB;
	private static final String ERROR_RRGGBB;
	private static final String LINK_RRGGBB;
	
	static {
		LINKED_URL_PRE = Main.LINKED_URL_PRE;
		HIGHLIGHT_RRGGBB_1 = Main.HIGHLIGHT_RRGGBB_1;
		HIGHLIGHT_RRGGBB_2 = Main.HIGHLIGHT_RRGGBB_2;
		EXPLAIN_RRGGBB = Main.EXPLAIN_RRGGBB;
		CODE_RRGGBB = Main.CODE_RRGGBB;
		ERROR_RRGGBB = Main.ERROR_RRGGBB;
		LINK_RRGGBB = Main.LINK_RRGGBB;
	}
	
    private Map<String, Example> exampleMap;

    // key of exampleMap
    private String exampleName;

    // fields of Example
    private String html;
    private AppState appState;
    private Command[] commands;
    private boolean isTutorial;

    // fields of AppState
    private String regex;
    private String inputChars;
    private String replacement;
    private int findIndex;
    private int regionStart;
    private int regionEnd;
    private int modemusk;
    private boolean isAnchoringBounds;
    private boolean isTransparentBounds;
    private boolean isCRLF;

    private boolean isThreeParamsAppState;

    private StringBuilder buffer;
    private boolean enabledCharacters;
    private boolean trimedCharacters;


    Map<String, Example> getExampleMap() {
        return exampleMap;
    }

    /*
     * 引数の InputSource オブジェクトが指す XML 文書のパースを行う。<br>
     * スキーマや DTD による検証は行わない。<br>
     * パーサはネームスペースを意識した処理を行う。<br>
     * パース時に発生する可能性のある IOException や SAXException は
     * 呼び出しもとで処理する。
     *
     * @param inputSource XMLを読み込む為のInputSourceオブジェクト。
     * @exception IllegalArgumentException inputSourceがnullの場合
     * @exception ParserConfigurationException SAXParserFactoryが
     *            SAXParserを生成できない場合
     * @exception IOException  XMLReader#parseがスローする可能性あり
     * @exception SAXException XMLReader#parseがスローする可能性あり
     */
    void parse(InputSource inputSource)
               throws IOException, SAXException,
                      ParserConfigurationException {
        parse(inputSource, true);
    }

    /*
     * 引数の InputSource オブジェクトが指す XML 文書のパースを行う。<br>
     * スキーマや DTD による検証は行わない。<br>
     * ネームスペースを意識しない場合には引数の boolean 値に
     * false を指定する。<br>
     * パース時に発生する可能性のある IOException や SAXException は
     * 呼び出しもとで処理する。
     *
     * @param inputSource XMLを読み込む為のInputSourceオブジェクト。
     * @param namespaceAwareness ネームスペースを考慮する場合には
     *                           trueを指定する。
     * @exception IllegalArgumentException inputSourceがnullの場合
     * @exception ParserConfigurationException SAXParserFactoryが
     *            SAXParserを生成できない場合
     * @exception IOException  XMLReader#parseがスローする可能性あり
     * @exception SAXException XMLReader#parseがスローする可能性あり
     */
    void parse(InputSource inputSource,
               boolean namespaceAwareness)
               throws IOException, SAXException,
                      ParserConfigurationException {
        if (inputSource == null) {
            throw new IllegalArgumentException("inputSource is null !");
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(namespaceAwareness);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(this);
        reader.setErrorHandler(this);
        reader.parse(inputSource);
    }


    //////////////////////////////////////////////
    // このメソッドはJDK1.5以降でコンパイル可能 //
    //////////////////////////////////////////////
    /*
     * 引数の InputSource オブジェクトが指す XML 文書と
     * Schema オブジェクトが表すスキーマでパースを行う。<br>
     * スキーマによる検証を行わない場合には引数の Schema オブジェクトは
     * null でよい。<br>
     * パーサはネームスペースを意識した処理を行う。<br>
     * パース時に発生する可能性のある IOException や SAXException は
     * 呼び出しもとで処理する。
     *
     * @param inputSource XMLを読み込む為のInputSourceオブジェクト。
     * @param schema スキーマを表すSchemaオブジェクト。
     *               nullの場合にはスキーマによる検証を行わない。
     * @exception IllegalArgumentException inputSourceがnullの場合
     * @exception ParserConfigurationException SAXParserFactoryが
     *            SAXParserを生成できない場合
     * @exception IOException  XMLReader#parseがスローする可能性あり
     * @exception SAXException XMLReader#parseがスローする可能性あり
     */
    void parse(InputSource inputSource,
               Schema schema)
               throws IOException, SAXException,
                      ParserConfigurationException {
        parse(inputSource, schema, true);
    }


    //////////////////////////////////////////////
    // このメソッドはJDK1.5以降でコンパイル可能 //
    //////////////////////////////////////////////
    /*
     * 引数の InputSource オブジェクトが指す XML 文書と
     * Schema オブジェクトが表すスキーマでパースを行う。<br>
     * スキーマによる検証を行わない場合には引数の Schema オブジェクトは
     * null でよい。<br>
     * ネームスペースを意識しない場合には引数の boolean 値に
     * false を指定する。<br>
     * パース時に発生する可能性のある IOException や SAXException は
     * 呼び出しもとで処理する。
     *
     * @param inputSource XMLを読み込む為のInputSourceオブジェクト。
     * @param schema      スキーマを表すSchemaオブジェクト。
     *                    nullの場合にはスキーマによる検証を行わない。
     * @param namespaceAwareness ネームスペースを考慮する場合には
     *                           trueを指定する。
     * @exception IllegalArgumentException inputSourceがnullの場合
     * @exception ParserConfigurationException SAXParserFactoryが
     *            SAXParserを生成できない場合
     * @exception IOException  XMLReader#parseがスローする可能性あり
     * @exception SAXException XMLReader#parseがスローする可能性あり
     */
    void parse(InputSource inputSource,
               Schema schema,
               boolean namespaceAwareness)
               throws IOException, SAXException,
                      ParserConfigurationException {
        if (inputSource == null) {
            throw new IllegalArgumentException("inputSource is null !");
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setSchema(schema);
        factory.setNamespaceAware(namespaceAwareness);
        SAXParser parser = factory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(this);
        reader.setErrorHandler(this);
        reader.parse(inputSource);
    }

    /*
     * ContentHandler#startElement の実装<br>
     * XML 文書における要素の開始を通知する。<br>
     *
     * @param uri 要素の名前空間、あるいは空の文字列
     * @param localName プレフィックスのない要素名、あるいは空の文字列
     * @param qName XML文書で要素にプレフィックスが
     *              付いていればプレフィックス付きのそうでなければ
     *              プレフィックス無しの要素名
     * @param atts 要素内の属性をすべて含む Attributes オブジェクト
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes atts)
                             throws SAXException {
        // Sun の jdk1.5 における動作では
        // SAXParserFactory factory = SAXParserFactory.newInstance();
        // SAXParser parser = factory.newSAXParser();
        // XMLReader reader = parser.getXMLReader();
        // このように作成された XMLReader の場合には
        // このメソッドの引数の uri と localName は設定されず
        // (空の文字列に設定される)、 qName は常に設定される。
        // ただし uri と localName は実装によっては
        // 設定されているかもしれない。
        //
        // SAXParserFactory factory = SAXParserFactory.newInstance();
        // factory.setNamespaceAware(true);
        // SAXParser parser = factory.newSAXParser();
        // XMLReader reader = parser.getXMLReader();
        // このように作成された XMLReader の場合には
        // uri, localName, qName すべてが設定されている。
        // よく理解していないがこの場合の qName は実装によっては
        // 設定されていないかもしれない...(そうではないかもしれない)
        // 名前空間を意識したプログラムでは uri と localName の
        // セットで要素を区別すれば間違いないと思われる。
    	
    	if (localName.equals("examples")) {
			startElementExamples(uri, localName, qName, atts);
		} else if (localName.equals("example")) {
			startElementExample(uri, localName, qName, atts);
		} else if (localName.equals("html")) {
			startElementHtml(uri, localName, qName, atts);
		} else if (localName.equals("s")) {
			startElement0(uri, localName, qName, atts);
		} else if (localName.equals("br")) {
			startElementBr(uri, localName, qName, atts);
		} else if (localName.equals("link")) {
			startElementLink(uri, localName, qName, atts);
		} else if (localName.equals("font")) {
			startElementFont(uri, localName, qName, atts);
		} else if (localName.equals("unicode")) {
			startElementUnicode(uri, localName, qName, atts);
		} else if (localName.equals("appstate")) {
			startElementAppstate(uri, localName, qName, atts);
		} else if (localName.equals("regex")) {
			startElementRegex(uri, localName, qName, atts);
		} else if (localName.equals("inputChars")) {
			startElementInputChars(uri, localName, qName, atts);
		} else if (localName.equals("replacement")) {
			startElementReplacement(uri, localName, qName, atts);
		} else if (localName.equals("findIndex")) {
			startElementFindIndex(uri, localName, qName, atts);
		} else if (localName.equals("regionStart")) {
			startElementRegionStart(uri, localName, qName, atts);
		} else if (localName.equals("regionEnd")) {
			startElementRegionEnd(uri, localName, qName, atts);
		} else if (localName.equals("anchoringBounds")) {
			startElementAnchoringBounds(uri, localName, qName, atts);
		} else if (localName.equals("transparentBounds")) {
			startElementTransparentBounds(uri, localName, qName, atts);
		} else if (localName.equals("crlf")) {
			startElementCrlf(uri, localName, qName, atts);
		} else if (localName.equals("modemusk")) {
			startElementModemusk(uri, localName, qName, atts);
		} else if (localName.equals("commands")) {
			startElementCommands(uri, localName, qName, atts);
		}
    }

    /**
     * ContentHandler#endElement の実装<br>
     * XML 文書における要素の終了を通知する。<br>
     *
     * @param uri 要素の名前空間、あるいは空の文字列
     * @param localName プレフィックスのない要素名、あるいは空の文字列
     * @param qName XML文書で要素にプレフィックスが
     *              付いていればプレフィックス付きのそうでなければ
     *              プレフィックス無しの要素名
     * @param atts 要素内の属性をすべて含むAttributesオブジェクト
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void endElement(String uri,
                           String localName,
                           String qName)
                           throws SAXException {
    	if (localName.equals("examples")) {
			endElementExamples(uri, localName, qName);
		} else if (localName.equals("example")) {
			endElementExample(uri, localName, qName);
		} else if (localName.equals("html")) {
			endElementHtml(uri, localName, qName);
		} else if (localName.equals("s")) {
			endElement0(uri, localName, qName);
		} else if (localName.equals("br")) {
			endElementBr(uri, localName, qName);
		} else if (localName.equals("link")) {
			endElementLink(uri, localName, qName);
		} else if (localName.equals("font")) {
			endElementFont(uri, localName, qName);
		} else if (localName.equals("unicode")) {
			endElementUnicode(uri, localName, qName);
		} else if (localName.equals("appstate")) {
			endElementAppstate(uri, localName, qName);
		} else if (localName.equals("regex")) {
			endElementRegex(uri, localName, qName);
		} else if (localName.equals("inputChars")) {
			endElementInputChars(uri, localName, qName);
		} else if (localName.equals("replacement")) {
			endElementReplacement(uri, localName, qName);
		} else if (localName.equals("findIndex")) {
			endElementFindIndex(uri, localName, qName);
		} else if (localName.equals("regionStart")) {
			endElementRegionStart(uri, localName, qName);
		} else if (localName.equals("regionEnd")) {
			endElementRegionEnd(uri, localName, qName);
		} else if (localName.equals("anchoringBounds")) {
			endElementAnchoringBounds(uri, localName, qName);
		} else if (localName.equals("transparentBounds")) {
			endElementTransparentBounds(uri, localName, qName);
		} else if (localName.equals("crlf")) {
			endElementCrlf(uri, localName, qName);
		} else if (localName.equals("modemusk")) {
			endElementModemusk(uri, localName, qName);
		} else if (localName.equals("commands")) {
			endElementCommands(uri, localName, qName);
		}
    }

    private void initBuffer(boolean trimedCharacters) {
        buffer.setLength(0);
        enabledCharacters = true;
        this.trimedCharacters = trimedCharacters;
    }

    // examples要素の出現
    private void startElementExamples(String uri, String localName,
                                      String qName, Attributes atts)
                                      throws SAXException {
        buffer = new StringBuilder();
        exampleMap = new HashMap<String, Example>();
    }

    // examples要素の終了
    private void endElementExamples(String uri, String localName,
                                    String qName)
                                    throws SAXException {}

    // example要素の出現
    private void startElementExample(String uri, String localName,
                                     String qName, Attributes atts)
                                     throws SAXException {
        exampleName = atts.getValue("name");
        isTutorial = (atts.getValue("tutorial").equals("true")) ? true
                                                              : false;
        html = null;
        appState = null;
        commands = null;

    }

    // example要素の終了
    private void endElementExample(String uri, String localName,
                                   String qName)
                                   throws SAXException {
        Example example = new Example(html, appState, commands, isTutorial);
        exampleMap.put(exampleName, example);
    }

    // html要素の出現
    private void startElementHtml(String uri, String localName,
                                  String qName, Attributes atts)
                                  throws SAXException {
        initBuffer(true);
    }

    // html要素の終了
    private void endElementHtml(String uri, String localName,
                                String qName)
                                throws SAXException {
        html = buffer.toString();
    }

    // s要素の出現
	private void startElement0(String uri, String localName,
	                           String qName, Attributes atts)
	                           throws SAXException {
		buffer.append("&nbsp;");
	}

	// s要素の終了
	private void endElement0(String uri, String localName,
	                         String qName)
	                         throws SAXException {
		
	}
	
    // br要素の出現
    private void startElementBr(String uri, String localName,
                                String qName, Attributes atts)
                                throws SAXException {
        // htmlにネストされているのでbufferはそのまま使う
        buffer.append("<br>");
    }

    // br要素の終了
    private void endElementBr(String uri, String localName,
                              String qName)
                              throws SAXException {}

    // link要素の出現
    private void startElementLink(String uri, String localName,
                                  String qName, Attributes atts)
                                  throws SAXException {
        // htmlにネストされているのでbufferはそのまま使う
    	buffer.append("<font color=\"#");
    	buffer.append(LINK_RRGGBB);
    	buffer.append("\">");
        buffer.append("<a href=\"");
        buffer.append(LINKED_URL_PRE);
        buffer.append(atts.getValue("href"));
        buffer.append("\">");
    }

    // link要素の終了
    private void endElementLink(String uri, String localName,
                                String qName)
                                throws SAXException {
        buffer.append("</a></font>");
    }

    // font要素の出現
    private void startElementFont(String uri, String localName,
                                  String qName, Attributes atts)
                                  throws SAXException {
        // htmlにネストされているのでbufferはそのまま使う
        buffer.append("<font color=\"#");
        String color = atts.getValue("color");
        if (color.equals("highlight1")) {
            buffer.append(HIGHLIGHT_RRGGBB_1);
        } else if (color.equals("highlight2")) {
            buffer.append(HIGHLIGHT_RRGGBB_2);
        } else if (color.equals("explain")) {
            buffer.append(EXPLAIN_RRGGBB);
        } else if (color.equals("code")) {
            buffer.append(CODE_RRGGBB);
        } else if (color.equals("error")) {
            buffer.append(ERROR_RRGGBB);
        }
        buffer.append("\">");
    }

    // font要素の終了
    private void endElementFont(String uri, String localName,
                                String qName)
                                throws SAXException {
        buffer.append("</font>");
    }
    
    // unicode要素の出現
	private void startElementUnicode(String uri, String localName,
	                                 String qName, Attributes atts)
	                                 throws SAXException {
		try {
			int start = Integer.parseInt(atts.getValue("start"), 16);
			int end = Integer.parseInt(atts.getValue("end"), 16);
			if (start > end) {
				throw new IllegalArgumentException("(start > end)");
			}
			
			buffer.append("<table style=\"font-size:large; color:");
			buffer.append(HIGHLIGHT_RRGGBB_2);
			buffer.append("\">");
			buffer.append("<tr align=\"center\"><td></td>");
			for (int i = 0; i < 16; i++) {
				buffer.append("<td>");
				buffer.append("<font color=\"");
				buffer.append(HIGHLIGHT_RRGGBB_1);
				buffer.append("\">");
				buffer.append(Integer.toHexString(i).toUpperCase());
				buffer.append("</font>");
				buffer.append("</td>");
			}
			buffer.append("</tr>");
			
			int col = 0;
			for (int i = start; i <= end; i++) {
				if (col == 0) {
					buffer.append("<tr align=\"center\"><td nowrap>");
					buffer.append("<font color=\"");
					buffer.append(HIGHLIGHT_RRGGBB_1);
					buffer.append("\">");
					buffer.append(Integer.toHexString(i).toUpperCase());
					buffer.append("</font>");
					buffer.append("</td>");
				} 
				buffer.append("<td>");
				buffer.append((char)i);
				buffer.append("</td>");
				if (++col == 16) {
					buffer.append("</tr>\n");
					col = 0;
				}
					
			}
			buffer.append("</table>");
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}

	// unicode要素の終了
	private void endElementUnicode(String uri, String localName,
	                               String qName)
	                               throws SAXException {
		
	}

    // appstate要素の出現
    private void startElementAppstate(String uri, String localName,
                                      String qName, Attributes atts)
                                      throws SAXException {
        regex = null;
        inputChars = null;
        replacement = null;
        isThreeParamsAppState = true;
    }

    // appstate要素の終了
    private void endElementAppstate(String uri, String localName,
                                    String qName)
                                    throws SAXException {
        appState = (isThreeParamsAppState)
                   ? new AppState(regex, inputChars, replacement)
                   : new AppState(regex, inputChars, replacement,
                                  findIndex, regionStart, regionEnd,
                                  modemusk, isAnchoringBounds,
                                  isTransparentBounds, isCRLF);


    }

    // regex要素の出現
    private void startElementRegex(String uri, String localName,
                                   String qName, Attributes atts)
                                   throws SAXException {
        initBuffer(true);
    }

    // regex要素の終了
    private void endElementRegex(String uri, String localName,
                                 String qName)
                                 throws SAXException {
        regex = buffer.toString();
    }

    // inputChars要素の出現
    private void startElementInputChars(String uri, String localName,
                                        String qName, Attributes atts)
                                        throws SAXException {
        initBuffer(false);
    }

    // inputChars要素の終了
    private void endElementInputChars(String uri, String localName,
                                      String qName)
                                      throws SAXException {
        inputChars = buffer.toString();
    }

    // replacement要素の出現
    private void startElementReplacement(String uri, String localName,
                                         String qName, Attributes atts)
                                         throws SAXException {
        initBuffer(false);
    }

    // replacement要素の終了
    private void endElementReplacement(String uri, String localName,
                                       String qName)
                                       throws SAXException {
        replacement = buffer.toString();
    }

    // findIndex要素の出現
    private void startElementFindIndex(String uri, String localName,
                                       String qName, Attributes atts)
                                       throws SAXException {

        // AppStateのfindIndex以降のフィールドはRELAX NGでgroupなので
        // isThreeParamsAppStateのチェックは１回でOK
        isThreeParamsAppState = false;
        findIndex = Integer.parseInt(atts.getValue("value"));
    }

    // findIndex要素の終了
    private void endElementFindIndex(String uri, String localName,
                                     String qName)
                                     throws SAXException {}

    // regionStart要素の出現
    private void startElementRegionStart(String uri, String localName,
                                         String qName, Attributes atts)
                                         throws SAXException {
        regionStart = Integer.parseInt(atts.getValue("value"));
    }

    // regionStart要素の終了
    private void endElementRegionStart(String uri, String localName,
                                       String qName)
                                       throws SAXException {}

    // regionEnd要素の出現
    private void startElementRegionEnd(String uri, String localName,
                                       String qName, Attributes atts)
                                       throws SAXException {
        regionEnd = Integer.parseInt(atts.getValue("value"));
    }

    // regionEnd要素の終了
    private void endElementRegionEnd(String uri, String localName,
                                     String qName)
                                     throws SAXException {}

    // anchoringBounds要素の出現
    private void startElementAnchoringBounds(String uri, String localName,
                                             String qName, Attributes atts)
                                             throws SAXException {
        isAnchoringBounds =
            Boolean.valueOf(atts.getValue("value")).booleanValue();
    }

    // anchoringBounds要素の終了
    private void endElementAnchoringBounds(String uri, String localName,
                                           String qName)
                                           throws SAXException {}

    // transparentBounds要素の出現
    private void startElementTransparentBounds(String uri, String localName,
                                               String qName, Attributes atts)
                                               throws SAXException {
        isTransparentBounds =
            Boolean.valueOf(atts.getValue("value")).booleanValue();
    }

    // transparentBounds要素の終了
    private void endElementTransparentBounds(String uri, String localName,
                                             String qName)
                                             throws SAXException {}

    // crlf要素の出現
    private void startElementCrlf(String uri, String localName,
                                  String qName, Attributes atts)
                                  throws SAXException {
        isCRLF = Boolean.valueOf(atts.getValue("value")).booleanValue();
    }

    // crlf要素の終了
    private void endElementCrlf(String uri, String localName,
                                String qName)
                                throws SAXException {}

    // modemusk要素の出現
    private void startElementModemusk(String uri, String localName,
                                      String qName, Attributes atts)
                                      throws SAXException {
        initBuffer(false);
    }

    // modemusk要素の終了
    private void endElementModemusk(String uri, String localName,
                                    String qName)
                                    throws SAXException {
        String str = buffer.toString().trim();
        if (str.equals("DO_NOTHING")) {
            modemusk = -1;
        } else if(str.equals("DEFAULT")) {
            modemusk = 0;
        } else {
            modemusk = 0;
            String[] modes = str.split("\\s");
            for (String mode : modes) {
                if (mode.equals("MULTILINE")) {
                    modemusk |= Pattern.MULTILINE;
                } else if (mode.equals("DOTALL")) {
                    modemusk |= Pattern.DOTALL;
                } else if (mode.equals("COMMENTS")) {
                    modemusk |= Pattern.COMMENTS;
                } else if (mode.equals("LITERAL")) {
                    modemusk |= Pattern.LITERAL;
                } else if (mode.equals("UNIX_LINES")) {
                    modemusk |= Pattern.UNIX_LINES;
                } else if (mode.equals("UNICODE_CASE")) {
                    modemusk |= Pattern.UNICODE_CASE;
                } else if (mode.equals("CASE_INSENSITIVE")) {
                    modemusk |= Pattern.CASE_INSENSITIVE;
                } else if (mode.equals("CANON_EQ")) {
                    modemusk |= Pattern.CANON_EQ;
                }
            }
        }
    }

    // commands要素の出現
    private void startElementCommands(String uri, String localName,
                                      String qName, Attributes atts)
                                      throws SAXException {
        initBuffer(false);
    }

    // commands要素の終了
    private void endElementCommands(String uri, String localName,
                                    String qName)
                                    throws SAXException {
        String list = buffer.toString();
        String[] commandArray = list.split("\\s");
        ArrayList<Command> commandList = new ArrayList<Command>();
        for (String command : commandArray) {
            if (command.equals("CLEAR_SOURCE")) {
                commandList.add(Command.CLEAR_SOURCE);
            } else if (command.equals("CLEAR_REPLACEMENT_BUF")) {
                commandList.add(Command.CLEAR_REPLACEMENT_BUF);
            } else if (command.equals("RESET")) {
                commandList.add(Command.RESET);
            } else if (command.equals("LOOKING_AT")) {
                commandList.add(Command.LOOKING_AT);
            } else if (command.equals("MATCHES")) {
                commandList.add(Command.MATCHES);
            } else if (command.equals("FIND")) {
                commandList.add(Command.FIND);
            } else if (command.equals("FIND_INT")) {
                commandList.add(Command.FIND_INT);
            } else if (command.equals("APPEND_REPLACEMENT")) {
                commandList.add(Command.APPEND_REPLACEMENT);
            } else if (command.equals("APPEND_TAIL")) {
                commandList.add(Command.APPEND_TAIL);
            } else if (command.equals("REPLACE_FIRST")) {
                commandList.add(Command.REPLACE_FIRST);
            } else if (command.equals("REPLACE_ALL")) {
                commandList.add(Command.REPLACE_ALL);
            } else if (command.equals("REGION")) {
                commandList.add(Command.REGION);
            } else if (command.equals("GROUP0")) {
                commandList.add(Command.GROUP0);
            } else if (command.equals("GROUP1")) {
                commandList.add(Command.GROUP1);
            } else if (command.equals("GROUP2")) {
                commandList.add(Command.GROUP2);
            } else if (command.equals("GROUP3")) {
                commandList.add(Command.GROUP3);
            } else if (command.equals("GROUP4")) {
                commandList.add(Command.GROUP4);
            } else if (command.equals("GROUP5")) {
                commandList.add(Command.GROUP5);
            }
        }
        commands = commandList.toArray(new Command[commandList.size()]);
    }

    /**
     * ContentHandler#characters の実装<br>
     *
     * @param ch 文字データを含むchar[]
     * @param start 文字データの ch に置ける開始インデックス
     * @param length 文字データの文字数
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void characters(char[] ch,
                           int start,
                           int length)
                           throws SAXException {
        if (enabledCharacters) {
            if (trimedCharacters) {
                appendTrimedStr(buffer, ch, start, length);
            } else {
                buffer.append(ch, start, length);
            }
        }

        // 文字データの通知を受ける。
        // 引数から String オブジェクトを生成する場合には
        // new String(ch, start, length)
        // となる。
        // StringBuffer オブジェクトに加える場合は
        // stringBuffer.append(ch, start, length)
        // となる。

    }

    /**
     * ContentHandler#setDocumentLocator の実装<br>
     *
     * @param locator Locatorオブジェクト
     */
    public void setDocumentLocator(Locator locator) {
        // パース処理の最中に現在の XML 文書内の位置を
        // 特定するための Locator オブジェクトを受け取る。
        //     private Locator locator;
        //     public void setDocumentLocator(Locator locator) {
        //         this.locator = locator;
        //     }
        // このようにフィールドとして保持しておくことで
        // ContentHandler の他のメソッドが呼び出された際に
        // その時点で解析しているXML文書の内の位置を locator の
        // getLineNumber getColumnNumber メソッドで取得できる。
        // ContentHandler のメソッドの中で一番先に呼び出される。

    }

    /**
     * ContentHandler#startDocument の実装<br>
     *
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void startDocument() throws SAXException {
        // XML 文書の開始の通知を受ける。
        // ContentHandler のメソッドの中で setDocumentLocator メソッドの
        // 次に呼び出される。
    }

    /**
     * ContentHandler#endDocument の実装<br>
     *
     * @exception SAXException SAXExceptionをスローする事ができる。
     */
    public void endDocument() throws SAXException {
        // XML 文書の終了の通知を受ける。
        // jdk1.5 のドキュメントには以下のような記述がある。
        // 「このメソッドのマニュアルと ContentHandler.fatalError() の
        // マニュアルとの間には明らかに矛盾があります。
        // クライアントは、今後のメジャーリリースでこのあいまいさが
        // 解決されないかぎり、パーサが fatalError() を報告したり例外を
        // スローしたときに endDocument() が呼び出されるかどうかを
        // 仮定しないようにする必要があります。」

    }

    /**
     * ContentHandler#startPrefixMapping の実装<br>
     *
     * @param prefix 名前空間をマッピングするプレフィックス名
     * @param uri 定義した名前空間
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void startPrefixMapping(String prefix,
                                   String uri)
                                   throws SAXException {
        // 名前空間が定義された事を通知する。
        // 名前空間を定義した要素の startElement メソッドと
        // このメソッドのどちらが先に呼び出されるかは定義されていない。
        // デフォルトの名前空間を定義してプレフィックスを
        // 定義していない場合には prefix は空の文字列となる。
        //     SAXParserFactory factory = SAXParserFactory.newInstance();
        //     factory.setNamespaceAware(true);
        // としてパーサが名前空間を意識している場合にこの通知を
        // 受ける事ができる。
        // パーサが名前空間を意識しない設定の場合にはこのメソッドは
        // 呼び出されない。
        //
        // <cafe:hoge xmlns:cafe="http://hoge.huga.com/cafe">
        //     <text/>
        // </cafe:hoge>
        // この場合には cafe:hoge 要素に対する startElement メソッドの
        // 前後どちらかで prefix に cafe を uri に
        // http://hoge.huga.com/cafe を引数に設定してこのメソッドが
        // 呼び出される。
        //
        // <hoge xmlns="http://hoge.huga.com/cafe">
        //     <text/>
        // </hoge>
        // この場合には hoge 要素に対する startElement メソッドの
        // 前後どちらかで prefix に空の文字列を uri に
        // http://hoge.huga.com/cafe を引数に設定してこのメソッドが
        // 呼び出される。

    }

    /**
     * ContentHandler#endPrefixMapping の実装<br>
     *
     * @param prefix プレフィックス名
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        // 名前空間の有効範囲の終了を通知する。
        // 名前空間を定義した要素の endElement メソッドと
        // このメソッドのどちらが先に呼び出されるかは定義されていない。
        // デフォルトの名前空間を定義してプレフィックスを
        // 定義していない場合には prefix は空の文字列となる。
        //     SAXParserFactory factory = SAXParserFactory.newInstance();
        //     factory.setNamespaceAware(true);
        // としてパーサが名前空間を意識している場合にこの通知を
        // 受ける事ができる。
        // パーサが名前空間を意識しない設定の場合にはこのメソッドは
        // 呼び出されない。
        //
        // <cafe:hoge xmlns:cafe="http://hoge.huga.com/cafe">
        //     <text/>
        // </cafe:hoge> ←ここで呼び出される
        // この場合には cafe:hoge 要素に対する endElement メソッドの
        // 前後どちらかで prefix に cafe を引数に設定して
        // このメソッドが呼び出される。
        //
        // <hoge xmlns="http://hoge.huga.com/cafe">
        //     <text/>
        // </hoge> ←ここで呼び出される
        // この場合には hoge 要素に対する endElement メソッドの
        // 前後どちらかで prefix に空の文字列を引数に設定して
        // このメソッドが呼び出される。

    }

    /**
     * ContentHandler#ignorableWhitespace の実装<br>
     *
     * @param ch 削除された空白類を含むchar[]
     * @param start 削除された空白類の ch に置ける開始インデックス
     * @param length 削除された空白類の文字数
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void ignorableWhitespace(char[] ch,
                                    int start,
                                    int length)
                                    throws SAXException {
        // パーサが無視できる空白として削除し、charactersメソッドの
        // 引数には含まれなかったスペースや改行などが通知される。
        // 通常はスキーマを基にバリデーションを行っている場合に
        // スキーマの定義をもとにパーサが無視できる空白を判断して
        // それらを削除した場合に、その削除した空白類を通知する。
        // よってバリデーションを行っていない場合には無視できる空白の
        // 削除は行われず、すべてが characters メソッドに通知され
        // このメソッドが呼び出される事も無い、という場合が多い。
        // 仕様ではバリデーションを行っていない場合でもパーサが
        // 無視できる空白を解析して削除し、それをこのメソッドで
        // 通知してもよい事になっている。
        // 引数から String オブジェクトを生成する場合には
        //     new String(ch, start, length)
        // となる。

    }

    /**
     * ContentHandler#processingInstruction の実装<br>
     *
     * @param target プロセッシングインストラクションのターゲット名
     * @param data  XMLプロセッサが解釈する何らかの情報
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void processingInstruction(String target,
                                      String data)
                                      throws SAXException {
        // XML プロセッサ依存情報をXMLドキュメントに記述するために
        // 用いられるプロセッシングインストラクションの通知を受ける。
        // XML 宣言が通知されることはない。
        // XML 宣言以外のプロセッシングインストラクションを含んでいる
        // XML 文書は見たことが無く、あったとしても XML プロセッサに
        // 対する情報なのでパース時にこれを処理する必要はほとんど
        // 無いと思われる。
        //
        // プロセッシングインストラクションは以下のような形式
        // <?hoge foo="bar" aaa?>
        // これは target が hoge になり
        // data は foo="bar" aaa になる。
        // あくまでXMLプロセッサに情報を伝えるものなので
        // XML文書の内容ではない。
        // < と > の中でなければ XML 文書内の任意の場所に
        // 記述できる。
        // data部分に < や > を含めても字句構文チェックは
        // 行われないので
        // <?hoge foo="bar" <aaa>?>
        // はエラーにはならないが
        // <?hoge foo="bar" <aaa?>?>
        // はエラーになる。

    }

    /**
     * ContentHandler#skippedEntity の実装
     *
     * @param name スキップされたエンティティ
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void skippedEntity(String name) throws SAXException {
        // DTD を使っていないので実装は不要(多分)

    }

    /**
     * ErrorHandler#warning の実装<br>
     *
     * @param exception SAXParseExceptionオブジェクト
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void warning(SAXParseException exception)
                        throws SAXException {
        // W3C XML 1.0 勧告セクション 1.2 の「fatal error」にも
        // 「error」にも該当しないが、問題のある部分を
        // 警告として通知するメソッド。
        // このメソッドが呼び出されてもそれは検証エラーではないので
        // XML 文書がバリッドではないという事にはならない。
        // jdk1.5 のドキュメントには「フィルタは、このメソッドを
        // 使用してその他の非 XML 警告も報告できます。」
        // という記述がある。
        // このメソッドが呼び出されてもパースはそのまま続けられるので
        // パース処理を終了したい場合には SAXException か
        // RuntimeException を生成してスローする。

    }

    /**
     * ErrorHandler#error の実装<br>
     *
     * @param exception SAXParseExceptionオブジェクト
     * @exception SAXException SAXExceptionを生成してスローする事で
     *                         パース処理を終了させることができる。
     */
    public void error(SAXParseException exception)
                      throws SAXException {
        // スキーマによる XML 文書検証に失敗した箇所で発生した
        // エラー通知を受けるメソッド。
        // 受け取るエラーの厳密な定義は、W3C XML 1.0 勧告セクション 1.2 の
        // 「error」に記述されている。
        // このメソッドが呼び出されてもパースはそのまま続けられるので
        // パース処理を終了したい場合には SAXException か
        // RuntimeException を生成してスローする。

    }

    /**
     * ErrorHandler#fatalError の実装<br>
     *
     * @param exception SAXParseExceptionオブジェクト
     * @exception SAXException SAXExceptionを生成してスローする事ができる。
     */
    public void fatalError(SAXParseException exception)
                           throws SAXException {
        // XML 文書の整形式が正しくない場合などの致命的なエラーが
        // 発生した場合にその通知を受けるメソッド。
        // 致命的エラーとは W3C XML 1.0 勧告セクション 1.2 の
        // 「fatal error」の定義に相当する。
        // このエラーが発生した場合には SAX パーサはこのメソッドを
        // 呼び出した後、その他のイベントの報告を停止してもかまわない
        // と jdk1.5 のドキュメントにあり、 Sun の jdk1.5 の実装では
        // fatalError が発生した場合には parse メソッドは
        // SAXParseException をスローして終了する。
        //
        // jdk1.5 のドキュメントには以下のような記述がある。
        // 「このメソッドのマニュアルと ContentHandler.endDocument() の
        // マニュアルとの間には明らかに矛盾があります。
        // クライアントは、今後のメジャーリリースでこのあいまいさが
        // 解決されないかぎり、パーサが fatalError() を報告したり例外を
        // スローしたときに endDocument() が呼び出されるかどうかを
        // 仮定しないようにする必要があります。」

    }

    // 引数のStringBufferオブジェクトに
    // new String(ch, start, length).trim() を加える。
    /*
    private void appendTrimedStr(StringBuffer buf,
                                 char[] ch,
                                 int start,
                                 int length) {
        int first = start;
        int last = start + length - 1;
        while ((first <= last) && (ch[first] <= ' ')) {
            first++;
        }
        while ((first <= last) && (ch[last] <= ' ')) {
            last--;
        }
        if (first <= last) {
            buf.append(ch, first, last - first + 1);
        }
    }
    */

    //////////////////////////////////////////////
    // このメソッドはJDK1.5以降でコンパイル可能 //
    //////////////////////////////////////////////
    // 引数のStringBuilderオブジェクトに
    // new String(ch, start, length).trim() を加える。
    private void appendTrimedStr(StringBuilder buf,
                                 char[] ch,
                                 int start,
                                 int length) {
        int first = start;
        int last = start + length - 1;
        while ((first <= last) && (ch[first] <= ' ')) {
            first++;
        }
        while ((first <= last) && (ch[last] <= ' ')) {
            last--;
        }
        if (first <= last) {
            buf.append(ch, first, last - first + 1);
        }
    }
}
