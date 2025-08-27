package org.ehcache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/PersistentCacheManager.class */
public interface PersistentCacheManager extends CacheManager {
    void destroy() throws CachePersistenceException;

    void destroyCache(String str) throws CachePersistenceException;
}
