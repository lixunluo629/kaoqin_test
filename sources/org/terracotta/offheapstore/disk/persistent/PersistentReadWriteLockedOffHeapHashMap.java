package org.terracotta.offheapstore.disk.persistent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/PersistentReadWriteLockedOffHeapHashMap.class */
public class PersistentReadWriteLockedOffHeapHashMap<K, V> extends AbstractPersistentLockedOffHeapHashMap<K, V> {
    private final ReentrantReadWriteLock lock;

    public PersistentReadWriteLockedOffHeapHashMap(MappedPageSource tableSource, PersistentStorageEngine<? super K, ? super V> storageEngine, boolean bootstrap) {
        super(tableSource, storageEngine, bootstrap);
        this.lock = new ReentrantReadWriteLock();
    }

    public PersistentReadWriteLockedOffHeapHashMap(MappedPageSource tableSource, PersistentStorageEngine<? super K, ? super V> storageEngine, int tableSize, boolean bootstrap) {
        super(tableSource, storageEngine, tableSize, bootstrap);
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
