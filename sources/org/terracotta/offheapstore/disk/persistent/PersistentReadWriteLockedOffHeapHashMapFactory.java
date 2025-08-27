package org.terracotta.offheapstore.disk.persistent;

import org.terracotta.offheapstore.disk.paging.MappedPageSource;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/PersistentReadWriteLockedOffHeapHashMapFactory.class */
public class PersistentReadWriteLockedOffHeapHashMapFactory<K, V> implements Factory<PersistentReadWriteLockedOffHeapHashMap<K, V>> {
    private static final int DEFAULT_TABLE_SIZE = 128;
    private final Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory;
    private final MappedPageSource tableSource;
    private final int tableSize;
    private final boolean bootstrap;

    public PersistentReadWriteLockedOffHeapHashMapFactory(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory) {
        this(tableSource, storageEngineFactory, 128);
    }

    public PersistentReadWriteLockedOffHeapHashMapFactory(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, boolean bootstrap) {
        this(tableSource, storageEngineFactory, 128, bootstrap);
    }

    public PersistentReadWriteLockedOffHeapHashMapFactory(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, int tableSize) {
        this(tableSource, storageEngineFactory, tableSize, true);
    }

    public PersistentReadWriteLockedOffHeapHashMapFactory(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, int tableSize, boolean bootstrap) {
        this.storageEngineFactory = storageEngineFactory;
        this.tableSource = tableSource;
        this.tableSize = tableSize;
        this.bootstrap = bootstrap;
    }

    @Override // org.terracotta.offheapstore.util.Factory
    public PersistentReadWriteLockedOffHeapHashMap<K, V> newInstance() {
        PersistentStorageEngine<? super K, ? super V> storageEngine = this.storageEngineFactory.newInstance();
        try {
            return new PersistentReadWriteLockedOffHeapHashMap<>(this.tableSource, storageEngine, this.tableSize, this.bootstrap);
        } catch (RuntimeException e) {
            storageEngine.destroy();
            throw e;
        }
    }
}
