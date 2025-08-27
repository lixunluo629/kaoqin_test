package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/SimpleCacheErrorHandler.class */
public class SimpleCacheErrorHandler implements CacheErrorHandler {
    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        throw exception;
    }

    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        throw exception;
    }

    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        throw exception;
    }

    @Override // org.springframework.cache.interceptor.CacheErrorHandler
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        throw exception;
    }
}
