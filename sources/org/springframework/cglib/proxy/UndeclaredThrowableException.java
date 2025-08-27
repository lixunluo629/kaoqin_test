package org.springframework.cglib.proxy;

import org.springframework.cglib.core.CodeGenerationException;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/proxy/UndeclaredThrowableException.class */
public class UndeclaredThrowableException extends CodeGenerationException {
    public UndeclaredThrowableException(Throwable t) {
        super(t);
    }

    public Throwable getUndeclaredThrowable() {
        return getCause();
    }
}
