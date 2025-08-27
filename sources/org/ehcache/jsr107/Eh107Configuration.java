package org.ehcache.jsr107;

import java.util.Collection;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107Configuration.class */
public abstract class Eh107Configuration<K, V> implements Configuration<K, V> {
    private static final long serialVersionUID = 7324956960962454439L;

    public abstract <T> T unwrap(Class<T> cls);

    abstract boolean isReadThrough();

    abstract boolean isWriteThrough();

    abstract boolean isStatisticsEnabled();

    abstract void setStatisticsEnabled(boolean z);

    abstract boolean isManagementEnabled();

    abstract void setManagementEnabled(boolean z);

    abstract void addCacheEntryListenerConfiguration(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration);

    abstract void removeCacheEntryListenerConfiguration(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration);

    public static <K, V> Configuration<K, V> fromEhcacheCacheConfiguration(CacheConfiguration<K, V> ehcacheConfig) {
        return new Eh107ConfigurationWrapper(ehcacheConfig);
    }

    public static <K, V> Configuration<K, V> fromEhcacheCacheConfiguration(Builder<? extends CacheConfiguration<K, V>> ehcacheConfigBuilder) {
        return new Eh107ConfigurationWrapper(ehcacheConfigBuilder.build());
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107Configuration$Eh107ConfigurationWrapper.class */
    static class Eh107ConfigurationWrapper<K, V> implements Configuration<K, V> {
        private static final long serialVersionUID = -142083549674760400L;
        private final transient CacheConfiguration<K, V> cacheConfiguration;

        private Eh107ConfigurationWrapper(CacheConfiguration<K, V> cacheConfiguration) {
            this.cacheConfiguration = cacheConfiguration;
        }

        CacheConfiguration<K, V> getCacheConfiguration() {
            return this.cacheConfiguration;
        }

        public Class<K> getKeyType() {
            return this.cacheConfiguration.getKeyType();
        }

        public Class<V> getValueType() {
            return this.cacheConfiguration.getValueType();
        }

        public boolean isStoreByValue() {
            Collection<DefaultCopierConfiguration> copierConfig = ServiceUtils.findAmongst(DefaultCopierConfiguration.class, this.cacheConfiguration.getServiceConfigurations());
            return !copierConfig.isEmpty();
        }
    }
}
