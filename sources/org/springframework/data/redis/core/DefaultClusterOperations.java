package org.springframework.data.redis.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterCommands;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/DefaultClusterOperations.class */
public class DefaultClusterOperations<K, V> extends AbstractOperations<K, V> implements ClusterOperations<K, V> {
    private final RedisTemplate<K, V> template;

    @Override // org.springframework.data.redis.core.AbstractOperations
    public /* bridge */ /* synthetic */ RedisOperations getOperations() {
        return super.getOperations();
    }

    public DefaultClusterOperations(RedisTemplate<K, V> template) {
        super(template);
        this.template = template;
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public Set<K> keys(final RedisClusterNode redisClusterNode, final K k) {
        Assert.notNull(redisClusterNode, "ClusterNode must not be null.");
        return (Set) execute(new RedisClusterCallback<Set<K>>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.1
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Set<K> doInRedis(RedisClusterConnection connection) throws DataAccessException {
                return DefaultClusterOperations.this.deserializeKeys(connection.keys(redisClusterNode, DefaultClusterOperations.this.rawKey(k)));
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public K randomKey(final RedisClusterNode redisClusterNode) {
        Assert.notNull(redisClusterNode, "ClusterNode must not be null.");
        return (K) execute(new RedisClusterCallback<K>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.2
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public K doInRedis(RedisClusterConnection connection) throws DataAccessException {
                return DefaultClusterOperations.this.deserializeKey(connection.randomKey(redisClusterNode));
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public String ping(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        return (String) execute(new RedisClusterCallback<String>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public String doInRedis(RedisClusterConnection connection) throws DataAccessException {
                return connection.ping(node);
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void addSlots(final RedisClusterNode node, final int... slots) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.clusterAddSlots(node, slots);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void addSlots(RedisClusterNode node, RedisClusterNode.SlotRange range) {
        Assert.notNull(node, "ClusterNode must not be null.");
        Assert.notNull(range, "Range must not be null.");
        addSlots(node, range.getSlotsArray());
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void bgReWriteAof(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.bgReWriteAof(node);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void bgSave(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.bgSave(node);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void meet(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.clusterMeet(node);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void forget(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.clusterForget(node);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void flushDb(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.flushDb(node);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public Collection<RedisClusterNode> getSlaves(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        return (Collection) execute(new RedisClusterCallback<Collection<RedisClusterNode>>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Collection<RedisClusterNode> doInRedis(RedisClusterConnection connection) throws DataAccessException {
                return connection.clusterGetSlaves(node);
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void save(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.save(node);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void shutdown(final RedisClusterNode node) {
        Assert.notNull(node, "ClusterNode must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.shutdown(node);
                return null;
            }
        });
    }

    @Override // org.springframework.data.redis.core.ClusterOperations
    public void reshard(final RedisClusterNode source, final int slot, final RedisClusterNode target) {
        Assert.notNull(source, "Source node must not be null.");
        Assert.notNull(target, "Target node must not be null.");
        execute(new RedisClusterCallback<Void>() { // from class: org.springframework.data.redis.core.DefaultClusterOperations.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.data.redis.core.RedisClusterCallback
            public Void doInRedis(RedisClusterConnection connection) throws DataAccessException {
                connection.clusterSetSlot(target, slot, RedisClusterCommands.AddSlots.IMPORTING);
                connection.clusterSetSlot(source, slot, RedisClusterCommands.AddSlots.MIGRATING);
                List<byte[]> keys = connection.clusterGetKeysInSlot(slot, Integer.MAX_VALUE);
                for (byte[] key : keys) {
                    connection.migrate(key, source, 0, RedisServerCommands.MigrateOption.COPY);
                }
                connection.clusterSetSlot(target, slot, RedisClusterCommands.AddSlots.NODE);
                return null;
            }
        });
    }

    public <T> T execute(RedisClusterCallback<T> callback) {
        Assert.notNull(callback, "ClusterCallback must not be null!");
        RedisClusterConnection connection = this.template.getConnectionFactory().getClusterConnection();
        try {
            T tDoInRedis = callback.doInRedis(connection);
            connection.close();
            return tDoInRedis;
        } catch (Throwable th) {
            connection.close();
            throw th;
        }
    }
}
