package org.springframework.web.context.request.async;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.StandardServletAsyncWebRequest;
import org.springframework.web.util.UrlPathHelper;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/WebAsyncManager.class */
public final class WebAsyncManager {
    private static final Object RESULT_NONE = new Object();
    private static final Log logger = LogFactory.getLog(WebAsyncManager.class);
    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();
    private static final CallableProcessingInterceptor timeoutCallableInterceptor = new TimeoutCallableProcessingInterceptor();
    private static final DeferredResultProcessingInterceptor timeoutDeferredResultInterceptor = new TimeoutDeferredResultProcessingInterceptor();
    private AsyncWebRequest asyncWebRequest;
    private volatile Object[] concurrentResultContext;
    private AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(getClass().getSimpleName());
    private volatile Object concurrentResult = RESULT_NONE;
    private final Map<Object, CallableProcessingInterceptor> callableInterceptors = new LinkedHashMap();
    private final Map<Object, DeferredResultProcessingInterceptor> deferredResultInterceptors = new LinkedHashMap();

    WebAsyncManager() {
    }

    public void setAsyncWebRequest(final AsyncWebRequest asyncWebRequest) {
        Assert.notNull(asyncWebRequest, "AsyncWebRequest must not be null");
        this.asyncWebRequest = asyncWebRequest;
        this.asyncWebRequest.addCompletionHandler(new Runnable() { // from class: org.springframework.web.context.request.async.WebAsyncManager.1
            @Override // java.lang.Runnable
            public void run() {
                asyncWebRequest.removeAttribute(WebAsyncUtils.WEB_ASYNC_MANAGER_ATTRIBUTE, 0);
            }
        });
    }

