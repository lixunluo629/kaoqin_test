package org.springframework.web.client;

import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/UnknownHttpStatusCodeException.class */
public class UnknownHttpStatusCodeException extends RestClientResponseException {
    private static final long serialVersionUID = 7103980251635005491L;

    public UnknownHttpStatusCodeException(int rawStatusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super("Unknown status code [" + rawStatusCode + "] " + statusText, rawStatusCode, statusText, responseHeaders, responseBody, responseCharset);
    }
}
