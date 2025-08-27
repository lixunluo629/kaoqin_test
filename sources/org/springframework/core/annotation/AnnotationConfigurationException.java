package org.springframework.core.annotation;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotationConfigurationException.class */
public class AnnotationConfigurationException extends NestedRuntimeException {
    public AnnotationConfigurationException(String message) {
        super(message);
    }

    public AnnotationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
