package org.springframework.web;

import java.util.List;
import org.springframework.http.MediaType;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/HttpMediaTypeNotAcceptableException.class */
public class HttpMediaTypeNotAcceptableException extends HttpMediaTypeException {
    public HttpMediaTypeNotAcceptableException(String message) {
        super(message);
    }

    public HttpMediaTypeNotAcceptableException(List<MediaType> supportedMediaTypes) {
        super("Could not find acceptable representation", supportedMediaTypes);
    }
}
