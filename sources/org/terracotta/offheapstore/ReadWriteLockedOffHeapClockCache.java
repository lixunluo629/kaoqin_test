package org.terracotta.offheapstore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/ReadWriteLockedOffHeapClockCache.class */
public class ReadWriteLockedOffHeapClockCache<K, V> extends AbstractOffHeapClockCache<K, V> {
    private final ReentrantReadWriteLock lock;

    public ReadWriteLockedOffHeapClockCache(PageSource source, StorageEngine<? super K, ? super V> storageEngine) {
        super(source, storageEngine);
        this.lock = new ReentrantReadWriteLock(true);
    }

    public ReadWriteLockedOffHeapClockCache(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine) {
        super(source, tableAllocationsSteal, storageEngine);
        this.lock = new ReentrantReadWriteLock(true);
    }

    public ReadWriteLockedOffHeapClockCache(PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(source, storageEngine, tableSize);
        this.lock = new ReentrantReadWriteLock(true);
    }

    public ReadWriteLockedOffHeapClockCache(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(source, tableAllocationsSteal, storageEngine, tableSize);
        this.lock = new ReentrantReadWriteLock(true);
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
