package org.springframework.data.redis.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.convert.CustomConversions;
import org.springframework.data.redis.core.convert.GeoIndexedPropertyValue;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.data.redis.core.convert.PathIndexResolver;
import org.springframework.data.redis.core.convert.RedisConverter;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.data.redis.core.convert.ReferenceResolverImpl;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.core.mapping.RedisPersistentEntity;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyValueAdapter.class */
public class RedisKeyValueAdapter extends AbstractKeyValueAdapter implements InitializingBean, ApplicationContextAware, ApplicationListener<RedisKeyspaceEvent> {
    private RedisOperations<?, ?> redisOps;
    private RedisConverter converter;
    private RedisMessageListenerContainer messageListenerContainer;
    private final AtomicReference<KeyExpirationEventMessageListener> expirationListener;
    private ApplicationEventPublisher eventPublisher;
    private EnableKeyspaceEvents enableKeyspaceEvents;
    private String keyspaceNotificationsConfigParameter;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyValueAdapter$EnableKeyspaceEvents.class */
    public enum EnableKeyspaceEvents {
        ON_STARTUP,
        ON_DEMAND,
        OFF
    }

    public RedisKeyValueAdapter(RedisOperations<?, ?> redisOps) {
        this(redisOps, new RedisMappingContext());
    }

    public RedisKeyValueAdapter(RedisOperations<?, ?> redisOps, RedisMappingContext mappingContext) {
        this(redisOps, mappingContext, new CustomConversions());
    }

    public RedisKeyValueAdapter(RedisOperations<?, ?> redisOps, RedisMappingContext mappingContext, CustomConversions customConversions) {
        super(new RedisQueryEngine());
        this.expirationListener = new AtomicReference<>(null);
        this.enableKeyspaceEvents = EnableKeyspaceEvents.OFF;
        this.keyspaceNotificationsConfigParameter = null;
        Assert.notNull(redisOps, "RedisOperations must not be null!");
        Assert.notNull(mappingContext, "RedisMappingContext must not be null!");
        MappingRedisConverter mappingConverter = new MappingRedisConverter(mappingContext, new PathIndexResolver(mappingContext), new ReferenceResolverImpl(redisOps));
        mappingConverter.setCustomConversions(customConversions == null ? new CustomConversions() : customConversions);
        mappingConverter.afterPropertiesSet();
        this.converter = mappingConverter;
        this.redisOps = redisOps;
        initMessageListenerContainer();
    }

    public RedisKeyValueAdapter(RedisOperations<?, ?> redisOps, RedisConverter redisConverter) {
        super(new RedisQueryEngine());
        this.expirationListener = new AtomicReference<>(null);
        this.enableKeyspaceEvents = EnableKeyspaceEvents.OFF;
        this.keyspaceNotificationsConfigParameter = null;
        Assert.notNull(redisOps, "RedisOperations must not be null!");
        this.converter = redisConverter;
        this.redisOps = redisOps;
        initMessageListenerContainer();
    }

