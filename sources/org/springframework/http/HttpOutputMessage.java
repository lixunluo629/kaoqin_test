package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/HttpOutputMessage.class */
public interface HttpOutputMessage extends HttpMessage {
    OutputStream getBody() throws IOException;
}
