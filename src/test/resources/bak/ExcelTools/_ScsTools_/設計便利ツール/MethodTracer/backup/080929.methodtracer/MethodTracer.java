package murata.ss.sz.szo0040.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import murata.co.test.producttest.testcase.ObjectAnalayser;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 共通モジュール系メソッドのInとOutを記録するトレーサ
 * @author K.Miura
 * @version $Id: MethodTracer.java 68504 2008-04-15 10:29:44Z A1B5MEMB $
 * @since JDK5.0
 */
public class MethodTracer implements MethodInterceptor {

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
            sb.append(arg.getClass().getName());
            sb.append(")");
            sb.append(CR);
            sb.append((arg == null) ? "null" : createObjectInfoString(arg));
        }
        outLog(sb.toString());

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
}
