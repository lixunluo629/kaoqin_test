package org.springframework.format.datetime.standard;

import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/format/datetime/standard/DateTimeContext.class */
public class DateTimeContext {
    private Chronology chronology;
    private ZoneId timeZone;

    public void setChronology(Chronology chronology) {
        this.chronology = chronology;
    }

    public Chronology getChronology() {
        return this.chronology;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public ZoneId getTimeZone() {
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
                formatter = formatter.withZone(timeZone.toZoneId());
            }
        }
        return formatter;
    }
}
