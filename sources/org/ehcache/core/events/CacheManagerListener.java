package org.ehcache.core.events;

import org.ehcache.Cache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/events/CacheManagerListener.class */
public interface CacheManagerListener extends StateChangeListener {
    void cacheAdded(String str, Cache<?, ?> cache);

    void cacheRemoved(String str, Cache<?, ?> cache);
}
