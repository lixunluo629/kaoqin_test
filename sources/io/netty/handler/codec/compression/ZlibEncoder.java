package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/compression/ZlibEncoder.class */
public abstract class ZlibEncoder extends MessageToByteEncoder<ByteBuf> {
    public abstract boolean isClosed();

    public abstract ChannelFuture close();

    public abstract ChannelFuture close(ChannelPromise channelPromise);

    protected ZlibEncoder() {
        super(false);
    }
}
