package io.netty.handler.codec.http2;

import io.netty.channel.Channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2StreamChannel.class */
public interface Http2StreamChannel extends Channel {
    Http2FrameStream stream();
}
