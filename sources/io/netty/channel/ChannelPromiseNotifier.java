package io.netty.channel;

import io.netty.util.concurrent.PromiseNotifier;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelPromiseNotifier.class */
public final class ChannelPromiseNotifier extends PromiseNotifier<Void, ChannelFuture> implements ChannelFutureListener {
    public ChannelPromiseNotifier(ChannelPromise... promises) {
        super(promises);
    }

    public ChannelPromiseNotifier(boolean logNotifyFailure, ChannelPromise... promises) {
        super(logNotifyFailure, promises);
    }
}
