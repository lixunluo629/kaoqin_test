package org.ehcache.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePool;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.WriteBehindConfigurationBuilder;
import org.ehcache.core.internal.util.ClassLoading;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventFiring;
import org.ehcache.event.EventOrdering;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.config.copy.DefaultCopyProviderConfiguration;
import org.ehcache.impl.config.event.CacheEventDispatcherFactoryConfiguration;
import org.ehcache.impl.config.event.DefaultCacheEventDispatcherConfiguration;
import org.ehcache.impl.config.executor.PooledExecutionServiceConfiguration;
import org.ehcache.impl.config.loaderwriter.DefaultCacheLoaderWriterConfiguration;
import org.ehcache.impl.config.loaderwriter.writebehind.WriteBehindProviderConfiguration;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.impl.config.serializer.DefaultSerializerConfiguration;
import org.ehcache.impl.config.store.disk.OffHeapDiskStoreConfiguration;
import org.ehcache.impl.config.store.disk.OffHeapDiskStoreProviderConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineProviderConfiguration;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.ehcache.xml.ConfigurationParser;
import org.ehcache.xml.exceptions.XmlConfigurationException;
import org.ehcache.xml.model.CopierType;
import org.ehcache.xml.model.EventType;
import org.ehcache.xml.model.SerializerType;
import org.ehcache.xml.model.ServiceType;
import org.ehcache.xml.model.ThreadPoolReferenceType;
import org.ehcache.xml.model.ThreadPoolsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/xml/XmlConfiguration.class */
public class XmlConfiguration implements Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) XmlConfiguration.class);
    private final URL xml;
    private final ClassLoader classLoader;
    private final Map<String, ClassLoader> cacheClassLoaders;
    private final Collection<ServiceCreationConfiguration<?>> serviceConfigurations;
    private final Map<String, CacheConfiguration<?, ?>> cacheConfigurations;
    private final Map<String, ConfigurationParser.CacheTemplate> templates;

    public XmlConfiguration(URL url) throws XmlConfigurationException {
        this(url, ClassLoading.getDefaultClassLoader());
    }

    public XmlConfiguration(URL url, ClassLoader classLoader) throws XmlConfigurationException {
        this(url, classLoader, Collections.emptyMap());
    }

    public XmlConfiguration(URL url, ClassLoader classLoader, Map<String, ClassLoader> cacheClassLoaders) throws XmlConfigurationException, JAXBException {
        this.serviceConfigurations = new ArrayList();
        this.cacheConfigurations = new HashMap();
        this.templates = new HashMap();
        if (url == null) {
            throw new NullPointerException("The url can not be null");
        }
        if (classLoader == null) {
            throw new NullPointerException("The classLoader can not be null");
        }
        if (cacheClassLoaders == null) {
            throw new NullPointerException("The cacheClassLoaders map can not be null");
        }
        this.xml = url;
        this.classLoader = classLoader;
        this.cacheClassLoaders = new HashMap(cacheClassLoaders);
        try {
            parseConfiguration();
        } catch (XmlConfigurationException e) {
            throw e;
        } catch (Exception e2) {
            throw new XmlConfigurationException("Error parsing XML configuration at " + url, e2);
        }
    }

    private void parseConfiguration() throws IllegalAccessException, ParserConfigurationException, SAXException, InstantiationException, ClassNotFoundException, IOException, JAXBException {
        WriteBehindConfigurationBuilder writeBehindConfigurationBuilder;
        LOGGER.info("Loading Ehcache XML configuration from {}.", this.xml.getPath());
        ConfigurationParser configurationParser = new ConfigurationParser(this.xml.toExternalForm());
        ArrayList<ServiceCreationConfiguration<?>> serviceConfigs = new ArrayList<>();
        for (ServiceType serviceType : configurationParser.getServiceElements()) {
            ServiceCreationConfiguration<?> serviceConfiguration = configurationParser.parseExtension(serviceType.getServiceCreationConfiguration());
            serviceConfigs.add(serviceConfiguration);
        }
        if (configurationParser.getDefaultSerializers() != null) {
            DefaultSerializationProviderConfiguration configuration = new DefaultSerializationProviderConfiguration();
            for (SerializerType.Serializer serializer : configurationParser.getDefaultSerializers().getSerializer()) {
                configuration.addSerializerFor(getClassForName(serializer.getType(), this.classLoader), getClassForName(serializer.getValue(), this.classLoader));
            }
            serviceConfigs.add(configuration);
        }
        if (configurationParser.getDefaultCopiers() != null) {
            DefaultCopyProviderConfiguration configuration2 = new DefaultCopyProviderConfiguration();
            for (CopierType.Copier copier : configurationParser.getDefaultCopiers().getCopier()) {
                configuration2.addCopierFor(getClassForName(copier.getType(), this.classLoader), getClassForName(copier.getValue(), this.classLoader));
            }
            serviceConfigs.add(configuration2);
        }
        if (configurationParser.getHeapStore() != null) {
            serviceConfigs.add(new DefaultSizeOfEngineProviderConfiguration(configurationParser.getHeapStore().getMaxObjectSize(), configurationParser.getHeapStore().getUnit(), configurationParser.getHeapStore().getMaxObjectGraphSize()));
        }
        if (configurationParser.getPersistence() != null) {
            serviceConfigs.add(new CacheManagerPersistenceConfiguration(new File(configurationParser.getPersistence().getDirectory())));
        }
        if (configurationParser.getThreadPools() != null) {
            PooledExecutionServiceConfiguration poolsConfiguration = new PooledExecutionServiceConfiguration();
            for (ThreadPoolsType.ThreadPool pool : configurationParser.getThreadPools().getThreadPool()) {
                if (pool.isDefault()) {
                    poolsConfiguration.addDefaultPool(pool.getAlias(), pool.getMinSize().intValue(), pool.getMaxSize().intValue());
                } else {
                    poolsConfiguration.addPool(pool.getAlias(), pool.getMinSize().intValue(), pool.getMaxSize().intValue());
                }
            }
            serviceConfigs.add(poolsConfiguration);
        }
        if (configurationParser.getEventDispatch() != null) {
            ThreadPoolReferenceType eventDispatchThreading = configurationParser.getEventDispatch();
            serviceConfigs.add(new CacheEventDispatcherFactoryConfiguration(eventDispatchThreading.getThreadPool()));
        }
        if (configurationParser.getWriteBehind() != null) {
            ThreadPoolReferenceType writeBehindThreading = configurationParser.getWriteBehind();
            serviceConfigs.add(new WriteBehindProviderConfiguration(writeBehindThreading.getThreadPool()));
        }
        if (configurationParser.getDiskStore() != null) {
            ThreadPoolReferenceType diskStoreThreading = configurationParser.getDiskStore();
            serviceConfigs.add(new OffHeapDiskStoreProviderConfiguration(diskStoreThreading.getThreadPool()));
        }
        for (ServiceCreationConfiguration<?> serviceConfiguration2 : Collections.unmodifiableList(serviceConfigs)) {
            this.serviceConfigurations.add(serviceConfiguration2);
        }
        for (ConfigurationParser.CacheDefinition cacheDefinition : configurationParser.getCacheElements()) {
            String alias = cacheDefinition.id();
            if (this.cacheConfigurations.containsKey(alias)) {
                throw new XmlConfigurationException("Two caches defined with the same alias: " + alias);
            }
            ClassLoader cacheClassLoader = this.cacheClassLoaders.get(alias);
            boolean classLoaderConfigured = false;
            if (cacheClassLoader != null) {
                classLoaderConfigured = true;
            }
            if (cacheClassLoader == null) {
                if (this.classLoader != null) {
                    cacheClassLoader = this.classLoader;
                } else {
                    cacheClassLoader = ClassLoading.getDefaultClassLoader();
                }
            }
            Class keyType = getClassForName(cacheDefinition.keyType(), cacheClassLoader);
            Class valueType = getClassForName(cacheDefinition.valueType(), cacheClassLoader);
            ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder();
            for (ResourcePool resourcePool : cacheDefinition.resourcePools()) {
                resourcePoolsBuilder = resourcePoolsBuilder.with(resourcePool);
            }
            CacheConfigurationBuilder<Object, Object> builder = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType, resourcePoolsBuilder);
            if (classLoaderConfigured) {
                builder = builder.withClassLoader(cacheClassLoader);
            }
            if (cacheDefinition.keySerializer() != null) {
                Class keySerializer = getClassForName(cacheDefinition.keySerializer(), cacheClassLoader);
                builder = builder.add(new DefaultSerializerConfiguration<>(keySerializer, DefaultSerializerConfiguration.Type.KEY));
            }
            if (cacheDefinition.keyCopier() != null) {
                Class keyCopier = getClassForName(cacheDefinition.keyCopier(), cacheClassLoader);
                builder = builder.add(new DefaultCopierConfiguration<>(keyCopier, DefaultCopierConfiguration.Type.KEY));
            }
            if (cacheDefinition.valueSerializer() != null) {
                Class valueSerializer = getClassForName(cacheDefinition.valueSerializer(), cacheClassLoader);
                builder = builder.add(new DefaultSerializerConfiguration<>(valueSerializer, DefaultSerializerConfiguration.Type.VALUE));
            }
            if (cacheDefinition.valueCopier() != null) {
                Class valueCopier = getClassForName(cacheDefinition.valueCopier(), cacheClassLoader);
                builder = builder.add(new DefaultCopierConfiguration<>(valueCopier, DefaultCopierConfiguration.Type.VALUE));
            }
            if (cacheDefinition.heapStoreSettings() != null) {
                builder = builder.add(new DefaultSizeOfEngineConfiguration(cacheDefinition.heapStoreSettings().getMaxObjectSize(), cacheDefinition.heapStoreSettings().getUnit(), cacheDefinition.heapStoreSettings().getMaxObjectGraphSize()));
            }
            EvictionAdvisor evictionAdvisor = (EvictionAdvisor) getInstanceOfName(cacheDefinition.evictionAdvisor(), cacheClassLoader, EvictionAdvisor.class);
            CacheConfigurationBuilder<Object, Object> builder2 = builder.withEvictionAdvisor(evictionAdvisor);
            ConfigurationParser.Expiry parsedExpiry = cacheDefinition.expiry();
            if (parsedExpiry != null) {
                builder2 = builder2.withExpiry(getExpiry(cacheClassLoader, parsedExpiry));
            }
            ConfigurationParser.DiskStoreSettings parsedDiskStoreSettings = cacheDefinition.diskStoreSettings();
            if (parsedDiskStoreSettings != null) {
                builder2 = builder2.add(new OffHeapDiskStoreConfiguration(parsedDiskStoreSettings.threadPool(), parsedDiskStoreSettings.writerConcurrency()));
            }
            for (ServiceConfiguration<?> serviceConfig : cacheDefinition.serviceConfigs()) {
                builder2 = builder2.add(serviceConfig);
            }
            if (cacheDefinition.loaderWriter() != null) {
                builder2 = builder2.add(new DefaultCacheLoaderWriterConfiguration(getClassForName(cacheDefinition.loaderWriter(), cacheClassLoader), new Object[0]));
                if (cacheDefinition.writeBehind() != null) {
                    ConfigurationParser.WriteBehind writeBehind = cacheDefinition.writeBehind();
                    if (writeBehind.batching() == null) {
                        writeBehindConfigurationBuilder = WriteBehindConfigurationBuilder.newUnBatchedWriteBehindConfiguration();
                    } else {
                        ConfigurationParser.Batching batching = writeBehind.batching();
                        writeBehindConfigurationBuilder = WriteBehindConfigurationBuilder.newBatchedWriteBehindConfiguration(batching.maxDelay(), batching.maxDelayUnit(), batching.batchSize());
                        if (batching.isCoalesced()) {
                            writeBehindConfigurationBuilder = ((WriteBehindConfigurationBuilder.BatchedWriteBehindConfigurationBuilder) writeBehindConfigurationBuilder).enableCoalescing();
                        }
                    }
                    builder2 = builder2.add(writeBehindConfigurationBuilder.useThreadPool(writeBehind.threadPool()).concurrencyLevel(writeBehind.concurrency()).queueSize(writeBehind.maxQueueSize()));
                }
            }
            CacheConfiguration<?, ?> config = handleListenersConfig(cacheDefinition.listenersConfig(), cacheClassLoader, builder2).build2();
            this.cacheConfigurations.put(alias, config);
        }
        this.templates.putAll(configurationParser.getTemplates());
    }

    private Expiry<? super Object, ? super Object> getExpiry(ClassLoader cacheClassLoader, ConfigurationParser.Expiry parsedExpiry) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Expiry<? super Object, ? super Object> expiry;
        if (parsedExpiry.isUserDef()) {
            expiry = (Expiry) getInstanceOfName(parsedExpiry.type(), cacheClassLoader, Expiry.class);
        } else if (parsedExpiry.isTTL()) {
            expiry = Expirations.timeToLiveExpiration(new Duration(parsedExpiry.value(), parsedExpiry.unit()));
        } else if (parsedExpiry.isTTI()) {
            expiry = Expirations.timeToIdleExpiration(new Duration(parsedExpiry.value(), parsedExpiry.unit()));
        } else {
            expiry = Expirations.noExpiration();
        }
        return expiry;
    }

    private static <T> T getInstanceOfName(String str, ClassLoader classLoader, Class<T> cls) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (str == null) {
            return null;
        }
        return (T) getClassForName(str, classLoader).asSubclass(cls).newInstance();
    }

    private static Class<?> getClassForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return Class.forName(name, true, classLoader);
    }

    public URL getURL() {
        return this.xml;
    }

    public <K, V> CacheConfigurationBuilder<K, V> newCacheConfigurationBuilderFromTemplate(String name, Class<K> keyType, Class<V> valueType) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return internalCacheConfigurationBuilderFromTemplate(name, keyType, valueType, null);
    }

    public <K, V> CacheConfigurationBuilder<K, V> newCacheConfigurationBuilderFromTemplate(String name, Class<K> keyType, Class<V> valueType, ResourcePools resourcePools) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (resourcePools == null || resourcePools.getResourceTypeSet().isEmpty()) {
            throw new IllegalArgumentException("ResourcePools parameter must define at least one resource");
        }
        return internalCacheConfigurationBuilderFromTemplate(name, keyType, valueType, resourcePools);
    }

    public <K, V> CacheConfigurationBuilder<K, V> newCacheConfigurationBuilderFromTemplate(String name, Class<K> keyType, Class<V> valueType, Builder<? extends ResourcePools> resourcePoolsBuilder) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return internalCacheConfigurationBuilderFromTemplate(name, keyType, valueType, resourcePoolsBuilder.build2());
    }

    private <K, V> CacheConfigurationBuilder<K, V> internalCacheConfigurationBuilderFromTemplate(String name, Class<K> keyType, Class<V> valueType, ResourcePools resourcePools) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        CacheConfigurationBuilder<K, V> builder;
        WriteBehindConfigurationBuilder writeBehindConfigurationBuilder;
        ConfigurationParser.CacheTemplate cacheTemplate = this.templates.get(name);
        if (cacheTemplate == null) {
            return null;
        }
        ClassLoader defaultClassLoader = ClassLoading.getDefaultClassLoader();
        Class keyClass = getClassForName(cacheTemplate.keyType(), defaultClassLoader);
        Class valueClass = getClassForName(cacheTemplate.valueType(), defaultClassLoader);
        if (keyType != null && cacheTemplate.keyType() != null && !keyClass.isAssignableFrom(keyType)) {
            throw new IllegalArgumentException("CacheTemplate '" + name + "' declares key type of " + cacheTemplate.keyType());
        }
        if (valueType != null && cacheTemplate.valueType() != null && !valueClass.isAssignableFrom(valueType)) {
            throw new IllegalArgumentException("CacheTemplate '" + name + "' declares value type of " + cacheTemplate.valueType());
        }
        if ((resourcePools == null || resourcePools.getResourceTypeSet().isEmpty()) && cacheTemplate.resourcePools().isEmpty()) {
            throw new IllegalStateException("Template defines no resources, and none were provided");
        }
        if (resourcePools != null) {
            builder = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType, resourcePools);
        } else {
            ResourcePoolsBuilder resourcePoolsBuilder = ResourcePoolsBuilder.newResourcePoolsBuilder();
            for (ResourcePool resourcePool : cacheTemplate.resourcePools()) {
                resourcePoolsBuilder = resourcePoolsBuilder.with(resourcePool);
            }
            builder = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType, resourcePoolsBuilder);
        }
        CacheConfigurationBuilder<K, V> builder2 = builder.withEvictionAdvisor((EvictionAdvisor) getInstanceOfName(cacheTemplate.evictionAdvisor(), defaultClassLoader, EvictionAdvisor.class));
        ConfigurationParser.Expiry parsedExpiry = cacheTemplate.expiry();
        if (parsedExpiry != null) {
            builder2 = builder2.withExpiry(getExpiry(defaultClassLoader, parsedExpiry));
        }
        if (cacheTemplate.keySerializer() != null) {
            builder2 = builder2.add(new DefaultSerializerConfiguration(getClassForName(cacheTemplate.keySerializer(), defaultClassLoader), DefaultSerializerConfiguration.Type.KEY));
        }
        if (cacheTemplate.keyCopier() != null) {
            builder2 = builder2.add(new DefaultCopierConfiguration(getClassForName(cacheTemplate.keyCopier(), defaultClassLoader), DefaultCopierConfiguration.Type.KEY));
        }
        if (cacheTemplate.valueSerializer() != null) {
            builder2 = builder2.add(new DefaultSerializerConfiguration(getClassForName(cacheTemplate.valueSerializer(), defaultClassLoader), DefaultSerializerConfiguration.Type.VALUE));
        }
        if (cacheTemplate.valueCopier() != null) {
            builder2 = builder2.add(new DefaultCopierConfiguration(getClassForName(cacheTemplate.valueCopier(), defaultClassLoader), DefaultCopierConfiguration.Type.VALUE));
        }
        if (cacheTemplate.heapStoreSettings() != null) {
            builder2 = builder2.add(new DefaultSizeOfEngineConfiguration(cacheTemplate.heapStoreSettings().getMaxObjectSize(), cacheTemplate.heapStoreSettings().getUnit(), cacheTemplate.heapStoreSettings().getMaxObjectGraphSize()));
        }
        String loaderWriter = cacheTemplate.loaderWriter();
        if (loaderWriter != null) {
            builder2 = builder2.add(new DefaultCacheLoaderWriterConfiguration(getClassForName(loaderWriter, defaultClassLoader), new Object[0]));
            if (cacheTemplate.writeBehind() != null) {
                ConfigurationParser.WriteBehind writeBehind = cacheTemplate.writeBehind();
                if (writeBehind.batching() == null) {
                    writeBehindConfigurationBuilder = WriteBehindConfigurationBuilder.newUnBatchedWriteBehindConfiguration();
                } else {
                    ConfigurationParser.Batching batching = writeBehind.batching();
                    writeBehindConfigurationBuilder = WriteBehindConfigurationBuilder.newBatchedWriteBehindConfiguration(batching.maxDelay(), batching.maxDelayUnit(), batching.batchSize());
                    if (batching.isCoalesced()) {
                        writeBehindConfigurationBuilder = ((WriteBehindConfigurationBuilder.BatchedWriteBehindConfigurationBuilder) writeBehindConfigurationBuilder).enableCoalescing();
                    }
                }
                builder2 = builder2.add(writeBehindConfigurationBuilder.concurrencyLevel(writeBehind.concurrency()).queueSize(writeBehind.maxQueueSize()));
            }
        }
        CacheConfigurationBuilder<K, V> builder3 = handleListenersConfig(cacheTemplate.listenersConfig(), defaultClassLoader, builder2);
        for (ServiceConfiguration<?> serviceConfiguration : cacheTemplate.serviceConfigs()) {
            builder3 = builder3.add(serviceConfiguration);
        }
        return builder3;
    }

    private <K, V> CacheConfigurationBuilder<K, V> handleListenersConfig(ConfigurationParser.ListenersConfig listenersConfig, ClassLoader defaultClassLoader, CacheConfigurationBuilder<K, V> builder) throws ClassNotFoundException {
        if (listenersConfig != null) {
            if (listenersConfig.threadPool() != null) {
                builder = builder.add(new DefaultCacheEventDispatcherConfiguration(listenersConfig.threadPool()));
            }
            if (listenersConfig.listeners() != null) {
                for (ConfigurationParser.Listener listener : listenersConfig.listeners()) {
                    Class<?> classForName = getClassForName(listener.className(), defaultClassLoader);
                    List<EventType> eventListToFireOn = listener.fireOn();
                    Set<org.ehcache.event.EventType> eventSetToFireOn = new HashSet<>();
                    for (EventType events : eventListToFireOn) {
                        switch (events) {
                            case CREATED:
                                eventSetToFireOn.add(org.ehcache.event.EventType.CREATED);
                                break;
                            case EVICTED:
                                eventSetToFireOn.add(org.ehcache.event.EventType.EVICTED);
                                break;
                            case EXPIRED:
                                eventSetToFireOn.add(org.ehcache.event.EventType.EXPIRED);
                                break;
                            case UPDATED:
                                eventSetToFireOn.add(org.ehcache.event.EventType.UPDATED);
                                break;
                            case REMOVED:
                                eventSetToFireOn.add(org.ehcache.event.EventType.REMOVED);
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid Event Type provided");
                        }
                    }
                    CacheEventListenerConfigurationBuilder listenerBuilder = CacheEventListenerConfigurationBuilder.newEventListenerConfiguration((Class<? extends CacheEventListener<?, ?>>) classForName, eventSetToFireOn).firingMode(EventFiring.valueOf(listener.eventFiring().value())).eventOrdering(EventOrdering.valueOf(listener.eventOrdering().value()));
                    builder = builder.add(listenerBuilder);
                }
            }
        }
        return builder;
    }

    @Override // org.ehcache.config.Configuration
    public Map<String, CacheConfiguration<?, ?>> getCacheConfigurations() {
        return this.cacheConfigurations;
    }

    @Override // org.ehcache.config.Configuration
    public Collection<ServiceCreationConfiguration<?>> getServiceCreationConfigurations() {
        return this.serviceConfigurations;
    }

    @Override // org.ehcache.config.Configuration
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
}
