package org.springframework.web.servlet.mvc.method.annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import org.springframework.core.MethodParameter;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/DeferredResultMethodReturnValueHandler.class */
public class DeferredResultMethodReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {
    private final Map<Class<?>, DeferredResultAdapter> adapterMap = new HashMap(5);

    public DeferredResultMethodReturnValueHandler() {
        this.adapterMap.put(DeferredResult.class, new SimpleDeferredResultAdapter());
        this.adapterMap.put(ListenableFuture.class, new ListenableFutureAdapter());
        if (ClassUtils.isPresent("java.util.concurrent.CompletionStage", getClass().getClassLoader())) {
            this.adapterMap.put(CompletionStage.class, new CompletionStageAdapter());
        }
    }

    @Deprecated
    public Map<Class<?>, DeferredResultAdapter> getAdapterMap() {
        return this.adapterMap;
    }

    private DeferredResultAdapter getAdapterFor(Class<?> type) {
        for (Class<?> adapteeType : getAdapterMap().keySet()) {
            if (adapteeType.isAssignableFrom(type)) {
                return getAdapterMap().get(adapteeType);
            }
        }
        return null;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        return getAdapterFor(returnType.getParameterType()) != null;
    }

    @Override // org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        return (returnValue == null || getAdapterFor(returnValue.getClass()) == null) ? false : true;
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }
        DeferredResultAdapter adapter = getAdapterFor(returnValue.getClass());
        if (adapter == null) {
            throw new IllegalStateException("Could not find DeferredResultAdapter for return value type: " + returnValue.getClass());
        }
        DeferredResult<?> result = adapter.adaptToDeferredResult(returnValue);
        WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(result, mavContainer);
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/DeferredResultMethodReturnValueHandler$SimpleDeferredResultAdapter.class */
    private static class SimpleDeferredResultAdapter implements DeferredResultAdapter {
        private SimpleDeferredResultAdapter() {
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.DeferredResultAdapter
        public DeferredResult<?> adaptToDeferredResult(Object returnValue) {
            Assert.isInstanceOf(DeferredResult.class, returnValue, "DeferredResult expected");
            return (DeferredResult) returnValue;
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/DeferredResultMethodReturnValueHandler$ListenableFutureAdapter.class */
    private static class ListenableFutureAdapter implements DeferredResultAdapter {
        private ListenableFutureAdapter() {
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.DeferredResultAdapter
        public DeferredResult<?> adaptToDeferredResult(Object returnValue) {
            Assert.isInstanceOf(ListenableFuture.class, returnValue, "ListenableFuture expected");
            final DeferredResult<Object> result = new DeferredResult<>();
            ((ListenableFuture) returnValue).addCallback(new ListenableFutureCallback<Object>() { // from class: org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler.ListenableFutureAdapter.1
                @Override // org.springframework.util.concurrent.SuccessCallback
                public void onSuccess(Object value) {
                    result.setResult(value);
                }

                @Override // org.springframework.util.concurrent.FailureCallback
                public void onFailure(Throwable ex) {
                    result.setErrorResult(ex);
                }
            });
            return result;
        }
    }

    @UsesJava8
    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/DeferredResultMethodReturnValueHandler$CompletionStageAdapter.class */
    private static class CompletionStageAdapter implements DeferredResultAdapter {
        private CompletionStageAdapter() {
        }

        @Override // org.springframework.web.servlet.mvc.method.annotation.DeferredResultAdapter
        public DeferredResult<?> adaptToDeferredResult(Object returnValue) {
            Assert.isInstanceOf(CompletionStage.class, returnValue, "CompletionStage expected");
            final DeferredResult<Object> result = new DeferredResult<>();
            CompletionStage<?> future = (CompletionStage) returnValue;
            future.handle(new BiFunction<Object, Throwable, Object>() { // from class: org.springframework.web.servlet.mvc.method.annotation.DeferredResultMethodReturnValueHandler.CompletionStageAdapter.1
                @Override // java.util.function.BiFunction
                public Object apply(Object value, Throwable ex) {
                    if (ex != null) {
                        result.setErrorResult(ex);
                        return null;
                    }
                    result.setResult(value);
                    return null;
                }
            });
            return result;
        }
    }
}
