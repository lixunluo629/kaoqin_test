package org.ehcache.impl.internal.store.disk;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.ehcache.CachePersistenceException;
import org.ehcache.Status;
import org.ehcache.config.Eviction;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.core.CacheConfigurationChangeListener;
import org.ehcache.core.collections.ConcurrentWeakIdentityHashMap;
import org.ehcache.core.events.StoreEventDispatcher;
import org.ehcache.core.spi.service.DiskResourceService;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.core.spi.service.FileBasedPersistenceContext;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.tiering.AuthoritativeTier;
import org.ehcache.core.spi.time.TimeSource;
import org.ehcache.core.spi.time.TimeSourceService;
import org.ehcache.core.statistics.AuthoritativeTierOperationOutcomes;
import org.ehcache.core.statistics.StoreOperationOutcomes;
import org.ehcache.core.statistics.TierOperationOutcomes;
import org.ehcache.impl.config.store.disk.OffHeapDiskStoreConfiguration;
import org.ehcache.impl.internal.events.ThreadLocalStoreEventDispatcher;
import org.ehcache.impl.internal.store.disk.factories.EhcachePersistentSegmentFactory;
import org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore;
import org.ehcache.impl.internal.store.offheap.EhcacheOffHeapBackingMap;
import org.ehcache.impl.internal.store.offheap.OffHeapValueHolder;
import org.ehcache.impl.internal.store.offheap.SwitchableEvictionAdvisor;
import org.ehcache.impl.internal.store.offheap.portability.OffHeapValueHolderPortability;
import org.ehcache.impl.internal.store.offheap.portability.SerializerPortability;
import org.ehcache.spi.persistence.PersistableResourceService;
import org.ehcache.spi.persistence.StateRepository;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.StatefulSerializer;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceDependencies;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.disk.paging.MappedPageSource;
import org.terracotta.offheapstore.disk.persistent.Persistent;
import org.terracotta.offheapstore.disk.persistent.PersistentPortability;
import org.terracotta.offheapstore.disk.storage.FileBackedStorageEngine;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.util.Factory;
import org.terracotta.offheapstore.util.MemoryUnit;
import org.terracotta.statistics.MappedOperationStatistic;
import org.terracotta.statistics.StatisticsManager;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/disk/OffHeapDiskStore.class */
public class OffHeapDiskStore<K, V> extends AbstractOffHeapStore<K, V> implements AuthoritativeTier<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) OffHeapDiskStore.class);
    private static final String STATISTICS_TAG = "Disk";
    private static final String KEY_TYPE_PROPERTY_NAME = "keyType";
    private static final String VALUE_TYPE_PROPERTY_NAME = "valueType";
    private static final int DEFAULT_CONCURRENCY = 16;
    protected final AtomicReference<Status> status;
    private final SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor;
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final ClassLoader classLoader;
    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;
    private final long sizeInBytes;
    private final FileBasedPersistenceContext fileBasedPersistenceContext;
    private final ExecutionService executionService;
    private final String threadPoolAlias;
    private final int writerConcurrency;
    private volatile EhcachePersistentConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> map;

    public OffHeapDiskStore(FileBasedPersistenceContext fileBasedPersistenceContext, ExecutionService executionService, String threadPoolAlias, int writerConcurrency, Store.Configuration<K, V> config, TimeSource timeSource, StoreEventDispatcher<K, V> eventDispatcher, long sizeInBytes) {
        super(STATISTICS_TAG, config, timeSource, eventDispatcher);
        this.status = new AtomicReference<>(Status.UNINITIALIZED);
        this.fileBasedPersistenceContext = fileBasedPersistenceContext;
        this.executionService = executionService;
        this.threadPoolAlias = threadPoolAlias;
        this.writerConcurrency = writerConcurrency;
        EvictionAdvisor<? super K, ? super V> evictionAdvisor = config.getEvictionAdvisor();
        if (evictionAdvisor != null) {
            this.evictionAdvisor = wrap(evictionAdvisor);
        } else {
            this.evictionAdvisor = wrap(Eviction.noAdvice());
        }
        this.keyType = config.getKeyType();
        this.valueType = config.getValueType();
        this.classLoader = config.getClassLoader();
        this.keySerializer = config.getKeySerializer();
        this.valueSerializer = config.getValueSerializer();
        this.sizeInBytes = sizeInBytes;
        if (!this.status.compareAndSet(Status.UNINITIALIZED, Status.AVAILABLE)) {
            throw new AssertionError();
        }
    }

    @Override // org.ehcache.core.spi.store.ConfigurationChangeSupport
    public List<CacheConfigurationChangeListener> getConfigurationChangeListeners() {
        return Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public EhcachePersistentConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> getBackingMap(long size, Serializer<K> keySerializer, Serializer<V> valueSerializer, SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor) {
        File dataFile = getDataFile();
        File indexFile = getIndexFile();
        File metadataFile = getMetadataFile();
        if (dataFile.isFile() && indexFile.isFile() && metadataFile.isFile()) {
            try {
                return recoverBackingMap(size, keySerializer, valueSerializer, evictionAdvisor);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            return createBackingMap(size, keySerializer, valueSerializer, evictionAdvisor);
        } catch (IOException ex2) {
            throw new RuntimeException(ex2);
        }
    }

    private EhcachePersistentConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> recoverBackingMap(long size, Serializer<K> keySerializer, Serializer<V> valueSerializer, SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor) throws IOException, ClassNotFoundException {
        File dataFile = getDataFile();
        File indexFile = getIndexFile();
        File metadataFile = getMetadataFile();
        FileInputStream fin = new FileInputStream(metadataFile);
        Properties properties = new Properties();
        try {
            properties.load(fin);
            fin.close();
            try {
                Class<?> persistedKeyType = Class.forName(properties.getProperty(KEY_TYPE_PROPERTY_NAME), false, this.classLoader);
                if (!this.keyType.equals(persistedKeyType)) {
                    throw new IllegalArgumentException("Persisted key type '" + persistedKeyType.getName() + "' is not the same as the configured key type '" + this.keyType.getName() + "'");
                }
                try {
                    Class<?> persistedValueType = Class.forName(properties.getProperty(VALUE_TYPE_PROPERTY_NAME), false, this.classLoader);
                    if (!this.valueType.equals(persistedValueType)) {
                        throw new IllegalArgumentException("Persisted value type '" + persistedValueType.getName() + "' is not the same as the configured value type '" + this.valueType.getName() + "'");
                    }
                    fin = new FileInputStream(indexFile);
                    try {
                        try {
                            ObjectInputStream input = new ObjectInputStream(fin);
                            long dataTimestampFromIndex = input.readLong();
                            long dataTimestampFromFile = dataFile.lastModified();
                            long delta = dataTimestampFromFile - dataTimestampFromIndex;
                            if (delta < 0) {
                                LOGGER.info("The index for data file {} is more recent than the data file itself by {}ms : this is harmless.", dataFile.getName(), Long.valueOf(-delta));
                            } else {
                                if (delta > TimeUnit.SECONDS.toMillis(1L)) {
                                    LOGGER.warn("The index for data file {} is out of date by {}ms, probably due to an unclean shutdown. Creating a new empty store.", dataFile.getName(), Long.valueOf(delta));
                                    return createBackingMap(size, keySerializer, valueSerializer, evictionAdvisor);
                                }
                                if (delta > 0) {
                                    LOGGER.info("The index for data file {} is out of date by {}ms, assuming this small delta is a result of the OS/filesystem.", dataFile.getName(), Long.valueOf(delta));
                                }
                            }
                            MappedPageSource source = new MappedPageSource(dataFile, false, size);
                            try {
                                PersistentPortability<K> keyPortability = persistent(new SerializerPortability(keySerializer));
                                PersistentPortability<OffHeapValueHolder<V>> elementPortability = persistent(new OffHeapValueHolderPortability(valueSerializer));
                                DiskWriteThreadPool writeWorkers = new DiskWriteThreadPool(this.executionService, this.threadPoolAlias, this.writerConcurrency);
                                Factory<FileBackedStorageEngine<K, OffHeapValueHolder<V>>> storageEngineFactory = FileBackedStorageEngine.createFactory(source, Math.max((size / 16) / 10, 1024L), MemoryUnit.BYTES, keyPortability, elementPortability, writeWorkers, false);
                                EhcachePersistentSegmentFactory<K, OffHeapValueHolder<V>> factory = new EhcachePersistentSegmentFactory<>(source, storageEngineFactory, 64, evictionAdvisor, this.mapEvictionListener, false);
                                EhcachePersistentConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> m = new EhcachePersistentConcurrentOffHeapClockCache<>(input, evictionAdvisor, factory);
                                m.bootstrap(input);
                                fin.close();
                                return m;
                            } catch (IOException e) {
                                source.close();
                                throw e;
                            }
                        } catch (Exception e2) {
                            LOGGER.info("Index file was corrupt. Deleting data file {}. {}", dataFile.getAbsolutePath(), e2.getMessage());
                            LOGGER.debug("Exception during recovery", (Throwable) e2);
                            EhcachePersistentConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> ehcachePersistentConcurrentOffHeapClockCacheCreateBackingMap = createBackingMap(size, keySerializer, valueSerializer, evictionAdvisor);
                            fin.close();
                            return ehcachePersistentConcurrentOffHeapClockCacheCreateBackingMap;
                        }
                    } catch (Throwable th) {
                        fin.close();
                        throw th;
                    }
                } catch (ClassNotFoundException cnfe) {
                    throw new IllegalStateException("Persisted value type class not found", cnfe);
                }
            } catch (ClassNotFoundException cnfe2) {
                throw new IllegalStateException("Persisted key type class not found", cnfe2);
            }
        } finally {
            fin.close();
        }
    }

    private EhcachePersistentConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> createBackingMap(long size, Serializer<K> keySerializer, Serializer<V> valueSerializer, SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor) throws IOException {
        File metadataFile = getMetadataFile();
        FileOutputStream fos = new FileOutputStream(metadataFile);
        try {
            Properties properties = new Properties();
            properties.put(KEY_TYPE_PROPERTY_NAME, this.keyType.getName());
            properties.put(VALUE_TYPE_PROPERTY_NAME, this.valueType.getName());
            properties.store(fos, "Key and value types");
            fos.close();
            MappedPageSource source = new MappedPageSource(getDataFile(), size);
            PersistentPortability<K> keyPortability = persistent(new SerializerPortability(keySerializer));
            PersistentPortability<OffHeapValueHolder<V>> elementPortability = persistent(new OffHeapValueHolderPortability(valueSerializer));
            DiskWriteThreadPool writeWorkers = new DiskWriteThreadPool(this.executionService, this.threadPoolAlias, this.writerConcurrency);
            Factory<FileBackedStorageEngine<K, OffHeapValueHolder<V>>> storageEngineFactory = FileBackedStorageEngine.createFactory(source, Math.max((size / 16) / 10, 1024L), MemoryUnit.BYTES, keyPortability, elementPortability, writeWorkers, true);
            EhcachePersistentSegmentFactory<K, OffHeapValueHolder<V>> factory = new EhcachePersistentSegmentFactory<>(source, storageEngineFactory, 64, evictionAdvisor, this.mapEvictionListener, true);
            return new EhcachePersistentConcurrentOffHeapClockCache<>(evictionAdvisor, factory, 16);
        } catch (Throwable th) {
            fos.close();
            throw th;
        }
    }

    @Override // org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore
    protected EhcacheOffHeapBackingMap<K, OffHeapValueHolder<V>> backingMap() {
        return this.map;
    }

    @Override // org.ehcache.impl.internal.store.offheap.AbstractOffHeapStore
    protected SwitchableEvictionAdvisor<K, OffHeapValueHolder<V>> evictionAdvisor() {
        return this.evictionAdvisor;
    }

    private File getDataFile() {
        return new File(this.fileBasedPersistenceContext.getDirectory(), "ehcache-disk-store.data");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File getIndexFile() {
        return new File(this.fileBasedPersistenceContext.getDirectory(), "ehcache-disk-store.index");
    }

    private File getMetadataFile() {
        return new File(this.fileBasedPersistenceContext.getDirectory(), "ehcache-disk-store.meta");
    }

    @ServiceDependencies({TimeSourceService.class, SerializationProvider.class, ExecutionService.class, DiskResourceService.class})
    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/disk/OffHeapDiskStore$Provider.class */
    public static class Provider implements Store.Provider, AuthoritativeTier.Provider {
        private final Map<OffHeapDiskStore<?, ?>, Collection<MappedOperationStatistic<?, ?>>> tierOperationStatistics;
        private final Map<Store<?, ?>, PersistableResourceService.PersistenceSpaceIdentifier> createdStores;
        private final String defaultThreadPool;
        private volatile ServiceProvider<Service> serviceProvider;
        private volatile DiskResourceService diskPersistenceService;

        @Override // org.ehcache.core.spi.store.Store.Provider
        public /* bridge */ /* synthetic */ Store createStore(Store.Configuration x0, ServiceConfiguration[] x1) {
            return createStore(x0, (ServiceConfiguration<?>[]) x1);
        }

        public Provider() {
            this(null);
        }

        public Provider(String threadPoolAlias) {
            this.tierOperationStatistics = new ConcurrentWeakIdentityHashMap();
            this.createdStores = new ConcurrentWeakIdentityHashMap();
            this.defaultThreadPool = threadPoolAlias;
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public int rank(Set<ResourceType<?>> resourceTypes, Collection<ServiceConfiguration<?>> serviceConfigs) {
            return resourceTypes.equals(Collections.singleton(ResourceType.Core.DISK)) ? 1 : 0;
        }

        @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.Provider
        public int rankAuthority(ResourceType<?> authorityResource, Collection<ServiceConfiguration<?>> serviceConfigs) {
            return authorityResource.equals(ResourceType.Core.DISK) ? 1 : 0;
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public <K, V> OffHeapDiskStore<K, V> createStore(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OffHeapDiskStore<K, V> store = createStoreInternal(storeConfig, new ThreadLocalStoreEventDispatcher(storeConfig.getDispatcherConcurrency()), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<StoreOperationOutcomes.GetOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(store, TierOperationOutcomes.GET_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.DISK.getTierHeight(), BeanUtil.PREFIX_GETTER_GET, OffHeapDiskStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(store);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(store, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.DISK.getTierHeight(), "eviction", OffHeapDiskStore.STATISTICS_TAG);
            StatisticsManager.associate(evict).withParent(store);
            tieredOps.add(evict);
            this.tierOperationStatistics.put(store, tieredOps);
            return store;
        }

        private <K, V> OffHeapDiskStore<K, V> createStoreInternal(Store.Configuration<K, V> storeConfig, StoreEventDispatcher<K, V> eventDispatcher, ServiceConfiguration<?>... serviceConfigs) {
            String threadPoolAlias;
            int writerConcurrency;
            if (this.serviceProvider == null) {
                throw new NullPointerException("ServiceProvider is null in OffHeapDiskStore.Provider.");
            }
            TimeSource timeSource = ((TimeSourceService) this.serviceProvider.getService(TimeSourceService.class)).getTimeSource();
            ExecutionService executionService = (ExecutionService) this.serviceProvider.getService(ExecutionService.class);
            SizedResourcePool diskPool = (SizedResourcePool) storeConfig.getResourcePools().getPoolForResource(ResourceType.Core.DISK);
            if (!(diskPool.getUnit() instanceof org.ehcache.config.units.MemoryUnit)) {
                throw new IllegalArgumentException("OffHeapDiskStore only supports resources configuration expressed in \"memory\" unit");
            }
            org.ehcache.config.units.MemoryUnit unit = (org.ehcache.config.units.MemoryUnit) diskPool.getUnit();
            OffHeapDiskStoreConfiguration config = (OffHeapDiskStoreConfiguration) ServiceUtils.findSingletonAmongst(OffHeapDiskStoreConfiguration.class, serviceConfigs);
            if (config == null) {
                threadPoolAlias = this.defaultThreadPool;
                writerConcurrency = 1;
            } else {
                threadPoolAlias = config.getThreadPoolAlias();
                writerConcurrency = config.getWriterConcurrency();
            }
            PersistableResourceService.PersistenceSpaceIdentifier<?> space = (PersistableResourceService.PersistenceSpaceIdentifier) ServiceUtils.findSingletonAmongst(PersistableResourceService.PersistenceSpaceIdentifier.class, serviceConfigs);
            if (space == null) {
                throw new IllegalStateException("No LocalPersistenceService could be found - did you configure it at the CacheManager level?");
            }
            try {
                FileBasedPersistenceContext persistenceContext = this.diskPersistenceService.createPersistenceContextWithin(space, "offheap-disk-store");
                OffHeapDiskStore<K, V> offHeapStore = new OffHeapDiskStore<>(persistenceContext, executionService, threadPoolAlias, writerConcurrency, storeConfig, timeSource, eventDispatcher, unit.toBytes(diskPool.getSize()));
                this.createdStores.put(offHeapStore, space);
                return offHeapStore;
            } catch (CachePersistenceException cpex) {
                throw new RuntimeException("Unable to create persistence context in " + space, cpex);
            }
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void releaseStore(Store<?, ?> resource) {
            if (this.createdStores.remove(resource) == null) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
            try {
                OffHeapDiskStore<?, ?> offHeapDiskStore = (OffHeapDiskStore) resource;
                close(offHeapDiskStore);
                StatisticsManager.nodeFor(offHeapDiskStore).clean();
                this.tierOperationStatistics.remove(offHeapDiskStore);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        static <K, V> void close(OffHeapDiskStore<K, V> resource) throws IOException {
            EhcachePersistentConcurrentOffHeapClockCache<K, OffHeapValueHolder<V>> localMap = ((OffHeapDiskStore) resource).map;
            if (localMap != null) {
                ((OffHeapDiskStore) resource).map = null;
                localMap.flush();
                ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(resource.getIndexFile()));
                try {
                    output.writeLong(System.currentTimeMillis());
                    localMap.persist(output);
                    output.close();
                    localMap.close();
                } catch (Throwable th) {
                    output.close();
                    throw th;
                }
            }
        }

        @Override // org.ehcache.core.spi.store.Store.Provider
        public void initStore(Store<?, ?> resource) {
            PersistableResourceService.PersistenceSpaceIdentifier identifier = this.createdStores.get(resource);
            if (identifier == null) {
                throw new IllegalArgumentException("Given store is not managed by this provider : " + resource);
            }
            OffHeapDiskStore<?, ?> diskStore = (OffHeapDiskStore) resource;
            Serializer keySerializer = ((OffHeapDiskStore) diskStore).keySerializer;
            if (keySerializer instanceof StatefulSerializer) {
                try {
                    StateRepository stateRepository = this.diskPersistenceService.getStateRepositoryWithin(identifier, "key-serializer");
                    ((StatefulSerializer) keySerializer).init(stateRepository);
                } catch (CachePersistenceException e) {
                    throw new RuntimeException(e);
                }
            }
            Serializer valueSerializer = ((OffHeapDiskStore) diskStore).valueSerializer;
            if (valueSerializer instanceof StatefulSerializer) {
                try {
                    StateRepository stateRepository2 = this.diskPersistenceService.getStateRepositoryWithin(identifier, "value-serializer");
                    ((StatefulSerializer) valueSerializer).init(stateRepository2);
                } catch (CachePersistenceException e2) {
                    throw new RuntimeException(e2);
                }
            }
            init(diskStore);
        }

        static <K, V> void init(OffHeapDiskStore<K, V> resource) {
            ((OffHeapDiskStore) resource).map = resource.getBackingMap(((OffHeapDiskStore) resource).sizeInBytes, ((OffHeapDiskStore) resource).keySerializer, ((OffHeapDiskStore) resource).valueSerializer, ((OffHeapDiskStore) resource).evictionAdvisor);
        }

        @Override // org.ehcache.spi.service.Service
        public void start(ServiceProvider<Service> serviceProvider) {
            this.serviceProvider = serviceProvider;
            this.diskPersistenceService = (DiskResourceService) serviceProvider.getService(DiskResourceService.class);
            if (this.diskPersistenceService == null) {
                throw new IllegalStateException("Unable to find file based persistence service");
            }
        }

        @Override // org.ehcache.spi.service.Service
        public void stop() {
            this.serviceProvider = null;
            this.createdStores.clear();
            this.diskPersistenceService = null;
        }

        @Override // org.ehcache.core.spi.store.tiering.AuthoritativeTier.Provider
        public <K, V> AuthoritativeTier<K, V> createAuthoritativeTier(Store.Configuration<K, V> storeConfig, ServiceConfiguration<?>... serviceConfigs) {
            OffHeapDiskStore<K, V> authoritativeTier = createStoreInternal(storeConfig, new ThreadLocalStoreEventDispatcher(storeConfig.getDispatcherConcurrency()), serviceConfigs);
            Collection<MappedOperationStatistic<?, ?>> tieredOps = new ArrayList<>();
            MappedOperationStatistic<AuthoritativeTierOperationOutcomes.GetAndFaultOutcome, TierOperationOutcomes.GetOutcome> get = new MappedOperationStatistic<>(authoritativeTier, TierOperationOutcomes.GET_AND_FAULT_TRANSLATION, BeanUtil.PREFIX_GETTER_GET, ResourceType.Core.DISK.getTierHeight(), "getAndFault", OffHeapDiskStore.STATISTICS_TAG);
            StatisticsManager.associate(get).withParent(authoritativeTier);
            tieredOps.add(get);
            MappedOperationStatistic<StoreOperationOutcomes.EvictionOutcome, TierOperationOutcomes.EvictionOutcome> evict = new MappedOperationStatistic<>(authoritativeTier, TierOperationOutcomes.EVICTION_TRANSLATION, "eviction", ResourceType.Core.DISK.getTierHeight(), "eviction", OffHeapDiskStore.STATISTICS_TAG);
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
    }

    public static <T> PersistentPortability<T> persistent(final Portability<T> normal) {
        Class<?> normalKlazz = normal.getClass();
        Class<?>[] delegateInterfaces = normalKlazz.getInterfaces();
        Class<?>[] proxyInterfaces = (Class[]) Arrays.copyOf(delegateInterfaces, delegateInterfaces.length + 1);
        proxyInterfaces[delegateInterfaces.length] = PersistentPortability.class;
        return (PersistentPortability) Proxy.newProxyInstance(normal.getClass().getClassLoader(), proxyInterfaces, new InvocationHandler() { // from class: org.ehcache.impl.internal.store.disk.OffHeapDiskStore.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object o, Method method, Object[] os) throws Throwable {
                if (method.getDeclaringClass().equals(Persistent.class)) {
                    return null;
                }
                return method.invoke(normal, os);
            }
        });
    }
}
