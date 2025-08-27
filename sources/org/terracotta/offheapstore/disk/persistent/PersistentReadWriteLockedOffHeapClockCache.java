package org.terracotta.offheapstore.disk.persistent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/PersistentReadWriteLockedOffHeapClockCache.class */
public class PersistentReadWriteLockedOffHeapClockCache<K, V> extends AbstractPersistentOffHeapCache<K, V> {
    private final ReentrantReadWriteLock lock;

    public PersistentReadWriteLockedOffHeapClockCache(MappedPageSource source, PersistentStorageEngine<? super K, ? super V> storageEngine, boolean bootstrap) {
        super(source, storageEngine, bootstrap);
        this.lock = new ReentrantReadWriteLock();
    }

    public PersistentReadWriteLockedOffHeapClockCache(MappedPageSource source, PersistentStorageEngine<? super K, ? super V> storageEngine, int tableSize, boolean bootstrap) {
        super(source, storageEngine, tableSize, bootstrap);
        this.lock = new ReentrantReadWriteLock();
    }

    @Override // org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, java.util.concurrent.locks.ReadWriteLock
    public Lock readLock() {
        return this.lock.readLock();
    }

    @Override // org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, java.util.concurrent.locks.ReadWriteLock
    public Lock writeLock() {
        return this.lock.writeLock();
    }

    @Override // org.terracotta.offheapstore.Segment
    public ReentrantReadWriteLock getLock() {
        return this.lock;
    }
}
