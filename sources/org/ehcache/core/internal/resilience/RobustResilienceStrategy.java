package org.ehcache.core.internal.resilience;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/resilience/RobustResilienceStrategy.class */
public abstract class RobustResilienceStrategy<K, V> implements ResilienceStrategy<K, V> {
    private final RecoveryCache<K> cache;

    protected abstract void recovered(K k, StoreAccessException storeAccessException);

    protected abstract void recovered(Iterable<? extends K> iterable, StoreAccessException storeAccessException);

    protected abstract void recovered(StoreAccessException storeAccessException);

    protected abstract void inconsistent(K k, StoreAccessException storeAccessException, StoreAccessException... storeAccessExceptionArr);

    protected abstract void inconsistent(Iterable<? extends K> iterable, StoreAccessException storeAccessException, StoreAccessException... storeAccessExceptionArr);

    protected abstract void inconsistent(StoreAccessException storeAccessException, StoreAccessException... storeAccessExceptionArr);

    public RobustResilienceStrategy(RecoveryCache<K> cache) {
        this.cache = cache;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V getFailure(K key, StoreAccessException e) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        return null;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V getFailure(K key, V loaded, StoreAccessException e) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        return loaded;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V getFailure(K key, StoreAccessException e, CacheLoadingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public boolean containsKeyFailure(K key, StoreAccessException e) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        return false;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public void putFailure(K key, V value, StoreAccessException e) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public void putFailure(K key, V value, StoreAccessException e, CacheWritingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public void removeFailure(K key, StoreAccessException e) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public void removeFailure(K key, StoreAccessException e, CacheWritingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public void clearFailure(StoreAccessException e) throws RuntimeException {
        cleanup(e);
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V putIfAbsentFailure(K key, V value, V loaderWriterFunctionResult, StoreAccessException e, boolean knownToBeAbsent) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        if (loaderWriterFunctionResult != null && !loaderWriterFunctionResult.equals(value)) {
            return loaderWriterFunctionResult;
        }
        return null;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V putIfAbsentFailure(K key, V value, StoreAccessException e, CacheWritingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V putIfAbsentFailure(K key, V value, StoreAccessException e, CacheLoadingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public boolean removeFailure(K key, V value, StoreAccessException e, boolean knownToBePresent) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        return knownToBePresent;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public boolean removeFailure(K key, V value, StoreAccessException e, CacheWritingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public boolean removeFailure(K key, V value, StoreAccessException e, CacheLoadingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V replaceFailure(K key, V value, StoreAccessException e) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        return null;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V replaceFailure(K key, V value, StoreAccessException e, CacheWritingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public V replaceFailure(K key, V value, StoreAccessException e, CacheLoadingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public boolean replaceFailure(K key, V value, V newValue, StoreAccessException e, boolean knownToMatch) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        return knownToMatch;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public boolean replaceFailure(K key, V value, V newValue, StoreAccessException e, CacheWritingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public boolean replaceFailure(K key, V value, V newValue, StoreAccessException e, CacheLoadingException f) throws RuntimeException {
        cleanup((RobustResilienceStrategy<K, V>) key, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public Map<K, V> getAllFailure(Iterable<? extends K> keys, StoreAccessException e) throws RuntimeException {
        cleanup((Iterable) keys, e);
        HashMap<K, V> result = new HashMap<>();
        for (K key : keys) {
            result.put(key, null);
        }
        return result;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public Map<K, V> getAllFailure(Iterable<? extends K> keys, Map<K, V> loaded, StoreAccessException e) throws RuntimeException {
        cleanup((Iterable) keys, e);
        return loaded;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public Map<K, V> getAllFailure(Iterable<? extends K> keys, StoreAccessException e, BulkCacheLoadingException f) throws RuntimeException {
        cleanup((Iterable) keys, e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public void putAllFailure(Map<? extends K, ? extends V> entries, StoreAccessException e) throws RuntimeException {
        cleanup((Iterable) entries.keySet(), e);
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public void putAllFailure(Map<? extends K, ? extends V> entries, StoreAccessException e, BulkCacheWritingException f) throws RuntimeException {
        cleanup((Iterable) entries.keySet(), e);
        throw f;
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public Map<K, V> removeAllFailure(Iterable<? extends K> entries, StoreAccessException e) throws RuntimeException {
        cleanup((Iterable) entries, e);
        return Collections.emptyMap();
    }

    @Override // org.ehcache.core.internal.resilience.ResilienceStrategy
    public Map<K, V> removeAllFailure(Iterable<? extends K> entries, StoreAccessException e, BulkCacheWritingException f) throws RuntimeException {
        cleanup((Iterable) entries, e);
        throw f;
    }

    private void cleanup(StoreAccessException from) throws RuntimeException {
        filterException(from);
        try {
            this.cache.obliterate();
            recovered(from);
        } catch (StoreAccessException e) {
            inconsistent(from, e);
        }
    }

    private void cleanup(Iterable<? extends K> keys, StoreAccessException from) throws RuntimeException {
        filterException(from);
        try {
            this.cache.obliterate((Iterable) keys);
            recovered((Iterable) keys, from);
        } catch (StoreAccessException e) {
            inconsistent((Iterable) keys, from, e);
        }
    }

    private void cleanup(K key, StoreAccessException from) throws RuntimeException {
        filterException(from);
        try {
            this.cache.obliterate((RecoveryCache<K>) key);
            recovered((RobustResilienceStrategy<K, V>) key, from);
        } catch (StoreAccessException e) {
            inconsistent((RobustResilienceStrategy<K, V>) key, from, e);
        }
    }

    @Deprecated
    void filterException(StoreAccessException cae) throws RuntimeException {
        if (cae instanceof RethrowingStoreAccessException) {
            throw ((RethrowingStoreAccessException) cae).getCause();
        }
    }
}
