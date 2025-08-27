package org.ehcache.impl.internal.store.disk.factories;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import org.ehcache.impl.internal.store.offheap.SwitchableEvictionAdvisor;
import org.ehcache.impl.internal.store.offheap.factories.EhcacheSegmentFactory;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;
import org.terracotta.offheapstore.disk.persistent.PersistentReadWriteLockedOffHeapClockCache;
import org.terracotta.offheapstore.disk.persistent.PersistentStorageEngine;
import org.terracotta.offheapstore.pinning.PinnableSegment;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/disk/factories/EhcachePersistentSegmentFactory.class */
public class EhcachePersistentSegmentFactory<K, V> implements Factory<PinnableSegment<K, V>> {
    private final Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory;
    private final MappedPageSource tableSource;
    private final int tableSize;
    private final SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private final EhcacheSegmentFactory.EhcacheSegment.EvictionListener<K, V> evictionListener;
    private final boolean bootstrap;

    public EhcachePersistentSegmentFactory(MappedPageSource source, Factory<? extends PersistentStorageEngine<? super K, ? super V>> storageEngineFactory, int initialTableSize, SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor, EhcacheSegmentFactory.EhcacheSegment.EvictionListener<K, V> evictionListener, boolean bootstrap) {
        this.storageEngineFactory = storageEngineFactory;
        this.tableSource = source;
        this.tableSize = initialTableSize;
        this.evictionAdvisor = evictionAdvisor;
        this.evictionListener = evictionListener;
        this.bootstrap = bootstrap;
    }

    @Override // org.terracotta.offheapstore.util.Factory
    public EhcachePersistentSegment<K, V> newInstance() {
        PersistentStorageEngine<? super K, ? super V> storageEngine = this.storageEngineFactory.newInstance();
        try {
            return new EhcachePersistentSegment<>(this.tableSource, storageEngine, this.tableSize, this.bootstrap, this.evictionAdvisor, this.evictionListener);
        } catch (RuntimeException e) {
            storageEngine.destroy();
            throw e;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/disk/factories/EhcachePersistentSegmentFactory$EhcachePersistentSegment.class */
    public static class EhcachePersistentSegment<K, V> extends PersistentReadWriteLockedOffHeapClockCache<K, V> {
        private final SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor;
        private final EhcacheSegmentFactory.EhcacheSegment.EvictionListener<K, V> evictionListener;

        EhcachePersistentSegment(MappedPageSource source, PersistentStorageEngine<? super K, ? super V> storageEngine, int tableSize, boolean bootstrap, SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor, EhcacheSegmentFactory.EhcacheSegment.EvictionListener<K, V> evictionListener) {
            super(source, storageEngine, tableSize, bootstrap);
            this.evictionAdvisor = evictionAdvisor;
            this.evictionListener = evictionListener;
        }

        @Override // org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
        public V put(K key, V value) {
            int metadata = getEvictionAdviceStatus(key, value);
            return put(key, value, metadata);
        }

        private int getEvictionAdviceStatus(K key, V value) {
            return this.evictionAdvisor.adviseAgainstEviction(key, value) ? 536870912 : 0;
        }

        @Override // org.terracotta.offheapstore.AbstractOffHeapClockCache, org.terracotta.offheapstore.pinning.PinnableCache
        public V putPinned(K key, V value) {
            int metadata = getEvictionAdviceStatus(key, value) | 1073741824;
            return put(key, value, metadata);
        }

        @Override // org.terracotta.offheapstore.AbstractOffHeapClockCache
        protected boolean evictable(int status) {
            return super.evictable(status) && ((status & 536870912) == 0 || !this.evictionAdvisor.isSwitchedOn());
        }

        @Override // org.terracotta.offheapstore.AbstractOffHeapClockCache, org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.storage.StorageEngine.Owner
        public boolean evict(int index, boolean shrink) {
            Lock lock = writeLock();
            lock.lock();
            try {
                Map.Entry<K, V> entry = getEntryAtTableOffset(index);
                boolean evicted = super.evict(index, shrink);
                if (evicted) {
                    this.evictionListener.onEviction(entry.getKey(), entry.getValue());
                }
                return evicted;
            } finally {
                lock.unlock();
            }
        }
    }
}
