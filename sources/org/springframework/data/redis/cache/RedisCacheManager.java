package org.springframework.data.redis.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCacheManager.class */
public class RedisCacheManager extends AbstractTransactionSupportingCacheManager {
    private final Log logger;
    private final RedisOperations redisOperations;
    private boolean usePrefix;
    private RedisCachePrefix cachePrefix;
    private boolean loadRemoteCachesOnStartup;
    private boolean dynamic;
    private long defaultExpiration;
    private Map<String, Long> expires;
    private Set<String> configuredCacheNames;
    private final boolean cacheNullValues;

    public RedisCacheManager(RedisOperations redisOperations) {
        this(redisOperations, Collections.emptyList());
    }

    public RedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        this(redisOperations, cacheNames, false);
    }

    public RedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames, boolean cacheNullValues) {
        this.logger = LogFactory.getLog(RedisCacheManager.class);
        this.usePrefix = false;
        this.cachePrefix = new DefaultRedisCachePrefix();
        this.loadRemoteCachesOnStartup = false;
        this.dynamic = true;
        this.defaultExpiration = 0L;
        this.expires = null;
        this.redisOperations = redisOperations;
        this.cacheNullValues = cacheNullValues;
        setCacheNames(cacheNames);
    }

    public void setCacheNames(Collection<String> cacheNames) {
        Set<String> newCacheNames = CollectionUtils.isEmpty(cacheNames) ? Collections.emptySet() : new HashSet<>(cacheNames);
        this.configuredCacheNames = newCacheNames;
        this.dynamic = newCacheNames.isEmpty();
    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    public void setCachePrefix(RedisCachePrefix cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public void setDefaultExpiration(long defaultExpireTime) {
        this.defaultExpiration = defaultExpireTime;
    }

    public void setExpires(Map<String, Long> expires) {
        this.expires = expires != null ? new ConcurrentHashMap(expires) : null;
    }

    public void setLoadRemoteCachesOnStartup(boolean loadRemoteCachesOnStartup) {
        this.loadRemoteCachesOnStartup = loadRemoteCachesOnStartup;
    }

    @Override // org.springframework.cache.support.AbstractCacheManager
    protected Collection<? extends Cache> loadCaches() {
        Assert.notNull(this.redisOperations, "A redis template is required in order to interact with data store");
        Set<Cache> caches = new LinkedHashSet<>(this.loadRemoteCachesOnStartup ? loadAndInitRemoteCaches() : new ArrayList<>());
        Set<String> cachesToLoad = new LinkedHashSet<>(this.configuredCacheNames);
        cachesToLoad.addAll(getCacheNames());
        if (!CollectionUtils.isEmpty(cachesToLoad)) {
            for (String cacheName : cachesToLoad) {
                caches.add(createCache(cacheName));
            }
        }
        return caches;
    }

    protected Collection<? extends Cache> addConfiguredCachesIfNecessary(Collection<? extends Cache> caches) {
        Assert.notNull(caches, "Caches must not be null!");
        Collection<Cache> result = new ArrayList<>(caches);
        for (String cacheName : getCacheNames()) {
            boolean configuredCacheAlreadyPresent = false;
            Iterator<? extends Cache> it = caches.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Cache cache = it.next();
                if (cache.getName().equals(cacheName)) {
                    configuredCacheAlreadyPresent = true;
                    break;
                }
            }
            if (!configuredCacheAlreadyPresent) {
                result.add(getCache(cacheName));
            }
        }
        return result;
    }

    @Deprecated
    protected Cache createAndAddCache(String cacheName) {
        Cache cache = super.getCache(cacheName);
        return cache != null ? cache : createCache(cacheName);
    }

    @Override // org.springframework.cache.support.AbstractCacheManager
    protected Cache getMissingCache(String name) {
        if (this.dynamic) {
            return createCache(name);
        }
        return null;
    }

    protected RedisCache createCache(String cacheName) {
        long expiration = computeExpiration(cacheName);
        return new RedisCache(cacheName, this.usePrefix ? this.cachePrefix.prefix(cacheName) : null, this.redisOperations, expiration, this.cacheNullValues);
    }

    protected long computeExpiration(String name) {
        Long expiration = null;
        if (this.expires != null) {
            expiration = this.expires.get(name);
        }
        return expiration != null ? expiration.longValue() : this.defaultExpiration;
    }

    protected List<Cache> loadAndInitRemoteCaches() {
        List<Cache> caches = new ArrayList<>();
        try {
            Set<String> cacheNames = loadRemoteCacheKeys();
            if (!CollectionUtils.isEmpty(cacheNames)) {
                for (String cacheName : cacheNames) {
                    if (null == super.getCache(cacheName)) {
                        caches.add(createCache(cacheName));
                    }
                }
            }
        } catch (Exception e) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Failed to initialize cache with remote cache keys.", e);
            }
        }
        return caches;
    }

    protected Set<String> loadRemoteCacheKeys() {
        return (Set) this.redisOperations.execute(new RedisCallback<Set<String>>() { // from class: org.springframework.data.redis.cache.RedisCacheManager.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<String> doInRedis2(RedisConnection connection) throws DataAccessException {
                Set<byte[]> keys = connection.keys(RedisCacheManager.this.redisOperations.getKeySerializer().serialize("*~keys"));
                Set<String> cacheKeys = new LinkedHashSet<>();
                if (!CollectionUtils.isEmpty(keys)) {
                    for (byte[] key : keys) {
                        cacheKeys.add(RedisCacheManager.this.redisOperations.getKeySerializer().deserialize(key).toString().replace("~keys", ""));
                    }
                }
                return cacheKeys;
            }
        });
    }

    protected RedisOperations getRedisOperations() {
        return this.redisOperations;
    }

    protected RedisCachePrefix getCachePrefix() {
        return this.cachePrefix;
    }

    protected boolean isUsePrefix() {
        return this.usePrefix;
    }

    @Override // org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager, org.springframework.cache.support.AbstractCacheManager
    protected Cache decorateCache(Cache cache) {
        if (isCacheAlreadyDecorated(cache)) {
            return cache;
        }
        return super.decorateCache(cache);
    }

    protected boolean isCacheAlreadyDecorated(Cache cache) {
        return isTransactionAware() && (cache instanceof TransactionAwareCacheDecorator);
    }
}
