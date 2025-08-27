package org.springframework.web.client;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RestClientException.class */
public class RestClientException extends NestedRuntimeException {
    private static final long serialVersionUID = -4084444984163796577L;

    public RestClientException(String msg) {
        super(msg);
    }

    public RestClientException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
