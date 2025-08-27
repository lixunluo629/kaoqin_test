package org.springframework.web;

import java.util.List;
import org.springframework.http.MediaType;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/HttpMediaTypeNotSupportedException.class */
public class HttpMediaTypeNotSupportedException extends HttpMediaTypeException {
    private final MediaType contentType;

    public HttpMediaTypeNotSupportedException(String message) {
        super(message);
        this.contentType = null;
    }

    public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes) {
        this(contentType, supportedMediaTypes, "Content type '" + contentType + "' not supported");
    }

    public HttpMediaTypeNotSupportedException(MediaType contentType, List<MediaType> supportedMediaTypes, String msg) {
        super(msg, supportedMediaTypes);
        this.contentType = contentType;
    }

    public MediaType getContentType() {
        return this.contentType;
    }
}
