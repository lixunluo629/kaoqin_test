package org.springframework.http.converter;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/HttpMessageNotReadableException.class */
public class HttpMessageNotReadableException extends HttpMessageConversionException {
    public HttpMessageNotReadableException(String msg) {
        super(msg);
    }

    public HttpMessageNotReadableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
