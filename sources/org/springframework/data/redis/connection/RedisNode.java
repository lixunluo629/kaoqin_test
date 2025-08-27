package org.springframework.data.redis.connection;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisNode.class */
public class RedisNode implements NamedNode {
    String id;
    String name;
    String host;
    int port;
    NodeType type;
    String masterId;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisNode$NodeType.class */
    public enum NodeType {
        MASTER,
        SLAVE
    }

    public RedisNode(String host, int port) {
        Assert.notNull(host, "host must not be null!");
        this.host = host;
        this.port = port;
    }

    protected RedisNode() {
    }

    public String getHost() {
        return this.host;
    }

    public Integer getPort() {
        return Integer.valueOf(this.port);
    }

    public String asString() {
        return this.host + ":" + this.port;
    }

    @Override // org.springframework.data.redis.connection.NamedNode
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMasterId() {
        return this.masterId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeType getType() {
        return this.type;
    }

    public boolean isMaster() {
        return ObjectUtils.nullSafeEquals(NodeType.MASTER, getType());
    }

    public boolean isSlave() {
        return ObjectUtils.nullSafeEquals(NodeType.SLAVE, getType());
    }

    public static RedisNodeBuilder newRedisNode() {
        return new RedisNodeBuilder();
    }

    public String toString() {
        return asString();
    }

    public int hashCode() {
        int result = (31 * 1) + ObjectUtils.nullSafeHashCode(this.host);
        return (31 * result) + ObjectUtils.nullSafeHashCode(Integer.valueOf(this.port));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof RedisNode)) {
            return false;
        }
        RedisNode other = (RedisNode) obj;
        if (!ObjectUtils.nullSafeEquals(this.host, other.host) || !ObjectUtils.nullSafeEquals(Integer.valueOf(this.port), Integer.valueOf(other.port)) || !ObjectUtils.nullSafeEquals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisNode$RedisNodeBuilder.class */
    public static class RedisNodeBuilder {
        private RedisNode node = new RedisNode();

        public RedisNodeBuilder withName(String name) {
            this.node.name = name;
            return this;
        }

        public RedisNodeBuilder listeningAt(String host, int port) {
            Assert.hasText(host, "Hostname must not be empty or null.");
            this.node.host = host;
            this.node.port = port;
            return this;
        }

        public RedisNodeBuilder withId(String id) {
            this.node.id = id;
            return this;
        }

        public RedisNodeBuilder promotedAs(NodeType type) {
            this.node.type = type;
            return this;
        }

        public RedisNodeBuilder slaveOf(String masterId) {
            this.node.masterId = masterId;
            return this;
        }

        public RedisNode build() {
            return this.node;
        }
    }
}
