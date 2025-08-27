package org.springframework.cache.support;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/support/NoOpCacheManager.class */
public class NoOpCacheManager implements CacheManager {
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap(16);
    private final Set<String> cacheNames = new LinkedHashSet(16);

    @Override // org.springframework.cache.CacheManager
    public Cache getCache(String name) {
        Cache cache = this.caches.get(name);
        if (cache == null) {
            this.caches.putIfAbsent(name, new NoOpCache(name));
            synchronized (this.cacheNames) {
                this.cacheNames.add(name);
            }
        }
        return this.caches.get(name);
    }

    @Override // org.springframework.cache.CacheManager
    public Collection<String> getCacheNames() {
        Set setUnmodifiableSet;
        synchronized (this.cacheNames) {
            setUnmodifiableSet = Collections.unmodifiableSet(this.cacheNames);
        }
        return setUnmodifiableSet;
    }
}
