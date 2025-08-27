package org.springframework.data.redis.cache;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import org.apache.poi.ss.util.CellUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.DecoratedRedisConnection;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache.class */
public class RedisCache extends AbstractValueAdaptingCache {
    private final RedisOperations redisOperations;
    private final RedisCacheMetadata cacheMetadata;
    private final CacheValueAccessor cacheValueAccessor;

    public RedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration) {
        this(name, prefix, redisOperations, expiration, false);
    }

    public RedisCache(String name, byte[] prefix, RedisOperations<? extends Object, ? extends Object> redisOperations, long expiration, boolean allowNullValues) {
        super(allowNullValues);
        Assert.hasText(name, "CacheName must not be null or empty!");
        RedisSerializer<?> serializer = redisOperations.getValueSerializer() != null ? redisOperations.getValueSerializer() : new JdkSerializationRedisSerializer();
        this.cacheMetadata = new RedisCacheMetadata(name, prefix);
        this.cacheMetadata.setDefaultExpiration(expiration);
        this.redisOperations = redisOperations;
        this.cacheValueAccessor = new CacheValueAccessor(serializer);
        if (allowNullValues) {
            if ((redisOperations.getValueSerializer() instanceof StringRedisSerializer) || (redisOperations.getValueSerializer() instanceof GenericToStringSerializer) || (redisOperations.getValueSerializer() instanceof JacksonJsonRedisSerializer) || (redisOperations.getValueSerializer() instanceof Jackson2JsonRedisSerializer)) {
                throw new IllegalArgumentException(String.format("Redis does not allow keys with null value ¯\\_(ツ)_/¯. The chosen %s does not support generic type handling and therefore cannot be used with allowNullValues enabled. Please use a different RedisSerializer or disable null value support.", ClassUtils.getShortName(redisOperations.getValueSerializer().getClass())));
            }
        }
    }

    @Override // org.springframework.cache.support.AbstractValueAdaptingCache, org.springframework.cache.Cache
    public <T> T get(Object obj, Class<T> cls) {
        Cache.ValueWrapper valueWrapper = get(obj);
        if (valueWrapper == null) {
            return null;
        }
        return (T) valueWrapper.get();
    }

    @Override // org.springframework.cache.support.AbstractValueAdaptingCache, org.springframework.cache.Cache
    public Cache.ValueWrapper get(Object key) {
        return get(getRedisCacheKey(key));
    }

    @Override // org.springframework.cache.Cache
    public <T> T get(Object obj, Callable<T> callable) {
        BinaryRedisCacheElement binaryRedisCacheElement = new BinaryRedisCacheElement(new RedisCacheElement(getRedisCacheKey(obj), new StoreTranslatingCallable(callable)).expireAfter(this.cacheMetadata.getDefaultExpiration()), this.cacheValueAccessor);
        Cache.ValueWrapper valueWrapper = get(obj);
        if (valueWrapper != null) {
            return (T) valueWrapper.get();
        }
        try {
            byte[] bArr = (byte[]) this.redisOperations.execute(new RedisWriteThroughCallback(binaryRedisCacheElement, this.cacheMetadata));
            if (bArr == null) {
                return null;
            }
            return (T) fromStoreValue(this.cacheValueAccessor.deserializeIfNecessary(bArr));
        } catch (RuntimeException e) {
            throw CacheValueRetrievalExceptionFactory.INSTANCE.create(obj, callable, e);
        }
    }

    public RedisCacheElement get(final RedisCacheKey cacheKey) {
        byte[] bytes;
        Assert.notNull(cacheKey, "CacheKey must not be null!");
        Boolean exists = (Boolean) this.redisOperations.execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.cache.RedisCache.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.exists(cacheKey.getKeyBytes());
            }
        });
        if (!exists.booleanValue() || (bytes = doLookup(cacheKey)) == null) {
            return null;
        }
        return new RedisCacheElement(cacheKey, fromStoreValue(deserialize(bytes)));
    }

    @Override // org.springframework.cache.Cache
    public void put(Object key, Object value) {
        put(new RedisCacheElement(getRedisCacheKey(key), toStoreValue(value)).expireAfter(this.cacheMetadata.getDefaultExpiration()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.cache.support.AbstractValueAdaptingCache
    public Object fromStoreValue(Object storeValue) {
        if (isAllowNullValues() && (storeValue instanceof NullValue)) {
            return null;
        }
        return super.fromStoreValue(storeValue);
    }

    public void put(RedisCacheElement element) {
        Assert.notNull(element, "Element must not be null!");
        this.redisOperations.execute(new RedisCachePutCallback(new BinaryRedisCacheElement(element, this.cacheValueAccessor), this.cacheMetadata));
    }

    @Override // org.springframework.cache.Cache
    public Cache.ValueWrapper putIfAbsent(Object key, Object value) {
        return putIfAbsent(new RedisCacheElement(getRedisCacheKey(key), toStoreValue(value)).expireAfter(this.cacheMetadata.getDefaultExpiration()));
    }

    public Cache.ValueWrapper putIfAbsent(RedisCacheElement element) {
        Assert.notNull(element, "Element must not be null!");
        new RedisCachePutIfAbsentCallback(new BinaryRedisCacheElement(element, this.cacheValueAccessor), this.cacheMetadata);
        return toWrapper(this.cacheValueAccessor.deserializeIfNecessary((byte[]) this.redisOperations.execute(new RedisCachePutIfAbsentCallback(new BinaryRedisCacheElement(element, this.cacheValueAccessor), this.cacheMetadata))));
    }

    @Override // org.springframework.cache.Cache
    public void evict(Object key) {
        evict(new RedisCacheElement(getRedisCacheKey(key), null));
    }

    public void evict(RedisCacheElement element) {
        Assert.notNull(element, "Element must not be null!");
        this.redisOperations.execute(new RedisCacheEvictCallback(new BinaryRedisCacheElement(element, this.cacheValueAccessor), this.cacheMetadata));
    }

    @Override // org.springframework.cache.Cache
    public void clear() {
        this.redisOperations.execute(this.cacheMetadata.usesKeyPrefix() ? new RedisCacheCleanByPrefixCallback(this.cacheMetadata) : new RedisCacheCleanByKeysCallback(this.cacheMetadata));
    }

    @Override // org.springframework.cache.Cache
    public String getName() {
        return this.cacheMetadata.getCacheName();
    }

    @Override // org.springframework.cache.Cache
    public Object getNativeCache() {
        return this.redisOperations;
    }

    private Cache.ValueWrapper toWrapper(Object value) {
        if (value != null) {
            return new SimpleValueWrapper(value);
        }
        return null;
    }

    @Override // org.springframework.cache.support.AbstractValueAdaptingCache
    protected Object lookup(Object key) {
        return deserialize(doLookup(key));
    }

    private byte[] doLookup(Object key) {
        RedisCacheKey cacheKey = key instanceof RedisCacheKey ? (RedisCacheKey) key : getRedisCacheKey(key);
        return (byte[]) this.redisOperations.execute(new AbstractRedisCacheCallback<byte[]>(new BinaryRedisCacheElement(new RedisCacheElement(cacheKey, null), this.cacheValueAccessor), this.cacheMetadata) { // from class: org.springframework.data.redis.cache.RedisCache.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.cache.RedisCache.AbstractRedisCacheCallback
            public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {
                return connection.get(element.getKeyBytes());
            }
        });
    }

    private Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return this.cacheValueAccessor.deserializeIfNecessary(bytes);
    }

    private RedisCacheKey getRedisCacheKey(Object key) {
        return new RedisCacheKey(key).usePrefix(this.cacheMetadata.getKeyPrefix()).withKeySerializer(this.redisOperations.getKeySerializer());
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$StoreTranslatingCallable.class */
    private class StoreTranslatingCallable implements Callable<Object> {
        private Callable<?> valueLoader;

        public StoreTranslatingCallable(Callable<?> valueLoader) {
            this.valueLoader = valueLoader;
        }

        @Override // java.util.concurrent.Callable
        public Object call() throws Exception {
            return RedisCache.this.toStoreValue(this.valueLoader.call());
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$RedisCacheMetadata.class */
    static class RedisCacheMetadata {
        private final String cacheName;
        private final byte[] keyPrefix;
        private final byte[] setOfKnownKeys;
        private final byte[] cacheLockName;
        private long defaultExpiration = 0;

        public RedisCacheMetadata(String cacheName, byte[] keyPrefix) {
            Assert.hasText(cacheName, "CacheName must not be null or empty!");
            this.cacheName = cacheName;
            this.keyPrefix = keyPrefix;
            StringRedisSerializer stringSerializer = new StringRedisSerializer();
            this.setOfKnownKeys = usesKeyPrefix() ? new byte[0] : stringSerializer.serialize(cacheName + "~keys");
            this.cacheLockName = stringSerializer.serialize(cacheName + "~lock");
        }

        public boolean usesKeyPrefix() {
            return this.keyPrefix != null && this.keyPrefix.length > 0;
        }

        public byte[] getKeyPrefix() {
            return this.keyPrefix;
        }

        public byte[] getSetOfKnownKeysKey() {
            return this.setOfKnownKeys;
        }

        public byte[] getCacheLockKey() {
            return this.cacheLockName;
        }

        public String getCacheName() {
            return this.cacheName;
        }

        public void setDefaultExpiration(long seconds) {
            this.defaultExpiration = seconds;
        }

        public long getDefaultExpiration() {
            return this.defaultExpiration;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$CacheValueAccessor.class */
    static class CacheValueAccessor {
        private final RedisSerializer valueSerializer;

        CacheValueAccessor(RedisSerializer valueRedisSerializer) {
            this.valueSerializer = valueRedisSerializer;
        }

        byte[] convertToBytesIfNecessary(Object value) {
            if (value == null) {
                return new byte[0];
            }
            if (this.valueSerializer == null && (value instanceof byte[])) {
                return (byte[]) value;
            }
            return this.valueSerializer.serialize(value);
        }

        Object deserializeIfNecessary(byte[] value) {
            if (this.valueSerializer != null) {
                return this.valueSerializer.deserialize(value);
            }
            return value;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$BinaryRedisCacheElement.class */
    static class BinaryRedisCacheElement extends RedisCacheElement {
        private byte[] keyBytes;
        private byte[] valueBytes;
        private RedisCacheElement element;
        private boolean lazyLoad;
        private CacheValueAccessor accessor;

        public BinaryRedisCacheElement(RedisCacheElement element, CacheValueAccessor accessor) {
            super(element.getKey(), element.get());
            this.element = element;
            this.keyBytes = element.getKeyBytes();
            this.accessor = accessor;
            this.lazyLoad = element.get() instanceof Callable;
            this.valueBytes = this.lazyLoad ? null : accessor.convertToBytesIfNecessary(element.get());
        }

        @Override // org.springframework.data.redis.cache.RedisCacheElement
        public byte[] getKeyBytes() {
            return this.keyBytes;
        }

        @Override // org.springframework.data.redis.cache.RedisCacheElement
        public long getTimeToLive() {
            return this.element.getTimeToLive();
        }

        @Override // org.springframework.data.redis.cache.RedisCacheElement
        public boolean hasKeyPrefix() {
            return this.element.hasKeyPrefix();
        }

        @Override // org.springframework.data.redis.cache.RedisCacheElement
        public boolean isEternal() {
            return this.element.isEternal();
        }

        @Override // org.springframework.data.redis.cache.RedisCacheElement
        public RedisCacheElement expireAfter(long seconds) {
            return this.element.expireAfter(seconds);
        }

        @Override // org.springframework.cache.support.SimpleValueWrapper, org.springframework.cache.Cache.ValueWrapper
        public byte[] get() {
            if (this.lazyLoad && this.valueBytes == null) {
                try {
                    this.valueBytes = this.accessor.convertToBytesIfNecessary(((Callable) this.element.get()).call());
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw ((RuntimeException) e);
                    }
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            return this.valueBytes;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$AbstractRedisCacheCallback.class */
    static abstract class AbstractRedisCacheCallback<T> implements RedisCallback<T> {
        private long WAIT_FOR_LOCK_TIMEOUT = 300;
        private final BinaryRedisCacheElement element;
        private final RedisCacheMetadata cacheMetadata;

        public abstract T doInRedis(BinaryRedisCacheElement binaryRedisCacheElement, RedisConnection redisConnection) throws DataAccessException;

        public AbstractRedisCacheCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            this.element = element;
            this.cacheMetadata = metadata;
        }

        @Override // org.springframework.data.redis.core.RedisCallback
        /* renamed from: doInRedis */
        public T doInRedis2(RedisConnection connection) throws DataAccessException, InterruptedException {
            waitForLock(connection);
            return doInRedis(this.element, connection);
        }

        protected void processKeyExpiration(RedisCacheElement element, RedisConnection connection) {
            if (!element.isEternal()) {
                connection.expire(element.getKeyBytes(), element.getTimeToLive());
            }
        }

        protected void maintainKnownKeys(RedisCacheElement element, RedisConnection connection) {
            if (!element.hasKeyPrefix()) {
                connection.zAdd(this.cacheMetadata.getSetOfKnownKeysKey(), 0.0d, element.getKeyBytes());
                if (!element.isEternal()) {
                    connection.expire(this.cacheMetadata.getSetOfKnownKeysKey(), element.getTimeToLive());
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
        protected void cleanKnownKeys(RedisCacheElement element, RedisConnection redisConnection) {
            if (!element.hasKeyPrefix()) {
                redisConnection.zRem(this.cacheMetadata.getSetOfKnownKeysKey(), new byte[]{element.getKeyBytes()});
            }
        }

        protected boolean waitForLock(RedisConnection connection) throws InterruptedException {
            boolean retry;
            boolean foundLock = false;
            do {
                retry = false;
                if (connection.exists(this.cacheMetadata.getCacheLockKey()).booleanValue()) {
                    foundLock = true;
                    try {
                        Thread.sleep(this.WAIT_FOR_LOCK_TIMEOUT);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    retry = true;
                }
            } while (retry);
            return foundLock;
        }

        protected void lock(RedisConnection connection) throws InterruptedException {
            waitForLock(connection);
            connection.set(this.cacheMetadata.getCacheLockKey(), CellUtil.LOCKED.getBytes());
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
        protected void unlock(RedisConnection redisConnection) {
            redisConnection.del(new byte[]{this.cacheMetadata.getCacheLockKey()});
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$LockingRedisCacheCallback.class */
    static abstract class LockingRedisCacheCallback<T> implements RedisCallback<T> {
        private final RedisCacheMetadata metadata;

        public abstract T doInLock(RedisConnection redisConnection);

        public LockingRedisCacheCallback(RedisCacheMetadata metadata) {
            this.metadata = metadata;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v10, types: [byte[], byte[][]] */
        /* JADX WARN: Type inference failed for: r1v4, types: [byte[], byte[][]] */
        @Override // org.springframework.data.redis.core.RedisCallback
        /* renamed from: doInRedis */
        public T doInRedis2(RedisConnection redisConnection) throws DataAccessException {
            if (redisConnection.exists(this.metadata.getCacheLockKey()).booleanValue()) {
                return null;
            }
            try {
                redisConnection.set(this.metadata.getCacheLockKey(), this.metadata.getCacheLockKey());
                T tDoInLock = doInLock(redisConnection);
                redisConnection.del(new byte[]{this.metadata.getCacheLockKey()});
                return tDoInLock;
            } catch (Throwable th) {
                redisConnection.del(new byte[]{this.metadata.getCacheLockKey()});
                throw th;
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$RedisCacheCleanByKeysCallback.class */
    static class RedisCacheCleanByKeysCallback extends LockingRedisCacheCallback<Void> {
        private static final int PAGE_SIZE = 128;
        private final RedisCacheMetadata metadata;

        RedisCacheCleanByKeysCallback(RedisCacheMetadata metadata) {
            super(metadata);
            this.metadata = metadata;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v5, types: [byte[], byte[][]] */
        @Override // org.springframework.data.redis.cache.RedisCache.LockingRedisCacheCallback
        public Void doInLock(RedisConnection redisConnection) {
            boolean finished;
            int offset = 0;
            do {
                Set<byte[]> keys = redisConnection.zRange(this.metadata.getSetOfKnownKeysKey(), offset * 128, ((offset + 1) * 128) - 1);
                finished = keys.size() < 128;
                offset++;
                if (!keys.isEmpty()) {
                    redisConnection.del((byte[][]) keys.toArray((Object[]) new byte[keys.size()]));
                }
            } while (!finished);
            redisConnection.del(new byte[]{this.metadata.getSetOfKnownKeysKey()});
            return null;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$RedisCacheCleanByPrefixCallback.class */
    static class RedisCacheCleanByPrefixCallback extends LockingRedisCacheCallback<Void> {
        private static final byte[] REMOVE_KEYS_BY_PATTERN_LUA = new StringRedisSerializer().serialize("local keys = redis.call('KEYS', ARGV[1]); local keysCount = table.getn(keys); if(keysCount > 0) then for _, key in ipairs(keys) do redis.call('del', key); end; end; return keysCount;");
        private static final byte[] WILD_CARD = new StringRedisSerializer().serialize("*");
        private final RedisCacheMetadata metadata;

        public RedisCacheCleanByPrefixCallback(RedisCacheMetadata metadata) {
            super(metadata);
            this.metadata = metadata;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v3, types: [byte[], byte[][]] */
        @Override // org.springframework.data.redis.cache.RedisCache.LockingRedisCacheCallback
        public Void doInLock(RedisConnection redisConnection) throws DataAccessException {
            byte[] prefixToUse = Arrays.copyOf(this.metadata.getKeyPrefix(), this.metadata.getKeyPrefix().length + WILD_CARD.length);
            System.arraycopy(WILD_CARD, 0, prefixToUse, this.metadata.getKeyPrefix().length, WILD_CARD.length);
            if (RedisCache.isClusterConnection(redisConnection)) {
                Set<byte[]> keys = redisConnection.keys(prefixToUse);
                if (!keys.isEmpty()) {
                    redisConnection.del((byte[][]) keys.toArray((Object[]) new byte[keys.size()]));
                    return null;
                }
                return null;
            }
            redisConnection.eval(REMOVE_KEYS_BY_PATTERN_LUA, ReturnType.INTEGER, 0, new byte[]{prefixToUse});
            return null;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$RedisCacheEvictCallback.class */
    static class RedisCacheEvictCallback extends AbstractRedisCacheCallback<Void> {
        public RedisCacheEvictCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
        @Override // org.springframework.data.redis.cache.RedisCache.AbstractRedisCacheCallback
        public Void doInRedis(BinaryRedisCacheElement element, RedisConnection redisConnection) throws DataAccessException {
            redisConnection.del(new byte[]{element.getKeyBytes()});
            cleanKnownKeys(element, redisConnection);
            return null;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$RedisCachePutCallback.class */
    static class RedisCachePutCallback extends AbstractRedisCacheCallback<Void> {
        public RedisCachePutCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v5, types: [byte[], byte[][]] */
        @Override // org.springframework.data.redis.cache.RedisCache.AbstractRedisCacheCallback
        public Void doInRedis(BinaryRedisCacheElement element, RedisConnection redisConnection) throws DataAccessException {
            if (!RedisCache.isClusterConnection(redisConnection)) {
                redisConnection.multi();
            }
            if (element.get().length == 0) {
                redisConnection.del(new byte[]{element.getKeyBytes()});
            } else {
                redisConnection.set(element.getKeyBytes(), element.get());
                processKeyExpiration(element, redisConnection);
                maintainKnownKeys(element, redisConnection);
            }
            if (!RedisCache.isClusterConnection(redisConnection)) {
                redisConnection.exec();
                return null;
            }
            return null;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$RedisCachePutIfAbsentCallback.class */
    static class RedisCachePutIfAbsentCallback extends AbstractRedisCacheCallback<byte[]> {
        public RedisCachePutIfAbsentCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.data.redis.cache.RedisCache.AbstractRedisCacheCallback
        public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection connection) throws DataAccessException {
            waitForLock(connection);
            byte[] keyBytes = element.getKeyBytes();
            byte[] value = element.get();
            if (!connection.setNX(keyBytes, value).booleanValue()) {
                return connection.get(keyBytes);
            }
            maintainKnownKeys(element, connection);
            processKeyExpiration(element, connection);
            return null;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$RedisWriteThroughCallback.class */
    static class RedisWriteThroughCallback extends AbstractRedisCacheCallback<byte[]> {
        public RedisWriteThroughCallback(BinaryRedisCacheElement element, RedisCacheMetadata metadata) {
            super(element, metadata);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v10, types: [byte[], byte[][]] */
        /* JADX WARN: Type inference failed for: r1v12, types: [byte[], byte[][]] */
        @Override // org.springframework.data.redis.cache.RedisCache.AbstractRedisCacheCallback
        public byte[] doInRedis(BinaryRedisCacheElement element, RedisConnection redisConnection) throws DataAccessException {
            try {
                lock(redisConnection);
                try {
                    byte[] value = redisConnection.get(element.getKeyBytes());
                    if (value == null) {
                        if (!RedisCache.isClusterConnection(redisConnection)) {
                            redisConnection.watch(new byte[]{element.getKeyBytes()});
                            redisConnection.multi();
                        }
                        byte[] value2 = element.get();
                        if (value2.length == 0) {
                            redisConnection.del(new byte[]{element.getKeyBytes()});
                        } else {
                            redisConnection.set(element.getKeyBytes(), value2);
                            processKeyExpiration(element, redisConnection);
                            maintainKnownKeys(element, redisConnection);
                        }
                        if (!RedisCache.isClusterConnection(redisConnection)) {
                            redisConnection.exec();
                        }
                        unlock(redisConnection);
                        return value2;
                    }
                    return value;
                } catch (RuntimeException e) {
                    if (!RedisCache.isClusterConnection(redisConnection)) {
                        redisConnection.discard();
                    }
                    throw e;
                }
            } finally {
                unlock(redisConnection);
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/cache/RedisCache$CacheValueRetrievalExceptionFactory.class */
    private enum CacheValueRetrievalExceptionFactory {
        INSTANCE;

        private static boolean isSpring43 = ClassUtils.isPresent("org.springframework.cache.Cache$ValueRetrievalException", ClassUtils.getDefaultClassLoader());

        public RuntimeException create(Object key, Callable<?> valueLoader, Throwable cause) throws LinkageError {
            if (isSpring43) {
                try {
                    Class<?> execption = ClassUtils.forName("org.springframework.cache.Cache$ValueRetrievalException", getClass().getClassLoader());
                    Constructor<?> c = ClassUtils.getConstructorIfAvailable(execption, Object.class, Callable.class, Throwable.class);
                    return (RuntimeException) c.newInstance(key, valueLoader, cause);
                } catch (Exception e) {
                }
            }
            return new RedisSystemException(String.format("Value for key '%s' could not be loaded using '%s'.", key, valueLoader), cause);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isClusterConnection(RedisConnection connection) {
        while (connection instanceof DecoratedRedisConnection) {
            connection = ((DecoratedRedisConnection) connection).getDelegate();
        }
        return connection instanceof RedisClusterConnection;
    }
}
