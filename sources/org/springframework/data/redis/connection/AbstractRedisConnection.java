package org.springframework.data.redis.connection;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.redis.RedisSystemException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/AbstractRedisConnection.class */
public abstract class AbstractRedisConnection implements RedisConnection {
    private RedisSentinelConfiguration sentinelConfiguration;
    private ConcurrentHashMap<RedisNode, RedisSentinelConnection> connectionCache = new ConcurrentHashMap<>();

    @Override // org.springframework.data.redis.connection.RedisConnection
    public RedisSentinelConnection getSentinelConnection() {
        if (!hasRedisSentinelConfigured()) {
            throw new InvalidDataAccessResourceUsageException("No sentinels configured.");
        }
        RedisNode node = selectActiveSentinel();
        RedisSentinelConnection connection = this.connectionCache.get(node);
        if (connection == null || !connection.isOpen()) {
            connection = getSentinelConnection(node);
            this.connectionCache.putIfAbsent(node, connection);
        }
        return connection;
    }

    public void setSentinelConfiguration(RedisSentinelConfiguration sentinelConfiguration) {
        this.sentinelConfiguration = sentinelConfiguration;
    }

    public boolean hasRedisSentinelConfigured() {
        return this.sentinelConfiguration != null;
    }

    private RedisNode selectActiveSentinel() {
        for (RedisNode node : this.sentinelConfiguration.getSentinels()) {
            if (isActive(node)) {
                return node;
            }
        }
        throw new InvalidDataAccessApiUsageException("Could not find any active sentinels");
    }

    protected boolean isActive(RedisNode node) {
        return false;
    }

    protected RedisSentinelConnection getSentinelConnection(RedisNode sentinel) {
        throw new UnsupportedOperationException("Sentinel is not supported by this client.");
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        if (!this.connectionCache.isEmpty()) {
            Iterator it = this.connectionCache.keySet().iterator();
            while (it.hasNext()) {
                RedisNode node = (RedisNode) it.next();
                RedisSentinelConnection connection = this.connectionCache.remove(node);
                if (connection.isOpen()) {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        throw new RedisSystemException("Failed to close sentinel connection", e);
                    }
                }
            }
        }
    }
}
