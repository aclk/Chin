package com.wayso.slf4jexam;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;
 
@BaseName("template")
@LocaleData( { @Locale("en_UK"), @Locale("ja_JP") })
public enum Msg  {
    SYSERR_CANNOT_CONNECT_DB,
    BIZERR_RESERVE_SHORTAGE,
    INFORM_START_ACTION,
    INFORM_END_ACTION
}
