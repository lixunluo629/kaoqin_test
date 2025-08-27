package org.springframework.core.convert.support;

import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/ConversionServiceFactory.class */
public abstract class ConversionServiceFactory {
    public static void registerConverters(Set<?> converters, ConverterRegistry registry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                    registry.addConverter((GenericConverter) converter);
                } else if (converter instanceof Converter) {
                    registry.addConverter((Converter<?, ?>) converter);
                } else if (converter instanceof ConverterFactory) {
                    registry.addConverterFactory((ConverterFactory) converter);
                } else {
                    throw new IllegalArgumentException("Each converter object must implement one of the Converter, ConverterFactory, or GenericConverter interfaces");
                }
            }
        }
    }
}
