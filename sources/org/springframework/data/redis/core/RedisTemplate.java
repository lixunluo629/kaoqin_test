package org.springframework.data.redis.core;

import java.io.Closeable;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.query.QueryUtils;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.script.DefaultScriptExecutor;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.script.ScriptExecutor;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisTemplate.class */
public class RedisTemplate<K, V> extends RedisAccessor implements RedisOperations<K, V>, BeanClassLoaderAware {
    private RedisSerializer<?> defaultSerializer;
    private ClassLoader classLoader;
    private ScriptExecutor<K> scriptExecutor;
    private ValueOperations<K, V> valueOps;
    private ListOperations<K, V> listOps;
    private SetOperations<K, V> setOps;
    private ZSetOperations<K, V> zSetOps;
    private GeoOperations<K, V> geoOps;
    private HyperLogLogOperations<K, V> hllOps;
    private boolean enableTransactionSupport = false;
    private boolean exposeConnection = false;
    private boolean initialized = false;
    private boolean enableDefaultSerializer = true;
    private RedisSerializer keySerializer = null;
    private RedisSerializer valueSerializer = null;
    private RedisSerializer hashKeySerializer = null;
    private RedisSerializer hashValueSerializer = null;
    private RedisSerializer<String> stringSerializer = new StringRedisSerializer();

    @Override // org.springframework.data.redis.core.RedisAccessor, org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        boolean defaultUsed = false;
        if (this.defaultSerializer == null) {
            this.defaultSerializer = new JdkSerializationRedisSerializer(this.classLoader != null ? this.classLoader : getClass().getClassLoader());
        }
        if (this.enableDefaultSerializer) {
            if (this.keySerializer == null) {
                this.keySerializer = this.defaultSerializer;
                defaultUsed = true;
            }
            if (this.valueSerializer == null) {
                this.valueSerializer = this.defaultSerializer;
                defaultUsed = true;
            }
            if (this.hashKeySerializer == null) {
                this.hashKeySerializer = this.defaultSerializer;
                defaultUsed = true;
            }
            if (this.hashValueSerializer == null) {
                this.hashValueSerializer = this.defaultSerializer;
                defaultUsed = true;
            }
        }
        if (this.enableDefaultSerializer && defaultUsed) {
            Assert.notNull(this.defaultSerializer, "default serializer null and not all serializers initialized");
        }
        if (this.scriptExecutor == null) {
            this.scriptExecutor = new DefaultScriptExecutor(this);
        }
        this.initialized = true;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <T> T execute(RedisCallback<T> redisCallback) {
        return (T) execute(redisCallback, isExposeConnection());
    }

    public <T> T execute(RedisCallback<T> redisCallback, boolean z) {
        return (T) execute((RedisCallback) redisCallback, z, false);
    }

