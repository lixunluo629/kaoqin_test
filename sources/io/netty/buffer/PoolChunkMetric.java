package io.netty.buffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolChunkMetric.class */
public interface PoolChunkMetric {
    int usage();

    int chunkSize();

    int freeBytes();
}
