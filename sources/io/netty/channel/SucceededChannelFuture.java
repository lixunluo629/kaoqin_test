package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/SucceededChannelFuture.class */
final class SucceededChannelFuture extends CompleteChannelFuture {
    SucceededChannelFuture(Channel channel, EventExecutor executor) {
        super(channel, executor);
    }

    @Override // io.netty.util.concurrent.Future
    public Throwable cause() {
        return null;
    }

    @Override // io.netty.util.concurrent.Future
    public boolean isSuccess() {
        return true;
    }
}
