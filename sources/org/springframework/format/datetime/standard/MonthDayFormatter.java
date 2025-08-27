package org.springframework.format.datetime.standard;

import java.text.ParseException;
import java.time.MonthDay;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/standard/MonthDayFormatter.class */
class MonthDayFormatter implements Formatter<MonthDay> {
    MonthDayFormatter() {
    }

    @Override // org.springframework.format.Parser
    public MonthDay parse(String text, Locale locale) throws ParseException {
        return MonthDay.parse(text);
    }

    @Override // org.springframework.format.Printer
    public String print(MonthDay object, Locale locale) {
        return object.toString();
    }
}
