package org.springframework.http;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/HttpInputMessage.class */
public interface HttpInputMessage extends HttpMessage {
    InputStream getBody() throws IOException;
}
