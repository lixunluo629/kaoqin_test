package org.terracotta.offheapstore.storage;

import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/HalfStorageEngine.class */
public interface HalfStorageEngine<T> {
    Integer write(T t, int i);

    void free(int i);

    T read(int i);

    boolean equals(Object obj, int i);

    void clear();

    long getAllocatedMemory();

    long getOccupiedMemory();

    long getVitalMemory();

    long getDataSize();

    void invalidateCache();

    void bind(StorageEngine.Owner owner, long j);

    void destroy();

    boolean shrink();
}
