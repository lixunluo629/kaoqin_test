package org.ehcache.config.builders;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.StateTransitionException;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.EhcacheManager;
import org.ehcache.impl.config.copy.DefaultCopyProviderConfiguration;
import org.ehcache.impl.config.event.CacheEventDispatcherFactoryConfiguration;
import org.ehcache.impl.config.loaderwriter.writebehind.WriteBehindProviderConfiguration;
import org.ehcache.impl.config.persistence.CacheManagerPersistenceConfiguration;
import org.ehcache.impl.config.serializer.DefaultSerializationProviderConfiguration;
import org.ehcache.impl.config.store.disk.OffHeapDiskStoreProviderConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineProviderConfiguration;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/CacheManagerBuilder.class */
public class CacheManagerBuilder<T extends CacheManager> implements Builder<T> {
    private final ConfigurationBuilder configBuilder;
    private final Set<Service> services;

    public T build(boolean z) throws StateTransitionException {
        T t = (T) newCacheManager(this.services, this.configBuilder.build2());
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

    private CacheManagerBuilder() {
        this.configBuilder = ConfigurationBuilder.newConfigurationBuilder();
        this.services = Collections.emptySet();
    }

    private CacheManagerBuilder(CacheManagerBuilder<T> builder, Set<Service> services) {
        this.configBuilder = builder.configBuilder;
        this.services = Collections.unmodifiableSet(services);
    }

    private CacheManagerBuilder(CacheManagerBuilder<T> builder, ConfigurationBuilder configBuilder) {
        this.configBuilder = configBuilder;
        this.services = builder.services;
    }

    public static CacheManager newCacheManager(Configuration configuration) {
        return new EhcacheManager(configuration);
    }

    T newCacheManager(Collection<Service> collection, Configuration configuration) {
        return (T) cast(new EhcacheManager(configuration, collection));
    }

    T cast(EhcacheManager ehcacheManager) {
        return ehcacheManager;
    }

    public <K, V> CacheManagerBuilder<T> withCache(String alias, CacheConfiguration<K, V> configuration) {
        return new CacheManagerBuilder<>(this, this.configBuilder.addCache(alias, configuration));
    }

    public <K, V> CacheManagerBuilder<T> withCache(String alias, Builder<? extends CacheConfiguration<K, V>> configurationBuilder) {
        return withCache(alias, configurationBuilder.build2());
    }

    public <N extends T> CacheManagerBuilder<N> with(CacheManagerConfiguration<N> cacheManagerConfiguration) {
        return cacheManagerConfiguration.builder(this);
    }

    public <N extends T> CacheManagerBuilder<N> with(Builder<? extends CacheManagerConfiguration<N>> cfgBuilder) {
        return with(cfgBuilder.build2());
    }

    public CacheManagerBuilder<T> using(Service service) {
        Set<Service> newServices = new HashSet<>(this.services);
        newServices.add(service);
        return new CacheManagerBuilder<>(this, newServices);
    }

    public <C> CacheManagerBuilder<T> withCopier(Class<C> clazz, Class<? extends Copier<C>> copier) {
        DefaultCopyProviderConfiguration service = (DefaultCopyProviderConfiguration) this.configBuilder.findServiceByClass(DefaultCopyProviderConfiguration.class);
        if (service == null) {
            DefaultCopyProviderConfiguration service2 = new DefaultCopyProviderConfiguration();
            service2.addCopierFor(clazz, copier);
            return new CacheManagerBuilder<>(this, this.configBuilder.addService(service2));
        }
        DefaultCopyProviderConfiguration newConfig = new DefaultCopyProviderConfiguration(service);
        newConfig.addCopierFor(clazz, copier, true);
        return new CacheManagerBuilder<>(this, this.configBuilder.removeService(service).addService(newConfig));
    }

    public <C> CacheManagerBuilder<T> withSerializer(Class<C> clazz, Class<? extends Serializer<C>> serializer) {
        DefaultSerializationProviderConfiguration service = (DefaultSerializationProviderConfiguration) this.configBuilder.findServiceByClass(DefaultSerializationProviderConfiguration.class);
        if (service == null) {
            DefaultSerializationProviderConfiguration service2 = new DefaultSerializationProviderConfiguration();
            service2.addSerializerFor(clazz, serializer);
            return new CacheManagerBuilder<>(this, this.configBuilder.addService(service2));
        }
        DefaultSerializationProviderConfiguration newConfig = new DefaultSerializationProviderConfiguration(service);
        newConfig.addSerializerFor(clazz, serializer, true);
        return new CacheManagerBuilder<>(this, this.configBuilder.removeService(service).addService(newConfig));
    }

    public CacheManagerBuilder<T> withDefaultSizeOfMaxObjectGraph(long size) {
        DefaultSizeOfEngineProviderConfiguration configuration = (DefaultSizeOfEngineProviderConfiguration) this.configBuilder.findServiceByClass(DefaultSizeOfEngineProviderConfiguration.class);
        if (configuration == null) {
            return new CacheManagerBuilder<>(this, this.configBuilder.addService(new DefaultSizeOfEngineProviderConfiguration(Long.MAX_VALUE, DefaultSizeOfEngineConfiguration.DEFAULT_UNIT, size)));
        }
        ConfigurationBuilder builder = this.configBuilder.removeService(configuration);
        return new CacheManagerBuilder<>(this, builder.addService(new DefaultSizeOfEngineProviderConfiguration(configuration.getMaxObjectSize(), configuration.getUnit(), size)));
    }

    public CacheManagerBuilder<T> withDefaultSizeOfMaxObjectSize(long size, MemoryUnit unit) {
        DefaultSizeOfEngineProviderConfiguration configuration = (DefaultSizeOfEngineProviderConfiguration) this.configBuilder.findServiceByClass(DefaultSizeOfEngineProviderConfiguration.class);
        if (configuration == null) {
            return new CacheManagerBuilder<>(this, this.configBuilder.addService(new DefaultSizeOfEngineProviderConfiguration(size, unit, 1000L)));
        }
        ConfigurationBuilder builder = this.configBuilder.removeService(configuration);
        return new CacheManagerBuilder<>(this, builder.addService(new DefaultSizeOfEngineProviderConfiguration(size, unit, configuration.getMaxObjectGraphSize())));
    }

    public CacheManagerBuilder<T> withDefaultWriteBehindThreadPool(String threadPoolAlias) {
        WriteBehindProviderConfiguration config = (WriteBehindProviderConfiguration) this.configBuilder.findServiceByClass(WriteBehindProviderConfiguration.class);
        if (config == null) {
            return new CacheManagerBuilder<>(this, this.configBuilder.addService(new WriteBehindProviderConfiguration(threadPoolAlias)));
        }
        ConfigurationBuilder builder = this.configBuilder.removeService(config);
        return new CacheManagerBuilder<>(this, builder.addService(new WriteBehindProviderConfiguration(threadPoolAlias)));
    }

    public CacheManagerBuilder<T> withDefaultDiskStoreThreadPool(String threadPoolAlias) {
        OffHeapDiskStoreProviderConfiguration config = (OffHeapDiskStoreProviderConfiguration) this.configBuilder.findServiceByClass(OffHeapDiskStoreProviderConfiguration.class);
        if (config == null) {
            return new CacheManagerBuilder<>(this, this.configBuilder.addService(new OffHeapDiskStoreProviderConfiguration(threadPoolAlias)));
        }
        ConfigurationBuilder builder = this.configBuilder.removeService(config);
        return new CacheManagerBuilder<>(this, builder.addService(new OffHeapDiskStoreProviderConfiguration(threadPoolAlias)));
    }

    public CacheManagerBuilder<T> withDefaultEventListenersThreadPool(String threadPoolAlias) {
        CacheEventDispatcherFactoryConfiguration config = (CacheEventDispatcherFactoryConfiguration) this.configBuilder.findServiceByClass(CacheEventDispatcherFactoryConfiguration.class);
        if (config == null) {
            return new CacheManagerBuilder<>(this, this.configBuilder.addService(new CacheEventDispatcherFactoryConfiguration(threadPoolAlias)));
        }
        ConfigurationBuilder builder = this.configBuilder.removeService(config);
        return new CacheManagerBuilder<>(this, builder.addService(new CacheEventDispatcherFactoryConfiguration(threadPoolAlias)));
    }

    public CacheManagerBuilder<T> using(ServiceCreationConfiguration<?> serviceConfiguration) {
        return new CacheManagerBuilder<>(this, this.configBuilder.addService(serviceConfiguration));
    }

    public CacheManagerBuilder<T> replacing(ServiceCreationConfiguration<?> overwriteServiceConfiguration) {
        ServiceCreationConfiguration existingConfiguration = (ServiceCreationConfiguration) this.configBuilder.findServiceByClass(overwriteServiceConfiguration.getClass());
        return new CacheManagerBuilder<>(this, this.configBuilder.removeService(existingConfiguration).addService(overwriteServiceConfiguration));
    }

    public CacheManagerBuilder<T> withClassLoader(ClassLoader classLoader) {
        return new CacheManagerBuilder<>(this, this.configBuilder.withClassLoader(classLoader));
    }

    public static CacheManagerBuilder<CacheManager> newCacheManagerBuilder() {
        return new CacheManagerBuilder<>();
    }

    public static CacheManagerConfiguration<PersistentCacheManager> persistence(String rootDirectory) {
        return new CacheManagerPersistenceConfiguration(new File(rootDirectory));
    }
}