    public <T> T execute(RedisCallback<T> redisCallback, boolean z, boolean z2) throws DataAccessException {
        Assert.isTrue(this.initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(redisCallback, "Callback object must not be null");
        RedisConnectionFactory connectionFactory = getConnectionFactory();
        RedisConnection connection = null;
        try {
            if (this.enableTransactionSupport) {
                connection = RedisConnectionUtils.bindConnection(connectionFactory, this.enableTransactionSupport);
            } else {
                connection = RedisConnectionUtils.getConnection(connectionFactory);
            }
            boolean zHasResource = TransactionSynchronizationManager.hasResource(connectionFactory);
            RedisConnection redisConnectionPreProcessConnection = preProcessConnection(connection, zHasResource);
            boolean zIsPipelined = redisConnectionPreProcessConnection.isPipelined();
            if (z2 && !zIsPipelined) {
                redisConnectionPreProcessConnection.openPipeline();
            }
            T tDoInRedis2 = redisCallback.doInRedis2(z ? redisConnectionPreProcessConnection : createRedisConnectionProxy(redisConnectionPreProcessConnection));
            if (z2 && !zIsPipelined) {
                redisConnectionPreProcessConnection.closePipeline();
            }
            T t = (T) postProcessResult(tDoInRedis2, redisConnectionPreProcessConnection, zHasResource);
            RedisConnectionUtils.releaseConnection(connection, connectionFactory);
            return t;
        } catch (Throwable th) {
            RedisConnectionUtils.releaseConnection(connection, connectionFactory);
            throw th;
        }
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <T> T execute(SessionCallback<T> session) throws DataAccessException {
        Assert.isTrue(this.initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(session, "Callback object must not be null");
        RedisConnectionFactory factory = getConnectionFactory();
        RedisConnectionUtils.bindConnection(factory, this.enableTransactionSupport);
        try {
            T tExecute = session.execute(this);
            RedisConnectionUtils.unbindConnection(factory);
            return tExecute;
        } catch (Throwable th) {
            RedisConnectionUtils.unbindConnection(factory);
            throw th;
        }
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<Object> executePipelined(SessionCallback<?> session) {
        return executePipelined(session, this.valueSerializer);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<Object> executePipelined(final SessionCallback<?> session, final RedisSerializer<?> resultSerializer) throws DataAccessException {
        Assert.isTrue(this.initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(session, "Callback object must not be null");
        RedisConnectionFactory factory = getConnectionFactory();
        RedisConnectionUtils.bindConnection(factory, this.enableTransactionSupport);
        try {
            List<Object> list = (List) execute(new RedisCallback<List<Object>>() { // from class: org.springframework.data.redis.core.RedisTemplate.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.springframework.data.redis.core.RedisCallback
                /* renamed from: doInRedis */
                public List<Object> doInRedis2(RedisConnection connection) throws DataAccessException {
                    connection.openPipeline();
                    try {
                        Object result = RedisTemplate.this.executeSession(session);
                        if (result != null) {
                            throw new InvalidDataAccessApiUsageException("Callback cannot return a non-null value as it gets overwritten by the pipeline");
                        }
                        List<Object> closePipeline = connection.closePipeline();
                        List<Object> listDeserializeMixedResults = RedisTemplate.this.deserializeMixedResults(closePipeline, resultSerializer, RedisTemplate.this.hashKeySerializer, RedisTemplate.this.hashValueSerializer);
                        if (1 == 0) {
                            connection.closePipeline();
                        }
                        return listDeserializeMixedResults;
                    } catch (Throwable th) {
                        if (0 == 0) {
                            connection.closePipeline();
                        }
                        throw th;
                    }
                }
            });
            RedisConnectionUtils.unbindConnection(factory);
            return list;
        } catch (Throwable th) {
            RedisConnectionUtils.unbindConnection(factory);
            throw th;
        }
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<Object> executePipelined(RedisCallback<?> action) {
        return executePipelined(action, this.valueSerializer);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<Object> executePipelined(final RedisCallback<?> action, final RedisSerializer<?> resultSerializer) {
        return (List) execute(new RedisCallback<List<Object>>() { // from class: org.springframework.data.redis.core.RedisTemplate.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<Object> doInRedis2(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                try {
                    Object result = action.doInRedis2(connection);
                    if (result != null) {
                        throw new InvalidDataAccessApiUsageException("Callback cannot return a non-null value as it gets overwritten by the pipeline");
                    }
                    List<Object> closePipeline = connection.closePipeline();
                    List<Object> listDeserializeMixedResults = RedisTemplate.this.deserializeMixedResults(closePipeline, resultSerializer, RedisTemplate.this.hashKeySerializer, RedisTemplate.this.hashValueSerializer);
                    if (1 == 0) {
                        connection.closePipeline();
                    }
                    return listDeserializeMixedResults;
                } catch (Throwable th) {
                    if (0 == 0) {
                        connection.closePipeline();
                    }
                    throw th;
                }
            }
        });
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <T> T execute(RedisScript<T> redisScript, List<K> list, Object... objArr) {
        return (T) this.scriptExecutor.execute(redisScript, list, objArr);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <T> T execute(RedisScript<T> redisScript, RedisSerializer<?> redisSerializer, RedisSerializer<T> redisSerializer2, List<K> list, Object... objArr) {
        return (T) this.scriptExecutor.execute(redisScript, redisSerializer, redisSerializer2, list, objArr);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> callback) {
        Assert.isTrue(this.initialized, "template not initialized; call afterPropertiesSet() before using it");
        Assert.notNull(callback, "Callback object must not be null");
        RedisConnectionFactory factory = getConnectionFactory();
        RedisConnection connection = preProcessConnection(RedisConnectionUtils.doGetConnection(factory, true, false, false), false);
        return callback.doInRedis2(connection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object executeSession(SessionCallback<?> session) {
        return session.execute(this);
    }

    protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
        Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(pm.getClass(), getClass().getClassLoader());
        return (RedisConnection) Proxy.newProxyInstance(pm.getClass().getClassLoader(), ifcs, new CloseSuppressingInvocationHandler(pm));
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return connection;
    }

    protected <T> T postProcessResult(T result, RedisConnection conn, boolean existingConnection) {
        return result;
    }

    public boolean isExposeConnection() {
        return this.exposeConnection;
    }

    public void setExposeConnection(boolean exposeConnection) {
        this.exposeConnection = exposeConnection;
    }

    public boolean isEnableDefaultSerializer() {
        return this.enableDefaultSerializer;
    }

    public void setEnableDefaultSerializer(boolean enableDefaultSerializer) {
        this.enableDefaultSerializer = enableDefaultSerializer;
    }

    public RedisSerializer<?> getDefaultSerializer() {
        return this.defaultSerializer;
    }

    public void setDefaultSerializer(RedisSerializer<?> serializer) {
        this.defaultSerializer = serializer;
    }

    public void setKeySerializer(RedisSerializer<?> serializer) {
        this.keySerializer = serializer;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public RedisSerializer<?> getKeySerializer() {
        return this.keySerializer;
    }

    public void setValueSerializer(RedisSerializer<?> serializer) {
        this.valueSerializer = serializer;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public RedisSerializer<?> getValueSerializer() {
        return this.valueSerializer;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public RedisSerializer<?> getHashKeySerializer() {
        return this.hashKeySerializer;
    }

    public void setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
        this.hashKeySerializer = hashKeySerializer;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public RedisSerializer<?> getHashValueSerializer() {
        return this.hashValueSerializer;
    }

    public void setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
        this.hashValueSerializer = hashValueSerializer;
    }

    public RedisSerializer<String> getStringSerializer() {
        return this.stringSerializer;
    }

    public void setStringSerializer(RedisSerializer<String> stringSerializer) {
        this.stringSerializer = stringSerializer;
    }

    public void setScriptExecutor(ScriptExecutor<K> scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
    }

    private byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        if (this.keySerializer == null && (key instanceof byte[])) {
            return (byte[]) key;
        }
        return this.keySerializer.serialize(key);
    }

    private byte[] rawString(String key) {
        return this.stringSerializer.serialize(key);
    }

    private byte[] rawValue(Object value) {
        if (this.valueSerializer == null && (value instanceof byte[])) {
            return (byte[]) value;
        }
        return this.valueSerializer.serialize(value);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    private byte[][] rawKeys(Collection<K> keys) {
        ?? r0 = new byte[keys.size()];
        int i = 0;
        for (K key : keys) {
            int i2 = i;
            i++;
            r0[i2] = rawKey(key);
        }
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private K deserializeKey(byte[] bArr) {
        return this.keySerializer != null ? (K) this.keySerializer.deserialize(bArr) : bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<Object> deserializeMixedResults(List<Object> rawValues, RedisSerializer valueSerializer, RedisSerializer hashKeySerializer, RedisSerializer hashValueSerializer) {
        if (rawValues == null) {
            return null;
        }
        List<Object> values = new ArrayList<>();
        for (Object rawValue : rawValues) {
            if ((rawValue instanceof byte[]) && valueSerializer != null) {
                values.add(valueSerializer.deserialize((byte[]) rawValue));
            } else if (rawValue instanceof List) {
                values.add(deserializeMixedResults((List) rawValue, valueSerializer, hashKeySerializer, hashValueSerializer));
            } else if ((rawValue instanceof Set) && !((Set) rawValue).isEmpty()) {
                values.add(deserializeSet((Set) rawValue, valueSerializer));
            } else if ((rawValue instanceof Map) && !((Map) rawValue).isEmpty() && (((Map) rawValue).values().iterator().next() instanceof byte[])) {
                values.add(SerializationUtils.deserialize((Map) rawValue, hashKeySerializer, hashValueSerializer));
            } else {
                values.add(rawValue);
            }
        }
        return values;
    }

    private Set<?> deserializeSet(Set rawSet, RedisSerializer valueSerializer) {
        if (rawSet.isEmpty()) {
            return rawSet;
        }
        Object setValue = rawSet.iterator().next();
        if ((setValue instanceof byte[]) && valueSerializer != null) {
            return SerializationUtils.deserialize((Set<byte[]>) rawSet, valueSerializer);
        }
        if (setValue instanceof RedisZSetCommands.Tuple) {
            return convertTupleValues(rawSet, valueSerializer);
        }
        return rawSet;
    }

    private Set<ZSetOperations.TypedTuple<V>> convertTupleValues(Set<RedisZSetCommands.Tuple> rawValues, RedisSerializer valueSerializer) throws SerializationException {
        Set<ZSetOperations.TypedTuple<V>> set = new LinkedHashSet<>(rawValues.size());
        for (RedisZSetCommands.Tuple rawValue : rawValues) {
            Object value = rawValue.getValue();
            if (valueSerializer != null) {
                value = valueSerializer.deserialize(rawValue.getValue());
            }
            set.add(new DefaultTypedTuple<>(value, rawValue.getScore()));
        }
        return set;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<Object> exec() {
        List<Object> results = execRaw();
        if (getConnectionFactory().getConvertPipelineAndTxResults()) {
            return deserializeMixedResults(results, this.valueSerializer, this.hashKeySerializer, this.hashValueSerializer);
        }
        return results;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<Object> exec(RedisSerializer<?> valueSerializer) {
        return deserializeMixedResults(execRaw(), valueSerializer, valueSerializer, valueSerializer);
    }

    protected List<Object> execRaw() {
        return (List) execute(new RedisCallback<List<Object>>() { // from class: org.springframework.data.redis.core.RedisTemplate.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<Object> doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.exec();
            }
        });
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void delete(K key) {
        final byte[] rawKey = rawKey(key);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.4
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection redisConnection) {
                redisConnection.del(new byte[]{rawKey});
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void delete(Collection<K> keys) {
        if (CollectionUtils.isEmpty((Collection<?>) keys)) {
            return;
        }
        final byte[][] rawKeys = rawKeys(keys);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.5
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.del(rawKeys);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Boolean hasKey(K key) {
        final byte[] rawKey = rawKey(key);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.RedisTemplate.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.exists(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Boolean expire(K key, final long timeout, final TimeUnit unit) {
        final byte[] rawKey = rawKey(key);
        final long rawTimeout = TimeoutUtils.toMillis(timeout, unit);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.RedisTemplate.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                try {
                    return connection.pExpire(rawKey, rawTimeout);
                } catch (Exception e) {
                    return connection.expire(rawKey, TimeoutUtils.toSeconds(timeout, unit));
                }
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Boolean expireAt(K key, final Date date) {
        final byte[] rawKey = rawKey(key);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.RedisTemplate.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                try {
                    return connection.pExpireAt(rawKey, date.getTime());
                } catch (Exception e) {
                    return connection.expireAt(rawKey, date.getTime() / 1000);
                }
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void convertAndSend(String channel, Object message) {
        Assert.hasText(channel, "a non-empty channel is required");
        final byte[] rawChannel = rawString(channel);
        final byte[] rawMessage = rawValue(message);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.9
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.publish(rawChannel, rawMessage);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Long getExpire(K key) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.RedisTemplate.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                return connection.ttl(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Long getExpire(K key, final TimeUnit timeUnit) {
        final byte[] rawKey = rawKey(key);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.RedisTemplate.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) {
                try {
                    return connection.pTtl(rawKey, timeUnit);
                } catch (Exception e) {
                    return connection.ttl(rawKey, timeUnit);
                }
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Set<K> keys(K pattern) {
        final byte[] rawKey = rawKey(pattern);
        Set<K> set = (Set) execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.RedisTemplate.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) {
                return connection.keys(rawKey);
            }
        }, true);
        return this.keySerializer != null ? SerializationUtils.deserialize((Set<byte[]>) set, this.keySerializer) : set;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Boolean persist(K key) {
        final byte[] rawKey = rawKey(key);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.RedisTemplate.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.persist(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Boolean move(K key, final int dbIndex) {
        final byte[] rawKey = rawKey(key);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.RedisTemplate.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.move(rawKey, dbIndex);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public K randomKey() {
        byte[] rawKey = (byte[]) execute(new RedisCallback<byte[]>() { // from class: org.springframework.data.redis.core.RedisTemplate.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public byte[] doInRedis2(RedisConnection connection) {
                return connection.randomKey();
            }
        }, true);
        return deserializeKey(rawKey);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void rename(K oldKey, K newKey) {
        final byte[] rawOldKey = rawKey(oldKey);
        final byte[] rawNewKey = rawKey(newKey);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.16
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.rename(rawOldKey, rawNewKey);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Boolean renameIfAbsent(K oldKey, K newKey) {
        final byte[] rawOldKey = rawKey(oldKey);
        final byte[] rawNewKey = rawKey(newKey);
        return (Boolean) execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.RedisTemplate.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) {
                return connection.renameNX(rawOldKey, rawNewKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public DataType type(K key) {
        final byte[] rawKey = rawKey(key);
        return (DataType) execute(new RedisCallback<DataType>() { // from class: org.springframework.data.redis.core.RedisTemplate.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public DataType doInRedis2(RedisConnection connection) {
                return connection.type(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public byte[] dump(K key) {
        final byte[] rawKey = rawKey(key);
        return (byte[]) execute(new RedisCallback<byte[]>() { // from class: org.springframework.data.redis.core.RedisTemplate.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public byte[] doInRedis2(RedisConnection connection) {
                return connection.dump(rawKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void restore(K key, final byte[] value, long timeToLive, TimeUnit unit) {
        final byte[] rawKey = rawKey(key);
        final long rawTimeout = TimeoutUtils.toMillis(timeToLive, unit);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.20
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis, reason: merged with bridge method [inline-methods] */
            public Object doInRedis2(RedisConnection connection) {
                connection.restore(rawKey, rawTimeout, value);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void multi() {
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.21
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) throws DataAccessException {
                connection.multi();
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void discard() {
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.22
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) throws DataAccessException {
                connection.discard();
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void watch(K key) {
        final byte[] rawKey = rawKey(key);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.23
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection redisConnection) {
                redisConnection.watch(new byte[]{rawKey});
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void watch(Collection<K> keys) {
        final byte[][] rawKeys = rawKeys(keys);
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.24
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) {
                connection.watch(rawKeys);
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void unwatch() {
        execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisTemplate.25
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection connection) throws DataAccessException {
                connection.unwatch();
                return null;
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<V> sort(SortQuery<K> sortQuery) {
        return (List<V>) sort((SortQuery) sortQuery, (RedisSerializer) this.valueSerializer);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <T> List<T> sort(SortQuery<K> query, RedisSerializer<T> resultSerializer) {
        final byte[] rawKey = rawKey(query.getKey());
        final SortParameters params = QueryUtils.convertQuery(query, this.stringSerializer);
        List<byte[]> vals = (List) execute(new RedisCallback<List<byte[]>>() { // from class: org.springframework.data.redis.core.RedisTemplate.26
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<byte[]> doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.sort(rawKey, params);
            }
        }, true);
        return SerializationUtils.deserialize(vals, (RedisSerializer) resultSerializer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.redis.core.RedisOperations
    public <T> List<T> sort(SortQuery<K> query, BulkMapper<T, V> bulkMapper) {
        return sort(query, bulkMapper, this.valueSerializer);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.redis.core.RedisOperations
    public <T, S> List<T> sort(SortQuery<K> query, BulkMapper<T, S> bulkMapper, RedisSerializer<S> redisSerializer) {
        List<T> listSort = sort((SortQuery) query, (RedisSerializer) redisSerializer);
        if (listSort == null || listSort.isEmpty()) {
            return Collections.emptyList();
        }
        int bulkSize = query.getGetPattern().size();
        List<T> result = new ArrayList<>((listSort.size() / bulkSize) + 1);
        List<S> bulk = new ArrayList<>(bulkSize);
        Iterator<T> it = listSort.iterator();
        while (it.hasNext()) {
            bulk.add(it.next());
            if (bulk.size() == bulkSize) {
                result.add(bulkMapper.mapBulk(Collections.unmodifiableList(bulk)));
                bulk = new ArrayList<>(bulkSize);
            }
        }
        return result;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public Long sort(SortQuery<K> query, K storeKey) {
        final byte[] rawStoreKey = rawKey(storeKey);
        final byte[] rawKey = rawKey(query.getKey());
        final SortParameters params = QueryUtils.convertQuery(query, this.stringSerializer);
        return (Long) execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.RedisTemplate.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.sort(rawKey, params, rawStoreKey);
            }
        }, true);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public BoundValueOperations<K, V> boundValueOps(K key) {
        return new DefaultBoundValueOperations(key, this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public ValueOperations<K, V> opsForValue() {
        if (this.valueOps == null) {
            this.valueOps = new DefaultValueOperations(this);
        }
        return this.valueOps;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public ListOperations<K, V> opsForList() {
        if (this.listOps == null) {
            this.listOps = new DefaultListOperations(this);
        }
        return this.listOps;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public BoundListOperations<K, V> boundListOps(K key) {
        return new DefaultBoundListOperations(key, this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public BoundSetOperations<K, V> boundSetOps(K key) {
        return new DefaultBoundSetOperations(key, this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public SetOperations<K, V> opsForSet() {
        if (this.setOps == null) {
            this.setOps = new DefaultSetOperations(this);
        }
        return this.setOps;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public BoundZSetOperations<K, V> boundZSetOps(K key) {
        return new DefaultBoundZSetOperations(key, this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public ZSetOperations<K, V> opsForZSet() {
        if (this.zSetOps == null) {
            this.zSetOps = new DefaultZSetOperations(this);
        }
        return this.zSetOps;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public GeoOperations<K, V> opsForGeo() {
        if (this.geoOps == null) {
            this.geoOps = new DefaultGeoOperations(this);
        }
        return this.geoOps;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public BoundGeoOperations<K, V> boundGeoOps(K key) {
        return new DefaultBoundGeoOperations(key, this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public HyperLogLogOperations<K, V> opsForHyperLogLog() {
        if (this.hllOps == null) {
            this.hllOps = new DefaultHyperLogLogOperations(this);
        }
        return this.hllOps;
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K key) {
        return new DefaultBoundHashOperations(key, this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
        return new DefaultHashOperations(this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public ClusterOperations<K, V> opsForCluster() {
        return new DefaultClusterOperations(this);
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void killClient(final String host, final int port) {
        execute(new RedisCallback<Void>() { // from class: org.springframework.data.redis.core.RedisTemplate.28
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Void doInRedis2(RedisConnection connection) throws DataAccessException {
                connection.killClient(host, port);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public List<RedisClientInfo> getClientList() {
        return (List) execute(new RedisCallback<List<RedisClientInfo>>() { // from class: org.springframework.data.redis.core.RedisTemplate.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public List<RedisClientInfo> doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.getClientList();
            }
        });
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void slaveOf(final String host, final int port) {
        execute(new RedisCallback<Void>() { // from class: org.springframework.data.redis.core.RedisTemplate.30
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Void doInRedis2(RedisConnection connection) throws DataAccessException {
                connection.slaveOf(host, port);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.RedisOperations
    public void slaveOfNoOne() {
        execute(new RedisCallback<Void>() { // from class: org.springframework.data.redis.core.RedisTemplate.31
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Void doInRedis2(RedisConnection connection) throws DataAccessException {
                connection.slaveOfNoOne();
                return null;
            }
        });
    }

    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        this.enableTransactionSupport = enableTransactionSupport;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
