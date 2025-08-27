package org.springframework.cache.annotation;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/annotation/CachingConfigurerSupport.class */
public class CachingConfigurerSupport implements CachingConfigurer {
    @Override // org.springframework.cache.annotation.CachingConfigurer
    public CacheManager cacheManager() {
        return null;
    }

    @Override // org.springframework.cache.annotation.CachingConfigurer
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override // org.springframework.cache.annotation.CachingConfigurer
    public KeyGenerator keyGenerator() {
        return null;
    }

    @Override // org.springframework.cache.annotation.CachingConfigurer
    public CacheErrorHandler errorHandler() {
        return null;
    }
}
