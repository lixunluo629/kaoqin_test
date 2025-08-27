package org.springframework.boot.bind;

import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/StringToCharArrayConverter.class */
class StringToCharArrayConverter implements Converter<String, char[]> {
    StringToCharArrayConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public char[] convert(String source) {
        return source.toCharArray();
    }
}
