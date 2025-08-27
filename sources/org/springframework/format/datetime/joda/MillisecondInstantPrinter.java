package org.springframework.format.datetime.joda;

import java.util.Locale;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.format.Printer;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/joda/MillisecondInstantPrinter.class */
public final class MillisecondInstantPrinter implements Printer<Long> {
    private final DateTimeFormatter formatter;

    public MillisecondInstantPrinter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override // org.springframework.format.Printer
    public String print(Long instant, Locale locale) {
        return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(instant.longValue());
    }
}
