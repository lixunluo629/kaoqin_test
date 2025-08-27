package org.ehcache;

import java.util.Map;
import java.util.Set;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/Cache.class */
public interface Cache<K, V> extends Iterable<Entry<K, V>> {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/Cache$Entry.class */
    public interface Entry<K, V> {
        K getKey();

        V getValue();
    }

    V get(K k) throws CacheLoadingException;

    void put(K k, V v) throws CacheWritingException;

    boolean containsKey(K k);

    void remove(K k) throws CacheWritingException;

    Map<K, V> getAll(Set<? extends K> set) throws BulkCacheLoadingException;

    void putAll(Map<? extends K, ? extends V> map) throws BulkCacheWritingException;

    void removeAll(Set<? extends K> set) throws BulkCacheWritingException;

    void clear();

    V putIfAbsent(K k, V v) throws CacheLoadingException, CacheWritingException;

    boolean remove(K k, V v) throws CacheWritingException;

    V replace(K k, V v) throws CacheLoadingException, CacheWritingException;

    boolean replace(K k, V v, V v2) throws CacheLoadingException, CacheWritingException;

    CacheRuntimeConfiguration<K, V> getRuntimeConfiguration();
}
