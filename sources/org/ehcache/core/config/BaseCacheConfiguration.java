package org.ehcache.core.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.config.ResourcePools;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/config/BaseCacheConfiguration.class */
public class BaseCacheConfiguration<K, V> implements CacheConfiguration<K, V> {
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final EvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private final Collection<ServiceConfiguration<?>> serviceConfigurations;
    private final ClassLoader classLoader;
    private final Expiry<? super K, ? super V> expiry;
    private final ResourcePools resourcePools;

    public BaseCacheConfiguration(Class<K> keyType, Class<V> valueType, EvictionAdvisor<? super K, ? super V> evictionAdvisor, ClassLoader classLoader, Expiry<? super K, ? super V> expiry, ResourcePools resourcePools, ServiceConfiguration<?>... serviceConfigurations) {
        if (keyType == null) {
            throw new NullPointerException("keyType cannot be null");
        }
        if (valueType == null) {
            throw new NullPointerException("valueType cannot be null");
        }
        if (resourcePools == null) {
            throw new NullPointerException("resourcePools cannot be null");
        }
        this.keyType = keyType;
        this.valueType = valueType;
        this.evictionAdvisor = evictionAdvisor;
        this.classLoader = classLoader;
        if (expiry != null) {
            this.expiry = expiry;
        } else {
            this.expiry = Expirations.noExpiration();
        }
        this.resourcePools = resourcePools;
        this.serviceConfigurations = Collections.unmodifiableCollection(Arrays.asList(serviceConfigurations));
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Collection<ServiceConfiguration<?>> getServiceConfigurations() {
        return this.serviceConfigurations;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Class<K> getKeyType() {
        return this.keyType;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Class<V> getValueType() {
        return this.valueType;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public EvictionAdvisor<? super K, ? super V> getEvictionAdvisor() {
        return this.evictionAdvisor;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public Expiry<? super K, ? super V> getExpiry() {
        return this.expiry;
    }

    @Override // org.ehcache.config.CacheConfiguration
    public ResourcePools getResourcePools() {
        return this.resourcePools;
    }
}
