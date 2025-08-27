package redis.clients.jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisClusterInfoCache.class */
public class JedisClusterInfoCache {
    private final Map<String, JedisPool> nodes;
    private final Map<Integer, JedisPool> slots;
    private final ReentrantReadWriteLock rwl;
    private final Lock r;
    private final Lock w;
    private volatile boolean rediscovering;
    private final GenericObjectPoolConfig poolConfig;
    private int connectionTimeout;
    private int soTimeout;
    private String password;
    private String clientName;
    private static final int MASTER_NODE_INDEX = 2;

    public JedisClusterInfoCache(GenericObjectPoolConfig poolConfig, int timeout) {
        this(poolConfig, timeout, timeout, null, null);
    }

    public JedisClusterInfoCache(GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, String clientName) {
        this.nodes = new HashMap();
        this.slots = new HashMap();
        this.rwl = new ReentrantReadWriteLock();
        this.r = this.rwl.readLock();
        this.w = this.rwl.writeLock();
        this.poolConfig = poolConfig;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = password;
        this.clientName = clientName;
    }

    public void discoverClusterNodesAndSlots(Jedis jedis) {
        this.w.lock();
        try {
            reset();
            List<Object> slots = jedis.clusterSlots();
            for (Object slotInfoObj : slots) {
                List<Object> slotInfo = (List) slotInfoObj;
                if (slotInfo.size() > 2) {
                    List<Integer> slotNums = getAssignedSlotArray(slotInfo);
                    int size = slotInfo.size();
                    for (int i = 2; i < size; i++) {
                        List<Object> hostInfos = (List) slotInfo.get(i);
                        if (hostInfos.size() > 0) {
                            HostAndPort targetNode = generateHostAndPort(hostInfos);
                            setupNodeIfNotExist(targetNode);
                            if (i == 2) {
                                assignSlotsToNode(slotNums, targetNode);
                            }
                        }
                    }
                }
            }
        } finally {
            this.w.unlock();
        }
    }

    public void renewClusterSlots(Jedis jedis) {
        if (this.rediscovering) {
            return;
        }
        try {
            this.w.lock();
            if (!this.rediscovering) {
                this.rediscovering = true;
                try {
                    if (jedis != null) {
                        try {
                            discoverClusterSlots(jedis);
                            this.rediscovering = false;
                            this.w.unlock();
                            return;
                        } catch (JedisException e) {
                        }
                    }
                    for (JedisPool jp : getShuffledNodesPool()) {
                        Jedis j = null;
                        try {
                            j = jp.getResource();
                            discoverClusterSlots(j);
                            if (j != null) {
                                j.close();
                            }
                            this.w.unlock();
                            return;
                        } catch (JedisConnectionException e2) {
                            if (j != null) {
                                j.close();
                            }
                        } catch (Throwable th) {
                            if (j != null) {
                                j.close();
                            }
                            throw th;
                        }
                    }
                    this.rediscovering = false;
                } finally {
                    this.rediscovering = false;
                }
            }
        } finally {
            this.w.unlock();
        }
    }

    private void discoverClusterSlots(Jedis jedis) {
        List<Object> slots = jedis.clusterSlots();
        this.slots.clear();
        for (Object slotInfoObj : slots) {
            List<Object> slotInfo = (List) slotInfoObj;
            if (slotInfo.size() > 2) {
                List<Integer> slotNums = getAssignedSlotArray(slotInfo);
                List<Object> hostInfos = (List) slotInfo.get(2);
                if (!hostInfos.isEmpty()) {
                    HostAndPort targetNode = generateHostAndPort(hostInfos);
                    assignSlotsToNode(slotNums, targetNode);
                }
            }
        }
    }

    private HostAndPort generateHostAndPort(List<Object> hostInfos) {
        return new HostAndPort(SafeEncoder.encode((byte[]) hostInfos.get(0)), ((Long) hostInfos.get(1)).intValue());
    }

