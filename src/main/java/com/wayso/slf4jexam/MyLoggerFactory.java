package com.wayso.slf4jexam;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import java.util.Locale;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

public class MyLoggerFactory {

    public static LocLogger getLogger(Class<?> clazz) {
        return getLogger(clazz, Locale.getDefault());
    }

    public static LocLogger getLogger(Class<?> clazz, Locale locale) {
        IMessageConveyor message = new MessageConveyor(locale);
        LocLoggerFactory factory = new LocLoggerFactory(message);
        return factory.getLocLogger(clazz);
    }
}
