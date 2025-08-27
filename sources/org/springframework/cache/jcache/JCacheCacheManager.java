package org.springframework.cache.jcache;

import java.util.Collection;
import java.util.LinkedHashSet;
import javax.cache.CacheManager;
import javax.cache.Caching;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/JCacheCacheManager.class */
public class JCacheCacheManager extends AbstractTransactionSupportingCacheManager {
    private CacheManager cacheManager;
    private boolean allowNullValues = true;

    public JCacheCacheManager() {
    }

    public JCacheCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public void setAllowNullValues(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    @Override // org.springframework.cache.support.AbstractCacheManager, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (getCacheManager() == null) {
            setCacheManager(Caching.getCachingProvider().getCacheManager());
        }
        super.afterPropertiesSet();
    }

    @Override // org.springframework.cache.support.AbstractCacheManager
    protected Collection<Cache> loadCaches() {
        Collection<Cache> caches = new LinkedHashSet<>();
        for (String cacheName : getCacheManager().getCacheNames()) {
            javax.cache.Cache<Object, Object> jcache = getCacheManager().getCache(cacheName);
            caches.add(new JCacheCache(jcache, isAllowNullValues()));
        }
        return caches;
    }

    @Override // org.springframework.cache.support.AbstractCacheManager
    protected Cache getMissingCache(String name) {
        javax.cache.Cache<Object, Object> jcache = getCacheManager().getCache(name);
        if (jcache != null) {
            return new JCacheCache(jcache, isAllowNullValues());
        }
        return null;
    }
}
