package org.springframework.web.servlet.mvc.method.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ListenableFutureReturnValueHandler.class */
public class ListenableFutureReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {
    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        return ListenableFuture.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override // org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        return returnValue != null && (returnValue instanceof ListenableFuture);
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }
        final DeferredResult<Object> deferredResult = new DeferredResult<>();
        WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, mavContainer);
        ListenableFuture<?> future = (ListenableFuture) returnValue;
        future.addCallback(new ListenableFutureCallback<Object>() { // from class: org.springframework.web.servlet.mvc.method.annotation.ListenableFutureReturnValueHandler.1
            @Override // org.springframework.util.concurrent.SuccessCallback
            public void onSuccess(Object result) {
                deferredResult.setResult(result);
            }

            @Override // org.springframework.util.concurrent.FailureCallback
            public void onFailure(Throwable ex) {
                deferredResult.setErrorResult(ex);
            }
        });
    }
}
