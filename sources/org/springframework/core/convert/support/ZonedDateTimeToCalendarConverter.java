package org.springframework.core.convert.support;

import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/ZonedDateTimeToCalendarConverter.class */
final class ZonedDateTimeToCalendarConverter implements Converter<ZonedDateTime, Calendar> {
    ZonedDateTimeToCalendarConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Calendar convert(ZonedDateTime source) {
        return GregorianCalendar.from(source);
    }
}
