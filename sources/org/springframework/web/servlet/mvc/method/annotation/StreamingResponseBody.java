package org.springframework.web.servlet.mvc.method.annotation;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-webmvc-4.3.25.RELEASE.jar:org/springframework/web/servlet/mvc/method/annotation/StreamingResponseBody.class */
public interface StreamingResponseBody {
    void writeTo(OutputStream outputStream) throws IOException;
}
