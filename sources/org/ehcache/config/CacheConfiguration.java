package org.ehcache.config;

import java.util.Collection;
import org.ehcache.expiry.Expiry;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/CacheConfiguration.class */
public interface CacheConfiguration<K, V> {
    Collection<ServiceConfiguration<?>> getServiceConfigurations();

    Class<K> getKeyType();

    Class<V> getValueType();

    EvictionAdvisor<? super K, ? super V> getEvictionAdvisor();

    ClassLoader getClassLoader();

    Expiry<? super K, ? super V> getExpiry();

    ResourcePools getResourcePools();
}
