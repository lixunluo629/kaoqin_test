package org.springframework.http.client;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/http/client/Netty4ClientHttpResponse.class */
class Netty4ClientHttpResponse extends AbstractClientHttpResponse {
    private final ChannelHandlerContext context;
    private final FullHttpResponse nettyResponse;
    private final ByteBufInputStream body;
    private volatile HttpHeaders headers;

    public Netty4ClientHttpResponse(ChannelHandlerContext context, FullHttpResponse nettyResponse) {
        Assert.notNull(context, "ChannelHandlerContext must not be null");
        Assert.notNull(nettyResponse, "FullHttpResponse must not be null");
        this.context = context;
        this.nettyResponse = nettyResponse;
        this.body = new ByteBufInputStream(this.nettyResponse.content());
        this.nettyResponse.retain();
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public int getRawStatusCode() throws IOException {
        return this.nettyResponse.getStatus().code();
    }

    @Override // org.springframework.http.client.ClientHttpResponse
    public String getStatusText() throws IOException {
        return this.nettyResponse.getStatus().reasonPhrase();
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        if (this.headers == null) {
            HttpHeaders headers = new HttpHeaders();
            Iterator<Map.Entry<String, String>> it = this.nettyResponse.headers().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                headers.add(entry.getKey(), entry.getValue());
            }
            this.headers = headers;
        }
        return this.headers;
    }

    @Override // org.springframework.http.HttpInputMessage
    public InputStream getBody() throws IOException {
        return this.body;
    }

    @Override // org.springframework.http.client.ClientHttpResponse, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.nettyResponse.release();
        this.context.close();
    }
}
