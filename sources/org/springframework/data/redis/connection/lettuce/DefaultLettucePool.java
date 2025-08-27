package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulConnection;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.resource.ClientResources;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Protocol;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/DefaultLettucePool.class */
public class DefaultLettucePool implements LettucePool, InitializingBean {
    private GenericObjectPool<StatefulConnection<byte[], byte[]>> internalPool;
    private RedisClient client;
    private int dbIndex;
    private GenericObjectPoolConfig poolConfig;
    private String hostName;
    private int port;
    private String password;
    private long timeout;
    private RedisSentinelConfiguration sentinelConfiguration;
    private ClientResources clientResources;

    public DefaultLettucePool() {
        this.dbIndex = 0;
        this.poolConfig = new GenericObjectPoolConfig();
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
    }

    public DefaultLettucePool(String hostName, int port) {
        this.dbIndex = 0;
        this.poolConfig = new GenericObjectPoolConfig();
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.hostName = hostName;
        this.port = port;
    }

    public DefaultLettucePool(RedisSentinelConfiguration sentinelConfiguration) {
        this.dbIndex = 0;
        this.poolConfig = new GenericObjectPoolConfig();
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.sentinelConfiguration = sentinelConfiguration;
    }

    public DefaultLettucePool(String hostName, int port, GenericObjectPoolConfig poolConfig) {
        this.dbIndex = 0;
        this.poolConfig = new GenericObjectPoolConfig();
        this.hostName = "localhost";
        this.port = Protocol.DEFAULT_PORT;
        this.timeout = TimeUnit.MILLISECONDS.convert(60L, TimeUnit.SECONDS);
        this.hostName = hostName;
        this.port = port;
        this.poolConfig = poolConfig;
    }

    public boolean isRedisSentinelAware() {
        return this.sentinelConfiguration != null;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.clientResources != null) {
            this.client = RedisClient.create(this.clientResources, getRedisURI());
        } else {
            this.client = RedisClient.create(getRedisURI());
        }
        this.client.setDefaultTimeout(this.timeout, TimeUnit.MILLISECONDS);
        this.internalPool = new GenericObjectPool<>(new LettuceFactory(this.client, this.dbIndex), this.poolConfig);
    }

    private RedisURI getRedisURI() {
        RedisURI redisUri = isRedisSentinelAware() ? LettuceConverters.sentinelConfigurationToRedisURI(this.sentinelConfiguration) : createSimpleHostRedisURI();
        if (StringUtils.hasText(this.password)) {
            redisUri.setPassword(this.password);
        }
        return redisUri;
    }

    private RedisURI createSimpleHostRedisURI() {
        return RedisURI.Builder.redis(this.hostName, this.port).withTimeout(this.timeout, TimeUnit.MILLISECONDS).build();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.connection.Pool
    public StatefulConnection<byte[], byte[]> getResource() {
        try {
            return this.internalPool.borrowObject();
        } catch (Exception e) {
            throw new PoolException("Could not get a resource from the pool", e);
        }
    }

    @Override // org.springframework.data.redis.connection.Pool
    public void returnBrokenResource(StatefulConnection<byte[], byte[]> resource) {
        try {
            this.internalPool.invalidateObject(resource);
        } catch (Exception e) {
            throw new PoolException("Could not invalidate the broken resource", e);
        }
    }

    @Override // org.springframework.data.redis.connection.Pool
    public void returnResource(StatefulConnection<byte[], byte[]> resource) {
        try {
            this.internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new PoolException("Could not return the resource to the pool", e);
        }
    }

    @Override // org.springframework.data.redis.connection.Pool
    public void destroy() {
        try {
            this.client.shutdown();
            this.internalPool.close();
        } catch (Exception e) {
            throw new PoolException("Could not destroy the pool", e);
        }
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettucePool
    /* renamed from: getClient, reason: merged with bridge method [inline-methods] */
    public RedisClient mo7763getClient() {
        return this.client;
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return this.poolConfig;
    }

    public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getDatabase() {
        return this.dbIndex;
    }

    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String host) {
        this.hostName = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public ClientResources getClientResources() {
        return this.clientResources;
    }

    public void setClientResources(ClientResources clientResources) {
        this.clientResources = clientResources;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/DefaultLettucePool$LettuceFactory.class */
    private static class LettuceFactory extends BasePooledObjectFactory<StatefulConnection<byte[], byte[]>> {
        private final RedisClient client;
        private int dbIndex;

        public LettuceFactory(RedisClient client, int dbIndex) {
            this.client = client;
            this.dbIndex = dbIndex;
        }

        @Override // org.apache.commons.pool2.BasePooledObjectFactory, org.apache.commons.pool2.PooledObjectFactory
        public void activateObject(PooledObject<StatefulConnection<byte[], byte[]>> pooledObject) throws Exception {
            if (pooledObject.getObject() instanceof StatefulRedisConnection) {
                pooledObject.getObject().sync().select(this.dbIndex);
            }
        }

        @Override // org.apache.commons.pool2.BasePooledObjectFactory, org.apache.commons.pool2.PooledObjectFactory
        public void destroyObject(PooledObject<StatefulConnection<byte[], byte[]>> obj) throws Exception {
            try {
                obj.getObject().close();
            } catch (Exception e) {
            }
        }

        @Override // org.apache.commons.pool2.BasePooledObjectFactory, org.apache.commons.pool2.PooledObjectFactory
        public boolean validateObject(PooledObject<StatefulConnection<byte[], byte[]>> obj) {
            try {
                if (obj.getObject() instanceof StatefulRedisConnection) {
                    obj.getObject().sync().ping();
                    return true;
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.apache.commons.pool2.BasePooledObjectFactory
        public StatefulConnection<byte[], byte[]> create() throws Exception {
            return this.client.connect(LettuceConnection.CODEC);
        }

        @Override // org.apache.commons.pool2.BasePooledObjectFactory
        public PooledObject<StatefulConnection<byte[], byte[]>> wrap(StatefulConnection<byte[], byte[]> obj) {
            return new DefaultPooledObject(obj);
        }
    }
}
