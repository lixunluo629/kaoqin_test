package org.terracotta.offheapstore.disk.persistent;

import java.io.IOException;
import java.io.ObjectInput;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/disk/persistent/PersistentConcurrentOffHeapClockCache.class */
public class PersistentConcurrentOffHeapClockCache<K, V> extends AbstractPersistentConcurrentOffHeapCache<K, V> {
    public PersistentConcurrentOffHeapClockCache(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory) {
        super(new PersistentReadWriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory));
    }

    public PersistentConcurrentOffHeapClockCache(ObjectInput input, MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory) throws IOException {
        super(new PersistentReadWriteLockedOffHeapClockCacheFactory(tableSource, (Factory) storageEngineFactory, false), readSegmentCount(input));
    }

    public PersistentConcurrentOffHeapClockCache(MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) {
        super(new PersistentReadWriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency)), concurrency);
    }

    public PersistentConcurrentOffHeapClockCache(ObjectInput input, MappedPageSource tableSource, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, long tableSize, int concurrency) throws IOException {
        super(new PersistentReadWriteLockedOffHeapClockCacheFactory(tableSource, storageEngineFactory, (int) (tableSize / concurrency), false), readSegmentCount(input));
    }
}
