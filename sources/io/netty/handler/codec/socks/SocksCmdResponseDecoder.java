package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.NetUtil;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socks/SocksCmdResponseDecoder.class */
public class SocksCmdResponseDecoder extends ReplayingDecoder<State> {
    private SocksCmdStatus cmdStatus;
    private SocksAddressType addressType;

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/socks/SocksCmdResponseDecoder$State.class */
    enum State {
        CHECK_PROTOCOL_VERSION,
        READ_CMD_HEADER,
        READ_CMD_ADDRESS
    }

    public SocksCmdResponseDecoder() {
        super(State.CHECK_PROTOCOL_VERSION);
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        switch (state()) {
            case CHECK_PROTOCOL_VERSION:
                if (byteBuf.readByte() != SocksProtocolVersion.SOCKS5.byteValue()) {
                    out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
                    ctx.pipeline().remove(this);
                    return;
                }
                checkpoint(State.READ_CMD_HEADER);
            case READ_CMD_HEADER:
                this.cmdStatus = SocksCmdStatus.valueOf(byteBuf.readByte());
                byteBuf.skipBytes(1);
                this.addressType = SocksAddressType.valueOf(byteBuf.readByte());
                checkpoint(State.READ_CMD_ADDRESS);
            case READ_CMD_ADDRESS:
                switch (this.addressType) {
                    case IPv4:
                        String host = NetUtil.intToIpAddress(byteBuf.readInt());
                        int port = byteBuf.readUnsignedShort();
                        out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host, port));
                        break;
                    case DOMAIN:
                        int fieldLength = byteBuf.readByte();
                        String host2 = SocksCommonUtils.readUsAscii(byteBuf, fieldLength);
                        int port2 = byteBuf.readUnsignedShort();
                        out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host2, port2));
                        break;
                    case IPv6:
                        byte[] bytes = new byte[16];
                        byteBuf.readBytes(bytes);
                        String host3 = SocksCommonUtils.ipv6toStr(bytes);
                        int port3 = byteBuf.readUnsignedShort();
                        out.add(new SocksCmdResponse(this.cmdStatus, this.addressType, host3, port3));
                        break;
                    case UNKNOWN:
                        out.add(SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE);
                        break;
                    default:
                        throw new Error();
                }
                ctx.pipeline().remove(this);
                return;
            default:
                throw new Error();
        }
    }
}
