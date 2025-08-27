package org.ehcache.core.spi.store.tiering;

import java.util.Collection;
import org.ehcache.config.ResourceType;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.spi.service.PluralService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/AuthoritativeTier.class */
public interface AuthoritativeTier<K, V> extends Store<K, V> {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/AuthoritativeTier$InvalidationValve.class */
    public interface InvalidationValve {
        void invalidateAll() throws StoreAccessException;

        void invalidateAllWithHash(long j) throws StoreAccessException;
    }

    @PluralService
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/tiering/AuthoritativeTier$Provider.class */
    public interface Provider extends Service {
        <K, V> AuthoritativeTier<K, V> createAuthoritativeTier(Store.Configuration<K, V> configuration, ServiceConfiguration<?>... serviceConfigurationArr);

        void releaseAuthoritativeTier(AuthoritativeTier<?, ?> authoritativeTier);

        void initAuthoritativeTier(AuthoritativeTier<?, ?> authoritativeTier);

        int rankAuthority(ResourceType<?> resourceType, Collection<ServiceConfiguration<?>> collection);
    }

    Store.ValueHolder<V> getAndFault(K k) throws StoreAccessException;

    Store.ValueHolder<V> computeIfAbsentAndFault(K k, Function<? super K, ? extends V> function) throws StoreAccessException;

    boolean flush(K k, Store.ValueHolder<V> valueHolder);

    void setInvalidationValve(InvalidationValve invalidationValve);
}
