package com.wayso.slf4jexam;

import java.util.Locale;
import org.slf4j.cal10n.LocLogger;

public class App2 {

    public static void main(String[] args) {
        LocLogger logger = MyLoggerFactory.getLogger(App.class);

        logger.info(Msg.INFORM_START_ACTION, "TEST");
        // LocLogger で、外部定義のメッセージテンプレートを使うときには、
        // プレースホルダに順番を設定できる {0} is {1}
        logger.warn(Msg.BIZERR_RESERVE_SHORTAGE, "Mario Bros. Co.", 18000);
        // メッセージテンプレートが即値の場合には Logger とおなじ
        logger.debug("顧客ID={}, 売掛金={}, 回収金={}", 01234, 141400, 123400);
        logger.info(Msg.INFORM_END_ACTION, "TEST");

        LocLogger logger2 = MyLoggerFactory.getLogger(App.class, Locale.UK);

        logger2.info(Msg.INFORM_START_ACTION, "TEST");
        logger2.warn(Msg.BIZERR_RESERVE_SHORTAGE, "Mario Bros. Co.", 18000);
        logger.debug("customer_id={}, book_credit={}, claw_back={}", 01234,
                141400, 123400);
        logger2.info(Msg.INFORM_END_ACTION, "TEST");
    }
}
