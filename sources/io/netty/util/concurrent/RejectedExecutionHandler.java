package io.netty.util.concurrent;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/RejectedExecutionHandler.class */
public interface RejectedExecutionHandler {
    void rejected(Runnable runnable, SingleThreadEventExecutor singleThreadEventExecutor);
}
