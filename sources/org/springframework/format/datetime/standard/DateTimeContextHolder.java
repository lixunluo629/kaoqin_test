package org.springframework.format.datetime.standard;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.core.NamedThreadLocal;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/standard/DateTimeContextHolder.class */
public final class DateTimeContextHolder {
    private static final ThreadLocal<DateTimeContext> dateTimeContextHolder = new NamedThreadLocal("DateTimeContext");

    public static void resetDateTimeContext() {
        dateTimeContextHolder.remove();
    }

    public static void setDateTimeContext(DateTimeContext dateTimeContext) {
        if (dateTimeContext == null) {
            resetDateTimeContext();
        } else {
            dateTimeContextHolder.set(dateTimeContext);
        }
    }

    public static DateTimeContext getDateTimeContext() {
        return dateTimeContextHolder.get();
    }

    public static DateTimeFormatter getFormatter(DateTimeFormatter formatter, Locale locale) {
        DateTimeFormatter formatterToUse = locale != null ? formatter.withLocale(locale) : formatter;
        DateTimeContext context = getDateTimeContext();
        return context != null ? context.getFormatter(formatterToUse) : formatterToUse;
    }
}
