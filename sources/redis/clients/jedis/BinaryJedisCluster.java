package redis.clients.jedis;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.exceptions.JedisClusterException;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.JedisClusterHashTagUtil;
import redis.clients.util.KeyMergeUtil;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryJedisCluster.class */
public class BinaryJedisCluster implements BasicCommands, BinaryJedisClusterCommands, MultiKeyBinaryJedisClusterCommands, JedisClusterBinaryScriptingCommands, Closeable {
    public static final short HASHSLOTS = 16384;
    protected static final int DEFAULT_TIMEOUT = 2000;
    protected static final int DEFAULT_MAX_REDIRECTIONS = 5;
    protected int maxAttempts;
    protected JedisClusterConnectionHandler connectionHandler;

    public BinaryJedisCluster(Set<HostAndPort> nodes, int timeout) {
        this(nodes, timeout, 5, new GenericObjectPoolConfig());
    }

    public BinaryJedisCluster(Set<HostAndPort> nodes) {
        this(nodes, 2000);
    }

    public BinaryJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        this.connectionHandler = new JedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, timeout);
        this.maxAttempts = maxAttempts;
    }

    public BinaryJedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        this.connectionHandler = new JedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, connectionTimeout, soTimeout);
        this.maxAttempts = maxAttempts;
    }

    public BinaryJedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        this.connectionHandler = new JedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, connectionTimeout, soTimeout, password);
        this.maxAttempts = maxAttempts;
    }

    public BinaryJedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig) {
        this.connectionHandler = new JedisSlotBasedConnectionHandler(jedisClusterNode, poolConfig, connectionTimeout, soTimeout, password, clientName);
        this.maxAttempts = maxAttempts;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.connectionHandler != null) {
            this.connectionHandler.close();
        }
    }

    public Map<String, JedisPool> getClusterNodes() {
        return this.connectionHandler.getNodes();
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public String set(final byte[] key, final byte[] value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.set(key, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public String set(final byte[] key, final byte[] value, final byte[] nxxx, final byte[] expx, final long time) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.set(key, value, nxxx, expx, time);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] get(final byte[] key) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.get(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Boolean exists(final byte[] key) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.exists(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long exists(final byte[]... keys) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.exists(keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long persist(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.persist(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public String type(final byte[] key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.type(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long expire(final byte[] key, final int seconds) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.expire(key, seconds);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long pexpire(final byte[] key, final long milliseconds) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pexpire(key, milliseconds);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long expireAt(final byte[] key, final long unixTime) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.expireAt(key, unixTime);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long pexpireAt(final byte[] key, final long millisecondsTimestamp) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pexpireAt(key, millisecondsTimestamp);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long ttl(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.ttl(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long pttl(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pttl(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Boolean setbit(final byte[] key, final long offset, final boolean value) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.setbit(key, offset, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Boolean setbit(final byte[] key, final long offset, final byte[] value) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.setbit(key, offset, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Boolean getbit(final byte[] key, final long offset) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.getbit(key, offset);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long setrange(final byte[] key, final long offset, final byte[] value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.setrange(key, offset, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] getrange(final byte[] key, final long startOffset, final long endOffset) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.getrange(key, startOffset, endOffset);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] getSet(final byte[] key, final byte[] value) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.getSet(key, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long setnx(final byte[] key, final byte[] value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.20
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.setnx(key, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public String setex(final byte[] key, final int seconds, final byte[] value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.setex(key, seconds, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long decrBy(final byte[] key, final long decrement) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.22
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.decrBy(key, decrement);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long decr(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.23
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.decr(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long incrBy(final byte[] key, final long increment) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.incrBy(key, increment);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Double incrByFloat(final byte[] key, final double increment) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.25
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.incrByFloat(key, increment);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long incr(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.26
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.incr(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long append(final byte[] key, final byte[] value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.append(key, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] substr(final byte[] key, final int start, final int end) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.28
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.substr(key, start, end);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long hset(final byte[] key, final byte[] field, final byte[] value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hset(key, field, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] hget(final byte[] key, final byte[] field) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.30
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.hget(key, field);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.31
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hsetnx(key, field, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.32
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.hmset(key, hash);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.33
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.hmget(key, fields);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long hincrBy(final byte[] key, final byte[] field, final long value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.34
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hincrBy(key, field, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Double hincrByFloat(final byte[] key, final byte[] field, final double value) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.35
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.hincrByFloat(key, field, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Boolean hexists(final byte[] key, final byte[] field) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.36
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.hexists(key, field);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long hdel(final byte[] key, final byte[]... field) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.37
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hdel(key, field);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long hlen(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.38
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hlen(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> hkeys(final byte[] key) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.39
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.hkeys(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Collection<byte[]> hvals(final byte[] key) {
        return new JedisClusterCommand<Collection<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.40
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Collection<byte[]> execute(Jedis connection) {
                return connection.hvals(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Map<byte[], byte[]> hgetAll(final byte[] key) {
        return new JedisClusterCommand<Map<byte[], byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.41
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Map<byte[], byte[]> execute(Jedis connection) {
                return connection.hgetAll(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long rpush(final byte[] key, final byte[]... args) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.42
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.rpush(key, args);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long lpush(final byte[] key, final byte[]... args) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.43
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.lpush(key, args);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long llen(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.44
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.llen(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<byte[]> lrange(final byte[] key, final long start, final long stop) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.45
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.lrange(key, start, stop);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public String ltrim(final byte[] key, final long start, final long stop) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.46
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.ltrim(key, start, stop);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] lindex(final byte[] key, final long index) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.47
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.lindex(key, index);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public String lset(final byte[] key, final long index, final byte[] value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.48
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.lset(key, index, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long lrem(final byte[] key, final long count, final byte[] value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.49
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.lrem(key, count, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] lpop(final byte[] key) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.50
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.lpop(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] rpop(final byte[] key) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.51
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.rpop(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long sadd(final byte[] key, final byte[]... member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.52
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sadd(key, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> smembers(final byte[] key) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.53
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.smembers(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long srem(final byte[] key, final byte[]... member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.54
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.srem(key, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] spop(final byte[] key) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.55
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.spop(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> spop(final byte[] key, final long count) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.56
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.spop(key, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long scard(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.57
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.scard(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Boolean sismember(final byte[] key, final byte[] member) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.58
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.sismember(key, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] srandmember(final byte[] key) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.59
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.srandmember(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long strlen(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.60
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.strlen(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zadd(final byte[] key, final double score, final byte[] member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.61
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, score, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.62
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, scoreMembers);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zadd(final byte[] key, final double score, final byte[] member, final ZAddParams params) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.63
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, score, member, params);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers, final ZAddParams params) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.64
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, scoreMembers, params);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrange(final byte[] key, final long start, final long stop) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.65
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrange(key, start, stop);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zrem(final byte[] key, final byte[]... members) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.66
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zrem(key, members);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Double zincrby(final byte[] key, final double increment, final byte[] member) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.67
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.zincrby(key, increment, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Double zincrby(final byte[] key, final double increment, final byte[] member, final ZIncrByParams params) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.68
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.zincrby(key, increment, member, params);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zrank(final byte[] key, final byte[] member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.69
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zrank(key, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zrevrank(final byte[] key, final byte[] member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.70
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zrevrank(key, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrevrange(final byte[] key, final long start, final long stop) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.71
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrevrange(key, start, stop);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrangeWithScores(final byte[] key, final long start, final long stop) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.72
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeWithScores(key, start, stop);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrevrangeWithScores(final byte[] key, final long start, final long stop) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.73
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeWithScores(key, start, stop);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zcard(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.74
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zcard(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Double zscore(final byte[] key, final byte[] member) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.75
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.zscore(key, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<byte[]> sort(final byte[] key) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.76
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.sort(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<byte[]> sort(final byte[] key, final SortingParams sortingParameters) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.77
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.sort(key, sortingParameters);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zcount(final byte[] key, final double min, final double max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.78
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zcount(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zcount(final byte[] key, final byte[] min, final byte[] max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.79
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zcount(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.80
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.81
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.82
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrangeByScore(final byte[] key, final double min, final double max, final int offset, final int count) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.83
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.84
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.85
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max, final double min, final int offset, final int count) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.86
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.87
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.88
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final double min, final double max, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.89
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.90
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.91
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.92
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.93
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final double max, final double min, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.94
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.95
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zremrangeByRank(final byte[] key, final long start, final long stop) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.96
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByRank(key, start, stop);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zremrangeByScore(final byte[] key, final double min, final double max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.97
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByScore(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zremrangeByScore(final byte[] key, final byte[] min, final byte[] max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.98
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByScore(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long linsert(final byte[] key, final BinaryClient.LIST_POSITION where, final byte[] pivot, final byte[] value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.99
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.linsert(key, where, pivot, value);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long lpushx(final byte[] key, final byte[]... arg) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.100
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.lpushx(key, arg);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long rpushx(final byte[] key, final byte[]... arg) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.101
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.rpushx(key, arg);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long del(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.102
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.del(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public byte[] echo(final byte[] arg) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.103
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.echo(arg);
            }
        }.runBinary(arg);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long bitcount(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.104
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitcount(key);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long bitcount(final byte[] key, final long start, final long end) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.105
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitcount(key, start, end);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long pfadd(final byte[] key, final byte[]... elements) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.106
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pfadd(key, elements);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public long pfcount(final byte[] key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.107
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return Long.valueOf(connection.pfcount(key));
            }
        }.runBinary(key).longValue();
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<byte[]> srandmember(final byte[] key, final int count) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.108
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.srandmember(key, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zlexcount(final byte[] key, final byte[] min, final byte[] max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.109
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zlexcount(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.110
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrangeByLex(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrangeByLex(final byte[] key, final byte[] min, final byte[] max, final int offset, final int count) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.111
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrangeByLex(key, min, max, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrevrangeByLex(final byte[] key, final byte[] max, final byte[] min) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.112
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrevrangeByLex(key, max, min);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Set<byte[]> zrevrangeByLex(final byte[] key, final byte[] max, final byte[] min, final int offset, final int count) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.113
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.zrevrangeByLex(key, max, min, offset, count);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long zremrangeByLex(final byte[] key, final byte[] min, final byte[] max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.114
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByLex(key, min, max);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public Object eval(final byte[] script, final byte[] keyCount, final byte[]... params) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.115
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.eval(script, keyCount, params);
            }
        }.runBinary(Integer.parseInt(SafeEncoder.encode(keyCount)), params);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public Object eval(final byte[] script, final int keyCount, final byte[]... params) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.116
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.eval(script, keyCount, params);
            }
        }.runBinary(keyCount, params);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public Object eval(final byte[] script, final List<byte[]> keys, final List<byte[]> args) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.117
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.eval(script, keys, args);
            }
        }.runBinary(keys.size(), (byte[][]) keys.toArray((Object[]) new byte[keys.size()]));
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public Object eval(final byte[] script, byte[] key) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.118
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.eval(script);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public Object evalsha(final byte[] sha1, byte[] key) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.119
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.evalsha(sha1);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public Object evalsha(final byte[] sha1, final List<byte[]> keys, final List<byte[]> args) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.120
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.evalsha(sha1, keys, args);
            }
        }.runBinary(keys.size(), (byte[][]) keys.toArray((Object[]) new byte[keys.size()]));
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public Object evalsha(final byte[] sha1, final int keyCount, final byte[]... params) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.121
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.evalsha(sha1, keyCount, params);
            }
        }.runBinary(keyCount, params);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public List<Long> scriptExists(byte[] key, final byte[][] sha1) {
        return new JedisClusterCommand<List<Long>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.122
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<Long> execute(Jedis connection) {
                return connection.scriptExists(sha1);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public byte[] scriptLoad(final byte[] script, byte[] key) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.123
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.scriptLoad(script);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public String scriptFlush(byte[] key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.124
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.scriptFlush();
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.JedisClusterBinaryScriptingCommands
    public String scriptKill(byte[] key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.125
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.scriptKill();
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long del(final byte[]... keys) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.126
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.del(keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public List<byte[]> blpop(final int timeout, final byte[]... keys) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.127
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.blpop(timeout, keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public List<byte[]> brpop(final int timeout, final byte[]... keys) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.128
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.brpop(timeout, keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public List<byte[]> mget(final byte[]... keys) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.129
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.mget(keys);
            }
        }.runBinary(keys.length, keys);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public String mset(final byte[]... keysvalues) {
        ?? r0 = new byte[keysvalues.length / 2];
        for (int keyIdx = 0; keyIdx < r0.length; keyIdx++) {
            r0[keyIdx] = keysvalues[keyIdx * 2];
        }
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.130
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.mset(keysvalues);
            }
        }.runBinary(r0.length, r0);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long msetnx(final byte[]... keysvalues) {
        ?? r0 = new byte[keysvalues.length / 2];
        for (int keyIdx = 0; keyIdx < r0.length; keyIdx++) {
            r0[keyIdx] = keysvalues[keyIdx * 2];
        }
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.131
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.msetnx(keysvalues);
            }
        }.runBinary(r0.length, r0);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public String rename(final byte[] oldkey, final byte[] newkey) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.132
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.rename(oldkey, newkey);
            }
        }.runBinary(2, new byte[]{oldkey, newkey});
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long renamenx(final byte[] oldkey, final byte[] newkey) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.133
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.renamenx(oldkey, newkey);
            }
        }.runBinary(2, new byte[]{oldkey, newkey});
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public byte[] rpoplpush(final byte[] srckey, final byte[] dstkey) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.134
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.rpoplpush(srckey, dstkey);
            }
        }.runBinary(2, new byte[]{srckey, dstkey});
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Set<byte[]> sdiff(final byte[]... keys) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.135
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.sdiff(keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long sdiffstore(final byte[] dstkey, final byte[]... keys) {
        byte[][] wholeKeys = KeyMergeUtil.merge(dstkey, keys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.136
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sdiffstore(dstkey, keys);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Set<byte[]> sinter(final byte[]... keys) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.137
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.sinter(keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long sinterstore(final byte[] dstkey, final byte[]... keys) {
        byte[][] wholeKeys = KeyMergeUtil.merge(dstkey, keys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.138
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sinterstore(dstkey, keys);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long smove(final byte[] srckey, final byte[] dstkey, final byte[] member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.139
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.smove(srckey, dstkey, member);
            }
        }.runBinary(2, new byte[]{srckey, dstkey});
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long sort(final byte[] key, final SortingParams sortingParameters, final byte[] dstkey) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.140
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sort(key, sortingParameters, dstkey);
            }
        }.runBinary(2, new byte[]{key, dstkey});
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long sort(final byte[] key, final byte[] dstkey) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.141
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sort(key, dstkey);
            }
        }.runBinary(2, new byte[]{key, dstkey});
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Set<byte[]> sunion(final byte[]... keys) {
        return new JedisClusterCommand<Set<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.142
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<byte[]> execute(Jedis connection) {
                return connection.sunion(keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long sunionstore(final byte[] dstkey, final byte[]... keys) {
        byte[][] wholeKeys = KeyMergeUtil.merge(dstkey, keys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.143
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sunionstore(dstkey, keys);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long zinterstore(final byte[] dstkey, final byte[]... sets) {
        byte[][] wholeKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.144
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zinterstore(dstkey, sets);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long zinterstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        byte[][] wholeKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.145
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zinterstore(dstkey, params, sets);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long zunionstore(final byte[] dstkey, final byte[]... sets) {
        byte[][] wholeKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.146
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zunionstore(dstkey, sets);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long zunionstore(final byte[] dstkey, final ZParams params, final byte[]... sets) {
        byte[][] wholeKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.147
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zunionstore(dstkey, params, sets);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public byte[] brpoplpush(final byte[] source, final byte[] destination, final int timeout) {
        return new JedisClusterCommand<byte[]>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.148
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public byte[] execute(Jedis connection) {
                return connection.brpoplpush(source, destination, timeout);
            }
        }.runBinary(2, new byte[]{source, destination});
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long publish(final byte[] channel, final byte[] message) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.149
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.publish(channel, message);
            }
        }.runWithAnyNode();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public void subscribe(final BinaryJedisPubSub jedisPubSub, final byte[]... channels) {
        new JedisClusterCommand<Integer>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.150
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Integer execute(Jedis connection) {
                connection.subscribe(jedisPubSub, channels);
                return 0;
            }
        }.runWithAnyNode();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public void psubscribe(final BinaryJedisPubSub jedisPubSub, final byte[]... patterns) {
        new JedisClusterCommand<Integer>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.151
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Integer execute(Jedis connection) {
                connection.psubscribe(jedisPubSub, patterns);
                return 0;
            }
        }.runWithAnyNode();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long bitop(final BitOP op, final byte[] destKey, final byte[]... srcKeys) {
        byte[][] wholeKeys = KeyMergeUtil.merge(destKey, srcKeys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.152
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitop(op, destKey, srcKeys);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public String pfmerge(final byte[] destkey, final byte[]... sourcekeys) {
        byte[][] wholeKeys = KeyMergeUtil.merge(destkey, sourcekeys);
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.153
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.pfmerge(destkey, sourcekeys);
            }
        }.runBinary(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryJedisClusterCommands
    public Long pfcount(final byte[]... keys) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.154
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pfcount(keys);
            }
        }.runBinary(keys.length, keys);
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String ping() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String quit() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String flushDB() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public Long dbSize() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String select(int index) {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String flushAll() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String auth(String password) {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String save() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String bgsave() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String bgrewriteaof() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public Long lastsave() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String shutdown() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String info() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String info(String section) {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String slaveof(String host, int port) {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String slaveofNoOne() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public Long getDB() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String debug(DebugParams params) {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public String configResetStat() {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BasicCommands
    @Deprecated
    public Long waitReplicas(int replicas, long timeout) {
        throw new JedisClusterException("No way to dispatch this command to Redis Cluster.");
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long geoadd(final byte[] key, final double longitude, final double latitude, final byte[] member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.155
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.geoadd(key, longitude, latitude, member);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Long geoadd(final byte[] key, final Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.156
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.geoadd(key, memberCoordinateMap);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Double geodist(final byte[] key, final byte[] member1, final byte[] member2) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.157
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.geodist(key, member1, member2);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public Double geodist(final byte[] key, final byte[] member1, final byte[] member2, final GeoUnit unit) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.158
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.geodist(key, member1, member2, unit);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<byte[]> geohash(final byte[] key, final byte[]... members) {
        return new JedisClusterCommand<List<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.159
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<byte[]> execute(Jedis connection) {
                return connection.geohash(key, members);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<GeoCoordinate> geopos(final byte[] key, final byte[]... members) {
        return new JedisClusterCommand<List<GeoCoordinate>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.160
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoCoordinate> execute(Jedis connection) {
                return connection.geopos(key, members);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<GeoRadiusResponse> georadius(final byte[] key, final double longitude, final double latitude, final double radius, final GeoUnit unit) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.161
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadius(key, longitude, latitude, radius, unit);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<GeoRadiusResponse> georadius(final byte[] key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.162
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadius(key, longitude, latitude, radius, unit, param);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<GeoRadiusResponse> georadiusByMember(final byte[] key, final byte[] member, final double radius, final GeoUnit unit) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.163
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadiusByMember(key, member, radius, unit);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<GeoRadiusResponse> georadiusByMember(final byte[] key, final byte[] member, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.164
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadiusByMember(key, member, radius, unit, param);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public ScanResult<byte[]> scan(final byte[] cursor, final ScanParams params) {
        String matchPattern;
        if (params == null || (matchPattern = params.match()) == null || matchPattern.isEmpty()) {
            throw new IllegalArgumentException(BinaryJedisCluster.class.getSimpleName() + " only supports SCAN commands with non-empty MATCH patterns");
        }
        if (!JedisClusterHashTagUtil.isClusterCompliantMatchPattern(matchPattern)) {
            throw new IllegalArgumentException(BinaryJedisCluster.class.getSimpleName() + " only supports SCAN commands with MATCH patterns containing hash-tags ( curly-brackets enclosed strings )");
        }
        return new JedisClusterCommand<ScanResult<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.165
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<byte[]> execute(Jedis connection) {
                return connection.scan(cursor, params);
            }
        }.runBinary(SafeEncoder.encode(matchPattern));
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(final byte[] key, final byte[] cursor) {
        return new JedisClusterCommand<ScanResult<Map.Entry<byte[], byte[]>>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.166
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Map.Entry<byte[], byte[]>> execute(Jedis connection) {
                return connection.hscan(key, cursor);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        return new JedisClusterCommand<ScanResult<Map.Entry<byte[], byte[]>>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.167
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Map.Entry<byte[], byte[]>> execute(Jedis connection) {
                return connection.hscan(key, cursor, params);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public ScanResult<byte[]> sscan(final byte[] key, final byte[] cursor) {
        return new JedisClusterCommand<ScanResult<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.168
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<byte[]> execute(Jedis connection) {
                return connection.sscan(key, cursor);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public ScanResult<byte[]> sscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        return new JedisClusterCommand<ScanResult<byte[]>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.169
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<byte[]> execute(Jedis connection) {
                return connection.sscan(key, cursor, params);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public ScanResult<Tuple> zscan(final byte[] key, final byte[] cursor) {
        return new JedisClusterCommand<ScanResult<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.170
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Tuple> execute(Jedis connection) {
                return connection.zscan(key, cursor);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public ScanResult<Tuple> zscan(final byte[] key, final byte[] cursor, final ScanParams params) {
        return new JedisClusterCommand<ScanResult<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.171
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Tuple> execute(Jedis connection) {
                return connection.zscan(key, cursor, params);
            }
        }.runBinary(key);
    }

    @Override // redis.clients.jedis.BinaryJedisClusterCommands
    public List<Long> bitfield(final byte[] key, final byte[]... arguments) {
        return new JedisClusterCommand<List<Long>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.BinaryJedisCluster.172
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<Long> execute(Jedis connection) {
                return connection.bitfield(key, arguments);
            }
        }.runBinary(key);
    }
}
