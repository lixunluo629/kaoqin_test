package org.springframework.scheduling;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scheduling/SchedulingException.class */
public class SchedulingException extends NestedRuntimeException {
    public SchedulingException(String msg) {
        super(msg);
    }

    public SchedulingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
