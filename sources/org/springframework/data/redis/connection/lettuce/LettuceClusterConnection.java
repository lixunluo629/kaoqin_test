package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.KeyValue;
import com.lambdaworks.redis.RedisException;
import com.lambdaworks.redis.api.StatefulConnection;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.SlotHash;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands;
import com.lambdaworks.redis.cluster.models.partitions.Partitions;
import com.lambdaworks.redis.codec.ByteArrayCodec;
import com.lambdaworks.redis.codec.RedisCodec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.PassThroughExceptionTranslationStrategy;
import org.springframework.data.redis.connection.ClusterCommandExecutor;
import org.springframework.data.redis.connection.ClusterInfo;
import org.springframework.data.redis.connection.ClusterNodeResourceProvider;
import org.springframework.data.redis.connection.ClusterSlotHashUtil;
import org.springframework.data.redis.connection.ClusterTopology;
import org.springframework.data.redis.connection.ClusterTopologyProvider;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisClusterCommands;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.util.ByteArraySet;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.util.ByteUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceClusterConnection.class */
public class LettuceClusterConnection extends LettuceConnection implements RedisClusterConnection {
    static final ExceptionTranslationStrategy exceptionConverter = new PassThroughExceptionTranslationStrategy(new LettuceExceptionConverter());
    static final RedisCodec<byte[], byte[]> CODEC = ByteArrayCodec.INSTANCE;
    private final Log log;
    private final RedisClusterClient clusterClient;
    private ClusterCommandExecutor clusterCommandExecutor;
    private ClusterTopologyProvider topologyProvider;
    private boolean disposeClusterCommandExecutorOnClose;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceClusterConnection$LettuceClusterCommandCallback.class */
    protected interface LettuceClusterCommandCallback<T> extends ClusterCommandExecutor.ClusterCommandCallback<RedisClusterCommands<byte[], byte[]>, T> {
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceClusterConnection$LettuceMultiKeyClusterCommandCallback.class */
    protected interface LettuceMultiKeyClusterCommandCallback<T> extends ClusterCommandExecutor.MultiKeyClusterCommandCallback<RedisClusterCommands<byte[], byte[]>, T> {
    }

    public LettuceClusterConnection(RedisClusterClient clusterClient) {
        this(clusterClient, 60L, new ClusterCommandExecutor(new LettuceClusterTopologyProvider(clusterClient), new LettuceClusterNodeResourceProvider(clusterClient), exceptionConverter));
        this.disposeClusterCommandExecutorOnClose = true;
    }

    public LettuceClusterConnection(RedisClusterClient clusterClient, ClusterCommandExecutor executor) {
        this(clusterClient, 60L, executor);
    }

