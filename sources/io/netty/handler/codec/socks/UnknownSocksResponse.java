package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socks/UnknownSocksResponse.class */
public final class UnknownSocksResponse extends SocksResponse {
    public UnknownSocksResponse() {
        super(SocksResponseType.UNKNOWN);
    }

    @Override // io.netty.handler.codec.socks.SocksMessage
    public void encodeAsByteBuf(ByteBuf byteBuf) {
    }
}
