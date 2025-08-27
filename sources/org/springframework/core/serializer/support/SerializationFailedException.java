package org.springframework.core.serializer.support;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/serializer/support/SerializationFailedException.class */
public class SerializationFailedException extends NestedRuntimeException {
    public SerializationFailedException(String message) {
        super(message);
    }

    public SerializationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
