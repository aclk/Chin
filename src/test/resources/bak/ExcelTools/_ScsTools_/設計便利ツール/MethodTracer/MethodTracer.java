package murata.ps.bmc.psshippinginstructionsalesbmc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import murata.co.component.core.Cacheable;
import murata.co.core.io.XMLUtilsBean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 共通モジュール系メソッドのInとOutを記録するトレーサ
 * @author K.Miura
 * @version $Id: MethodTracer.java 148745 2008-10-31 12:35:07Z A1S0MEM099 $
 * @since JDK5.0
 */
public class MethodTracer implements MethodInterceptor , Cacheable {

    /** 出力ファイル名のプレフィックスの初期値 */
    protected static final String OUT_FILE_PREFIX = "methodTraceInAndOut";

    /** 出力ファイル名のサイフィックス(拡張子)の初期値 */
    protected static final String OUT_FILE_SYFIX = ".log";

    /** システムごとの改行文字 */
    protected static final String CR = System.getProperty("line.separator");

    /** トレース結果の出力パス */
    protected String eviOutputPath;

    /** 出力ファイル名のプレフィックス */
    protected String outfilePrefix = OUT_FILE_PREFIX;

    // ログの処理は横断的な関心事です
//    private static final Log log = LogFactory.getLog(MethodTracer.class);

    /** 呼び出し回数 */
    protected long callCount = 0;
    /** ファイルの番号(今何個目を吐き出しているか) */
    protected long fileNo = 0;

    /**
     * {@inheritDoc}
     * @throws Throwable
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {

        StringBuilder sb = new StringBuilder();
        Object ret = null;

        // 引数情報出力
        String logHeader = "No" + (++callCount) + ":" + invocation.getThis().getClass().getSimpleName() + "." + invocation.getMethod().getName() + "() を実行。";
        sb.append(logHeader);
        int i = 0;
        for (Object arg : invocation.getArguments()){
            sb.append(CR);
            sb.append("引数");
            sb.append(++i);
            sb.append(" : (型名-");
            sb.append((arg == null) ? "null" : arg.getClass().getName());
            sb.append(")");

            // 引数の値を出力
            if (arg instanceof String
                || arg instanceof Date
                || arg instanceof Long
                || arg instanceof BigDecimal
            ) {
                sb.append(CR);
                sb.append(arg.toString());
            }

            sb.append(CR);
            sb.append((arg == null) ? "null" : createObjectInfoString(arg));
        }
        outLog(sb.toString());

        // 経過時間を記録する
        Date startTime = new Date();

        try {
            // メソッドを実行し、結果を保持
            ret = invocation.proceed();

        } catch (Exception e) {
            sb = new StringBuilder();
            sb.append(logHeader);
            sb.append(CR);
            sb.append("戻り値 : ※Exceptionが発生しています。戻り値不明。");
            sb.append(CR);
            outLog(sb.toString());
            throw e;
        }

        // 戻り値情報出力
        sb = new StringBuilder();
        sb.append(logHeader);
        sb.append(CR);

        sb.append("実行時間 : ");
        sb.append((new Date()).getTime() - startTime.getTime());
        sb.append("ミリ秒");
        sb.append(CR);

        sb.append("戻り値 : (型名-");
        if (ret == null) {
            sb.append("null");
            sb.append(")\n");
        } else {
            sb.append(ret.getClass().getName());
            sb.append(")");
            sb.append(CR);
            if (ret instanceof String
                || ret instanceof Date
                || ret instanceof Long
                || ret instanceof BigDecimal
            ) {
                sb.append(ret.toString());
            } else {
                sb.append(createObjectInfoString(ret));
            }
        }

        outLog(sb.toString());
        return ret;
    }

    /**
     * オブジェクトを解析しプロパティなどの情報を文字列表現で取得<br>
     * (テストFW系の結果VO出力ユティリティを拝借)
     * @param vo 解析するオブジェクト
     * @return 解析結果の文字列表現
     */
    public String createObjectInfoString(Object vo) {
        StringBuilder sb = new StringBuilder();
        List<String> analysisList = ObjectAnalayser.analays(vo);
        if (analysisList !=null) {
            // 文字列取得・連結処理
            for (String outputValue : analysisList) {
                sb.append(outputValue);
                sb.append(CR);
            }
        }
        return sb.toString();
    }

