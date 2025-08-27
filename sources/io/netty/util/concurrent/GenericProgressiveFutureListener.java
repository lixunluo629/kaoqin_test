package io.netty.util.concurrent;

import io.netty.util.concurrent.ProgressiveFuture;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/GenericProgressiveFutureListener.class */
public interface GenericProgressiveFutureListener<F extends ProgressiveFuture<?>> extends GenericFutureListener<F> {
    void operationProgressed(F f, long j, long j2) throws Exception;
}
