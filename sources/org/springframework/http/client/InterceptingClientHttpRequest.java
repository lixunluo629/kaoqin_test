package org.springframework.http.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.util.StreamUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/InterceptingClientHttpRequest.class */
class InterceptingClientHttpRequest extends AbstractBufferingClientHttpRequest {
    private final ClientHttpRequestFactory requestFactory;
    private final List<ClientHttpRequestInterceptor> interceptors;
    private HttpMethod method;
    private URI uri;

    protected InterceptingClientHttpRequest(ClientHttpRequestFactory requestFactory, List<ClientHttpRequestInterceptor> interceptors, URI uri, HttpMethod method) {
        this.requestFactory = requestFactory;
        this.interceptors = interceptors;
        this.method = method;
        this.uri = uri;
    }

    @Override // org.springframework.http.HttpRequest
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override // org.springframework.http.HttpRequest
    public URI getURI() {
        return this.uri;
    }

    @Override // org.springframework.http.client.AbstractBufferingClientHttpRequest
    protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput) throws IOException {
        InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
        return requestExecution.execute(this, bufferedOutput);
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/InterceptingClientHttpRequest$InterceptingRequestExecution.class */
    private class InterceptingRequestExecution implements ClientHttpRequestExecution {
        private final Iterator<ClientHttpRequestInterceptor> iterator;

        public InterceptingRequestExecution() {
            this.iterator = InterceptingClientHttpRequest.this.interceptors.iterator();
        }

        @Override // org.springframework.http.client.ClientHttpRequestExecution
        public ClientHttpResponse execute(HttpRequest request, final byte[] body) throws IOException {
            if (!this.iterator.hasNext()) {
                ClientHttpRequest delegate = InterceptingClientHttpRequest.this.requestFactory.createRequest(request.getURI(), request.getMethod());
                for (Map.Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
                    List<String> values = entry.getValue();
                    for (String value : values) {
                        delegate.getHeaders().add(entry.getKey(), value);
                    }
                }
                if (body.length > 0) {
                    if (delegate instanceof StreamingHttpOutputMessage) {
                        StreamingHttpOutputMessage streamingOutputMessage = (StreamingHttpOutputMessage) delegate;
                        streamingOutputMessage.setBody(new StreamingHttpOutputMessage.Body() { // from class: org.springframework.http.client.InterceptingClientHttpRequest.InterceptingRequestExecution.1
                            @Override // org.springframework.http.StreamingHttpOutputMessage.Body
                            public void writeTo(OutputStream outputStream) throws IOException {
                                StreamUtils.copy(body, outputStream);
                            }
                        });
                    } else {
                        StreamUtils.copy(body, delegate.getBody());
                    }
                }
                return delegate.execute();
            }
            ClientHttpRequestInterceptor nextInterceptor = this.iterator.next();
            return nextInterceptor.intercept(request, body, this);
        }
    }
}
