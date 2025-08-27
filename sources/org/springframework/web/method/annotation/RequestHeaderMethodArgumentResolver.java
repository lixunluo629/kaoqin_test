package org.springframework.web.method.annotation;

import java.util.Map;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/RequestHeaderMethodArgumentResolver.class */
public class RequestHeaderMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {
    public RequestHeaderMethodArgumentResolver(ConfigurableBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override // org.springframework.web.method.support.HandlerMethodArgumentResolver
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeader.class) && !Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType());
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        RequestHeader annotation = (RequestHeader) parameter.getParameterAnnotation(RequestHeader.class);
        return new RequestHeaderNamedValueInfo(annotation);
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        String[] headerValues = request.getHeaderValues(name);
        if (headerValues != null) {
            return headerValues.length == 1 ? headerValues[0] : headerValues;
        }
        return null;
    }

    @Override // org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver
    protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
        throw new ServletRequestBindingException("Missing request header '" + name + "' for method parameter of type " + parameter.getNestedParameterType().getSimpleName());
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/RequestHeaderMethodArgumentResolver$RequestHeaderNamedValueInfo.class */
    private static class RequestHeaderNamedValueInfo extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo {
        private RequestHeaderNamedValueInfo(RequestHeader annotation) {
            super(annotation.name(), annotation.required(), annotation.defaultValue());
        }
    }
}
