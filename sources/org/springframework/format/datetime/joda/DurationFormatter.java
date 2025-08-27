package org.springframework.format.datetime.joda;

import java.text.ParseException;
import java.util.Locale;
import org.joda.time.Duration;
import org.springframework.format.Formatter;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/joda/DurationFormatter.class */
class DurationFormatter implements Formatter<Duration> {
    DurationFormatter() {
    }

    @Override // org.springframework.format.Parser
    public Duration parse(String text, Locale locale) throws ParseException {
        return Duration.parse(text);
    }

    @Override // org.springframework.format.Printer
    public String print(Duration object, Locale locale) {
        return object.toString();
    }
}
