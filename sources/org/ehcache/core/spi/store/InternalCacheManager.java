package org.ehcache.core.spi.store;

import org.ehcache.CacheManager;
import org.ehcache.core.events.CacheManagerListener;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/InternalCacheManager.class */
public interface InternalCacheManager extends CacheManager {
    void registerListener(CacheManagerListener cacheManagerListener);

    void deregisterListener(CacheManagerListener cacheManagerListener);
}