    protected RedisKeyValueAdapter() {
        this.expirationListener = new AtomicReference<>(null);
        this.enableKeyspaceEvents = EnableKeyspaceEvents.OFF;
        this.keyspaceNotificationsConfigParameter = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Object put(Serializable id, Object item, Serializable keyspace) {
        final RedisData rdo = item instanceof RedisData ? (RedisData) item : new RedisData();
        if (!(item instanceof RedisData)) {
            this.converter.write(item, rdo);
        }
        if (ObjectUtils.nullSafeEquals(EnableKeyspaceEvents.ON_DEMAND, this.enableKeyspaceEvents) && this.expirationListener.get() == null && rdo.getTimeToLive() != null && rdo.getTimeToLive().longValue() > 0) {
            initKeyExpirationListener();
        }
        if (rdo.getId() == null) {
            rdo.setId((String) this.converter.getConversionService().convert(id, String.class));
            if (!(item instanceof RedisData)) {
                KeyValuePersistentProperty idProperty = (KeyValuePersistentProperty) this.converter.getMappingContext().getPersistentEntity(item.getClass()).getIdProperty();
                this.converter.getMappingContext().getPersistentEntity(item.getClass()).getPropertyAccessor(item).setProperty(idProperty, id);
            }
        }
        this.redisOps.execute(new RedisCallback<Object>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v20, types: [byte[], byte[][]] */
            /* JADX WARN: Type inference failed for: r1v7, types: [byte[], byte[][]] */
            /* JADX WARN: Type inference failed for: r2v12, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Object doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                byte[] key = RedisKeyValueAdapter.this.toBytes(rdo.getId());
                byte[] objectKey = RedisKeyValueAdapter.this.createKey(rdo.getKeyspace(), rdo.getId());
                boolean isNew = redisConnection.del(new byte[]{objectKey}).longValue() == 0;
                redisConnection.hMSet(objectKey, rdo.getBucket().rawMap());
                if (rdo.getTimeToLive() != null && rdo.getTimeToLive().longValue() > 0) {
                    redisConnection.expire(objectKey, rdo.getTimeToLive().longValue());
                    byte[] phantomKey = ByteUtils.concat(objectKey, MappingRedisConverter.BinaryKeyspaceIdentifier.PHANTOM_SUFFIX);
                    redisConnection.del(new byte[]{phantomKey});
                    redisConnection.hMSet(phantomKey, rdo.getBucket().rawMap());
                    redisConnection.expire(phantomKey, rdo.getTimeToLive().longValue() + 300);
                }
                redisConnection.sAdd(RedisKeyValueAdapter.this.toBytes(rdo.getKeyspace()), new byte[]{key});
                IndexWriter indexWriter = new IndexWriter(redisConnection, RedisKeyValueAdapter.this.converter);
                if (isNew) {
                    indexWriter.createIndexes(key, rdo.getIndexedData());
                    return null;
                }
                indexWriter.deleteAndUpdateIndexes(key, rdo.getIndexedData());
                return null;
            }
        });
        return item;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public boolean contains(final Serializable id, final Serializable keyspace) {
        Boolean exists = (Boolean) this.redisOps.execute(new RedisCallback<Boolean>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Boolean doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.sIsMember(RedisKeyValueAdapter.this.toBytes(keyspace), RedisKeyValueAdapter.this.toBytes(id));
            }
        });
        if (exists != null) {
            return exists.booleanValue();
        }
        return false;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Object get(Serializable id, Serializable keyspace) {
        return get(id, keyspace, Object.class);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.data.keyvalue.core.AbstractKeyValueAdapter, org.springframework.data.keyvalue.core.KeyValueAdapter
    public <T> T get(Serializable serializable, Serializable serializable2, Class<T> cls) {
        String strAsString = asString(serializable);
        String strAsString2 = asString(serializable2);
        final byte[] bArrCreateKey = createKey(strAsString2, strAsString);
        RedisData redisData = new RedisData((Map<byte[], byte[]>) this.redisOps.execute(new RedisCallback<Map<byte[], byte[]>>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Map<byte[], byte[]> doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.hGetAll(bArrCreateKey);
            }
        }));
        redisData.setId(strAsString);
        redisData.setKeyspace(strAsString2);
        return (T) readBackTimeToLiveIfSet(bArrCreateKey, this.converter.read(cls, redisData));
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public Object delete(Serializable id, Serializable keyspace) {
        return delete(id, keyspace, Object.class);
    }

    @Override // org.springframework.data.keyvalue.core.AbstractKeyValueAdapter, org.springframework.data.keyvalue.core.KeyValueAdapter
    public <T> T delete(Serializable serializable, final Serializable serializable2, Class<T> cls) {
        final byte[] bytes = toBytes(serializable);
        final byte[] bytes2 = toBytes(serializable2);
        T t = (T) get(serializable, serializable2, cls);
        if (t != null) {
            final byte[] bArrCreateKey = createKey(asString(serializable2), asString(serializable));
            this.redisOps.execute(new RedisCallback<Void>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.4
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
                /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
                @Override // org.springframework.data.redis.core.RedisCallback
                /* renamed from: doInRedis */
                public Void doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.del(new byte[]{bArrCreateKey});
                    redisConnection.sRem(bytes2, new byte[]{bytes});
                    new IndexWriter(redisConnection, RedisKeyValueAdapter.this.converter).removeKeyFromIndexes(RedisKeyValueAdapter.this.asString(serializable2), bytes);
                    return null;
                }
            });
        }
        return t;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public List<?> getAllOf(Serializable keyspace) {
        return getAllOf(keyspace, -1, -1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [byte[], java.io.Serializable] */
    public List<?> getAllOf(Serializable keyspace, int offset, int rows) {
        final byte[] binKeyspace = toBytes(keyspace);
        Set<byte[]> ids = (Set) this.redisOps.execute(new RedisCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Set<byte[]> doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.sMembers(binKeyspace);
            }
        });
        List<Object> result = new ArrayList<>();
        List<byte[]> keys = new ArrayList<>(ids);
        if (keys.isEmpty() || keys.size() < offset) {
            return Collections.emptyList();
        }
        int offset2 = Math.max(0, offset);
        if (offset2 >= 0 && rows > 0) {
            keys = keys.subList(offset2, Math.min(offset2 + rows, keys.size()));
        }
        Iterator<byte[]> it = keys.iterator();
        while (it.hasNext()) {
            result.add(get(it.next(), keyspace));
        }
        return result;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public void deleteAllOf(final Serializable keyspace) {
        this.redisOps.execute(new RedisCallback<Void>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.6
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Void doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.del(new byte[]{RedisKeyValueAdapter.this.toBytes(keyspace)});
                new IndexWriter(redisConnection, RedisKeyValueAdapter.this.converter).removeAllIndexes(RedisKeyValueAdapter.this.asString(keyspace));
                return null;
            }
        });
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public CloseableIterator<Map.Entry<Serializable, Object>> entries(Serializable keyspace) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public long count(final Serializable keyspace) {
        Long count = (Long) this.redisOps.execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) throws DataAccessException {
                return connection.sCard(RedisKeyValueAdapter.this.toBytes(keyspace));
            }
        });
        if (count != null) {
            return count.longValue();
        }
        return 0L;
    }

    public void update(final PartialUpdate<?> update) {
        RedisPersistentEntity<?> entity = this.converter.getMappingContext().getPersistentEntity(update.getTarget());
        final String keyspace = entity.getKeySpace();
        final Object id = update.getId();
        final byte[] redisKey = createKey(keyspace, (String) this.converter.getConversionService().convert(id, String.class));
        final RedisData rdo = new RedisData();
        this.converter.write(update, rdo);
        this.redisOps.execute(new RedisCallback<Void>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.8
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v27, types: [byte[], byte[][]] */
            /* JADX WARN: Type inference failed for: r2v29, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Void doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                RedisUpdateObject redisUpdateObject = new RedisUpdateObject(redisKey, keyspace, id);
                for (PartialUpdate.PropertyUpdate pUpdate : update.getPropertyUpdates()) {
                    String propertyPath = pUpdate.getPropertyPath();
                    if (PartialUpdate.UpdateCommand.DEL.equals(pUpdate.getCmd()) || (pUpdate.getValue() instanceof Collection) || (pUpdate.getValue() instanceof Map) || ((pUpdate.getValue() != null && pUpdate.getValue().getClass().isArray()) || (pUpdate.getValue() != null && !RedisKeyValueAdapter.this.converter.getConversionService().canConvert(pUpdate.getValue().getClass(), byte[].class)))) {
                        redisUpdateObject = RedisKeyValueAdapter.this.fetchDeletePathsFromHashAndUpdateIndex(redisUpdateObject, propertyPath, redisConnection);
                    }
                }
                if (!redisUpdateObject.fieldsToRemove.isEmpty()) {
                    redisConnection.hDel(redisKey, (byte[][]) redisUpdateObject.fieldsToRemove.toArray((Object[]) new byte[redisUpdateObject.fieldsToRemove.size()]));
                }
                for (RedisUpdateObject.Index index : redisUpdateObject.indexesToUpdate) {
                    if (ObjectUtils.nullSafeEquals(DataType.ZSET, index.type)) {
                        redisConnection.zRem(index.key, new byte[]{RedisKeyValueAdapter.this.toBytes(redisUpdateObject.targetId)});
                    } else {
                        redisConnection.sRem(index.key, new byte[]{RedisKeyValueAdapter.this.toBytes(redisUpdateObject.targetId)});
                    }
                }
                if (!rdo.getBucket().isEmpty() && (rdo.getBucket().size() > 1 || (rdo.getBucket().size() == 1 && !rdo.getBucket().asMap().containsKey(ChangeSetPersister.CLASS_KEY)))) {
                    redisConnection.hMSet(redisKey, rdo.getBucket().rawMap());
                }
                if (update.isRefreshTtl()) {
                    if (rdo.getTimeToLive() != null && rdo.getTimeToLive().longValue() > 0) {
                        redisConnection.expire(redisKey, rdo.getTimeToLive().longValue());
                        byte[] phantomKey = ByteUtils.concat(redisKey, MappingRedisConverter.BinaryKeyspaceIdentifier.PHANTOM_SUFFIX);
                        redisConnection.hMSet(phantomKey, rdo.getBucket().rawMap());
                        redisConnection.expire(phantomKey, rdo.getTimeToLive().longValue() + 300);
                    } else {
                        redisConnection.persist(redisKey);
                        redisConnection.persist(ByteUtils.concat(redisKey, MappingRedisConverter.BinaryKeyspaceIdentifier.PHANTOM_SUFFIX));
                    }
                }
                new IndexWriter(redisConnection, RedisKeyValueAdapter.this.converter).updateIndexes(RedisKeyValueAdapter.this.toBytes(id), rdo.getIndexedData());
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v21, types: [byte[], java.io.Serializable, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r0v37, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r0v50, types: [byte[], byte[][]] */
    public RedisUpdateObject fetchDeletePathsFromHashAndUpdateIndex(RedisUpdateObject redisUpdateObject, String path, RedisConnection connection) {
        redisUpdateObject.addFieldToRemove(toBytes(path));
        byte[] value = connection.hGet(redisUpdateObject.targetKey, toBytes(path));
        if (value != null && value.length > 0) {
            byte[] existingValueIndexKey = value != null ? ByteUtils.concatAll(new byte[]{toBytes(redisUpdateObject.keyspace), toBytes(":" + path), toBytes(":"), value}) : null;
            if (connection.exists(existingValueIndexKey).booleanValue()) {
                redisUpdateObject.addIndexToUpdate(new RedisUpdateObject.Index(existingValueIndexKey, DataType.SET));
            }
            return redisUpdateObject;
        }
        Set<byte[]> existingFields = connection.hKeys(redisUpdateObject.targetKey);
        for (byte[] bArr : existingFields) {
            if (asString(bArr).startsWith(path + ".")) {
                redisUpdateObject.addFieldToRemove(bArr);
                byte[] value2 = connection.hGet(redisUpdateObject.targetKey, toBytes(bArr));
                if (value2 != null) {
                    byte[] existingValueIndexKey2 = value2 != null ? ByteUtils.concatAll(new byte[]{toBytes(redisUpdateObject.keyspace), toBytes(":"), bArr, toBytes(":"), value2}) : null;
                    if (connection.exists(existingValueIndexKey2).booleanValue()) {
                        redisUpdateObject.addIndexToUpdate(new RedisUpdateObject.Index(existingValueIndexKey2, DataType.SET));
                    }
                }
            }
        }
        String pathToUse = GeoIndexedPropertyValue.geoIndexName(path);
        byte[] existingGeoIndexKey = ByteUtils.concatAll(new byte[]{toBytes(redisUpdateObject.keyspace), toBytes(":"), toBytes(pathToUse)});
        if (connection.zRank(existingGeoIndexKey, toBytes(redisUpdateObject.targetId)) != null) {
            redisUpdateObject.addIndexToUpdate(new RedisUpdateObject.Index(existingGeoIndexKey, DataType.ZSET));
        }
        return redisUpdateObject;
    }

    public <T> T execute(RedisCallback<T> redisCallback) {
        return (T) this.redisOps.execute(redisCallback);
    }

    public RedisConverter getConverter() {
        return this.converter;
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueAdapter
    public void clear() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String asString(Serializable value) {
        return value instanceof String ? (String) value : (String) getConverter().getConversionService().convert(value, String.class);
    }

    public byte[] createKey(String keyspace, String id) {
        return toBytes(keyspace + ":" + id);
    }

    public byte[] toBytes(Object source) {
        if (source instanceof byte[]) {
            return (byte[]) source;
        }
        return (byte[]) this.converter.getConversionService().convert(source, byte[].class);
    }

    private <T> T readBackTimeToLiveIfSet(final byte[] key, T target) {
        if (target == null || key == null) {
            return target;
        }
        RedisPersistentEntity<?> entity = this.converter.getMappingContext().getPersistentEntity(target.getClass());
        if (entity.hasExplictTimeToLiveProperty()) {
            PersistentProperty<?> ttlProperty = entity.getExplicitTimeToLiveProperty();
            final TimeToLive ttl = (TimeToLive) ttlProperty.findAnnotation(TimeToLive.class);
            Long timeout = (Long) this.redisOps.execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.9
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.springframework.data.redis.core.RedisCallback
                /* renamed from: doInRedis */
                public Long doInRedis2(RedisConnection connection) throws DataAccessException {
                    if (ObjectUtils.nullSafeEquals(TimeUnit.SECONDS, ttl.unit())) {
                        return connection.ttl(key);
                    }
                    return connection.pTtl(key, ttl.unit());
                }
            });
            if (timeout != null || !ttlProperty.getType().isPrimitive()) {
                entity.getPropertyAccessor(target).setProperty(ttlProperty, this.converter.getConversionService().convert(timeout, ttlProperty.getType()));
            }
        }
        return target;
    }

    public void setEnableKeyspaceEvents(EnableKeyspaceEvents enableKeyspaceEvents) {
        this.enableKeyspaceEvents = enableKeyspaceEvents;
    }

    public void setKeyspaceNotificationsConfigParameter(String keyspaceNotificationsConfigParameter) {
        this.keyspaceNotificationsConfigParameter = keyspaceNotificationsConfigParameter;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (ObjectUtils.nullSafeEquals(EnableKeyspaceEvents.ON_STARTUP, this.enableKeyspaceEvents)) {
            initKeyExpirationListener();
        }
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        if (this.expirationListener.get() != null) {
            this.expirationListener.get().destroy();
        }
        if (this.messageListenerContainer != null) {
            this.messageListenerContainer.destroy();
        }
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(RedisKeyspaceEvent event) {
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.eventPublisher = applicationContext;
    }

    private void initMessageListenerContainer() {
        this.messageListenerContainer = new RedisMessageListenerContainer();
        this.messageListenerContainer.setConnectionFactory(((RedisTemplate) this.redisOps).getConnectionFactory());
        this.messageListenerContainer.afterPropertiesSet();
        this.messageListenerContainer.start();
    }

    private void initKeyExpirationListener() {
        if (this.expirationListener.get() == null) {
            MappingExpirationListener listener = new MappingExpirationListener(this.messageListenerContainer, this.redisOps, this.converter);
            listener.setKeyspaceNotificationsConfigParameter(this.keyspaceNotificationsConfigParameter);
            if (this.eventPublisher != null) {
                listener.setApplicationEventPublisher(this.eventPublisher);
            }
            if (this.expirationListener.compareAndSet(null, listener)) {
                listener.init();
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyValueAdapter$MappingExpirationListener.class */
    static class MappingExpirationListener extends KeyExpirationEventMessageListener {
        private final RedisOperations<?, ?> ops;
        private final RedisConverter converter;

        public MappingExpirationListener(RedisMessageListenerContainer listenerContainer, RedisOperations<?, ?> ops, RedisConverter converter) {
            super(listenerContainer);
            this.ops = ops;
            this.converter = converter;
        }

        @Override // org.springframework.data.redis.listener.KeyspaceEventMessageListener, org.springframework.data.redis.connection.MessageListener
        public void onMessage(Message message, byte[] pattern) {
            if (!isKeyExpirationMessage(message)) {
                return;
            }
            byte[] key = message.getBody();
            final byte[] phantomKey = ByteUtils.concat(key, (byte[]) this.converter.getConversionService().convert(MappingRedisConverter.KeyspaceIdentifier.PHANTOM_SUFFIX, byte[].class));
            Map<byte[], byte[]> hash = (Map) this.ops.execute(new RedisCallback<Map<byte[], byte[]>>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.MappingExpirationListener.1
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Type inference failed for: r1v3, types: [byte[], byte[][]] */
                @Override // org.springframework.data.redis.core.RedisCallback
                /* renamed from: doInRedis */
                public Map<byte[], byte[]> doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                    Map<byte[], byte[]> hash2 = redisConnection.hGetAll(phantomKey);
                    if (!CollectionUtils.isEmpty((Map<?, ?>) hash2)) {
                        redisConnection.del(new byte[]{phantomKey});
                    }
                    return hash2;
                }
            });
            Object value = this.converter.read(Object.class, new RedisData(hash));
            String channel = !ObjectUtils.isEmpty(message.getChannel()) ? (String) this.converter.getConversionService().convert(message.getChannel(), String.class) : null;
            final RedisKeyExpiredEvent event = new RedisKeyExpiredEvent(channel, key, value);
            this.ops.execute(new RedisCallback<Void>() { // from class: org.springframework.data.redis.core.RedisKeyValueAdapter.MappingExpirationListener.2
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Type inference failed for: r2v4, types: [byte[], byte[][]] */
                @Override // org.springframework.data.redis.core.RedisCallback
                /* renamed from: doInRedis */
                public Void doInRedis2(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.sRem((byte[]) MappingExpirationListener.this.converter.getConversionService().convert(event.getKeyspace(), byte[].class), new byte[]{event.getId()});
                    new IndexWriter(redisConnection, MappingExpirationListener.this.converter).removeKeyFromIndexes(event.getKeyspace(), event.getId());
                    return null;
                }
            });
            publishEvent(event);
        }

        private boolean isKeyExpirationMessage(Message message) {
            if (message == null || message.getChannel() == null || message.getBody() == null) {
                return false;
            }
            return MappingRedisConverter.BinaryKeyspaceIdentifier.isValid(message.getBody());
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyValueAdapter$RedisUpdateObject.class */
    private static class RedisUpdateObject {
        private final String keyspace;
        private final Object targetId;
        private final byte[] targetKey;
        private Set<byte[]> fieldsToRemove = new LinkedHashSet();
        private Set<Index> indexesToUpdate = new LinkedHashSet();

        RedisUpdateObject(byte[] targetKey, String keyspace, Object targetId) {
            this.targetKey = targetKey;
            this.keyspace = keyspace;
            this.targetId = targetId;
        }

        void addFieldToRemove(byte[] field) {
            this.fieldsToRemove.add(field);
        }

        void addIndexToUpdate(Index index) {
            this.indexesToUpdate.add(index);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyValueAdapter$RedisUpdateObject$Index.class */
        static class Index {
            final DataType type;
            final byte[] key;

            public Index(byte[] key, DataType type) {
                this.key = key;
                this.type = type;
            }
        }
    }
}
