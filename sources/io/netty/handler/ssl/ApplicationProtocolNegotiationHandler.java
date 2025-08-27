package io.netty.handler.ssl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/ApplicationProtocolNegotiationHandler.class */
public abstract class ApplicationProtocolNegotiationHandler extends ChannelInboundHandlerAdapter {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) ApplicationProtocolNegotiationHandler.class);
    private final String fallbackProtocol;

    protected abstract void configurePipeline(ChannelHandlerContext channelHandlerContext, String str) throws Exception;

    protected ApplicationProtocolNegotiationHandler(String fallbackProtocol) {
        this.fallbackProtocol = (String) ObjectUtil.checkNotNull(fallbackProtocol, "fallbackProtocol");
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        try {
            if (evt instanceof SslHandshakeCompletionEvent) {
                try {
                    SslHandshakeCompletionEvent handshakeEvent = (SslHandshakeCompletionEvent) evt;
                    if (handshakeEvent.isSuccess()) {
                        SslHandler sslHandler = (SslHandler) ctx.pipeline().get(SslHandler.class);
                        if (sslHandler == null) {
                            throw new IllegalStateException("cannot find an SslHandler in the pipeline (required for application-level protocol negotiation)");
                        }
                        String protocol = sslHandler.applicationProtocol();
                        configurePipeline(ctx, protocol != null ? protocol : this.fallbackProtocol);
                    } else {
                        handshakeFailure(ctx, handshakeEvent.cause());
                    }
                    ChannelPipeline pipeline = ctx.pipeline();
                    if (pipeline.context(this) != null) {
                        pipeline.remove(this);
                    }
                } catch (Throwable cause) {
                    exceptionCaught(ctx, cause);
                    ChannelPipeline pipeline2 = ctx.pipeline();
                    if (pipeline2.context(this) != null) {
                        pipeline2.remove(this);
                    }
                }
            }
            ctx.fireUserEventTriggered(evt);
        } catch (Throwable th) {
            ChannelPipeline pipeline3 = ctx.pipeline();
            if (pipeline3.context(this) != null) {
                pipeline3.remove(this);
            }
            throw th;
        }
    }

    protected void handshakeFailure(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("{} TLS handshake failed:", ctx.channel(), cause);
        ctx.close();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("{} Failed to select the application-level protocol:", ctx.channel(), cause);
        ctx.fireExceptionCaught(cause);
        ctx.close();
    }
}
