package org.springframework.core.convert.support;

import java.nio.charset.Charset;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToCharsetConverter.class */
class StringToCharsetConverter implements Converter<String, Charset> {
    StringToCharsetConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Charset convert(String source) {
        return Charset.forName(source);
    }
}
