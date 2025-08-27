package org.springframework.data.redis.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.keyvalue.core.KeyValueAdapter;
import org.springframework.data.keyvalue.core.KeyValueCallback;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyValueTemplate.class */
public class RedisKeyValueTemplate extends KeyValueTemplate {
    public RedisKeyValueTemplate(RedisKeyValueAdapter adapter, RedisMappingContext mappingContext) {
        super(adapter, mappingContext);
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueTemplate, org.springframework.data.keyvalue.core.KeyValueOperations
    public RedisMappingContext getMappingContext() {
        return (RedisMappingContext) super.getMappingContext();
    }

    public <T> List<T> find(final RedisCallback<?> callback, final Class<T> type) {
        Assert.notNull(callback, "Callback must not be null.");
        return (List) execute(new RedisKeyValueCallback<List<T>>() { // from class: org.springframework.data.redis.core.RedisKeyValueTemplate.1
            @Override // org.springframework.data.redis.core.RedisKeyValueTemplate.RedisKeyValueCallback
            public List<T> doInRedis(RedisKeyValueAdapter adapter) {
                Object callbackResult = adapter.execute(callback);
                if (callbackResult == null) {
                    return Collections.emptyList();
                }
                Iterable<?> ids = ClassUtils.isAssignable(Iterable.class, callbackResult.getClass()) ? (Iterable) callbackResult : Collections.singleton(callbackResult);
                ArrayList arrayList = new ArrayList();
                for (Object id : ids) {
                    String idToUse = adapter.getConverter().getConversionService().canConvert(id.getClass(), String.class) ? (String) adapter.getConverter().getConversionService().convert(id, String.class) : id.toString();
                    Object objFindById = RedisKeyValueTemplate.this.findById(idToUse, type);
                    if (objFindById != null) {
                        arrayList.add(objFindById);
                    }
                }
                return arrayList;
            }
        });
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueTemplate, org.springframework.data.keyvalue.core.KeyValueOperations
    public void insert(Serializable id, Object objectToInsert) {
        if (objectToInsert instanceof PartialUpdate) {
            doPartialUpdate((PartialUpdate) objectToInsert);
        } else {
            super.insert(id, objectToInsert);
        }
    }

    @Override // org.springframework.data.keyvalue.core.KeyValueTemplate, org.springframework.data.keyvalue.core.KeyValueOperations
    public void update(Object objectToUpdate) {
        if (objectToUpdate instanceof PartialUpdate) {
            doPartialUpdate((PartialUpdate) objectToUpdate);
        } else {
            super.update(objectToUpdate);
        }
    }

    protected void doPartialUpdate(final PartialUpdate<?> update) {
        execute(new RedisKeyValueCallback<Void>() { // from class: org.springframework.data.redis.core.RedisKeyValueTemplate.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisKeyValueTemplate.RedisKeyValueCallback
            public Void doInRedis(RedisKeyValueAdapter adapter) {
                adapter.update(update);
                return null;
            }
        });
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisKeyValueTemplate$RedisKeyValueCallback.class */
    public static abstract class RedisKeyValueCallback<T> implements KeyValueCallback<T> {
        public abstract T doInRedis(RedisKeyValueAdapter redisKeyValueAdapter);

        @Override // org.springframework.data.keyvalue.core.KeyValueCallback
        public T doInKeyValue(KeyValueAdapter adapter) {
            return doInRedis((RedisKeyValueAdapter) adapter);
        }
    }
}
