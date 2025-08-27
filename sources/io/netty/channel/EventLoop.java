package io.netty.channel;

import io.netty.util.concurrent.OrderedEventExecutor;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/EventLoop.class */
public interface EventLoop extends OrderedEventExecutor, EventLoopGroup {
    EventLoopGroup parent();
}
