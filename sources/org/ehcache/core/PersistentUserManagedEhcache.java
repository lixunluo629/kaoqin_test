package org.ehcache.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.ehcache.Cache;
import org.ehcache.CachePersistenceException;
import org.ehcache.PersistentUserManagedCache;
import org.ehcache.Status;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.ResourceType;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.core.StatusTransitioner;
import org.ehcache.core.events.CacheEventDispatcher;
import org.ehcache.core.spi.LifeCycled;
import org.ehcache.core.spi.service.DiskResourceService;
import org.ehcache.core.spi.store.Store;
import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/PersistentUserManagedEhcache.class */
public class PersistentUserManagedEhcache<K, V> implements PersistentUserManagedCache<K, V> {
    private final StatusTransitioner statusTransitioner;
    private final Logger logger;
    private final InternalCache<K, V> cache;
    private final DiskResourceService diskPersistenceService;
    private final String id;

    public PersistentUserManagedEhcache(CacheConfiguration<K, V> configuration, Store<K, V> store, DiskResourceService diskPersistenceService, CacheLoaderWriter<? super K, V> cacheLoaderWriter, CacheEventDispatcher<K, V> eventDispatcher, String id) {
        this.logger = LoggerFactory.getLogger(PersistentUserManagedEhcache.class.getName() + "-" + id);
        this.statusTransitioner = new StatusTransitioner(this.logger);
        if (cacheLoaderWriter == null) {
            this.cache = new Ehcache(new EhcacheRuntimeConfiguration(configuration), store, eventDispatcher, this.logger, this.statusTransitioner);
        } else {
            this.cache = new EhcacheWithLoaderWriter(new EhcacheRuntimeConfiguration(configuration), store, cacheLoaderWriter, eventDispatcher, true, this.logger, this.statusTransitioner);
        }
        this.diskPersistenceService = diskPersistenceService;
        this.id = id;
    }

    @Override // org.ehcache.PersistentUserManagedCache
    public void destroy() throws CachePersistenceException {
        StatusTransitioner.Transition st = this.statusTransitioner.maintenance();
        try {
            st.succeeded();
            destroyInternal();
        } catch (Throwable t) {
            throw st.failed(t);
        }
    }

    void create() {
        this.statusTransitioner.checkMaintenance();
        try {
            if (!((SizedResourcePool) getRuntimeConfiguration().getResourcePools().getPoolForResource(ResourceType.Core.DISK)).isPersistent()) {
                destroy();
            }
            this.diskPersistenceService.getPersistenceSpaceIdentifier(this.id, this.cache.getRuntimeConfiguration());
        } catch (CachePersistenceException e) {
            throw new RuntimeException("Unable to create persistence space for user managed cache " + this.id, e);
        }
    }

    void destroyInternal() throws CachePersistenceException {
        this.statusTransitioner.checkMaintenance();
        this.diskPersistenceService.destroy(this.id);
    }

    @Override // org.ehcache.UserManagedCache
    public void init() {
        this.cache.init();
    }

    @Override // org.ehcache.UserManagedCache, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.cache.close();
        if (!((SizedResourcePool) getRuntimeConfiguration().getResourcePools().getPoolForResource(ResourceType.Core.DISK)).isPersistent()) {
            try {
                this.diskPersistenceService.destroy(this.id);
            } catch (CachePersistenceException e) {
                this.logger.debug("Unable to clear persistence space for user managed cache {}", this.id, e);
            }
        }
    }

    @Override // org.ehcache.UserManagedCache
    public Status getStatus() {
        return this.statusTransitioner.currentStatus();
    }

    @Override // org.ehcache.Cache
    public V get(K key) throws CacheLoadingException {
        return this.cache.get(key);
    }

    @Override // org.ehcache.Cache
    public void put(K key, V value) throws CacheWritingException {
        this.cache.put(key, value);
    }

    @Override // org.ehcache.Cache
    public boolean containsKey(K key) {
        return this.cache.containsKey(key);
    }

    @Override // org.ehcache.Cache
    public void remove(K key) throws CacheWritingException {
        this.cache.remove(key);
    }

    @Override // org.ehcache.Cache
    public Map<K, V> getAll(Set<? extends K> keys) throws BulkCacheLoadingException {
        return this.cache.getAll(keys);
    }

    @Override // org.ehcache.Cache
    public void putAll(Map<? extends K, ? extends V> entries) throws BulkCacheWritingException {
        this.cache.putAll(entries);
    }

    @Override // org.ehcache.Cache
    public void removeAll(Set<? extends K> keys) throws BulkCacheWritingException {
        this.cache.removeAll(keys);
    }

    @Override // org.ehcache.Cache
    public void clear() {
        this.cache.clear();
    }

    @Override // org.ehcache.Cache
    public V putIfAbsent(K key, V value) throws CacheLoadingException, CacheWritingException {
        return this.cache.putIfAbsent(key, value);
    }

    @Override // org.ehcache.Cache
    public boolean remove(K key, V value) throws CacheWritingException {
        return this.cache.remove(key, value);
    }

    @Override // org.ehcache.Cache
    public V replace(K key, V value) throws CacheLoadingException, CacheWritingException {
        return this.cache.replace(key, value);
    }

    @Override // org.ehcache.Cache
    public boolean replace(K key, V oldValue, V newValue) throws CacheLoadingException, CacheWritingException {
        return this.cache.replace(key, oldValue, newValue);
    }

    @Override // org.ehcache.Cache
    public CacheRuntimeConfiguration<K, V> getRuntimeConfiguration() {
        return this.cache.getRuntimeConfiguration();
    }

    @Override // java.lang.Iterable
    public Iterator<Cache.Entry<K, V>> iterator() {
        return this.cache.iterator();
    }

    public void addHook(LifeCycled lifeCycled) {
        this.statusTransitioner.addHook(lifeCycled);
    }
}
