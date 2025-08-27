package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/ResponseErrorHandler.class */
public interface ResponseErrorHandler {
    boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException;

    void handleError(ClientHttpResponse clientHttpResponse) throws IOException;
}
