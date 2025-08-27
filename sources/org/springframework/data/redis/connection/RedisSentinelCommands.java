package org.springframework.data.redis.connection;

import java.util.Collection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisSentinelCommands.class */
public interface RedisSentinelCommands {
    void failover(NamedNode namedNode);

    Collection<RedisServer> masters();

    Collection<RedisServer> slaves(NamedNode namedNode);

    void remove(NamedNode namedNode);

    void monitor(RedisServer redisServer);
}
