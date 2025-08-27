package org.springframework.web.context.request.async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/StandardServletAsyncWebRequest.class */
public class StandardServletAsyncWebRequest extends ServletWebRequest implements AsyncWebRequest, AsyncListener {
    private Long timeout;
    private AsyncContext asyncContext;
    private AtomicBoolean asyncCompleted;
    private final List<Runnable> timeoutHandlers;
    private ErrorHandler errorHandler;
    private final List<Runnable> completionHandlers;

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/StandardServletAsyncWebRequest$ErrorHandler.class */
    interface ErrorHandler {
        void handle(Throwable th);
    }

    public StandardServletAsyncWebRequest(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.asyncCompleted = new AtomicBoolean(false);
        this.timeoutHandlers = new ArrayList();
        this.completionHandlers = new ArrayList();
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void setTimeout(Long timeout) {
        Assert.state(!isAsyncStarted(), "Cannot change the timeout with concurrent handling in progress");
        this.timeout = timeout;
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void addTimeoutHandler(Runnable timeoutHandler) {
        this.timeoutHandlers.add(timeoutHandler);
    }

    void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void addCompletionHandler(Runnable runnable) {
        this.completionHandlers.add(runnable);
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public boolean isAsyncStarted() {
        return this.asyncContext != null && getRequest().isAsyncStarted();
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public boolean isAsyncComplete() {
        return this.asyncCompleted.get();
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void startAsync() {
        Assert.state(getRequest().isAsyncSupported(), "Async support must be enabled on a servlet and for all filters involved in async request processing. This is done in Java code using the Servlet API or by adding \"<async-supported>true</async-supported>\" to servlet and filter declarations in web.xml.");
        Assert.state(!isAsyncComplete(), "Async processing has already completed");
        if (isAsyncStarted()) {
            return;
        }
        this.asyncContext = getRequest().startAsync(getRequest(), getResponse());
        this.asyncContext.addListener(this);
        if (this.timeout != null) {
            this.asyncContext.setTimeout(this.timeout.longValue());
        }
    }

    @Override // org.springframework.web.context.request.async.AsyncWebRequest
    public void dispatch() {
        Assert.notNull(this.asyncContext, "Cannot dispatch without an AsyncContext");
        this.asyncContext.dispatch();
    }

    @Override // javax.servlet.AsyncListener
    public void onStartAsync(AsyncEvent event) throws IOException {
    }

    @Override // javax.servlet.AsyncListener
    public void onError(AsyncEvent event) throws IOException {
        if (this.errorHandler != null) {
            this.errorHandler.handle(event.getThrowable());
        }
    }

    @Override // javax.servlet.AsyncListener
    public void onTimeout(AsyncEvent event) throws IOException {
        for (Runnable handler : this.timeoutHandlers) {
            handler.run();
        }
    }

    @Override // javax.servlet.AsyncListener
    public void onComplete(AsyncEvent event) throws IOException {
        for (Runnable handler : this.completionHandlers) {
            handler.run();
        }
        this.asyncContext = null;
        this.asyncCompleted.set(true);
    }
}
