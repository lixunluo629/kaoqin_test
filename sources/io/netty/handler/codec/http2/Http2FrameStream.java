package io.netty.handler.codec.http2;

import io.netty.handler.codec.http2.Http2Stream;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2FrameStream.class */
public interface Http2FrameStream {
    int id();

    Http2Stream.State state();
}
