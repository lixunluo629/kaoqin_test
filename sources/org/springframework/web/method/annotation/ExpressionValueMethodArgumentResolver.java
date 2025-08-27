package org.springframework.web.method.annotation;

import javax.servlet.ServletException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/ExpressionValueMethodArgumentResolver.class */
public class ExpressionValueMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {
    public ExpressionValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Value.class);
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        Value annotation = (Value) parameter.getParameterAnnotation(Value.class);
        return new ExpressionValueNamedValueInfo(annotation);
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
        return null;
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected void handleMissingValue(String name, MethodParameter parameter) throws ServletException {
        throw new UnsupportedOperationException("@Value is never required: " + parameter.getMethod());
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/ExpressionValueMethodArgumentResolver$ExpressionValueNamedValueInfo.class */
    private static class ExpressionValueNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo {
        private ExpressionValueNamedValueInfo(Value annotation) {
            super("@Value", false, annotation.value());
        }
    }
}
