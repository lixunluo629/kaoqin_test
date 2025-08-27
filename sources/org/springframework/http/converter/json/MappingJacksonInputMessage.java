package org.springframework.http.converter.json;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/converter/json/MappingJacksonInputMessage.class */
public class MappingJacksonInputMessage implements HttpInputMessage {
    private final InputStream body;
    private final HttpHeaders headers;
    private Class<?> deserializationView;

    public MappingJacksonInputMessage(InputStream body, HttpHeaders headers) {
        this.body = body;
        this.headers = headers;
    }

    public MappingJacksonInputMessage(InputStream body, HttpHeaders headers, Class<?> deserializationView) {
        this(body, headers);
        this.deserializationView = deserializationView;
    }

    @Override // org.springframework.http.HttpInputMessage
    public InputStream getBody() throws IOException {
        return this.body;
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public void setDeserializationView(Class<?> deserializationView) {
        this.deserializationView = deserializationView;
    }

    public Class<?> getDeserializationView() {
        return this.deserializationView;
    }
}
