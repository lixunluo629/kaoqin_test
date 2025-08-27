package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/DelegatingChannelPromiseNotifier.class */
public final class DelegatingChannelPromiseNotifier implements ChannelPromise, ChannelFutureListener {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DelegatingChannelPromiseNotifier.class);
    private final ChannelPromise delegate;
    private final boolean logNotifyFailure;

    public DelegatingChannelPromiseNotifier(ChannelPromise delegate) {
        this(delegate, !(delegate instanceof VoidChannelPromise));
    }

    public DelegatingChannelPromiseNotifier(ChannelPromise delegate, boolean logNotifyFailure) {
        this.delegate = (ChannelPromise) ObjectUtil.checkNotNull(delegate, "delegate");
        this.logNotifyFailure = logNotifyFailure;
    }

    @Override // io.netty.util.concurrent.GenericFutureListener
    public void operationComplete(ChannelFuture future) throws Exception {
        InternalLogger internalLogger = this.logNotifyFailure ? logger : null;
        if (future.isSuccess()) {
            Void result = (Void) future.get();
            PromiseNotificationUtil.trySuccess(this.delegate, result, internalLogger);
        } else if (future.isCancelled()) {
            PromiseNotificationUtil.tryCancel(this.delegate, internalLogger);
        } else {
            Throwable cause = future.cause();
            PromiseNotificationUtil.tryFailure(this.delegate, cause, internalLogger);
        }
    }

    @Override // io.netty.channel.ChannelPromise, io.netty.channel.ChannelFuture
    public Channel channel() {
        return this.delegate.channel();
    }

    @Override // io.netty.util.concurrent.Promise, io.netty.util.concurrent.ProgressivePromise
    public ChannelPromise setSuccess(Void result) {
        this.delegate.setSuccess(result);
        return this;
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelPromise setSuccess() {
        this.delegate.setSuccess();
        return this;
    }

    @Override // io.netty.channel.ChannelPromise
    public boolean trySuccess() {
        return this.delegate.trySuccess();
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean trySuccess(Void result) {
        return this.delegate.trySuccess(result);
    }

    @Override // io.netty.util.concurrent.Promise, io.netty.channel.ChannelPromise
    public ChannelPromise setFailure(Throwable cause) {
        this.delegate.setFailure(cause);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: addListener */
    public Future<Void> addListener2(GenericFutureListener<? extends Future<? super Void>> listener) {
        this.delegate.addListener2(listener);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: addListeners */
    public Future<Void> addListeners2(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        this.delegate.addListeners2(listeners);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: removeListener */
    public Future<Void> removeListener2(GenericFutureListener<? extends Future<? super Void>> listener) {
        this.delegate.removeListener2(listener);
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: removeListeners */
    public Future<Void> removeListeners2(GenericFutureListener<? extends Future<? super Void>>... listeners) {
        this.delegate.removeListeners2(listeners);
        return this;
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean tryFailure(Throwable cause) {
        return this.delegate.tryFailure(cause);
    }

    @Override // io.netty.util.concurrent.Promise
    public boolean setUncancellable() {
        return this.delegate.setUncancellable();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: await */
    public Future<Void> await2() throws InterruptedException {
        this.delegate.await2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: awaitUninterruptibly */
    public Future<Void> awaitUninterruptibly2() {
        this.delegate.awaitUninterruptibly2();
        return this;
    }

    @Override // io.netty.channel.ChannelFuture
    public boolean isVoid() {
        return this.delegate.isVoid();
    }

    @Override // io.netty.channel.ChannelPromise
    public ChannelPromise unvoid() {
        return isVoid() ? new DelegatingChannelPromiseNotifier(this.delegate.unvoid()) : this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return this.delegate.await(timeout, unit);
    }

    @Override // io.netty.util.concurrent.Future
    public boolean await(long timeoutMillis) throws InterruptedException {
        return this.delegate.await(timeoutMillis);
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return this.delegate.awaitUninterruptibly(timeout, unit);
    }

    @Override // io.netty.util.concurrent.Future
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return this.delegate.awaitUninterruptibly(timeoutMillis);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    public Void getNow() {
        return this.delegate.getNow();
    }

    @Override // io.netty.util.concurrent.Future, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return this.delegate.cancel(mayInterruptIfRunning);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.delegate.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.delegate.isDone();
    }

    @Override // java.util.concurrent.Future
    public Void get() throws ExecutionException, InterruptedException {
        return (Void) this.delegate.get();
    }

    @Override // java.util.concurrent.Future
    public Void get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        return (Void) this.delegate.get(timeout, unit);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: sync */
    public Future<Void> sync2() throws InterruptedException {
        this.delegate.sync2();
        return this;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // io.netty.util.concurrent.Future
    /* renamed from: syncUninterruptibly */
    public Future<Void> syncUninterruptibly2() {
        this.delegate.syncUninterruptibly2();
        return this;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return this.delegate.isSuccess();
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isCancellable() {
        return this.delegate.isCancellable();
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return this.delegate.cause();
    }
}
