package org.springframework.web.method.annotation;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/MethodArgumentTypeMismatchException.class */
public class MethodArgumentTypeMismatchException extends TypeMismatchException {
    private final String name;
    private final MethodParameter parameter;

    public MethodArgumentTypeMismatchException(Object value, Class<?> requiredType, String name, MethodParameter param, Throwable cause) {
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
