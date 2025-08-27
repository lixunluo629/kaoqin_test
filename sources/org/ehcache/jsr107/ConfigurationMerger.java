package org.ehcache.jsr107;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.Factory;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheWriter;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.InternalCache;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.config.copy.DefaultCopyProviderConfiguration;
import org.ehcache.impl.config.loaderwriter.DefaultCacheLoaderWriterConfiguration;
import org.ehcache.impl.copy.SerializingCopier;
import org.ehcache.impl.internal.classes.ClassInstanceConfiguration;
import org.ehcache.jsr107.config.ConfigurationElementState;
import org.ehcache.jsr107.config.Jsr107CacheConfiguration;
import org.ehcache.jsr107.config.Jsr107Service;
import org.ehcache.jsr107.internal.Jsr107CacheLoaderWriter;
import org.ehcache.spi.copy.Copier;
import org.ehcache.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/ConfigurationMerger.class */
class ConfigurationMerger {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) ConfigurationMerger.class);
    private final XmlConfiguration xmlConfiguration;
    private final Jsr107Service jsr107Service;
    private final Eh107CacheLoaderWriterProvider cacheLoaderWriterFactory;

    ConfigurationMerger(Configuration ehConfig, Jsr107Service jsr107Service, Eh107CacheLoaderWriterProvider cacheLoaderWriterFactory) {
        if (ehConfig instanceof XmlConfiguration) {
            this.xmlConfiguration = (XmlConfiguration) ehConfig;
        } else {
            this.xmlConfiguration = null;
        }
        this.jsr107Service = jsr107Service;
        this.cacheLoaderWriterFactory = cacheLoaderWriterFactory;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.Throwable, org.ehcache.jsr107.MultiCacheException] */
    <K, V> ConfigHolder<K, V> mergeConfigurations(String cacheName, javax.cache.configuration.Configuration<K, V> configuration) {
        boolean useEhcacheLoaderWriter;
        CacheConfigurationBuilder<K, V> templateBuilder;
        Eh107CompleteConfiguration<K, V> jsr107Configuration = new Eh107CompleteConfiguration<>(configuration);
        Eh107Expiry<K, V> expiryPolicy = null;
        Jsr107CacheLoaderWriter<K, V> jsr107CacheLoaderWriterInitCacheLoaderWriter = null;
        try {
            CacheConfigurationBuilder<K, V> builder = CacheConfigurationBuilder.newCacheConfigurationBuilder(configuration.getKeyType(), configuration.getValueType(), ResourcePoolsBuilder.heap(Long.MAX_VALUE));
            String templateName = this.jsr107Service.getTemplateNameForCache(cacheName);
            if (this.xmlConfiguration != null && templateName != null) {
                try {
                    templateBuilder = this.xmlConfiguration.newCacheConfigurationBuilderFromTemplate(templateName, jsr107Configuration.getKeyType(), jsr107Configuration.getValueType());
                } catch (IllegalStateException e) {
                    templateBuilder = this.xmlConfiguration.newCacheConfigurationBuilderFromTemplate(templateName, jsr107Configuration.getKeyType(), jsr107Configuration.getValueType(), ResourcePoolsBuilder.heap(Long.MAX_VALUE));
                }
                if (templateBuilder != null) {
                    builder = templateBuilder;
                    LOG.info("Configuration of cache {} will be supplemented by template {}", cacheName, templateName);
                }
            }
            CacheConfigurationBuilder<K, V> builder2 = handleStoreByValue(jsr107Configuration, builder, cacheName);
            boolean hasConfiguredExpiry = builder2.hasConfiguredExpiry();
            if (hasConfiguredExpiry) {
                LOG.info("Cache {} will use expiry configuration from template {}", cacheName, templateName);
            } else {
                expiryPolicy = initExpiryPolicy(jsr107Configuration);
                builder2 = builder2.withExpiry(expiryPolicy);
            }
            DefaultCacheLoaderWriterConfiguration ehcacheLoaderWriterConfiguration = (DefaultCacheLoaderWriterConfiguration) builder2.getExistingServiceConfiguration(DefaultCacheLoaderWriterConfiguration.class);
            if (ehcacheLoaderWriterConfiguration == null) {
                useEhcacheLoaderWriter = false;
                jsr107CacheLoaderWriterInitCacheLoaderWriter = initCacheLoaderWriter(jsr107Configuration, new MultiCacheException());
                if (jsr107CacheLoaderWriterInitCacheLoaderWriter != null && (jsr107Configuration.isReadThrough() || jsr107Configuration.isWriteThrough())) {
                    this.cacheLoaderWriterFactory.registerJsr107Loader(cacheName, jsr107CacheLoaderWriterInitCacheLoaderWriter);
                }
            } else {
                useEhcacheLoaderWriter = true;
                if (!jsr107Configuration.isReadThrough() && !jsr107Configuration.isWriteThrough()) {
                    LOG.warn("Activating Ehcache loader/writer for JSR-107 cache {} which was neither read-through nor write-through", cacheName);
                }
                LOG.info("Cache {} will use loader/writer configuration from template {}", cacheName, templateName);
            }
            CacheConfiguration<K, V> cacheConfiguration = builder2.build2();
            setupManagementAndStatsInternal(jsr107Configuration, (Jsr107CacheConfiguration) ServiceUtils.findSingletonAmongst(Jsr107CacheConfiguration.class, cacheConfiguration.getServiceConfigurations()));
            if (hasConfiguredExpiry) {
                expiryPolicy = new EhcacheExpiryWrapper<>(cacheConfiguration.getExpiry());
            }
            return new ConfigHolder<>(new CacheResources(cacheName, jsr107CacheLoaderWriterInitCacheLoaderWriter, expiryPolicy, initCacheEventListeners(jsr107Configuration)), new Eh107CompleteConfiguration(jsr107Configuration, cacheConfiguration, hasConfiguredExpiry, useEhcacheLoaderWriter), cacheConfiguration, useEhcacheLoaderWriter);
        } catch (Throwable throwable) {
            ?? multiCacheException = new MultiCacheException();
            CacheResources.close(expiryPolicy, multiCacheException);
            CacheResources.close(jsr107CacheLoaderWriterInitCacheLoaderWriter, multiCacheException);
            if (throwable instanceof IllegalArgumentException) {
                String message = throwable.getMessage();
                if (multiCacheException.getMessage() != null) {
                    message = message + "\nSuppressed " + multiCacheException.getMessage();
                }
                throw new IllegalArgumentException(message, throwable);
            }
            multiCacheException.addFirstThrowable(throwable);
            throw multiCacheException;
        }
    }

    private <K, V> CacheConfigurationBuilder<K, V> handleStoreByValue(Eh107CompleteConfiguration<K, V> jsr107Configuration, CacheConfigurationBuilder<K, V> builder, String cacheName) {
        DefaultCopyProviderConfiguration defaultCopyProviderConfiguration;
        DefaultCopierConfiguration copierConfig = (DefaultCopierConfiguration) builder.getExistingServiceConfiguration(DefaultCopierConfiguration.class);
        if (copierConfig == null) {
            if (jsr107Configuration.isStoreByValue()) {
                if (this.xmlConfiguration != null && (defaultCopyProviderConfiguration = (DefaultCopyProviderConfiguration) ServiceUtils.findSingletonAmongst(DefaultCopyProviderConfiguration.class, this.xmlConfiguration.getServiceCreationConfigurations().toArray())) != null) {
                    Map<Class<?>, ClassInstanceConfiguration<Copier<?>>> defaults = defaultCopyProviderConfiguration.getDefaults();
                    handleCopierDefaultsforImmutableTypes(defaults);
                    boolean matchingDefault = false;
                    if (defaults.containsKey(jsr107Configuration.getKeyType())) {
                        matchingDefault = true;
                    } else {
                        builder = builder.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.KEY));
                    }
                    if (defaults.containsKey(jsr107Configuration.getValueType())) {
                        matchingDefault = true;
                    } else {
                        builder = builder.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.VALUE));
                    }
                    if (matchingDefault) {
                        LOG.info("CacheManager level copier configuration overwriting JSR-107 by-value semantics for cache {}", cacheName);
                    }
                    return builder;
                }
                builder = addDefaultCopiers(builder, jsr107Configuration.getKeyType(), jsr107Configuration.getValueType());
                LOG.debug("Using default Copier for JSR-107 store-by-value cache {}", cacheName);
            }
        } else {
            LOG.info("Cache level copier configuration overwriting JSR-107 by-value semantics for cache {}", cacheName);
        }
        return builder;
    }

    private static <K, V> CacheConfigurationBuilder<K, V> addDefaultCopiers(CacheConfigurationBuilder<K, V> builder, Class keyType, Class valueType) {
        CacheConfigurationBuilder<K, V> builder2;
        CacheConfigurationBuilder<K, V> builder3;
        Set<Class> immutableTypes = new HashSet<>();
        immutableTypes.add(String.class);
        immutableTypes.add(Long.class);
        immutableTypes.add(Float.class);
        immutableTypes.add(Double.class);
        immutableTypes.add(Character.class);
        immutableTypes.add(Integer.class);
        if (immutableTypes.contains(keyType)) {
            builder2 = builder.add(new DefaultCopierConfiguration(Eh107IdentityCopier.class, DefaultCopierConfiguration.Type.KEY));
        } else {
            builder2 = builder.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.KEY));
        }
        if (immutableTypes.contains(valueType)) {
            builder3 = builder2.add(new DefaultCopierConfiguration(Eh107IdentityCopier.class, DefaultCopierConfiguration.Type.VALUE));
        } else {
            builder3 = builder2.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.VALUE));
        }
        return builder3;
    }

    private static void handleCopierDefaultsforImmutableTypes(Map<Class<?>, ClassInstanceConfiguration<Copier<?>>> defaults) {
        addIdentityCopierIfNoneRegistered(defaults, Long.class);
        addIdentityCopierIfNoneRegistered(defaults, Integer.class);
        addIdentityCopierIfNoneRegistered(defaults, String.class);
        addIdentityCopierIfNoneRegistered(defaults, Float.class);
        addIdentityCopierIfNoneRegistered(defaults, Double.class);
        addIdentityCopierIfNoneRegistered(defaults, Character.class);
    }

    private static void addIdentityCopierIfNoneRegistered(Map<Class<?>, ClassInstanceConfiguration<Copier<?>>> defaults, Class<?> clazz) {
        if (!defaults.containsKey(clazz)) {
            defaults.put(clazz, new DefaultCopierConfiguration(Eh107IdentityCopier.class, DefaultCopierConfiguration.Type.VALUE));
        }
    }

    private <K, V> Map<CacheEntryListenerConfiguration<K, V>, ListenerResources<K, V>> initCacheEventListeners(CompleteConfiguration<K, V> config) {
        Map<CacheEntryListenerConfiguration<K, V>, ListenerResources<K, V>> listenerResources = new ConcurrentHashMap<>();
        MultiCacheException mce = new MultiCacheException();
        for (CacheEntryListenerConfiguration<K, V> listenerConfig : config.getCacheEntryListenerConfigurations()) {
            listenerResources.put(listenerConfig, ListenerResources.createListenerResources(listenerConfig, mce));
        }
        return listenerResources;
    }

    private <K, V> Eh107Expiry<K, V> initExpiryPolicy(CompleteConfiguration<K, V> config) {
        return new ExpiryPolicyToEhcacheExpiry((ExpiryPolicy) config.getExpiryPolicyFactory().create());
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <K, V> Jsr107CacheLoaderWriter<K, V> initCacheLoaderWriter(CompleteConfiguration<K, V> config, MultiCacheException multiCacheException) {
        CacheWriter<K, V> cacheWriter;
        Factory<CacheLoader<K, V>> cacheLoaderFactory = config.getCacheLoaderFactory();
        Factory<CacheWriter<K, V>> cacheWriterFactory = config.getCacheWriterFactory();
        if (config.isReadThrough() && cacheLoaderFactory == null) {
            throw new IllegalArgumentException("read-through enabled without a CacheLoader factory provided");
        }
        if (config.isWriteThrough() && cacheWriterFactory == null) {
            throw new IllegalArgumentException("write-through enabled without a CacheWriter factory provided");
        }
        CacheLoader<K, V> cacheLoader = cacheLoaderFactory == null ? null : (CacheLoader) cacheLoaderFactory.create();
        if (cacheWriterFactory == null) {
            cacheWriter = null;
        } else {
            try {
                cacheWriter = (CacheWriter) cacheWriterFactory.create();
            } catch (Throwable t) {
                if (t != multiCacheException) {
                    multiCacheException.addThrowable(t);
                }
                CacheResources.close(cacheLoader, multiCacheException);
                throw multiCacheException;
            }
        }
        CacheWriter<K, V> cacheWriter2 = cacheWriter;
        if (cacheLoader == null && cacheWriter2 == null) {
            return null;
        }
        return new Eh107CacheLoaderWriter(cacheLoader, config.isReadThrough(), cacheWriter2, config.isWriteThrough());
    }

    void setUpManagementAndStats(InternalCache<?, ?> cache, Eh107Configuration<?, ?> configuration) {
        Jsr107CacheConfiguration cacheConfiguration = (Jsr107CacheConfiguration) ServiceUtils.findSingletonAmongst(Jsr107CacheConfiguration.class, cache.getRuntimeConfiguration().getServiceConfigurations());
        setupManagementAndStatsInternal(configuration, cacheConfiguration);
    }

    private void setupManagementAndStatsInternal(Eh107Configuration<?, ?> configuration, Jsr107CacheConfiguration cacheConfiguration) {
        ConfigurationElementState enableManagement = this.jsr107Service.isManagementEnabledOnAllCaches();
        ConfigurationElementState enableStatistics = this.jsr107Service.isStatisticsEnabledOnAllCaches();
        if (cacheConfiguration != null) {
            ConfigurationElementState managementEnabled = cacheConfiguration.isManagementEnabled();
            if (managementEnabled != null && managementEnabled != ConfigurationElementState.UNSPECIFIED) {
                enableManagement = managementEnabled;
            }
            ConfigurationElementState statisticsEnabled = cacheConfiguration.isStatisticsEnabled();
            if (statisticsEnabled != null && statisticsEnabled != ConfigurationElementState.UNSPECIFIED) {
                enableStatistics = statisticsEnabled;
            }
        }
        if (enableManagement != null && enableManagement != ConfigurationElementState.UNSPECIFIED) {
            configuration.setManagementEnabled(enableManagement.asBoolean());
        }
        if (enableStatistics != null && enableStatistics != ConfigurationElementState.UNSPECIFIED) {
            configuration.setStatisticsEnabled(enableStatistics.asBoolean());
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/ConfigurationMerger$ConfigHolder.class */
    static class ConfigHolder<K, V> {
        final CacheResources<K, V> cacheResources;
        final CacheConfiguration<K, V> cacheConfiguration;
        final Eh107CompleteConfiguration<K, V> jsr107Configuration;
        final boolean useEhcacheLoaderWriter;

        public ConfigHolder(CacheResources<K, V> cacheResources, Eh107CompleteConfiguration<K, V> jsr107Configuration, CacheConfiguration<K, V> cacheConfiguration, boolean useEhcacheLoaderWriter) {
            this.cacheResources = cacheResources;
            this.jsr107Configuration = jsr107Configuration;
            this.cacheConfiguration = cacheConfiguration;
            this.useEhcacheLoaderWriter = useEhcacheLoaderWriter;
        }
    }
}
