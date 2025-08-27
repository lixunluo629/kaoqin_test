package org.springframework.http.converter;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/HttpMessageConversionException.class */
public class HttpMessageConversionException extends NestedRuntimeException {
    public HttpMessageConversionException(String msg) {
        super(msg);
    }

    public HttpMessageConversionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
