package com.moredian.onpremise.iot;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/IOTHubInitializer.class */
public class IOTHubInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public IOTHubInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.ChannelInitializer
    public void initChannel(SocketChannel ch2) throws Exception {
        ChannelPipeline pipeline = ch2.pipeline();
        if (this.sslCtx != null) {
            pipeline.addFirst("ssl", new SslHandler(this.sslCtx.newEngine(ch2.alloc())));
        }
        pipeline.addLast(new IOTEventDecoder());
        pipeline.addLast(new IOTEventEncoder());
        pipeline.addLast(IOTHubHandler.getInstance());
    }

    @Override // io.netty.channel.ChannelInitializer, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
