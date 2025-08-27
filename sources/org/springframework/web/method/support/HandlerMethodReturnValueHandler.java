package org.springframework.web.method.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/support/HandlerMethodReturnValueHandler.class */
public interface HandlerMethodReturnValueHandler {
    boolean supportsReturnType(MethodParameter methodParameter);

    void handleReturnValue(Object obj, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception;
}
