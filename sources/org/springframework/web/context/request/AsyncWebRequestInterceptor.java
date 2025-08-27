package org.springframework.web.context.request;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/AsyncWebRequestInterceptor.class */
public interface AsyncWebRequestInterceptor extends WebRequestInterceptor {
    void afterConcurrentHandlingStarted(WebRequest webRequest);
}
