package org.apache.ibatis.cache;

import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.cache.decorators.TransactionalCache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/TransactionalCacheManager.class */
public class TransactionalCacheManager {
    private final Map<Cache, TransactionalCache> transactionalCaches = new HashMap();

    public void clear(Cache cache) {
        getTransactionalCache(cache).clear();
    }

    public Object getObject(Cache cache, CacheKey key) {
        return getTransactionalCache(cache).getObject(key);
    }

    public void putObject(Cache cache, CacheKey key, Object value) {
        getTransactionalCache(cache).putObject(key, value);
    }

    public void commit() {
        for (TransactionalCache txCache : this.transactionalCaches.values()) {
            txCache.commit();
        }
    }

    public void rollback() {
        for (TransactionalCache txCache : this.transactionalCaches.values()) {
            txCache.rollback();
        }
    }

    private TransactionalCache getTransactionalCache(Cache cache) {
        TransactionalCache txCache = this.transactionalCaches.get(cache);
        if (txCache == null) {
            txCache = new TransactionalCache(cache);
            this.transactionalCaches.put(cache, txCache);
        }
        return txCache;
    }
}
