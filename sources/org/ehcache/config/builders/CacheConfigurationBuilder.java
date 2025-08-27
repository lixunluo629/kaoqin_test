package org.ehcache.config.builders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.BaseCacheConfiguration;
import org.ehcache.expiry.Expiry;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.config.event.DefaultCacheEventDispatcherConfiguration;
import org.ehcache.impl.config.event.DefaultCacheEventListenerConfiguration;
import org.ehcache.impl.config.event.DefaultEventSourceConfiguration;
import org.ehcache.impl.config.loaderwriter.DefaultCacheLoaderWriterConfiguration;
import org.ehcache.impl.config.serializer.DefaultSerializerConfiguration;
import org.ehcache.impl.config.store.disk.OffHeapDiskStoreConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.impl.copy.SerializingCopier;
import org.ehcache.spi.copy.Copier;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/CacheConfigurationBuilder.class */
public class CacheConfigurationBuilder<K, V> implements Builder<CacheConfiguration<K, V>> {
    private final Collection<ServiceConfiguration<?>> serviceConfigurations;
    private Expiry<? super K, ? super V> expiry;
    private ClassLoader classLoader;
    private EvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private ResourcePools resourcePools;
    private Class<K> keyType;
    private Class<V> valueType;

    public static <K, V> CacheConfigurationBuilder<K, V> newCacheConfigurationBuilder(Class<K> keyType, Class<V> valueType, ResourcePools resourcePools) {
        return new CacheConfigurationBuilder<>(keyType, valueType, resourcePools);
    }

    public static <K, V> CacheConfigurationBuilder<K, V> newCacheConfigurationBuilder(Class<K> keyType, Class<V> valueType, Builder<? extends ResourcePools> resourcePoolsBuilder) {
        return new CacheConfigurationBuilder<>(keyType, valueType, resourcePoolsBuilder.build2());
    }

    public static <K, V> CacheConfigurationBuilder<K, V> newCacheConfigurationBuilder(CacheConfiguration<K, V> configuration) {
        CacheConfigurationBuilder<K, V> builder = newCacheConfigurationBuilder(configuration.getKeyType(), configuration.getValueType(), configuration.getResourcePools()).withClassLoader(configuration.getClassLoader()).withEvictionAdvisor(configuration.getEvictionAdvisor()).withExpiry(configuration.getExpiry());
        for (ServiceConfiguration<?> serviceConfig : configuration.getServiceConfigurations()) {
            builder = builder.add(serviceConfig);
        }
        return builder;
    }

    private CacheConfigurationBuilder(Class<K> keyType, Class<V> valueType, ResourcePools resourcePools) {
        this.serviceConfigurations = new HashSet();
        this.classLoader = null;
        this.keyType = keyType;
        this.valueType = valueType;
        this.resourcePools = resourcePools;
    }

    private CacheConfigurationBuilder(CacheConfigurationBuilder<K, V> other) {
        this.serviceConfigurations = new HashSet();
        this.classLoader = null;
        this.keyType = other.keyType;
        this.valueType = other.valueType;
        this.expiry = other.expiry;
        this.classLoader = other.classLoader;
        this.evictionAdvisor = other.evictionAdvisor;
        this.resourcePools = other.resourcePools;
        this.serviceConfigurations.addAll(other.serviceConfigurations);
    }

