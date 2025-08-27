package org.springframework.web.context.request.async;

import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/AsyncWebRequest.class */
public interface AsyncWebRequest extends NativeWebRequest {
    void setTimeout(Long l);

    void addTimeoutHandler(Runnable runnable);

    void addCompletionHandler(Runnable runnable);

    void startAsync();

    boolean isAsyncStarted();

    void dispatch();

    boolean isAsyncComplete();
}
