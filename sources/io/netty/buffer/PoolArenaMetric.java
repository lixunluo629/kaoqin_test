package io.netty.buffer;

import java.util.List;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/buffer/PoolArenaMetric.class */
public interface PoolArenaMetric {
    int numThreadCaches();

    int numTinySubpages();

    int numSmallSubpages();

    int numChunkLists();

    List<PoolSubpageMetric> tinySubpages();

    List<PoolSubpageMetric> smallSubpages();

    List<PoolChunkListMetric> chunkLists();

    long numAllocations();

    long numTinyAllocations();

    long numSmallAllocations();

    long numNormalAllocations();

    long numHugeAllocations();

    long numDeallocations();

    long numTinyDeallocations();

    long numSmallDeallocations();

    long numNormalDeallocations();

    long numHugeDeallocations();

    long numActiveAllocations();

    long numActiveTinyAllocations();

    long numActiveSmallAllocations();

    long numActiveNormalAllocations();

    long numActiveHugeAllocations();

    long numActiveBytes();
}
