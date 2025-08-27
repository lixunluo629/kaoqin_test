package org.springframework.data.redis.connection.jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.PropertyAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.ClusterStateFailureException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.FallbackExceptionTranslationStrategy;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.ClusterCommandExecutor;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.ClusterNodeResourceProvider;
import org.springframework.data.redis.connection.ClusterSlotHashUtil;
import org.springframework.data.redis.connection.ClusterTopology;
import org.springframework.data.redis.connection.ClusterTopologyProvider;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisClusterCommands;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPipelineException;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisSubscribedConnectionException;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.connection.Subscription;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.util.ByteArraySet;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanCursor;
import org.springframework.data.redis.core.ScanIteration;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterConnectionHandler;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.geo.GeoRadiusParam;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisClusterConnection.class */
public class JedisClusterConnection implements RedisClusterConnection {
    private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new FallbackExceptionTranslationStrategy(JedisConverters.exceptionConverter());
    private final Log log = LogFactory.getLog(getClass());
    private final JedisCluster cluster;
    private boolean closed;
    private final JedisClusterTopologyProvider topologyProvider;
    private ClusterCommandExecutor clusterCommandExecutor;
    private final boolean disposeClusterCommandExecutorOnClose;
    private volatile JedisSubscription subscription;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisClusterConnection$JedisClusterCommandCallback.class */
    protected interface JedisClusterCommandCallback<T> extends ClusterCommandExecutor.ClusterCommandCallback<Jedis, T> {
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisClusterConnection$JedisMultiKeyClusterCommandCallback.class */
    protected interface JedisMultiKeyClusterCommandCallback<T> extends ClusterCommandExecutor.MultiKeyClusterCommandCallback<Jedis, T> {
    }

    public JedisClusterConnection(JedisCluster cluster) {
        Assert.notNull(cluster, "JedisCluster must not be null.");
        this.cluster = cluster;
        this.closed = false;
        this.topologyProvider = new JedisClusterTopologyProvider(cluster);
        this.clusterCommandExecutor = new ClusterCommandExecutor(this.topologyProvider, new JedisClusterNodeResourceProvider(cluster, this.topologyProvider), EXCEPTION_TRANSLATION);
        this.disposeClusterCommandExecutorOnClose = true;
        try {
            DirectFieldAccessor dfa = new DirectFieldAccessor(cluster);
            this.clusterCommandExecutor.setMaxRedirects(((Integer) dfa.getPropertyValue("maxRedirections")).intValue());
        } catch (Exception e) {
        }
    }

    public JedisClusterConnection(JedisCluster cluster, ClusterCommandExecutor executor) {
        Assert.notNull(cluster, "JedisCluster must not be null.");
        Assert.notNull(executor, "ClusterCommandExecutor must not be null.");
        this.closed = false;
        this.cluster = cluster;
        this.topologyProvider = new JedisClusterTopologyProvider(cluster);
        this.clusterCommandExecutor = executor;
        this.disposeClusterCommandExecutorOnClose = false;
    }

