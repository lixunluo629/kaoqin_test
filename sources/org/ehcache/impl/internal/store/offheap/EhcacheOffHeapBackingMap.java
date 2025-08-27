package org.ehcache.impl.internal.store.offheap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.terracotta.offheapstore.Segment;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/EhcacheOffHeapBackingMap.class */
public interface EhcacheOffHeapBackingMap<K, V> extends ConcurrentMap<K, V>, OffHeapMapStatistics {
    V compute(K k, BiFunction<K, V, V> biFunction, boolean z);

    V computeIfPresent(K k, BiFunction<K, V, V> biFunction);

    boolean computeIfPinned(K k, BiFunction<K, V, V> biFunction, Function<V, Boolean> function);

    V computeIfPresentAndPin(K k, BiFunction<K, V, V> biFunction);

    long nextIdFor(K k);

    V getAndPin(K k);

    Integer getAndSetMetadata(K k, int i, int i2);

    List<Segment<K, V>> getSegments();

    boolean shrinkOthers(int i);

    Map<K, V> removeAllWithHash(int i);
}
