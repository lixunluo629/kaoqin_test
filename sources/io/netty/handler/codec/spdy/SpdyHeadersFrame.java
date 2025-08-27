package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyHeadersFrame.class */
public interface SpdyHeadersFrame extends SpdyStreamFrame {
    boolean isInvalid();

    SpdyHeadersFrame setInvalid();

    boolean isTruncated();

    SpdyHeadersFrame setTruncated();

    SpdyHeaders headers();

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    SpdyHeadersFrame setStreamId(int i);

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    SpdyHeadersFrame setLast(boolean z);
}
