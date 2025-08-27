package org.terracotta.offheapstore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/ReadWriteLockedOffHeapHashMap.class */
public class ReadWriteLockedOffHeapHashMap<K, V> extends AbstractLockedOffHeapHashMap<K, V> {
    private final ReentrantReadWriteLock lock;

    public ReadWriteLockedOffHeapHashMap(PageSource tableSource, StorageEngine<? super K, ? super V> storageEngine) {
        super(tableSource, storageEngine);
        this.lock = new ReentrantReadWriteLock();
    }

    public ReadWriteLockedOffHeapHashMap(PageSource tableSource, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine) {
        super(tableSource, tableAllocationsSteal, storageEngine);
        this.lock = new ReentrantReadWriteLock();
    }

    public ReadWriteLockedOffHeapHashMap(PageSource tableSource, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(tableSource, storageEngine, tableSize);
        this.lock = new ReentrantReadWriteLock();
    }

    public ReadWriteLockedOffHeapHashMap(PageSource tableSource, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(tableSource, tableAllocationsSteal, storageEngine, tableSize);
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
