package org.springframework.core.convert.converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/converter/ConverterRegistry.class */
public interface ConverterRegistry {
    void addConverter(Converter<?, ?> converter);

    <S, T> void addConverter(Class<S> cls, Class<T> cls2, Converter<? super S, ? extends T> converter);

    void addConverter(GenericConverter genericConverter);

    void addConverterFactory(ConverterFactory<?, ?> converterFactory);

    void removeConvertible(Class<?> cls, Class<?> cls2);
}
