package io.netty.util.concurrent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/Promise.class */
public interface Promise<V> extends Future<V> {
    Promise<V> setSuccess(V v);

    boolean trySuccess(V v);

    Promise<V> setFailure(Throwable th);

    boolean tryFailure(Throwable th);

    boolean setUncancellable();

    Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener);

    Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... genericFutureListenerArr);

    Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> genericFutureListener);

    Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... genericFutureListenerArr);

    Promise<V> await() throws InterruptedException;

    Promise<V> awaitUninterruptibly();

    Promise<V> sync() throws InterruptedException;

    Promise<V> syncUninterruptibly();
}