    @Override // org.springframework.data.redis.connection.RedisCommands
    public Object execute(String command, byte[]... args) {
        throw new UnsupportedOperationException("Execute is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long del(byte[]... keys) {
        Assert.noNullElements(keys, "Keys must not be null or contain null key!");
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            try {
                return this.cluster.del(keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        return Long.valueOf(this.clusterCommandExecutor.executeMuliKeyCommand(new JedisMultiKeyClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.1
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public Long doInCluster(Jedis client, byte[] key) {
                return client.del(key);
            }
        }, Arrays.asList(keys)).resultsAsList().size());
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public DataType type(byte[] key) {
        try {
            return JedisConverters.toDataType(this.cluster.type(key));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Set<byte[]> keys(final byte[] pattern) {
        Assert.notNull(pattern, "Pattern must not be null!");
        Collection<Set<byte[]>> keysPerNode = this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.2
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Set<byte[]> doInCluster(Jedis client) {
                return client.keys(pattern);
            }
        }).resultsAsList();
        Set<byte[]> keys = new HashSet<>();
        for (Set<byte[]> keySet : keysPerNode) {
            keys.addAll(keySet);
        }
        return keys;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Set<byte[]> keys(RedisClusterNode node, final byte[] pattern) {
        Assert.notNull(pattern, "Pattern must not be null!");
        return (Set) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.3
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Set<byte[]> doInCluster(Jedis client) {
                return client.keys(pattern);
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Cursor<byte[]> scan(ScanOptions options) {
        throw new InvalidDataAccessApiUsageException("Scan is not supported accros multiple nodes within a cluster");
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] randomKey() {
        RedisClusterNode node;
        List<RedisClusterNode> nodes = new ArrayList<>(this.topologyProvider.getTopology().getActiveMasterNodes());
        Set<RedisNode> inspectedNodes = new HashSet<>(nodes.size());
        do {
            RedisClusterNode redisClusterNode = nodes.get(new Random().nextInt(nodes.size()));
            while (true) {
                node = redisClusterNode;
                if (!inspectedNodes.contains(node)) {
                    break;
                }
                redisClusterNode = nodes.get(new Random().nextInt(nodes.size()));
            }
            inspectedNodes.add(node);
            byte[] key = randomKey(node);
            if (key != null && key.length > 0) {
                return key;
            }
        } while (nodes.size() != inspectedNodes.size());
        return null;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public byte[] randomKey(RedisClusterNode node) {
        return (byte[]) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<byte[]>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.4
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public byte[] doInCluster(Jedis client) {
                return client.randomBinaryKey();
            }
        }, node).getValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v7, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void rename(byte[] sourceKey, byte[] targetKey) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{sourceKey, targetKey})) {
            try {
                this.cluster.rename(sourceKey, targetKey);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        } else {
            byte[] value = dump(sourceKey);
            if (value != null && value.length > 0) {
                restore(targetKey, 0L, value);
                del(new byte[]{sourceKey});
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v8, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean renameNX(byte[] sourceKey, byte[] targetKey) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{sourceKey, targetKey})) {
            try {
                return JedisConverters.toBoolean(this.cluster.renamenx(sourceKey, targetKey));
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        byte[] value = dump(sourceKey);
        if (value != null && value.length > 0 && !exists(targetKey).booleanValue()) {
            restore(targetKey, 0L, value);
            del(new byte[]{sourceKey});
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expire(byte[] key, long seconds) {
        if (seconds > 2147483647L) {
            throw new UnsupportedOperationException("Jedis does not support seconds exceeding Integer.MAX_VALUE.");
        }
        try {
            return JedisConverters.toBoolean(this.cluster.expire(key, Long.valueOf(seconds).intValue()));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpire(byte[] key, long millis) {
        try {
            return JedisConverters.toBoolean(this.cluster.pexpire(key, millis));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expireAt(byte[] key, long unixTime) {
        try {
            return JedisConverters.toBoolean(this.cluster.expireAt(key, unixTime));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        try {
            return JedisConverters.toBoolean(this.cluster.pexpireAt(key, unixTimeInMillis));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean persist(byte[] key) {
        try {
            return JedisConverters.toBoolean(this.cluster.persist(key));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean move(byte[] key, int dbIndex) {
        throw new UnsupportedOperationException("Cluster mode does not allow moving keys.");
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key) {
        try {
            return this.cluster.ttl(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        try {
            return Long.valueOf(Converters.secondsToTimeUnit(this.cluster.ttl(key).longValue(), timeUnit));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(final byte[] key) {
        return (Long) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.5
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(Jedis client) {
                return client.pttl(key);
            }
        }, this.topologyProvider.getTopology().getKeyServingMasterNode(key)).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(final byte[] key, final TimeUnit timeUnit) {
        return (Long) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.6
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(Jedis client) {
                return Long.valueOf(Converters.millisecondsToTimeUnit(client.pttl(key).longValue(), timeUnit));
            }
        }, this.topologyProvider.getTopology().getKeyServingMasterNode(key)).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public List<byte[]> sort(byte[] key, SortParameters params) {
        try {
            return this.cluster.sort(key, JedisConverters.toSortingParams(params));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        List<byte[]> sorted = sort(key, params);
        if (!CollectionUtils.isEmpty(sorted)) {
            byte[] bArr = new byte[sorted.size()];
            switch (type(key)) {
                case SET:
                    sAdd(storeKey, (byte[][]) sorted.toArray((Object[]) bArr));
                    return 1L;
                case LIST:
                    lPush(storeKey, (byte[][]) sorted.toArray((Object[]) bArr));
                    return 1L;
                default:
                    throw new IllegalArgumentException("sort and store is only supported for SET and LIST");
            }
        }
        return 0L;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] dump(final byte[] key) {
        return (byte[]) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<byte[]>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.7
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public byte[] doInCluster(Jedis client) {
                return client.dump(key);
            }
        }, this.topologyProvider.getTopology().getKeyServingMasterNode(key)).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void restore(final byte[] key, final long ttlInMillis, final byte[] serializedValue) {
        if (ttlInMillis > 2147483647L) {
            throw new UnsupportedOperationException("Jedis does not support ttlInMillis exceeding Integer.MAX_VALUE.");
        }
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.8
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.restore(key, Long.valueOf(ttlInMillis).intValue(), serializedValue);
            }
        }, clusterGetNodeForKey(key));
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] get(byte[] key) {
        try {
            return this.cluster.get(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getSet(byte[] key, byte[] value) {
        try {
            return this.cluster.getSet(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public List<byte[]> mGet(byte[]... keys) {
        Assert.noNullElements(keys, "Keys must not contain null elements!");
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            return this.cluster.mget(keys);
        }
        return this.clusterCommandExecutor.executeMuliKeyCommand(new JedisMultiKeyClusterCommandCallback<byte[]>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.9
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public byte[] doInCluster(Jedis client, byte[] key) {
                return client.get(key);
            }
        }, Arrays.asList(keys)).resultsAsListSortBy(keys);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value) {
        try {
            this.cluster.set(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value, Expiration expiration, RedisStringCommands.SetOption option) {
        if (expiration == null || expiration.isPersistent()) {
            if (option == null || ObjectUtils.nullSafeEquals(RedisStringCommands.SetOption.UPSERT, option)) {
                set(key, value);
                return;
            } else {
                if (ObjectUtils.nullSafeEquals(RedisStringCommands.SetOption.SET_IF_PRESENT, option)) {
                    throw new UnsupportedOperationException("Jedis does not support SET XX without PX or EX on BinaryCluster.");
                }
                setNX(key, value);
                return;
            }
        }
        if (option == null || ObjectUtils.nullSafeEquals(RedisStringCommands.SetOption.UPSERT, option)) {
            if (ObjectUtils.nullSafeEquals(TimeUnit.MILLISECONDS, expiration.getTimeUnit())) {
                pSetEx(key, expiration.getExpirationTime(), value);
                return;
            } else {
                setEx(key, expiration.getExpirationTime(), value);
                return;
            }
        }
        byte[] nxxx = JedisConverters.toSetCommandNxXxArgument(option);
        byte[] expx = JedisConverters.toSetCommandExPxArgument(expiration);
        try {
            this.cluster.set(key, value, nxxx, expx, expiration.getExpirationTime());
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setNX(byte[] key, byte[] value) {
        try {
            return JedisConverters.toBoolean(this.cluster.setnx(key, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setEx(byte[] key, long seconds, byte[] value) {
        if (seconds > 2147483647L) {
            throw new IllegalArgumentException("Seconds have cannot exceed Integer.MAX_VALUE!");
        }
        try {
            this.cluster.setex(key, Long.valueOf(seconds).intValue(), value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void pSetEx(final byte[] key, final long milliseconds, final byte[] value) {
        if (milliseconds > 2147483647L) {
            throw new IllegalArgumentException("Milliseconds have cannot exceed Integer.MAX_VALUE!");
        }
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.10
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.psetex(key, milliseconds, value);
            }
        }, this.topologyProvider.getTopology().getKeyServingMasterNode(key));
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void mSet(Map<byte[], byte[]> tuples) {
        Assert.notNull(tuples, "Tuples must not be null!");
        if (ClusterSlotHashUtil.isSameSlotForAllKeys((byte[][]) tuples.keySet().toArray((Object[]) new byte[tuples.keySet().size()]))) {
            try {
                this.cluster.mset(JedisConverters.toByteArrays(tuples));
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        } else {
            for (Map.Entry<byte[], byte[]> entry : tuples.entrySet()) {
                set(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean mSetNX(Map<byte[], byte[]> tuples) {
        Assert.notNull(tuples, "Tuple must not be null!");
        if (ClusterSlotHashUtil.isSameSlotForAllKeys((byte[][]) tuples.keySet().toArray((Object[]) new byte[tuples.keySet().size()]))) {
            try {
                return JedisConverters.toBoolean(this.cluster.msetnx(JedisConverters.toByteArrays(tuples)));
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        boolean result = true;
        for (Map.Entry<byte[], byte[]> entry : tuples.entrySet()) {
            if (!setNX(entry.getKey(), entry.getValue()).booleanValue() && result) {
                result = false;
            }
        }
        return Boolean.valueOf(result);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incr(byte[] key) {
        try {
            return this.cluster.incr(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incrBy(byte[] key, long value) {
        try {
            return this.cluster.incrBy(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Double incrBy(byte[] key, double value) {
        try {
            return this.cluster.incrByFloat(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decr(byte[] key) {
        try {
            return this.cluster.decr(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decrBy(byte[] key, long value) {
        try {
            return this.cluster.decrBy(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long append(byte[] key, byte[] value) {
        try {
            return this.cluster.append(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getRange(byte[] key, long begin, long end) {
        try {
            return this.cluster.getrange(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setRange(byte[] key, byte[] value, long offset) {
        try {
            this.cluster.setrange(key, offset, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean getBit(byte[] key, long offset) {
        try {
            return this.cluster.getbit(key, offset);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setBit(byte[] key, long offset, boolean value) {
        try {
            return this.cluster.setbit(key, offset, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key) {
        try {
            return this.cluster.bitcount(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key, long begin, long end) {
        try {
            return this.cluster.bitcount(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destination, keys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                return this.cluster.bitop(JedisConverters.toBitOp(op), destination, keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("BITOP is only supported for same slot keys in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long strLen(byte[] key) {
        try {
            return this.cluster.strlen(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPush(byte[] key, byte[]... values) {
        try {
            return this.cluster.rpush(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPush(byte[] key, byte[]... values) {
        try {
            return this.cluster.lpush(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPushX(byte[] key, byte[] value) {
        try {
            return this.cluster.rpushx(key, (byte[][]) new byte[]{value});
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPushX(byte[] key, byte[] value) {
        try {
            return this.cluster.lpushx(key, (byte[][]) new byte[]{value});
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lLen(byte[] key) {
        try {
            return this.cluster.llen(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> lRange(byte[] key, long begin, long end) {
        try {
            return this.cluster.lrange(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lTrim(byte[] key, long begin, long end) {
        try {
            this.cluster.ltrim(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lIndex(byte[] key, long index) {
        try {
            return this.cluster.lindex(key, index);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lInsert(byte[] key, RedisListCommands.Position where, byte[] pivot, byte[] value) {
        try {
            return this.cluster.linsert(key, JedisConverters.toListPosition(where), pivot, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lSet(byte[] key, long index, byte[] value) {
        try {
            this.cluster.lset(key, index, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lRem(byte[] key, long count, byte[] value) {
        try {
            return this.cluster.lrem(key, count, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lPop(byte[] key) {
        try {
            return this.cluster.lpop(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPop(byte[] key) {
        try {
            return this.cluster.rpop(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bLPop(final int timeout, byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            try {
                return this.cluster.blpop(timeout, keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        return (List) this.clusterCommandExecutor.executeMuliKeyCommand(new JedisMultiKeyClusterCommandCallback<List<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.11
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public List<byte[]> doInCluster(Jedis jedis, byte[] key) {
                return jedis.blpop(timeout, (byte[][]) new byte[]{key});
            }
        }, Arrays.asList(keys)).getFirstNonNullNotEmptyOrDefault(Collections.emptyList());
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bRPop(final int timeout, byte[]... keys) {
        return (List) this.clusterCommandExecutor.executeMuliKeyCommand(new JedisMultiKeyClusterCommandCallback<List<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.12
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public List<byte[]> doInCluster(Jedis jedis, byte[] key) {
                return jedis.brpop(timeout, (byte[][]) new byte[]{key});
            }
        }, Arrays.asList(keys)).getFirstNonNullNotEmptyOrDefault(Collections.emptyList());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v4, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{srcKey, dstKey})) {
            try {
                return this.cluster.rpoplpush(srcKey, dstKey);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        byte[] val = rPop(srcKey);
        lPush(dstKey, new byte[]{val});
        return val;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v4, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v6, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{srcKey, dstKey})) {
            try {
                return this.cluster.brpoplpush(srcKey, dstKey, timeout);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        List<byte[]> val = bRPop(timeout, new byte[]{srcKey});
        if (!CollectionUtils.isEmpty(val)) {
            lPush(dstKey, new byte[]{val.get(1)});
            return val.get(1);
        }
        return null;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sAdd(byte[] key, byte[]... values) {
        try {
            return this.cluster.sadd(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sRem(byte[] key, byte[]... values) {
        try {
            return this.cluster.srem(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sPop(byte[] key) {
        try {
            return this.cluster.spop(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v4, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v7, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{srcKey, destKey})) {
            try {
                return JedisConverters.toBoolean(this.cluster.smove(srcKey, destKey, value));
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        if (exists(srcKey).booleanValue() && sRem(srcKey, new byte[]{value}).longValue() > 0 && !sIsMember(destKey, value).booleanValue()) {
            return JedisConverters.toBoolean(sAdd(destKey, new byte[]{value}));
        }
        return Boolean.FALSE;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sCard(byte[] key) {
        try {
            return this.cluster.scard(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sIsMember(byte[] key, byte[] value) {
        try {
            return this.cluster.sismember(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sInter(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            try {
                return this.cluster.sinter(keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        Collection<Set<byte[]>> resultList = this.clusterCommandExecutor.executeMuliKeyCommand(new JedisMultiKeyClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.13
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public Set<byte[]> doInCluster(Jedis client, byte[] key) {
                return client.smembers(key);
            }
        }, Arrays.asList(keys)).resultsAsList();
        ByteArraySet result = null;
        for (Set<byte[]> value : resultList) {
            ByteArraySet tmp = new ByteArraySet(value);
            if (result == null) {
                result = tmp;
            } else {
                result.retainAll(tmp);
                if (result.isEmpty()) {
                    break;
                }
            }
        }
        if (result.isEmpty()) {
            return Collections.emptySet();
        }
        return result.asRawSet();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, keys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                return this.cluster.sinterstore(destKey, keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        Set<byte[]> result = sInter(keys);
        if (result.isEmpty()) {
            return 0L;
        }
        return sAdd(destKey, (byte[][]) result.toArray((Object[]) new byte[result.size()]));
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sUnion(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            try {
                return this.cluster.sunion(keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        Collection<Set<byte[]>> resultList = this.clusterCommandExecutor.executeMuliKeyCommand(new JedisMultiKeyClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.14
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public Set<byte[]> doInCluster(Jedis client, byte[] key) {
                return client.smembers(key);
            }
        }, Arrays.asList(keys)).resultsAsList();
        ByteArraySet result = new ByteArraySet();
        for (Set<byte[]> entry : resultList) {
            result.addAll((Iterable<byte[]>) entry);
        }
        if (result.isEmpty()) {
            return Collections.emptySet();
        }
        return result.asRawSet();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, keys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                return this.cluster.sunionstore(destKey, keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        Set<byte[]> result = sUnion(keys);
        if (result.isEmpty()) {
            return 0L;
        }
        return sAdd(destKey, (byte[][]) result.toArray((Object[]) new byte[result.size()]));
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sDiff(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            try {
                return this.cluster.sdiff(keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        byte[] source = keys[0];
        byte[][] others = (byte[][]) Arrays.copyOfRange(keys, 1, keys.length);
        ByteArraySet values = new ByteArraySet(sMembers(source));
        Collection<Set<byte[]>> resultList = this.clusterCommandExecutor.executeMuliKeyCommand(new JedisMultiKeyClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.15
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public Set<byte[]> doInCluster(Jedis client, byte[] key) {
                return client.smembers(key);
            }
        }, Arrays.asList(others)).resultsAsList();
        if (values.isEmpty()) {
            return Collections.emptySet();
        }
        for (Set<byte[]> singleNodeValue : resultList) {
            values.removeAll(singleNodeValue);
        }
        return values.asRawSet();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, keys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                return this.cluster.sdiffstore(destKey, keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        Set<byte[]> diff = sDiff(keys);
        if (diff.isEmpty()) {
            return 0L;
        }
        return sAdd(destKey, (byte[][]) diff.toArray((Object[]) new byte[diff.size()]));
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sMembers(byte[] key) {
        try {
            return this.cluster.smembers(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sRandMember(byte[] key) {
        try {
            return this.cluster.srandmember(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public List<byte[]> sRandMember(byte[] key, long count) {
        if (count > 2147483647L) {
            throw new IllegalArgumentException("Count cannot exceed Integer.MAX_VALUE!");
        }
        try {
            return this.cluster.srandmember(key, Long.valueOf(count).intValue());
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Cursor<byte[]> sScan(final byte[] key, ScanOptions options) {
        return new ScanCursor<byte[]>(options) { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.16
            @Override // org.springframework.data.redis.core.ScanCursor
            protected ScanIteration<byte[]> doScan(long cursorId, ScanOptions options2) {
                ScanParams params = JedisConverters.toScanParams(options2);
                ScanResult<byte[]> result = JedisClusterConnection.this.cluster.sscan(key, JedisConverters.toBytes(Long.valueOf(cursorId)), params);
                return new ScanIteration<>(Long.valueOf(result.getStringCursor()).longValue(), result.getResult());
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        try {
            return JedisConverters.toBoolean(this.cluster.zadd(key, score, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zAdd(byte[] key, Set<RedisZSetCommands.Tuple> tuples) {
        try {
            return this.cluster.zadd(key, JedisConverters.toTupleMap(tuples));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRem(byte[] key, byte[]... values) {
        try {
            return this.cluster.zrem(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        try {
            return this.cluster.zincrby(key, increment, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRank(byte[] key, byte[] value) {
        try {
            return this.cluster.zrank(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRevRank(byte[] key, byte[] value) {
        try {
            return this.cluster.zrevrank(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRange(byte[] key, long begin, long end) {
        try {
            return this.cluster.zrange(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return zRangeByScoreWithScores(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZRANGEBYSCOREWITHSCORES.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (limit != null) {
                return JedisConverters.toTupleSet(this.cluster.zrangeByScoreWithScores(key, min, max, limit.getOffset(), limit.getCount()));
            }
            return JedisConverters.toTupleSet(this.cluster.zrangeByScoreWithScores(key, min, max));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return zRevRangeByScore(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZREVRANGEBYSCORE.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (limit != null) {
                return this.cluster.zrevrangeByScore(key, max, min, limit.getOffset(), limit.getCount());
            }
            return this.cluster.zrevrangeByScore(key, max, min);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        return zRevRangeByScoreWithScores(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZREVRANGEBYSCOREWITHSCORES.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (limit != null) {
                return JedisConverters.toTupleSet(this.cluster.zrevrangeByScoreWithScores(key, max, min, limit.getOffset(), limit.getCount()));
            }
            return JedisConverters.toTupleSet(this.cluster.zrevrangeByScoreWithScores(key, max, min));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, RedisZSetCommands.Range range) {
        Assert.notNull(range, "Range cannot be null for ZCOUNT.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            return this.cluster.zcount(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        Assert.notNull(range, "Range cannot be null for ZREMRANGEBYSCORE.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            return this.cluster.zremrangeByScore(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        return zRangeByScore(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZRANGEBYSCORE.");
        byte[] min = JedisConverters.boundaryToBytesForZRange(range.getMin(), JedisConverters.NEGATIVE_INFINITY_BYTES);
        byte[] max = JedisConverters.boundaryToBytesForZRange(range.getMax(), JedisConverters.POSITIVE_INFINITY_BYTES);
        try {
            if (limit != null) {
                return this.cluster.zrangeByScore(key, min, max, limit.getOffset(), limit.getCount());
            }
            return this.cluster.zrangeByScore(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key) {
        return zRangeByLex(key, RedisZSetCommands.Range.unbounded());
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range) {
        return zRangeByLex(key, range, null);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Assert.notNull(range, "Range cannot be null for ZRANGEBYLEX.");
        byte[] min = JedisConverters.boundaryToBytesForZRangeByLex(range.getMin(), JedisConverters.toBytes("-"));
        byte[] max = JedisConverters.boundaryToBytesForZRangeByLex(range.getMax(), JedisConverters.toBytes("+"));
        try {
            if (limit != null) {
                return this.cluster.zrangeByLex(key, min, max, limit.getOffset(), limit.getCount());
            }
            return this.cluster.zrangeByLex(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(byte[] key, long begin, long end) {
        try {
            return JedisConverters.toTupleSet(this.cluster.zrangeWithScores(key, begin, end));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        try {
            return this.cluster.zrangeByScore(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        try {
            return JedisConverters.toTupleSet(this.cluster.zrangeByScoreWithScores(key, min, max));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        if (offset > 2147483647L || count > 2147483647L) {
            throw new IllegalArgumentException("Count/Offset cannot exceed Integer.MAX_VALUE!");
        }
        try {
            return this.cluster.zrangeByScore(key, min, max, Long.valueOf(offset).intValue(), Long.valueOf(count).intValue());
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        if (offset > 2147483647L || count > 2147483647L) {
            throw new IllegalArgumentException("Count/Offset cannot exceed Integer.MAX_VALUE!");
        }
        try {
            return JedisConverters.toTupleSet(this.cluster.zrangeByScoreWithScores(key, min, max, Long.valueOf(offset).intValue(), Long.valueOf(count).intValue()));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRange(byte[] key, long begin, long end) {
        try {
            return this.cluster.zrevrange(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(byte[] key, long begin, long end) {
        try {
            return JedisConverters.toTupleSet(this.cluster.zrevrangeWithScores(key, begin, end));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        try {
            return this.cluster.zrevrangeByScore(key, max, min);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        try {
            return JedisConverters.toTupleSet(this.cluster.zrevrangeByScoreWithScores(key, max, min));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        if (offset > 2147483647L || count > 2147483647L) {
            throw new IllegalArgumentException("Count/Offset cannot exceed Integer.MAX_VALUE!");
        }
        try {
            return this.cluster.zrevrangeByScore(key, max, min, Long.valueOf(offset).intValue(), Long.valueOf(count).intValue());
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        if (offset > 2147483647L || count > 2147483647L) {
            throw new IllegalArgumentException("Count/Offset cannot exceed Integer.MAX_VALUE!");
        }
        try {
            return JedisConverters.toTupleSet(this.cluster.zrevrangeByScoreWithScores(key, max, min, Long.valueOf(offset).intValue(), Long.valueOf(count).intValue()));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, double min, double max) {
        try {
            return this.cluster.zcount(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCard(byte[] key) {
        try {
            return this.cluster.zcard(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zScore(byte[] key, byte[] value) {
        try {
            return this.cluster.zscore(key, value);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRange(byte[] key, long begin, long end) {
        try {
            return this.cluster.zremrangeByRank(key, begin, end);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        try {
            return this.cluster.zremrangeByScore(key, min, max);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, sets);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                return this.cluster.zunionstore(destKey, sets);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("ZUNIONSTORE can only be executed when all keys map to the same slot");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, sets);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            ZParams zparams = new ZParams().weights(weights).aggregate(ZParams.Aggregate.valueOf(aggregate.name()));
            try {
                return this.cluster.zunionstore(destKey, zparams, sets);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("ZUNIONSTORE can only be executed when all keys map to the same slot");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, sets);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                return this.cluster.zinterstore(destKey, sets);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("ZINTERSTORE can only be executed when all keys map to the same slot");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, sets);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            ZParams zparams = new ZParams().weights(weights).aggregate(ZParams.Aggregate.valueOf(aggregate.name()));
            try {
                return this.cluster.zinterstore(destKey, zparams, sets);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        throw new IllegalArgumentException("ZINTERSTORE can only be executed when all keys map to the same slot");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Cursor<RedisZSetCommands.Tuple> zScan(final byte[] key, ScanOptions options) {
        return new ScanCursor<RedisZSetCommands.Tuple>(options) { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.17
            @Override // org.springframework.data.redis.core.ScanCursor
            protected ScanIteration<RedisZSetCommands.Tuple> doScan(long cursorId, ScanOptions options2) {
                ScanParams params = JedisConverters.toScanParams(options2);
                ScanResult<Tuple> result = JedisClusterConnection.this.cluster.zscan(key, JedisConverters.toBytes(Long.valueOf(cursorId)), params);
                return new ScanIteration<>(Long.valueOf(result.getStringCursor()).longValue(), JedisConverters.tuplesToTuples().convert(result.getResult()));
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        try {
            return this.cluster.zrangeByScore(key, JedisConverters.toBytes(min), JedisConverters.toBytes(max));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        if (offset > 2147483647L || count > 2147483647L) {
            throw new IllegalArgumentException("Count/Offset cannot exceed Integer.MAX_VALUE!");
        }
        try {
            return this.cluster.zrangeByScore(key, JedisConverters.toBytes(min), JedisConverters.toBytes(max), Long.valueOf(offset).intValue(), Long.valueOf(count).intValue());
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        try {
            return JedisConverters.toBoolean(this.cluster.hset(key, field, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        try {
            return JedisConverters.toBoolean(this.cluster.hsetnx(key, field, value));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public byte[] hGet(byte[] key, byte[] field) {
        try {
            return this.cluster.hget(key, field);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        try {
            return this.cluster.hmget(key, fields);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
        try {
            this.cluster.hmset(key, hashes);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        try {
            return this.cluster.hincrBy(key, field, delta);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        try {
            return this.cluster.hincrByFloat(key, field, delta);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hExists(byte[] key, byte[] field) {
        try {
            return this.cluster.hexists(key, field);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hDel(byte[] key, byte[]... fields) {
        try {
            return this.cluster.hdel(key, fields);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hLen(byte[] key) {
        try {
            return this.cluster.hlen(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Set<byte[]> hKeys(byte[] key) {
        try {
            return this.cluster.hkeys(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hVals(byte[] key) {
        try {
            return new ArrayList(this.cluster.hvals(key));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        try {
            return this.cluster.hgetAll(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Cursor<Map.Entry<byte[], byte[]>> hScan(final byte[] key, ScanOptions options) {
        return new ScanCursor<Map.Entry<byte[], byte[]>>(options) { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.18
            @Override // org.springframework.data.redis.core.ScanCursor
            protected ScanIteration<Map.Entry<byte[], byte[]>> doScan(long cursorId, ScanOptions options2) {
                ScanParams params = JedisConverters.toScanParams(options2);
                ScanResult<Map.Entry<byte[], byte[]>> result = JedisClusterConnection.this.cluster.hscan(key, JedisConverters.toBytes(Long.valueOf(cursorId)), params);
                return new ScanIteration<>(Long.valueOf(result.getStringCursor()).longValue(), result.getResult());
            }
        }.open();
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void multi() {
        throw new InvalidDataAccessApiUsageException("MULTI is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public List<Object> exec() {
        throw new InvalidDataAccessApiUsageException("EXEC is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void discard() {
        throw new InvalidDataAccessApiUsageException("DISCARD is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void watch(byte[]... keys) {
        throw new InvalidDataAccessApiUsageException("WATCH is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void unwatch() {
        throw new InvalidDataAccessApiUsageException("UNWATCH is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public boolean isSubscribed() {
        return this.subscription != null && this.subscription.isAlive();
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Subscription getSubscription() {
        return this.subscription;
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Long publish(byte[] channel, byte[] message) {
        try {
            return this.cluster.publish(channel, message);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void subscribe(MessageListener listener, byte[]... channels) {
        if (isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        }
        try {
            BinaryJedisPubSub jedisPubSub = new JedisMessageListener(listener);
            this.subscription = new JedisSubscription(listener, jedisPubSub, channels, (byte[][]) null);
            this.cluster.subscribe(jedisPubSub, channels);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        if (isSubscribed()) {
            throw new RedisSubscribedConnectionException("Connection already subscribed; use the connection Subscription to cancel or add new channels");
        }
        try {
            BinaryJedisPubSub jedisPubSub = new JedisMessageListener(listener);
            this.subscription = new JedisSubscription(listener, jedisPubSub, (byte[][]) null, patterns);
            this.cluster.psubscribe(jedisPubSub, patterns);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(point, "Point must not be null!");
        Assert.notNull(member, "Member must not be null!");
        try {
            return this.cluster.geoadd(key, point.getX(), point.getY(), member);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, RedisGeoCommands.GeoLocation<byte[]> location) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(location, "Location must not be null!");
        return geoAdd(key, location.getPoint(), location.getName());
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(memberCoordinateMap, "MemberCoordinateMap must not be null!");
        Map<byte[], GeoCoordinate> redisGeoCoordinateMap = new HashMap<>();
        for (byte[] mapKey : memberCoordinateMap.keySet()) {
            redisGeoCoordinateMap.put(mapKey, JedisConverters.toGeoCoordinate(memberCoordinateMap.get(mapKey)));
        }
        try {
            return this.cluster.geoadd(key, redisGeoCoordinateMap);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Iterable<RedisGeoCommands.GeoLocation<byte[]>> locations) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(locations, "Locations must not be null!");
        Map<byte[], GeoCoordinate> redisGeoCoordinateMap = new HashMap<>();
        for (RedisGeoCommands.GeoLocation<byte[]> location : locations) {
            redisGeoCoordinateMap.put(location.getName(), JedisConverters.toGeoCoordinate(location.getPoint()));
        }
        try {
            return this.cluster.geoadd(key, redisGeoCoordinateMap);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member1, "Member1 must not be null!");
        Assert.notNull(member2, "Member2 must not be null!");
        try {
            return JedisConverters.distanceConverterForMetric(RedisGeoCommands.DistanceUnit.METERS).convert2(this.cluster.geodist(key, member1, member2));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member1, "Member1 must not be null!");
        Assert.notNull(member2, "Member2 must not be null!");
        Assert.notNull(metric, "Metric must not be null!");
        GeoUnit geoUnit = JedisConverters.toGeoUnit(metric);
        try {
            return JedisConverters.distanceConverterForMetric(metric).convert2(this.cluster.geodist(key, member1, member2, geoUnit));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<String> geoHash(byte[] key, byte[]... members) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(members, "Members must not be null!");
        Assert.noNullElements(members, "Members must not contain null!");
        try {
            return JedisConverters.toStrings(this.cluster.geohash(key, members));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<Point> geoPos(byte[] key, byte[]... members) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(members, "Members must not be null!");
        Assert.noNullElements(members, "Members must not contain null!");
        try {
            return JedisConverters.geoCoordinateToPointConverter().convert(this.cluster.geopos(key, members));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(within, "Within must not be null!");
        try {
            return JedisConverters.geoRadiusResponseToGeoResultsConverter(within.getRadius().getMetric()).convert2(this.cluster.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric())));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(within, "Within must not be null!");
        Assert.notNull(args, "Args must not be null!");
        GeoRadiusParam geoRadiusParam = JedisConverters.toGeoRadiusParam(args);
        try {
            return JedisConverters.geoRadiusResponseToGeoResultsConverter(within.getRadius().getMetric()).convert2(this.cluster.georadius(key, within.getCenter().getX(), within.getCenter().getY(), within.getRadius().getValue(), JedisConverters.toGeoUnit(within.getRadius().getMetric()), geoRadiusParam));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        return geoRadiusByMember(key, member, new Distance(radius, RedisGeoCommands.DistanceUnit.METERS));
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member, "Member must not be null!");
        Assert.notNull(radius, "Radius must not be null!");
        GeoUnit geoUnit = JedisConverters.toGeoUnit(radius.getMetric());
        try {
            return JedisConverters.geoRadiusResponseToGeoResultsConverter(radius.getMetric()).convert2(this.cluster.georadiusByMember(key, member, radius.getValue(), geoUnit));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, RedisGeoCommands.GeoRadiusCommandArgs args) {
        Assert.notNull(key, "Key must not be null!");
        Assert.notNull(member, "Member must not be null!");
        Assert.notNull(radius, "Radius must not be null!");
        Assert.notNull(args, "Args must not be null!");
        GeoUnit geoUnit = JedisConverters.toGeoUnit(radius.getMetric());
        GeoRadiusParam geoRadiusParam = JedisConverters.toGeoRadiusParam(args);
        try {
            return JedisConverters.geoRadiusResponseToGeoResultsConverter(radius.getMetric()).convert2(this.cluster.georadiusByMember(key, member, radius.getValue(), geoUnit, geoRadiusParam));
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoRemove(byte[] key, byte[]... members) {
        return zRem(key, members);
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public void select(int dbIndex) {
        if (dbIndex != 0) {
            throw new InvalidDataAccessApiUsageException("Cannot SELECT non zero index in cluster mode.");
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public byte[] echo(byte[] message) {
        try {
            return this.cluster.echo(message);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public String ping() {
        if (this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.19
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.ping();
            }
        }).resultsAsList().isEmpty()) {
            return null;
        }
        return LettuceConnectionFactory.PING_REPLY;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public String ping(RedisClusterNode node) {
        return (String) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.20
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.ping();
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgWriteAof() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.21
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.bgrewriteaof();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void bgReWriteAof(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.22
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.bgrewriteaof();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgReWriteAof() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.23
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.bgrewriteaof();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgSave() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.24
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.bgsave();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void bgSave(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.25
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.bgsave();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long lastSave() {
        List<Long> result = new ArrayList<>(this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.26
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(Jedis client) {
                return client.lastsave();
            }
        }).resultsAsList());
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        Collections.sort(result, Collections.reverseOrder());
        return result.get(0);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Long lastSave(RedisClusterNode node) {
        return (Long) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.27
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(Jedis client) {
                return client.lastsave();
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void save() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.28
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.save();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void save(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.29
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.save();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long dbSize() {
        Collection<Long> dbSizes = this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.30
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(Jedis client) {
                return client.dbSize();
            }
        }).resultsAsList();
        if (CollectionUtils.isEmpty(dbSizes)) {
            return 0L;
        }
        Long size = 0L;
        for (Long value : dbSizes) {
            size = Long.valueOf(size.longValue() + value.longValue());
        }
        return size;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Long dbSize(RedisClusterNode node) {
        return (Long) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.31
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(Jedis client) {
                return client.dbSize();
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushDb() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.32
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.flushDB();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void flushDb(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.33
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.flushDB();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushAll() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.34
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.flushAll();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void flushAll(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.35
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.flushAll();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info() {
        Properties infos = new Properties();
        List<ClusterCommandExecutor.NodeResult<Properties>> nodeResults = this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<Properties>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.36
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Properties doInCluster(Jedis client) {
                return JedisConverters.toProperties(client.info());
            }
        }).getResults();
        for (ClusterCommandExecutor.NodeResult<Properties> nodePorperties : nodeResults) {
            for (Map.Entry<Object, Object> entry : nodePorperties.getValue().entrySet()) {
                infos.put(nodePorperties.getNode().asString() + "." + entry.getKey(), entry.getValue());
            }
        }
        return infos;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Properties info(RedisClusterNode node) {
        return JedisConverters.toProperties((String) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.37
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.info();
            }
        }, node).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info(final String section) {
        Properties infos = new Properties();
        List<ClusterCommandExecutor.NodeResult<Properties>> nodeResults = this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<Properties>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.38
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Properties doInCluster(Jedis client) {
                return JedisConverters.toProperties(client.info(section));
            }
        }).getResults();
        for (ClusterCommandExecutor.NodeResult<Properties> nodePorperties : nodeResults) {
            for (Map.Entry<Object, Object> entry : nodePorperties.getValue().entrySet()) {
                infos.put(nodePorperties.getNode().asString() + "." + entry.getKey(), entry.getValue());
            }
        }
        return infos;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Properties info(RedisClusterNode node, final String section) {
        return JedisConverters.toProperties((String) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.39
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.info(section);
            }
        }, node).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.40
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.shutdown();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void shutdown(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.41
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.shutdown();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown(RedisServerCommands.ShutdownOption option) {
        if (option == null) {
            shutdown();
            return;
        }
        throw new IllegalArgumentException("Shutdown with options is not supported for jedis.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<String> getConfig(final String pattern) {
        List<ClusterCommandExecutor.NodeResult<List<String>>> mapResult = this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<List<String>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.42
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<String> doInCluster(Jedis client) {
                return client.configGet(pattern);
            }
        }).getResults();
        List<String> result = new ArrayList<>();
        for (ClusterCommandExecutor.NodeResult<List<String>> entry : mapResult) {
            String prefix = entry.getNode().asString();
            int i = 0;
            for (String value : entry.getValue()) {
                int i2 = i;
                i++;
                result.add((i2 % 2 == 0 ? prefix + "." : "") + value);
            }
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public List<String> getConfig(RedisClusterNode node, final String pattern) {
        return (List) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<List<String>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.43
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<String> doInCluster(Jedis client) {
                return client.configGet(pattern);
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setConfig(final String param, final String value) {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.44
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.configSet(param, value);
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void setConfig(RedisClusterNode node, final String param, final String value) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.45
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.configSet(param, value);
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void resetConfigStats() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.46
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.configResetStat();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void resetConfigStats(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.47
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.configResetStat();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long time() {
        return convertListOfStringToTime((List) this.clusterCommandExecutor.executeCommandOnArbitraryNode(new JedisClusterCommandCallback<List<String>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.48
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<String> doInCluster(Jedis client) {
                return client.time();
            }
        }).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Long time(RedisClusterNode node) {
        return convertListOfStringToTime((List) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<List<String>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.49
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<String> doInCluster(Jedis client) {
                return client.time();
            }
        }, node).getValue());
    }

    private Long convertListOfStringToTime(List<String> serverTimeInformation) {
        Assert.notEmpty(serverTimeInformation, "Received invalid result from server. Expected 2 items in collection.");
        Assert.isTrue(serverTimeInformation.size() == 2, "Received invalid number of arguments from redis server. Expected 2 received " + serverTimeInformation.size());
        return Converters.toTimeMillis(serverTimeInformation.get(0), serverTimeInformation.get(1));
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void killClient(String host, int port) {
        final String hostAndPort = String.format("%s:%s", host, Integer.valueOf(port));
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.50
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clientKill(hostAndPort);
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setClientName(byte[] name) {
        throw new InvalidDataAccessApiUsageException("CLIENT SETNAME is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public String getClientName() {
        throw new InvalidDataAccessApiUsageException("CLIENT GETNAME is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<RedisClientInfo> getClientList() {
        Collection<String> map = this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.51
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clientList();
            }
        }).resultsAsList();
        ArrayList<RedisClientInfo> result = new ArrayList<>();
        for (String infos : map) {
            result.addAll(JedisConverters.toListOfRedisClientInformation(infos));
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public List<RedisClientInfo> getClientList(RedisClusterNode node) {
        return JedisConverters.toListOfRedisClientInformation((String) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.52
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clientList();
            }
        }, node).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOf(String host, int port) {
        throw new InvalidDataAccessApiUsageException("SlaveOf is not supported in cluster environment. Please use CLUSTER REPLICATE.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOfNoOne() {
        throw new InvalidDataAccessApiUsageException("SlaveOf is not supported in cluster environment. Please use CLUSTER REPLICATE.");
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptFlush() {
        throw new InvalidDataAccessApiUsageException("ScriptFlush is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptKill() {
        throw new InvalidDataAccessApiUsageException("ScriptKill is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public String scriptLoad(byte[] script) {
        throw new InvalidDataAccessApiUsageException("ScriptLoad is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public List<Boolean> scriptExists(String... scriptShas) {
        throw new InvalidDataAccessApiUsageException("ScriptExists is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw new InvalidDataAccessApiUsageException("Eval is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw new InvalidDataAccessApiUsageException("EvalSha is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(byte[] scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw new InvalidDataAccessApiUsageException("EvalSha is not supported in cluster environment.");
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfAdd(byte[] key, byte[]... values) {
        try {
            return this.cluster.pfadd(key, values);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfCount(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            try {
                return this.cluster.pfcount(keys);
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("All keys must map to same slot for pfcount in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destinationKey, sourceKeys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                this.cluster.pfmerge(destinationKey, sourceKeys);
                return;
            } catch (Exception ex) {
                throw convertJedisAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("All keys must map to same slot for pfmerge in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean exists(byte[] key) {
        try {
            return this.cluster.exists(key);
        } catch (Exception ex) {
            throw convertJedisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterSetSlot(RedisClusterNode node, final int slot, final RedisClusterCommands.AddSlots mode) {
        Assert.notNull(node, "Node must not be null.");
        Assert.notNull(mode, "AddSlots mode must not be null.");
        RedisClusterNode nodeToUse = this.topologyProvider.getTopology().lookup(node);
        final String nodeId = nodeToUse.getId();
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.53
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                switch (AnonymousClass66.$SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[mode.ordinal()]) {
                    case 1:
                        return client.clusterSetSlotImporting(slot, nodeId);
                    case 2:
                        return client.clusterSetSlotMigrating(slot, nodeId);
                    case 3:
                        return client.clusterSetSlotStable(slot);
                    case 4:
                        return client.clusterSetSlotNode(slot, nodeId);
                    default:
                        throw new IllegalArgumentException(String.format("Unknown AddSlots mode '%s'.", mode));
                }
            }
        }, node);
    }

    /* renamed from: org.springframework.data.redis.connection.jedis.JedisClusterConnection$66, reason: invalid class name */
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisClusterConnection$66.class */
    static /* synthetic */ class AnonymousClass66 {
        static final /* synthetic */ int[] $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots = new int[RedisClusterCommands.AddSlots.values().length];

        static {
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.IMPORTING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.MIGRATING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.STABLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.NODE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$org$springframework$data$redis$connection$DataType = new int[DataType.values().length];
            try {
                $SwitchMap$org$springframework$data$redis$connection$DataType[DataType.SET.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$DataType[DataType.LIST.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public List<byte[]> clusterGetKeysInSlot(final int slot, final Integer count) {
        RedisClusterNode node = clusterGetNodeForSlot(slot);
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<List<byte[]>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.54
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<byte[]> doInCluster(Jedis client) {
                return JedisConverters.stringListToByteList().convert(client.clusterGetKeysInSlot(slot, count != null ? count.intValue() : Integer.MAX_VALUE));
            }
        }, node);
        return null;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterAddSlots(RedisClusterNode node, final int... slots) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.55
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clusterAddSlots(slots);
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterAddSlots(RedisClusterNode node, RedisClusterNode.SlotRange range) {
        Assert.notNull(range, "Range must not be null.");
        clusterAddSlots(node, range.getSlotsArray());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Long clusterCountKeysInSlot(final int slot) {
        RedisClusterNode node = clusterGetNodeForSlot(slot);
        return (Long) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.56
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(Jedis client) {
                return client.clusterCountKeysInSlot(slot);
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterDeleteSlots(RedisClusterNode node, final int... slots) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.57
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clusterDelSlots(slots);
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterDeleteSlotsInRange(RedisClusterNode node, RedisClusterNode.SlotRange range) {
        Assert.notNull(range, "Range must not be null.");
        clusterDeleteSlots(node, range.getSlotsArray());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterForget(final RedisClusterNode node) {
        Set<RedisClusterNode> nodes = new LinkedHashSet<>(this.topologyProvider.getTopology().getActiveMasterNodes());
        RedisClusterNode nodeToRemove = this.topologyProvider.getTopology().lookup(node);
        nodes.remove(nodeToRemove);
        this.clusterCommandExecutor.executeCommandAsyncOnNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.58
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clusterForget(node.getId());
            }
        }, nodes);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterMeet(final RedisClusterNode node) {
        Assert.notNull(node, "Cluster node must not be null for CLUSTER MEET command!");
        Assert.hasText(node.getHost(), "Node to meet cluster must have a host!");
        Assert.isTrue(node.getPort().intValue() > 0, "Node to meet cluster must have a port greater 0!");
        this.clusterCommandExecutor.executeCommandOnAllNodes(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.59
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clusterMeet(node.getHost(), node.getPort().intValue());
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterReplicate(RedisClusterNode master, RedisClusterNode slave) {
        final RedisClusterNode masterNode = this.topologyProvider.getTopology().lookup(master);
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.60
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clusterReplicate(masterNode.getId());
            }
        }, slave);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Integer clusterGetSlotForKey(final byte[] key) {
        return (Integer) this.clusterCommandExecutor.executeCommandOnArbitraryNode(new JedisClusterCommandCallback<Integer>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.61
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Integer doInCluster(Jedis client) {
                return Integer.valueOf(client.clusterKeySlot(JedisConverters.toString(key)).intValue());
            }
        }).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public RedisClusterNode clusterGetNodeForSlot(int slot) {
        for (RedisClusterNode node : this.topologyProvider.getTopology().getSlotServingNodes(slot)) {
            if (node.isMaster()) {
                return node;
            }
        }
        return null;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Set<RedisClusterNode> clusterGetNodes() {
        return this.topologyProvider.getTopology().getNodes();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Set<RedisClusterNode> clusterGetSlaves(RedisClusterNode master) {
        Assert.notNull(master, "Master cannot be null!");
        final RedisClusterNode nodeToUse = this.topologyProvider.getTopology().lookup(master);
        return JedisConverters.toSetOfRedisClusterNodes((Collection<String>) this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<List<String>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.62
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<String> doInCluster(Jedis client) {
                return client.clusterSlaves(nodeToUse.getId());
            }
        }, master).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Map<RedisClusterNode, Collection<RedisClusterNode>> clusterGetMasterSlaveMap() {
        List<ClusterCommandExecutor.NodeResult<Collection<RedisClusterNode>>> nodeResults = this.clusterCommandExecutor.executeCommandAsyncOnNodes(new JedisClusterCommandCallback<Collection<RedisClusterNode>>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.63
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Set<RedisClusterNode> doInCluster(Jedis client) {
                return JedisConverters.toSetOfRedisClusterNodes(client.clusterSlaves((String) client.eval("return redis.call('cluster', 'myid')", 0, new String[0])));
            }
        }, this.topologyProvider.getTopology().getActiveMasterNodes()).getResults();
        Map<RedisClusterNode, Collection<RedisClusterNode>> result = new LinkedHashMap<>();
        for (ClusterCommandExecutor.NodeResult<Collection<RedisClusterNode>> nodeResult : nodeResults) {
            result.put(nodeResult.getNode(), nodeResult.getValue());
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public RedisClusterNode clusterGetNodeForKey(byte[] key) {
        return clusterGetNodeForSlot(clusterGetSlotForKey(key).intValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public ClusterInfo clusterGetClusterInfo() {
        return new ClusterInfo(JedisConverters.toProperties((String) this.clusterCommandExecutor.executeCommandOnArbitraryNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.64
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.clusterInfo();
            }
        }).getValue()));
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option) {
        migrate(key, target, dbIndex, option, Long.MAX_VALUE);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(final byte[] key, final RedisNode target, final int dbIndex, RedisServerCommands.MigrateOption option, long timeout) {
        final int timeoutToUse = timeout <= 2147483647L ? (int) timeout : Integer.MAX_VALUE;
        RedisClusterNode node = this.topologyProvider.getTopology().lookup(target.getHost(), target.getPort().intValue());
        this.clusterCommandExecutor.executeCommandOnSingleNode(new JedisClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.jedis.JedisClusterConnection.65
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(Jedis client) {
                return client.migrate(JedisConverters.toBytes(target.getHost()), target.getPort().intValue(), key, dbIndex, timeoutToUse);
            }
        }, node);
    }

    protected DataAccessException convertJedisAccessException(Exception ex) {
        DataAccessException translated = EXCEPTION_TRANSLATION.translate(ex);
        return translated != null ? translated : new RedisSystemException(ex.getMessage(), ex);
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        if (!this.closed && this.disposeClusterCommandExecutorOnClose) {
            try {
                this.clusterCommandExecutor.destroy();
            } catch (Exception ex) {
                this.log.warn("Cannot properly close cluster command executor", ex);
            }
        }
        this.closed = true;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isClosed() {
        return this.closed;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public JedisCluster getNativeConnection() {
        return this.cluster;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isQueueing() {
        return false;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isPipelined() {
        return false;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void openPipeline() {
        throw new UnsupportedOperationException("Pipeline is currently not supported for JedisClusterConnection.");
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public List<Object> closePipeline() throws RedisPipelineException {
        throw new UnsupportedOperationException("Pipeline is currently not supported for JedisClusterConnection.");
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public RedisSentinelConnection getSentinelConnection() {
        throw new UnsupportedOperationException("Sentinel is currently not supported for JedisClusterConnection.");
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisClusterConnection$JedisClusterNodeResourceProvider.class */
    static class JedisClusterNodeResourceProvider implements ClusterNodeResourceProvider {
        private final JedisCluster cluster;
        private final ClusterTopologyProvider topologyProvider;
        private final JedisClusterConnectionHandler connectionHandler;

        JedisClusterNodeResourceProvider(JedisCluster cluster, ClusterTopologyProvider topologyProvider) {
            this.cluster = cluster;
            this.topologyProvider = topologyProvider;
            if (cluster != null) {
                PropertyAccessor accessor = new DirectFieldAccessFallbackBeanWrapper(cluster);
                this.connectionHandler = accessor.isReadableProperty("connectionHandler") ? (JedisClusterConnectionHandler) accessor.getPropertyValue("connectionHandler") : null;
            } else {
                this.connectionHandler = null;
            }
        }

        @Override // org.springframework.data.redis.connection.ClusterNodeResourceProvider
        public Jedis getResourceForSpecificNode(RedisClusterNode node) {
            Assert.notNull(node, "Cannot get Pool for 'null' node!");
            JedisPool pool = getResourcePoolForSpecificNode(node);
            if (pool != null) {
                return pool.getResource();
            }
            Jedis connection = getConnectionForSpecificNode(node);
            if (connection != null) {
                return connection;
            }
            throw new IllegalStateException(String.format("Node %s is unknown to cluster", node));
        }

        private JedisPool getResourcePoolForSpecificNode(RedisClusterNode node) {
            Map<String, JedisPool> clusterNodes = this.cluster.getClusterNodes();
            if (clusterNodes.containsKey(node.asString())) {
                return clusterNodes.get(node.asString());
            }
            return null;
        }

        private Jedis getConnectionForSpecificNode(RedisClusterNode node) {
            RedisClusterNode member = this.topologyProvider.getTopology().lookup(node);
            if (member != null && this.connectionHandler != null) {
                return this.connectionHandler.getConnectionFromNode(new HostAndPort(member.getHost(), member.getPort().intValue()));
            }
            return null;
        }

        @Override // org.springframework.data.redis.connection.ClusterNodeResourceProvider
        public void returnResourceForSpecificNode(RedisClusterNode node, Object client) {
            ((Jedis) client).close();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisClusterConnection$JedisClusterTopologyProvider.class */
    static class JedisClusterTopologyProvider implements ClusterTopologyProvider {
        private final JedisCluster cluster;
        private ClusterTopology cached;
        private final Object lock = new Object();
        private long time = 0;

        public JedisClusterTopologyProvider(JedisCluster cluster) {
            this.cluster = cluster;
        }

        @Override // org.springframework.data.redis.connection.ClusterTopologyProvider
        public ClusterTopology getTopology() {
            if (this.cached != null && this.time + 100 > System.currentTimeMillis()) {
                return this.cached;
            }
            Map<String, Exception> errors = new LinkedHashMap<>();
            for (Map.Entry<String, JedisPool> entry : this.cluster.getClusterNodes().entrySet()) {
                Jedis jedis = null;
                try {
                    Jedis jedis2 = entry.getValue().getResource();
                    this.time = System.currentTimeMillis();
                    Set<RedisClusterNode> nodes = Converters.toSetOfRedisClusterNodes(jedis2.clusterNodes());
                    synchronized (this.lock) {
                        this.cached = new ClusterTopology(nodes);
                    }
                    ClusterTopology clusterTopology = this.cached;
                    if (jedis2 != null) {
                        jedis2.close();
                    }
                    return clusterTopology;
                } catch (Exception ex) {
                    try {
                        errors.put(entry.getKey(), ex);
                        if (0 != 0) {
                            jedis.close();
                        }
                    } catch (Throwable th) {
                        if (0 != 0) {
                            jedis.close();
                        }
                        throw th;
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Exception> entry2 : errors.entrySet()) {
                sb.append(String.format("\r\n\t- %s failed: %s", entry2.getKey(), entry2.getValue().getMessage()));
            }
            throw new ClusterStateFailureException("Could not retrieve cluster information. CLUSTER NODES returned with error." + sb.toString());
        }
    }
}
