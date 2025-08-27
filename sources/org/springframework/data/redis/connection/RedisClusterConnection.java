package org.springframework.data.redis.connection;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.springframework.data.redis.core.types.RedisClientInfo;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterConnection.class */
public interface RedisClusterConnection extends RedisConnection, RedisClusterCommands {
    String ping(RedisClusterNode redisClusterNode);

    void bgReWriteAof(RedisClusterNode redisClusterNode);

    void bgSave(RedisClusterNode redisClusterNode);

    Long lastSave(RedisClusterNode redisClusterNode);

    void save(RedisClusterNode redisClusterNode);

    Long dbSize(RedisClusterNode redisClusterNode);

    void flushDb(RedisClusterNode redisClusterNode);

    void flushAll(RedisClusterNode redisClusterNode);

    Properties info(RedisClusterNode redisClusterNode);

    Properties info(RedisClusterNode redisClusterNode, String str);

    Set<byte[]> keys(RedisClusterNode redisClusterNode, byte[] bArr);

    byte[] randomKey(RedisClusterNode redisClusterNode);

    void shutdown(RedisClusterNode redisClusterNode);

    List<String> getConfig(RedisClusterNode redisClusterNode, String str);

    void setConfig(RedisClusterNode redisClusterNode, String str, String str2);

    void resetConfigStats(RedisClusterNode redisClusterNode);

    Long time(RedisClusterNode redisClusterNode);

    List<RedisClientInfo> getClientList(RedisClusterNode redisClusterNode);
}
