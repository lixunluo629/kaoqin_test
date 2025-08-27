package org.springframework.web.bind.support;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/bind/support/WebArgumentResolver.class */
public interface WebArgumentResolver {
    public static final Object UNRESOLVED = new Object();

    Object resolveArgument(MethodParameter methodParameter, NativeWebRequest nativeWebRequest) throws Exception;
}
