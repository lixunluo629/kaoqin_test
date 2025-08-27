package io.netty.channel.oio;

import io.netty.channel.ThreadPerChannelEventLoopGroup;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/oio/OioEventLoopGroup.class */
public class OioEventLoopGroup extends ThreadPerChannelEventLoopGroup {
    public OioEventLoopGroup() {
        this(0);
    }

    public OioEventLoopGroup(int maxChannels) {
        this(maxChannels, (ThreadFactory) null);
    }

    public OioEventLoopGroup(int maxChannels, Executor executor) {
        super(maxChannels, executor, new Object[0]);
    }

    public OioEventLoopGroup(int maxChannels, ThreadFactory threadFactory) {
        super(maxChannels, threadFactory, new Object[0]);
    }
}
