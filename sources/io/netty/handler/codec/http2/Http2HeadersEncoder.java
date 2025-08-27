package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2HeadersEncoder.class */
public interface Http2HeadersEncoder {
    public static final SensitivityDetector NEVER_SENSITIVE = new SensitivityDetector() { // from class: io.netty.handler.codec.http2.Http2HeadersEncoder.1
        @Override // io.netty.handler.codec.http2.Http2HeadersEncoder.SensitivityDetector
        public boolean isSensitive(CharSequence name, CharSequence value) {
            return false;
        }
    };
    public static final SensitivityDetector ALWAYS_SENSITIVE = new SensitivityDetector() { // from class: io.netty.handler.codec.http2.Http2HeadersEncoder.2
        @Override // io.netty.handler.codec.http2.Http2HeadersEncoder.SensitivityDetector
        public boolean isSensitive(CharSequence name, CharSequence value) {
            return true;
        }
    };

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2HeadersEncoder$Configuration.class */
    public interface Configuration {
        void maxHeaderTableSize(long j) throws Http2Exception;

        long maxHeaderTableSize();

        void maxHeaderListSize(long j) throws Http2Exception;

        long maxHeaderListSize();
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2HeadersEncoder$SensitivityDetector.class */
    public interface SensitivityDetector {
        boolean isSensitive(CharSequence charSequence, CharSequence charSequence2);
    }

    void encodeHeaders(int i, Http2Headers http2Headers, ByteBuf byteBuf) throws Http2Exception;

    Configuration configuration();
}
