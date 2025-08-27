package io.netty.handler.codec;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.TypeParameterMatcher;
import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/MessageToMessageCodec.class */
public abstract class MessageToMessageCodec<INBOUND_IN, OUTBOUND_IN> extends ChannelDuplexHandler {
    private final MessageToMessageEncoder<Object> encoder;
    private final MessageToMessageDecoder<Object> decoder;
    private final TypeParameterMatcher inboundMsgMatcher;
    private final TypeParameterMatcher outboundMsgMatcher;

    protected abstract void encode(ChannelHandlerContext channelHandlerContext, OUTBOUND_IN outbound_in, List<Object> list) throws Exception;

    protected abstract void decode(ChannelHandlerContext channelHandlerContext, INBOUND_IN inbound_in, List<Object> list) throws Exception;

    protected MessageToMessageCodec() {
        this.encoder = new MessageToMessageEncoder<Object>() { // from class: io.netty.handler.codec.MessageToMessageCodec.1
            @Override // io.netty.handler.codec.MessageToMessageEncoder
            public boolean acceptOutboundMessage(Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptOutboundMessage(msg);
            }

            @Override // io.netty.handler.codec.MessageToMessageEncoder
            protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
                MessageToMessageCodec.this.encode(ctx, msg, out);
            }
        };
        this.decoder = new MessageToMessageDecoder<Object>() { // from class: io.netty.handler.codec.MessageToMessageCodec.2
            @Override // io.netty.handler.codec.MessageToMessageDecoder
            public boolean acceptInboundMessage(Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptInboundMessage(msg);
            }

            @Override // io.netty.handler.codec.MessageToMessageDecoder
            protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
                MessageToMessageCodec.this.decode(ctx, msg, out);
            }
        };
        this.inboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "INBOUND_IN");
        this.outboundMsgMatcher = TypeParameterMatcher.find(this, MessageToMessageCodec.class, "OUTBOUND_IN");
    }

    protected MessageToMessageCodec(Class<? extends INBOUND_IN> inboundMessageType, Class<? extends OUTBOUND_IN> outboundMessageType) {
        this.encoder = new MessageToMessageEncoder<Object>() { // from class: io.netty.handler.codec.MessageToMessageCodec.1
            @Override // io.netty.handler.codec.MessageToMessageEncoder
            public boolean acceptOutboundMessage(Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptOutboundMessage(msg);
            }

            @Override // io.netty.handler.codec.MessageToMessageEncoder
            protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
                MessageToMessageCodec.this.encode(ctx, msg, out);
            }
        };
        this.decoder = new MessageToMessageDecoder<Object>() { // from class: io.netty.handler.codec.MessageToMessageCodec.2
            @Override // io.netty.handler.codec.MessageToMessageDecoder
            public boolean acceptInboundMessage(Object msg) throws Exception {
                return MessageToMessageCodec.this.acceptInboundMessage(msg);
            }

            @Override // io.netty.handler.codec.MessageToMessageDecoder
            protected void decode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
                MessageToMessageCodec.this.decode(ctx, msg, out);
            }
        };
        this.inboundMsgMatcher = TypeParameterMatcher.get(inboundMessageType);
        this.outboundMsgMatcher = TypeParameterMatcher.get(outboundMessageType);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.decoder.channelRead(ctx, msg);
    }

    @Override // io.netty.channel.ChannelDuplexHandler, io.netty.channel.ChannelOutboundHandler
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        this.encoder.write(ctx, msg, promise);
    }

    public boolean acceptInboundMessage(Object msg) throws Exception {
        return this.inboundMsgMatcher.match(msg);
    }

    public boolean acceptOutboundMessage(Object msg) throws Exception {
        return this.outboundMsgMatcher.match(msg);
    }
}
