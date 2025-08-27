package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.PromiseNotificationUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/PromiseNotifier.class */
public class PromiseNotifier<V, F extends Future<V>> implements GenericFutureListener<F> {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) PromiseNotifier.class);
    private final Promise<? super V>[] promises;
    private final boolean logNotifyFailure;

    @SafeVarargs
    public PromiseNotifier(Promise<? super V>... promises) {
        this(true, promises);
    }

    @SafeVarargs
    public PromiseNotifier(boolean logNotifyFailure, Promise<? super V>... promises) {
        ObjectUtil.checkNotNull(promises, "promises");
        for (Promise<? super V> promise : promises) {
            if (promise == null) {
                throw new IllegalArgumentException("promises contains null Promise");
            }
        }
        this.promises = (Promise[]) promises.clone();
        this.logNotifyFailure = logNotifyFailure;
    }

    @Override // io.netty.util.concurrent.GenericFutureListener
    public void operationComplete(F future) throws Exception {
        InternalLogger internalLogger = this.logNotifyFailure ? logger : null;
        if (future.isSuccess()) {
            Object obj = future.get();
            for (Promise<? super V> p : this.promises) {
                PromiseNotificationUtil.trySuccess(p, obj, internalLogger);
            }
            return;
        }
        if (future.isCancelled()) {
            for (Promise<? super V> p2 : this.promises) {
                PromiseNotificationUtil.tryCancel(p2, internalLogger);
            }
            return;
        }
        Throwable cause = future.cause();
        for (Promise<? super V> p3 : this.promises) {
            PromiseNotificationUtil.tryFailure(p3, cause, internalLogger);
        }
    }
}
