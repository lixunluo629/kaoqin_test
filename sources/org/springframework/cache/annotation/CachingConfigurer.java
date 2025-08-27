package org.springframework.cache.annotation;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/annotation/CachingConfigurer.class */
public interface CachingConfigurer {
    CacheManager cacheManager();

    CacheResolver cacheResolver();

    KeyGenerator keyGenerator();

    CacheErrorHandler errorHandler();
}