    public void setTaskExecutor(AsyncTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public boolean isConcurrentHandlingStarted() {
        return this.asyncWebRequest != null && this.asyncWebRequest.isAsyncStarted();
    }

    public boolean hasConcurrentResult() {
        return this.concurrentResult != RESULT_NONE;
    }

    public Object getConcurrentResult() {
        return this.concurrentResult;
    }

    public Object[] getConcurrentResultContext() {
        return this.concurrentResultContext;
    }

    public CallableProcessingInterceptor getCallableInterceptor(Object key) {
        return this.callableInterceptors.get(key);
    }

    public DeferredResultProcessingInterceptor getDeferredResultInterceptor(Object key) {
        return this.deferredResultInterceptors.get(key);
    }

    public void registerCallableInterceptor(Object key, CallableProcessingInterceptor interceptor) {
        Assert.notNull(key, "Key is required");
        Assert.notNull(interceptor, "CallableProcessingInterceptor  is required");
        this.callableInterceptors.put(key, interceptor);
    }

    public void registerCallableInterceptors(CallableProcessingInterceptor... interceptors) {
        Assert.notNull(interceptors, "A CallableProcessingInterceptor is required");
        for (CallableProcessingInterceptor interceptor : interceptors) {
            String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
            this.callableInterceptors.put(key, interceptor);
        }
    }

    public void registerDeferredResultInterceptor(Object key, DeferredResultProcessingInterceptor interceptor) {
        Assert.notNull(key, "Key is required");
        Assert.notNull(interceptor, "DeferredResultProcessingInterceptor is required");
        this.deferredResultInterceptors.put(key, interceptor);
    }

    public void registerDeferredResultInterceptors(DeferredResultProcessingInterceptor... interceptors) {
        Assert.notNull(interceptors, "A DeferredResultProcessingInterceptor is required");
        for (DeferredResultProcessingInterceptor interceptor : interceptors) {
            String key = interceptor.getClass().getName() + ":" + interceptor.hashCode();
            this.deferredResultInterceptors.put(key, interceptor);
        }
    }

    public void clearConcurrentResult() {
        synchronized (this) {
            this.concurrentResult = RESULT_NONE;
            this.concurrentResultContext = null;
        }
    }

    public void startCallableProcessing(Callable<?> callable, Object... processingContext) throws Exception {
        Assert.notNull(callable, "Callable must not be null");
        startCallableProcessing(new WebAsyncTask<>(callable), processingContext);
    }

    public void startCallableProcessing(WebAsyncTask<?> webAsyncTask, Object... processingContext) throws Exception {
        Assert.notNull(webAsyncTask, "WebAsyncTask must not be null");
        Assert.state(this.asyncWebRequest != null, "AsyncWebRequest must not be null");
        Long timeout = webAsyncTask.getTimeout();
        if (timeout != null) {
            this.asyncWebRequest.setTimeout(timeout);
        }
        AsyncTaskExecutor executor = webAsyncTask.getExecutor();
        if (executor != null) {
            this.taskExecutor = executor;
        }
        List<CallableProcessingInterceptor> interceptors = new ArrayList<>();
        interceptors.add(webAsyncTask.getInterceptor());
        interceptors.addAll(this.callableInterceptors.values());
        interceptors.add(timeoutCallableInterceptor);
        final Callable<?> callable = webAsyncTask.getCallable();
        final CallableInterceptorChain interceptorChain = new CallableInterceptorChain(interceptors);
        this.asyncWebRequest.addTimeoutHandler(new Runnable() { // from class: org.springframework.web.context.request.async.WebAsyncManager.2
            @Override // java.lang.Runnable
            public void run() {
                WebAsyncManager.logger.debug("Processing timeout");
                Object result = interceptorChain.triggerAfterTimeout(WebAsyncManager.this.asyncWebRequest, callable);
                if (result != CallableProcessingInterceptor.RESULT_NONE) {
                    WebAsyncManager.this.setConcurrentResultAndDispatch(result);
                }
            }
        });
        if (this.asyncWebRequest instanceof StandardServletAsyncWebRequest) {
            ((StandardServletAsyncWebRequest) this.asyncWebRequest).setErrorHandler(new StandardServletAsyncWebRequest.ErrorHandler() { // from class: org.springframework.web.context.request.async.WebAsyncManager.3
                @Override // org.springframework.web.context.request.async.StandardServletAsyncWebRequest.ErrorHandler
                public void handle(Throwable ex) {
                    WebAsyncManager.this.setConcurrentResultAndDispatch(ex);
                }
            });
        }
        this.asyncWebRequest.addCompletionHandler(new Runnable() { // from class: org.springframework.web.context.request.async.WebAsyncManager.4
            @Override // java.lang.Runnable
            public void run() {
                interceptorChain.triggerAfterCompletion(WebAsyncManager.this.asyncWebRequest, callable);
            }
        });
        interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, callable);
        startAsyncProcessing(processingContext);
        try {
            Future<?> future = this.taskExecutor.submit(new Runnable() { // from class: org.springframework.web.context.request.async.WebAsyncManager.5
                @Override // java.lang.Runnable
                public void run() {
                    Object result;
                    Object result2 = null;
                    try {
                        interceptorChain.applyPreProcess(WebAsyncManager.this.asyncWebRequest, callable);
                        result2 = callable.call();
                        result = interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, callable, result2);
                    } catch (Throwable ex) {
                        result = interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, callable, ex);
                    }
                    WebAsyncManager.this.setConcurrentResultAndDispatch(result);
                }
            });
            interceptorChain.setTaskFuture(future);
        } catch (RejectedExecutionException ex) {
            Object result = interceptorChain.applyPostProcess(this.asyncWebRequest, callable, ex);
            setConcurrentResultAndDispatch(result);
            throw ex;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setConcurrentResultAndDispatch(Object result) {
        synchronized (this) {
            if (this.concurrentResult != RESULT_NONE) {
                return;
            }
            this.concurrentResult = result;
            if (this.asyncWebRequest.isAsyncComplete()) {
                logger.error("Could not complete async processing due to timeout or network error");
                return;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Concurrent result value [" + this.concurrentResult + "] - dispatching request to resume processing");
            }
            this.asyncWebRequest.dispatch();
        }
    }

    public void startDeferredResultProcessing(final DeferredResult<?> deferredResult, Object... processingContext) throws Exception {
        Assert.notNull(deferredResult, "DeferredResult must not be null");
        Assert.state(this.asyncWebRequest != null, "AsyncWebRequest must not be null");
        Long timeout = deferredResult.getTimeoutValue();
        if (timeout != null) {
            this.asyncWebRequest.setTimeout(timeout);
        }
        List<DeferredResultProcessingInterceptor> interceptors = new ArrayList<>();
        interceptors.add(deferredResult.getInterceptor());
        interceptors.addAll(this.deferredResultInterceptors.values());
        interceptors.add(timeoutDeferredResultInterceptor);
        final DeferredResultInterceptorChain interceptorChain = new DeferredResultInterceptorChain(interceptors);
        this.asyncWebRequest.addTimeoutHandler(new Runnable() { // from class: org.springframework.web.context.request.async.WebAsyncManager.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    interceptorChain.triggerAfterTimeout(WebAsyncManager.this.asyncWebRequest, deferredResult);
                } catch (Throwable ex) {
                    WebAsyncManager.this.setConcurrentResultAndDispatch(ex);
                }
            }
        });
        if (this.asyncWebRequest instanceof StandardServletAsyncWebRequest) {
            ((StandardServletAsyncWebRequest) this.asyncWebRequest).setErrorHandler(new StandardServletAsyncWebRequest.ErrorHandler() { // from class: org.springframework.web.context.request.async.WebAsyncManager.7
                @Override // org.springframework.web.context.request.async.StandardServletAsyncWebRequest.ErrorHandler
                public void handle(Throwable ex) {
                    deferredResult.setErrorResult(ex);
                }
            });
        }
        this.asyncWebRequest.addCompletionHandler(new Runnable() { // from class: org.springframework.web.context.request.async.WebAsyncManager.8
            @Override // java.lang.Runnable
            public void run() {
                interceptorChain.triggerAfterCompletion(WebAsyncManager.this.asyncWebRequest, deferredResult);
            }
        });
        interceptorChain.applyBeforeConcurrentHandling(this.asyncWebRequest, deferredResult);
        startAsyncProcessing(processingContext);
        try {
            interceptorChain.applyPreProcess(this.asyncWebRequest, deferredResult);
            deferredResult.setResultHandler(new DeferredResult.DeferredResultHandler() { // from class: org.springframework.web.context.request.async.WebAsyncManager.9
                @Override // org.springframework.web.context.request.async.DeferredResult.DeferredResultHandler
                public void handleResult(Object result) {
                    WebAsyncManager.this.setConcurrentResultAndDispatch(interceptorChain.applyPostProcess(WebAsyncManager.this.asyncWebRequest, deferredResult, result));
                }
            });
        } catch (Throwable ex) {
            setConcurrentResultAndDispatch(ex);
        }
    }

    private void startAsyncProcessing(Object[] processingContext) {
        synchronized (this) {
            this.concurrentResult = RESULT_NONE;
            this.concurrentResultContext = processingContext;
        }
        this.asyncWebRequest.startAsync();
        if (logger.isDebugEnabled()) {
            HttpServletRequest request = (HttpServletRequest) this.asyncWebRequest.getNativeRequest(HttpServletRequest.class);
            String requestUri = urlPathHelper.getRequestUri(request);
            logger.debug("Concurrent handling starting for " + request.getMethod() + " [" + requestUri + "]");
        }
    }
}
