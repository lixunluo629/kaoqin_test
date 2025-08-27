package org.springframework.data.redis.connection.jredis;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jredis.JRedis;
import org.jredis.connector.Connection;
import org.jredis.connector.ConnectionSpec;
import org.jredis.ri.alphazero.JRedisClient;
import org.jredis.ri.alphazero.connection.DefaultConnectionSpec;
import org.springframework.data.redis.connection.Pool;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.util.StringUtils;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jredis/JredisPool.class */
public class JredisPool implements Pool<JRedis> {
    private final GenericObjectPool<JRedis> internalPool;

    public JredisPool(String hostName, int port) {
        this(hostName, port, 0, null, 0, new GenericObjectPoolConfig());
    }

    public JredisPool(String hostName, int port, GenericObjectPoolConfig poolConfig) {
        this(hostName, port, 0, null, 0, poolConfig);
    }

    public JredisPool(ConnectionSpec connectionSpec) {
        this.internalPool = new GenericObjectPool<>(new JredisFactory(connectionSpec), new GenericObjectPoolConfig());
    }

    public JredisPool(ConnectionSpec connectionSpec, GenericObjectPoolConfig poolConfig) {
        this.internalPool = new GenericObjectPool<>(new JredisFactory(connectionSpec), poolConfig);
    }

    public JredisPool(String hostName, int port, int dbIndex, String password, int timeout) {
        this(hostName, port, dbIndex, password, timeout, new GenericObjectPoolConfig());
    }

    public JredisPool(String hostName, int port, int dbIndex, String password, int timeout, GenericObjectPoolConfig poolConfig) {
        ConnectionSpec connectionSpec = DefaultConnectionSpec.newSpec(hostName, port, dbIndex, (byte[]) null);
        connectionSpec.setConnectionFlag(Connection.Flag.RELIABLE, false);
        if (StringUtils.hasLength(password)) {
            connectionSpec.setCredentials(password);
        }
        if (timeout > 0) {
            connectionSpec.setSocketProperty(Connection.Socket.Property.SO_TIMEOUT, Integer.valueOf(timeout));
        }
        this.internalPool = new GenericObjectPool<>(new JredisFactory(connectionSpec), poolConfig);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.data.redis.connection.Pool
    public JRedis getResource() {
        try {
            return this.internalPool.borrowObject();
        } catch (Exception e) {
            throw new PoolException("Could not get a resource from the pool", e);
        }
    }

    @Override // org.springframework.data.redis.connection.Pool
    public void returnBrokenResource(JRedis resource) {
        try {
            this.internalPool.invalidateObject(resource);
        } catch (Exception e) {
            throw new PoolException("Could not invalidate the broken resource", e);
        }
    }

    @Override // org.springframework.data.redis.connection.Pool
    public void returnResource(JRedis resource) {
        try {
            this.internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new PoolException("Could not return the resource to the pool", e);
        }
    }

    @Override // org.springframework.data.redis.connection.Pool
    public void destroy() {
        try {
            this.internalPool.close();
        } catch (Exception e) {
            throw new PoolException("Could not destroy the pool", e);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jredis/JredisPool$JredisFactory.class */
    private static class JredisFactory extends BasePooledObjectFactory<JRedis> {
        private final ConnectionSpec connectionSpec;

        public JredisFactory(ConnectionSpec connectionSpec) {
            this.connectionSpec = connectionSpec;
        }

        @Override // org.apache.commons.pool2.BasePooledObjectFactory, org.apache.commons.pool2.PooledObjectFactory
        public void destroyObject(PooledObject<JRedis> obj) throws Exception {
            try {
                obj.getObject().quit();
            } catch (Exception e) {
            }
        }

        @Override // org.apache.commons.pool2.BasePooledObjectFactory, org.apache.commons.pool2.PooledObjectFactory
        public boolean validateObject(PooledObject<JRedis> obj) {
            try {
                obj.getObject().ping();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.apache.commons.pool2.BasePooledObjectFactory
        public JRedis create() throws Exception {
            return new JRedisClient(this.connectionSpec);
        }

        @Override // org.apache.commons.pool2.BasePooledObjectFactory
        public PooledObject<JRedis> wrap(JRedis obj) {
            return new DefaultPooledObject(obj);
        }
    }
}
