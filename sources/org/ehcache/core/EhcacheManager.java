package org.ehcache.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.ehcache.Cache;
import org.ehcache.CachePersistenceException;
import org.ehcache.PersistentCacheManager;
import org.ehcache.StateTransitionException;
import org.ehcache.Status;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourceType;
import org.ehcache.core.StatusTransitioner;
import org.ehcache.core.config.BaseCacheConfiguration;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.core.config.store.StoreEventSourceConfiguration;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.events.CacheEventDispatcherFactory;
import org.ehcache.core.events.CacheEventListenerConfiguration;
import org.ehcache.core.events.CacheEventListenerProvider;
import org.ehcache.core.events.CacheManagerListener;
import org.ehcache.core.internal.service.ServiceLocator;
import org.ehcache.core.internal.store.StoreConfigurationImpl;
import org.ehcache.core.internal.store.StoreSupport;
import org.ehcache.core.internal.util.ClassLoading;
import org.ehcache.core.spi.LifeCycled;
import org.ehcache.core.spi.LifeCycledAdapter;
import org.ehcache.core.spi.service.CacheManagerProviderService;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.core.spi.store.InternalCacheManager;
import org.ehcache.core.spi.store.Store;
import org.ehcache.event.CacheEventListener;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoaderWriterProvider;
import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;
import org.ehcache.spi.loaderwriter.WriteBehindProvider;
import org.ehcache.spi.persistence.PersistableResourceService;
import org.ehcache.spi.serialization.SerializationProvider;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.UnsupportedTypeException;
import org.ehcache.spi.service.MaintainableService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.spi.service.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/EhcacheManager.class */
public class EhcacheManager implements PersistentCacheManager, InternalCacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) EhcacheManager.class);
    private final DefaultConfiguration configuration;
    private final ClassLoader cacheManagerClassLoader;
    private final boolean useLoaderInAtomics;
    private final ConcurrentMap<String, CacheHolder> caches;
    private final CopyOnWriteArrayList<CacheManagerListener> listeners;
    private final StatusTransitioner statusTransitioner;
    private final String simpleName;
    protected final ServiceLocator serviceLocator;

    public EhcacheManager(Configuration config) {
        this(config, Collections.emptyList(), true);
    }

    public EhcacheManager(Configuration config, Collection<Service> services) {
        this(config, services, true);
    }

    public EhcacheManager(Configuration config, Collection<Service> services, boolean useLoaderInAtomics) {
        this.caches = new ConcurrentHashMap();
        this.listeners = new CopyOnWriteArrayList<>();
        this.statusTransitioner = new StatusTransitioner(LOGGER);
        String simpleName = getClass().getSimpleName();
        this.simpleName = simpleName.isEmpty() ? getClass().getName() : simpleName;
        this.configuration = new DefaultConfiguration(config);
        this.cacheManagerClassLoader = config.getClassLoader() != null ? config.getClassLoader() : ClassLoading.getDefaultClassLoader();
        this.useLoaderInAtomics = useLoaderInAtomics;
        validateServicesConfigs();
        this.serviceLocator = resolveServices(services);
    }

    private void validateServicesConfigs() {
        HashSet<Class> classes = new HashSet<>();
        for (ServiceCreationConfiguration<?> service : this.configuration.getServiceCreationConfigurations()) {
            if (!classes.add(service.getServiceType())) {
                throw new IllegalStateException("Duplicate creation configuration for service " + service.getServiceType());
            }
        }
    }

    private ServiceLocator resolveServices(Collection<Service> services) {
        ServiceLocator.DependencySet builder = ServiceLocator.dependencySet().with(Store.Provider.class).with(CacheLoaderWriterProvider.class).with(WriteBehindProvider.class).with(CacheEventDispatcherFactory.class).with(CacheEventListenerProvider.class).with(services);
        if (!builder.contains(CacheManagerProviderService.class)) {
            builder = builder.with(new DefaultCacheManagerProviderService(this));
        }
        Iterator i$ = this.configuration.getServiceCreationConfigurations().iterator();
        while (i$.hasNext()) {
            builder = builder.with(i$.next());
        }
        return builder.build2();
    }

    StatusTransitioner getStatusTransitioner() {
        return this.statusTransitioner;
    }

    @Override // org.ehcache.CacheManager
    public <K, V> Cache<K, V> getCache(String alias, Class<K> keyType, Class<V> valueType) {
        this.statusTransitioner.checkAvailable();
        CacheHolder cacheHolder = this.caches.get(alias);
        if (cacheHolder == null) {
            return null;
        }
        try {
            return cacheHolder.retrieve(keyType, valueType);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cache '" + alias + "' type is <" + cacheHolder.keyType.getName() + ", " + cacheHolder.valueType.getName() + ">, but you retrieved it with <" + keyType.getName() + ", " + valueType.getName() + ">");
        }
    }

    @Override // org.ehcache.CacheManager
    public void removeCache(String alias) {
        if (alias == null) {
            throw new NullPointerException("Alias cannot be null");
        }
        removeCache(alias, true);
    }

    private void removeCache(String alias, boolean removeFromConfig) {
        this.statusTransitioner.checkAvailable();
        CacheHolder cacheHolder = this.caches.remove(alias);
        if (cacheHolder != null) {
            InternalCache<?, ?> ehcache = cacheHolder.retrieve(cacheHolder.keyType, cacheHolder.valueType);
            if (ehcache != null) {
                if (removeFromConfig) {
                    this.configuration.removeCacheConfiguration(alias);
                }
                if (!this.statusTransitioner.isTransitioning()) {
                    Iterator i$ = this.listeners.iterator();
                    while (i$.hasNext()) {
                        CacheManagerListener listener = i$.next();
                        listener.cacheRemoved(alias, ehcache);
                    }
                }
                ehcache.close();
                closeEhcache(alias, ehcache);
            }
            LOGGER.info("Cache '{}' removed from {}.", alias, this.simpleName);
        }
    }

    protected void closeEhcache(String alias, InternalCache<?, ?> ehcache) {
        for (ResourceType<?> resourceType : ehcache.getRuntimeConfiguration().getResourcePools().getResourceTypeSet()) {
            if (resourceType.isPersistable()) {
                ResourcePool resourcePool = ehcache.getRuntimeConfiguration().getResourcePools().getPoolForResource(resourceType);
                if (!resourcePool.isPersistent()) {
                    PersistableResourceService persistableResourceService = getPersistableResourceService(resourceType);
                    try {
                        persistableResourceService.destroy(alias);
                    } catch (CachePersistenceException e) {
                        LOGGER.warn("Unable to clear persistence space for cache {}", alias, e);
                    }
                }
            }
        }
    }

    @Override // org.ehcache.CacheManager
    public <K, V> Cache<K, V> createCache(String alias, Builder<? extends CacheConfiguration<K, V>> configBuilder) {
        return createCache(alias, configBuilder.build2());
    }

    @Override // org.ehcache.CacheManager
    public <K, V> Cache<K, V> createCache(String alias, CacheConfiguration<K, V> config) throws IllegalArgumentException {
        return createCache(alias, config, true);
    }

    private <K, V> Cache<K, V> createCache(String alias, CacheConfiguration<K, V> originalConfig, boolean addToConfig) throws IllegalArgumentException {
        this.statusTransitioner.checkAvailable();
        LOGGER.debug("Creating Cache '{}' in {}.", alias, this.simpleName);
        CacheConfiguration<K, V> config = adjustConfigurationWithCacheManagerDefaults(originalConfig);
        Class<K> keyType = config.getKeyType();
        Class<V> valueType = config.getValueType();
        CacheHolder value = new CacheHolder(keyType, valueType, null);
        if (this.caches.putIfAbsent(alias, value) != null) {
            throw new IllegalArgumentException("Cache '" + alias + "' already exists");
        }
        InternalCache<K, V> cache = null;
        RuntimeException failure = null;
        try {
            cache = createNewEhcache(alias, config, keyType, valueType);
            cache.init();
            if (addToConfig) {
                this.configuration.addCacheConfiguration(alias, cache.getRuntimeConfiguration());
            } else {
                this.configuration.replaceCacheConfiguration(alias, originalConfig, cache.getRuntimeConfiguration());
            }
            if (1 == 0) {
                this.caches.remove(alias);
                value.setCache(null);
            }
        } catch (RuntimeException e) {
            failure = e;
            if (0 == 0) {
                this.caches.remove(alias);
                value.setCache(null);
            }
        } catch (Throwable th) {
            if (0 == 0) {
                this.caches.remove(alias);
                value.setCache(null);
            }
            throw th;
        }
        if (failure == null) {
            try {
                if (!this.statusTransitioner.isTransitioning()) {
                    Iterator i$ = this.listeners.iterator();
                    while (i$.hasNext()) {
                        CacheManagerListener listener = i$.next();
                        listener.cacheAdded(alias, cache);
                    }
                }
                LOGGER.info("Cache '{}' created in {}.", alias, this.simpleName);
                return cache;
            } finally {
                value.setCache(cache);
            }
        }
        throw new IllegalStateException("Cache '" + alias + "' creation in " + this.simpleName + " failed.", failure);
    }

    <K, V> InternalCache<K, V> createNewEhcache(String alias, CacheConfiguration<K, V> config, Class<K> keyType, Class<V> valueType) {
        final CacheLoaderWriter<? super K, V> decorator;
        InternalCache<K, V> cache;
        Collection<ServiceConfiguration<?>> adjustedServiceConfigs = new ArrayList<>(config.getServiceConfigurations());
        List<ServiceConfiguration> unknownServiceConfigs = new ArrayList<>();
        for (ServiceConfiguration serviceConfig : adjustedServiceConfigs) {
            if (!this.serviceLocator.knowsServiceFor(serviceConfig)) {
                unknownServiceConfigs.add(serviceConfig);
            }
        }
        if (!unknownServiceConfigs.isEmpty()) {
            throw new IllegalStateException("Cannot find service(s) that can handle following configuration(s) : " + unknownServiceConfigs);
        }
        List<LifeCycled> lifeCycledList = new ArrayList<>();
        Store<K, V> store = getStore(alias, config, keyType, valueType, adjustedServiceConfigs, lifeCycledList);
        final CacheLoaderWriterProvider cacheLoaderWriterProvider = (CacheLoaderWriterProvider) this.serviceLocator.getService(CacheLoaderWriterProvider.class);
        if (cacheLoaderWriterProvider != null) {
            final CacheLoaderWriter<? super K, V> loaderWriter = cacheLoaderWriterProvider.createCacheLoaderWriter(alias, config);
            WriteBehindConfiguration writeBehindConfiguration = (WriteBehindConfiguration) ServiceUtils.findSingletonAmongst(WriteBehindConfiguration.class, config.getServiceConfigurations().toArray());
            if (writeBehindConfiguration == null) {
                decorator = loaderWriter;
            } else {
                final WriteBehindProvider factory = (WriteBehindProvider) this.serviceLocator.getService(WriteBehindProvider.class);
                decorator = factory.createWriteBehindLoaderWriter(loaderWriter, writeBehindConfiguration);
                if (decorator != null) {
                    lifeCycledList.add(new LifeCycledAdapter() { // from class: org.ehcache.core.EhcacheManager.1
                        @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                        public void close() {
                            factory.releaseWriteBehindLoaderWriter(decorator);
                        }
                    });
                }
            }
            if (loaderWriter != null) {
                lifeCycledList.add(new LifeCycledAdapter() { // from class: org.ehcache.core.EhcacheManager.2
                    @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                    public void close() throws Exception {
                        cacheLoaderWriterProvider.releaseCacheLoaderWriter(loaderWriter);
                    }
                });
            }
        } else {
            decorator = null;
        }
        final CacheEventDispatcherFactory cenlProvider = (CacheEventDispatcherFactory) this.serviceLocator.getService(CacheEventDispatcherFactory.class);
        final CacheEventDispatcher<K, V> evtService = cenlProvider.createCacheEventDispatcher(store, (ServiceConfiguration[]) adjustedServiceConfigs.toArray(new ServiceConfiguration[adjustedServiceConfigs.size()]));
        lifeCycledList.add(new LifeCycledAdapter() { // from class: org.ehcache.core.EhcacheManager.3
            @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
            public void close() {
                cenlProvider.releaseCacheEventDispatcher(evtService);
            }
        });
        evtService.setStoreEventSource(store.getStoreEventSource());
        if (decorator == null) {
            cache = new Ehcache(config, store, evtService, LoggerFactory.getLogger(Ehcache.class + "-" + alias));
        } else {
            cache = new EhcacheWithLoaderWriter(config, store, decorator, evtService, this.useLoaderInAtomics, LoggerFactory.getLogger(EhcacheWithLoaderWriter.class + "-" + alias));
        }
        final CacheEventListenerProvider evntLsnrFactory = (CacheEventListenerProvider) this.serviceLocator.getService(CacheEventListenerProvider.class);
        if (evntLsnrFactory != null) {
            Collection<CacheEventListenerConfiguration> evtLsnrConfigs = ServiceUtils.findAmongst(CacheEventListenerConfiguration.class, config.getServiceConfigurations());
            for (CacheEventListenerConfiguration lsnrConfig : evtLsnrConfigs) {
                final CacheEventListener<? super K, ? super V> cacheEventListenerCreateEventListener = evntLsnrFactory.createEventListener(alias, lsnrConfig);
                if (cacheEventListenerCreateEventListener != null) {
                    cache.getRuntimeConfiguration().registerCacheEventListener(cacheEventListenerCreateEventListener, lsnrConfig.orderingMode(), lsnrConfig.firingMode(), lsnrConfig.fireOn());
                    lifeCycledList.add(new LifeCycled() { // from class: org.ehcache.core.EhcacheManager.4
                        @Override // org.ehcache.core.spi.LifeCycled
                        public void init() throws Exception {
                        }

                        @Override // org.ehcache.core.spi.LifeCycled
                        public void close() throws Exception {
                            evntLsnrFactory.releaseEventListener(cacheEventListenerCreateEventListener);
                        }
                    });
                }
            }
            evtService.setListenerSource(cache);
        }
        for (LifeCycled lifeCycled : lifeCycledList) {
            cache.addHook(lifeCycled);
        }
        return cache;
    }

    protected <K, V> Store<K, V> getStore(String alias, CacheConfiguration<K, V> config, Class<K> keyType, Class<V> valueType, Collection<ServiceConfiguration<?>> serviceConfigs, List<LifeCycled> lifeCycledList) {
        int dispatcherConcurrency;
        Set<ResourceType<?>> resourceTypes = config.getResourcePools().getResourceTypeSet();
        for (ResourceType<?> resourceType : resourceTypes) {
            if (resourceType.isPersistable()) {
                final PersistableResourceService persistableResourceService = getPersistableResourceService(resourceType);
                try {
                    final PersistableResourceService.PersistenceSpaceIdentifier<?> spaceIdentifier = persistableResourceService.getPersistenceSpaceIdentifier(alias, config);
                    serviceConfigs.add(spaceIdentifier);
                    lifeCycledList.add(new LifeCycledAdapter() { // from class: org.ehcache.core.EhcacheManager.5
                        @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                        public void close() throws Exception {
                            persistableResourceService.releasePersistenceSpaceIdentifier(spaceIdentifier);
                        }
                    });
                } catch (CachePersistenceException e) {
                    throw new RuntimeException("Unable to handle persistence", e);
                }
            }
        }
        final Store.Provider storeProvider = StoreSupport.selectStoreProvider(this.serviceLocator, resourceTypes, serviceConfigs);
        Serializer<K> keySerializer = null;
        Serializer<V> valueSerializer = null;
        final SerializationProvider serialization = (SerializationProvider) this.serviceLocator.getService(SerializationProvider.class);
        ServiceConfiguration<?>[] serviceConfigArray = (ServiceConfiguration[]) serviceConfigs.toArray(new ServiceConfiguration[serviceConfigs.size()]);
        if (serialization != null) {
            try {
                final Serializer<K> keySer = serialization.createKeySerializer(keyType, config.getClassLoader(), serviceConfigArray);
                lifeCycledList.add(new LifeCycledAdapter() { // from class: org.ehcache.core.EhcacheManager.6
                    @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                    public void close() throws Exception {
                        serialization.releaseSerializer(keySer);
                    }
                });
                keySerializer = keySer;
            } catch (UnsupportedTypeException e2) {
                for (ResourceType<?> resource : resourceTypes) {
                    if (resource.requiresSerialization()) {
                        throw new RuntimeException(e2);
                    }
                }
                LOGGER.debug("Could not create serializers for {}", alias, e2);
            }
            try {
                final Serializer<V> valueSer = serialization.createValueSerializer(valueType, config.getClassLoader(), serviceConfigArray);
                lifeCycledList.add(new LifeCycledAdapter() { // from class: org.ehcache.core.EhcacheManager.7
                    @Override // org.ehcache.core.spi.LifeCycledAdapter, org.ehcache.core.spi.LifeCycled
                    public void close() throws Exception {
                        serialization.releaseSerializer(valueSer);
                    }
                });
                valueSerializer = valueSer;
            } catch (UnsupportedTypeException e3) {
                for (ResourceType<?> resource2 : resourceTypes) {
                    if (resource2.requiresSerialization()) {
                        throw new RuntimeException(e3);
                    }
                }
                LOGGER.debug("Could not create serializers for {}", alias, e3);
            }
        }
        StoreEventSourceConfiguration eventSourceConfiguration = (StoreEventSourceConfiguration) ServiceUtils.findSingletonAmongst(StoreEventSourceConfiguration.class, config.getServiceConfigurations().toArray());
        if (eventSourceConfiguration != null) {
            dispatcherConcurrency = eventSourceConfiguration.getDispatcherConcurrency();
        } else {
            dispatcherConcurrency = 1;
        }
        final Store<K, V> store = storeProvider.createStore(new StoreConfigurationImpl(config, dispatcherConcurrency, keySerializer, valueSerializer), serviceConfigArray);
        lifeCycledList.add(new LifeCycled() { // from class: org.ehcache.core.EhcacheManager.8
            @Override // org.ehcache.core.spi.LifeCycled
            public void init() throws Exception {
                storeProvider.initStore(store);
            }

            @Override // org.ehcache.core.spi.LifeCycled
            public void close() {
                storeProvider.releaseStore(store);
            }
        });
        return store;
    }

    private PersistableResourceService getPersistableResourceService(ResourceType<?> resourceType) {
        Collection<PersistableResourceService> services = this.serviceLocator.getServicesOfType(PersistableResourceService.class);
        for (PersistableResourceService service : services) {
            if (service.handlesResourceType(resourceType)) {
                return service;
            }
        }
        throw new IllegalStateException("No service found for persistable resource: " + resourceType);
    }

    private <K, V> CacheConfiguration<K, V> adjustConfigurationWithCacheManagerDefaults(CacheConfiguration<K, V> config) {
        ClassLoader cacheClassLoader = config.getClassLoader();
        if (cacheClassLoader == null) {
            cacheClassLoader = this.cacheManagerClassLoader;
        }
        if (cacheClassLoader != config.getClassLoader()) {
            config = new BaseCacheConfiguration(config.getKeyType(), config.getValueType(), config.getEvictionAdvisor(), cacheClassLoader, config.getExpiry(), config.getResourcePools(), (ServiceConfiguration[]) config.getServiceConfigurations().toArray(new ServiceConfiguration[config.getServiceConfigurations().size()]));
        }
        return config;
    }

    @Override // org.ehcache.core.spi.store.InternalCacheManager
    public void registerListener(CacheManagerListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
            this.statusTransitioner.registerListener(listener);
        }
    }

    @Override // org.ehcache.core.spi.store.InternalCacheManager
    public void deregisterListener(CacheManagerListener listener) {
        if (this.listeners.remove(listener)) {
            this.statusTransitioner.deregisterListener(listener);
        }
    }

    @Override // org.ehcache.CacheManager
    public void init() {
        StatusTransitioner.Transition st = this.statusTransitioner.init();
        try {
            try {
                this.serviceLocator.startAllServices();
                Deque<String> initiatedCaches = new ArrayDeque<>();
                try {
                    for (Map.Entry<String, CacheConfiguration<?, ?>> cacheConfigurationEntry : this.configuration.getCacheConfigurations().entrySet()) {
                        String alias = cacheConfigurationEntry.getKey();
                        createCache(alias, cacheConfigurationEntry.getValue(), false);
                        initiatedCaches.push(alias);
                    }
                    st.succeeded();
                    st.failed(null);
                } catch (RuntimeException e) {
                    while (!initiatedCaches.isEmpty()) {
                        String toBeClosed = initiatedCaches.pop();
                        try {
                            removeCache(toBeClosed, false);
                        } catch (Exception exceptionClosingCache) {
                            LOGGER.error("Cache '{}' could not be removed after initialization failure due to ", toBeClosed, exceptionClosingCache);
                        }
                    }
                    try {
                        this.serviceLocator.stopAllServices();
                    } catch (Exception exceptionStoppingServices) {
                        LOGGER.error("Stopping services after initialization failure failed due to ", (Throwable) exceptionStoppingServices);
                    }
                    throw e;
                }
            } catch (Exception e2) {
                throw st.failed(e2);
            }
        } catch (Throwable th) {
            st.failed(null);
            throw th;
        }
    }

    @Override // org.ehcache.CacheManager
    public Status getStatus() {
        return this.statusTransitioner.currentStatus();
    }

    @Override // org.ehcache.CacheManager, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        StatusTransitioner.Transition st = this.statusTransitioner.close();
        Exception firstException = null;
        try {
            for (String alias : this.caches.keySet()) {
                try {
                    removeCache(alias, false);
                } catch (Exception e) {
                    if (firstException == null) {
                        firstException = e;
                    } else {
                        LOGGER.error("Cache '{}' could not be removed due to ", alias, e);
                    }
                }
            }
            this.serviceLocator.stopAllServices();
            if (firstException == null) {
                st.succeeded();
            }
            if (firstException != null) {
                throw st.failed(firstException);
            }
            st.failed(null);
        } catch (Exception e2) {
            if (firstException == null) {
                firstException = e2;
            }
            if (firstException != null) {
                throw st.failed(firstException);
            }
            st.failed(null);
        } catch (Throwable th) {
            if (firstException != null) {
                throw st.failed(firstException);
            }
            st.failed(null);
            throw th;
        }
    }

    @Override // org.ehcache.CacheManager
    public Configuration getRuntimeConfiguration() {
        return this.configuration;
    }

    @Override // org.ehcache.PersistentCacheManager
    public void destroyCache(String alias) throws CachePersistenceException {
        StatusTransitioner.Transition st;
        StateTransitionException stateTransitionExceptionFailed;
        if (alias == null) {
            throw new NullPointerException("Alias cannot be null");
        }
        LOGGER.debug("Destroying Cache '{}' in {}.", alias, this.simpleName);
        StatusTransitioner.Transition maintenance = null;
        try {
            maintenance = this.statusTransitioner.maintenance();
        } catch (IllegalStateException e) {
            this.statusTransitioner.checkAvailable();
        }
        if (maintenance != null) {
            try {
                startMaintainableServices(MaintainableService.MaintenanceScope.CACHE);
                maintenance.succeeded();
            } catch (Throwable t) {
                throw maintenance.failed(t);
            }
        }
        try {
            removeCache(alias, true);
            destroyPersistenceSpace(alias);
            if (maintenance != null) {
                st = this.statusTransitioner.exitMaintenance();
                try {
                    stopMaintainableServices();
                    st.succeeded();
                } finally {
                }
            }
            LOGGER.info("Cache '{}' successfully destroyed in {}.", alias, this.simpleName);
        } catch (Throwable th) {
            if (maintenance != null) {
                st = this.statusTransitioner.exitMaintenance();
                try {
                    stopMaintainableServices();
                    st.succeeded();
                } finally {
                }
            }
            throw th;
        }
    }

    private void destroyPersistenceSpace(String alias) throws CachePersistenceException {
        Collection<PersistableResourceService> services = this.serviceLocator.getServicesOfType(PersistableResourceService.class);
        for (PersistableResourceService service : services) {
            service.destroy(alias);
        }
    }

    @Override // org.ehcache.PersistentCacheManager
    public void destroy() throws CachePersistenceException {
        StateTransitionException stateTransitionExceptionFailed;
        StatusTransitioner.Transition st = this.statusTransitioner.maintenance();
        try {
            startMaintainableServices(MaintainableService.MaintenanceScope.CACHE_MANAGER);
            st.succeeded();
            destroyInternal();
            st = this.statusTransitioner.exitMaintenance();
            try {
                stopMaintainableServices();
                st.succeeded();
                LOGGER.info("All persistent data destroyed for {}", this.simpleName);
            } finally {
            }
        } finally {
        }
    }

    private void startMaintainableServices(MaintainableService.MaintenanceScope maintenanceScope) {
        ServiceProvider<MaintainableService> provider = getMaintainableServiceProvider();
        Collection<MaintainableService> services = this.serviceLocator.getServicesOfType(MaintainableService.class);
        for (MaintainableService service : services) {
            service.startForMaintenance(provider, maintenanceScope);
        }
    }

    private ServiceProvider<MaintainableService> getMaintainableServiceProvider() {
        return new ServiceProvider<MaintainableService>() { // from class: org.ehcache.core.EhcacheManager.9
            @Override // org.ehcache.spi.service.ServiceProvider
            public <U extends MaintainableService> U getService(Class<U> serviceType) {
                return (U) EhcacheManager.this.serviceLocator.getService(serviceType);
            }

            @Override // org.ehcache.spi.service.ServiceProvider
            public <U extends MaintainableService> Collection<U> getServicesOfType(Class<U> serviceType) {
                return EhcacheManager.this.serviceLocator.getServicesOfType(serviceType);
            }
        };
    }

    private void stopMaintainableServices() {
        Collection<MaintainableService> services = this.serviceLocator.getServicesOfType(MaintainableService.class);
        for (MaintainableService service : services) {
            service.stop();
        }
    }

    ClassLoader getClassLoader() {
        return this.cacheManagerClassLoader;
    }

    void destroyInternal() throws CachePersistenceException {
        this.statusTransitioner.checkMaintenance();
        Collection<PersistableResourceService> services = this.serviceLocator.getServicesOfType(PersistableResourceService.class);
        for (PersistableResourceService service : services) {
            service.destroyAll();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/core/EhcacheManager$CacheHolder.class */
    private static final class CacheHolder {
        private final Class<?> keyType;
        private final Class<?> valueType;
        private volatile InternalCache<?, ?> cache;
        private volatile boolean isValueSet = false;

        CacheHolder(Class<?> keyType, Class<?> valueType, InternalCache<?, ?> cache) {
            this.keyType = keyType;
            this.valueType = valueType;
            this.cache = cache;
        }

        /* JADX WARN: Finally extract failed */
        <K, V> InternalCache<K, V> retrieve(Class<K> refKeyType, Class<V> refValueType) {
            if (!this.isValueSet) {
                synchronized (this) {
                    boolean interrupted = false;
                    while (!this.isValueSet) {
                        try {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                interrupted = true;
                            }
                        } catch (Throwable th) {
                            if (interrupted) {
                                Thread.currentThread().interrupt();
                            }
                            throw th;
                        }
                    }
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (this.keyType == refKeyType && this.valueType == refValueType) {
                return cast(this.cache);
            }
            throw new IllegalArgumentException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static <K, V> InternalCache<K, V> cast(InternalCache<?, ?> internalCache) {
            return internalCache;
        }

        public synchronized void setCache(InternalCache<?, ?> cache) {
            this.cache = cache;
            this.isValueSet = true;
            notifyAll();
        }
    }
}
