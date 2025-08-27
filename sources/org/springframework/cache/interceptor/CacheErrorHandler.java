package org.springframework.cache.interceptor;

import org.springframework.cache.Cache;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheErrorHandler.class */
public interface CacheErrorHandler {
    void handleCacheGetError(RuntimeException runtimeException, Cache cache, Object obj);

    void handleCachePutError(RuntimeException runtimeException, Cache cache, Object obj, Object obj2);

    void handleCacheEvictError(RuntimeException runtimeException, Cache cache, Object obj);

    void handleCacheClearError(RuntimeException runtimeException, Cache cache);
}
