package org.springframework.data.redis.core.index;

import org.springframework.data.redis.core.index.RedisIndexDefinition;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/SimpleIndexDefinition.class */
public class SimpleIndexDefinition extends RedisIndexDefinition implements PathBasedRedisIndexDefinition {
    public SimpleIndexDefinition(String keyspace, String path) {
        this(keyspace, path, path);
    }

    public SimpleIndexDefinition(String keyspace, String path, String name) {
        super(keyspace, path, name);
        addCondition(new RedisIndexDefinition.PathCondition(path));
    }
}
