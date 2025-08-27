package io.netty.buffer;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolSubpageMetric.class */
public interface PoolSubpageMetric {
    int maxNumElements();

    int numAvailable();

    int elementSize();

    int pageSize();
}
