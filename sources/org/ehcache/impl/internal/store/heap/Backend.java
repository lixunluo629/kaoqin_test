package org.ehcache.impl.internal.store.heap;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/heap/Backend.class */
interface Backend<K, V> {
    OnHeapValueHolder<V> remove(K k);

    OnHeapValueHolder<V> computeIfPresent(K k, BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>> biFunction);

    OnHeapValueHolder<V> compute(K k, BiFunction<K, OnHeapValueHolder<V>, OnHeapValueHolder<V>> biFunction);

    Backend<K, V> clear();

    Map<K, OnHeapValueHolder<V>> removeAllWithHash(int i);

    Iterable<K> keySet();

    Iterator<Map.Entry<K, OnHeapValueHolder<V>>> entrySetIterator();

    OnHeapValueHolder<V> get(K k);

    OnHeapValueHolder<V> putIfAbsent(K k, OnHeapValueHolder<V> onHeapValueHolder);

    boolean remove(K k, OnHeapValueHolder<V> onHeapValueHolder);

    boolean replace(K k, OnHeapValueHolder<V> onHeapValueHolder, OnHeapValueHolder<V> onHeapValueHolder2);

    long mappingCount();

    long byteSize();

    long naturalSize();

    void updateUsageInBytesIfRequired(long j);

    Map.Entry<K, OnHeapValueHolder<V>> getEvictionCandidate(Random random, int i, Comparator<? super Store.ValueHolder<V>> comparator, EvictionAdvisor<Object, ? super OnHeapValueHolder<?>> evictionAdvisor);
}
