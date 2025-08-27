package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.AbstractInboundHttp2ToHttpAdapterBuilder;
import io.netty.handler.codec.http2.InboundHttp2ToHttpAdapter;
import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/AbstractInboundHttp2ToHttpAdapterBuilder.class */
public abstract class AbstractInboundHttp2ToHttpAdapterBuilder<T extends InboundHttp2ToHttpAdapter, B extends AbstractInboundHttp2ToHttpAdapterBuilder<T, B>> {
    private final Http2Connection connection;
    private int maxContentLength;
    private boolean validateHttpHeaders;
    private boolean propagateSettings;

    protected abstract T build(Http2Connection http2Connection, int i, boolean z, boolean z2) throws Exception;

    protected AbstractInboundHttp2ToHttpAdapterBuilder(Http2Connection connection) {
        this.connection = (Http2Connection) ObjectUtil.checkNotNull(connection, "connection");
    }

    protected final B self() {
        return this;
    }

    protected Http2Connection connection() {
        return this.connection;
    }

    protected int maxContentLength() {
        return this.maxContentLength;
    }

    protected B maxContentLength(int i) {
        this.maxContentLength = i;
        return (B) self();
    }

    protected boolean isValidateHttpHeaders() {
        return this.validateHttpHeaders;
    }

    protected B validateHttpHeaders(boolean z) {
        this.validateHttpHeaders = z;
        return (B) self();
    }

    protected boolean isPropagateSettings() {
        return this.propagateSettings;
    }

    protected B propagateSettings(boolean z) {
        this.propagateSettings = z;
        return (B) self();
    }

    protected T build() {
        try {
            T t = (T) build(connection(), maxContentLength(), isValidateHttpHeaders(), isPropagateSettings());
            this.connection.addListener(t);
            return t;
        } catch (Throwable th) {
            throw new IllegalStateException("failed to create a new InboundHttp2ToHttpAdapter", th);
        }
    }
}
