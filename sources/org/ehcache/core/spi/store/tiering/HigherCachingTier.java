package org.ehcache.core.spi.store.tiering;

import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.spi.service.PluralService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/HigherCachingTier.class */
public interface HigherCachingTier<K, V> extends CachingTier<K, V> {

    @PluralService
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/HigherCachingTier$Provider.class */
    public interface Provider extends Service {
        <K, V> HigherCachingTier<K, V> createHigherCachingTier(Store.Configuration<K, V> configuration, ServiceConfiguration<?>... serviceConfigurationArr);

        void releaseHigherCachingTier(HigherCachingTier<?, ?> higherCachingTier);

        void initHigherCachingTier(HigherCachingTier<?, ?> higherCachingTier);
    }

    void silentInvalidate(K k, Function<Store.ValueHolder<V>, Void> function) throws StoreAccessException;

    void silentInvalidateAll(BiFunction<K, Store.ValueHolder<V>, Void> biFunction) throws StoreAccessException;

    void silentInvalidateAllWithHash(long j, BiFunction<K, Store.ValueHolder<V>, Void> biFunction) throws StoreAccessException;
}
