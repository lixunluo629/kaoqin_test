package io.netty.channel;

import io.netty.channel.ChannelFlushPromiseNotifier;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/DefaultChannelPromise.class */
public class DefaultChannelPromise extends DefaultPromise<Void> implements ChannelPromise, ChannelFlushPromiseNotifier.FlushCheckpoint {
    private final Channel channel;
    private long checkpoint;

    public DefaultChannelPromise(Channel channel) {
        this.channel = (Channel) ObjectUtil.checkNotNull(channel, "channel");
    }

    public DefaultChannelPromise(Channel channel, EventExecutor executor) {
        super(executor);
        this.channel = (Channel) ObjectUtil.checkNotNull(channel, "channel");
    }

    @Override // io.netty.util.concurrent.DefaultPromise
    protected EventExecutor executor() {
        EventExecutor e = super.executor();
        if (e == null) {
            return channel().eventLoop();
        }
        return e;
    }

    @Override // io.netty.channel.ChannelPromise, io.netty.channel.ChannelFuture
    public Channel channel() {
        return this.channel;
    }

    public ChannelPromise setSuccess() {
        return setSuccess((Void) null);
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public ChannelPromise setSuccess(Void result) {
        super.setSuccess((DefaultChannelPromise) result);
        return this;
    }

    public boolean trySuccess() {
        return trySuccess(null);
    }

    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public ChannelPromise setFailure(Throwable cause) {
        super.setFailure(cause);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: addListener, reason: merged with bridge method [inline-methods] */
    public Future<Void> addListener2(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener2((GenericFutureListener) listener);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: addListeners, reason: merged with bridge method [inline-methods] */
    public Future<Void> addListeners2(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.addListeners2((GenericFutureListener[]) listeners);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: removeListener, reason: merged with bridge method [inline-methods] */
    public Future<Void> removeListener2(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener2((GenericFutureListener) listener);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: removeListeners, reason: merged with bridge method [inline-methods] */
    public Future<Void> removeListeners2(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        super.removeListeners2((GenericFutureListener[]) listeners);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: sync, reason: merged with bridge method [inline-methods] */
    public Future<Void> sync2() throws InterruptedException {
        super.sync2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: syncUninterruptibly, reason: merged with bridge method [inline-methods] */
    public Future<Void> syncUninterruptibly2() {
        super.syncUninterruptibly2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: await, reason: merged with bridge method [inline-methods] */
    public Future<Void> await2() throws InterruptedException {
        super.await2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.DefaultPromise, io.netty.util.concurrent.Future
    /* renamed from: awaitUninterruptibly, reason: merged with bridge method [inline-methods] */
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
    public ChannelPromise promise() {
        return this;
    }

    @Override // io.netty.util.concurrent.DefaultPromise
    protected void checkDeadLock() {
        if (channel().isRegistered()) {
            super.checkDeadLock();
        }
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelPromise unvoid() {
        return this;
    }

    @Override // io.netty.channel.ChannelFuture
    public boolean isVoid() {
        return false;
    }
}
