package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import java.net.SocketAddress;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/dns/TcpDnsResponseDecoder.class */
public final class TcpDnsResponseDecoder extends LengthFieldBasedFrameDecoder {
    private final DnsResponseDecoder<SocketAddress> responseDecoder;

    public TcpDnsResponseDecoder() {
        this(DnsRecordDecoder.DEFAULT, 65536);
    }

    public TcpDnsResponseDecoder(DnsRecordDecoder recordDecoder, int maxFrameLength) {
        super(maxFrameLength, 0, 2, 0, 2);
        this.responseDecoder = new DnsResponseDecoder<SocketAddress>(recordDecoder) { // from class: io.netty.handler.codec.dns.TcpDnsResponseDecoder.1
            @Override // io.netty.handler.codec.dns.DnsResponseDecoder
            protected DnsResponse newResponse(SocketAddress sender, SocketAddress recipient, int id, DnsOpCode opCode, DnsResponseCode responseCode) {
                return new DefaultDnsResponse(id, opCode, responseCode);
            }
        };
    }

    @Override // io.netty.handler.codec.LengthFieldBasedFrameDecoder
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        try {
            DnsResponse dnsResponseDecode = this.responseDecoder.decode(ctx.channel().remoteAddress(), ctx.channel().localAddress(), frame.slice());
            frame.release();
            return dnsResponseDecode;
        } catch (Throwable th) {
            frame.release();
            throw th;
        }
    }

    @Override // io.netty.handler.codec.LengthFieldBasedFrameDecoder
    protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
        return buffer.copy(index, length);
    }
}
