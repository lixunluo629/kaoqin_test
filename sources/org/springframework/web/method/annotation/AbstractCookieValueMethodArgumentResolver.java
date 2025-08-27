package org.springframework.web.method.annotation;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/AbstractCookieValueMethodArgumentResolver.class */
public abstract class AbstractCookieValueMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {
    public AbstractCookieValueMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CookieValue.class);
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        CookieValue annotation = (CookieValue) parameter.getParameterAnnotation(CookieValue.class);
        return new CookieValueNamedValueInfo(annotation);
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
        throw new ServletRequestBindingException("Missing cookie '" + name + "' for method parameter of type " + parameter.getNestedParameterType().getSimpleName());
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/AbstractCookieValueMethodArgumentResolver$CookieValueNamedValueInfo.class */
    private static class CookieValueNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo {
        private CookieValueNamedValueInfo(CookieValue annotation) {
            super(annotation.name(), annotation.required(), annotation.defaultValue());
        }
    }
}
