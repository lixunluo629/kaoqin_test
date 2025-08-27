package org.springframework.jmx.access;

import javax.management.JMRuntimeException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/access/InvalidInvocationException.class */
public class InvalidInvocationException extends JMRuntimeException {
    public InvalidInvocationException(String msg) {
        super(msg);
    }
}
