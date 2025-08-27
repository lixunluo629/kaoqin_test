package org.springframework.core.convert.support;

import java.util.TimeZone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.UsesJava8;
import org.springframework.util.StringUtils;

@UsesJava8
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToTimeZoneConverter.class */
class StringToTimeZoneConverter implements Converter<String, TimeZone> {
    StringToTimeZoneConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public TimeZone convert(String source) {
        return StringUtils.parseTimeZoneString(source);
    }
}
