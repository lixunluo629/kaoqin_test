package io.netty.handler.codec.http2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.channels.ClosedChannelException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/http2/Http2StreamChannelBootstrap.class */
public final class Http2StreamChannelBootstrap {
    private static final InternalLogger logger;
    private static final Map.Entry<ChannelOption<?>, Object>[] EMPTY_OPTION_ARRAY;
    private static final Map.Entry<AttributeKey<?>, Object>[] EMPTY_ATTRIBUTE_ARRAY;
    private final Map<ChannelOption<?>, Object> options = new LinkedHashMap();
    private final Map<AttributeKey<?>, Object> attrs = new ConcurrentHashMap();
    private final Channel channel;
    private volatile ChannelHandler handler;
    private volatile ChannelHandlerContext multiplexCtx;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Http2StreamChannelBootstrap.class.desiredAssertionStatus();
        logger = InternalLoggerFactory.getInstance((Class<?>) Http2StreamChannelBootstrap.class);
        EMPTY_OPTION_ARRAY = new Map.Entry[0];
        EMPTY_ATTRIBUTE_ARRAY = new Map.Entry[0];
    }

    public Http2StreamChannelBootstrap(Channel channel) {
        this.channel = (Channel) ObjectUtil.checkNotNull(channel, "channel");
    }

    public <T> Http2StreamChannelBootstrap option(ChannelOption<T> option, T value) {
        ObjectUtil.checkNotNull(option, "option");
        synchronized (this.options) {
            if (value == null) {
                this.options.remove(option);
            } else {
                this.options.put(option, value);
            }
        }
        return this;
    }

    public <T> Http2StreamChannelBootstrap attr(AttributeKey<T> key, T value) {
        ObjectUtil.checkNotNull(key, "key");
        if (value == null) {
            this.attrs.remove(key);
        } else {
            this.attrs.put(key, value);
        }
        return this;
    }

    public Http2StreamChannelBootstrap handler(ChannelHandler handler) {
        this.handler = (ChannelHandler) ObjectUtil.checkNotNull(handler, "handler");
        return this;
    }

    public Future<Http2StreamChannel> open() {
        return open(this.channel.eventLoop().newPromise());
    }

    public Future<Http2StreamChannel> open(final Promise<Http2StreamChannel> promise) {
        try {
            final ChannelHandlerContext ctx = findCtx();
            EventExecutor executor = ctx.executor();
            if (executor.inEventLoop()) {
                open0(ctx, promise);
            } else {
                executor.execute(new Runnable() { // from class: io.netty.handler.codec.http2.Http2StreamChannelBootstrap.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Http2StreamChannelBootstrap.this.open0(ctx, promise);
                    }
                });
            }
        } catch (Throwable cause) {
            promise.setFailure(cause);
        }
        return promise;
    }

    private ChannelHandlerContext findCtx() throws ClosedChannelException {
        ChannelHandlerContext ctx = this.multiplexCtx;
        if (ctx != null && !ctx.isRemoved()) {
            return ctx;
        }
        ChannelPipeline pipeline = this.channel.pipeline();
        ChannelHandlerContext ctx2 = pipeline.context(Http2MultiplexCodec.class);
        if (ctx2 == null) {
            ctx2 = pipeline.context(Http2MultiplexHandler.class);
        }
        if (ctx2 == null) {
            if (this.channel.isActive()) {
                throw new IllegalStateException(StringUtil.simpleClassName((Class<?>) Http2MultiplexCodec.class) + " or " + StringUtil.simpleClassName((Class<?>) Http2MultiplexHandler.class) + " must be in the ChannelPipeline of Channel " + this.channel);
            }
            throw new ClosedChannelException();
        }
        this.multiplexCtx = ctx2;
        return ctx2;
    }

    @Deprecated
    public void open0(ChannelHandlerContext ctx, final Promise<Http2StreamChannel> promise) {
        Http2StreamChannel streamChannel;
        if (!$assertionsDisabled && !ctx.executor().inEventLoop()) {
            throw new AssertionError();
        }
        if (!promise.setUncancellable()) {
            return;
        }
        if (ctx.handler() instanceof Http2MultiplexCodec) {
            streamChannel = ((Http2MultiplexCodec) ctx.handler()).newOutboundStream();
        } else {
            streamChannel = ((Http2MultiplexHandler) ctx.handler()).newOutboundStream();
        }
        try {
            init(streamChannel);
            ChannelFuture future = ctx.channel().eventLoop().register(streamChannel);
            final Http2StreamChannel http2StreamChannel = streamChannel;
            future.addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.codec.http2.Http2StreamChannelBootstrap.2
                @Override // io.netty.util.concurrent.GenericFutureListener
                public void operationComplete(ChannelFuture future2) {
                    if (future2.isSuccess()) {
                        promise.setSuccess(http2StreamChannel);
                        return;
                    }
                    if (future2.isCancelled()) {
                        promise.cancel(false);
                        return;
                    }
                    if (http2StreamChannel.isRegistered()) {
                        http2StreamChannel.close();
                    } else {
                        http2StreamChannel.unsafe().closeForcibly();
                    }
                    promise.setFailure(future2.cause());
                }
            });
        } catch (Exception e) {
            streamChannel.unsafe().closeForcibly();
            promise.setFailure(e);
        }
    }

    private void init(Channel channel) {
        Map.Entry<ChannelOption<?>, Object>[] optionArray;
        ChannelPipeline p = channel.pipeline();
        ChannelHandler handler = this.handler;
        if (handler != null) {
            p.addLast(handler);
        }
        synchronized (this.options) {
            optionArray = (Map.Entry[]) this.options.entrySet().toArray(EMPTY_OPTION_ARRAY);
        }
        setChannelOptions(channel, optionArray);
        setAttributes(channel, (Map.Entry[]) this.attrs.entrySet().toArray(EMPTY_ATTRIBUTE_ARRAY));
    }

    private static void setChannelOptions(Channel channel, Map.Entry<ChannelOption<?>, Object>[] options) {
        for (Map.Entry<ChannelOption<?>, Object> e : options) {
            setChannelOption(channel, e.getKey(), e.getValue());
        }
    }

    private static void setChannelOption(Channel channel, ChannelOption<?> option, Object value) {
        try {
            if (!channel.config().setOption(option, value)) {
                logger.warn("Unknown channel option '{}' for channel '{}'", option, channel);
            }
        } catch (Throwable t) {
            logger.warn("Failed to set channel option '{}' with value '{}' for channel '{}'", option, value, channel, t);
        }
    }

    private static void setAttributes(Channel channel, Map.Entry<AttributeKey<?>, Object>[] options) {
        for (Map.Entry<AttributeKey<?>, Object> e : options) {
            AttributeKey<Object> key = e.getKey();
            channel.attr(key).set(e.getValue());
        }
    }
}
