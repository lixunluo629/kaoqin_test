package org.ehcache.core.spi.store.tiering;

import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.store.ConfigurationChangeSupport;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.core.spi.store.tiering.CachingTier;
import org.ehcache.spi.service.PluralService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/LowerCachingTier.class */
public interface LowerCachingTier<K, V> extends ConfigurationChangeSupport {

    @PluralService
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/LowerCachingTier$Provider.class */
    public interface Provider extends Service {
        <K, V> LowerCachingTier<K, V> createCachingTier(Store.Configuration<K, V> configuration, ServiceConfiguration<?>... serviceConfigurationArr);

        void releaseCachingTier(LowerCachingTier<?, ?> lowerCachingTier);

        void initCachingTier(LowerCachingTier<?, ?> lowerCachingTier);
    }

    Store.ValueHolder<V> installMapping(K k, Function<K, Store.ValueHolder<V>> function) throws StoreAccessException;

    Store.ValueHolder<V> getAndRemove(K k) throws StoreAccessException;

    void invalidate(K k) throws StoreAccessException;

    void invalidateAll() throws StoreAccessException;

    void invalidateAllWithHash(long j) throws StoreAccessException;

    void clear() throws StoreAccessException;

    void setInvalidationListener(CachingTier.InvalidationListener<K, V> invalidationListener);
}
