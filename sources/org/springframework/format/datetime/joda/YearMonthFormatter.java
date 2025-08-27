package org.springframework.format.datetime.joda;

import java.text.ParseException;
import java.util.Locale;
import org.joda.time.YearMonth;
import org.springframework.format.Formatter;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/joda/YearMonthFormatter.class */
class YearMonthFormatter implements Formatter<YearMonth> {
    YearMonthFormatter() {
    }

    @Override // org.springframework.format.Parser
    public YearMonth parse(String text, Locale locale) throws ParseException {
        return YearMonth.parse(text);
    }

    @Override // org.springframework.format.Printer
    public String print(YearMonth object, Locale locale) {
        return object.toString();
    }
}
