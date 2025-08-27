package org.springframework.web.bind;

import org.springframework.web.util.NestedServletException;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/ServletRequestBindingException.class */
public class ServletRequestBindingException extends NestedServletException {
    public ServletRequestBindingException(String msg) {
        super(msg);
    }

    public ServletRequestBindingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
