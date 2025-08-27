package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import java.util.List;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/stomp/StompSubframeEncoder.class */
public class StompSubframeEncoder extends MessageToMessageEncoder<StompSubframe> {
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    protected /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, StompSubframe stompSubframe, List list) throws Exception {
        encode2(channelHandlerContext, stompSubframe, (List<Object>) list);
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, StompSubframe msg, List<Object> out) throws Exception {
        if (msg instanceof StompFrame) {
            StompFrame frame = (StompFrame) msg;
            ByteBuf frameBuf = encodeFrame(frame, ctx);
            if (frame.content().isReadable()) {
                out.add(frameBuf);
                ByteBuf contentBuf = encodeContent(frame, ctx);
                out.add(contentBuf);
                return;
            } else {
                frameBuf.writeByte(0);
                out.add(frameBuf);
                return;
            }
        }
        if (msg instanceof StompHeadersSubframe) {
            ByteBuf buf = encodeFrame((StompHeadersSubframe) msg, ctx);
            out.add(buf);
        } else if (msg instanceof StompContentSubframe) {
            StompContentSubframe stompContentSubframe = (StompContentSubframe) msg;
            ByteBuf buf2 = encodeContent(stompContentSubframe, ctx);
            out.add(buf2);
        }
    }

    private static ByteBuf encodeContent(StompContentSubframe content, ChannelHandlerContext ctx) {
        if (content instanceof LastStompContentSubframe) {
            ByteBuf buf = ctx.alloc().buffer(content.content().readableBytes() + 1);
            buf.writeBytes(content.content());
            buf.writeByte(0);
            return buf;
        }
        return content.content().retain();
    }

    private static ByteBuf encodeFrame(StompHeadersSubframe frame, ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeCharSequence(frame.command().toString(), CharsetUtil.UTF_8);
        buf.writeByte(10);
        for (Map.Entry<CharSequence, CharSequence> entry : frame.headers()) {
            ByteBufUtil.writeUtf8(buf, entry.getKey());
            buf.writeByte(58);
            ByteBufUtil.writeUtf8(buf, entry.getValue());
            buf.writeByte(10);
        }
        buf.writeByte(10);
        return buf;
    }
}
