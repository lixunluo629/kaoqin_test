package org.springframework.core.convert.converter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/converter/Converter.class */
public interface Converter<S, T> {
    T convert(S s);
}
