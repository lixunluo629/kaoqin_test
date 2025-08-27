package org.springframework.data.redis.core.script;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/script/RedisScript.class */
public interface RedisScript<T> {
    String getSha1();

    Class<T> getResultType();

    String getScriptAsString();
}
