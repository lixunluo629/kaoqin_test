package redis.clients.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.util.backoff.ExponentialBackOff;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisPoolConfig.class */
public class JedisPoolConfig extends GenericObjectPoolConfig {
    public JedisPoolConfig() {
        setTestWhileIdle(true);
        setMinEvictableIdleTimeMillis(60000L);
        setTimeBetweenEvictionRunsMillis(ExponentialBackOff.DEFAULT_MAX_INTERVAL);
        setNumTestsPerEvictionRun(-1);
    }
}
