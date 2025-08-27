package org.springframework.web.servlet.mvc.method.annotation;

import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import org.springframework.core.MethodParameter;
import org.springframework.lang.UsesJava8;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncUtils;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

@UsesJava8
@Deprecated
/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/CompletionStageReturnValueHandler.class */
public class CompletionStageReturnValueHandler implements AsyncHandlerMethodReturnValueHandler {
    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public boolean supportsReturnType(MethodParameter returnType) {
        return CompletionStage.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override // org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler
    public boolean isAsyncReturnValue(Object returnValue, MethodParameter returnType) {
        return returnValue != null && (returnValue instanceof CompletionStage);
    }

    @Override // org.springframework.web.method.support.HandlerMethodReturnValueHandler
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }
        final DeferredResult<Object> deferredResult = new DeferredResult<>();
        WebAsyncUtils.getAsyncManager(webRequest).startDeferredResultProcessing(deferredResult, mavContainer);
        CompletionStage<Object> future = (CompletionStage) returnValue;
        future.thenAccept(new Consumer<Object>() { // from class: org.springframework.web.servlet.mvc.method.annotation.CompletionStageReturnValueHandler.1
            @Override // java.util.function.Consumer
            public void accept(Object result) {
                deferredResult.setResult(result);
            }
        });
        future.exceptionally(new Function<Throwable, Object>() { // from class: org.springframework.web.servlet.mvc.method.annotation.CompletionStageReturnValueHandler.2
            @Override // java.util.function.Function
            public Object apply(Throwable ex) {
                deferredResult.setErrorResult(ex);
                return null;
            }
        });
    }
}
