package org.springframework.context;

import org.springframework.beans.FatalBeanException;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ApplicationContextException.class */
public class ApplicationContextException extends FatalBeanException {
    public ApplicationContextException(String msg) {
        super(msg);
    }

    public ApplicationContextException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
