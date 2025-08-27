package org.springframework.http.converter;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/HttpMessageNotWritableException.class */
public class HttpMessageNotWritableException extends HttpMessageConversionException {
    public HttpMessageNotWritableException(String msg) {
        super(msg);
    }

    public HttpMessageNotWritableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
