package org.springframework.jmx;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/JmxException.class */
public class JmxException extends NestedRuntimeException {
    public JmxException(String msg) {
        super(msg);
    }

    public JmxException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