    public CacheConfigurationBuilder<K, V> add(ServiceConfiguration<?> configuration) {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        if (getExistingServiceConfiguration(configuration.getClass()) != null) {
            if (configuration instanceof DefaultCopierConfiguration) {
                DefaultCopierConfiguration copierConfiguration = (DefaultCopierConfiguration) configuration;
                removeExistingCopierConfigFor(copierConfiguration.getType(), otherBuilder);
            } else if (configuration instanceof DefaultSerializerConfiguration) {
                DefaultSerializerConfiguration serializerConfiguration = (DefaultSerializerConfiguration) configuration;
                removeExistingSerializerConfigFor(serializerConfiguration.getType(), otherBuilder);
            } else if (!(configuration instanceof DefaultCacheEventListenerConfiguration)) {
                throw new IllegalStateException("Cannot add a generic service configuration when another one already exists. Rely on specific with* methods or make sure your remove other configuration first.");
            }
        }
        otherBuilder.serviceConfigurations.add(configuration);
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> add(Builder<? extends ServiceConfiguration<?>> configurationBuilder) {
        return add(configurationBuilder.build2());
    }

    public CacheConfigurationBuilder<K, V> withEvictionAdvisor(EvictionAdvisor<? super K, ? super V> evictionAdvisor) {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        otherBuilder.evictionAdvisor = evictionAdvisor;
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> remove(ServiceConfiguration<?> configuration) {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        otherBuilder.serviceConfigurations.remove(configuration);
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> clearAllServiceConfig() {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        otherBuilder.serviceConfigurations.clear();
        return otherBuilder;
    }

    public <T extends ServiceConfiguration<?>> T getExistingServiceConfiguration(Class<T> clazz) {
        for (ServiceConfiguration<?> serviceConfiguration : this.serviceConfigurations) {
            if (clazz.equals(serviceConfiguration.getClass())) {
                return clazz.cast(serviceConfiguration);
            }
        }
        return null;
    }

    public <T extends ServiceConfiguration<?>> List<T> getExistingServiceConfigurations(Class<T> clazz) {
        ArrayList<T> results = new ArrayList<>();
        for (ServiceConfiguration<?> serviceConfiguration : this.serviceConfigurations) {
            if (clazz.equals(serviceConfiguration.getClass())) {
                results.add(clazz.cast(serviceConfiguration));
            }
        }
        return results;
    }

    public CacheConfigurationBuilder<K, V> withClassLoader(ClassLoader classLoader) {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        otherBuilder.classLoader = classLoader;
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withResourcePools(ResourcePools resourcePools) {
        if (resourcePools == null) {
            throw new NullPointerException("Null resource pools");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        otherBuilder.resourcePools = resourcePools;
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withResourcePools(ResourcePoolsBuilder resourcePoolsBuilder) {
        if (resourcePoolsBuilder == null) {
            throw new NullPointerException("Null resource pools builder");
        }
        return withResourcePools(resourcePoolsBuilder.build2());
    }

    public CacheConfigurationBuilder<K, V> withExpiry(Expiry<? super K, ? super V> expiry) {
        if (expiry == null) {
            throw new NullPointerException("Null expiry");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        otherBuilder.expiry = expiry;
        return otherBuilder;
    }

    public boolean hasConfiguredExpiry() {
        return this.expiry != null;
    }

    public CacheConfigurationBuilder<K, V> withLoaderWriter(CacheLoaderWriter<K, V> loaderWriter) {
        if (loaderWriter == null) {
            throw new NullPointerException("Null loaderWriter");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        DefaultCacheLoaderWriterConfiguration existingServiceConfiguration = (DefaultCacheLoaderWriterConfiguration) otherBuilder.getExistingServiceConfiguration(DefaultCacheLoaderWriterConfiguration.class);
        if (existingServiceConfiguration != null) {
            otherBuilder.serviceConfigurations.remove(existingServiceConfiguration);
        }
        otherBuilder.serviceConfigurations.add(new DefaultCacheLoaderWriterConfiguration(loaderWriter));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withLoaderWriter(Class<CacheLoaderWriter<K, V>> loaderWriterClass, Object... arguments) {
        if (loaderWriterClass == null) {
            throw new NullPointerException("Null loaderWriterClass");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        DefaultCacheLoaderWriterConfiguration existingServiceConfiguration = (DefaultCacheLoaderWriterConfiguration) otherBuilder.getExistingServiceConfiguration(DefaultCacheLoaderWriterConfiguration.class);
        if (existingServiceConfiguration != null) {
            otherBuilder.serviceConfigurations.remove(existingServiceConfiguration);
        }
        otherBuilder.serviceConfigurations.add(new DefaultCacheLoaderWriterConfiguration(loaderWriterClass, arguments));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withKeySerializingCopier() {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingCopierConfigFor(DefaultCopierConfiguration.Type.KEY, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.KEY));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withValueSerializingCopier() {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingCopierConfigFor(DefaultCopierConfiguration.Type.VALUE, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultCopierConfiguration(SerializingCopier.asCopierClass(), DefaultCopierConfiguration.Type.VALUE));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withKeyCopier(Copier<K> keyCopier) {
        if (keyCopier == null) {
            throw new NullPointerException("Null key copier");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingCopierConfigFor(DefaultCopierConfiguration.Type.KEY, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultCopierConfiguration(keyCopier, DefaultCopierConfiguration.Type.KEY));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withKeyCopier(Class<? extends Copier<K>> keyCopierClass) {
        if (keyCopierClass == null) {
            throw new NullPointerException("Null key copier class");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingCopierConfigFor(DefaultCopierConfiguration.Type.KEY, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultCopierConfiguration(keyCopierClass, DefaultCopierConfiguration.Type.KEY));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withValueCopier(Copier<V> valueCopier) {
        if (valueCopier == null) {
            throw new NullPointerException("Null value copier");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingCopierConfigFor(DefaultCopierConfiguration.Type.VALUE, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultCopierConfiguration(valueCopier, DefaultCopierConfiguration.Type.VALUE));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withValueCopier(Class<? extends Copier<V>> valueCopierClass) {
        if (valueCopierClass == null) {
            throw new NullPointerException("Null value copier");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingCopierConfigFor(DefaultCopierConfiguration.Type.VALUE, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultCopierConfiguration(valueCopierClass, DefaultCopierConfiguration.Type.VALUE));
        return otherBuilder;
    }

    private void removeExistingCopierConfigFor(DefaultCopierConfiguration.Type type, CacheConfigurationBuilder<K, V> otherBuilder) {
        for (T configuration : otherBuilder.getExistingServiceConfigurations(DefaultCopierConfiguration.class)) {
            if (configuration.getType().equals(type)) {
                otherBuilder.serviceConfigurations.remove(configuration);
            }
        }
    }

    private void removeExistingSerializerConfigFor(DefaultSerializerConfiguration.Type type, CacheConfigurationBuilder<K, V> otherBuilder) {
        for (T configuration : otherBuilder.getExistingServiceConfigurations(DefaultSerializerConfiguration.class)) {
            if (configuration.getType().equals(type)) {
                otherBuilder.serviceConfigurations.remove(configuration);
            }
        }
    }

    public CacheConfigurationBuilder<K, V> withKeySerializer(Serializer<K> keySerializer) {
        if (keySerializer == null) {
            throw new NullPointerException("Null key serializer");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingSerializerConfigFor(DefaultSerializerConfiguration.Type.KEY, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultSerializerConfiguration(keySerializer, DefaultSerializerConfiguration.Type.KEY));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withKeySerializer(Class<? extends Serializer<K>> keySerializerClass) {
        if (keySerializerClass == null) {
            throw new NullPointerException("Null key serializer class");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingSerializerConfigFor(DefaultSerializerConfiguration.Type.KEY, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultSerializerConfiguration(keySerializerClass, DefaultSerializerConfiguration.Type.KEY));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withValueSerializer(Serializer<V> valueSerializer) {
        if (valueSerializer == null) {
            throw new NullPointerException("Null value serializer");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingSerializerConfigFor(DefaultSerializerConfiguration.Type.VALUE, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultSerializerConfiguration(valueSerializer, DefaultSerializerConfiguration.Type.VALUE));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withValueSerializer(Class<? extends Serializer<V>> valueSerializerClass) {
        if (valueSerializerClass == null) {
            throw new NullPointerException("Null value serializer class");
        }
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        removeExistingSerializerConfigFor(DefaultSerializerConfiguration.Type.VALUE, otherBuilder);
        otherBuilder.serviceConfigurations.add(new DefaultSerializerConfiguration(valueSerializerClass, DefaultSerializerConfiguration.Type.VALUE));
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withDispatcherConcurrency(int dispatcherConcurrency) {
        DefaultEventSourceConfiguration configuration = new DefaultEventSourceConfiguration(dispatcherConcurrency);
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        DefaultEventSourceConfiguration existingServiceConfiguration = (DefaultEventSourceConfiguration) otherBuilder.getExistingServiceConfiguration(DefaultEventSourceConfiguration.class);
        if (existingServiceConfiguration != null) {
            otherBuilder.serviceConfigurations.remove(existingServiceConfiguration);
        }
        otherBuilder.serviceConfigurations.add(configuration);
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withEventListenersThreadPool(String threadPoolAlias) {
        DefaultCacheEventDispatcherConfiguration configuration = new DefaultCacheEventDispatcherConfiguration(threadPoolAlias);
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        DefaultCacheEventDispatcherConfiguration existingServiceConfiguration = (DefaultCacheEventDispatcherConfiguration) otherBuilder.getExistingServiceConfiguration(DefaultCacheEventDispatcherConfiguration.class);
        if (existingServiceConfiguration != null) {
            otherBuilder.serviceConfigurations.remove(existingServiceConfiguration);
        }
        otherBuilder.serviceConfigurations.add(configuration);
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withDiskStoreThreadPool(String threadPoolAlias, int concurrency) {
        OffHeapDiskStoreConfiguration configuration = new OffHeapDiskStoreConfiguration(threadPoolAlias, concurrency);
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        OffHeapDiskStoreConfiguration existingServiceConfiguration = (OffHeapDiskStoreConfiguration) getExistingServiceConfiguration(OffHeapDiskStoreConfiguration.class);
        if (existingServiceConfiguration != null) {
            otherBuilder.serviceConfigurations.remove(existingServiceConfiguration);
        }
        otherBuilder.serviceConfigurations.add(configuration);
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withSizeOfMaxObjectGraph(long size) {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        DefaultSizeOfEngineConfiguration configuration = (DefaultSizeOfEngineConfiguration) otherBuilder.getExistingServiceConfiguration(DefaultSizeOfEngineConfiguration.class);
        if (configuration == null) {
            otherBuilder.serviceConfigurations.add(new DefaultSizeOfEngineConfiguration(Long.MAX_VALUE, DefaultSizeOfEngineConfiguration.DEFAULT_UNIT, size));
        } else {
            otherBuilder.serviceConfigurations.remove(configuration);
            otherBuilder.serviceConfigurations.add(new DefaultSizeOfEngineConfiguration(configuration.getMaxObjectSize(), configuration.getUnit(), size));
        }
        return otherBuilder;
    }

    public CacheConfigurationBuilder<K, V> withSizeOfMaxObjectSize(long size, MemoryUnit unit) {
        CacheConfigurationBuilder<K, V> otherBuilder = new CacheConfigurationBuilder<>(this);
        DefaultSizeOfEngineConfiguration configuration = (DefaultSizeOfEngineConfiguration) getExistingServiceConfiguration(DefaultSizeOfEngineConfiguration.class);
        if (configuration == null) {
            otherBuilder.serviceConfigurations.add(new DefaultSizeOfEngineConfiguration(size, unit, 1000L));
        } else {
            otherBuilder.serviceConfigurations.remove(configuration);
            otherBuilder.serviceConfigurations.add(new DefaultSizeOfEngineConfiguration(size, unit, configuration.getMaxObjectGraphSize()));
        }
        return otherBuilder;
    }

    @Override // org.ehcache.config.Builder
    /* renamed from: build */
    public CacheConfiguration<K, V> build2() {
        return new BaseCacheConfiguration(this.keyType, this.valueType, this.evictionAdvisor, this.classLoader, this.expiry, this.resourcePools, (ServiceConfiguration[]) this.serviceConfigurations.toArray(new ServiceConfiguration[this.serviceConfigurations.size()]));
    }
}
