package org.springframework.format.datetime.standard;

import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/standard/InstantFormatter.class */
public class InstantFormatter implements Formatter<Instant> {
    @Override // org.springframework.format.Parser
    public Instant parse(String text, Locale locale) throws ParseException {
        if (text.length() > 0 && Character.isDigit(text.charAt(0))) {
            return Instant.parse(text);
        }
        return Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(text));
    }

    @Override // org.springframework.format.Printer
    public String print(Instant object, Locale locale) {
        return object.toString();
    }
}
