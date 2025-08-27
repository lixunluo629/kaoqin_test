package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RequestCallback.class */
public interface RequestCallback {
    void doWithRequest(ClientHttpRequest clientHttpRequest) throws IOException;
}
