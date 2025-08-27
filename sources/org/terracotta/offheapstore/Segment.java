package org.terracotta.offheapstore;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.terracotta.offheapstore.jdk8.BiFunction;
import org.terracotta.offheapstore.jdk8.Function;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/Segment.class */
public interface Segment<K, V> extends ConcurrentMap<K, V>, MapInternals, ReadWriteLock, HashingMap<K, V> {
    V fill(K k, V v);

    V fill(K k, V v, int i);

    V put(K k, V v, int i);

    Integer getMetadata(K k, int i);

    Integer getAndSetMetadata(K k, int i, int i2);

    V getValueAndSetMetadata(K k, int i, int i2);

    ReentrantReadWriteLock getLock() throws UnsupportedOperationException;

    boolean removeNoReturn(Object obj);

    void destroy();

    boolean shrink();

    MetadataTuple<V> computeWithMetadata(K k, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> biFunction);

    MetadataTuple<V> computeIfAbsentWithMetadata(K k, Function<? super K, ? extends MetadataTuple<V>> function);

    MetadataTuple<V> computeIfPresentWithMetadata(K k, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> biFunction);
}
