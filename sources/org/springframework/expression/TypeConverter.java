package org.springframework.expression;

import org.springframework.core.convert.TypeDescriptor;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/TypeConverter.class */
public interface TypeConverter {
    boolean canConvert(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);

    Object convertValue(Object obj, TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);
}
