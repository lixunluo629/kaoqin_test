package org.springframework.data.redis.connection.jedis;

import java.io.IOException;
import java.util.List;
import org.springframework.data.redis.connection.NamedNode;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisSentinelConnection.class */
public class JedisSentinelConnection implements RedisSentinelConnection {
    private Jedis jedis;

    public JedisSentinelConnection(RedisNode sentinel) {
        this(sentinel.getHost(), sentinel.getPort().intValue());
    }

    public JedisSentinelConnection(String host, int port) {
        this(new Jedis(host, port));
    }

    public JedisSentinelConnection(Jedis jedis) {
        Assert.notNull(jedis, "Cannot created JedisSentinelConnection using 'null' as client.");
        this.jedis = jedis;
        init();
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public void failover(NamedNode master) {
        Assert.notNull(master, "Redis node master must not be 'null' for failover.");
        Assert.hasText(master.getName(), "Redis master name must not be 'null' or empty for failover.");
        this.jedis.sentinelFailover(master.getName());
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public List<RedisServer> masters() {
        return JedisConverters.toListOfRedisServer(this.jedis.sentinelMasters());
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public List<RedisServer> slaves(NamedNode master) {
        Assert.notNull(master, "Master node cannot be 'null' when loading slaves.");
        return slaves(master.getName());
    }

    public List<RedisServer> slaves(String masterName) {
        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when loading slaves.");
        return JedisConverters.toListOfRedisServer(this.jedis.sentinelSlaves(masterName));
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public void remove(NamedNode master) {
        Assert.notNull(master, "Master node cannot be 'null' when trying to remove.");
        remove(master.getName());
    }

    public void remove(String masterName) {
        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when trying to remove.");
        this.jedis.sentinelRemove(masterName);
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public void monitor(RedisServer server) {
        Assert.notNull(server, "Cannot monitor 'null' server.");
        Assert.hasText(server.getName(), "Name of server to monitor must not be 'null' or empty.");
        Assert.hasText(server.getHost(), "Host must not be 'null' for server to monitor.");
        Assert.notNull(server.getPort(), "Port must not be 'null' for server to monitor.");
        Assert.notNull(server.getQuorum(), "Quorum must not be 'null' for server to monitor.");
        this.jedis.sentinelMonitor(server.getName(), server.getHost(), server.getPort().intValue(), server.getQuorum().intValue());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.jedis.close();
    }

    private void init() {
        if (!this.jedis.isConnected()) {
            doInit(this.jedis);
        }
    }

    protected void doInit(Jedis jedis) {
        jedis.connect();
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelConnection
    public boolean isOpen() {
        return this.jedis != null && this.jedis.isConnected();
    }
}
