package org.springframework.data.redis.connection;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.jredis.JredisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.srp.SrpConnectionFactory;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ConnectionUtils.class */
public abstract class ConnectionUtils {
    public static boolean isAsync(RedisConnectionFactory connectionFactory) {
        return (connectionFactory instanceof LettuceConnectionFactory) || (connectionFactory instanceof SrpConnectionFactory);
    }

    public static boolean isSrp(RedisConnectionFactory connectionFactory) {
        return connectionFactory instanceof SrpConnectionFactory;
    }

    public static boolean isJredis(RedisConnectionFactory connectionFactory) {
        return connectionFactory instanceof JredisConnectionFactory;
    }

    public static boolean isLettuce(RedisConnectionFactory connectionFactory) {
        return connectionFactory instanceof LettuceConnectionFactory;
    }

    public static boolean isJedis(RedisConnectionFactory connectionFactory) {
        return connectionFactory instanceof JedisConnectionFactory;
    }
}
