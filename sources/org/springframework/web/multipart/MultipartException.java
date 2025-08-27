package org.springframework.web.multipart;

import org.springframework.core.NestedRuntimeException;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/multipart/MultipartException.class */
public class MultipartException extends NestedRuntimeException {
    public MultipartException(String msg) {
        super(msg);
    }

    public MultipartException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
