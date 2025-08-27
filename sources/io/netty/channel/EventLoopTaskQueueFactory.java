package io.netty.channel;

import java.util.Queue;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/channel/EventLoopTaskQueueFactory.class */
public interface EventLoopTaskQueueFactory {
    Queue<Runnable> newTaskQueue(int i);
}
