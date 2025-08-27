package io.netty.handler.codec.spdy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdySynReplyFrame.class */
public interface SpdySynReplyFrame extends SpdyHeadersFrame {
    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    SpdySynReplyFrame setStreamId(int i);

    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame, io.netty.handler.codec.spdy.SpdyStreamFrame, io.netty.handler.codec.spdy.SpdyDataFrame
    SpdySynReplyFrame setLast(boolean z);

    @Override // io.netty.handler.codec.spdy.SpdyHeadersFrame
    SpdySynReplyFrame setInvalid();
}
