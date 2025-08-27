package redis.clients.jedis;

import java.io.Closeable;
import java.util.Map;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisClusterConnectionHandler.class */
public abstract class JedisClusterConnectionHandler implements Closeable {
    protected final JedisClusterInfoCache cache;

    abstract Jedis getConnection();

    abstract Jedis getConnectionFromSlot(int i);

    public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password) {
        this(nodes, poolConfig, connectionTimeout, soTimeout, password, null);
    }

    public JedisClusterConnectionHandler(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, String clientName) {
        this.cache = new JedisClusterInfoCache(poolConfig, connectionTimeout, soTimeout, password, clientName);
        initializeSlotsCache(nodes, poolConfig, connectionTimeout, soTimeout, password, clientName);
    }

    public Jedis getConnectionFromNode(HostAndPort node) {
        return this.cache.setupNodeIfNotExist(node).getResource();
    }

    public Map<String, JedisPool> getNodes() {
        return this.cache.getNodes();
    }

    private void initializeSlotsCache(Set<HostAndPort> startNodes, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, String clientName) {
        for (HostAndPort hostAndPort : startNodes) {
            Jedis jedis = null;
            try {
                jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), connectionTimeout, soTimeout);
                if (password != null) {
                    jedis.auth(password);
                }
                if (clientName != null) {
                    jedis.clientSetname(clientName);
                }
                this.cache.discoverClusterNodesAndSlots(jedis);
                if (jedis != null) {
                    jedis.close();
                    return;
                }
                return;
            } catch (JedisConnectionException e) {
                if (jedis != null) {
                    jedis.close();
                }
            } catch (Throwable th) {
                if (jedis != null) {
                    jedis.close();
                }
                throw th;
            }
        }
    }

    public void renewSlotCache() {
        this.cache.renewClusterSlots(null);
    }

    public void renewSlotCache(Jedis jedis) {
        this.cache.renewClusterSlots(jedis);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.cache.reset();
    }
}
