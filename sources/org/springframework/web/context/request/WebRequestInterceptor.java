package org.springframework.web.context.request;

import org.springframework.ui.ModelMap;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/WebRequestInterceptor.class */
public interface WebRequestInterceptor {
    void preHandle(WebRequest webRequest) throws Exception;

    void postHandle(WebRequest webRequest, ModelMap modelMap) throws Exception;

    void afterCompletion(WebRequest webRequest, Exception exc) throws Exception;
}
