package org.ehcache.impl.internal.store.tiering;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.ehcache.Cache;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.collections.ConcurrentWeakIdentityHashMap;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.function.NullaryFunction;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.core.spi.store.events.StoreEventSource;
import org.ehcache.core.spi.store.tiering.AuthoritativeTier;
import org.ehcache.core.spi.store.tiering.CachingTier;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.statistics.StatisticsManager;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/TieredStore.class */
public class TieredStore<K, V> implements Store<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) TieredStore.class);
    private final AtomicReference<CachingTier<K, V>> cachingTierRef;
    private final CachingTier<K, V> noopCachingTier;
    private final CachingTier<K, V> realCachingTier;
    private final AuthoritativeTier<K, V> authoritativeTier;

    public TieredStore(CachingTier<K, V> cachingTier, AuthoritativeTier<K, V> authoritativeTier) {
        this.cachingTierRef = new AtomicReference<>(cachingTier);
        this.authoritativeTier = authoritativeTier;
        this.realCachingTier = cachingTier;
        this.noopCachingTier = new NoopCachingTier(authoritativeTier);
        this.realCachingTier.setInvalidationListener(new CachingTier.InvalidationListener<K, V>() { // from class: org.ehcache.impl.internal.store.tiering.TieredStore.1
            @Override // org.ehcache.core.spi.store.tiering.CachingTier.InvalidationListener
            public void onInvalidation(K key, Store.ValueHolder<V> valueHolder) {
                TieredStore.this.authoritativeTier.flush(key, valueHolder);
            }
        });
        this.authoritativeTier.setInvalidationValve(new AuthoritativeTier.InvalidationValve() { // from class: org.ehcache.impl.internal.store.tiering.TieredStore.2
            @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.InvalidationValve
            public void invalidateAll() throws StoreAccessException {
                TieredStore.this.invalidateAllInternal();
            }

            @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.InvalidationValve
            public void invalidateAllWithHash(long hash) throws StoreAccessException {
                TieredStore.this.cachingTier().invalidateAllWithHash(hash);
            }
        });
        StatisticsManager.associate(cachingTier).withParent(this);
        StatisticsManager.associate(authoritativeTier).withParent(this);
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> get(K key) throws StoreAccessException {
        try {
            return cachingTier().getOrComputeIfAbsent(key, new Function<K, Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.tiering.TieredStore.3
                @Override // org.ehcache.core.spi.function.Function
                public /* bridge */ /* synthetic */ Object apply(Object x0) {
                    return apply((AnonymousClass3) x0);
                }

                @Override // org.ehcache.core.spi.function.Function
                public Store.ValueHolder<V> apply(K key2) {
                    try {
                        return TieredStore.this.authoritativeTier.getAndFault(key2);
                    } catch (StoreAccessException cae) {
                        throw new ComputationException(cae);
                    }
                }
            });
        } catch (ComputationException ce) {
            throw ce.getStoreAccessException();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/TieredStore$ComputationException.class */
    static class ComputationException extends RuntimeException {
        public ComputationException(StoreAccessException cause) {
            super(cause);
        }

        public StoreAccessException getStoreAccessException() {
            return (StoreAccessException) getCause();
        }

        @Override // java.lang.Throwable
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean containsKey(K key) throws StoreAccessException {
        return this.authoritativeTier.containsKey(key);
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.PutStatus put(K key, V value) throws StoreAccessException {
        try {
            Store.PutStatus putStatusPut = this.authoritativeTier.put(key, value);
            cachingTier().invalidate(key);
            return putStatusPut;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> putIfAbsent(K key, V value) throws StoreAccessException {
        try {
            Store.ValueHolder<V> valueHolderPutIfAbsent = this.authoritativeTier.putIfAbsent(key, value);
            cachingTier().invalidate(key);
            return valueHolderPutIfAbsent;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public boolean remove(K key) throws StoreAccessException {
        try {
            boolean zRemove = this.authoritativeTier.remove(key);
            cachingTier().invalidate(key);
            return zRemove;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.RemoveStatus remove(K key, V value) throws StoreAccessException {
        try {
            Store.RemoveStatus removeStatusRemove = this.authoritativeTier.remove(key, value);
            cachingTier().invalidate(key);
            return removeStatusRemove;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> replace(K key, V value) throws StoreAccessException {
        try {
            Store.ValueHolder<V> valueHolderReplace = this.authoritativeTier.replace(key, value);
            cachingTier().invalidate(key);
            return valueHolderReplace;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ReplaceStatus replace(K key, V oldValue, V newValue) throws StoreAccessException {
        try {
            Store.ReplaceStatus replaceStatusReplace = this.authoritativeTier.replace(key, oldValue, newValue);
            cachingTier().invalidate(key);
            return replaceStatusReplace;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public void clear() throws StoreAccessException {
        swapCachingTiers();
        try {
            this.authoritativeTier.clear();
            try {
                this.realCachingTier.clear();
                swapBackCachingTiers();
            } finally {
            }
        } catch (Throwable th) {
            try {
                this.realCachingTier.clear();
                swapBackCachingTiers();
                throw th;
            } finally {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateAllInternal() throws StoreAccessException {
        swapCachingTiers();
        try {
            this.realCachingTier.invalidateAll();
            swapBackCachingTiers();
        } catch (Throwable th) {
            swapBackCachingTiers();
            throw th;
        }
    }

    private void swapCachingTiers() {
        boolean interrupted = false;
        while (!this.cachingTierRef.compareAndSet(this.realCachingTier, this.noopCachingTier)) {
            synchronized (this.noopCachingTier) {
                if (this.cachingTierRef.get() == this.noopCachingTier) {
                    try {
                        this.noopCachingTier.wait();
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    private void swapBackCachingTiers() {
        if (!this.cachingTierRef.compareAndSet(this.noopCachingTier, this.realCachingTier)) {
            throw new AssertionError("Something bad happened");
        }
        synchronized (this.noopCachingTier) {
            this.noopCachingTier.notify();
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public StoreEventSource<K, V> getStoreEventSource() {
        return this.authoritativeTier.getStoreEventSource();
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.Iterator<Cache.Entry<K, Store.ValueHolder<V>>> iterator() {
        return this.authoritativeTier.iterator();
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(K key, BiFunction<? super K, ? super V, ? extends V> mappingFunction) throws StoreAccessException {
        try {
            Store.ValueHolder<V> valueHolderCompute = this.authoritativeTier.compute(key, mappingFunction);
            cachingTier().invalidate(key);
            return valueHolderCompute;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> compute(K key, BiFunction<? super K, ? super V, ? extends V> mappingFunction, NullaryFunction<Boolean> replaceEqual) throws StoreAccessException {
        try {
            Store.ValueHolder<V> valueHolderCompute = this.authoritativeTier.compute(key, mappingFunction, replaceEqual);
            cachingTier().invalidate(key);
            return valueHolderCompute;
        } catch (Throwable th) {
            cachingTier().invalidate(key);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Store.ValueHolder<V> computeIfAbsent(K key, final Function<? super K, ? extends V> mappingFunction) throws StoreAccessException {
        try {
            return cachingTier().getOrComputeIfAbsent(key, new Function<K, Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.tiering.TieredStore.4
                @Override // org.ehcache.core.spi.function.Function
                public /* bridge */ /* synthetic */ Object apply(Object x0) {
                    return apply((AnonymousClass4) x0);
                }

                @Override // org.ehcache.core.spi.function.Function
                public Store.ValueHolder<V> apply(K k) {
                    try {
                        return TieredStore.this.authoritativeTier.computeIfAbsentAndFault(k, mappingFunction);
                    } catch (StoreAccessException cae) {
                        throw new ComputationException(cae);
                    }
                }
            });
        } catch (ComputationException ce) {
            throw ce.getStoreAccessException();
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkCompute(Set<? extends K> keys, Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> remappingFunction) throws StoreAccessException {
        try {
            Map<K, Store.ValueHolder<V>> mapBulkCompute = this.authoritativeTier.bulkCompute(keys, remappingFunction);
            for (K key : keys) {
                cachingTier().invalidate(key);
            }
            return mapBulkCompute;
        } catch (Throwable th) {
            for (K key2 : keys) {
                cachingTier().invalidate(key2);
            }
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkCompute(Set<? extends K> keys, Function<Iterable<? extends Map.Entry<? extends K, ? extends V>>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> remappingFunction, NullaryFunction<Boolean> replaceEqual) throws StoreAccessException {
        try {
            Map<K, Store.ValueHolder<V>> mapBulkCompute = this.authoritativeTier.bulkCompute(keys, remappingFunction, replaceEqual);
            for (K key : keys) {
                cachingTier().invalidate(key);
            }
            return mapBulkCompute;
        } catch (Throwable th) {
            for (K key2 : keys) {
                cachingTier().invalidate(key2);
            }
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.Store
    public Map<K, Store.ValueHolder<V>> bulkComputeIfAbsent(Set<? extends K> keys, Function<Iterable<? extends K>, Iterable<? extends Map.Entry<? extends K, ? extends V>>> mappingFunction) throws StoreAccessException {
        try {
            Map<K, Store.ValueHolder<V>> mapBulkComputeIfAbsent = this.authoritativeTier.bulkComputeIfAbsent(keys, mappingFunction);
            for (K key : keys) {
                cachingTier().invalidate(key);
            }
            return mapBulkComputeIfAbsent;
        } catch (Throwable th) {
            for (K key2 : keys) {
                cachingTier().invalidate(key2);
            }
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        List<CacheConfigurationChangeListener> configurationChangeListenerList = new ArrayList<>();
        configurationChangeListenerList.addAll(this.realCachingTier.getConfigurationChangeListeners());
        configurationChangeListenerList.addAll(this.authoritativeTier.getConfigurationChangeListeners());
        return configurationChangeListenerList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CachingTier<K, V> cachingTier() {
        return this.cachingTierRef.get();
    }

    @ServiceDependencies({CachingTier.Provider.class, AuthoritativeTier.Provider.class})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/TieredStore$Provider.class */
    public static class Provider implements Store.Provider {
        private volatile ServiceProvider<Service> serviceProvider;
        private final ConcurrentMap<Store<?, ?>, Map.Entry<CachingTier.Provider, AuthoritativeTier.Provider>> providersMap = new ConcurrentWeakIdentityHashMap();

        @Override // org.ehcache.core.spi.store.Store.Provider
        public int rank(Set<ResourceType<?>> resourceTypes, Collection<ServiceConfiguration<?>> serviceConfigs) {
            if (resourceTypes.size() == 1) {
                return 0;
            }
            ResourceType<?> authorityResource = getAuthorityResource(resourceTypes);
            int authorityRank = 0;
            for (U authorityProvider : this.serviceProvider.getServicesOfType(AuthoritativeTier.Provider.class)) {
                int newRank = authorityProvider.rankAuthority(authorityResource, serviceConfigs);
                if (newRank > authorityRank) {
                    authorityRank = newRank;
                }
            }
            if (authorityRank == 0) {
                return 0;
            }
            Set<ResourceType<?>> cachingResources = new HashSet<>();
            cachingResources.addAll(resourceTypes);
            cachingResources.remove(authorityResource);
            int cachingTierRank = 0;
            for (U cachingTierProvider : this.serviceProvider.getServicesOfType(CachingTier.Provider.class)) {
                int newRank2 = cachingTierProvider.rankCachingTier(cachingResources, serviceConfigs);
                if (newRank2 > cachingTierRank) {
                    cachingTierRank = newRank2;
                }
            }
            if (cachingTierRank == 0) {
                return 0;
            }
            return authorityRank + cachingTierRank;
        }

        private ResourceType<?> getAuthorityResource(Set<ResourceType<?>> resourceTypes) {
            ResourceType<?> authorityResource = null;
            for (ResourceType<?> resourceType : resourceTypes) {
                if (authorityResource == null || authorityResource.getTierHeight() > resourceType.getTierHeight()) {
                    authorityResource = resourceType;
                }
            }
            return authorityResource;
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public <K, V> Store<K, V> createStore(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            List<ServiceConfiguration<?>> enhancedServiceConfigs = new ArrayList<>(Arrays.asList(serviceConfigs));
            ResourcePools resourcePools = storeConfig.getResourcePools();
            if (rank(resourcePools.getResourceTypeSet(), enhancedServiceConfigs) == 0) {
                throw new IllegalArgumentException("TieredStore.Provider does not support configured resource types " + resourcePools.getResourceTypeSet());
            }
            ResourceType<?> authorityResource = getAuthorityResource(resourcePools.getResourceTypeSet());
            AuthoritativeTier.Provider authoritativeTierProvider = getAuthoritativeTierProvider(authorityResource, enhancedServiceConfigs);
            Set<ResourceType<?>> cachingResources = new HashSet<>();
            cachingResources.addAll(resourcePools.getResourceTypeSet());
            cachingResources.remove(authorityResource);
            CachingTier.Provider cachingTierProvider = getCachingTierProvider(cachingResources, enhancedServiceConfigs);
            ServiceConfiguration<?>[] configurations = (ServiceConfiguration[]) enhancedServiceConfigs.toArray(new ServiceConfiguration[enhancedServiceConfigs.size()]);
            CachingTier<K, V> cachingTier = cachingTierProvider.createCachingTier(storeConfig, configurations);
            AuthoritativeTier<K, V> authoritativeTier = authoritativeTierProvider.createAuthoritativeTier(storeConfig, configurations);
            TieredStore<K, V> store = new TieredStore<>(cachingTier, authoritativeTier);
            registerStore(store, cachingTierProvider, authoritativeTierProvider);
            return store;
        }

        private CachingTier.Provider getCachingTierProvider(Set<ResourceType<?>> cachingResources, List<ServiceConfiguration<?>> enhancedServiceConfigs) {
            CachingTier.Provider cachingTierProvider = null;
            Iterator i$ = this.serviceProvider.getServicesOfType(CachingTier.Provider.class).iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                CachingTier.Provider provider = (CachingTier.Provider) i$.next();
                if (provider.rankCachingTier(cachingResources, enhancedServiceConfigs) != 0) {
                    cachingTierProvider = provider;
                    break;
                }
            }
            if (cachingTierProvider == null) {
                throw new AssertionError("No CachingTier.Provider found although ranking found one for " + cachingResources);
            }
            return cachingTierProvider;
        }

        private AuthoritativeTier.Provider getAuthoritativeTierProvider(ResourceType<?> authorityResource, List<ServiceConfiguration<?>> enhancedServiceConfigs) {
            AuthoritativeTier.Provider authoritativeTierProvider = null;
            Iterator i$ = this.serviceProvider.getServicesOfType(AuthoritativeTier.Provider.class).iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                AuthoritativeTier.Provider provider = (AuthoritativeTier.Provider) i$.next();
                if (provider.rankAuthority(authorityResource, enhancedServiceConfigs) != 0) {
                    authoritativeTierProvider = provider;
                    break;
                }
            }
            if (authoritativeTierProvider == null) {
                throw new AssertionError("No AuthoritativeTier.Provider found although ranking found one for " + authorityResource);
            }
            return authoritativeTierProvider;
        }

        <K, V> void registerStore(TieredStore<K, V> store, CachingTier.Provider cachingTierProvider, AuthoritativeTier.Provider authoritativeTierProvider) {
            if (this.providersMap.putIfAbsent(store, new AbstractMap.SimpleEntry(cachingTierProvider, authoritativeTierProvider)) != null) {
                throw new IllegalStateException("Instance of the Store already registered!");
            }
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void releaseStore(Store<?, ?> resource) {
            Map.Entry<CachingTier.Provider, AuthoritativeTier.Provider> entry = this.providersMap.get(resource);
            if (entry == null) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
            TieredStore tieredStore = (TieredStore) resource;
            tieredStore.authoritativeTier.setInvalidationValve(new AuthoritativeTier.InvalidationValve() { // from class: org.ehcache.impl.internal.store.tiering.TieredStore.Provider.1
                @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.InvalidationValve
                public void invalidateAll() throws StoreAccessException {
                }

                @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.InvalidationValve
                public void invalidateAllWithHash(long hash) throws StoreAccessException {
                }
            });
            entry.getKey().releaseCachingTier(tieredStore.realCachingTier);
            entry.getValue().releaseAuthoritativeTier(tieredStore.authoritativeTier);
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void initStore(Store<?, ?> resource) {
            Map.Entry<CachingTier.Provider, AuthoritativeTier.Provider> entry = this.providersMap.get(resource);
            if (entry == null) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
            TieredStore tieredStore = (TieredStore) resource;
            entry.getKey().initCachingTier(tieredStore.realCachingTier);
            entry.getValue().initAuthoritativeTier(tieredStore.authoritativeTier);
        }

        @Override // org.ehcache.spi.service.Service
        public void start(ServiceProvider<Service> serviceProvider) {
            this.serviceProvider = serviceProvider;
        }

        @Override // org.ehcache.spi.service.Service
        public void stop() {
            this.serviceProvider = null;
            this.providersMap.clear();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/TieredStore$NoopCachingTier.class */
    private static class NoopCachingTier<K, V> implements CachingTier<K, V> {
        private final AuthoritativeTier<K, V> authoritativeTier;

        public NoopCachingTier(AuthoritativeTier<K, V> authoritativeTier) {
            this.authoritativeTier = authoritativeTier;
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier
        public Store.ValueHolder<V> getOrComputeIfAbsent(K key, Function<K, Store.ValueHolder<V>> source) throws StoreAccessException {
            Store.ValueHolder<V> apply = source.apply(key);
            this.authoritativeTier.flush(key, apply);
            return apply;
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier
        public void invalidate(K key) throws StoreAccessException {
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier
        public void invalidateAll() {
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier
        public void clear() throws StoreAccessException {
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier
        public void setInvalidationListener(CachingTier.InvalidationListener<K, V> invalidationListener) {
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier
        public void invalidateAllWithHash(long hash) throws StoreAccessException {
        }

        @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
        public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
            return null;
        }
    }
}
