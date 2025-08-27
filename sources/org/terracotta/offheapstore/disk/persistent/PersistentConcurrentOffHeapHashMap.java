package org.terracotta.offheapstore.disk.persistent;

import java.io.IOException;
import java.io.ObjectInput;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/PersistentConcurrentOffHeapHashMap.class */
public class PersistentConcurrentOffHeapHashMap<K, V> extends AbstractPersistentConcurrentOffHeapMap<K, V> {
    public PersistentConcurrentOffHeapHashMap(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory) {
        super(new PersistentReadWriteLockedOffHeapHashMapFactory(tableSource, storageEngineFactory));
    }

    public PersistentConcurrentOffHeapHashMap(ObjectInput input, MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory) throws IOException {
        super(new PersistentReadWriteLockedOffHeapHashMapFactory(tableSource, (Factory) storageEngineFactory, false), readSegmentCount(input));
    }

    public PersistentConcurrentOffHeapHashMap(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) {
        super(new PersistentReadWriteLockedOffHeapHashMapFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency)), concurrency);
    }

    public PersistentConcurrentOffHeapHashMap(ObjectInput input, MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) throws IOException {
        super(new PersistentReadWriteLockedOffHeapHashMapFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency), false), readSegmentCount(input));
    }
}
