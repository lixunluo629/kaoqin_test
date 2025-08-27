package org.springframework.data.redis.connection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisClusterConfiguration.class */
public class RedisClusterConfiguration {
    private static final String REDIS_CLUSTER_NODES_CONFIG_PROPERTY = "spring.redis.cluster.nodes";
    private static final String REDIS_CLUSTER_MAX_REDIRECTS_CONFIG_PROPERTY = "spring.redis.cluster.max-redirects";
    private Set<RedisNode> clusterNodes;
    private Integer maxRedirects;

    public RedisClusterConfiguration() {
        this(new MapPropertySource("RedisClusterConfiguration", Collections.emptyMap()));
    }

    public RedisClusterConfiguration(Collection<String> clusterNodes) {
        this(new MapPropertySource("RedisClusterConfiguration", asMap(clusterNodes, -1L, -1, null)));
    }

    public RedisClusterConfiguration(PropertySource<?> propertySource) {
        Assert.notNull(propertySource, "PropertySource must not be null!");
        this.clusterNodes = new LinkedHashSet();
        if (propertySource.containsProperty(REDIS_CLUSTER_NODES_CONFIG_PROPERTY)) {
            appendClusterNodes(StringUtils.commaDelimitedListToSet(propertySource.getProperty(REDIS_CLUSTER_NODES_CONFIG_PROPERTY).toString()));
        }
        if (propertySource.containsProperty(REDIS_CLUSTER_MAX_REDIRECTS_CONFIG_PROPERTY)) {
            this.maxRedirects = (Integer) NumberUtils.parseNumber(propertySource.getProperty(REDIS_CLUSTER_MAX_REDIRECTS_CONFIG_PROPERTY).toString(), Integer.class);
        }
    }

    public void setClusterNodes(Iterable<RedisNode> nodes) {
        Assert.notNull(nodes, "Cannot set cluster nodes to 'null'.");
        this.clusterNodes.clear();
        for (RedisNode clusterNode : nodes) {
            addClusterNode(clusterNode);
        }
    }

    public Set<RedisNode> getClusterNodes() {
        return Collections.unmodifiableSet(this.clusterNodes);
    }

    public void addClusterNode(RedisNode node) {
        Assert.notNull(node, "ClusterNode must not be 'null'.");
        this.clusterNodes.add(node);
    }

    public RedisClusterConfiguration clusterNode(RedisNode node) {
        this.clusterNodes.add(node);
        return this;
    }

    public Integer getMaxRedirects() {
        if (this.maxRedirects == null || this.maxRedirects.intValue() <= Integer.MIN_VALUE) {
            return null;
        }
        return this.maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        Assert.isTrue(maxRedirects >= 0, "MaxRedirects must be greater or equal to 0");
        this.maxRedirects = Integer.valueOf(maxRedirects);
    }

    public RedisClusterConfiguration clusterNode(String host, Integer port) {
        return clusterNode(new RedisNode(host, port.intValue()));
    }

    private void appendClusterNodes(Set<String> hostAndPorts) {
        for (String hostAndPort : hostAndPorts) {
            addClusterNode(readHostAndPortFromString(hostAndPort));
        }
    }

    private RedisNode readHostAndPortFromString(String hostAndPort) {
        String[] args = StringUtils.split(hostAndPort, ":");
        Assert.notNull(args, "HostAndPort need to be seperated by  ':'.");
        Assert.isTrue(args.length == 2, "Host and Port String needs to specified as host:port");
        return new RedisNode(args[0], Integer.valueOf(args[1]).intValue());
    }

    private static Map<String, Object> asMap(Collection<String> clusterHostAndPorts, long timeout, int redirects, String password) {
        Assert.notNull(clusterHostAndPorts, "ClusterHostAndPorts must not be null!");
        Map<String, Object> map = new HashMap<>();
        map.put(REDIS_CLUSTER_NODES_CONFIG_PROPERTY, StringUtils.collectionToCommaDelimitedString(clusterHostAndPorts));
        if (redirects >= 0) {
            map.put(REDIS_CLUSTER_MAX_REDIRECTS_CONFIG_PROPERTY, Integer.valueOf(redirects));
        }
        return map;
    }
}
