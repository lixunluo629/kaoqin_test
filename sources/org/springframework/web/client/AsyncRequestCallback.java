package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.AsyncClientHttpRequest;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/AsyncRequestCallback.class */
public interface AsyncRequestCallback {
    void doWithRequest(AsyncClientHttpRequest asyncClientHttpRequest) throws IOException;
}
