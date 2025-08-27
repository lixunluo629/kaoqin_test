package org.springframework.data.redis.connection;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.data.redis.connection.RedisClusterNode;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterCommands.class */
public interface RedisClusterCommands {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterCommands$AddSlots.class */
    public enum AddSlots {
        MIGRATING,
        IMPORTING,
        STABLE,
        NODE
    }

    Iterable<RedisClusterNode> clusterGetNodes();

    Collection<RedisClusterNode> clusterGetSlaves(RedisClusterNode redisClusterNode);

    Map<RedisClusterNode, Collection<RedisClusterNode>> clusterGetMasterSlaveMap();

    Integer clusterGetSlotForKey(byte[] bArr);

    RedisClusterNode clusterGetNodeForSlot(int i);

    RedisClusterNode clusterGetNodeForKey(byte[] bArr);

    ClusterInfo clusterGetClusterInfo();

    void clusterAddSlots(RedisClusterNode redisClusterNode, int... iArr);

    void clusterAddSlots(RedisClusterNode redisClusterNode, RedisClusterNode.SlotRange slotRange);

    Long clusterCountKeysInSlot(int i);

    void clusterDeleteSlots(RedisClusterNode redisClusterNode, int... iArr);

    void clusterDeleteSlotsInRange(RedisClusterNode redisClusterNode, RedisClusterNode.SlotRange slotRange);

    void clusterForget(RedisClusterNode redisClusterNode);

    void clusterMeet(RedisClusterNode redisClusterNode);

    void clusterSetSlot(RedisClusterNode redisClusterNode, int i, AddSlots addSlots);

    List<byte[]> clusterGetKeysInSlot(int i, Integer num);

    void clusterReplicate(RedisClusterNode redisClusterNode, RedisClusterNode redisClusterNode2);
}
