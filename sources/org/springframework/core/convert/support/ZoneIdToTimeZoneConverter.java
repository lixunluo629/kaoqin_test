package org.springframework.core.convert.support;

import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.UsesJava8;

@UsesJava8
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/ZoneIdToTimeZoneConverter.class */
final class ZoneIdToTimeZoneConverter implements Converter<ZoneId, TimeZone> {
    ZoneIdToTimeZoneConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public TimeZone convert(ZoneId source) {
        return TimeZone.getTimeZone(source);
    }
}
