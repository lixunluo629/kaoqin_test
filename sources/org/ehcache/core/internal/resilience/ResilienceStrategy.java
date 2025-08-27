package org.ehcache.core.internal.resilience;

import java.util.Map;
import org.ehcache.Cache;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/resilience/ResilienceStrategy.class */
public interface ResilienceStrategy<K, V> {
    V getFailure(K k, StoreAccessException storeAccessException);

    V getFailure(K k, V v, StoreAccessException storeAccessException);

    V getFailure(K k, StoreAccessException storeAccessException, CacheLoadingException cacheLoadingException);

    boolean containsKeyFailure(K k, StoreAccessException storeAccessException);

    void putFailure(K k, V v, StoreAccessException storeAccessException);

    void putFailure(K k, V v, StoreAccessException storeAccessException, CacheWritingException cacheWritingException);

    void removeFailure(K k, StoreAccessException storeAccessException);

    void removeFailure(K k, StoreAccessException storeAccessException, CacheWritingException cacheWritingException);

    void clearFailure(StoreAccessException storeAccessException);

    Cache.Entry<K, V> iteratorFailure(StoreAccessException storeAccessException);

    V putIfAbsentFailure(K k, V v, V v2, StoreAccessException storeAccessException, boolean z);

    V putIfAbsentFailure(K k, V v, StoreAccessException storeAccessException, CacheWritingException cacheWritingException);

    V putIfAbsentFailure(K k, V v, StoreAccessException storeAccessException, CacheLoadingException cacheLoadingException);

    boolean removeFailure(K k, V v, StoreAccessException storeAccessException, boolean z);

    boolean removeFailure(K k, V v, StoreAccessException storeAccessException, CacheWritingException cacheWritingException);

    boolean removeFailure(K k, V v, StoreAccessException storeAccessException, CacheLoadingException cacheLoadingException);

    V replaceFailure(K k, V v, StoreAccessException storeAccessException);

    V replaceFailure(K k, V v, StoreAccessException storeAccessException, CacheWritingException cacheWritingException);

    V replaceFailure(K k, V v, StoreAccessException storeAccessException, CacheLoadingException cacheLoadingException);

    boolean replaceFailure(K k, V v, V v2, StoreAccessException storeAccessException, boolean z);

    boolean replaceFailure(K k, V v, V v2, StoreAccessException storeAccessException, CacheWritingException cacheWritingException);

    boolean replaceFailure(K k, V v, V v2, StoreAccessException storeAccessException, CacheLoadingException cacheLoadingException);

    Map<K, V> getAllFailure(Iterable<? extends K> iterable, StoreAccessException storeAccessException);

    Map<K, V> getAllFailure(Iterable<? extends K> iterable, Map<K, V> map, StoreAccessException storeAccessException);

    Map<K, V> getAllFailure(Iterable<? extends K> iterable, StoreAccessException storeAccessException, BulkCacheLoadingException bulkCacheLoadingException);

    void putAllFailure(Map<? extends K, ? extends V> map, StoreAccessException storeAccessException);

    void putAllFailure(Map<? extends K, ? extends V> map, StoreAccessException storeAccessException, BulkCacheWritingException bulkCacheWritingException);

    Map<K, V> removeAllFailure(Iterable<? extends K> iterable, StoreAccessException storeAccessException);

    Map<K, V> removeAllFailure(Iterable<? extends K> iterable, StoreAccessException storeAccessException, BulkCacheWritingException bulkCacheWritingException);
}
