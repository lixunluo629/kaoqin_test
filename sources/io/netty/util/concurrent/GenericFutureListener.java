package io.netty.util.concurrent;

import io.netty.util.concurrent.Future;
import java.util.EventListener;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/GenericFutureListener.class */
public interface GenericFutureListener<F extends Future<?>> extends EventListener {
    void operationComplete(F f) throws Exception;
}
