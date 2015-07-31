package com.wayso.slf4jexam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class App {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(App.class);

        // バインドしているログフレームワークのマップ化コンテキストと連携
        // Log 用の ThreadLocal な Map
        // Log4j の MDC に対応 (NDC((スタック))に対応する者は無い)
        MDC.put("uid", "USER01234");

        logger.trace("Hello Trace");
        logger.debug("Hello Debug");
        logger.warn("Hello Warn");
        logger.info("Hello Info");
        logger.error("Hello Error");
        // fatal は無い

        logger.info("\\{} はプレースホルダ => {} is not {}", new Object[] { "1", "2" });
        logger.info("\\{} に順版を指定できない => {1} is not {0}", new Object[] { "1",
                "2" });

        try {
            throw new NullPointerException("ぬるぽ");
        } catch (NullPointerException e) {
            // logback では、設定ファイルで Marker によって出力する・しないのフィルタリングができる
            // たとえば ERROR レベルで、syslogMarker マーカーのついているログだけ、SYSLOG に出力して
            // 運用監視するとか
            //
            // <turboFilter class="ch.qos.logback.classic.turbo.MarkerFilter">
            // <Marker>REPORT</Marker>
            // <OnMatch>ACCEPT</OnMatch>
            // </turboFilter>

            Marker syslogMarker = MarkerFactory.getMarker("REPORT");
            // 引数にthrowableを入れるとスタックトレースが出る
            logger.error(syslogMarker, "エラーが発生しました", e);
        }

        // MDC は、処理終了時に必ず clear() する
        // (特に Web アプリなどで ThreadPool で Thread が使いまわされる可能性があるので)
        MDC.clear();
    }
}
