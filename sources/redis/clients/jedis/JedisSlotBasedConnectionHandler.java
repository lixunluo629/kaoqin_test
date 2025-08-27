package redis.clients.jedis;

import java.util.List;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisNoReachableClusterNodeException;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisSlotBasedConnectionHandler.class */
public class JedisSlotBasedConnectionHandler extends JedisClusterConnectionHandler {
    public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int timeout) {
        this(nodes, poolConfig, timeout, timeout);
    }

    public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout) {
        super(nodes, poolConfig, connectionTimeout, soTimeout, null);
    }

    public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password) {
        super(nodes, poolConfig, connectionTimeout, soTimeout, password);
    }

    public JedisSlotBasedConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, String clientName) {
        super(nodes, poolConfig, connectionTimeout, soTimeout, password, clientName);
    }

    @Override // redis.clients.jedis.JedisClusterConnectionHandler
    public Jedis getConnection() {
        List<JedisPool> pools = this.cache.getShuffledNodesPool();
        for (JedisPool pool : pools) {
            Jedis jedis = null;
            try {
                jedis = pool.getResource();
                if (jedis != null) {
                    String result = jedis.ping();
                    if (result.equalsIgnoreCase("pong")) {
                        return jedis;
                    }
                    jedis.close();
                }
            } catch (JedisException e) {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
        throw new JedisNoReachableClusterNodeException("No reachable node in cluster");
    }

    @Override // redis.clients.jedis.JedisClusterConnectionHandler
    public Jedis getConnectionFromSlot(int slot) {
        JedisPool connectionPool = this.cache.getSlotPool(slot);
        if (connectionPool != null) {
            return connectionPool.getResource();
        }
        renewSlotCache();
        JedisPool connectionPool2 = this.cache.getSlotPool(slot);
        if (connectionPool2 != null) {
            return connectionPool2.getResource();
        }
        return getConnection();
    }
}
