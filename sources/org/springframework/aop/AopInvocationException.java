package org.springframework.aop;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/AopInvocationException.class */
public class AopInvocationException extends NestedRuntimeException {
    public AopInvocationException(String msg) {
        super(msg);
    }

    public AopInvocationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
