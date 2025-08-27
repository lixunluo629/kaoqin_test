package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisCommands.class */
public interface RedisCommands extends RedisKeyCommands, RedisStringCommands, RedisListCommands, RedisSetCommands, RedisZSetCommands, RedisHashCommands, RedisTxCommands, RedisPubSubCommands, RedisConnectionCommands, RedisServerCommands, RedisScriptingCommands, RedisGeoCommands, HyperLogLogCommands {
    Object execute(String str, byte[]... bArr);
}
