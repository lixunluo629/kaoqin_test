package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/ObjectToStringConverter.class */
final class ObjectToStringConverter implements Converter<Object, String> {
    ObjectToStringConverter() {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.core.convert.converter.Converter
    public String convert(Object source) {
        return source.toString();
    }
}
