package io.netty.channel;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/MessageSizeEstimator.class */
public interface MessageSizeEstimator {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/MessageSizeEstimator$Handle.class */
    public interface Handle {
        int size(Object obj);
    }

    Handle newHandle();
}
