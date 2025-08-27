package org.springframework.core.convert.support;

import java.util.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToLocaleConverter.class */
final class StringToLocaleConverter implements Converter<String, Locale> {
    StringToLocaleConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Locale convert(String source) {
        return StringUtils.parseLocaleString(source);
    }
}
