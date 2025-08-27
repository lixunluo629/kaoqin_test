package org.springframework.data.redis.connection;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.redis.ClusterStateFailureException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterTopology.class */
public class ClusterTopology {
    private final Set<RedisClusterNode> nodes;

    public ClusterTopology(Set<RedisClusterNode> nodes) {
        this.nodes = nodes != null ? nodes : Collections.emptySet();
    }

    public Set<RedisClusterNode> getNodes() {
        return Collections.unmodifiableSet(this.nodes);
    }

    public Set<RedisClusterNode> getActiveNodes() {
        Set<RedisClusterNode> activeNodes = new LinkedHashSet<>(this.nodes.size());
        for (RedisClusterNode node : this.nodes) {
            if (node.isConnected() && !node.isMarkedAsFail()) {
                activeNodes.add(node);
            }
        }
        return activeNodes;
    }

    public Set<RedisClusterNode> getActiveMasterNodes() {
        Set<RedisClusterNode> activeMasterNodes = new LinkedHashSet<>(this.nodes.size());
        for (RedisClusterNode node : this.nodes) {
            if (node.isMaster() && node.isConnected() && !node.isMarkedAsFail()) {
                activeMasterNodes.add(node);
            }
        }
        return activeMasterNodes;
    }

    public Set<RedisClusterNode> getMasterNodes() {
        Set<RedisClusterNode> masterNodes = new LinkedHashSet<>(this.nodes.size());
        for (RedisClusterNode node : this.nodes) {
            if (node.isMaster()) {
                masterNodes.add(node);
            }
        }
        return masterNodes;
    }

    public Set<RedisClusterNode> getSlotServingNodes(int slot) {
        Set<RedisClusterNode> slotServingNodes = new LinkedHashSet<>(this.nodes.size());
        for (RedisClusterNode node : this.nodes) {
            if (node.servesSlot(slot)) {
                slotServingNodes.add(node);
            }
        }
        return slotServingNodes;
    }

    public RedisClusterNode getKeyServingMasterNode(byte[] key) {
        Assert.notNull(key, "Key for node lookup must not be null!");
        int slot = ClusterSlotHashUtil.calculateSlot(key);
        for (RedisClusterNode node : this.nodes) {
            if (node.isMaster() && node.servesSlot(slot)) {
                return node;
            }
        }
        throw new ClusterStateFailureException(String.format("Could not find master node serving slot %s for key '%s',", Integer.valueOf(slot), key));
    }

    public RedisClusterNode lookup(String host, int port) {
        for (RedisClusterNode node : this.nodes) {
            if (host.equals(node.getHost()) && port == node.getPort().intValue()) {
                return node;
            }
        }
        throw new ClusterStateFailureException(String.format("Could not find node at %s:%s. Is your cluster info up to date?", host, Integer.valueOf(port)));
    }

    public RedisClusterNode lookup(String nodeId) {
        Assert.notNull(nodeId, "NodeId must not be null");
        for (RedisClusterNode node : this.nodes) {
            if (nodeId.equals(node.getId())) {
                return node;
            }
        }
        throw new ClusterStateFailureException(String.format("Could not find node at %s. Is your cluster info up to date?", nodeId));
    }

    public RedisClusterNode lookup(RedisClusterNode node) {
        Assert.notNull(node, "RedisClusterNode must not be null");
        if (this.nodes.contains(node) && StringUtils.hasText(node.getHost()) && StringUtils.hasText(node.getId())) {
            return node;
        }
        if (StringUtils.hasText(node.getHost())) {
            return lookup(node.getHost(), node.getPort().intValue());
        }
        if (StringUtils.hasText(node.getId())) {
            return lookup(node.getId());
        }
        throw new ClusterStateFailureException(String.format("Could not find node at %s. Have you provided either host and port or the nodeId?", node));
    }

    public Set<RedisClusterNode> getKeyServingNodes(byte[] key) {
        Assert.notNull(key, "Key must not be null for Cluster Node lookup.");
        return getSlotServingNodes(ClusterSlotHashUtil.calculateSlot(key));
    }
}
