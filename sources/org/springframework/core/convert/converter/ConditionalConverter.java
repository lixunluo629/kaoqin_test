package org.springframework.core.convert.converter;

import org.springframework.core.convert.TypeDescriptor;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/converter/ConditionalConverter.class */
public interface ConditionalConverter {
    boolean matches(TypeDescriptor typeDescriptor, TypeDescriptor typeDescriptor2);
}
