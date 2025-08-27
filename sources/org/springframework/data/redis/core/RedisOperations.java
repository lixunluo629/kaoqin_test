package org.springframework.data.redis.core;

import java.io.Closeable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.serializer.RedisSerializer;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisOperations.class */
public interface RedisOperations<K, V> {
    <T> T execute(RedisCallback<T> redisCallback);

    <T> T execute(SessionCallback<T> sessionCallback);

    List<Object> executePipelined(RedisCallback<?> redisCallback);

    List<Object> executePipelined(RedisCallback<?> redisCallback, RedisSerializer<?> redisSerializer);

    List<Object> executePipelined(SessionCallback<?> sessionCallback);

    List<Object> executePipelined(SessionCallback<?> sessionCallback, RedisSerializer<?> redisSerializer);

    <T> T execute(RedisScript<T> redisScript, List<K> list, Object... objArr);

    <T> T execute(RedisScript<T> redisScript, RedisSerializer<?> redisSerializer, RedisSerializer<T> redisSerializer2, List<K> list, Object... objArr);

    <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> redisCallback);

    Boolean hasKey(K k);

    void delete(K k);

    void delete(Collection<K> collection);

    DataType type(K k);

    Set<K> keys(K k);

    K randomKey();

    void rename(K k, K k2);

    Boolean renameIfAbsent(K k, K k2);

    Boolean expire(K k, long j, TimeUnit timeUnit);

    Boolean expireAt(K k, Date date);

    Boolean persist(K k);

    Boolean move(K k, int i);

    byte[] dump(K k);

    void restore(K k, byte[] bArr, long j, TimeUnit timeUnit);

    Long getExpire(K k);

    Long getExpire(K k, TimeUnit timeUnit);

    List<V> sort(SortQuery<K> sortQuery);

    <T> List<T> sort(SortQuery<K> sortQuery, RedisSerializer<T> redisSerializer);

    <T> List<T> sort(SortQuery<K> sortQuery, BulkMapper<T, V> bulkMapper);

    <T, S> List<T> sort(SortQuery<K> sortQuery, BulkMapper<T, S> bulkMapper, RedisSerializer<S> redisSerializer);

    Long sort(SortQuery<K> sortQuery, K k);

    void watch(K k);

    void watch(Collection<K> collection);

    void unwatch();

    void multi();

    void discard();

    List<Object> exec();

    List<Object> exec(RedisSerializer<?> redisSerializer);

    List<RedisClientInfo> getClientList();

    void killClient(String str, int i);

    void slaveOf(String str, int i);

    void slaveOfNoOne();

    void convertAndSend(String str, Object obj);

    ValueOperations<K, V> opsForValue();

    BoundValueOperations<K, V> boundValueOps(K k);

    ListOperations<K, V> opsForList();

    BoundListOperations<K, V> boundListOps(K k);

    SetOperations<K, V> opsForSet();

    BoundSetOperations<K, V> boundSetOps(K k);

    ZSetOperations<K, V> opsForZSet();

    HyperLogLogOperations<K, V> opsForHyperLogLog();

    BoundZSetOperations<K, V> boundZSetOps(K k);

    <HK, HV> HashOperations<K, HK, HV> opsForHash();

    <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K k);

    GeoOperations<K, V> opsForGeo();

    BoundGeoOperations<K, V> boundGeoOps(K k);

    ClusterOperations<K, V> opsForCluster();

    RedisSerializer<?> getKeySerializer();

    RedisSerializer<?> getValueSerializer();

    RedisSerializer<?> getHashKeySerializer();

    RedisSerializer<?> getHashValueSerializer();
}
