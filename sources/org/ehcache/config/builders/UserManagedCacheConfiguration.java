package org.ehcache.config.builders;

import org.ehcache.UserManagedCache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/UserManagedCacheConfiguration.class */
public interface UserManagedCacheConfiguration<K, V, T extends UserManagedCache<K, V>> {
    UserManagedCacheBuilder<K, V, T> builder(UserManagedCacheBuilder<K, V, ? extends UserManagedCache<K, V>> userManagedCacheBuilder);
}
