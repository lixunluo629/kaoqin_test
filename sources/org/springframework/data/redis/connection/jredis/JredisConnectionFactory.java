package org.springframework.data.redis.connection.jredis;

import org.jredis.ClientRuntimeException;
import org.jredis.JRedis;
import org.jredis.connector.Connection;
import org.jredis.connector.ConnectionSpec;
import org.jredis.ri.alphazero.JRedisClient;
import org.jredis.ri.alphazero.connection.DefaultConnectionSpec;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Pool;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jredis/JredisConnectionFactory.class */
public class JredisConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {
    private ConnectionSpec connectionSpec;
    private int timeout;
    private Pool<JRedis> pool;
    private static final int DEFAULT_REDIS_PORT = 6379;
    private static final int DEFAULT_REDIS_DB = 0;
    private static final byte[] DEFAULT_REDIS_PASSWORD = null;
    private String hostName = "localhost";
    private int port = 6379;
    private String password = null;
    private int dbIndex = 0;

    public JredisConnectionFactory() {
    }

    public JredisConnectionFactory(ConnectionSpec connectionSpec) {
        this.connectionSpec = connectionSpec;
    }

    public JredisConnectionFactory(Pool<JRedis> pool) {
        this.pool = pool;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.connectionSpec == null && this.pool == null) {
            Assert.hasText(this.hostName, "Redis host name must not be null!");
            this.connectionSpec = DefaultConnectionSpec.newSpec(this.hostName, this.port, this.dbIndex, DEFAULT_REDIS_PASSWORD);
            this.connectionSpec.setConnectionFlag(Connection.Flag.RELIABLE, false);
            if (StringUtils.hasLength(this.password)) {
                this.connectionSpec.setCredentials(this.password);
            }
            if (this.timeout > 0) {
                this.connectionSpec.setSocketProperty(Connection.Socket.Property.SO_TIMEOUT, Integer.valueOf(this.timeout));
            }
        }
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() throws Exception {
        if (this.pool != null) {
            this.pool.destroy();
            this.pool = null;
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisConnection getConnection() {
        JredisConnection connection;
        if (this.pool != null) {
            connection = new JredisConnection(this.pool.getResource(), this.pool);
        } else {
            connection = new JredisConnection(new JRedisClient(this.connectionSpec), null);
        }
        return postProcessConnection(connection);
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisClusterConnection getClusterConnection() {
        throw new UnsupportedOperationException("Jredis does not support Redis Cluster.");
    }

    protected RedisConnection postProcessConnection(JredisConnection connection) {
        return connection;
    }

    @Override // org.springframework.dao.support.PersistenceExceptionTranslator
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        if (ex instanceof ClientRuntimeException) {
            return JredisUtils.convertJredisAccessException((ClientRuntimeException) ex);
        }
        return null;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return this.dbIndex;
    }

    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public boolean getConvertPipelineAndTxResults() {
        return false;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionFactory
    public RedisSentinelConnection getSentinelConnection() {
        throw new UnsupportedOperationException();
    }
}
