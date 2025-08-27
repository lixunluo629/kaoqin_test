package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/StreamingHttpOutputMessage.class */
public interface StreamingHttpOutputMessage extends HttpOutputMessage {

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/StreamingHttpOutputMessage$Body.class */
    public interface Body {
        void writeTo(OutputStream outputStream) throws IOException;
    }

    void setBody(Body body);
}
