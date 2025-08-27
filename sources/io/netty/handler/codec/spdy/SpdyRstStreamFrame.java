package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyRstStreamFrame.class */
public interface SpdyRstStreamFrame extends SpdyStreamFrame {
    SpdyStreamStatus status();

    SpdyRstStreamFrame setStatus(SpdyStreamStatus spdyStreamStatus);

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    SpdyRstStreamFrame setStreamId(int i);

    @Override // io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    SpdyRstStreamFrame setLast(boolean z);
}