    public LettuceClusterConnection(RedisClusterClient clusterClient, long timeout, ClusterCommandExecutor executor) {
        super(null, timeout, clusterClient, null, 0);
        this.log = LogFactory.getLog(getClass());
        Assert.notNull(clusterClient, "RedisClusterClient must not be null.");
        Assert.notNull(executor, "ClusterCommandExecutor must not be null.");
        this.clusterClient = clusterClient;
        this.topologyProvider = new LettuceClusterTopologyProvider(clusterClient);
        this.clusterCommandExecutor = executor;
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection
    public Cursor<byte[]> scan(long cursorId, ScanOptions options) {
        throw new InvalidDataAccessApiUsageException("Scan is not supported across multiple nodes within a cluster.");
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisKeyCommands
    public Set<byte[]> keys(final byte[] pattern) {
        Assert.notNull(pattern, "Pattern must not be null!");
        Collection<List<byte[]>> keysPerNode = this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<List<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.1
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<byte[]> doInCluster(RedisClusterCommands<byte[], byte[]> connection) {
                return connection.keys(pattern);
            }
        }).resultsAsList();
        Set<byte[]> keys = new HashSet<>();
        for (List<byte[]> keySet : keysPerNode) {
            keys.addAll(keySet);
        }
        return keys;
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public void flushAll() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.2
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return client.flushall();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public void flushDb() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.3
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return client.flushdb();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public Long dbSize() {
        Collection<Long> dbSizes = this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.4
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return client.dbsize();
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

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public Properties info() {
        Properties infos = new Properties();
        List<ClusterCommandExecutor.NodeResult<Properties>> nodeResults = this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<Properties>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.5
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Properties doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return LettuceConverters.toProperties(client.info());
            }
        }).getResults();
        for (ClusterCommandExecutor.NodeResult<Properties> nodePorperties : nodeResults) {
            for (Map.Entry<Object, Object> entry : nodePorperties.getValue().entrySet()) {
                infos.put(nodePorperties.getNode().asString() + "." + entry.getKey(), entry.getValue());
            }
        }
        return infos;
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public Properties info(final String section) {
        Properties infos = new Properties();
        List<ClusterCommandExecutor.NodeResult<Properties>> nodeResults = this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<Properties>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.6
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Properties doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return LettuceConverters.toProperties(client.info(section));
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
        return LettuceConverters.toProperties((String) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.7
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return client.info(section);
            }
        }, node).getValue());
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean move(byte[] key, int dbIndex) {
        throw new UnsupportedOperationException("MOVE not supported in CLUSTER mode!");
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisKeyCommands
    public Long del(byte[]... keys) {
        Assert.noNullElements(keys, "Keys must not be null or contain null key!");
        return super.del(keys);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Set<RedisClusterNode> clusterGetSlaves(RedisClusterNode master) {
        Assert.notNull(master, "Master must not be null!");
        final RedisClusterNode nodeToUse = this.topologyProvider.getTopology().lookup(master);
        return (Set) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<Set<RedisClusterNode>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.8
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Set<RedisClusterNode> doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return LettuceConverters.toSetOfRedisClusterNodes(client.clusterSlaves(nodeToUse.getId()));
            }
        }, master).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Integer clusterGetSlotForKey(byte[] key) {
        return Integer.valueOf(SlotHash.getSlot(key));
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public RedisClusterNode clusterGetNodeForSlot(int slot) {
        DirectFieldAccessor accessor = new DirectFieldAccessor(this.clusterClient);
        return LettuceConverters.toRedisClusterNode(((Partitions) accessor.getPropertyValue("partitions")).getPartitionBySlot(slot));
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public RedisClusterNode clusterGetNodeForKey(byte[] key) {
        return clusterGetNodeForSlot(clusterGetSlotForKey(key).intValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public ClusterInfo clusterGetClusterInfo() {
        return (ClusterInfo) this.clusterCommandExecutor.executeCommandOnArbitraryNode(new LettuceClusterCommandCallback<ClusterInfo>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.9
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public ClusterInfo doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return new ClusterInfo(LettuceConverters.toProperties(client.clusterInfo()));
            }
        }).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterAddSlots(RedisClusterNode node, final int... slots) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.10
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(RedisClusterCommands<byte[], byte[]> client) {
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
    public void clusterDeleteSlots(RedisClusterNode node, final int... slots) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.11
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(RedisClusterCommands<byte[], byte[]> client) {
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
    public void clusterForget(RedisClusterNode node) {
        List<RedisClusterNode> nodes = new ArrayList<>(clusterGetNodes());
        final RedisClusterNode nodeToRemove = this.topologyProvider.getTopology().lookup(node);
        nodes.remove(nodeToRemove);
        this.clusterCommandExecutor.executeCommandAsyncOnNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.12
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return client.clusterForget(nodeToRemove.getId());
            }
        }, nodes);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterMeet(final RedisClusterNode node) {
        Assert.notNull(node, "Cluster node must not be null for CLUSTER MEET command!");
        Assert.hasText(node.getHost(), "Node to meet cluster must have a host!");
        Assert.isTrue(node.getPort().intValue() > 0, "Node to meet cluster must have a port greater 0!");
        this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.13
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(RedisClusterCommands<byte[], byte[]> client) {
                return client.clusterMeet(node.getHost(), node.getPort().intValue());
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterSetSlot(RedisClusterNode node, final int slot, final RedisClusterCommands.AddSlots mode) {
        Assert.notNull(node, "Node must not be null.");
        Assert.notNull(mode, "AddSlots mode must not be null.");
        RedisClusterNode nodeToUse = this.topologyProvider.getTopology().lookup(node);
        final String nodeId = nodeToUse.getId();
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.14
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                switch (AnonymousClass45.$SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[mode.ordinal()]) {
                    case 1:
                        return client.clusterSetSlotMigrating(slot, nodeId);
                    case 2:
                        return client.clusterSetSlotImporting(slot, nodeId);
                    case 3:
                        return client.clusterSetSlotNode(slot, nodeId);
                    case 4:
                        return client.clusterSetSlotStable(slot);
                    default:
                        throw new InvalidDataAccessApiUsageException("Invalid import mode for cluster slot: " + slot);
                }
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public List<byte[]> clusterGetKeysInSlot(int slot, Integer count) {
        try {
            return getConnection().clusterGetKeysInSlot(slot, count.intValue());
        } catch (Exception ex) {
            throw exceptionConverter.translate(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Long clusterCountKeysInSlot(int slot) {
        try {
            return getConnection().clusterCountKeysInSlot(slot);
        } catch (Exception ex) {
            throw exceptionConverter.translate(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public void clusterReplicate(RedisClusterNode master, RedisClusterNode slave) {
        final RedisClusterNode masterNode = this.topologyProvider.getTopology().lookup(master);
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.15
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.clusterReplicate(masterNode.getId());
            }
        }, slave);
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisConnectionCommands
    public String ping() {
        Collection<String> ping = this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.16
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> connection) {
                return connection.ping();
            }
        }).resultsAsList();
        for (String result : ping) {
            if (!ObjectUtils.nullSafeEquals(LettuceConnectionFactory.PING_REPLY, result)) {
                return "";
            }
        }
        return LettuceConnectionFactory.PING_REPLY;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public String ping(RedisClusterNode node) {
        return (String) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.17
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.ping();
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void bgReWriteAof(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.18
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.bgrewriteaof();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void bgSave(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.19
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.bgsave();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Long lastSave(RedisClusterNode node) {
        return (Long) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.20
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return Long.valueOf(client.lastsave().getTime());
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void save(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.21
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.save();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Long dbSize(RedisClusterNode node) {
        return (Long) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<Long>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.22
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Long doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.dbsize();
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void flushDb(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.23
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.flushdb();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void flushAll(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.24
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.flushall();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Properties info(RedisClusterNode node) {
        return LettuceConverters.toProperties((String) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.25
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.info();
            }
        }, node).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Set<byte[]> keys(RedisClusterNode node, final byte[] pattern) {
        return LettuceConverters.toBytesSet((List) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<List<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.26
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.keys(pattern);
            }
        }, node).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public byte[] randomKey(RedisClusterNode node) {
        return (byte[]) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<byte[]>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.27
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public byte[] doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return (byte[]) client.randomkey();
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] randomKey() {
        RedisClusterNode node;
        List<RedisClusterNode> nodes = clusterGetNodes();
        Set<RedisClusterNode> inspectedNodes = new HashSet<>(nodes.size());
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v5, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisKeyCommands
    public void rename(byte[] oldName, byte[] newName) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{oldName, newName})) {
            super.rename(oldName, newName);
            return;
        }
        byte[] value = dump(oldName);
        if (value != null && value.length > 0) {
            restore(newName, 0L, value);
            del(new byte[]{oldName});
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v6, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{oldName, newName})) {
            return super.renameNX(oldName, newName);
        }
        byte[] value = dump(oldName);
        if (value != null && value.length > 0 && !exists(newName).booleanValue()) {
            restore(newName, 0L, value);
            del(new byte[]{oldName});
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void shutdown(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<Void>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.28
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Void doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                client.shutdown(true);
                return null;
            }
        }, node);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisKeyCommands
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{key, storeKey})) {
            return super.sort(key, params, storeKey);
        }
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

    /* renamed from: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection$45, reason: invalid class name */
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceClusterConnection$45.class */
    static /* synthetic */ class AnonymousClass45 {
        static final /* synthetic */ int[] $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots;

        static {
            try {
                $SwitchMap$org$springframework$data$redis$connection$DataType[DataType.SET.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$DataType[DataType.LIST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots = new int[RedisClusterCommands.AddSlots.values().length];
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.MIGRATING.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.IMPORTING.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.NODE.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisClusterCommands$AddSlots[RedisClusterCommands.AddSlots.STABLE.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisStringCommands
    public List<byte[]> mGet(byte[]... keys) {
        Assert.notNull(keys, "Keys must not be null!");
        return super.mGet(keys);
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisStringCommands
    public void mSet(Map<byte[], byte[]> tuples) {
        Assert.notNull(tuples, "Tuples must not be null!");
        super.mSet(tuples);
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisStringCommands
    public Boolean mSetNX(Map<byte[], byte[]> tuples) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys((byte[][]) tuples.keySet().toArray((Object[]) new byte[tuples.keySet().size()]))) {
            return super.mSetNX(tuples);
        }
        boolean result = true;
        for (Map.Entry<byte[], byte[]> entry : tuples.entrySet()) {
            if (!setNX(entry.getKey(), entry.getValue()).booleanValue() && result) {
                result = false;
            }
        }
        return Boolean.valueOf(result);
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bLPop(final int timeout, byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            return super.bLPop(timeout, keys);
        }
        List<KeyValue<byte[], byte[]>> resultList = this.clusterCommandExecutor.executeMuliKeyCommand(new LettuceMultiKeyClusterCommandCallback<KeyValue<byte[], byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.29
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], java.lang.Object[]] */
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public KeyValue<byte[], byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client, byte[] key) {
                return client.blpop(timeout, (Object[]) new byte[]{key});
            }
        }, Arrays.asList(keys)).resultsAsList();
        for (KeyValue<byte[], byte[]> kv : resultList) {
            if (kv != null) {
                return LettuceConverters.toBytesList(kv);
            }
        }
        return Collections.emptyList();
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bRPop(final int timeout, byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            return super.bRPop(timeout, keys);
        }
        List<KeyValue<byte[], byte[]>> resultList = this.clusterCommandExecutor.executeMuliKeyCommand(new LettuceMultiKeyClusterCommandCallback<KeyValue<byte[], byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.30
            /* JADX WARN: Type inference failed for: r2v1, types: [byte[], java.lang.Object[]] */
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public KeyValue<byte[], byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client, byte[] key) {
                return client.brpop(timeout, (Object[]) new byte[]{key});
            }
        }, Arrays.asList(keys)).resultsAsList();
        for (KeyValue<byte[], byte[]> kv : resultList) {
            if (kv != null) {
                return LettuceConverters.toBytesList(kv);
            }
        }
        return Collections.emptyList();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v3, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{srcKey, dstKey})) {
            return super.rPopLPush(srcKey, dstKey);
        }
        byte[] val = rPop(srcKey);
        lPush(dstKey, new byte[]{val});
        return val;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v3, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v5, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisListCommands
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{srcKey, dstKey})) {
            return super.bRPopLPush(timeout, srcKey, dstKey);
        }
        List<byte[]> val = bRPop(timeout, new byte[]{srcKey});
        if (!CollectionUtils.isEmpty(val)) {
            lPush(dstKey, new byte[]{val.get(1)});
            return val.get(1);
        }
        return null;
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisConnectionCommands
    public void select(int dbIndex) {
        if (dbIndex != 0) {
            throw new InvalidDataAccessApiUsageException("Cannot SELECT non zero index in cluster mode.");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v3, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r2v6, types: [byte[], byte[][]] */
    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(new byte[]{srcKey, destKey})) {
            return super.sMove(srcKey, destKey, value);
        }
        if (exists(srcKey).booleanValue() && sRem(srcKey, new byte[]{value}).longValue() > 0 && !sIsMember(destKey, value).booleanValue()) {
            return LettuceConverters.toBoolean(sAdd(destKey, new byte[]{value}));
        }
        return Boolean.FALSE;
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sInter(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            return super.sInter(keys);
        }
        Collection<Set<byte[]>> nodeResult = this.clusterCommandExecutor.executeMuliKeyCommand(new LettuceMultiKeyClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.31
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public Set<byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client, byte[] key) {
                return client.smembers(key);
            }
        }, Arrays.asList(keys)).resultsAsList();
        ByteArraySet result = null;
        for (Set<byte[]> entry : nodeResult) {
            ByteArraySet tmp = new ByteArraySet(entry);
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

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisSetCommands
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, keys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            return super.sInterStore(destKey, keys);
        }
        Set<byte[]> result = sInter(keys);
        if (result.isEmpty()) {
            return 0L;
        }
        return sAdd(destKey, (byte[][]) result.toArray((Object[]) new byte[result.size()]));
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sUnion(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            return super.sUnion(keys);
        }
        Collection<Set<byte[]>> nodeResult = this.clusterCommandExecutor.executeMuliKeyCommand(new LettuceMultiKeyClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.32
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public Set<byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client, byte[] key) {
                return client.smembers(key);
            }
        }, Arrays.asList(keys)).resultsAsList();
        ByteArraySet result = new ByteArraySet();
        for (Set<byte[]> entry : nodeResult) {
            result.addAll((Iterable<byte[]>) entry);
        }
        if (result.isEmpty()) {
            return Collections.emptySet();
        }
        return result.asRawSet();
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisSetCommands
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, keys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            return super.sUnionStore(destKey, keys);
        }
        Set<byte[]> result = sUnion(keys);
        if (result.isEmpty()) {
            return 0L;
        }
        return sAdd(destKey, (byte[][]) result.toArray((Object[]) new byte[result.size()]));
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sDiff(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            return super.sDiff(keys);
        }
        byte[] source = keys[0];
        byte[][] others = (byte[][]) Arrays.copyOfRange(keys, 1, keys.length);
        ByteArraySet values = new ByteArraySet(sMembers(source));
        Collection<Set<byte[]>> nodeResult = this.clusterCommandExecutor.executeMuliKeyCommand(new LettuceMultiKeyClusterCommandCallback<Set<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.33
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.MultiKeyClusterCommandCallback
            public Set<byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client, byte[] key) {
                return client.smembers(key);
            }
        }, Arrays.asList(others)).resultsAsList();
        if (values.isEmpty()) {
            return Collections.emptySet();
        }
        for (Set<byte[]> toSubstract : nodeResult) {
            values.removeAll(toSubstract);
        }
        return values.asRawSet();
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisSetCommands
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destKey, keys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            return super.sDiffStore(destKey, keys);
        }
        Set<byte[]> diff = sDiff(keys);
        if (diff.isEmpty()) {
            return 0L;
        }
        return sAdd(destKey, (byte[][]) diff.toArray((Object[]) new byte[diff.size()]));
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection
    protected StatefulConnection<byte[], byte[]> doGetAsyncDedicatedConnection() {
        return this.clusterClient.connect(CODEC);
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public List<RedisClusterNode> clusterGetNodes() {
        return LettuceConverters.partitionsToClusterNodes(this.clusterClient.getPartitions());
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfCount(byte[]... keys) {
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys)) {
            try {
                return super.pfCount(keys);
            } catch (Exception ex) {
                throw convertLettuceAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("All keys must map to same slot for pfcount in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.HyperLogLogCommands
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        byte[][] allKeys = ByteUtils.mergeArrays(destinationKey, sourceKeys);
        if (ClusterSlotHashUtil.isSameSlotForAllKeys(allKeys)) {
            try {
                super.pfMerge(destinationKey, sourceKeys);
                return;
            } catch (Exception ex) {
                throw convertLettuceAccessException(ex);
            }
        }
        throw new InvalidDataAccessApiUsageException("All keys must map to same slot for pfmerge in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisTxCommands
    public void watch(byte[]... keys) {
        throw new InvalidDataAccessApiUsageException("WATCH is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisTxCommands
    public void unwatch() {
        throw new InvalidDataAccessApiUsageException("UNWATCH is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisTxCommands
    public void multi() {
        throw new InvalidDataAccessApiUsageException("MULTI is currently not supported in cluster mode.");
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public List<String> getConfig(final String pattern) {
        List<ClusterCommandExecutor.NodeResult<List<String>>> mapResult = this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<List<String>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.34
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<String> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
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
        return (List) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<List<String>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.35
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<String> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.configGet(pattern);
            }
        }, node).getValue();
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public void setConfig(final String param, final String value) {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.36
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.configSet(param, value);
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void setConfig(RedisClusterNode node, final String param, final String value) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.37
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.configSet(param, value);
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public void resetConfigStats() {
        this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.38
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.configResetstat();
            }
        });
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public void resetConfigStats(RedisClusterNode node) {
        this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.39
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.configResetstat();
            }
        }, node);
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public Long time() {
        return convertListOfStringToTime((List) this.clusterCommandExecutor.executeCommandOnArbitraryNode(new LettuceClusterCommandCallback<List<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.40
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.time();
            }
        }).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public Long time(RedisClusterNode node) {
        return convertListOfStringToTime((List) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<List<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.41
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public List<byte[]> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.time();
            }
        }, node).getValue());
    }

    private Long convertListOfStringToTime(List<byte[]> serverTimeInformation) {
        Assert.notEmpty(serverTimeInformation, "Received invalid result from server. Expected 2 items in collection.");
        Assert.isTrue(serverTimeInformation.size() == 2, "Received invalid number of arguments from redis server. Expected 2 received " + serverTimeInformation.size());
        return Converters.toTimeMillis(LettuceConverters.toString(serverTimeInformation.get(0)), LettuceConverters.toString(serverTimeInformation.get(1)));
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.RedisServerCommands
    public List<RedisClientInfo> getClientList() {
        List<String> map = this.clusterCommandExecutor.executeCommandOnAllNodes(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.42
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.clientList();
            }
        }).resultsAsList();
        ArrayList<RedisClientInfo> result = new ArrayList<>();
        for (String infos : map) {
            result.addAll(LettuceConverters.toListOfRedisClientInformation(infos));
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisClusterConnection
    public List<RedisClientInfo> getClientList(RedisClusterNode node) {
        return LettuceConverters.toListOfRedisClientInformation((String) this.clusterCommandExecutor.executeCommandOnSingleNode(new LettuceClusterCommandCallback<String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.43
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public String doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return client.clientList();
            }
        }, node).getValue());
    }

    @Override // org.springframework.data.redis.connection.RedisClusterCommands
    public Map<RedisClusterNode, Collection<RedisClusterNode>> clusterGetMasterSlaveMap() {
        List<ClusterCommandExecutor.NodeResult<Collection<RedisClusterNode>>> nodeResults = this.clusterCommandExecutor.executeCommandAsyncOnNodes(new LettuceClusterCommandCallback<Collection<RedisClusterNode>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceClusterConnection.44
            @Override // org.springframework.data.redis.connection.ClusterCommandExecutor.ClusterCommandCallback
            public Set<RedisClusterNode> doInCluster(com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> client) {
                return Converters.toSetOfRedisClusterNodes(client.clusterSlaves(client.clusterMyId()));
            }
        }, this.topologyProvider.getTopology().getActiveMasterNodes()).getResults();
        Map<RedisClusterNode, Collection<RedisClusterNode>> result = new LinkedHashMap<>();
        for (ClusterCommandExecutor.NodeResult<Collection<RedisClusterNode>> nodeResult : nodeResults) {
            result.put(nodeResult.getNode(), nodeResult.getValue());
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.lettuce.LettuceConnection, org.springframework.data.redis.connection.AbstractRedisConnection, org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        if (!isClosed() && this.disposeClusterCommandExecutorOnClose) {
            try {
                this.clusterCommandExecutor.destroy();
            } catch (Exception ex) {
                this.log.warn("Cannot properly close cluster command executor", ex);
            }
        }
        super.close();
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceClusterConnection$LettuceClusterNodeResourceProvider.class */
    static class LettuceClusterNodeResourceProvider implements ClusterNodeResourceProvider, DisposableBean {
        private final RedisClusterClient client;
        private volatile StatefulRedisClusterConnection<byte[], byte[]> connection;

        public LettuceClusterNodeResourceProvider(RedisClusterClient client) {
            this.client = client;
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: com.lambdaworks.redis.RedisException */
        @Override // org.springframework.data.redis.connection.ClusterNodeResourceProvider
        public com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands<byte[], byte[]> getResourceForSpecificNode(RedisClusterNode node) throws RedisException {
            Assert.notNull(node, "Node must not be null!");
            if (this.connection == null) {
                synchronized (this) {
                    if (this.connection == null) {
                        this.connection = this.client.connect(LettuceClusterConnection.CODEC);
                    }
                }
            }
            try {
                return this.connection.getConnection(node.getHost(), node.getPort().intValue()).sync();
            } catch (RedisException e) {
                if (e.getCause() instanceof IllegalArgumentException) {
                    throw ((IllegalArgumentException) e.getCause());
                }
                throw e;
            }
        }

        @Override // org.springframework.data.redis.connection.ClusterNodeResourceProvider
        public void returnResourceForSpecificNode(RedisClusterNode node, Object resource) {
        }

        @Override // org.springframework.beans.factory.DisposableBean
        public void destroy() throws Exception {
            if (this.connection != null) {
                this.connection.close();
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceClusterConnection$LettuceClusterTopologyProvider.class */
    static class LettuceClusterTopologyProvider implements ClusterTopologyProvider {
        private final RedisClusterClient client;

        public LettuceClusterTopologyProvider(RedisClusterClient client) {
            Assert.notNull(client, "RedisClusterClient must not be null.");
            this.client = client;
        }

        @Override // org.springframework.data.redis.connection.ClusterTopologyProvider
        public ClusterTopology getTopology() {
            return new ClusterTopology(new LinkedHashSet(LettuceConverters.partitionsToClusterNodes(this.client.getPartitions())));
        }
    }
}
