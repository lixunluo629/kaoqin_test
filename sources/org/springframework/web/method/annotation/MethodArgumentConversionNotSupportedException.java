package org.springframework.web.method.annotation;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.core.MethodParameter;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/MethodArgumentConversionNotSupportedException.class */
public class MethodArgumentConversionNotSupportedException extends ConversionNotSupportedException {
    private final String name;
    private final MethodParameter parameter;

    public MethodArgumentConversionNotSupportedException(Object value, Class<?> requiredType, String name, MethodParameter param, Throwable cause) {
        super(value, requiredType, cause);
        this.name = name;
        this.parameter = param;
    }

    public String getName() {
        return this.name;
    }

    public MethodParameter getParameter() {
        return this.parameter;
    }
}
