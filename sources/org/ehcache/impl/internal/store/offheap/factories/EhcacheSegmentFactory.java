package org.ehcache.impl.internal.store.offheap.factories;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import org.ehcache.impl.internal.store.offheap.SwitchableEvictionAdvisor;
import org.terracotta.offheapstore.ReadWriteLockedOffHeapClockCache;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.pinning.PinnableSegment;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/factories/EhcacheSegmentFactory.class */
public class EhcacheSegmentFactory<K, V> implements Factory<PinnableSegment<K, V>> {
    private final Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory;
    private final PageSource tableSource;
    private final int tableSize;
    private final SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private final EhcacheSegment.EvictionListener<K, V> evictionListener;

    public EhcacheSegmentFactory(PageSource source, Factory<? extends StorageEngine<? super K, ? super V>> storageEngineFactory, int initialTableSize, SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor, EhcacheSegment.EvictionListener<K, V> evictionListener) {
        this.storageEngineFactory = storageEngineFactory;
        this.tableSource = source;
        this.tableSize = initialTableSize;
        this.evictionAdvisor = evictionAdvisor;
        this.evictionListener = evictionListener;
    }

    @Override // org.terracotta.offheapstore.util.Factory
    public PinnableSegment<K, V> newInstance() {
        StorageEngine<? super K, ? super V> storageEngine = this.storageEngineFactory.newInstance();
        try {
            return new EhcacheSegment(this.tableSource, storageEngine, this.tableSize, this.evictionAdvisor, this.evictionListener);
        } catch (RuntimeException e) {
            storageEngine.destroy();
            throw e;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/factories/EhcacheSegmentFactory$EhcacheSegment.class */
    public static class EhcacheSegment<K, V> extends ReadWriteLockedOffHeapClockCache<K, V> {
        public static final int ADVISED_AGAINST_EVICTION = 536870912;
        private final SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor;
        private final EvictionListener<K, V> evictionListener;

        /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/factories/EhcacheSegmentFactory$EhcacheSegment$EvictionListener.class */
        public interface EvictionListener<K, V> {
            void onEviction(K k, V v);
        }

        EhcacheSegment(PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize, SwitchableEvictionAdvisor<? super K, ? super V> evictionAdvisor, EvictionListener<K, V> evictionListener) {
            super(source, true, storageEngine, tableSize);
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