    /**
     * 出力処理。
     * 切り替えられるようにメソッドに切り出します。Logにできるならそれでもよし。
     * @param message
     * @param isAppend
     */
    public void outLog(String message, boolean isAppend) {
        // log.info(message);
        try {
            String outFilePath = (new File(eviOutputPath)).getPath() + File.separator + outfilePrefix + fileNo + OUT_FILE_SYFIX;
            FileWriter fw = new FileWriter(outFilePath , isAppend);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(message);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (Exception e) {}
    }

    public void outLog(String message) {
        outLog(message , true);
    }


    public void initOutMessage(String message) {
        fileNo++;
        callCount = 0;
        outLog(message, false);
    }


    /**
     * eviOutputPathを戻す。
     * <br>
     * @return  eviOutputPath
     */
    public String getEviOutputPath() {
        return eviOutputPath;
    }


    /**
     * eviOutputPathを設定する。
     * <br>
     * @param eviOutputPath String
     */
    public void setEviOutputPath(String eviOutputPath) {
        this.eviOutputPath = eviOutputPath;
    }


    /**
     * outfilePrefixを戻す。
     * <br>
     * @return  outfilePrefix
     */
    public String getOutfilePrefix() {
        return outfilePrefix;
    }


    /**
     * outfilePrefixを設定する。
     * <br>
     * @param outfilePrefix String
     */
    public void setOutfilePrefix(String outfilePrefix) {
        this.outfilePrefix = outfilePrefix;
    }

    /**
     *
     * {@inheritDoc}
     * @see murata.co.component.core.Cacheable#clear()
     */
    public void clear() {
        this.initOutMessage("モジュールのパラメータ(IN,OUT)の記録開始");
    }
}

/**
 * オブジェクト内のプロパティ情報を解析するクラス
 * @author K.Miura
 */
class ObjectAnalayser {

    // インデントの数
    private static int indentCount = 0;

    // 無視するタイプ
    private static final List<String> PASS_TYPES = new ArrayList<String>() {

        {
            super.add("class");
            super.add("pageKeyList");
            super.add("currentKey");
        }
    };

    /**
     * デフォルトコンストラクタ
     * @param arg0 初期化引数
     */
    private ObjectAnalayser() {
    }

