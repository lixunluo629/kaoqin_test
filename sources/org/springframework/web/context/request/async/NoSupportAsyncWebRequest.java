package org.springframework.web.context.request.async;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.ServletWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/NoSupportAsyncWebRequest.class */
public class NoSupportAsyncWebRequest extends ServletWebRequest implements AsyncWebRequest {
    public NoSupportAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void addCompletionHandler(Runnable runnable) {
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void setTimeout(Long timeout) {
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void addTimeoutHandler(Runnable runnable) {
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public boolean isAsyncStarted() {
        return false;
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void startAsync() {
        throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public boolean isAsyncComplete() {
        throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void dispatch() {
        throw new UnsupportedOperationException("No async support in a pre-Servlet 3.0 runtime");
    }
}
