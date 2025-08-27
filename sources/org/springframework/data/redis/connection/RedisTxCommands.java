package org.springframework.data.redis.connection;

import java.util.List;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisTxCommands.class */
public interface RedisTxCommands {
    void multi();

    List<Object> exec();

    void discard();

    void watch(byte[]... bArr);

    void unwatch();
}
