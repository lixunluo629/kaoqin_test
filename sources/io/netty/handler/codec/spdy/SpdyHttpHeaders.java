package io.netty.handler.codec.spdy;

import io.netty.util.AsciiString;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHttpHeaders.class */
public final class SpdyHttpHeaders {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHttpHeaders$Names.class */
    public static final class Names {
        public static final AsciiString STREAM_ID = AsciiString.cached("x-spdy-stream-id");
        public static final AsciiString ASSOCIATED_TO_STREAM_ID = AsciiString.cached("x-spdy-associated-to-stream-id");
        public static final AsciiString PRIORITY = AsciiString.cached("x-spdy-priority");
        public static final AsciiString SCHEME = AsciiString.cached("x-spdy-scheme");

        private Names() {
        }
    }

    private SpdyHttpHeaders() {
    }
}
