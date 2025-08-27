package org.ehcache.spi.loaderwriter;

import java.util.Map;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/CacheLoaderWriter.class */
public interface CacheLoaderWriter<K, V> {
    V load(K k) throws Exception;

    Map<K, V> loadAll(Iterable<? extends K> iterable) throws Exception;

    void write(K k, V v) throws Exception;

    void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) throws Exception;

    void delete(K k) throws Exception;

    void deleteAll(Iterable<? extends K> iterable) throws Exception;
}
