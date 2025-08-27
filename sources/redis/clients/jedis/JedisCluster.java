package redis.clients.jedis;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.JedisClusterHashTagUtil;
import redis.clients.util.KeyMergeUtil;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisCluster.class */
public class JedisCluster extends BinaryJedisCluster implements JedisCommands, MultiKeyJedisClusterCommands, JedisClusterScriptingCommands {

    /* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisCluster$Reset.class */
    public enum Reset {
        SOFT,
        HARD
    }

    public JedisCluster(HostAndPort node) {
        this((Set<HostAndPort>) Collections.singleton(node));
    }

    public JedisCluster(HostAndPort node, int timeout) {
        this((Set<HostAndPort>) Collections.singleton(node), timeout);
    }

    public JedisCluster(HostAndPort node, int timeout, int maxAttempts) {
        this((Set<HostAndPort>) Collections.singleton(node), timeout, maxAttempts);
    }

    public JedisCluster(HostAndPort node, GenericObjectPoolConfig poolConfig) {
        this((Set<HostAndPort>) Collections.singleton(node), poolConfig);
    }

    public JedisCluster(HostAndPort node, int timeout, GenericObjectPoolConfig poolConfig) {
        this((Set<HostAndPort>) Collections.singleton(node), timeout, poolConfig);
    }

    public JedisCluster(HostAndPort node, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        this((Set<HostAndPort>) Collections.singleton(node), timeout, maxAttempts, poolConfig);
    }

