package org.springframework.format.datetime.joda;

import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/joda/JodaTimeContext.class */
public class JodaTimeContext {
    private Chronology chronology;
    private DateTimeZone timeZone;

    public void setChronology(Chronology chronology) {
        this.chronology = chronology;
    }

    public Chronology getChronology() {
        return this.chronology;
    }

    public void setTimeZone(DateTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public DateTimeZone getTimeZone() {
        return this.timeZone;
    }

    public DateTimeFormatter getFormatter(DateTimeFormatter formatter) {
        TimeZone timeZone;
        if (this.chronology != null) {
            formatter = formatter.withChronology(this.chronology);
        }
        if (this.timeZone != null) {
            formatter = formatter.withZone(this.timeZone);
        } else {
            LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
            if ((localeContext instanceof TimeZoneAwareLocaleContext) && (timeZone = ((TimeZoneAwareLocaleContext) localeContext).getTimeZone()) != null) {
                formatter = formatter.withZone(DateTimeZone.forTimeZone(timeZone));
            }
        }
        return formatter;
    }
}
