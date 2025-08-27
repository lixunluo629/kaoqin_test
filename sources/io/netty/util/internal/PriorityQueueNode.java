package io.netty.util.internal;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/PriorityQueueNode.class */
public interface PriorityQueueNode {
    public static final int INDEX_NOT_IN_QUEUE = -1;

    int priorityQueueIndex(DefaultPriorityQueue<?> defaultPriorityQueue);

    void priorityQueueIndex(DefaultPriorityQueue<?> defaultPriorityQueue, int i);
}
