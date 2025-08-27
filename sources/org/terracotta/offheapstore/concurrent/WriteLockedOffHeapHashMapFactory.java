package org.terracotta.offheapstore.concurrent;

import org.terracotta.offheapstore.WriteLockedOffHeapHashMap;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/WriteLockedOffHeapHashMapFactory.class */
public class WriteLockedOffHeapHashMapFactory<K, V> implements Factory<WriteLockedOffHeapHashMap<K, V>> {
    private static final int DEFAULT_TABLE_SIZE = 128;
    private final Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory;
    private final PageSource tableSource;
    private final int tableSize;

    public WriteLockedOffHeapHashMapFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        this(tableSource, storageEngineFactory, 128);
    }

    public WriteLockedOffHeapHashMapFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, int tableSize) {
        this.storageEngineFactory = storageEngineFactory;
        this.tableSource = tableSource;
        this.tableSize = tableSize;
    }

    @Override // org.terracotta.offheapstore.util.Factory
    public WriteLockedOffHeapHashMap<K, V> newInstance() {
        StorageEngine<? super K, ? super V> storageEngine = this.storageEngineFactory.newInstance();
        try {
            return new WriteLockedOffHeapHashMap<>(this.tableSource, storageEngine, this.tableSize);
        } catch (RuntimeException e) {
            storageEngine.destroy();
            throw e;
        }
    }
}
