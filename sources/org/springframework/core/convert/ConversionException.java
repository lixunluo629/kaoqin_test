package org.springframework.core.convert;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/ConversionException.class */
public abstract class ConversionException extends NestedRuntimeException {
    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
