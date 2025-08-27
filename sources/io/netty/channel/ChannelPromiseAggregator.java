package io.netty.channel;

import io.netty.util.concurrent.PromiseAggregator;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/ChannelPromiseAggregator.class */
public final class ChannelPromiseAggregator extends PromiseAggregator<Void, ChannelFuture> implements ChannelFutureListener {
    public ChannelPromiseAggregator(ChannelPromise aggregatePromise) {
        super(aggregatePromise);
    }
}