    public JedisPool setupNodeIfNotExist(HostAndPort node) {
        this.w.lock();
        try {
            String nodeKey = getNodeKey(node);
            JedisPool existingPool = this.nodes.get(nodeKey);
            if (existingPool != null) {
                return existingPool;
            }
            JedisPool nodePool = new JedisPool(this.poolConfig, node.getHost(), node.getPort(), this.connectionTimeout, this.soTimeout, this.password, 0, this.clientName, false, null, null, null);
            this.nodes.put(nodeKey, nodePool);
            this.w.unlock();
            return nodePool;
        } finally {
            this.w.unlock();
        }
    }

    public JedisPool setupNodeIfNotExist(HostAndPort node, boolean ssl) {
        this.w.lock();
        try {
            String nodeKey = getNodeKey(node);
            JedisPool existingPool = this.nodes.get(nodeKey);
            if (existingPool != null) {
                return existingPool;
            }
            JedisPool nodePool = new JedisPool(this.poolConfig, node.getHost(), node.getPort(), this.connectionTimeout, this.soTimeout, this.password, 0, null, ssl, null, null, null);
            this.nodes.put(nodeKey, nodePool);
            this.w.unlock();
            return nodePool;
        } finally {
            this.w.unlock();
        }
    }

    public JedisPool setupNodeIfNotExist(HostAndPort node, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.w.lock();
        try {
            String nodeKey = getNodeKey(node);
            JedisPool existingPool = this.nodes.get(nodeKey);
            if (existingPool != null) {
                return existingPool;
            }
            JedisPool nodePool = new JedisPool(this.poolConfig, node.getHost(), node.getPort(), this.connectionTimeout, this.soTimeout, this.password, 0, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
            this.nodes.put(nodeKey, nodePool);
            this.w.unlock();
            return nodePool;
        } finally {
            this.w.unlock();
        }
    }

    public void assignSlotToNode(int slot, HostAndPort targetNode) {
        this.w.lock();
        try {
            JedisPool targetPool = setupNodeIfNotExist(targetNode);
            this.slots.put(Integer.valueOf(slot), targetPool);
            this.w.unlock();
        } catch (Throwable th) {
            this.w.unlock();
            throw th;
        }
    }

    public void assignSlotsToNode(List<Integer> targetSlots, HostAndPort targetNode) {
        this.w.lock();
        try {
            JedisPool targetPool = setupNodeIfNotExist(targetNode);
            for (Integer slot : targetSlots) {
                this.slots.put(slot, targetPool);
            }
        } finally {
            this.w.unlock();
        }
    }

    public JedisPool getNode(String nodeKey) {
        this.r.lock();
        try {
            return this.nodes.get(nodeKey);
        } finally {
            this.r.unlock();
        }
    }

    public JedisPool getSlotPool(int slot) {
        this.r.lock();
        try {
            return this.slots.get(Integer.valueOf(slot));
        } finally {
            this.r.unlock();
        }
    }

    public Map<String, JedisPool> getNodes() {
        this.r.lock();
        try {
            return new HashMap(this.nodes);
        } finally {
            this.r.unlock();
        }
    }

    public List<JedisPool> getShuffledNodesPool() {
        this.r.lock();
        try {
            List<JedisPool> pools = new ArrayList<>(this.nodes.values());
            Collections.shuffle(pools);
            return pools;
        } finally {
            this.r.unlock();
        }
    }

    public void reset() {
        this.w.lock();
        try {
            for (JedisPool pool : this.nodes.values()) {
                if (pool != null) {
                    try {
                        pool.destroy();
                    } catch (Exception e) {
                    }
                }
            }
            this.nodes.clear();
            this.slots.clear();
            this.w.unlock();
        } catch (Throwable th) {
            this.w.unlock();
            throw th;
        }
    }

    public static String getNodeKey(HostAndPort hnp) {
        return hnp.getHost() + ":" + hnp.getPort();
    }

    public static String getNodeKey(Client client) {
        return client.getHost() + ":" + client.getPort();
    }

    public static String getNodeKey(Jedis jedis) {
        return getNodeKey(jedis.getClient());
    }

    private List<Integer> getAssignedSlotArray(List<Object> slotInfo) {
        List<Integer> slotNums = new ArrayList<>();
        for (int slot = ((Long) slotInfo.get(0)).intValue(); slot <= ((Long) slotInfo.get(1)).intValue(); slot++) {
            slotNums.add(Integer.valueOf(slot));
        }
        return slotNums;
    }
}
