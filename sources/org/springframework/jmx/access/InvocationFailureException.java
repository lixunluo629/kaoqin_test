package org.springframework.jmx.access;

import org.springframework.jmx.JmxException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/access/InvocationFailureException.class */
public class InvocationFailureException extends JmxException {
    public InvocationFailureException(String msg) {
        super(msg);
    }

    public InvocationFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
