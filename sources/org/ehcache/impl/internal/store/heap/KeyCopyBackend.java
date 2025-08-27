package org.ehcache.impl.internal.store.heap;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.impl.internal.store.heap.holders.CopiedOnHeapKey;
import org.ehcache.impl.internal.store.heap.holders.LookupOnlyOnHeapKey;
import org.ehcache.impl.internal.store.heap.holders.OnHeapKey;
import org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder;
import org.ehcache.spi.copy.Copier;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/KeyCopyBackend.class */
class KeyCopyBackend<K, V> implements Backend<K, V> {
    private final boolean byteSized;
    private final Copier<K> keyCopier;
    private final AtomicLong byteSize = new AtomicLong(0);
    private final ConcurrentHashMap<OnHeapKey<K>, OnHeapValueHolder<V>> keyCopyMap = new ConcurrentHashMap<>();

    KeyCopyBackend(boolean byteSized, Copier<K> keyCopier) {
        this.byteSized = byteSized;
        this.keyCopier = keyCopier;
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public boolean remove(K key, OnHeapValueHolder<V> value) {
        return this.keyCopyMap.remove(lookupOnlyKey(key), value);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Map.Entry<K, OnHeapValueHolder<V>> getEvictionCandidate(Random random, int size, Comparator<? super Store.ValueHolder<V>> prioritizer, EvictionAdvisor<Object, ? super OnHeapValueHolder<?>> evictionAdvisor) {
        Map.Entry<OnHeapKey<K>, OnHeapValueHolder<V>> candidate = this.keyCopyMap.getEvictionCandidate(random, size, prioritizer, evictionAdvisor);
        if (candidate == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry(candidate.getKey().getActualKeyObject(), candidate.getValue());
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public long mappingCount() {
        return this.keyCopyMap.mappingCount();
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
        final Iterator<OnHeapKey<K>> iter = this.keyCopyMap.keySet().iterator();
        return new Iterable<K>() { // from class: org.ehcache.impl.internal.store.heap.KeyCopyBackend.1
            @Override // java.lang.Iterable
            public Iterator<K> iterator() {
                return new Iterator<K>() { // from class: org.ehcache.impl.internal.store.heap.KeyCopyBackend.1.1
                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override // java.util.Iterator
                    public K next() {
                        return (K) ((OnHeapKey) iter.next()).getActualKeyObject();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        iter.remove();
                    }
                };
            }
        };
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Iterator<Map.Entry<K, OnHeapValueHolder<V>>> entrySetIterator() {
        final Iterator<Map.Entry<OnHeapKey<K>, OnHeapValueHolder<V>>> iter = this.keyCopyMap.entrySet().iterator();
        return new Iterator<Map.Entry<K, OnHeapValueHolder<V>>>() { // from class: org.ehcache.impl.internal.store.heap.KeyCopyBackend.2
            @Override // java.util.Iterator
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<K, OnHeapValueHolder<V>> next() {
                Map.Entry<OnHeapKey<K>, OnHeapValueHolder<V>> entry = (Map.Entry) iter.next();
                return new AbstractMap.SimpleEntry(entry.getKey().getActualKeyObject(), entry.getValue());
            }

            @Override // java.util.Iterator
            public void remove() {
                iter.remove();
            }
        };
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> compute(K key, final BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>> computeFunction) {
        return this.keyCopyMap.compute((ConcurrentHashMap<OnHeapKey<K>, OnHeapValueHolder<V>>) makeKey(key), (BiFunction<? super ConcurrentHashMap<OnHeapKey<K>, OnHeapValueHolder<V>>, ? super OnHeapValueHolder<V>, ? extends OnHeapValueHolder<V>>) new BiFunction<OnHeapKey<K>, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.KeyCopyBackend.3
            @Override // org.ehcache.core.spi.function.BiFunction
            public OnHeapValueHolder<V> apply(OnHeapKey<K> mappedKey, OnHeapValueHolder<V> mappedValue) {
                return (OnHeapValueHolder) computeFunction.apply(mappedKey.getActualKeyObject(), mappedValue);
            }
        });
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Backend<K, V> clear() {
        return new KeyCopyBackend(this.byteSized, this.keyCopier);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public Map<K, OnHeapValueHolder<V>> removeAllWithHash(int hash) {
        Map<K, OnHeapValueHolder<V>> result = new HashMap<>();
        Map<OnHeapKey<K>, OnHeapValueHolder<V>> removed = this.keyCopyMap.removeAllWithHash(hash);
        long delta = 0;
        for (Map.Entry<OnHeapKey<K>, OnHeapValueHolder<V>> entry : removed.entrySet()) {
            delta -= entry.getValue().size();
            result.put(entry.getKey().getActualKeyObject(), entry.getValue());
        }
        updateUsageInBytesIfRequired(delta);
        return result;
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> remove(K key) {
        return this.keyCopyMap.remove(lookupOnlyKey(key));
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> computeIfPresent(K key, final BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>> computeFunction) {
        return this.keyCopyMap.computeIfPresent((ConcurrentHashMap<OnHeapKey<K>, OnHeapValueHolder<V>>) makeKey(key), (BiFunction<? super ConcurrentHashMap<OnHeapKey<K>, OnHeapValueHolder<V>>, ? super OnHeapValueHolder<V>, ? extends OnHeapValueHolder<V>>) new BiFunction<OnHeapKey<K>, OnHeapValueHolder<V>, OnHeapValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.heap.KeyCopyBackend.4
            @Override // org.ehcache.core.spi.function.BiFunction
            public OnHeapValueHolder<V> apply(OnHeapKey<K> mappedKey, OnHeapValueHolder<V> mappedValue) {
                return (OnHeapValueHolder) computeFunction.apply(mappedKey.getActualKeyObject(), mappedValue);
            }
        });
    }

    private OnHeapKey<K> makeKey(K key) {
        return new CopiedOnHeapKey(key, this.keyCopier);
    }

    private OnHeapKey<K> lookupOnlyKey(K key) {
        return new LookupOnlyOnHeapKey(key);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> get(K key) {
        return this.keyCopyMap.get(lookupOnlyKey(key));
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public OnHeapValueHolder<V> putIfAbsent(K key, OnHeapValueHolder<V> valueHolder) {
        return this.keyCopyMap.putIfAbsent(makeKey(key), valueHolder);
    }

    @Override // org.ehcache.impl.internal.store.heap.Backend
    public boolean replace(K key, OnHeapValueHolder<V> oldValue, OnHeapValueHolder<V> newValue) {
        return this.keyCopyMap.replace(lookupOnlyKey(key), oldValue, newValue);
    }
}