    public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        this((Set<HostAndPort>) Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, poolConfig);
    }

    public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        this((Set<HostAndPort>) Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    }

    public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig) {
        this((Set<HostAndPort>) Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
    }

    public JedisCluster(Set<HostAndPort> nodes) {
        this(nodes, 2000);
    }

    public JedisCluster(Set<HostAndPort> nodes, int timeout) {
        this(nodes, timeout, 5);
    }

    public JedisCluster(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
        this(nodes, timeout, maxAttempts, new GenericObjectPoolConfig());
    }

    public JedisCluster(Set<HostAndPort> nodes, GenericObjectPoolConfig poolConfig) {
        this(nodes, 2000, 5, poolConfig);
    }

    public JedisCluster(Set<HostAndPort> nodes, int timeout, GenericObjectPoolConfig poolConfig) {
        this(nodes, timeout, 5, poolConfig);
    }

    public JedisCluster(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, maxAttempts, poolConfig);
    }

    public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
    }

    public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
    }

    public JedisCluster(Set<HostAndPort> jedisClusterNode, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(final String key, final String value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.set(key, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.set(key, value, nxxx, expx, time);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String get(final String key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.get(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean exists(final String key) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.exists(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long exists(final String... keys) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.exists(keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long persist(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.persist(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String type(final String key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.type(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long expire(final String key, final int seconds) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.expire(key, seconds);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pexpire(final String key, final long milliseconds) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pexpire(key, milliseconds);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long expireAt(final String key, final long unixTime) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.expireAt(key, unixTime);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pexpireAt(key, millisecondsTimestamp);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long ttl(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.ttl(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pttl(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pttl(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean setbit(final String key, final long offset, final boolean value) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.setbit(key, offset, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean setbit(final String key, final long offset, final String value) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.setbit(key, offset, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean getbit(final String key, final long offset) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.getbit(key, offset);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long setrange(final String key, final long offset, final String value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.setrange(key, offset, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String getrange(final String key, final long startOffset, final long endOffset) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.18
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.getrange(key, startOffset, endOffset);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String getSet(final String key, final String value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.getSet(key, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long setnx(final String key, final String value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.20
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.setnx(key, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String setex(final String key, final int seconds, final String value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.setex(key, seconds, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String psetex(final String key, final long milliseconds, final String value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.22
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.psetex(key, milliseconds, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long decrBy(final String key, final long decrement) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.23
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.decrBy(key, decrement);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long decr(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.decr(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long incrBy(final String key, final long increment) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.25
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.incrBy(key, increment);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double incrByFloat(final String key, final double increment) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.26
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.incrByFloat(key, increment);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long incr(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.27
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.incr(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long append(final String key, final String value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.28
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.append(key, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String substr(final String key, final int start, final int end) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.substr(key, start, end);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hset(final String key, final String field, final String value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.30
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hset(key, field, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String hget(final String key, final String field) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.31
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.hget(key, field);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hsetnx(final String key, final String field, final String value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.32
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hsetnx(key, field, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String hmset(final String key, final Map<String, String> hash) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.33
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.hmset(key, hash);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> hmget(final String key, final String... fields) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.34
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.hmget(key, fields);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hincrBy(final String key, final String field, final long value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.35
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hincrBy(key, field, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double hincrByFloat(final String key, final String field, final double value) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.36
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.hincrByFloat(key, field, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean hexists(final String key, final String field) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.37
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.hexists(key, field);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hdel(final String key, final String... field) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.38
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hdel(key, field);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hlen(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.39
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.hlen(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> hkeys(final String key) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.40
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.hkeys(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> hvals(final String key) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.41
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.hvals(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Map<String, String> hgetAll(final String key) {
        return new JedisClusterCommand<Map<String, String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.42
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Map<String, String> execute(Jedis connection) {
                return connection.hgetAll(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long rpush(final String key, final String... string) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.43
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.rpush(key, string);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lpush(final String key, final String... string) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.44
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.lpush(key, string);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long llen(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.45
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.llen(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> lrange(final String key, final long start, final long stop) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.46
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.lrange(key, start, stop);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String ltrim(final String key, final long start, final long stop) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.47
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.ltrim(key, start, stop);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lindex(final String key, final long index) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.48
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.lindex(key, index);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lset(final String key, final long index, final String value) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.49
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.lset(key, index, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lrem(final String key, final long count, final String value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.50
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.lrem(key, count, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lpop(final String key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.51
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.lpop(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String rpop(final String key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.52
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.rpop(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long sadd(final String key, final String... member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.53
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sadd(key, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> smembers(final String key) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.54
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.smembers(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long srem(final String key, final String... member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.55
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.srem(key, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String spop(final String key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.56
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.spop(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> spop(final String key, final long count) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.57
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.spop(key, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long scard(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.58
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.scard(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean sismember(final String key, final String member) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.59
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.sismember(key, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String srandmember(final String key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.60
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.srandmember(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> srandmember(final String key, final int count) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.61
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.srandmember(key, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long strlen(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.62
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.strlen(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(final String key, final double score, final String member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.63
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, score, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(final String key, final double score, final String member, final ZAddParams params) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.64
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, score, member, params);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.65
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, scoreMembers);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.66
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zadd(key, scoreMembers, params);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrange(final String key, final long start, final long stop) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.67
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrange(key, start, stop);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrem(final String key, final String... members) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.68
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zrem(key, members);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zincrby(final String key, final double increment, final String member) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.69
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.zincrby(key, increment, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zincrby(final String key, final double increment, final String member, final ZIncrByParams params) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.70
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.zincrby(key, increment, member, params);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrank(final String key, final String member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.71
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zrank(key, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrevrank(final String key, final String member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.72
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zrevrank(key, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrange(final String key, final long start, final long stop) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.73
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrevrange(key, start, stop);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeWithScores(final String key, final long start, final long stop) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.74
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeWithScores(key, start, stop);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long stop) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.75
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeWithScores(key, start, stop);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcard(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.76
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zcard(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zscore(final String key, final String member) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.77
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.zscore(key, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> sort(final String key) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.78
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.sort(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> sort(final String key, final SortingParams sortingParameters) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.79
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.sort(key, sortingParameters);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcount(final String key, final double min, final double max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.80
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zcount(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcount(final String key, final String min, final String max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.81
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zcount(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.82
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.83
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.84
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.85
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.86
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.87
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrangeByScore(key, min, max, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.88
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.89
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.90
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.91
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.92
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByScore(key, max, min, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.93
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.94
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.95
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.96
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        return new JedisClusterCommand<Set<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.97
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<Tuple> execute(Jedis connection) {
                return connection.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByRank(final String key, final long start, final long stop) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.98
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByRank(key, start, stop);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByScore(final String key, final double min, final double max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.99
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByScore(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByScore(final String key, final String min, final String max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.100
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByScore(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zlexcount(final String key, final String min, final String max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.101
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zlexcount(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.102
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrangeByLex(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.103
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrangeByLex(key, min, max, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.104
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByLex(key, max, min);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByLex(final String key, final String max, final String min, final int offset, final int count) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.105
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.zrevrangeByLex(key, max, min, offset, count);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByLex(final String key, final String min, final String max) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.106
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zremrangeByLex(key, min, max);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot, final String value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.107
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.linsert(key, where, pivot, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lpushx(final String key, final String... string) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.108
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.lpushx(key, string);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long rpushx(final String key, final String... string) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.109
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.rpushx(key, string);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long del(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.110
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.del(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String echo(final String string) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.111
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.echo(string);
            }
        }.run(string);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitcount(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.112
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitcount(key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitcount(final String key, final long start, final long end) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.113
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitcount(key, start, end);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public ScanResult<String> scan(final String cursor, final ScanParams params) {
        String matchPattern;
        if (params == null || (matchPattern = params.match()) == null || matchPattern.isEmpty()) {
            throw new IllegalArgumentException(JedisCluster.class.getSimpleName() + " only supports SCAN commands with non-empty MATCH patterns");
        }
        if (!JedisClusterHashTagUtil.isClusterCompliantMatchPattern(matchPattern)) {
            throw new IllegalArgumentException(JedisCluster.class.getSimpleName() + " only supports SCAN commands with MATCH patterns containing hash-tags ( curly-brackets enclosed strings )");
        }
        return new JedisClusterCommand<ScanResult<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.114
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<String> execute(Jedis connection) {
                return connection.scan(cursor, params);
            }
        }.run(matchPattern);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitpos(final String key, final boolean value) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.115
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitpos(key, value);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitpos(final String key, final boolean value, final BitPosParams params) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.116
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitpos(key, value, params);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        return new JedisClusterCommand<ScanResult<Map.Entry<String, String>>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.117
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Map.Entry<String, String>> execute(Jedis connection) {
                return connection.hscan(key, cursor);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor, final ScanParams params) {
        return new JedisClusterCommand<ScanResult<Map.Entry<String, String>>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.118
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Map.Entry<String, String>> execute(Jedis connection) {
                return connection.hscan(key, cursor, params);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<String> sscan(final String key, final String cursor) {
        return new JedisClusterCommand<ScanResult<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.119
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<String> execute(Jedis connection) {
                return connection.sscan(key, cursor);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
        return new JedisClusterCommand<ScanResult<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.120
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<String> execute(Jedis connection) {
                return connection.sscan(key, cursor, params);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        return new JedisClusterCommand<ScanResult<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.121
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Tuple> execute(Jedis connection) {
                return connection.zscan(key, cursor);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
        return new JedisClusterCommand<ScanResult<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.122
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Tuple> execute(Jedis connection) {
                return connection.zscan(key, cursor, params);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pfadd(final String key, final String... elements) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.123
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.pfadd(key, elements);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public long pfcount(final String key) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.124
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return Long.valueOf(connection.pfcount(key));
            }
        }.run(key).longValue();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> blpop(final int timeout, final String key) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.125
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.blpop(timeout, key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> brpop(final int timeout, final String key) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.126
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.brpop(timeout, key);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long del(final String... keys) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.127
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.del(keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public List<String> blpop(final int timeout, final String... keys) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.128
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.blpop(timeout, keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public List<String> brpop(final int timeout, final String... keys) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.129
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.brpop(timeout, keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public List<String> mget(final String... keys) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.130
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.mget(keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public String mset(final String... keysvalues) {
        String[] keys = new String[keysvalues.length / 2];
        for (int keyIdx = 0; keyIdx < keys.length; keyIdx++) {
            keys[keyIdx] = keysvalues[keyIdx * 2];
        }
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.131
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.mset(keysvalues);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long msetnx(final String... keysvalues) {
        String[] keys = new String[keysvalues.length / 2];
        for (int keyIdx = 0; keyIdx < keys.length; keyIdx++) {
            keys[keyIdx] = keysvalues[keyIdx * 2];
        }
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.132
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.msetnx(keysvalues);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public String rename(final String oldkey, final String newkey) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.133
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.rename(oldkey, newkey);
            }
        }.run(2, oldkey, newkey);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long renamenx(final String oldkey, final String newkey) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.134
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.renamenx(oldkey, newkey);
            }
        }.run(2, oldkey, newkey);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public String rpoplpush(final String srckey, final String dstkey) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.135
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.rpoplpush(srckey, dstkey);
            }
        }.run(2, srckey, dstkey);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Set<String> sdiff(final String... keys) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.136
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.sdiff(keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long sdiffstore(final String dstkey, final String... keys) {
        String[] mergedKeys = KeyMergeUtil.merge(dstkey, keys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.137
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sdiffstore(dstkey, keys);
            }
        }.run(mergedKeys.length, mergedKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Set<String> sinter(final String... keys) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.138
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.sinter(keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long sinterstore(final String dstkey, final String... keys) {
        String[] mergedKeys = KeyMergeUtil.merge(dstkey, keys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.139
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sinterstore(dstkey, keys);
            }
        }.run(mergedKeys.length, mergedKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long smove(final String srckey, final String dstkey, final String member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.140
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.smove(srckey, dstkey, member);
            }
        }.run(2, srckey, dstkey);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.141
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sort(key, sortingParameters, dstkey);
            }
        }.run(2, key, dstkey);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long sort(final String key, final String dstkey) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.142
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sort(key, dstkey);
            }
        }.run(2, key, dstkey);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Set<String> sunion(final String... keys) {
        return new JedisClusterCommand<Set<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.143
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Set<String> execute(Jedis connection) {
                return connection.sunion(keys);
            }
        }.run(keys.length, keys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long sunionstore(final String dstkey, final String... keys) {
        String[] wholeKeys = KeyMergeUtil.merge(dstkey, keys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.144
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.sunionstore(dstkey, keys);
            }
        }.run(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long zinterstore(final String dstkey, final String... sets) {
        String[] wholeKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.145
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zinterstore(dstkey, sets);
            }
        }.run(wholeKeys.length, wholeKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
        String[] mergedKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.146
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zinterstore(dstkey, params, sets);
            }
        }.run(mergedKeys.length, mergedKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long zunionstore(final String dstkey, final String... sets) {
        String[] mergedKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.147
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zunionstore(dstkey, sets);
            }
        }.run(mergedKeys.length, mergedKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
        String[] mergedKeys = KeyMergeUtil.merge(dstkey, sets);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.148
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.zunionstore(dstkey, params, sets);
            }
        }.run(mergedKeys.length, mergedKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public String brpoplpush(final String source, final String destination, final int timeout) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.149
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.brpoplpush(source, destination, timeout);
            }
        }.run(2, source, destination);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long publish(final String channel, final String message) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.150
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.publish(channel, message);
            }
        }.runWithAnyNode();
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        new JedisClusterCommand<Integer>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.151
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Integer execute(Jedis connection) {
                connection.subscribe(jedisPubSub, channels);
                return 0;
            }
        }.runWithAnyNode();
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
        new JedisClusterCommand<Integer>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.152
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Integer execute(Jedis connection) {
                connection.psubscribe(jedisPubSub, patterns);
                return 0;
            }
        }.runWithAnyNode();
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public Long bitop(final BitOP op, final String destKey, final String... srcKeys) {
        String[] mergedKeys = KeyMergeUtil.merge(destKey, srcKeys);
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.153
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.bitop(op, destKey, srcKeys);
            }
        }.run(mergedKeys.length, mergedKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public String pfmerge(final String destkey, final String... sourcekeys) {
        String[] mergedKeys = KeyMergeUtil.merge(destkey, sourcekeys);
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.154
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.pfmerge(destkey, sourcekeys);
            }
        }.run(mergedKeys.length, mergedKeys);
    }

    @Override // redis.clients.jedis.MultiKeyJedisClusterCommands
    public long pfcount(final String... keys) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.155
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return Long.valueOf(connection.pfcount(keys));
            }
        }.run(keys.length, keys).longValue();
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public Object eval(final String script, final int keyCount, final String... params) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.156
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.eval(script, keyCount, params);
            }
        }.run(keyCount, params);
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public Object eval(final String script, String key) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.157
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.eval(script);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public Object eval(final String script, final List<String> keys, final List<String> args) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.158
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.eval(script, keys, args);
            }
        }.run(keys.size(), (String[]) keys.toArray(new String[keys.size()]));
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public Object evalsha(final String sha1, final int keyCount, final String... params) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.159
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.evalsha(sha1, keyCount, params);
            }
        }.run(keyCount, params);
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public Object evalsha(final String sha1, final List<String> keys, final List<String> args) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.160
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.evalsha(sha1, keys, args);
            }
        }.run(keys.size(), (String[]) keys.toArray(new String[keys.size()]));
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public Object evalsha(final String sha1, String key) {
        return new JedisClusterCommand<Object>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.161
            @Override // redis.clients.jedis.JedisClusterCommand
            public Object execute(Jedis connection) {
                return connection.evalsha(sha1);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public Boolean scriptExists(final String sha1, String key) {
        return new JedisClusterCommand<Boolean>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.162
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Boolean execute(Jedis connection) {
                return connection.scriptExists(sha1);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public List<Boolean> scriptExists(String key, final String... sha1) {
        return new JedisClusterCommand<List<Boolean>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.163
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<Boolean> execute(Jedis connection) {
                return connection.scriptExists(sha1);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisClusterScriptingCommands
    public String scriptLoad(final String script, String key) {
        return new JedisClusterCommand<String>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.164
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public String execute(Jedis connection) {
                return connection.scriptLoad(script);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public String set(String key, String value, String nxxx) {
        if (setnx(key, value).longValue() == 1) {
            return "OK";
        }
        return null;
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public List<String> blpop(final String arg) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.165
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.blpop(arg);
            }
        }.run(arg);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public List<String> brpop(final String arg) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.166
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.brpop(arg);
            }
        }.run(arg);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public Long move(final String key, final int dbIndex) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.167
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.move(key, dbIndex);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<Map.Entry<String, String>> hscan(final String key, final int cursor) {
        return new JedisClusterCommand<ScanResult<Map.Entry<String, String>>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.168
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Map.Entry<String, String>> execute(Jedis connection) {
                return connection.hscan(key, cursor);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<String> sscan(final String key, final int cursor) {
        return new JedisClusterCommand<ScanResult<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.169
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<String> execute(Jedis connection) {
                return connection.sscan(key, cursor);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<Tuple> zscan(final String key, final int cursor) {
        return new JedisClusterCommand<ScanResult<Tuple>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.170
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public ScanResult<Tuple> execute(Jedis connection) {
                return connection.zscan(key, cursor);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long geoadd(final String key, final double longitude, final double latitude, final String member) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.171
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.geoadd(key, longitude, latitude, member);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long geoadd(final String key, final Map<String, GeoCoordinate> memberCoordinateMap) {
        return new JedisClusterCommand<Long>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.172
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Long execute(Jedis connection) {
                return connection.geoadd(key, memberCoordinateMap);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double geodist(final String key, final String member1, final String member2) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.173
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.geodist(key, member1, member2);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double geodist(final String key, final String member1, final String member2, final GeoUnit unit) {
        return new JedisClusterCommand<Double>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.174
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public Double execute(Jedis connection) {
                return connection.geodist(key, member1, member2, unit);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> geohash(final String key, final String... members) {
        return new JedisClusterCommand<List<String>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.175
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<String> execute(Jedis connection) {
                return connection.geohash(key, members);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoCoordinate> geopos(final String key, final String... members) {
        return new JedisClusterCommand<List<GeoCoordinate>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.176
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoCoordinate> execute(Jedis connection) {
                return connection.geopos(key, members);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.177
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadius(key, longitude, latitude, radius, unit);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.178
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadius(key, longitude, latitude, radius, unit, param);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.179
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadiusByMember(key, member, radius, unit);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return new JedisClusterCommand<List<GeoRadiusResponse>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.180
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<GeoRadiusResponse> execute(Jedis connection) {
                return connection.georadiusByMember(key, member, radius, unit, param);
            }
        }.run(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<Long> bitfield(final String key, final String... arguments) {
        return new JedisClusterCommand<List<Long>>(this.connectionHandler, this.maxAttempts) { // from class: redis.clients.jedis.JedisCluster.181
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // redis.clients.jedis.JedisClusterCommand
            public List<Long> execute(Jedis connection) {
                return connection.bitfield(key, arguments);
            }
        }.run(key);
    }
}
