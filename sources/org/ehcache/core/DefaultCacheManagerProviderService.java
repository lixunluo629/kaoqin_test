package org.ehcache.core;

import org.ehcache.core.spi.service.CacheManagerProviderService;
import org.ehcache.core.spi.store.InternalCacheManager;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/DefaultCacheManagerProviderService.class */
class DefaultCacheManagerProviderService implements CacheManagerProviderService {
    private final InternalCacheManager cacheManager;

    DefaultCacheManagerProviderService(InternalCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override // org.ehcache.core.spi.service.CacheManagerProviderService
    public InternalCacheManager getCacheManager() {
        return this.cacheManager;
    }

    @Override // org.ehcache.spi.service.Service
    public void start(ServiceProvider<Service> serviceProvider) {
    }

    @Override // org.ehcache.spi.service.Service
    public void stop() {
    }
}
