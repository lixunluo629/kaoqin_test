package org.springframework.web.context.request.async;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/DeferredResult.class */
public class DeferredResult<T> {
    private static final Object RESULT_NONE = new Object();
    private static final Log logger = LogFactory.getLog(DeferredResult.class);
    private final Long timeoutValue;
    private final Object timeoutResult;
    private Runnable timeoutCallback;
    private Runnable completionCallback;
    private DeferredResultHandler resultHandler;
    private volatile Object result;
    private volatile boolean expired;

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/context/request/async/DeferredResult$DeferredResultHandler.class */
    public interface DeferredResultHandler {
        void handleResult(Object obj);
    }

    public DeferredResult() {
        this(null, RESULT_NONE);
    }

    public DeferredResult(Long timeoutValue) {
        this(timeoutValue, RESULT_NONE);
    }

    public DeferredResult(Long timeoutValue, Object timeoutResult) {
        this.result = RESULT_NONE;
        this.expired = false;
        this.timeoutValue = timeoutValue;
        this.timeoutResult = timeoutResult;
    }

    public final boolean isSetOrExpired() {
        return this.result != RESULT_NONE || this.expired;
    }

    public boolean hasResult() {
        return this.result != RESULT_NONE;
    }

    public Object getResult() {
        Object resultToCheck = this.result;
        if (resultToCheck != RESULT_NONE) {
            return resultToCheck;
        }
        return null;
    }

    final Long getTimeoutValue() {
        return this.timeoutValue;
    }

    public void onTimeout(Runnable callback) {
        this.timeoutCallback = callback;
    }

    public void onCompletion(Runnable callback) {
        this.completionCallback = callback;
    }

    public final void setResultHandler(DeferredResultHandler resultHandler) {
        Assert.notNull(resultHandler, "DeferredResultHandler is required");
        if (this.expired) {
            return;
        }
        synchronized (this) {
            if (this.expired) {
                return;
            }
            Object resultToHandle = this.result;
            if (resultToHandle == RESULT_NONE) {
                this.resultHandler = resultHandler;
                return;
            }
            try {
                resultHandler.handleResult(resultToHandle);
            } catch (Throwable ex) {
                logger.debug("Failed to handle existing result", ex);
            }
        }
    }

    public boolean setResult(T result) {
        return setResultInternal(result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setResultInternal(Object result) {
        if (isSetOrExpired()) {
            return false;
        }
        synchronized (this) {
            if (isSetOrExpired()) {
                return false;
            }
            this.result = result;
            DeferredResultHandler resultHandlerToUse = this.resultHandler;
            if (resultHandlerToUse == null) {
                return true;
            }
            this.resultHandler = null;
            resultHandlerToUse.handleResult(result);
            return true;
        }
    }

    public boolean setErrorResult(Object result) {
        return setResultInternal(result);
    }

    final DeferredResultProcessingInterceptor getInterceptor() {
        return new DeferredResultProcessingInterceptorAdapter() { // from class: org.springframework.web.context.request.async.DeferredResult.1
            /* JADX WARN: Finally extract failed */
            @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter, org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
            public <S> boolean handleTimeout(NativeWebRequest request, DeferredResult<S> deferredResult) {
                boolean continueProcessing = true;
                try {
                    if (DeferredResult.this.timeoutCallback != null) {
                        DeferredResult.this.timeoutCallback.run();
                    }
                    if (DeferredResult.this.timeoutResult != DeferredResult.RESULT_NONE) {
                        continueProcessing = false;
                        try {
                            DeferredResult.this.setResultInternal(DeferredResult.this.timeoutResult);
                        } catch (Throwable ex) {
                            DeferredResult.logger.debug("Failed to handle timeout result", ex);
                        }
                    }
                    return continueProcessing;
                } catch (Throwable th) {
                    if (DeferredResult.this.timeoutResult != DeferredResult.RESULT_NONE) {
                        try {
                            DeferredResult.this.setResultInternal(DeferredResult.this.timeoutResult);
                        } catch (Throwable ex2) {
                            DeferredResult.logger.debug("Failed to handle timeout result", ex2);
                        }
                    }
                    throw th;
                }
            }

            @Override // org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter, org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
            public <S> void afterCompletion(NativeWebRequest request, DeferredResult<S> deferredResult) {
                DeferredResult.this.expired = true;
                if (DeferredResult.this.completionCallback != null) {
                    DeferredResult.this.completionCallback.run();
                }
            }
        };
    }
}
