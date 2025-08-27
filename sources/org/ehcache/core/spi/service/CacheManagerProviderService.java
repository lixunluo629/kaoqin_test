package org.ehcache.core.spi.service;

import org.ehcache.core.spi.store.InternalCacheManager;
import org.ehcache.spi.service.Service;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/CacheManagerProviderService.class */
public interface CacheManagerProviderService extends Service {
    InternalCacheManager getCacheManager();
}
