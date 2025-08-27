package io.netty.buffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/ByteBufAllocatorMetric.class */
public interface ByteBufAllocatorMetric {
    long usedHeapMemory();

    long usedDirectMemory();
}
