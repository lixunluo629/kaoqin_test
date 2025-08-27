package org.ehcache.impl.internal.store.tiering;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.config.ResourceType;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.collections.ConcurrentWeakIdentityHashMap;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.core.spi.store.tiering.CachingTier;
import org.ehcache.core.spi.store.tiering.HigherCachingTier;
import org.ehcache.core.spi.store.tiering.LowerCachingTier;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.statistics.StatisticsManager;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/CompoundCachingTier.class */
public class CompoundCachingTier<K, V> implements CachingTier<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) CompoundCachingTier.class);
    private final HigherCachingTier<K, V> higher;
    private final LowerCachingTier<K, V> lower;
    private volatile CachingTier.InvalidationListener<K, V> invalidationListener;

    public CompoundCachingTier(HigherCachingTier<K, V> higher, final LowerCachingTier<K, V> lower) {
        this.higher = higher;
        this.lower = lower;
        this.higher.setInvalidationListener(new CachingTier.InvalidationListener<K, V>() { // from class: org.ehcache.impl.internal.store.tiering.CompoundCachingTier.1
            @Override // org.ehcache.core.spi.store.tiering.CachingTier.InvalidationListener
            public void onInvalidation(K key, final Store.ValueHolder<V> valueHolder) {
                try {
                    CompoundCachingTier.this.lower.installMapping(key, new Function<K, Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.tiering.CompoundCachingTier.1.1
                        @Override // org.ehcache.core.spi.function.Function
                        public /* bridge */ /* synthetic */ Object apply(Object x0) {
                            return apply((C00571) x0);
                        }

                        @Override // org.ehcache.core.spi.function.Function
                        public Store.ValueHolder<V> apply(K k) {
                            return valueHolder;
                        }
                    });
                } catch (StoreAccessException cae) {
                    CompoundCachingTier.this.notifyInvalidation(key, valueHolder);
                    CompoundCachingTier.LOGGER.warn("Error overflowing '{}' into lower caching tier {}", key, lower, cae);
                }
            }
        });
        StatisticsManager.associate(higher).withParent(this);
        StatisticsManager.associate(lower).withParent(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyInvalidation(K key, Store.ValueHolder<V> p) {
        CachingTier.InvalidationListener<K, V> invalidationListener = this.invalidationListener;
        if (invalidationListener != null) {
            invalidationListener.onInvalidation(key, p);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/CompoundCachingTier$ComputationException.class */
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

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public Store.ValueHolder<V> getOrComputeIfAbsent(K key, final Function<K, Store.ValueHolder<V>> source) throws StoreAccessException {
        try {
            return this.higher.getOrComputeIfAbsent(key, new Function<K, Store.ValueHolder<V>>() { // from class: org.ehcache.impl.internal.store.tiering.CompoundCachingTier.2
                @Override // org.ehcache.core.spi.function.Function
                public /* bridge */ /* synthetic */ Object apply(Object x0) {
                    return apply((AnonymousClass2) x0);
                }

                @Override // org.ehcache.core.spi.function.Function
                public Store.ValueHolder<V> apply(K k) {
                    try {
                        Store.ValueHolder<V> valueHolder = CompoundCachingTier.this.lower.getAndRemove(k);
                        if (valueHolder != null) {
                            return valueHolder;
                        }
                        return (Store.ValueHolder) source.apply(k);
                    } catch (StoreAccessException cae) {
                        throw new ComputationException(cae);
                    }
                }
            });
        } catch (ComputationException ce) {
            throw ce.getStoreAccessException();
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void invalidate(final K key) throws StoreAccessException {
        try {
            this.higher.silentInvalidate(key, new Function<Store.ValueHolder<V>, Void>() { // from class: org.ehcache.impl.internal.store.tiering.CompoundCachingTier.3
                /* JADX WARN: Multi-variable type inference failed */
                @Override // org.ehcache.core.spi.function.Function
                public Void apply(Store.ValueHolder<V> mappedValue) {
                    try {
                        if (mappedValue != null) {
                            CompoundCachingTier.this.notifyInvalidation(key, mappedValue);
                        } else {
                            CompoundCachingTier.this.lower.invalidate(key);
                        }
                        return null;
                    } catch (StoreAccessException cae) {
                        throw new ComputationException(cae);
                    }
                }
            });
        } catch (ComputationException ce) {
            throw ce.getStoreAccessException();
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void invalidateAll() throws StoreAccessException {
        try {
            this.higher.silentInvalidateAll(new BiFunction<K, Store.ValueHolder<V>, Void>() { // from class: org.ehcache.impl.internal.store.tiering.CompoundCachingTier.4
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Void apply(Object x0, Object x1) {
                    return apply((AnonymousClass4) x0, (Store.ValueHolder) x1);
                }

                public Void apply(K key, Store.ValueHolder<V> mappedValue) {
                    if (mappedValue != null) {
                        CompoundCachingTier.this.notifyInvalidation(key, mappedValue);
                        return null;
                    }
                    return null;
                }
            });
            this.lower.invalidateAll();
        } catch (Throwable th) {
            this.lower.invalidateAll();
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void invalidateAllWithHash(long hash) throws StoreAccessException {
        try {
            this.higher.silentInvalidateAllWithHash(hash, new BiFunction<K, Store.ValueHolder<V>, Void>() { // from class: org.ehcache.impl.internal.store.tiering.CompoundCachingTier.5
                @Override // org.ehcache.core.spi.function.BiFunction
                public /* bridge */ /* synthetic */ Void apply(Object x0, Object x1) {
                    return apply((AnonymousClass5) x0, (Store.ValueHolder) x1);
                }

                public Void apply(K key, Store.ValueHolder<V> mappedValue) {
                    if (mappedValue != null) {
                        CompoundCachingTier.this.notifyInvalidation(key, mappedValue);
                        return null;
                    }
                    return null;
                }
            });
            this.lower.invalidateAllWithHash(hash);
        } catch (Throwable th) {
            this.lower.invalidateAllWithHash(hash);
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void clear() throws StoreAccessException {
        try {
            this.higher.clear();
            this.lower.clear();
        } catch (Throwable th) {
            this.lower.clear();
            throw th;
        }
    }

    @Override // org.ehcache.core.spi.store.tiering.CachingTier
    public void setInvalidationListener(CachingTier.InvalidationListener<K, V> invalidationListener) {
        this.invalidationListener = invalidationListener;
        this.lower.setInvalidationListener(invalidationListener);
    }

    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        List<CacheConfigurationChangeListener> listeners = new ArrayList<>();
        listeners.addAll(this.higher.getConfigurationChangeListeners());
        listeners.addAll(this.lower.getConfigurationChangeListeners());
        return listeners;
    }

    @ServiceDependencies({HigherCachingTier.Provider.class, LowerCachingTier.Provider.class})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/tiering/CompoundCachingTier$Provider.class */
    public static class Provider implements CachingTier.Provider {
        private volatile ServiceProvider<Service> serviceProvider;
        private final ConcurrentMap<CachingTier<?, ?>, Map.Entry<HigherCachingTier.Provider, LowerCachingTier.Provider>> providersMap = new ConcurrentWeakIdentityHashMap();

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public <K, V> CachingTier<K, V> createCachingTier(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            if (this.serviceProvider == null) {
                throw new RuntimeException("ServiceProvider is null.");
            }
            Collection<U> servicesOfType = this.serviceProvider.getServicesOfType(HigherCachingTier.Provider.class);
            if (servicesOfType.size() != 1) {
                throw new IllegalStateException("Cannot handle multiple higher tier providers");
            }
            HigherCachingTier.Provider higherProvider = (HigherCachingTier.Provider) servicesOfType.iterator().next();
            HigherCachingTier<K, V> higherCachingTier = higherProvider.createHigherCachingTier(storeConfig, serviceConfigs);
            Collection<U> servicesOfType2 = this.serviceProvider.getServicesOfType(LowerCachingTier.Provider.class);
            if (servicesOfType2.size() != 1) {
                throw new IllegalStateException("Cannot handle multiple lower tier providers");
            }
            LowerCachingTier.Provider lowerProvider = (LowerCachingTier.Provider) servicesOfType2.iterator().next();
            LowerCachingTier<K, V> lowerCachingTier = lowerProvider.createCachingTier(storeConfig, serviceConfigs);
            CompoundCachingTier<K, V> compoundCachingTier = new CompoundCachingTier<>(higherCachingTier, lowerCachingTier);
            this.providersMap.put(compoundCachingTier, new AbstractMap.SimpleEntry(higherProvider, lowerProvider));
            return compoundCachingTier;
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public void releaseCachingTier(CachingTier<?, ?> resource) {
            if (!this.providersMap.containsKey(resource)) {
                throw new IllegalArgumentException("Given caching tier is not managed by this provider : " + resource);
            }
            CompoundCachingTier compoundCachingTier = (CompoundCachingTier) resource;
            Map.Entry<HigherCachingTier.Provider, LowerCachingTier.Provider> entry = this.providersMap.get(resource);
            entry.getKey().releaseHigherCachingTier(compoundCachingTier.higher);
            entry.getValue().releaseCachingTier(compoundCachingTier.lower);
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public void initCachingTier(CachingTier<?, ?> resource) {
            if (!this.providersMap.containsKey(resource)) {
                throw new IllegalArgumentException("Given caching tier is not managed by this provider : " + resource);
            }
            CompoundCachingTier compoundCachingTier = (CompoundCachingTier) resource;
            Map.Entry<HigherCachingTier.Provider, LowerCachingTier.Provider> entry = this.providersMap.get(resource);
            entry.getValue().initCachingTier(compoundCachingTier.lower);
            entry.getKey().initHigherCachingTier(compoundCachingTier.higher);
        }

        @Override // org.ehcache.core.spi.store.tiering.CachingTier.Provider
        public int rankCachingTier(Set<ResourceType<?>> resourceTypes, Collection<ServiceConfiguration<?>> serviceConfigs) {
            return resourceTypes.equals(Collections.unmodifiableSet(EnumSet.of(ResourceType.Core.HEAP, ResourceType.Core.OFFHEAP))) ? 2 : 0;
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
}
