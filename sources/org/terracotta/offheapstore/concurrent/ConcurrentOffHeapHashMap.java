package org.terracotta.offheapstore.concurrent;

import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/ConcurrentOffHeapHashMap.class */
public class ConcurrentOffHeapHashMap<K, V> extends AbstractConcurrentOffHeapMap<K, V> {
    public ConcurrentOffHeapHashMap(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        super(new ReadWriteLockedOffHeapHashMapFactory(tableSource, storageEngineFactory));
    }

    public ConcurrentOffHeapHashMap(PageSource tableSource, boolean tableAllocationsSteal, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        super(new ReadWriteLockedOffHeapHashMapFactory(tableSource, tableAllocationsSteal, storageEngineFactory));
    }

    public ConcurrentOffHeapHashMap(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) {
        super(new ReadWriteLockedOffHeapHashMapFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency)), concurrency);
    }

    public ConcurrentOffHeapHashMap(PageSource tableSource, boolean tableAllocationsSteal, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) {
        super(new ReadWriteLockedOffHeapHashMapFactory(tableSource, tableAllocationsSteal, storageEngineFactory, (int) (tableSize / concurrency)), concurrency);
    }
}
