package org.springframework.http.client;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/OkHttpAsyncClientHttpRequest.class */
class OkHttpAsyncClientHttpRequest extends AbstractBufferingAsyncClientHttpRequest {
    private final OkHttpClient client;
    private final URI uri;
    private final HttpMethod method;

    public OkHttpAsyncClientHttpRequest(OkHttpClient client, URI uri, HttpMethod method) {
        this.client = client;
        this.uri = uri;
        this.method = method;
    }

    @Override // org.springframework.http.HttpRequest
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override // org.springframework.http.HttpRequest
    public URI getURI() {
        return this.uri;
    }

    @Override // org.springframework.http.client.AbstractBufferingAsyncClientHttpRequest
    protected ListenableFuture<ClientHttpResponse> executeInternal(HttpHeaders headers, byte[] content) throws IOException {
        Request request = OkHttpClientHttpRequestFactory.buildRequest(headers, content, this.uri, this.method);
        return new OkHttpListenableFuture(this.client.newCall(request));
    }

    /* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/OkHttpAsyncClientHttpRequest$OkHttpListenableFuture.class */
    private static class OkHttpListenableFuture extends SettableListenableFuture<ClientHttpResponse> {
        private final Call call;

        public OkHttpListenableFuture(Call call) {
            this.call = call;
            this.call.enqueue(new Callback() { // from class: org.springframework.http.client.OkHttpAsyncClientHttpRequest.OkHttpListenableFuture.1
                public void onResponse(Response response) {
                    OkHttpListenableFuture.this.set(new OkHttpClientHttpResponse(response));
                }

                public void onFailure(Request request, IOException ex) {
                    OkHttpListenableFuture.this.setException(ex);
                }
            });
        }

        @Override // org.springframework.util.concurrent.SettableListenableFuture
        protected void interruptTask() {
            this.call.cancel();
        }
    }
}
