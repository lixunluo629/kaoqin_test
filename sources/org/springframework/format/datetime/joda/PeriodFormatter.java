package org.springframework.format.datetime.joda;

import java.text.ParseException;
import java.util.Locale;
import org.joda.time.Period;
import org.springframework.format.Formatter;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/joda/PeriodFormatter.class */
class PeriodFormatter implements Formatter<Period> {
    PeriodFormatter() {
    }

    @Override // org.springframework.format.Parser
    public Period parse(String text, Locale locale) throws ParseException {
        return Period.parse(text);
    }

    @Override // org.springframework.format.Printer
    public String print(Period object, Locale locale) {
        return object.toString();
    }
}
