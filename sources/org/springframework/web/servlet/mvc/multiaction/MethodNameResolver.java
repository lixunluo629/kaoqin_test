package org.springframework.web.servlet.mvc.multiaction;

import javax.servlet.http.HttpServletRequest;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/multiaction/MethodNameResolver.class */
public interface MethodNameResolver {
    String getHandlerMethodName(HttpServletRequest httpServletRequest) throws NoSuchRequestHandlingMethodException;
}
