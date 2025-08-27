package org.ehcache.core.internal.store;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.core.spi.store.Store;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.serialization.Serializer;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/store/StoreConfigurationImpl.class */
public class StoreConfigurationImpl<K, V> implements Store.Configuration<K, V> {
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final EvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private final ClassLoader classLoader;
    private final Expiry<? super K, ? super V> expiry;
    private final ResourcePools resourcePools;
    private final Serializer<K> keySerializer;
    private final Serializer<V> valueSerializer;
    private final int dispatcherConcurrency;

    public StoreConfigurationImpl(CacheConfiguration<K, V> cacheConfig, int dispatcherConcurrency, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        this(cacheConfig.getKeyType(), cacheConfig.getValueType(), cacheConfig.getEvictionAdvisor(), cacheConfig.getClassLoader(), cacheConfig.getExpiry(), cacheConfig.getResourcePools(), dispatcherConcurrency, keySerializer, valueSerializer);
    }

    public StoreConfigurationImpl(Class<K> keyType, Class<V> valueType, EvictionAdvisor<? super K, ? super V> evictionAdvisor, ClassLoader classLoader, Expiry<? super K, ? super V> expiry, ResourcePools resourcePools, int dispatcherConcurrency, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        this.keyType = keyType;
        this.valueType = valueType;
        this.evictionAdvisor = evictionAdvisor;
        this.classLoader = classLoader;
        this.expiry = expiry;
        this.resourcePools = resourcePools;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
        this.dispatcherConcurrency = dispatcherConcurrency;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public Class<K> getKeyType() {
        return this.keyType;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public Class<V> getValueType() {
        return this.valueType;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public EvictionAdvisor<? super K, ? super V> getEvictionAdvisor() {
        return this.evictionAdvisor;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public Expiry<? super K, ? super V> getExpiry() {
        return this.expiry;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public ResourcePools getResourcePools() {
        return this.resourcePools;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public Serializer<K> getKeySerializer() {
        return this.keySerializer;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public Serializer<V> getValueSerializer() {
        return this.valueSerializer;
    }

    @Override // org.ehcache.core.spi.store.Store.Configuration
    public int getDispatcherConcurrency() {
        return this.dispatcherConcurrency;
    }
}
