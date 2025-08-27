package org.ehcache.config.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import org.ehcache.Cache;
import org.ehcache.CachePersistenceException;
import org.ehcache.StateTransitionException;
import org.ehcache.UserManagedCache;
import org.ehcache.config.Builder;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.Ehcache;
import org.ehcache.core.EhcacheWithLoaderWriter;
import org.ehcache.core.InternalCache;
import org.ehcache.core.PersistentUserManagedEhcache;
import org.ehcache.core.config.BaseCacheConfiguration;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.events.CacheEventListenerConfiguration;
import org.ehcache.core.events.CacheEventListenerProvider;
import org.ehcache.core.internal.service.ServiceLocator;
import org.ehcache.core.internal.store.StoreConfigurationImpl;
import org.ehcache.core.internal.store.StoreSupport;
import org.ehcache.core.internal.util.ClassLoading;
import org.ehcache.core.spi.LifeCycled;
import org.ehcache.core.spi.LifeCycledAdapter;
import org.ehcache.core.spi.service.DiskResourceService;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.event.CacheEventListener;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.config.serializer.DefaultSerializerConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineProviderConfiguration;
import org.ehcache.impl.copy.SerializingCopier;
import org.ehcache.impl.events.CacheEventDispatcherImpl;
import org.ehcache.impl.internal.events.DisabledCacheEventNotificationService;
import org.ehcache.impl.internal.spi.event.DefaultCacheEventListenerProvider;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.persistence.PersistableResourceService;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.UnsupportedTypeException;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/UserManagedCacheBuilder.class */
public class UserManagedCacheBuilder<K, V, T extends UserManagedCache<K, V>> implements Builder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) UserManagedCacheBuilder.class);
    private static final AtomicLong instanceId = new AtomicLong(0);
    private final Class<K> keyType;
    private final Class<V> valueType;
    private String id;
    private final Set<Service> services;
    private final Set<ServiceCreationConfiguration<?>> serviceCreationConfigurations;
    private Expiry<? super K, ? super V> expiry;
    private ClassLoader classLoader;
    private EvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private CacheLoaderWriter<? super K, V> cacheLoaderWriter;
    private CacheEventDispatcher<K, V> eventDispatcher;
    private ResourcePools resourcePools;
    private Copier<K> keyCopier;
    private boolean useKeySerializingCopier;
    private Copier<V> valueCopier;
    private boolean useValueSerializingCopier;
    private Serializer<K> keySerializer;
    private Serializer<V> valueSerializer;
    private int dispatcherConcurrency;
    private List<CacheEventListenerConfiguration> eventListenerConfigurations;
    private ExecutorService unOrderedExecutor;
    private ExecutorService orderedExecutor;
    private long objectGraphSize;
    private long maxObjectSize;
    private MemoryUnit sizeOfUnit;

    UserManagedCacheBuilder(Class<K> keyType, Class<V> valueType) {
        this.services = new HashSet();
        this.serviceCreationConfigurations = new HashSet();
        this.expiry = Expirations.noExpiration();
        this.classLoader = ClassLoading.getDefaultClassLoader();
        this.eventDispatcher = new DisabledCacheEventNotificationService();
        this.resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(Long.MAX_VALUE, EntryUnit.ENTRIES).build2();
        this.dispatcherConcurrency = 4;
        this.eventListenerConfigurations = new ArrayList();
        this.objectGraphSize = 1000L;
        this.maxObjectSize = Long.MAX_VALUE;
        this.sizeOfUnit = DefaultSizeOfEngineConfiguration.DEFAULT_UNIT;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    private UserManagedCacheBuilder(UserManagedCacheBuilder<K, V, T> toCopy) {
        this.services = new HashSet();
        this.serviceCreationConfigurations = new HashSet();
        this.expiry = Expirations.noExpiration();
        this.classLoader = ClassLoading.getDefaultClassLoader();
        this.eventDispatcher = new DisabledCacheEventNotificationService();
        this.resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(Long.MAX_VALUE, EntryUnit.ENTRIES).build2();
        this.dispatcherConcurrency = 4;
        this.eventListenerConfigurations = new ArrayList();
        this.objectGraphSize = 1000L;
        this.maxObjectSize = Long.MAX_VALUE;
        this.sizeOfUnit = DefaultSizeOfEngineConfiguration.DEFAULT_UNIT;
        this.keyType = toCopy.keyType;
        this.valueType = toCopy.valueType;
        this.id = toCopy.id;
        this.services.addAll(toCopy.services);
        this.serviceCreationConfigurations.addAll(toCopy.serviceCreationConfigurations);
        this.expiry = toCopy.expiry;
        this.classLoader = toCopy.classLoader;
        this.evictionAdvisor = toCopy.evictionAdvisor;
        this.cacheLoaderWriter = toCopy.cacheLoaderWriter;
        this.eventDispatcher = toCopy.eventDispatcher;
        this.resourcePools = toCopy.resourcePools;
        this.keyCopier = toCopy.keyCopier;
        this.valueCopier = toCopy.valueCopier;
        this.keySerializer = toCopy.keySerializer;
        this.valueSerializer = toCopy.valueSerializer;
        this.useKeySerializingCopier = toCopy.useKeySerializingCopier;
        this.useValueSerializingCopier = toCopy.useValueSerializingCopier;
        this.eventListenerConfigurations = toCopy.eventListenerConfigurations;
        this.unOrderedExecutor = toCopy.unOrderedExecutor;
        this.orderedExecutor = toCopy.orderedExecutor;
        this.objectGraphSize = toCopy.objectGraphSize;
        this.maxObjectSize = toCopy.maxObjectSize;
        this.sizeOfUnit = toCopy.sizeOfUnit;
    }

    T build(ServiceLocator.DependencySet dependencySet) throws IllegalStateException {
        InternalCache ehcacheWithLoaderWriter;
        validateListenerConfig();
        try {
            Iterator<ServiceCreationConfiguration<?>> it = this.serviceCreationConfigurations.iterator();
            while (it.hasNext()) {
                dependencySet = dependencySet.with(it.next());
            }
            ServiceLocator serviceLocatorBuild2 = dependencySet.with(Store.Provider.class).build2();
            serviceLocatorBuild2.startAllServices();
            ArrayList arrayList = new ArrayList();
            if (this.keyCopier != null) {
                arrayList.add(new DefaultCopierConfiguration(this.keyCopier, DefaultCopierConfiguration.Type.KEY));
            } else if (this.useKeySerializingCopier) {
                arrayList.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.KEY));
            }
            if (this.valueCopier != null) {
                arrayList.add(new DefaultCopierConfiguration(this.valueCopier, DefaultCopierConfiguration.Type.VALUE));
            } else if (this.useValueSerializingCopier) {
                arrayList.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.VALUE));
            }
            BaseCacheConfiguration baseCacheConfiguration = new BaseCacheConfiguration(this.keyType, this.valueType, this.evictionAdvisor, this.classLoader, this.expiry, this.resourcePools, new ServiceConfiguration[0]);
            ArrayList arrayList2 = new ArrayList();
            Set<ResourceType<?>> resourceTypeSet = this.resourcePools.getResourceTypeSet();
            boolean zContains = resourceTypeSet.contains(ResourceType.Core.DISK);
            if (zContains) {
                if (this.id == null) {
                    throw new IllegalStateException("Persistent user managed caches must have an id set");
                }
                final DiskResourceService diskResourceService = (DiskResourceService) serviceLocatorBuild2.getService(DiskResourceService.class);
                if (!((SizedResourcePool) this.resourcePools.getPoolForResource(ResourceType.Core.DISK)).isPersistent()) {
                    try {
                        diskResourceService.destroy(this.id);
                    } catch (CachePersistenceException e) {
                        throw new RuntimeException("Unable to clean-up persistence space for non-restartable cache " + this.id, e);
                    }
                }
                try {
                    final PersistableResourceService.PersistenceSpaceIdentifier<?> persistenceSpaceIdentifier = diskResourceService.getPersistenceSpaceIdentifier(this.id, baseCacheConfiguration);
                    arrayList2.add(new LifeCycledAdapter() { // from class: org.ehcache.config.builders.UserManagedCacheBuilder.1
                        @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                        public void close() throws Exception {
                            diskResourceService.releasePersistenceSpaceIdentifier(persistenceSpaceIdentifier);
                        }
                    });
                    arrayList.add(persistenceSpaceIdentifier);
                } catch (CachePersistenceException e2) {
                    throw new RuntimeException("Unable to create persistence space for cache " + this.id, e2);
                }
            }
            Serializer<K> serializer = this.keySerializer;
            Serializer<V> serializer2 = this.valueSerializer;
            if (serializer != null) {
                arrayList.add(new DefaultSerializerConfiguration(this.keySerializer, DefaultSerializerConfiguration.Type.KEY));
            }
            if (serializer2 != null) {
                arrayList.add(new DefaultSerializerConfiguration(this.valueSerializer, DefaultSerializerConfiguration.Type.VALUE));
            }
            ServiceConfiguration<?>[] serviceConfigurationArr = (ServiceConfiguration[]) arrayList.toArray(new ServiceConfiguration[0]);
            final SerializationProvider serializationProvider = (SerializationProvider) serviceLocatorBuild2.getService(SerializationProvider.class);
            if (serializationProvider != null) {
                if (serializer == null) {
                    try {
                        final Serializer<K> serializerCreateKeySerializer = serializationProvider.createKeySerializer(this.keyType, this.classLoader, serviceConfigurationArr);
                        arrayList2.add(new LifeCycledAdapter() { // from class: org.ehcache.config.builders.UserManagedCacheBuilder.2
                            @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                            public void close() throws Exception {
                                serializationProvider.releaseSerializer(serializerCreateKeySerializer);
                            }
                        });
                        serializer = serializerCreateKeySerializer;
                    } catch (UnsupportedTypeException e3) {
                        if (resourceTypeSet.contains(ResourceType.Core.OFFHEAP) || resourceTypeSet.contains(ResourceType.Core.DISK)) {
                            throw new RuntimeException(e3);
                        }
                        LOGGER.debug("Serializers for cache '{}' failed creation ({}). However, depending on the configuration, they might not be needed", this.id, e3.getMessage());
                    }
                }
                if (serializer2 == null) {
                    final Serializer<V> serializerCreateValueSerializer = serializationProvider.createValueSerializer(this.valueType, this.classLoader, serviceConfigurationArr);
                    arrayList2.add(new LifeCycledAdapter() { // from class: org.ehcache.config.builders.UserManagedCacheBuilder.3
                        @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                        public void close() throws Exception {
                            serializationProvider.releaseSerializer(serializerCreateValueSerializer);
                        }
                    });
                    serializer2 = serializerCreateValueSerializer;
                }
            }
            final Store.Provider providerSelectStoreProvider = StoreSupport.selectStoreProvider(serviceLocatorBuild2, resourceTypeSet, arrayList);
            final Store<K, V> storeCreateStore = providerSelectStoreProvider.createStore(new StoreConfigurationImpl(this.keyType, this.valueType, this.evictionAdvisor, this.classLoader, this.expiry, this.resourcePools, this.dispatcherConcurrency, serializer, serializer2), serviceConfigurationArr);
            arrayList2.add(new LifeCycled() { // from class: org.ehcache.config.builders.UserManagedCacheBuilder.4
                @Override // org.ehcache.core.spi.LifeCycled
                public void init() throws Exception {
                    providerSelectStoreProvider.initStore(storeCreateStore);
                }

                @Override // org.ehcache.core.spi.LifeCycled
                public void close() throws Exception {
                    providerSelectStoreProvider.releaseStore(storeCreateStore);
                }
            });
            if (this.eventDispatcher instanceof DisabledCacheEventNotificationService) {
                if ((this.orderedExecutor != null) & (this.unOrderedExecutor != null)) {
                    this.eventDispatcher = new CacheEventDispatcherImpl(this.unOrderedExecutor, this.orderedExecutor);
                }
            }
            this.eventDispatcher.setStoreEventSource(storeCreateStore.getStoreEventSource());
            if (zContains) {
                DiskResourceService diskResourceService2 = (DiskResourceService) serviceLocatorBuild2.getService(DiskResourceService.class);
                if (diskResourceService2 == null) {
                    throw new IllegalStateException("No LocalPersistenceService could be found - did you configure one?");
                }
                PersistentUserManagedEhcache persistentUserManagedEhcache = new PersistentUserManagedEhcache(baseCacheConfiguration, storeCreateStore, diskResourceService2, this.cacheLoaderWriter, this.eventDispatcher, this.id);
                registerListeners(persistentUserManagedEhcache, serviceLocatorBuild2, arrayList2);
                Iterator it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    persistentUserManagedEhcache.addHook((LifeCycled) it2.next());
                }
                return (T) cast(persistentUserManagedEhcache);
            }
            if (this.cacheLoaderWriter == null) {
                ehcacheWithLoaderWriter = new Ehcache(baseCacheConfiguration, storeCreateStore, this.eventDispatcher, getLoggerFor(Ehcache.class));
            } else {
                ehcacheWithLoaderWriter = new EhcacheWithLoaderWriter(baseCacheConfiguration, storeCreateStore, this.cacheLoaderWriter, this.eventDispatcher, getLoggerFor(EhcacheWithLoaderWriter.class));
            }
            registerListeners(ehcacheWithLoaderWriter, serviceLocatorBuild2, arrayList2);
            Iterator it3 = arrayList2.iterator();
            while (it3.hasNext()) {
                ehcacheWithLoaderWriter.addHook((LifeCycled) it3.next());
            }
            return (T) cast(ehcacheWithLoaderWriter);
        } catch (Exception e4) {
            throw new IllegalStateException("UserManagedCacheBuilder failed to build.", e4);
        }
    }

    private Logger getLoggerFor(Class clazz) {
        String loggerName;
        if (this.id != null) {
            loggerName = clazz.getName() + "-" + this.id;
        } else {
            loggerName = clazz.getName() + "-UserManaged" + instanceId.incrementAndGet();
        }
        return LoggerFactory.getLogger(loggerName);
    }

    private void validateListenerConfig() {
        if (!this.eventListenerConfigurations.isEmpty() && (this.eventDispatcher instanceof DisabledCacheEventNotificationService) && this.orderedExecutor == null && this.unOrderedExecutor == null) {
            throw new IllegalArgumentException("Listeners will not work unless Executors or EventDispatcher is configured.");
        }
    }

    private void registerListeners(Cache<K, V> cache, ServiceProvider<Service> serviceProvider, List<LifeCycled> lifeCycledList) {
        CacheEventListenerProvider listenerProvider;
        if (!this.eventListenerConfigurations.isEmpty()) {
            CacheEventListenerProvider provider = (CacheEventListenerProvider) serviceProvider.getService(CacheEventListenerProvider.class);
            if (provider != null) {
                listenerProvider = provider;
            } else {
                listenerProvider = new DefaultCacheEventListenerProvider();
            }
            for (CacheEventListenerConfiguration config : this.eventListenerConfigurations) {
                final CacheEventListener<? super K, ? super V> cacheEventListenerCreateEventListener = listenerProvider.createEventListener(this.id, config);
                if (cacheEventListenerCreateEventListener != null) {
                    cache.getRuntimeConfiguration().registerCacheEventListener(cacheEventListenerCreateEventListener, config.orderingMode(), config.firingMode(), config.fireOn());
                    final CacheEventListenerProvider cacheEventListenerProvider = listenerProvider;
                    lifeCycledList.add(new LifeCycled() { // from class: org.ehcache.config.builders.UserManagedCacheBuilder.5
                        @Override // org.ehcache.core.spi.LifeCycled
                        public void init() throws Exception {
                        }

                        @Override // org.ehcache.core.spi.LifeCycled
                        public void close() throws Exception {
                            cacheEventListenerProvider.releaseEventListener(cacheEventListenerCreateEventListener);
                        }
                    });
                }
            }
        }
        this.eventDispatcher.setListenerSource(cache);
    }

    /* JADX WARN: Multi-variable type inference failed */
    T cast(UserManagedCache<K, V> userManagedCache) {
        return userManagedCache;
    }

    public final T build(boolean z) throws IllegalStateException, StateTransitionException {
        T t = (T) build(ServiceLocator.dependencySet().with(this.services));
        if (z) {
            t.init();
        }
        return t;
    }

    @Override // org.ehcache.config.Builder
    /* renamed from: build */
    public T build2() {
        return (T) build(false);
    }

    public final <N extends T> UserManagedCacheBuilder<K, V, N> with(UserManagedCacheConfiguration<K, V, N> userManagedCacheConfiguration) {
        return userManagedCacheConfiguration.builder(this);
    }

    public final UserManagedCacheBuilder<K, V, T> identifier(String identifier) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.id = identifier;
        return otherBuilder;
    }

    public final UserManagedCacheBuilder<K, V, T> withClassLoader(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new NullPointerException("Null classloader");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.classLoader = classLoader;
        return otherBuilder;
    }

    public final UserManagedCacheBuilder<K, V, T> withExpiry(Expiry<? super K, ? super V> expiry) {
        if (expiry == null) {
            throw new NullPointerException("Null expiry");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.expiry = expiry;
        return otherBuilder;
    }

    public final UserManagedCacheBuilder<K, V, T> withEventDispatcher(CacheEventDispatcher<K, V> eventDispatcher) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.orderedExecutor = null;
        otherBuilder.unOrderedExecutor = null;
        otherBuilder.eventDispatcher = eventDispatcher;
        return otherBuilder;
    }

    public final UserManagedCacheBuilder<K, V, T> withEventExecutors(ExecutorService orderedExecutor, ExecutorService unOrderedExecutor) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.eventDispatcher = new DisabledCacheEventNotificationService();
        otherBuilder.orderedExecutor = orderedExecutor;
        otherBuilder.unOrderedExecutor = unOrderedExecutor;
        return otherBuilder;
    }

    public final UserManagedCacheBuilder<K, V, T> withEventListeners(CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration) {
        return withEventListeners(cacheEventListenerConfiguration.build2());
    }

    public final UserManagedCacheBuilder<K, V, T> withEventListeners(CacheEventListenerConfiguration... cacheEventListenerConfigurations) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.eventListenerConfigurations.addAll(Arrays.asList(cacheEventListenerConfigurations));
        return otherBuilder;
    }

    public final UserManagedCacheBuilder<K, V, T> withResourcePools(ResourcePools resourcePools) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.resourcePools = resourcePools;
        return otherBuilder;
    }

    public final UserManagedCacheBuilder<K, V, T> withResourcePools(ResourcePoolsBuilder resourcePoolsBuilder) {
        return withResourcePools(resourcePoolsBuilder.build2());
    }

    public final UserManagedCacheBuilder<K, V, T> withDispatcherConcurrency(int dispatcherConcurrency) {
        this.dispatcherConcurrency = dispatcherConcurrency;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public UserManagedCacheBuilder<K, V, T> withEvictionAdvisor(EvictionAdvisor<K, V> evictionAdvisor) {
        if (evictionAdvisor == 0) {
            throw new NullPointerException("Null eviction advisor");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.evictionAdvisor = evictionAdvisor;
        return otherBuilder;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public UserManagedCacheBuilder<K, V, T> withLoaderWriter(CacheLoaderWriter<K, V> cacheLoaderWriter) {
        if (cacheLoaderWriter == 0) {
            throw new NullPointerException("Null loaderWriter");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.cacheLoaderWriter = cacheLoaderWriter;
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withKeySerializingCopier() {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.keyCopier = null;
        otherBuilder.useKeySerializingCopier = true;
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withValueSerializingCopier() {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.valueCopier = null;
        otherBuilder.useValueSerializingCopier = true;
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withKeyCopier(Copier<K> keyCopier) {
        if (keyCopier == null) {
            throw new NullPointerException("Null key copier");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.keyCopier = keyCopier;
        otherBuilder.useKeySerializingCopier = false;
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withValueCopier(Copier<V> valueCopier) {
        if (valueCopier == null) {
            throw new NullPointerException("Null value copier");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.valueCopier = valueCopier;
        otherBuilder.useValueSerializingCopier = false;
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withKeySerializer(Serializer<K> keySerializer) {
        if (keySerializer == null) {
            throw new NullPointerException("Null key serializer");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.keySerializer = keySerializer;
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withValueSerializer(Serializer<V> valueSerializer) {
        if (valueSerializer == null) {
            throw new NullPointerException("Null value serializer");
        }
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        otherBuilder.valueSerializer = valueSerializer;
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withSizeOfMaxObjectGraph(long size) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        removeAnySizeOfEngine(otherBuilder);
        otherBuilder.objectGraphSize = size;
        otherBuilder.serviceCreationConfigurations.add(new DefaultSizeOfEngineProviderConfiguration(otherBuilder.maxObjectSize, otherBuilder.sizeOfUnit, otherBuilder.objectGraphSize));
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> withSizeOfMaxObjectSize(long size, MemoryUnit unit) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        removeAnySizeOfEngine(otherBuilder);
        otherBuilder.maxObjectSize = size;
        otherBuilder.sizeOfUnit = unit;
        otherBuilder.serviceCreationConfigurations.add(new DefaultSizeOfEngineProviderConfiguration(otherBuilder.maxObjectSize, otherBuilder.sizeOfUnit, otherBuilder.objectGraphSize));
        return otherBuilder;
    }

    public static <K, V> UserManagedCacheBuilder<K, V, UserManagedCache<K, V>> newUserManagedCacheBuilder(Class<K> keyType, Class<V> valueType) {
        return new UserManagedCacheBuilder<>(keyType, valueType);
    }

    public UserManagedCacheBuilder<K, V, T> using(Service service) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        if (service instanceof SizeOfEngineProvider) {
            removeAnySizeOfEngine(otherBuilder);
        }
        otherBuilder.services.add(service);
        return otherBuilder;
    }

    public UserManagedCacheBuilder<K, V, T> using(ServiceCreationConfiguration<?> serviceConfiguration) {
        UserManagedCacheBuilder<K, V, T> otherBuilder = new UserManagedCacheBuilder<>(this);
        if (serviceConfiguration instanceof DefaultSizeOfEngineProviderConfiguration) {
            removeAnySizeOfEngine(otherBuilder);
        }
        otherBuilder.serviceCreationConfigurations.add(serviceConfiguration);
        return otherBuilder;
    }

    private static void removeAnySizeOfEngine(UserManagedCacheBuilder builder) {
        builder.services.remove(ServiceUtils.findSingletonAmongst(SizeOfEngineProvider.class, builder.services));
        builder.serviceCreationConfigurations.remove(ServiceUtils.findSingletonAmongst(DefaultSizeOfEngineProviderConfiguration.class, builder.serviceCreationConfigurations));
    }
}
