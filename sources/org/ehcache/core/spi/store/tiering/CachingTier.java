package org.ehcache.core.spi.store.tiering;

import java.util.Collection;
import java.util.Set;
import org.ehcache.config.ResourceType;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.store.ConfigurationChangeSupport;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.spi.service.PluralService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/CachingTier.class */
public interface CachingTier<K, V> extends ConfigurationChangeSupport {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/CachingTier$InvalidationListener.class */
    public interface InvalidationListener<K, V> {
        void onInvalidation(K k, Store.ValueHolder<V> valueHolder);
    }

    @PluralService
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/CachingTier$Provider.class */
    public interface Provider extends Service {
        <K, V> CachingTier<K, V> createCachingTier(Store.Configuration<K, V> configuration, ServiceConfiguration<?>... serviceConfigurationArr);

        void releaseCachingTier(CachingTier<?, ?> cachingTier);

        void initCachingTier(CachingTier<?, ?> cachingTier);

        int rankCachingTier(Set<ResourceType<?>> set, Collection<ServiceConfiguration<?>> collection);
    }

    Store.ValueHolder<V> getOrComputeIfAbsent(K k, Function<K, Store.ValueHolder<V>> function) throws StoreAccessException;

    void invalidate(K k) throws StoreAccessException;

    void invalidateAll() throws StoreAccessException;

    void invalidateAllWithHash(long j) throws StoreAccessException;

    void clear() throws StoreAccessException;

    void setInvalidationListener(InvalidationListener<K, V> invalidationListener);
}
