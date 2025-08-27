package org.springframework.cache.jcache.config;

import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.jcache.interceptor.DefaultJCacheOperationSource;
import org.springframework.cache.jcache.interceptor.JCacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration
/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/config/AbstractJCacheConfiguration.class */
public class AbstractJCacheConfiguration extends AbstractCachingConfiguration {
    protected CacheResolver exceptionCacheResolver;

    @Override // org.springframework.cache.annotation.AbstractCachingConfiguration
    protected void useCachingConfigurer(CachingConfigurer config) {
        super.useCachingConfigurer(config);
        if (config instanceof JCacheConfigurer) {
            this.exceptionCacheResolver = ((JCacheConfigurer) config).exceptionCacheResolver();
        }
    }

    @Bean(name = {"jCacheOperationSource"})
    @Role(2)
    public JCacheOperationSource cacheOperationSource() {
        DefaultJCacheOperationSource source = new DefaultJCacheOperationSource();
        if (this.cacheManager != null) {
            source.setCacheManager(this.cacheManager);
        }
        if (this.keyGenerator != null) {
            source.setKeyGenerator(this.keyGenerator);
        }
        if (this.cacheResolver != null) {
            source.setCacheResolver(this.cacheResolver);
        }
        if (this.exceptionCacheResolver != null) {
            source.setExceptionCacheResolver(this.exceptionCacheResolver);
        }
        return source;
    }
}
