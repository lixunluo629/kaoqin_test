package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socks/SocksAuthRequestDecoder.class */
public class SocksAuthRequestDecoder extends ReplayingDecoder<State> {
    private String username;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socks/SocksAuthRequestDecoder$State.class */
    enum State {
        CHECK_PROTOCOL_VERSION,
        READ_USERNAME,
        READ_PASSWORD
    }

    public SocksAuthRequestDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        switch (state()) {
            case CHECK_PROTOCOL_VERSION:
                if (byteBuf.readByte() != SocksSubnegotiationVersion.AUTH_PASSWORD.byteValue()) {
                    out.add(SocksCommonUtils.UNKNOWN_SOCKS_REQUEST);
                    ctx.pipeline().remove(this);
                    return;
                }
                checkpoint(State.READ_USERNAME);
            case READ_USERNAME:
                int fieldLength = byteBuf.readByte();
                this.username = SocksCommonUtils.readUsAscii(byteBuf, fieldLength);
                checkpoint(State.READ_PASSWORD);
            case READ_PASSWORD:
                int fieldLength2 = byteBuf.readByte();
                String password = SocksCommonUtils.readUsAscii(byteBuf, fieldLength2);
                out.add(new SocksAuthRequest(this.username, password));
                ctx.pipeline().remove(this);
                return;
            default:
                throw new Error();
        }
    }
}
