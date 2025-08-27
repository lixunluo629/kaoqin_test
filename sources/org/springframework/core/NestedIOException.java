package org.springframework.core;

import java.io.IOException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/NestedIOException.class */
public class NestedIOException extends IOException {
    static {
        NestedExceptionUtils.class.getName();
    }

    public NestedIOException(String msg) {
        super(msg);
    }

    public NestedIOException(String msg, Throwable cause) {
        super(msg, cause);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }
}
