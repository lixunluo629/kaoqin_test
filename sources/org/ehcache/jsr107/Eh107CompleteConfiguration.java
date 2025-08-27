package org.ehcache.jsr107;

import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.Factory;
import javax.cache.expiry.EternalExpiryPolicy;
import javax.cache.expiry.ExpiryPolicy;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheWriter;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.copy.IdentityCopier;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CompleteConfiguration.class */
class Eh107CompleteConfiguration<K, V> extends Eh107Configuration<K, V> implements CompleteConfiguration<K, V> {
    private static final long serialVersionUID = -142083640934760400L;
    private final Class<K> keyType;
    private final Class<V> valueType;
    private final boolean isStoreByValue;
    private final boolean isReadThrough;
    private final boolean isWriteThrough;
    private volatile boolean isStatisticsEnabled;
    private volatile boolean isManagementEnabled;
    private final List<CacheEntryListenerConfiguration<K, V>> cacheEntryListenerConfigs;
    private final Factory<CacheLoader<K, V>> cacheLoaderFactory;
    private final Factory<CacheWriter<? super K, ? super V>> cacheWriterFactory;
    private final Factory<ExpiryPolicy> expiryPolicyFactory;
    private final transient CacheConfiguration<K, V> ehcacheConfig;

    Eh107CompleteConfiguration(Configuration<K, V> config) {
        this(config, null);
    }

    Eh107CompleteConfiguration(Configuration<K, V> config, CacheConfiguration<K, V> ehcacheConfig) {
        this(config, ehcacheConfig, false, false);
    }

    public Eh107CompleteConfiguration(Configuration<K, V> configuration, CacheConfiguration<K, V> cacheConfiguration, boolean z, boolean z2) {
        this.cacheEntryListenerConfigs = new CopyOnWriteArrayList();
        this.ehcacheConfig = cacheConfiguration;
        this.keyType = configuration.getKeyType();
        this.valueType = configuration.getValueType();
        this.isStoreByValue = isStoreByValue(configuration, cacheConfiguration);
        Factory factoryFactoryOf = EternalExpiryPolicy.factoryOf();
        if (configuration instanceof CompleteConfiguration) {
            CompleteConfiguration completeConfiguration = (CompleteConfiguration) configuration;
            this.isReadThrough = completeConfiguration.isReadThrough();
            this.isWriteThrough = completeConfiguration.isWriteThrough();
            this.isStatisticsEnabled = completeConfiguration.isStatisticsEnabled();
            this.isManagementEnabled = completeConfiguration.isManagementEnabled();
            if (z2) {
                this.cacheLoaderFactory = (Factory<CacheLoader<K, V>>) createThrowingFactory();
                this.cacheWriterFactory = (Factory<CacheWriter<? super K, ? super V>>) createThrowingFactory();
            } else {
                this.cacheLoaderFactory = completeConfiguration.getCacheLoaderFactory();
                this.cacheWriterFactory = completeConfiguration.getCacheWriterFactory();
            }
            factoryFactoryOf = completeConfiguration.getExpiryPolicyFactory();
            Iterator it = completeConfiguration.getCacheEntryListenerConfigurations().iterator();
            while (it.hasNext()) {
                this.cacheEntryListenerConfigs.add((CacheEntryListenerConfiguration) it.next());
            }
        } else {
            this.isReadThrough = false;
            this.isWriteThrough = false;
            this.isStatisticsEnabled = false;
            this.isManagementEnabled = false;
            this.cacheLoaderFactory = null;
            this.cacheWriterFactory = null;
        }
        this.expiryPolicyFactory = z ? createThrowingFactory() : factoryFactoryOf;
    }

    private static <K, V> boolean isStoreByValue(Configuration<K, V> config, CacheConfiguration<K, V> ehcacheConfig) {
        if (ehcacheConfig != null) {
            Collection<ServiceConfiguration<?>> serviceConfigurations = ehcacheConfig.getServiceConfigurations();
            for (ServiceConfiguration<?> serviceConfiguration : serviceConfigurations) {
                if (serviceConfiguration instanceof DefaultCopierConfiguration) {
                    DefaultCopierConfiguration<?> copierConfig = (DefaultCopierConfiguration) serviceConfiguration;
                    if (copierConfig.getType().equals(DefaultCopierConfiguration.Type.VALUE)) {
                        if (copierConfig.getClazz().isAssignableFrom(IdentityCopier.class)) {
                            return false;
                        }
                        return true;
                    }
                }
            }
        }
        return config.isStoreByValue();
    }

    public Class<K> getKeyType() {
        return this.keyType;
    }

    public Class<V> getValueType() {
        return this.valueType;
    }

    public boolean isStoreByValue() {
        return this.isStoreByValue;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isReadThrough() {
        return this.isReadThrough;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isWriteThrough() {
        return this.isWriteThrough;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isStatisticsEnabled() {
        return this.isStatisticsEnabled;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isManagementEnabled() {
        return this.isManagementEnabled;
    }

    public Iterable<CacheEntryListenerConfiguration<K, V>> getCacheEntryListenerConfigurations() {
        return Collections.unmodifiableList(this.cacheEntryListenerConfigs);
    }

    public Factory<CacheLoader<K, V>> getCacheLoaderFactory() {
        return this.cacheLoaderFactory;
    }

    public Factory<CacheWriter<? super K, ? super V>> getCacheWriterFactory() {
        return this.cacheWriterFactory;
    }

    public Factory<ExpiryPolicy> getExpiryPolicyFactory() {
        return this.expiryPolicyFactory;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    void setManagementEnabled(boolean isManagementEnabled) {
        this.isManagementEnabled = isManagementEnabled;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    void setStatisticsEnabled(boolean isStatisticsEnabled) {
        this.isStatisticsEnabled = isStatisticsEnabled;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    void addCacheEntryListenerConfiguration(CacheEntryListenerConfiguration<K, V> listenerConfig) {
        this.cacheEntryListenerConfigs.add(listenerConfig);
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    void removeCacheEntryListenerConfiguration(CacheEntryListenerConfiguration<K, V> listenerConfig) {
        this.cacheEntryListenerConfigs.remove(listenerConfig);
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public <T> T unwrap(Class<T> cls) {
        return (T) Unwrap.unwrap(cls, this, this.ehcacheConfig);
    }

    private Object writeReplace() throws ObjectStreamException {
        throw new UnsupportedOperationException("Serialization of Ehcache provider configuration classes is not supported");
    }

    private <T> Factory<T> createThrowingFactory() {
        return new Factory<T>() { // from class: org.ehcache.jsr107.Eh107CompleteConfiguration.1
            public T create() {
                throw new UnsupportedOperationException("Cannot convert from Ehcache type to JSR-107 factory");
            }
        };
    }
}
