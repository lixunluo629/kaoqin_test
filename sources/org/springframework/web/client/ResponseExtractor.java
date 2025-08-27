package org.springframework.web.client;

import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/ResponseExtractor.class */
public interface ResponseExtractor<T> {
    T extractData(ClientHttpResponse clientHttpResponse) throws IOException;
}
