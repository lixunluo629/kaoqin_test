package org.ehcache.core.config;

import com.itextpdf.layout.element.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.Configuration;
import org.ehcache.core.HumanReadable;
import org.ehcache.core.internal.util.ClassLoading;
import org.ehcache.spi.service.ServiceCreationConfiguration;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/config/DefaultConfiguration.class */
public final class DefaultConfiguration implements Configuration, HumanReadable {
    private final ConcurrentMap<String, CacheConfiguration<?, ?>> caches;
    private final Collection<ServiceCreationConfiguration<?>> services;
    private final ClassLoader classLoader;

    public DefaultConfiguration(Configuration cfg) {
        if (cfg.getClassLoader() == null) {
            throw new NullPointerException();
        }
        this.caches = new ConcurrentHashMap(cfg.getCacheConfigurations());
        this.services = Collections.unmodifiableCollection(cfg.getServiceCreationConfigurations());
        this.classLoader = cfg.getClassLoader();
    }

    public DefaultConfiguration(ClassLoader classLoader, ServiceCreationConfiguration<?>... services) {
        this(emptyCacheMap(), classLoader, services);
    }

    public DefaultConfiguration(Map<String, CacheConfiguration<?, ?>> caches, ClassLoader classLoader, ServiceCreationConfiguration<?>... services) {
        this.services = Collections.unmodifiableCollection(Arrays.asList(services));
        this.caches = new ConcurrentHashMap(caches);
        this.classLoader = classLoader == null ? ClassLoading.getDefaultClassLoader() : classLoader;
    }

    @Override // org.ehcache.config.Configuration
    public Map<String, CacheConfiguration<?, ?>> getCacheConfigurations() {
        return Collections.unmodifiableMap(this.caches);
    }

    @Override // org.ehcache.config.Configuration
    public Collection<ServiceCreationConfiguration<?>> getServiceCreationConfigurations() {
        return this.services;
    }

    @Override // org.ehcache.config.Configuration
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    private static Map<String, CacheConfiguration<?, ?>> emptyCacheMap() {
        return Collections.emptyMap();
    }

    public void addCacheConfiguration(String alias, CacheConfiguration<?, ?> config) {
        if (this.caches.put(alias, config) != null) {
            throw new IllegalStateException("Cache '" + alias + "' already present!");
        }
    }

    public void removeCacheConfiguration(String alias) {
        this.caches.remove(alias);
    }

    public <K, V> void replaceCacheConfiguration(String alias, CacheConfiguration<K, V> config, CacheRuntimeConfiguration<K, V> runtimeConfiguration) {
        if (!this.caches.replace(alias, config, runtimeConfiguration)) {
            throw new IllegalStateException("The expected configuration doesn't match!");
        }
    }

    @Override // org.ehcache.core.HumanReadable
    public String readableString() {
        StringBuilder cachesToStringBuilder = new StringBuilder();
        for (Map.Entry<String, CacheConfiguration<?, ?>> cacheConfigurationEntry : this.caches.entrySet()) {
            if (cacheConfigurationEntry.getValue() instanceof HumanReadable) {
                cachesToStringBuilder.append(cacheConfigurationEntry.getKey()).append(":\n    ").append(((HumanReadable) cacheConfigurationEntry.getValue()).readableString().replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\n    ")).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        if (cachesToStringBuilder.length() > 0) {
            cachesToStringBuilder.deleteCharAt(cachesToStringBuilder.length() - 1);
        }
        StringBuilder serviceCreationConfigurationsToStringBuilder = new StringBuilder();
        for (ServiceCreationConfiguration<?> serviceCreationConfiguration : this.services) {
            serviceCreationConfigurationsToStringBuilder.append(List.DEFAULT_LIST_SYMBOL);
            if (serviceCreationConfiguration instanceof HumanReadable) {
                serviceCreationConfigurationsToStringBuilder.append(((HumanReadable) serviceCreationConfiguration).readableString()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            } else {
                serviceCreationConfigurationsToStringBuilder.append(serviceCreationConfiguration.getClass().getName()).append(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            }
        }
        if (serviceCreationConfigurationsToStringBuilder.length() > 0) {
            serviceCreationConfigurationsToStringBuilder.deleteCharAt(serviceCreationConfigurationsToStringBuilder.length() - 1);
        }
        return "caches:\n    " + cachesToStringBuilder.toString().replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\n    ") + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR + "services: \n    " + serviceCreationConfigurationsToStringBuilder.toString().replace(ScriptUtils.FALLBACK_STATEMENT_SEPARATOR, "\n    ");
    }
}
