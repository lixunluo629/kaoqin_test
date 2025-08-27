package org.springframework.core.convert;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/ConversionService.class */
public interface ConversionService {
    boolean canConvert(Class<?> cls, Class<?> cls2);

    boolean canConvert(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);

    <T> T convert(Object obj, Class<T> cls);

    Object convert(Object obj, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);
}
