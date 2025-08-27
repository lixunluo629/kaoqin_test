package org.springframework.cache.jcache.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/config/JCacheConfigurerSupport.class */
public class JCacheConfigurerSupport extends CachingConfigurerSupport implements JCacheConfigurer {
    @Override // org.springframework.cache.jcache.config.JCacheConfigurer
    public CacheResolver exceptionCacheResolver() {
        return null;
    }
}
