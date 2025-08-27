package io.netty.buffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolChunkListMetric.class */
public interface PoolChunkListMetric extends Iterable<PoolChunkMetric> {
    int minUsage();

    int maxUsage();
}
