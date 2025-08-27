package org.springframework.web.client;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/RestClientResponseException.class */
public class RestClientResponseException extends RestClientException {
    private static final long serialVersionUID = -8803556342728481792L;
    private static final String DEFAULT_CHARSET = "ISO-8859-1";
    private final int rawStatusCode;
    private final String statusText;
    private final byte[] responseBody;
    private final HttpHeaders responseHeaders;
    private final String responseCharset;

    public RestClientResponseException(String message, int statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset) {
        super(message);
        this.rawStatusCode = statusCode;
        this.statusText = statusText;
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody != null ? responseBody : new byte[0];
        this.responseCharset = responseCharset != null ? responseCharset.name() : "ISO-8859-1";
    }

    public int getRawStatusCode() {
        return this.rawStatusCode;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public HttpHeaders getResponseHeaders() {
        return this.responseHeaders;
    }

    public byte[] getResponseBodyAsByteArray() {
        return this.responseBody;
    }

    public String getResponseBodyAsString() {
        try {
            return new String(this.responseBody, this.responseCharset);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
