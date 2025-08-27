package org.ehcache.impl.internal.store.offheap;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ehcache.config.Eviction;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.collections.ConcurrentWeakIdentityHashMap;
import org.ehcache.core.events.NullStoreEventDispatcher;
import org.ehcache.core.events.StoreEventDispatcher;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.StoreAccessException;
import org.ehcache.core.spi.store.tiering.AuthoritativeTier;
import org.ehcache.core.spi.store.tiering.LowerCachingTier;
import org.ehcache.core.spi.time.TimeSource;
import org.ehcache.core.spi.time.TimeSourceService;
import org.ehcache.core.statistics.AuthoritativeTierOperationOutcomes;
import org.ehcache.core.statistics.LowerCachingTierOperationsOutcome;
import org.ehcache.core.statistics.StoreOperationOutcomes;
import org.ehcache.core.statistics.TierOperationOutcomes;
import org.ehcache.impl.internal.events.ThreadLocalStoreEventDispatcher;
import org.ehcache.impl.internal.store.offheap.factories.EhcacheSegmentFactory;
import org.ehcache.impl.internal.store.offheap.portability.OffHeapValueHolderPortability;
import org.ehcache.impl.internal.store.offheap.portability.SerializerPortability;
import org.ehcache.impl.serialization.TransientStateRepository;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.StatefulSerializer;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.paging.UpfrontAllocatingPageSource;
import org.terracotta.offheapstore.pinning.PinnableSegment;
import org.terracotta.offheapstore.storage.OffHeapBufferStorageEngine;
import org.terracotta.offheapstore.storage.PointerSize;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.util.Factory;
import org.terracotta.statistics.MappedOperationStatistic;
import org.terracotta.statistics.StatisticsManager;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/OffHeapStore.class */
public class OffHeapStore<K, V> extends AbstractOffHeapStore<K, V> {
    private static final String STATISTICS_TAG = "OffHeap";
    private final SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor;
    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;
    private final long sizeInBytes;
    private volatile EhcacheConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> map;

    public OffHeapStore(Store.Configuration<K, V> config, TimeSource timeSource, StoreEventDispatcher<K, V> eventDispatcher, long sizeInBytes) {
        super(STATISTICS_TAG, config, timeSource, eventDispatcher);
        EvictionAdvisor<? super K, ? super V> evictionAdvisor = config.getEvictionAdvisor();
        if (evictionAdvisor != null) {
            this.evictionAdvisor = wrap(evictionAdvisor);
        } else {
            this.evictionAdvisor = wrap(Eviction.noAdvice());
        }
        this.keySerializer = config.getKeySerializer();
        this.valueSerializer = config.getValueSerializer();
        this.sizeInBytes = sizeInBytes;
    }

    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        return Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EhcacheConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> createBackingMap(long size, Serializer<K> keySerializer, Serializer<V> valueSerializer, SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor) {
        HeuristicConfiguration config = new HeuristicConfiguration(size);
        PageSource source = new UpfrontAllocatingPageSource(OffHeapStoreUtils.getBufferSource(), config.getMaximumSize(), config.getMaximumChunkSize(), config.getMinimumChunkSize());
        Portability<K> keyPortability = new SerializerPortability<>(keySerializer);
        Portability<OffHeapValueHolder<V>> elementPortability = new OffHeapValueHolderPortability<>(valueSerializer);
        Factory<OffHeapBufferStorageEngine<K, OffHeapValueHolder<V>>> storageEngineFactory = OffHeapBufferStorageEngine.createFactory(PointerSize.INT, source, config.getSegmentDataPageSize(), keyPortability, elementPortability, false, true);
        Factory<? extends PinnableSegment<K, OffHeapValueHolder<V>>> segmentFactory = new EhcacheSegmentFactory<>(source, storageEngineFactory, config.getInitialSegmentTableSize(), evictionAdvisor, this.mapEvictionListener);
        return new EhcacheConcurrentOffHeapClockCache<>(evictionAdvisor, segmentFactory, config.getConcurrency());
    }

    @Override // org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore
    protected EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>> backingMap() {
        return this.map;
    }

    @Override // org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore
    protected SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor() {
        return this.evictionAdvisor;
    }

