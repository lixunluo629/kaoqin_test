package org.springframework.web;

import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import org.springframework.http.MediaType;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/HttpMediaTypeException.class */
public abstract class HttpMediaTypeException extends ServletException {
    private final List<MediaType> supportedMediaTypes;

    protected HttpMediaTypeException(String message) {
        super(message);
        this.supportedMediaTypes = Collections.emptyList();
    }

    protected HttpMediaTypeException(String message, List<MediaType> supportedMediaTypes) {
        super(message);
        this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);
    }

    public List<MediaType> getSupportedMediaTypes() {
        return this.supportedMediaTypes;
    }
}
