package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/AbstractCacheInvoker.class */
public abstract class AbstractCacheInvoker {
    private CacheErrorHandler errorHandler;

    protected AbstractCacheInvoker() {
        this(new SimpleCacheErrorHandler());
    }

    protected AbstractCacheInvoker(CacheErrorHandler errorHandler) {
        Assert.notNull(errorHandler, "ErrorHandler must not be null");
        this.errorHandler = errorHandler;
    }

    public void setErrorHandler(CacheErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public CacheErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    protected Cache.ValueWrapper doGet(Cache cache, Object key) {
        try {
            return cache.get(key);
        } catch (RuntimeException ex) {
            getErrorHandler().handleCacheGetError(ex, cache, key);
            return null;
        }
    }

    protected void doPut(Cache cache, Object key, Object result) {
        try {
            cache.put(key, result);
        } catch (RuntimeException ex) {
            getErrorHandler().handleCachePutError(ex, cache, key, result);
        }
    }

    protected void doEvict(Cache cache, Object key) {
        try {
            cache.evict(key);
        } catch (RuntimeException ex) {
            getErrorHandler().handleCacheEvictError(ex, cache, key);
        }
    }

    protected void doClear(Cache cache) {
        try {
            cache.clear();
        } catch (RuntimeException ex) {
            getErrorHandler().handleCacheClearError(ex, cache);
        }
    }
}
