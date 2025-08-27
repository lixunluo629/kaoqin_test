package io.netty.util.concurrent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/EventExecutor.class */
public interface EventExecutor extends EventExecutorGroup {
    @Override // io.netty.util.concurrent.EventExecutorGroup, io.netty.channel.EventLoopGroup
    EventExecutor next();

    EventExecutorGroup parent();

    boolean inEventLoop();

    boolean inEventLoop(Thread thread);

    <V> Promise<V> newPromise();

    <V> ProgressivePromise<V> newProgressivePromise();

    <V> Future<V> newSucceededFuture(V v);

    <V> Future<V> newFailedFuture(Throwable th);
}
