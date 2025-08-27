package org.ehcache.config.builders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/ConfigurationBuilder.class */
class ConfigurationBuilder implements Builder<Configuration> {
    private final Map<String, CacheConfiguration<?, ?>> caches;
    private final List<ServiceCreationConfiguration<?>> serviceConfigurations;
    private final ClassLoader classLoader;

    static ConfigurationBuilder newConfigurationBuilder() {
        return new ConfigurationBuilder();
    }

    private ConfigurationBuilder() {
        this.caches = Collections.emptyMap();
        this.serviceConfigurations = Collections.emptyList();
        this.classLoader = null;
    }

    private ConfigurationBuilder(ConfigurationBuilder builder, Map<String, CacheConfiguration<?, ?>> caches) {
        this.caches = Collections.unmodifiableMap(caches);
        this.serviceConfigurations = builder.serviceConfigurations;
        this.classLoader = builder.classLoader;
    }

    private ConfigurationBuilder(ConfigurationBuilder builder, List<ServiceCreationConfiguration<?>> serviceConfigurations) {
        this.caches = builder.caches;
        this.serviceConfigurations = Collections.unmodifiableList(serviceConfigurations);
        this.classLoader = builder.classLoader;
    }

    private ConfigurationBuilder(ConfigurationBuilder builder, ClassLoader classLoader) {
        this.caches = builder.caches;
        this.serviceConfigurations = builder.serviceConfigurations;
        this.classLoader = classLoader;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.config.Builder
    /* renamed from: build */
    public Configuration build2() {
        return new DefaultConfiguration(this.caches, this.classLoader, (ServiceCreationConfiguration[]) this.serviceConfigurations.toArray(new ServiceCreationConfiguration[this.serviceConfigurations.size()]));
    }

    ConfigurationBuilder addCache(String alias, CacheConfiguration<?, ?> config) {
        Map<String, CacheConfiguration<?, ?>> newCaches = new HashMap<>(this.caches);
        if (newCaches.put(alias, config) != null) {
            throw new IllegalArgumentException("Cache alias '" + alias + "' already exists");
        }
        return new ConfigurationBuilder(this, newCaches);
    }

    public ConfigurationBuilder removeCache(String alias) {
        Map<String, CacheConfiguration<?, ?>> newCaches = new HashMap<>(this.caches);
        newCaches.remove(alias);
        return new ConfigurationBuilder(this, newCaches);
    }

    ConfigurationBuilder addService(ServiceCreationConfiguration<?> serviceConfiguration) {
        if (findServiceByClass(serviceConfiguration.getClass()) != null) {
            throw new IllegalArgumentException("There is already a ServiceCreationConfiguration registered for service " + serviceConfiguration.getServiceType() + " of type " + serviceConfiguration.getClass());
        }
        List<ServiceCreationConfiguration<?>> newServiceConfigurations = new ArrayList<>(this.serviceConfigurations);
        newServiceConfigurations.add(serviceConfiguration);
        return new ConfigurationBuilder(this, newServiceConfigurations);
    }

    <T> T findServiceByClass(Class<T> type) {
        for (ServiceCreationConfiguration<?> serviceConfiguration : this.serviceConfigurations) {
            if (serviceConfiguration.getClass().equals(type)) {
                return type.cast(serviceConfiguration);
            }
        }
        return null;
    }

    ConfigurationBuilder removeService(ServiceCreationConfiguration<?> serviceConfiguration) {
        List<ServiceCreationConfiguration<?>> newServiceConfigurations = new ArrayList<>(this.serviceConfigurations);
        newServiceConfigurations.remove(serviceConfiguration);
        return new ConfigurationBuilder(this, newServiceConfigurations);
    }

    ConfigurationBuilder withClassLoader(ClassLoader classLoader) {
        return new ConfigurationBuilder(this, classLoader);
    }
}
