package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/support/AsyncHandlerMethodReturnValueHandler.class */
public interface AsyncHandlerMethodReturnValueHandler extends HandlerMethodReturnValueHandler {
    boolean isAsyncReturnValue(Object obj, MethodParameter methodParameter);
}
