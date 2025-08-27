package org.ehcache.impl.internal.store.offheap;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/OffHeapMapStatistics.class */
public interface OffHeapMapStatistics {
    long allocatedMemory();

    long occupiedMemory();

    long dataAllocatedMemory();

    long dataOccupiedMemory();

    long dataSize();

    long longSize();

    long tableCapacity();

    long usedSlotCount();

    long removedSlotCount();

    long reprobeLength();

    long vitalMemory();

    long dataVitalMemory();
}
