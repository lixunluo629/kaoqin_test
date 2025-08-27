package org.springframework.core.convert.converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/converter/ConverterFactory.class */
public interface ConverterFactory<S, R> {
    <T extends R> Converter<S, T> getConverter(Class<T> cls);
}
