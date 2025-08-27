package org.terracotta.offheapstore.eviction;

import java.util.Map;
import java.util.concurrent.Callable;
import org.terracotta.offheapstore.ReadWriteLockedOffHeapClockCache;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/eviction/EvictionListeningReadWriteLockedOffHeapClockCache.class */
public class EvictionListeningReadWriteLockedOffHeapClockCache<K, V> extends ReadWriteLockedOffHeapClockCache<K, V> {
    private final EvictionListener<K, V> listener;

    public EvictionListeningReadWriteLockedOffHeapClockCache(EvictionListener<K, V> listener, PageSource source, StorageEngine<? super K, ? super V> storageEngine) {
        super(source, storageEngine);
        this.listener = listener;
    }

    public EvictionListeningReadWriteLockedOffHeapClockCache(EvictionListener<K, V> listener, PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(source, storageEngine, tableSize);
        this.listener = listener;
    }

    @Override // org.terracotta.offheapstore.AbstractOffHeapClockCache, org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.storage.StorageEngine.Owner
    public boolean evict(final int index, boolean shrink) {
        try {
            this.listener.evicting(new Callable<Map.Entry<K, V>>() { // from class: org.terracotta.offheapstore.eviction.EvictionListeningReadWriteLockedOffHeapClockCache.1
                @Override // java.util.concurrent.Callable
                public Map.Entry<K, V> call() throws Exception {
                    return EvictionListeningReadWriteLockedOffHeapClockCache.this.getEntryAtTableOffset(index);
                }
            });
            boolean evicted = super.evict(index, shrink);
            return evicted;
        } catch (Throwable th) {
            super.evict(index, shrink);
            throw th;
        }
    }
}
