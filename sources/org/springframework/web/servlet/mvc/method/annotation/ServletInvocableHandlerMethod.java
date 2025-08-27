package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.View;
import org.springframework.web.util.NestedServletException;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ServletInvocableHandlerMethod.class */
public class ServletInvocableHandlerMethod extends InvocableHandlerMethod {
    private static final Method CALLABLE_METHOD = ClassUtils.getMethod(Callable.class, "call", new Class[0]);
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    public ServletInvocableHandlerMethod(Object handler, Method method) {
        super(handler, method);
    }

    public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers = returnValueHandlers;
    }

    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
        Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
        setResponseStatus(webRequest);
        if (returnValue == null) {
            if (isRequestNotModified(webRequest) || getResponseStatus() != null || mavContainer.isRequestHandled()) {
                mavContainer.setRequestHandled(true);
                return;
            }
        } else if (StringUtils.hasText(getResponseStatusReason())) {
            mavContainer.setRequestHandled(true);
            return;
        }
        mavContainer.setRequestHandled(false);
        try {
            this.returnValueHandlers.handleReturnValue(returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
        } catch (Exception ex) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace(getReturnValueHandlingErrorMessage("Error handling return value", returnValue), ex);
            }
            throw ex;
        }
    }

    private void setResponseStatus(ServletWebRequest webRequest) throws IOException {
        HttpStatus status = getResponseStatus();
        if (status == null) {
            return;
        }
        String reason = getResponseStatusReason();
        if (StringUtils.hasText(reason)) {
            webRequest.getResponse().sendError(status.value(), reason);
        } else {
            webRequest.getResponse().setStatus(status.value());
        }
        webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, status);
    }

    private boolean isRequestNotModified(ServletWebRequest webRequest) {
        return webRequest.isNotModified();
    }

    private String getReturnValueHandlingErrorMessage(String message, Object returnValue) {
        StringBuilder sb = new StringBuilder(message);
        if (returnValue != null) {
            sb.append(" [type=").append(returnValue.getClass().getName()).append("]");
        }
        sb.append(" [value=").append(returnValue).append("]");
        return getDetailedErrorMessage(sb.toString());
    }

    ServletInvocableHandlerMethod wrapConcurrentResult(Object result) {
        return new ConcurrentResultHandlerMethod(result, new ConcurrentResultMethodParameter(result));
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ServletInvocableHandlerMethod$ConcurrentResultHandlerMethod.class */
    private class ConcurrentResultHandlerMethod extends ServletInvocableHandlerMethod {
        private final MethodParameter returnType;

        public ConcurrentResultHandlerMethod(final Object result, ConcurrentResultMethodParameter returnType) {
            super(new Callable<Object>() { // from class: org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.ConcurrentResultHandlerMethod.1
                @Override // java.util.concurrent.Callable
                public Object call() throws Exception {
                    if (result instanceof Exception) {
                        throw ((Exception) result);
                    }
                    if (result instanceof Throwable) {
                        throw new NestedServletException("Async processing failed", (Throwable) result);
                    }
                    return result;
                }
            }, ServletInvocableHandlerMethod.CALLABLE_METHOD);
            setHandlerMethodReturnValueHandlers(ServletInvocableHandlerMethod.this.returnValueHandlers);
            this.returnType = returnType;
        }

        @Override // org.springframework.web.method.HandlerMethod
        public Class<?> getBeanType() {
            return ServletInvocableHandlerMethod.this.getBeanType();
        }

        @Override // org.springframework.web.method.HandlerMethod
        public MethodParameter getReturnValueType(Object returnValue) {
            return this.returnType;
        }

        @Override // org.springframework.web.method.HandlerMethod
        public <A extends Annotation> A getMethodAnnotation(Class<A> cls) {
            return (A) ServletInvocableHandlerMethod.this.getMethodAnnotation(cls);
        }

        @Override // org.springframework.web.method.HandlerMethod
        public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
            return ServletInvocableHandlerMethod.this.hasMethodAnnotation(annotationType);
        }
    }

    /* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/ServletInvocableHandlerMethod$ConcurrentResultMethodParameter.class */
    private class ConcurrentResultMethodParameter extends HandlerMethod.HandlerMethodParameter {
        private final Object returnValue;
        private final ResolvableType returnType;

        public ConcurrentResultMethodParameter(Object returnValue) {
            super(-1);
            this.returnValue = returnValue;
            this.returnType = ResolvableType.forType(super.getGenericParameterType()).getGeneric(0);
        }

        public ConcurrentResultMethodParameter(ConcurrentResultMethodParameter original) {
            super(original);
            this.returnValue = original.returnValue;
            this.returnType = original.returnType;
        }

        @Override // org.springframework.core.MethodParameter
        public Class<?> getParameterType() {
            if (this.returnValue != null) {
                return this.returnValue.getClass();
            }
            if (!ResolvableType.NONE.equals(this.returnType)) {
                return this.returnType.resolve(Object.class);
            }
            return super.getParameterType();
        }

        @Override // org.springframework.core.MethodParameter
        public Type getGenericParameterType() {
            return this.returnType.getType();
        }

        @Override // org.springframework.web.method.HandlerMethod.HandlerMethodParameter, org.springframework.core.annotation.SynthesizingMethodParameter, org.springframework.core.MethodParameter
        /* renamed from: clone */
        public ConcurrentResultMethodParameter mo7600clone() {
            return ServletInvocableHandlerMethod.this.new ConcurrentResultMethodParameter(this);
        }
    }
}
