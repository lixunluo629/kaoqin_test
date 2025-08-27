package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToEnumConverterFactory.class */
final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {
    StringToEnumConverterFactory() {
    }

    @Override // org.springframework.core.convert.converter.ConverterFactory
    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum(ConversionUtils.getEnumType(targetType));
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/StringToEnumConverterFactory$StringToEnum.class */
    private class StringToEnum<T extends Enum> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override // org.springframework.core.convert.converter.Converter
        public T convert(String str) {
            if (str.isEmpty()) {
                return null;
            }
            return (T) Enum.valueOf(this.enumType, str.trim());
        }
    }
}
