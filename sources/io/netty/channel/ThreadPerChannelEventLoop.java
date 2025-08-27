package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ThreadPerChannelEventLoop.class */
public class ThreadPerChannelEventLoop extends SingleThreadEventLoop {
    private final ThreadPerChannelEventLoopGroup parent;

    /* renamed from: ch, reason: collision with root package name */
    private Channel f2ch;

    public ThreadPerChannelEventLoop(ThreadPerChannelEventLoopGroup parent) {
        super((EventLoopGroup) parent, parent.executor, true);
        this.parent = parent;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [io.netty.channel.ChannelFuture] */
    @Override // io.netty.channel.SingleThreadEventLoop, io.netty.channel.EventLoopGroup
    public ChannelFuture register(ChannelPromise promise) {
        return super.register(promise).addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.ThreadPerChannelEventLoop.1
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ThreadPerChannelEventLoop.this.f2ch = future.channel();
                } else {
                    ThreadPerChannelEventLoop.this.deregister();
                }
            }
        });
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [io.netty.channel.ChannelFuture] */
    @Override // io.netty.channel.SingleThreadEventLoop, io.netty.channel.EventLoopGroup
    @Deprecated
    public ChannelFuture register(Channel channel, ChannelPromise promise) {
        return super.register(channel, promise).addListener2((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.channel.ThreadPerChannelEventLoop.2
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ThreadPerChannelEventLoop.this.f2ch = future.channel();
                } else {
                    ThreadPerChannelEventLoop.this.deregister();
                }
            }
        });
    }

    @Override // io.netty.util.concurrent.SingleThreadEventExecutor
    protected void run() {
        while (true) {
            Runnable task = takeTask();
            if (task != null) {
                task.run();
                updateLastExecutionTime();
            }
            Channel ch2 = this.f2ch;
            if (isShuttingDown()) {
                if (ch2 != null) {
                    ch2.unsafe().close(ch2.unsafe().voidPromise());
                }
                if (confirmShutdown()) {
                    return;
                }
            } else if (ch2 != null && !ch2.isRegistered()) {
                runAllTasks();
                deregister();
            }
        }
    }

    protected void deregister() {
        this.f2ch = null;
        this.parent.activeChildren.remove(this);
        this.parent.idleChildren.add(this);
    }

    @Override // io.netty.channel.SingleThreadEventLoop
    public int registeredChannels() {
        return 1;
    }
}
