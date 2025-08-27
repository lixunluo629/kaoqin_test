package io.netty.handler.codec.http;

import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/DefaultHttpMessage.class */
public abstract class DefaultHttpMessage extends DefaultHttpObject implements HttpMessage {
    private static final int HASH_CODE_PRIME = 31;
    private HttpVersion version;
    private final HttpHeaders headers;

    protected DefaultHttpMessage(HttpVersion version) {
        this(version, true, false);
    }

    protected DefaultHttpMessage(HttpVersion version, boolean validateHeaders, boolean singleFieldHeaders) {
        this(version, singleFieldHeaders ? new CombinedHttpHeaders(validateHeaders) : new DefaultHttpHeaders(validateHeaders));
    }

    protected DefaultHttpMessage(HttpVersion version, HttpHeaders headers) {
        this.version = (HttpVersion) ObjectUtil.checkNotNull(version, "version");
        this.headers = (HttpHeaders) ObjectUtil.checkNotNull(headers, "headers");
    }

    @Override // io.netty.handler.codec.http.HttpMessage
    public HttpHeaders headers() {
        return this.headers;
    }

    @Override // io.netty.handler.codec.http.HttpMessage
    @Deprecated
    public HttpVersion getProtocolVersion() {
        return protocolVersion();
    }

    @Override // io.netty.handler.codec.http.HttpMessage
    public HttpVersion protocolVersion() {
        return this.version;
    }

    @Override // io.netty.handler.codec.http.DefaultHttpObject
    public int hashCode() {
        int result = (31 * 1) + this.headers.hashCode();
        return (31 * ((31 * result) + this.version.hashCode())) + super.hashCode();
    }

    @Override // io.netty.handler.codec.http.DefaultHttpObject
    public boolean equals(Object o) {
        if (!(o instanceof DefaultHttpMessage)) {
            return false;
        }
        DefaultHttpMessage other = (DefaultHttpMessage) o;
        return headers().equals(other.headers()) && protocolVersion().equals(other.protocolVersion()) && super.equals(o);
    }

    public HttpMessage setProtocolVersion(HttpVersion version) {
        this.version = (HttpVersion) ObjectUtil.checkNotNull(version, "version");
        return this;
    }
}
