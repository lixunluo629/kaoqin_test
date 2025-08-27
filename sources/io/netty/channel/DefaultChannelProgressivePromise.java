package io.netty.channel;

import io.netty.channel.ChannelFlushPromiseNotifier;
import io.netty.util.concurrent.DefaultProgressivePromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/DefaultChannelProgressivePromise.class */
public class DefaultChannelProgressivePromise extends DefaultProgressivePromise<Void> implements ChannelProgressivePromise, ChannelFlushPromiseNotifier.FlushCheckpoint {
    private final Channel channel;
    private long checkpoint;

    public DefaultChannelProgressivePromise(Channel channel) {
        this.channel = channel;
    }

    public DefaultChannelProgressivePromise(Channel channel, EventExecutor executor) {
        super(executor);
        this.channel = channel;
    }

    @Override // io.netty.util.concurrent.DefaultPromise
    protected EventExecutor executor() {
        EventExecutor e = super.executor();
        if (e == null) {
            return channel().eventLoop();
        }
        return e;
    }

    @Override // io.netty.channel.ChannelFuture
    public Channel channel() {
        return this.channel;
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelProgressivePromise setSuccess() {
        return setSuccess((Void) null);
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelProgressivePromise setSuccess(Void result) {
        super.setSuccess((DefaultChannelProgressivePromise) result);
        return this;
    }

    @Override // io.netty.channel.ChannelPromise
    public boolean trySuccess() {
        return trySuccess(null);
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public ChannelProgressivePromise setFailure(Throwable cause) {
        super.setFailure(cause);
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.ProgressivePromise
    public ChannelProgressivePromise setProgress(long progress, long total) {
        super.setProgress(progress, total);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: addListener */
    public Future<Void> addListener2(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener2((GenericFutureListener) listener);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: addListeners */
    public Future<Void> addListeners2(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners2((GenericFutureListener[]) listeners);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: removeListener */
    public Future<Void> removeListener2(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener2((GenericFutureListener) listener);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: removeListeners */
    public Future<Void> removeListeners2(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners2((GenericFutureListener[]) listeners);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: sync */
    public Future<Void> sync2() throws InterruptedException {
        super.sync2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: syncUninterruptibly */
    public Future<Void> syncUninterruptibly2() {
        super.syncUninterruptibly2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: await */
    public Future<Void> await2() throws InterruptedException {
        super.await2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultProgressivePromise, io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: awaitUninterruptibly */
    public Future<Void> awaitUninterruptibly2() {
        super.awaitUninterruptibly2();
        return this;
    }

    @Override // io.netty.channel.ChannelFlushPromiseNotifier.FlushCheckpoint
    public long flushCheckpoint() {
        return this.checkpoint;
    }

    @Override // io.netty.channel.ChannelFlushPromiseNotifier.FlushCheckpoint
    public void flushCheckpoint(long checkpoint) {
        this.checkpoint = checkpoint;
    }

    @Override // io.netty.channel.ChannelFlushPromiseNotifier.FlushCheckpoint
    public ChannelProgressivePromise promise() {
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise
    protected void checkDeadLock() {
        if (channel().isRegistered()) {
            super.checkDeadLock();
        }
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelProgressivePromise unvoid() {
        return this;
    }

    @Override // io.netty.channel.ChannelFuture
    public boolean isVoid() {
        return false;
    }
}
