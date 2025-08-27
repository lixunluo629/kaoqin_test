package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http/websocketx/Utf8FrameValidator.class */
public class Utf8FrameValidator extends ChannelInboundHandlerAdapter {
    private int fragmentedFramesCount;
    private Utf8Validator utf8Validator;

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            try {
                if (((WebSocketFrame) msg).isFinalFragment()) {
                    if (!(frame instanceof PingWebSocketFrame)) {
                        this.fragmentedFramesCount = 0;
                        if ((frame instanceof TextWebSocketFrame) || (this.utf8Validator != null && this.utf8Validator.isChecking())) {
                            checkUTF8String(frame.content());
                            this.utf8Validator.finish();
                        }
                    }
                } else {
                    if (this.fragmentedFramesCount == 0) {
                        if (frame instanceof TextWebSocketFrame) {
                            checkUTF8String(frame.content());
                        }
                    } else if (this.utf8Validator != null && this.utf8Validator.isChecking()) {
                        checkUTF8String(frame.content());
                    }
                    this.fragmentedFramesCount++;
                }
            } catch (CorruptedWebSocketFrameException e) {
                frame.release();
                throw e;
            }
        }
        super.channelRead(ctx, msg);
    }

    private void checkUTF8String(ByteBuf buffer) {
        if (this.utf8Validator == null) {
            this.utf8Validator = new Utf8Validator();
        }
        this.utf8Validator.check(buffer);
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if ((cause instanceof CorruptedFrameException) && ctx.channel().isOpen()) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener2((GenericFutureListener<? extends Future<? super Void>>) ChannelFutureListener.CLOSE);
        }
        super.exceptionCaught(ctx, cause);
    }
}
