package io.netty.channel;

import io.netty.util.IntSupplier;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/DefaultSelectStrategy.class */
final class DefaultSelectStrategy implements SelectStrategy {
    static final SelectStrategy INSTANCE = new DefaultSelectStrategy();

    private DefaultSelectStrategy() {
    }

    @Override // io.netty.channel.SelectStrategy
    public int calculateStrategy(IntSupplier selectSupplier, boolean hasTasks) throws Exception {
        if (hasTasks) {
            return selectSupplier.get();
        }
        return -1;
    }
}
