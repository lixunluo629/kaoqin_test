package org.springframework.cache.jcache.config;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheResolver;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/config/JCacheConfigurer.class */
public interface JCacheConfigurer extends CachingConfigurer {
    CacheResolver exceptionCacheResolver();
}
