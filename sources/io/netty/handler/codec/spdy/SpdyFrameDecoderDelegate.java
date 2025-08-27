package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/spdy/SpdyFrameDecoderDelegate.class */
public interface SpdyFrameDecoderDelegate {
    void readDataFrame(int i, boolean z, ByteBuf byteBuf);

    void readSynStreamFrame(int i, int i2, byte b, boolean z, boolean z2);

    void readSynReplyFrame(int i, boolean z);

    void readRstStreamFrame(int i, int i2);

    void readSettingsFrame(boolean z);

    void readSetting(int i, int i2, boolean z, boolean z2);

    void readSettingsEnd();

    void readPingFrame(int i);

    void readGoAwayFrame(int i, int i2);

    void readHeadersFrame(int i, boolean z);

    void readWindowUpdateFrame(int i, int i2);

    void readHeaderBlock(ByteBuf byteBuf);

    void readHeaderBlockEnd();

    void readFrameError(String str);
}
