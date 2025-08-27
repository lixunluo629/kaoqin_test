package com.moredian.onpremise.iot;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
/* loaded from: onpremise-iot-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/iot/IOTHubServer.class */
public class IOTHubServer implements InitializingBean {

    @Value("${onpremise.iot.port:19001}")
    private int iothubPort = 19001;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) IOTHubServer.class);
    private static boolean SSL = true;

    /* JADX WARN: Type inference failed for: r0v19, types: [io.netty.channel.ChannelFuture] */
    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(this.bossGroup, this.workerGroup).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(10240, 20480, 1024000)).channel(NioServerSocketChannel.class).childHandler(new IOTHubInitializer(sslCtx)).handler(new LoggingHandler(LogLevel.DEBUG));
        b.bind(this.iothubPort).sync2().channel().closeFuture().addListener2((GenericFutureListener<? extends Future<? super Void>>) new GenericFutureListener<Future<? super Void>>() { // from class: com.moredian.onpremise.iot.IOTHubServer.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<? super Void> future) throws Exception {
                IOTHubServer.logger.warn("End to listen on port:" + IOTHubServer.this.iothubPort);
            }
        });
    }

    @PreDestroy
    public void close() {
        if (this.bossGroup != null) {
            this.bossGroup.shutdownGracefully();
        }
        if (this.workerGroup != null) {
            this.workerGroup.shutdownGracefully();
        }
    }
}
