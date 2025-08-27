package org.springframework.web.util;

import javax.servlet.ServletException;
import org.springframework.core.NestedExceptionUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/util/NestedServletException.class */
public class NestedServletException extends ServletException {
    private static final long serialVersionUID = -5292377985529381145L;

    static {
        NestedExceptionUtils.class.getName();
    }

    public NestedServletException(String msg) {
        super(msg);
    }

    public NestedServletException(String msg, Throwable cause) {
        super(msg, cause);
        if (getCause() == null && cause != null) {
            initCause(cause);
        }
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }
}
