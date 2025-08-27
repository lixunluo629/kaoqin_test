package org.terracotta.offheapstore.concurrent;

import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/ConcurrentWriteLockedOffHeapHashMap.class */
public class ConcurrentWriteLockedOffHeapHashMap<K, V> extends AbstractConcurrentOffHeapMap<K, V> {
    public ConcurrentWriteLockedOffHeapHashMap(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        super(new WriteLockedOffHeapHashMapFactory(tableSource, storageEngineFactory));
    }

    public ConcurrentWriteLockedOffHeapHashMap(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) {
        super(new WriteLockedOffHeapHashMapFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency)), concurrency);
    }
}
