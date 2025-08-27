package org.terracotta.offheapstore.concurrent;

import org.terracotta.offheapstore.ReadWriteLockedOffHeapHashMap;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/ReadWriteLockedOffHeapHashMapFactory.class */
public class ReadWriteLockedOffHeapHashMapFactory<K, V> implements Factory<ReadWriteLockedOffHeapHashMap<K, V>> {
    private static final int DEFAULT_TABLE_SIZE = 128;
    private final Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory;
    private final PageSource tableSource;
    private final boolean tableAllocationsSteal;
    private final int tableSize;

    public ReadWriteLockedOffHeapHashMapFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        this(tableSource, false, storageEngineFactory, 128);
    }

    public ReadWriteLockedOffHeapHashMapFactory(PageSource tableSource, boolean tableAllocationsSteal, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory) {
        this(tableSource, tableAllocationsSteal, storageEngineFactory, 128);
    }

    public ReadWriteLockedOffHeapHashMapFactory(PageSource tableSource, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, int tableSize) {
        this(tableSource, false, storageEngineFactory, tableSize);
    }

    public ReadWriteLockedOffHeapHashMapFactory(PageSource tableSource, boolean tableAllocationsSteal, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, int tableSize) {
        this.storageEngineFactory = storageEngineFactory;
        this.tableSource = tableSource;
        this.tableAllocationsSteal = tableAllocationsSteal;
        this.tableSize = tableSize;
    }

    @Override // org.terracotta.offheapstore.util.Factory
    public ReadWriteLockedOffHeapHashMap<K, V> newInstance() {
        StorageEngine<? super K, ? super V> storageEngine = this.storageEngineFactory.newInstance();
        try {
            return new ReadWriteLockedOffHeapHashMap<>(this.tableSource, this.tableAllocationsSteal, storageEngine, this.tableSize);
        } catch (RuntimeException e) {
            storageEngine.destroy();
            throw e;
        }
    }
}
