package org.springframework.data.redis.core.script;

import java.util.List;
import org.springframework.data.redis.serializer.RedisSerializer;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/script/ScriptExecutor.class */
public interface ScriptExecutor<K> {
    <T> T execute(RedisScript<T> redisScript, List<K> list, Object... objArr);

    <T> T execute(RedisScript<T> redisScript, RedisSerializer<?> redisSerializer, RedisSerializer<T> redisSerializer2, List<K> list, Object... objArr);
}
