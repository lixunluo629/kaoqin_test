package org.springframework.http.client;

import java.io.IOException;
import org.springframework.http.HttpStatus;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/AbstractClientHttpResponse.class */
public abstract class AbstractClientHttpResponse implements ClientHttpResponse {
    @Override // org.springframework.http.client.ClientHttpResponse
    public HttpStatus getStatusCode() throws IOException {
        return HttpStatus.valueOf(getRawStatusCode());
    }
}
