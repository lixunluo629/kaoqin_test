package org.terracotta.offheapstore;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/WriteLockedOffHeapHashMap.class */
public class WriteLockedOffHeapHashMap<K, V> extends AbstractLockedOffHeapHashMap<K, V> {
    private final Lock lock;

    public WriteLockedOffHeapHashMap(PageSource tableSource, StorageEngine<? super K, ? super V> storageEngine) {
        super(tableSource, storageEngine);
        this.lock = new ReentrantLock();
    }

    public WriteLockedOffHeapHashMap(PageSource tableSource, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(tableSource, storageEngine, tableSize);
        this.lock = new ReentrantLock();
    }

    @Override // org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, java.util.concurrent.locks.ReadWriteLock
    public Lock readLock() {
        return this.lock;
    }

    @Override // org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, java.util.concurrent.locks.ReadWriteLock
    public Lock writeLock() {
        return this.lock;
    }

    @Override // org.terracotta.offheapstore.Segment
    public ReentrantReadWriteLock getLock() {
        throw new UnsupportedOperationException();
    }
}
