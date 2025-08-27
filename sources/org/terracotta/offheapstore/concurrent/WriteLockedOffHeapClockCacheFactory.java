package org.terracotta.offheapstore.concurrent;

import org.terracotta.offheapstore.WriteLockedOffHeapClockCache;
import org.terracotta.offheapstore.eviction.EvictionListener;
import org.terracotta.offheapstore.eviction.EvictionListeningWriteLockedOffHeapClockCache;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/WriteLockedOffHeapClockCacheFactory.class */
public class WriteLockedOffHeapClockCacheFactory<K, V> implements Factory<WriteLockedOffHeapClockCache<K, V>> {
    private static final int DEFAULT_TABLE_SIZE = 128;
    private final Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory;
    private final PageSource tableSource;
    private final int tableSize;
    private final EvictionListener<K, V> evictionListener;

    public WriteLockedOffHeapClockCacheFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        this(tableSource, storageEngineFactory, 128);
    }

    public WriteLockedOffHeapClockCacheFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, EvictionListener<K, V> evictionListener) {
        this(tableSource, storageEngineFactory, evictionListener, 128);
    }

    public WriteLockedOffHeapClockCacheFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, int tableSize) {
        this(tableSource, storageEngineFactory, null, tableSize);
    }

    public WriteLockedOffHeapClockCacheFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, EvictionListener<K, V> evictionListener, int tableSize) {
        this.storageEngineFactory = storageEngineFactory;
        this.tableSource = tableSource;
        this.tableSize = tableSize;
        this.evictionListener = evictionListener;
    }

    @Override // org.terracotta.offheapstore.util.Factory
    public WriteLockedOffHeapClockCache<K, V> newInstance() {
        StorageEngine<? super K, ? super V> storageEngine = this.storageEngineFactory.newInstance();
        try {
            if (this.evictionListener == null) {
                return new WriteLockedOffHeapClockCache<>(this.tableSource, storageEngine, this.tableSize);
            }
            return new EvictionListeningWriteLockedOffHeapClockCache(this.evictionListener, this.tableSource, storageEngine, this.tableSize);
        } catch (RuntimeException e) {
            storageEngine.destroy();
            throw e;
        }
    }
}
