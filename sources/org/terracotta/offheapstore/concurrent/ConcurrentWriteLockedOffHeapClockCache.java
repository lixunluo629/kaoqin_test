package org.terracotta.offheapstore.concurrent;

import org.terracotta.offheapstore.eviction.EvictionListener;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/ConcurrentWriteLockedOffHeapClockCache.class */
public class ConcurrentWriteLockedOffHeapClockCache<K, V> extends AbstractConcurrentOffHeapCache<K, V> {
    public ConcurrentWriteLockedOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        super(new WriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory));
    }

    public ConcurrentWriteLockedOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, EvictionListener<K, V> evictionListener) {
        super(new WriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, evictionListener));
    }

    public ConcurrentWriteLockedOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) {
        super(new WriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency)), concurrency);
    }

    public ConcurrentWriteLockedOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, EvictionListener<K, V> evictionListener, long tableSize, int concurrency) {
        super(new WriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, evictionListener, (int) (tableSize / concurrency)), concurrency);
    }
}
