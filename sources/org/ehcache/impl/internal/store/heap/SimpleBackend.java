package org.ehcache.impl.internal.store.heap;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/SimpleBackend.class */
class SimpleBackend<K, V> implements Backend<K, V> {
    private final boolean byteSized;
    private final AtomicLong byteSize = new AtomicLong(0);
    private final ConcurrentHashMap<K, OnHeapValueHolder<V>> realMap = new ConcurrentHashMap<>();

    SimpleBackend(boolean byteSized) {
        this.byteSized = byteSized;
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public boolean remove(K key, OnHeapValueHolder<V> value) {
        return this.realMap.remove(key, value);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Map.Entry<K, OnHeapValueHolder<V>> getEvictionCandidate(Random random, int size, Comparator<? super Store.ValueHolder<V>> prioritizer, EvictionAdvisor<Object, ? super OnHeapValueHolder<?>> evictionAdvisor) {
        return this.realMap.getEvictionCandidate(random, size, prioritizer, evictionAdvisor);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public long mappingCount() {
        return this.realMap.mappingCount();
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public long byteSize() {
        if (this.byteSized) {
            return this.byteSize.get();
        }
        throw new IllegalStateException("This store is not byte sized");
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public long naturalSize() {
        if (this.byteSized) {
            return this.byteSize.get();
        }
        return mappingCount();
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public void updateUsageInBytesIfRequired(long delta) {
        if (this.byteSized) {
            this.byteSize.addAndGet(delta);
        }
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Iterable<K> keySet() {
        return this.realMap.keySet();
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Iterator<Map.Entry<K, OnHeapValueHolder<V>>> entrySetIterator() {
        return this.realMap.entrySet().iterator();
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> compute(K key, BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>> computeFunction) {
        return this.realMap.compute((ConcurrentHashMap<K, OnHeapValueHolder<V>>) key, (BiFunction<? super ConcurrentHashMap<K, OnHeapValueHolder<V>>, ? super OnHeapValueHolder<V>, ? extends OnHeapValueHolder<V>>) computeFunction);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Backend<K, V> clear() {
        return new SimpleBackend(this.byteSized);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Map<K, OnHeapValueHolder<V>> removeAllWithHash(int hash) {
        Map<K, OnHeapValueHolder<V>> removed = this.realMap.removeAllWithHash(hash);
        if (this.byteSized) {
            long delta = 0;
            for (Map.Entry<K, OnHeapValueHolder<V>> entry : removed.entrySet()) {
                delta -= entry.getValue().size();
            }
            updateUsageInBytesIfRequired(delta);
        }
        return removed;
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> remove(K key) {
        return this.realMap.remove(key);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> computeIfPresent(K key, BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>> computeFunction) {
        return this.realMap.computeIfPresent((ConcurrentHashMap<K, OnHeapValueHolder<V>>) key, (BiFunction<? super ConcurrentHashMap<K, OnHeapValueHolder<V>>, ? super OnHeapValueHolder<V>, ? extends OnHeapValueHolder<V>>) computeFunction);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> get(K key) {
        return this.realMap.get(key);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> putIfAbsent(K key, OnHeapValueHolder<V> valueHolder) {
        return this.realMap.putIfAbsent(key, valueHolder);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public boolean replace(K key, OnHeapValueHolder<V> oldValue, OnHeapValueHolder<V> newValue) {
        return this.realMap.replace(key, oldValue, newValue);
    }
}
