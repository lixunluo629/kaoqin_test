package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/NumberToCharacterConverter.class */
final class NumberToCharacterConverter implements Converter<Number, Character> {
    NumberToCharacterConverter() {
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Character convert(Number source) {
        return Character.valueOf((char) source.shortValue());
    }
}
