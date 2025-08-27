package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;
import org.springframework.web.servlet.tags.BindTag;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/DefaultHttpResponse.class */
public class DefaultHttpResponse extends DefaultHttpMessage implements HttpResponse {
    private HttpResponseStatus status;

    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status) {
        this(version, status, true, false);
    }

    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders) {
        this(version, status, validateHeaders, false);
    }

    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders, boolean singleFieldHeaders) {
        super(version, validateHeaders, singleFieldHeaders);
        this.status = (HttpResponseStatus) ObjectUtil.checkNotNull(status, BindTag.STATUS_VARIABLE_NAME);
    }

    public DefaultHttpResponse(HttpVersion version, HttpResponseStatus status, HttpHeaders headers) {
        super(version, headers);
        this.status = (HttpResponseStatus) ObjectUtil.checkNotNull(status, BindTag.STATUS_VARIABLE_NAME);
    }

    @Override // io.netty.handler.codec.http.HttpResponse
    @Deprecated
    public HttpResponseStatus getStatus() {
        return status();
    }

    @Override // io.netty.handler.codec.http.HttpResponse
    public HttpResponseStatus status() {
        return this.status;
    }

    public HttpResponse setStatus(HttpResponseStatus status) {
        this.status = (HttpResponseStatus) ObjectUtil.checkNotNull(status, BindTag.STATUS_VARIABLE_NAME);
        return this;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.HttpMessage, io.netty.handler.codec.http.HttpRequest, io.netty.handler.codec.http.FullHttpRequest
    public HttpResponse setProtocolVersion(HttpVersion version) {
        super.setProtocolVersion(version);
        return this;
    }

    public String toString() {
        return HttpMessageUtil.appendResponse(new StringBuilder(256), this).toString();
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public int hashCode() {
        int result = (31 * 1) + this.status.hashCode();
        return (31 * result) + super.hashCode();
    }

    @Override // io.netty.handler.codec.http.DefaultHttpMessage, io.netty.handler.codec.http.DefaultHttpObject
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttpResponse)) {
            return false;
        }
        DefaultHttpResponse other = (DefaultHttpResponse) o;
        return this.status.equals(other.status()) && super.equals(o);
    }
}
