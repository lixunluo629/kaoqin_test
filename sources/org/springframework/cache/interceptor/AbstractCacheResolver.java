package org.springframework.cache.interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/AbstractCacheResolver.class */
public abstract class AbstractCacheResolver implements CacheResolver, InitializingBean {
    private CacheManager cacheManager;

    protected abstract Collection<String> getCacheNames(CacheOperationInvocationContext<?> cacheOperationInvocationContext);

    protected AbstractCacheResolver() {
    }

    protected AbstractCacheResolver(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        Assert.notNull(this.cacheManager, "CacheManager is required");
    }

    @Override // org.springframework.cache.interceptor.CacheResolver
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<String> cacheNames = getCacheNames(context);
        if (cacheNames == null) {
            return Collections.emptyList();
        }
        Collection<Cache> result = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            Cache cache = getCacheManager().getCache(cacheName);
            if (cache == null) {
                throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for " + context.getOperation());
            }
            result.add(cache);
        }
        return result;
    }
}
