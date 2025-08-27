package org.springframework.aop.framework;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/AopConfigException.class */
public class AopConfigException extends NestedRuntimeException {
    public AopConfigException(String msg) {
        super(msg);
    }

    public AopConfigException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
