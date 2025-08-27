package org.springframework.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/AsyncHandlerInterceptor.class */
public interface AsyncHandlerInterceptor extends HandlerInterceptor {
    void afterConcurrentHandlingStarted(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj) throws Exception;
}
