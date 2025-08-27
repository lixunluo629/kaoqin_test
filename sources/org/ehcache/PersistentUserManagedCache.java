package org.ehcache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/PersistentUserManagedCache.class */
public interface PersistentUserManagedCache<K, V> extends UserManagedCache<K, V> {
    void destroy() throws CachePersistenceException;
}
