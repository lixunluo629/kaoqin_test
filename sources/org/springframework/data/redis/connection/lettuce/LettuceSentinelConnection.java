package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.resource.ClientResources;
import com.lambdaworks.redis.sentinel.api.StatefulRedisSentinelConnection;
import com.lambdaworks.redis.sentinel.api.sync.RedisSentinelCommands;
import java.io.IOException;
import java.util.List;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.FallbackExceptionTranslationStrategy;
import org.springframework.data.redis.connection.NamedNode;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceSentinelConnection.class */
public class LettuceSentinelConnection implements RedisSentinelConnection {
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new FallbackExceptionTranslationStrategy(LettuceConverters.exceptionConverter());
    private RedisClient redisClient;
    private StatefulRedisSentinelConnection<String, String> connection;

    public LettuceSentinelConnection(RedisNode sentinel) {
        this(sentinel.getHost(), sentinel.getPort().intValue());
    }

    public LettuceSentinelConnection(String host, int port) {
        Assert.notNull(host, "Cannot create LettuceSentinelConnection using 'null' as host.");
        this.redisClient = RedisClient.create(RedisURI.Builder.redis(host, port).build());
        init();
    }

    public LettuceSentinelConnection(String host, int port, ClientResources clientResources) {
        Assert.notNull(clientResources, "Cannot create LettuceSentinelConnection using 'null' as ClientResources.");
        Assert.notNull(host, "Cannot create LettuceSentinelConnection using 'null' as host.");
        this.redisClient = RedisClient.create(clientResources, RedisURI.Builder.redis(host, port).build());
        init();
    }

    public LettuceSentinelConnection(RedisClient redisClient) {
        Assert.notNull(redisClient, "Cannot create LettuceSentinelConnection using 'null' as client.");
        this.redisClient = redisClient;
        init();
    }

    protected LettuceSentinelConnection(StatefulRedisSentinelConnection<String, String> connection) {
        Assert.notNull(connection, "Cannot create LettuceSentinelConnection using 'null' as connection.");
        this.connection = connection;
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public void failover(NamedNode master) {
        Assert.notNull(master, "Redis node master must not be 'null' for failover.");
        Assert.hasText(master.getName(), "Redis master name must not be 'null' or empty for failover.");
        getSentinelCommands().failover(master.getName());
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public List<RedisServer> masters() {
        try {
            return LettuceConverters.toListOfRedisServer(getSentinelCommands().masters());
        } catch (Exception e) {
            throw EXCEPTION_TRANSLATION.translate(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public List<RedisServer> slaves(NamedNode master) {
        Assert.notNull(master, "Master node cannot be 'null' when loading slaves.");
        return slaves(master.getName());
    }

    public List<RedisServer> slaves(String masterName) {
        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when loading slaves.");
        try {
            return LettuceConverters.toListOfRedisServer(getSentinelCommands().slaves(masterName));
        } catch (Exception e) {
            throw EXCEPTION_TRANSLATION.translate(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public void remove(NamedNode master) {
        Assert.notNull(master, "Master node cannot be 'null' when trying to remove.");
        remove(master.getName());
    }

    public void remove(String masterName) {
        Assert.hasText(masterName, "Name of redis master cannot be 'null' or empty when trying to remove.");
        getSentinelCommands().remove(masterName);
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelCommands
    public void monitor(RedisServer server) {
        Assert.notNull(server, "Cannot monitor 'null' server.");
        Assert.hasText(server.getName(), "Name of server to monitor must not be 'null' or empty.");
        Assert.hasText(server.getHost(), "Host must not be 'null' for server to monitor.");
        Assert.notNull(server.getPort(), "Port must not be 'null' for server to monitor.");
        Assert.notNull(server.getQuorum(), "Quorum must not be 'null' for server to monitor.");
        getSentinelCommands().monitor(server.getName(), server.getHost(), server.getPort().intValue(), server.getQuorum().intValue());
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.connection.close();
        this.connection = null;
        if (this.redisClient != null) {
            this.redisClient.shutdown();
        }
    }

    private void init() {
        if (this.connection == null) {
            this.connection = connectSentinel();
        }
    }

    private RedisSentinelCommands<String, String> getSentinelCommands() {
        return this.connection.sync();
    }

    private StatefulRedisSentinelConnection<String, String> connectSentinel() {
        return this.redisClient.connectSentinel();
    }

    @Override // org.springframework.data.redis.connection.RedisSentinelConnection
    public boolean isOpen() {
        return this.connection != null && this.connection.isOpen();
    }
}
