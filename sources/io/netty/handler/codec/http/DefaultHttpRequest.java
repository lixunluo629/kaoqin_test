package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/DefaultHttpRequest.class */
public class DefaultHttpRequest extends DefaultHttpMessage implements HttpRequest {
    private static final int HASH_CODE_PRIME = 31;
    private HttpMethod method;
    private String uri;

    public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri) {
        this(httpVersion, method, uri, true);
    }

    public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, boolean validateHeaders) {
        super(httpVersion, validateHeaders, false);
        this.method = (HttpMethod) ObjectUtil.checkNotNull(method, JamXmlElements.METHOD);
        this.uri = (String) ObjectUtil.checkNotNull(uri, "uri");
    }

    public DefaultHttpRequest(HttpVersion httpVersion, HttpMethod method, String uri, HttpHeaders headers) {
        super(httpVersion, headers);
        this.method = (HttpMethod) ObjectUtil.checkNotNull(method, JamXmlElements.METHOD);
        this.uri = (String) ObjectUtil.checkNotNull(uri, "uri");
    }

    @Override // io.netty.handler.codec.http.HttpRequest
    @Deprecated
    public HttpMethod getMethod() {
        return method();
    }

    @Override // io.netty.handler.codec.http.HttpRequest
    public HttpMethod method() {
        return this.method;
    }

    @Override // io.netty.handler.codec.http.HttpRequest
    @Deprecated
    public String getUri() {
        return uri();
    }

    @Override // io.netty.handler.codec.http.HttpRequest
    public String uri() {
        return this.uri;
    }

    public HttpRequest setMethod(HttpMethod method) {
        this.method = (HttpMethod) ObjectUtil.checkNotNull(method, JamXmlElements.METHOD);
        return this;
    }

    public HttpRequest setUri(String uri) {
        this.uri = (String) ObjectUtil.checkNotNull(uri, "uri");
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    public HttpRequest setProtocolVersion(HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public int hashCode() {
        int result = (31 * 1) + this.method.hashCode();
        return (31 * ((31 * result) + this.uri.hashCode())) + super.hashCode();
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttpRequest)) {
            return false;
        }
        DefaultHttpRequest other = (DefaultHttpRequest) o;
        return method().equals(other.method()) && uri().equalsIgnoreCase(other.uri()) && super.equals(o);
    }

    public String toString() {
        return HttpMessageUtil.appendRequest(new StringBuilder(256), this).toString();
    }
}
