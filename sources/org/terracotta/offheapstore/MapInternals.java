package org.terracotta.offheapstore;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/MapInternals.class */
public interface MapInternals {
    long getSize();

    long getTableCapacity();

    long getUsedSlotCount();

    long getRemovedSlotCount();

    int getReprobeLength();

    long getAllocatedMemory();

    long getOccupiedMemory();

    long getVitalMemory();

    long getDataAllocatedMemory();

    long getDataOccupiedMemory();

    long getDataVitalMemory();

    long getDataSize();
}
