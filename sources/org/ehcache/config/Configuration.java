package org.ehcache.config;

import java.util.Collection;
import java.util.Map;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/Configuration.class */
public interface Configuration {
    Map<String, CacheConfiguration<?, ?>> getCacheConfigurations();

    Collection<ServiceCreationConfiguration<?>> getServiceCreationConfigurations();

    ClassLoader getClassLoader();
}
