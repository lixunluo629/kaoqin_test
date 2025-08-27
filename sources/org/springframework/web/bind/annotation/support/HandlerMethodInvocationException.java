package org.springframework.web.bind.annotation.support;

import java.lang.reflect.Method;
import org.springframework.core.NestedRuntimeException;

@Deprecated
/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/annotation/support/HandlerMethodInvocationException.class */
public class HandlerMethodInvocationException extends NestedRuntimeException {
    public HandlerMethodInvocationException(Method handlerMethod, Throwable cause) {
        super("Failed to invoke handler method [" + handlerMethod + "]", cause);
    }
}
