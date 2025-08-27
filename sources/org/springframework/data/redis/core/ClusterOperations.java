package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.Set;
import org.springframework.data.redis.connection.RedisClusterNode;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/ClusterOperations.class */
public interface ClusterOperations<K, V> {
    Set<K> keys(RedisClusterNode redisClusterNode, K k);

    String ping(RedisClusterNode redisClusterNode);

    K randomKey(RedisClusterNode redisClusterNode);

    void addSlots(RedisClusterNode redisClusterNode, int... iArr);

    void addSlots(RedisClusterNode redisClusterNode, RedisClusterNode.SlotRange slotRange);

    void bgReWriteAof(RedisClusterNode redisClusterNode);

    void bgSave(RedisClusterNode redisClusterNode);

    void meet(RedisClusterNode redisClusterNode);

    void forget(RedisClusterNode redisClusterNode);

    void flushDb(RedisClusterNode redisClusterNode);

    Collection<RedisClusterNode> getSlaves(RedisClusterNode redisClusterNode);

    void save(RedisClusterNode redisClusterNode);

    void shutdown(RedisClusterNode redisClusterNode);

    void reshard(RedisClusterNode redisClusterNode, int i, RedisClusterNode redisClusterNode2);
}
