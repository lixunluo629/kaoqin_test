package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/IntegerToEnumConverterFactory.class */
final class IntegerToEnumConverterFactory implements ConverterFactory<Integer, Enum> {
    IntegerToEnumConverterFactory() {
    }

    @Override // org.springframework.core.convert.converter.ConverterFactory
    public <T extends Enum> Converter<Integer, T> getConverter(Class<T> targetType) {
        return new IntegerToEnum(ConversionUtils.getEnumType(targetType));
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/IntegerToEnumConverterFactory$IntegerToEnum.class */
    private class IntegerToEnum<T extends Enum> implements Converter<Integer, T> {
        private final Class<T> enumType;

        public IntegerToEnum(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override // org.springframework.core.convert.converter.Converter
        public T convert(Integer source) {
            return this.enumType.getEnumConstants()[source.intValue()];
        }
    }
}
