package org.springframework.data.redis.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.keyvalue.core.CriteriaAccessor;
import org.springframework.data.keyvalue.core.QueryEngine;
import org.springframework.data.keyvalue.core.SortAccessor;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.convert.GeoIndexedPropertyValue;
import org.springframework.data.redis.core.convert.RedisData;
import org.springframework.data.redis.repository.query.RedisOperationChain;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisQueryEngine.class */
class RedisQueryEngine extends QueryEngine<RedisKeyValueAdapter, RedisOperationChain, Comparator<?>> {
    public RedisQueryEngine() {
        this(new RedisCriteriaAccessor(), null);
    }

    public RedisQueryEngine(CriteriaAccessor<RedisOperationChain> criteriaAccessor, SortAccessor<Comparator<?>> sortAccessor) {
        super(criteriaAccessor, sortAccessor);
    }

    @Override // org.springframework.data.keyvalue.core.QueryEngine
    public <T> Collection<T> execute(final RedisOperationChain criteria, Comparator<?> sort, final int offset, final int rows, final Serializable keyspace, Class<T> type) {
        if (criteria == null || (CollectionUtils.isEmpty(criteria.getOrSismember()) && CollectionUtils.isEmpty(criteria.getSismember()) && criteria.getNear() == null)) {
            return getAdapter().getAllOf(keyspace, offset, rows);
        }
        RedisCallback<Map<byte[], Map<byte[], byte[]>>> callback = new RedisCallback<Map<byte[], Map<byte[], byte[]>>>() { // from class: org.springframework.data.redis.core.RedisQueryEngine.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Map<byte[], Map<byte[], byte[]>> doInRedis2(RedisConnection connection) throws DataAccessException {
                List<byte[]> allKeys = new ArrayList<>();
                if (!criteria.getSismember().isEmpty()) {
                    allKeys.addAll(connection.sInter(RedisQueryEngine.this.keys(keyspace + ":", criteria.getSismember())));
                }
                if (!criteria.getOrSismember().isEmpty()) {
                    allKeys.addAll(connection.sUnion(RedisQueryEngine.this.keys(keyspace + ":", criteria.getOrSismember())));
                }
                if (criteria.getNear() != null) {
                    GeoResults<RedisGeoCommands.GeoLocation<byte[]>> x = connection.geoRadius(RedisQueryEngine.this.geoKey(keyspace + ":", criteria.getNear()), new Circle(criteria.getNear().getPoint(), criteria.getNear().getDistance()));
                    Iterator<GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> it = x.iterator();
                    while (it.hasNext()) {
                        GeoResult<RedisGeoCommands.GeoLocation<byte[]>> y = it.next();
                        allKeys.add(y.getContent().getName());
                    }
                }
                byte[] keyspaceBin = (byte[]) ((RedisKeyValueAdapter) RedisQueryEngine.this.getAdapter()).getConverter().getConversionService().convert(keyspace + ":", byte[].class);
                Map<byte[], Map<byte[], byte[]>> rawData = new LinkedHashMap<>();
                if (allKeys.isEmpty() || allKeys.size() < offset) {
                    return Collections.emptyMap();
                }
                int offsetToUse = Math.max(0, offset);
                if (rows > 0) {
                    allKeys = allKeys.subList(Math.max(0, offsetToUse), Math.min(offsetToUse + rows, allKeys.size()));
                }
                for (byte[] id : allKeys) {
                    byte[] singleKey = ByteUtils.concat(keyspaceBin, id);
                    rawData.put(id, connection.hGetAll(singleKey));
                }
                return rawData;
            }
        };
        Map<byte[], Map<byte[], byte[]>> raw = (Map) getAdapter().execute(callback);
        ArrayList arrayList = new ArrayList(raw.size());
        for (Map.Entry<byte[], Map<byte[], byte[]>> entry : raw.entrySet()) {
            RedisData data = new RedisData(entry.getValue());
            data.setId((String) getAdapter().getConverter().getConversionService().convert(entry.getKey(), String.class));
            data.setKeyspace(keyspace.toString());
            Object obj = getAdapter().getConverter().read(type, data);
            if (obj != null) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    @Override // org.springframework.data.keyvalue.core.QueryEngine
    public Collection<?> execute(RedisOperationChain criteria, Comparator<?> sort, int offset, int rows, Serializable keyspace) {
        return execute(criteria, sort, offset, rows, keyspace, Object.class);
    }

    @Override // org.springframework.data.keyvalue.core.QueryEngine
    public long count(final RedisOperationChain criteria, final Serializable keyspace) {
        if (criteria == null) {
            return getAdapter().count(keyspace);
        }
        return ((Long) getAdapter().execute(new RedisCallback<Long>() { // from class: org.springframework.data.redis.core.RedisQueryEngine.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisCallback
            /* renamed from: doInRedis */
            public Long doInRedis2(RedisConnection connection) throws DataAccessException {
                long result = 0;
                if (!criteria.getOrSismember().isEmpty()) {
                    result = 0 + connection.sUnion(RedisQueryEngine.this.keys(keyspace + ":", criteria.getOrSismember())).size();
                }
                if (!criteria.getSismember().isEmpty()) {
                    result += connection.sInter(RedisQueryEngine.this.keys(keyspace + ":", criteria.getSismember())).size();
                }
                return Long.valueOf(result);
            }
        })).longValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    public byte[][] keys(String prefix, Collection<RedisOperationChain.PathAndValue> source) {
        ?? r0 = new byte[source.size()];
        int i = 0;
        for (RedisOperationChain.PathAndValue pathAndValue : source) {
            byte[] convertedValue = (byte[]) getAdapter().getConverter().getConversionService().convert(pathAndValue.getFirstValue(), byte[].class);
            byte[] fullPath = (byte[]) getAdapter().getConverter().getConversionService().convert(prefix + pathAndValue.getPath() + ":", byte[].class);
            r0[i] = ByteUtils.concat(fullPath, convertedValue);
            i++;
        }
        return r0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] geoKey(String prefix, RedisOperationChain.NearPath source) {
        String path = GeoIndexedPropertyValue.geoIndexName(source.getPath());
        return (byte[]) getAdapter().getConverter().getConversionService().convert(prefix + path, byte[].class);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisQueryEngine$RedisCriteriaAccessor.class */
    static class RedisCriteriaAccessor implements CriteriaAccessor<RedisOperationChain> {
        RedisCriteriaAccessor() {
        }

        @Override // org.springframework.data.keyvalue.core.CriteriaAccessor
        public /* bridge */ /* synthetic */ RedisOperationChain resolve(KeyValueQuery keyValueQuery) {
            return resolve((KeyValueQuery<?>) keyValueQuery);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.data.keyvalue.core.CriteriaAccessor
        public RedisOperationChain resolve(KeyValueQuery<?> query) {
            return (RedisOperationChain) query.getCriteria();
        }
    }
}
