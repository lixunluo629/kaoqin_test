package org.springframework.core.convert;

import org.springframework.util.ObjectUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/ConversionFailedException.class */
public class ConversionFailedException extends ConversionException {
    private final TypeDescriptor sourceType;
    private final TypeDescriptor targetType;
    private final Object value;

    public ConversionFailedException(TypeDescriptor sourceType, TypeDescriptor targetType, Object value, Throwable cause) {
        super("Failed to convert from type [" + sourceType + "] to type [" + targetType + "] for value '" + ObjectUtils.nullSafeToString(value) + "'", cause);
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.value = value;
    }

    public TypeDescriptor getSourceType() {
        return this.sourceType;
    }

    public TypeDescriptor getTargetType() {
        return this.targetType;
    }

    public Object getValue() {
        return this.value;
    }
}
