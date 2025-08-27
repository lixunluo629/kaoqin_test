package org.springframework.ejb.access;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ejb/access/EjbAccessException.class */
public class EjbAccessException extends NestedRuntimeException {
    public EjbAccessException(String msg) {
        super(msg);
    }

    public EjbAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