    /**
     * 解析呼出メソッド
     * <br />
     * 全ての値を解析し、String型のリストで一括返却を行う
     * @param obj ファイル出力を行うオブジェクト
     * @return 解析結果
     */
    public static List<String> analays(Object obj) {
        // 戻り値リスト
        List<String> analysisList = null;
        try {
            // インデントを初期化(staticのため、ガベージ対象にならなかった場合を考慮)
            indentCount = 0;
            // オブジェクトの解析を開始する
            analysisList = analaysObject(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 結果を返す
        return analysisList;
    }

    /**
     * 出力オブジェクトの解析メソッド
     * <br />
     * 全ての値を解析し、String型のリストで一括返却を行う
     * @param obj ファイル出力を行うオブジェクト
     * @return ファイル出力内容
     */
    private static List<String> analaysObject(Object obj) {

        // 最終的に出力内容を格納する
        List<String> analaysResult = new ArrayList<String>();

        try {
            if (obj == null) {
                return null;
            }

            // List or Map は処理を変更する
            if (obj instanceof Collection) {
                List<String> result = analaysCollection((Collection)obj);
                analaysResult.addAll(result);
            } else if (obj instanceof Map) {
                List<String> result = analaysMap((Map)obj);
                analaysResult.addAll(result);
            } else if (XMLUtilsBean.SupportClasses.isSupport(obj)) {

            } else {
                // 値チェック
                Set<Entry> entrys = PropertyUtils.describe(obj).entrySet();
                for (Entry entry : entrys) {
                    if (PASS_TYPES.contains((String)entry.getKey())) continue;
                    // インデント情報を付加する
                    // 出力値を生成する
                    String message = getIndent() + entry.getKey() + " = ";
                    // 再帰処理判断
                    if (isRecompare(entry.getValue())) {
                        // 再帰的に呼出す
                        analaysResult.add(message);
                        // インデントのカウントアップ
                        indentCount++;
                        analaysResult.addAll(analaysObject(entry.getValue()));
                    } else {
                        // 出力値を生成する
                        // 単独プロパティのため、出力情報としてListへ登録
                        analaysResult.add(message + entry.getValue());
                    }
                }
            }
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }

        // インデント情報が0より大きい場合は戻りの際に一つ戻す
        if (0 < indentCount) {
            indentCount--;
        }

        return analaysResult;
    }

    /**
     * インデント情報を元にスペース文字列を作成し返却する
     * @return インデント文字列
     */
    private static String getIndent() {
        // インデントの数分文字列を生成し返却する
        return StringUtils.repeat("    ", indentCount);
    }

    /**
     * マップクラスを解析する
     * @param valueMap 解析するマップクラス
     * @return 解析結果
     */
    private static List<String> analaysMap(Map valueMap) {
        // 新規リストを生成する
        List<String> analaysResult = new ArrayList<String>();
        // 値チェック(Map設定KeyはString固定ではない)
        Set<Entry> entrys = valueMap.entrySet();
        for (Entry entry : entrys) {
            // 再帰処理判断
            if (isRecompare(entry.getValue())) {
                // インデント情報を付加する
                analaysResult.add(getIndent() + entry.getKey() + " = ");
                // インデントのカウントアップ
                indentCount++;
                analaysResult.addAll(analaysObject(entry.getValue()));
            } else {
                // 出力値を生成する
                // 単独プロパティのため、出力情報としてListへ登録
                analaysResult.add(getIndent() + entry.getKey() + " = "
                    + entry.getValue());
            }
        }
        // 結果を返却する
        return analaysResult;
    }

    /**
     * リストクラスを解析する。
     * @param values 解析するリストクラス
     * @return 解析結果
     */
    private static List<String> analaysCollection(Collection values) {
        // 要素数カウント
        int i = 0;
        // 新規リストを生成する
        List<String> analaysResult = new ArrayList<String>();
        for (Object value : values) {
            String elementCount = "第" + (i++) + "要素"; // カウントアップ
            // 再帰処理判断
            if (isRecompare(value)) {
                analaysResult.add(getIndent() + elementCount + " = ");
                // インデントのカウントアップ
                indentCount++;
                analaysResult.addAll(analaysObject(value));
            } else {
                // インデント情報を付加する
                // 単独プロパティのため、出力情報としてListへ登録
                analaysResult.add(getIndent() + elementCount + " = " + value);
            }
        }
        return analaysResult;
    }

    /**
     * 再帰的に比較メソッドを呼び出すかの判断メソッド。<br>
     * objectが「List」、「Map」の場合、再帰処理を実行します。
     * @param result 値出力オブジェクト
     * @return true：再帰処理対象、false：再帰処理対象外
     */
    private static boolean isRecompare(Object outputObj) {
        if (outputObj == null) {
            return false;
        } else if (outputObj instanceof Set || outputObj instanceof List
            || outputObj instanceof Map) {
            return true;
        } else if (outputObj.getClass().getSimpleName().endsWith("VO")) {
            // VOは接尾辞で判断する(ネーミングルールに準拠)
            return true;
        } else if (outputObj.getClass().getPackage().getName()
            .indexOf("entity") > 0) {
            // entityはPackage名称から判断を行なう(ネーミングルールに準拠(パッケージ構成))
            return true;
        }
        return false;
    }
}
