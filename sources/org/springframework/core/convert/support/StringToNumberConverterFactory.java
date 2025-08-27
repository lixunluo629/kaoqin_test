package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.NumberUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToNumberConverterFactory.class */
final class StringToNumberConverterFactory implements ConverterFactory<String, Number> {
    StringToNumberConverterFactory() {
    }

    @Override // org.springframework.core.convert.converter.ConverterFactory
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber(targetType);
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToNumberConverterFactory$StringToNumber.class */
    private static final class StringToNumber<T extends Number> implements Converter<String, T> {
        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override // org.springframework.core.convert.converter.Converter
        public T convert(String str) {
            if (str.isEmpty()) {
                return null;
            }
            return (T) NumberUtils.parseNumber(str, this.targetType);
        }
    }
}
