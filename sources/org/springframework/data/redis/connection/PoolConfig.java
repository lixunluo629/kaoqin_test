package org.springframework.data.redis.connection;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/PoolConfig.class */
public class PoolConfig extends GenericObjectPoolConfig {
    public void setMaxActive(int maxActive) {
        setMaxTotal(maxActive);
    }
}
