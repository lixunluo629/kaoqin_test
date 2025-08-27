package redis.clients.jedis;

import redis.clients.jedis.exceptions.JedisAskDataException;
import redis.clients.jedis.exceptions.JedisClusterException;
import redis.clients.jedis.exceptions.JedisClusterMaxRedirectionsException;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisMovedDataException;
import redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException;
import redis.clients.jedis.exceptions.JedisRedirectionException;
import redis.clients.util.JedisClusterCRC16;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisClusterCommand.class */
public abstract class JedisClusterCommand<T> {
    private final JedisClusterConnectionHandler connectionHandler;
    private final int maxAttempts;
    private final ThreadLocal<Jedis> askConnection = new ThreadLocal<>();

    public abstract T execute(Jedis jedis);

    public JedisClusterCommand(JedisClusterConnectionHandler connectionHandler, int maxAttempts) {
        this.connectionHandler = connectionHandler;
        this.maxAttempts = maxAttempts;
    }

    public T run(String key) {
        if (key == null) {
            throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
        }
        return runWithRetries(JedisClusterCRC16.getSlot(key), this.maxAttempts, false, false);
    }

    public T run(int keyCount, String... keys) {
        if (keys == null || keys.length == 0) {
            throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
        }
        int slot = JedisClusterCRC16.getSlot(keys[0]);
        if (keys.length > 1) {
            for (int i = 1; i < keyCount; i++) {
                int nextSlot = JedisClusterCRC16.getSlot(keys[i]);
                if (slot != nextSlot) {
                    throw new JedisClusterException("No way to dispatch this command to Redis Cluster because keys have different slots.");
                }
            }
        }
        return runWithRetries(slot, this.maxAttempts, false, false);
    }

    public T runBinary(byte[] key) {
        if (key == null) {
            throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
        }
        return runWithRetries(JedisClusterCRC16.getSlot(key), this.maxAttempts, false, false);
    }

    public T runBinary(int keyCount, byte[]... keys) {
        if (keys == null || keys.length == 0) {
            throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
        }
        int slot = JedisClusterCRC16.getSlot(keys[0]);
        if (keys.length > 1) {
            for (int i = 1; i < keyCount; i++) {
                int nextSlot = JedisClusterCRC16.getSlot(keys[i]);
                if (slot != nextSlot) {
                    throw new JedisClusterException("No way to dispatch this command to Redis Cluster because keys have different slots.");
                }
            }
        }
        return runWithRetries(slot, this.maxAttempts, false, false);
    }

    public T runWithAnyNode() {
        Jedis connection = null;
        try {
            try {
                connection = this.connectionHandler.getConnection();
                T tExecute = execute(connection);
                releaseConnection(connection);
                return tExecute;
            } catch (JedisConnectionException e) {
                throw e;
            }
        } catch (Throwable th) {
            releaseConnection(connection);
            throw th;
        }
    }

    private T runWithRetries(int slot, int attempts, boolean tryRandomNode, boolean asking) {
        if (attempts <= 0) {
            throw new JedisClusterMaxRedirectionsException("Too many Cluster redirections?");
        }
        Jedis connection = null;
        try {
            try {
                try {
                    if (asking) {
                        connection = this.askConnection.get();
                        connection.asking();
                        asking = false;
                    } else {
                        connection = tryRandomNode ? this.connectionHandler.getConnection() : this.connectionHandler.getConnectionFromSlot(slot);
                    }
                    T tExecute = execute(connection);
                    releaseConnection(connection);
                    return tExecute;
                } catch (JedisNoReachableClusterNodeException jnrcne) {
                    throw jnrcne;
                } catch (JedisRedirectionException jre) {
                    if (jre instanceof JedisMovedDataException) {
                        this.connectionHandler.renewSlotCache(connection);
                    }
                    releaseConnection(connection);
                    if (jre instanceof JedisAskDataException) {
                        asking = true;
                        this.askConnection.set(this.connectionHandler.getConnectionFromNode(jre.getTargetNode()));
                    } else if (!(jre instanceof JedisMovedDataException)) {
                        throw new JedisClusterException(jre);
                    }
                    T tRunWithRetries = runWithRetries(slot, attempts - 1, false, asking);
                    releaseConnection(null);
                    return tRunWithRetries;
                }
            } catch (JedisConnectionException e) {
                releaseConnection(connection);
                if (attempts <= 1) {
                    this.connectionHandler.renewSlotCache();
                }
                T tRunWithRetries2 = runWithRetries(slot, attempts - 1, tryRandomNode, asking);
                releaseConnection(null);
                return tRunWithRetries2;
            }
        } catch (Throwable th) {
            releaseConnection(connection);
            throw th;
        }
    }

    private void releaseConnection(Jedis connection) {
        if (connection != null) {
            connection.close();
        }
    }
}
