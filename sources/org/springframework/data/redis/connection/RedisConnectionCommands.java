package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisConnectionCommands.class */
public interface RedisConnectionCommands {
    void select(int i);

    byte[] echo(byte[] bArr);

    String ping();
}
