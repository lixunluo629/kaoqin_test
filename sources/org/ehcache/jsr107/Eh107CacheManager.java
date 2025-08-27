package org.ehcache.jsr107;

import java.lang.management.ManagementFactory;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import org.ehcache.Cache;
import org.ehcache.Status;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.core.EhcacheManager;
import org.ehcache.core.InternalCache;
import org.ehcache.impl.config.copy.DefaultCopierConfiguration;
import org.ehcache.impl.copy.IdentityCopier;
import org.ehcache.jsr107.ConfigurationMerger;
import org.ehcache.jsr107.Eh107Configuration;
import org.ehcache.jsr107.internal.Jsr107CacheLoaderWriter;
import org.ehcache.jsr107.internal.WrappedCacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.service.ServiceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessor;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheManager.class */
class Eh107CacheManager implements CacheManager {
    private static final Logger LOG;
    private static MBeanServer MBEAN_SERVER;
    private final Object cachesLock = new Object();
    private final ConcurrentMap<String, Eh107Cache<?, ?>> caches = new ConcurrentHashMap();
    private final EhcacheManager ehCacheManager;
    private final EhcacheCachingProvider cachingProvider;
    private final ClassLoader classLoader;
    private final URI uri;
    private final Properties props;
    private final ConfigurationMerger configurationMerger;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Eh107CacheManager.class.desiredAssertionStatus();
        LOG = LoggerFactory.getLogger((Class<?>) Eh107CacheManager.class);
        MBEAN_SERVER = ManagementFactory.getPlatformMBeanServer();
    }

    Eh107CacheManager(EhcacheCachingProvider cachingProvider, EhcacheManager ehCacheManager, Properties props, ClassLoader classLoader, URI uri, ConfigurationMerger configurationMerger) {
        this.cachingProvider = cachingProvider;
        this.ehCacheManager = ehCacheManager;
        this.props = props;
        this.classLoader = classLoader;
        this.uri = uri;
        this.configurationMerger = configurationMerger;
        refreshAllCaches();
    }

    EhcacheManager getEhCacheManager() {
        return this.ehCacheManager;
    }

    private void refreshAllCaches() {
        for (Map.Entry<String, CacheConfiguration<?, ?>> entry : this.ehCacheManager.getRuntimeConfiguration().getCacheConfigurations().entrySet()) {
            String name = entry.getKey();
            CacheConfiguration<?, ?> config = entry.getValue();
            this.caches.putIfAbsent(name, wrapEhcacheCache(name, config));
        }
        for (Map.Entry<String, Eh107Cache<?, ?>> namedCacheEntry : this.caches.entrySet()) {
            Eh107Cache<?, ?> cache = namedCacheEntry.getValue();
            if (!cache.isClosed()) {
                Eh107Configuration<?, ?> configuration = (Eh107Configuration) cache.getConfiguration(Eh107Configuration.class);
                if (configuration.isManagementEnabled()) {
                    enableManagement(cache, true);
                }
                if (configuration.isStatisticsEnabled()) {
                    enableStatistics(cache, true);
                }
            }
        }
    }

    private <K, V> Eh107Cache<K, V> wrapEhcacheCache(String alias, CacheConfiguration<K, V> ehConfig) {
        Cache<K, V> cache = this.ehCacheManager.getCache(alias, ehConfig.getKeyType(), ehConfig.getValueType());
        return wrapEhcacheCache(alias, (InternalCache) cache);
    }

    private <K, V> Eh107Cache<K, V> wrapEhcacheCache(String alias, InternalCache<K, V> cache) {
        CacheLoaderWriter<K, V> cacheLoaderWriter = cache.getCacheLoaderWriter();
        boolean storeByValueOnHeap = false;
        Iterator i$ = cache.getRuntimeConfiguration().getServiceConfigurations().iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            ServiceConfiguration<?> serviceConfiguration = i$.next();
            if (serviceConfiguration instanceof DefaultCopierConfiguration) {
                DefaultCopierConfiguration<?> copierConfig = (DefaultCopierConfiguration) serviceConfiguration;
                if (!copierConfig.getClazz().isAssignableFrom(IdentityCopier.class)) {
                    storeByValueOnHeap = true;
                }
            }
        }
        Eh107Configuration<K, V> config = new Eh107ReverseConfiguration<>(cache, cacheLoaderWriter != null, cacheLoaderWriter != null, storeByValueOnHeap);
        this.configurationMerger.setUpManagementAndStats(cache, config);
        Eh107Expiry<K, V> expiry = new EhcacheExpiryWrapper<>(cache.getRuntimeConfiguration().getExpiry());
        CacheResources<K, V> resources = new CacheResources<>(alias, wrapCacheLoaderWriter(cacheLoaderWriter), expiry);
        return new Eh107Cache<>(alias, config, resources, cache, this);
    }

    private <K, V> Jsr107CacheLoaderWriter<K, V> wrapCacheLoaderWriter(CacheLoaderWriter<K, V> cacheLoaderWriter) {
        return new WrappedCacheLoaderWriter(cacheLoaderWriter);
    }

    public CachingProvider getCachingProvider() {
        return this.cachingProvider;
    }

    public URI getURI() {
        return this.uri;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    public Properties getProperties() {
        return new Properties(this.props);
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.Throwable, org.ehcache.jsr107.MultiCacheException] */
    /* JADX WARN: Type inference failed for: r0v17, types: [java.lang.Throwable, org.ehcache.jsr107.MultiCacheException] */
    /* JADX WARN: Type inference failed for: r0v28, types: [java.lang.Throwable, org.ehcache.jsr107.MultiCacheException] */
    public <K, V, C extends Configuration<K, V>> javax.cache.Cache<K, V> createCache(String cacheName, C config) throws IllegalArgumentException {
        checkClosed();
        if (cacheName == null || config == null) {
            throw new NullPointerException();
        }
        synchronized (this.cachesLock) {
            if (config instanceof Eh107Configuration.Eh107ConfigurationWrapper) {
                Eh107Configuration.Eh107ConfigurationWrapper<K, V> configurationWrapper = (Eh107Configuration.Eh107ConfigurationWrapper) config;
                CacheConfiguration<K, V> unwrap = configurationWrapper.getCacheConfiguration();
                try {
                    Cache<K, V> ehcache = this.ehCacheManager.createCache(cacheName, unwrap);
                    Eh107Cache<K, V> cache = wrapEhcacheCache(cacheName, (InternalCache) ehcache);
                    if (!$assertionsDisabled && safeCacheRetrieval(cacheName) != null) {
                        throw new AssertionError();
                    }
                    this.caches.put(cacheName, cache);
                    Eh107Configuration<?, ?> configuration = (Eh107Configuration) cache.getConfiguration(Eh107Configuration.class);
                    if (configuration.isManagementEnabled()) {
                        enableManagement(cacheName, true);
                    }
                    if (configuration.isStatisticsEnabled()) {
                        enableStatistics(cacheName, true);
                    }
                    return cache;
                } catch (IllegalArgumentException e) {
                    throw new CacheException("A Cache named [" + cacheName + "] already exists");
                }
            }
            ConfigurationMerger.ConfigHolder<K, V> configHolder = this.configurationMerger.mergeConfigurations(cacheName, config);
            try {
                InternalCache<K, V> ehCache = (InternalCache) this.ehCacheManager.createCache(cacheName, configHolder.cacheConfiguration);
                Eh107Cache<?, ?> eh107Cache = null;
                CacheResources<K, V> cacheResources = configHolder.cacheResources;
                try {
                    if (configHolder.useEhcacheLoaderWriter) {
                        cacheResources = new CacheResources<>(cacheName, wrapCacheLoaderWriter(ehCache.getCacheLoaderWriter()), cacheResources.getExpiryPolicy(), cacheResources.getListenerResources());
                    }
                    eh107Cache = new Eh107Cache<>(cacheName, new Eh107CompleteConfiguration(configHolder.jsr107Configuration, ehCache.getRuntimeConfiguration()), cacheResources, ehCache, this);
                    this.caches.put(cacheName, eh107Cache);
                    if (configHolder.jsr107Configuration.isManagementEnabled()) {
                        enableManagement(cacheName, true);
                    }
                    if (configHolder.jsr107Configuration.isStatisticsEnabled()) {
                        enableStatistics(cacheName, true);
                    }
                    return eh107Cache;
                } catch (Throwable t) {
                    ?? multiCacheException = new MultiCacheException(t);
                    if (eh107Cache != null) {
                        eh107Cache.closeInternal(multiCacheException);
                    } else {
                        cacheResources.closeResources(multiCacheException);
                    }
                    throw multiCacheException;
                }
            } catch (IllegalArgumentException e2) {
                ?? multiCacheException2 = new MultiCacheException(e2);
                configHolder.cacheResources.closeResources(multiCacheException2);
                throw new CacheException("A Cache named [" + cacheName + "] already exists", (Throwable) multiCacheException2);
            } catch (Throwable t2) {
                ?? multiCacheException3 = new MultiCacheException(t2);
                configHolder.cacheResources.closeResources(multiCacheException3);
                throw multiCacheException3;
            }
        }
    }

    private void checkClosed() {
        if (isClosed()) {
            throw new IllegalStateException(toString() + " is closed");
        }
    }

    public String toString() {
        return getClass().getSimpleName() + PropertyAccessor.PROPERTY_KEY_PREFIX + this.uri + "]";
    }

    public <K, V> javax.cache.Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        checkClosed();
        if (cacheName == null || keyType == null || valueType == null) {
            throw new NullPointerException();
        }
        Eh107Cache<K, V> cache = safeCacheRetrieval(cacheName);
        if (cache == null) {
            return null;
        }
        Class<K> keyType2 = cache.getConfiguration(Configuration.class).getKeyType();
        Class<V> valueType2 = cache.getConfiguration(Configuration.class).getValueType();
        if (keyType != keyType2) {
            throw new ClassCastException("Cache has key type " + keyType2.getName() + ", but getCache() called with key type " + keyType.getName());
        }
        if (valueType != valueType2) {
            throw new ClassCastException("Cache has value type " + valueType2.getName() + ", but getCache() called with value type " + valueType.getName());
        }
        return cache;
    }

    public <K, V> javax.cache.Cache<K, V> getCache(String cacheName) {
        checkClosed();
        if (cacheName == null) {
            throw new NullPointerException();
        }
        Eh107Cache<K, V> cache = safeCacheRetrieval(cacheName);
        if (cache == null) {
            return null;
        }
        if (cache.getConfiguration(Configuration.class).getKeyType() != Object.class || cache.getConfiguration(Configuration.class).getValueType() != Object.class) {
            throw new IllegalArgumentException("Cache [" + cacheName + "] specifies key/value types. Use getCache(String, Class, Class)");
        }
        return cache;
    }

    private <K, V> Eh107Cache<K, V> safeCacheRetrieval(String cacheName) {
        Eh107Cache<K, V> eh107Cache = (Eh107Cache) this.caches.get(cacheName);
        if (eh107Cache != null && eh107Cache.isClosed()) {
            return null;
        }
        return eh107Cache;
    }

    public Iterable<String> getCacheNames() {
        refreshAllCaches();
        return Collections.unmodifiableList(new ArrayList(this.caches.keySet()));
    }

    public void destroyCache(String cacheName) throws CacheException {
        if (cacheName == null) {
            throw new NullPointerException();
        }
        MultiCacheException destroyException = new MultiCacheException();
        synchronized (this.cachesLock) {
            checkClosed();
            Eh107Cache<?, ?> cache = this.caches.remove(cacheName);
            if (cache == null) {
                return;
            }
            try {
                enableManagement(cache, false);
            } catch (Throwable t) {
                destroyException.addThrowable(t);
            }
            try {
                enableStatistics(cache, false);
            } catch (Throwable t2) {
                destroyException.addThrowable(t2);
            }
            cache.destroy(destroyException);
            try {
                this.ehCacheManager.removeCache(cache.getName());
            } catch (Throwable t3) {
                destroyException.addThrowable(t3);
            }
            destroyException.throwIfNotEmpty();
        }
    }

    public void enableManagement(String cacheName, boolean enabled) {
        checkClosed();
        if (cacheName == null) {
            throw new NullPointerException();
        }
        Eh107Cache<?, ?> cache = safeCacheRetrieval(cacheName);
        if (cache == null) {
            throw new IllegalArgumentException("No such Cache named " + cacheName);
        }
        enableManagement(cache, enabled);
    }

    private void enableManagement(Eh107Cache<?, ?> cache, boolean enabled) {
        synchronized (this.cachesLock) {
            checkClosed();
            if (enabled) {
                registerObject(cache.getManagementMBean());
            } else {
                unregisterObject(cache.getManagementMBean());
            }
            cache.setManagementEnabled(enabled);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.CacheException */
    private void unregisterObject(Eh107MXBean bean) throws CacheException {
        try {
            MBEAN_SERVER.unregisterMBean(bean.getObjectName());
        } catch (InstanceNotFoundException e) {
        } catch (Exception e2) {
            throw new CacheException(e2);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.CacheException */
    private void registerObject(Eh107MXBean bean) throws CacheException {
        try {
            LOG.info("Registering Ehcache MBean {}", bean.getObjectName());
            MBEAN_SERVER.registerMBean(bean, bean.getObjectName());
        } catch (InstanceAlreadyExistsException e) {
        } catch (Exception e2) {
            throw new CacheException(e2);
        }
    }

    public void enableStatistics(String cacheName, boolean enabled) {
        checkClosed();
        if (cacheName == null) {
            throw new NullPointerException();
        }
        Eh107Cache<?, ?> cache = safeCacheRetrieval(cacheName);
        if (cache == null) {
            throw new IllegalArgumentException("No such Cache named " + cacheName);
        }
        enableStatistics(cache, enabled);
    }

    private void enableStatistics(Eh107Cache<?, ?> cache, boolean enabled) {
        synchronized (this.cachesLock) {
            checkClosed();
            if (enabled) {
                registerObject(cache.getStatisticsMBean());
            } else {
                unregisterObject(cache.getStatisticsMBean());
            }
            cache.setStatisticsEnabled(enabled);
        }
    }

    public boolean isClosed() {
        return this.ehCacheManager.getStatus() == Status.UNINITIALIZED;
    }

    public <T> T unwrap(Class<T> cls) {
        return (T) Unwrap.unwrap(cls, this, this.ehCacheManager);
    }

    public void close() throws CacheException {
        MultiCacheException closeException = new MultiCacheException();
        this.cachingProvider.close(this, closeException);
        closeException.throwIfNotEmpty();
    }

    void closeInternal(MultiCacheException closeException) {
        try {
            synchronized (this.cachesLock) {
                for (Eh107Cache<?, ?> cache : this.caches.values()) {
                    try {
                        close(cache, closeException);
                    } catch (Throwable t) {
                        closeException.addThrowable(t);
                    }
                }
                try {
                    this.caches.clear();
                } catch (Throwable t2) {
                    closeException.addThrowable(t2);
                }
                try {
                    this.ehCacheManager.close();
                } catch (Throwable t3) {
                    closeException.addThrowable(t3);
                }
            }
        } catch (Throwable t4) {
            closeException.addThrowable(t4);
        }
    }

    void close(Eh107Cache<?, ?> cache, MultiCacheException closeException) {
        try {
            if (this.caches.remove(cache.getName(), cache)) {
                try {
                    unregisterObject(cache.getManagementMBean());
                } catch (Throwable t) {
                    closeException.addThrowable(t);
                }
                try {
                    unregisterObject(cache.getStatisticsMBean());
                } catch (Throwable t2) {
                    closeException.addThrowable(t2);
                }
                try {
                    cache.closeInternal(closeException);
                } catch (Throwable t3) {
                    closeException.addThrowable(t3);
                }
                try {
                    this.ehCacheManager.removeCache(cache.getName());
                } catch (Throwable t4) {
                    closeException.addThrowable(t4);
                }
            }
        } catch (Throwable t5) {
            closeException.addThrowable(t5);
        }
    }
}
