package org.ehcache.impl.config.persistence;

import org.ehcache.PersistentUserManagedCache;
import org.ehcache.UserManagedCache;
import org.ehcache.config.builders.UserManagedCacheBuilder;
import org.ehcache.config.builders.UserManagedCacheConfiguration;
import org.ehcache.core.spi.service.LocalPersistenceService;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/persistence/UserManagedPersistenceContext.class */
public class UserManagedPersistenceContext<K, V> implements UserManagedCacheConfiguration<K, V, PersistentUserManagedCache<K, V>> {
    private final String identifier;
    private final LocalPersistenceService persistenceService;

    public UserManagedPersistenceContext(String identifier, LocalPersistenceService persistenceService) {
        this.identifier = identifier;
        this.persistenceService = persistenceService;
    }

    @Override // org.ehcache.config.builders.UserManagedCacheConfiguration
    public UserManagedCacheBuilder<K, V, PersistentUserManagedCache<K, V>> builder(UserManagedCacheBuilder<K, V, ? extends UserManagedCache<K, V>> builder) {
        return builder.identifier(this.identifier).using(this.persistenceService);
    }
}
