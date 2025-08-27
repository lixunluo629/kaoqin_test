package org.springframework.data.redis.connection;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisSentinelConfiguration.class */
public class RedisSentinelConfiguration {
    private static final String REDIS_SENTINEL_MASTER_CONFIG_PROPERTY = "spring.redis.sentinel.master";
    private static final String REDIS_SENTINEL_NODES_CONFIG_PROPERTY = "spring.redis.sentinel.nodes";
    private NamedNode master;
    private Set<RedisNode> sentinels;

    public RedisSentinelConfiguration() {
        this(new MapPropertySource("RedisSentinelConfiguration", Collections.emptyMap()));
    }

    public RedisSentinelConfiguration(String master, Set<String> sentinelHostAndPorts) {
        this(new MapPropertySource("RedisSentinelConfiguration", asMap(master, sentinelHostAndPorts)));
    }

    public RedisSentinelConfiguration(PropertySource<?> propertySource) {
        Assert.notNull(propertySource, "PropertySource must not be null!");
        this.sentinels = new LinkedHashSet();
        if (propertySource.containsProperty(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY)) {
            setMaster(propertySource.getProperty(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY).toString());
        }
        if (propertySource.containsProperty(REDIS_SENTINEL_NODES_CONFIG_PROPERTY)) {
            appendSentinels(StringUtils.commaDelimitedListToSet(propertySource.getProperty(REDIS_SENTINEL_NODES_CONFIG_PROPERTY).toString()));
        }
    }

    public void setSentinels(Iterable<RedisNode> sentinels) {
        Assert.notNull(sentinels, "Cannot set sentinels to 'null'.");
        this.sentinels.clear();
        for (RedisNode sentinel : sentinels) {
            addSentinel(sentinel);
        }
    }

    public Set<RedisNode> getSentinels() {
        return Collections.unmodifiableSet(this.sentinels);
    }

    public void addSentinel(RedisNode sentinel) {
        Assert.notNull(sentinel, "Sentinel must not be 'null'.");
        this.sentinels.add(sentinel);
    }

    public void setMaster(final String name) {
        Assert.notNull(name, "Name of sentinel master must not be null.");
        setMaster(new NamedNode() { // from class: org.springframework.data.redis.connection.RedisSentinelConfiguration.1
            @Override // org.springframework.data.redis.connection.NamedNode
            public String getName() {
                return name;
            }
        });
    }

    public void setMaster(NamedNode master) {
        Assert.notNull(master, "Sentinel master node must not be 'null'.");
        this.master = master;
    }

    public NamedNode getMaster() {
        return this.master;
    }

    public RedisSentinelConfiguration master(String master) {
        setMaster(master);
        return this;
    }

    public RedisSentinelConfiguration master(NamedNode master) {
        setMaster(master);
        return this;
    }

    public RedisSentinelConfiguration sentinel(RedisNode sentinel) {
        addSentinel(sentinel);
        return this;
    }

    public RedisSentinelConfiguration sentinel(String host, Integer port) {
        return sentinel(new RedisNode(host, port.intValue()));
    }

    private void appendSentinels(Set<String> hostAndPorts) {
        for (String hostAndPort : hostAndPorts) {
            addSentinel(readHostAndPortFromString(hostAndPort));
        }
    }

    private RedisNode readHostAndPortFromString(String hostAndPort) {
        String[] args = StringUtils.split(hostAndPort, ":");
        Assert.notNull(args, "HostAndPort need to be seperated by  ':'.");
        Assert.isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
        return new RedisNode(args[0], Integer.valueOf(args[1]).intValue());
    }

    private static Map<String, Object> asMap(String master, Set<String> sentinelHostAndPorts) {
        Assert.hasText(master, "Master address must not be null or empty!");
        Assert.notNull(sentinelHostAndPorts, "SentinelHostAndPorts must not be null!");
        Map<String, Object> map = new HashMap<>();
        map.put(REDIS_SENTINEL_MASTER_CONFIG_PROPERTY, master);
        map.put(REDIS_SENTINEL_NODES_CONFIG_PROPERTY, StringUtils.collectionToCommaDelimitedString(sentinelHostAndPorts));
        return map;
    }
}