    @ServiceDependencies({TimeSourceService.class, SerializationProvider.class})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/OffHeapStore$Provider.class */
    public static class Provider implements Store.Provider, AuthoritativeTier.Provider, LowerCachingTier.Provider {
        private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) Provider.class);
        private volatile ServiceProvider<Service> serviceProvider;
        private final Set<Store<?, ?>> createdStores = Collections.newSetFromMap(new ConcurrentWeakIdentityHashMap());
        private final Map<OffHeapStore<?, ?>, Collection<MappedOperationStatistic<?, ?>>> tierOperationStatistics = new ConcurrentWeakIdentityHashMap();

        @Override // org.ehcache.core.spi.store.Store.Provider
        public /* bridge */ /* synthetic */ Store createStore(Store.Configuration x0, ServiceConfiguration[] x1) {
            return createStore(x0, (ServiceConfiguration<?>[]) x1);
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public int rank(Set<ResourceType<?>> resourceTypes, Collection<ServiceConfiguration<?>> serviceConfigs) {
            return resourceTypes.equals(Collections.singleton(ResourceType.Core.OFFHEAP)) ? 1 : 0;
        }

        @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.Provider
        public int rankAuthority(ResourceType<?> authorityResource, Collection<ServiceConfiguration<?>> serviceConfigs) {
            return authorityResource.equals(ResourceType.Core.OFFHEAP) ? 1 : 0;
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public <K, V> OffHeapStore<K, V> createStore(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OffHeapStore<K, V> store = createStoreInternal(storeConfig, new ThreadLocalStoreEventDispatcher(storeConfig.getDispatcherConcurrency()), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<StoreOperationOutcomes.GetOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(store, TierOperationOutcomes.GET_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.OFFHEAP.getTierHeight(), BeanUtil.PREFIX_GETTER_GET, OffHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(store);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(store, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.OFFHEAP.getTierHeight(), "eviction", OffHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(evict).withParent(store);
            tieredOps.add(evict);
            this.tierOperationStatistics.put(store, tieredOps);
            return store;
        }

        private <K, V> OffHeapStore<K, V> createStoreInternal(Store.Configuration<K, V> storeConfig, StoreEventDispatcher<K, V> eventDispatcher, ServiceConfiguration<?>... serviceConfigs) {
            if (this.serviceProvider == null) {
                throw new NullPointerException("ServiceProvider is null in OffHeapStore.Provider.");
            }
            TimeSource timeSource = ((TimeSourceService) this.serviceProvider.getService(TimeSourceService.class)).getTimeSource();
            SizedResourcePool offHeapPool = (SizedResourcePool) storeConfig.getResourcePools().getPoolForResource(ResourceType.Core.OFFHEAP);
            if (!(offHeapPool.getUnit() instanceof MemoryUnit)) {
                throw new IllegalArgumentException("OffHeapStore only supports resources with memory unit");
            }
            MemoryUnit unit = (MemoryUnit) offHeapPool.getUnit();
            OffHeapStore<K, V> offHeapStore = new OffHeapStore<>(storeConfig, timeSource, eventDispatcher, unit.toBytes(offHeapPool.getSize()));
            this.createdStores.add(offHeapStore);
            return offHeapStore;
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void releaseStore(Store<?, ?> resource) {
            if (!this.createdStores.contains(resource)) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
            OffHeapStore offHeapStore = (OffHeapStore) resource;
            close(offHeapStore);
            StatisticsManager.nodeFor(offHeapStore).clean();
            this.tierOperationStatistics.remove(offHeapStore);
        }

        static void close(OffHeapStore<?, ?> resource) {
            EhcacheConcurrentOffHeapClockCache<?, ?> localMap = ((OffHeapStore) resource).map;
            if (localMap != null) {
                ((OffHeapStore) resource).map = null;
                localMap.destroy();
            }
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void initStore(Store<?, ?> resource) {
            if (!this.createdStores.contains(resource)) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
            OffHeapStore<?, ?> offHeapStore = (OffHeapStore) resource;
            Serializer keySerializer = ((OffHeapStore) offHeapStore).keySerializer;
            if (keySerializer instanceof StatefulSerializer) {
                ((StatefulSerializer) keySerializer).init(new TransientStateRepository());
            }
            Serializer valueSerializer = ((OffHeapStore) offHeapStore).valueSerializer;
            if (valueSerializer instanceof StatefulSerializer) {
                ((StatefulSerializer) valueSerializer).init(new TransientStateRepository());
            }
            init(offHeapStore);
        }

        static <K, V> void init(OffHeapStore<K, V> resource) {
            ((OffHeapStore) resource).map = resource.createBackingMap(((OffHeapStore) resource).sizeInBytes, ((OffHeapStore) resource).keySerializer, ((OffHeapStore) resource).valueSerializer, ((OffHeapStore) resource).evictionAdvisor);
        }

        @Override // org.ehcache.spi.service.Service
        public void start(ServiceProvider<Service> serviceProvider) {
            this.serviceProvider = serviceProvider;
        }

        @Override // org.ehcache.spi.service.Service
        public void stop() {
            this.serviceProvider = null;
            this.createdStores.clear();
        }

        @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.Provider
        public <K, V> AuthoritativeTier<K, V> createAuthoritativeTier(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OffHeapStore<K, V> authoritativeTier = createStoreInternal(storeConfig, new ThreadLocalStoreEventDispatcher(storeConfig.getDispatcherConcurrency()), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<AuthoritativeTierOperationOutcomes.GetAndFaultOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(authoritativeTier, TierOperationOutcomes.GET_AND_FAULT_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.OFFHEAP.getTierHeight(), "getAndFault", OffHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(authoritativeTier);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(authoritativeTier, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.OFFHEAP.getTierHeight(), "eviction", OffHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(evict).withParent(authoritativeTier);
            tieredOps.add(evict);
            this.tierOperationStatistics.put(authoritativeTier, tieredOps);
            return authoritativeTier;
        }

        @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.Provider
        public void releaseAuthoritativeTier(AuthoritativeTier<?, ?> resource) {
            releaseStore(resource);
        }

        @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.Provider
        public void initAuthoritativeTier(AuthoritativeTier<?, ?> resource) {
            initStore(resource);
        }

        @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier.Provider
        public <K, V> LowerCachingTier<K, V> createCachingTier(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OffHeapStore<K, V> lowerCachingTier = createStoreInternal(storeConfig, NullStoreEventDispatcher.nullStoreEventDispatcher(), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<LowerCachingTierOperationsOutcome.GetAndRemoveOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(lowerCachingTier, TierOperationOutcomes.GET_AND_REMOVE_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.OFFHEAP.getTierHeight(), "getAndRemove", OffHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(lowerCachingTier);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(lowerCachingTier, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.OFFHEAP.getTierHeight(), "eviction", OffHeapStore.STATISTICS_TAG);
            StatisticsManager.associate(evict).withParent(lowerCachingTier);
            tieredOps.add(evict);
            this.tierOperationStatistics.put(lowerCachingTier, tieredOps);
            return lowerCachingTier;
        }

        @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier.Provider
        public void releaseCachingTier(LowerCachingTier<?, ?> resource) {
            if (!this.createdStores.contains(resource)) {
                throw new IllegalArgumentException("Given caching tier is not managed by this provider : " + resource);
            }
            flushToLowerTier((OffHeapStore) resource);
            releaseStore((Store) resource);
        }

        private void flushToLowerTier(OffHeapStore<Object, ?> resource) {
            StoreAccessException lastFailure = null;
            int failureCount = 0;
            Set<Object> keys = resource.backingMap().keySet();
            for (Object key : keys) {
                try {
                    resource.invalidate(key);
                } catch (StoreAccessException cae) {
                    lastFailure = cae;
                    failureCount++;
                    LOGGER.warn("Error flushing '{}' to lower tier", key, cae);
                }
            }
            if (lastFailure != null) {
                throw new RuntimeException("Failed to flush some mappings to lower tier, " + failureCount + " could not be flushed. This error represents the last failure.", lastFailure);
            }
        }

        @Override // org.ehcache.core.spi.store.tiering.LowerCachingTier.Provider
        public void initCachingTier(LowerCachingTier<?, ?> resource) {
            if (!this.createdStores.contains(resource)) {
                throw new IllegalArgumentException("Given caching tier is not managed by this provider : " + resource);
            }
            init((OffHeapStore) resource);
        }
    }
}
