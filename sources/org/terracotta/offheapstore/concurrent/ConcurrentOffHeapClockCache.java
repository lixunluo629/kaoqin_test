package org.terracotta.offheapstore.concurrent;

import org.terracotta.offheapstore.AbstractOffHeapClockCache;
import org.terracotta.offheapstore.eviction.EvictionListener;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/ConcurrentOffHeapClockCache.class */
public class ConcurrentOffHeapClockCache<K, V> extends AbstractConcurrentOffHeapCache<K, V> {
    public ConcurrentOffHeapClockCache(Factory<? extends AbstractOffHeapClockCache<K, V>> segmentFactory, int concurrency) {
        super(segmentFactory, concurrency);
    }

    public ConcurrentOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        super(new ReadWriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory));
    }

    public ConcurrentOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, EvictionListener<K, V> evictionListener) {
        super(new ReadWriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, evictionListener));
    }

    public ConcurrentOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) {
        super(new ReadWriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency)), concurrency);
    }

    public ConcurrentOffHeapClockCache(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, EvictionListener<K, V> evictionListener, long tableSize, int concurrency) {
        super(new ReadWriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, evictionListener, (int) (tableSize / concurrency)), concurrency);
    }
}
