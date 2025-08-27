package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/EnumToIntegerConverter.class */
final class EnumToIntegerConverter extends AbstractConditionalEnumConverter implements Converter<Enum<?>, Integer> {
    public EnumToIntegerConverter(ConversionService conversionService) {
        super(conversionService);
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Integer convert(Enum<?> source) {
        return Integer.valueOf(source.ordinal());
    }
}
