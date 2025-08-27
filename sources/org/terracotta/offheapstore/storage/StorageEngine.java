package org.terracotta.offheapstore.storage;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReadWriteLock;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/StorageEngine.class */
public interface StorageEngine<K, V> {

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/StorageEngine$Owner.class */
    public interface Owner extends ReadWriteLock {
        Long getEncodingForHashAndBinary(int i, ByteBuffer byteBuffer);

        long getSize();

        long installMappingForHashAndEncoding(int i, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, int i2);

        Iterable<Long> encodingSet();

        boolean updateEncoding(int i, long j, long j2, long j3);

        Integer getSlotForHashAndEncoding(int i, long j, long j2);

        boolean evict(int i, boolean z);

        boolean isThiefForTableAllocations();
    }

    Long writeMapping(K k, V v, int i, int i2);

    void attachedMapping(long j, int i, int i2);

    void freeMapping(long j, int i, boolean z);

    V readValue(long j);

    boolean equalsValue(Object obj, long j);

    K readKey(long j, int i);

    boolean equalsKey(Object obj, long j);

    void clear();

    long getAllocatedMemory();

    long getOccupiedMemory();

    long getVitalMemory();

    long getDataSize();

    void invalidateCache();

    void bind(Owner owner);

    void destroy();

    boolean shrink();
}
