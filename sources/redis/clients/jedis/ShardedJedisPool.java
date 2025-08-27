package redis.clients.jedis;

import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.clients.util.Hashing;
import redis.clients.util.Pool;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ShardedJedisPool.class */
public class ShardedJedisPool extends Pool<ShardedJedis> {
    public ShardedJedisPool(GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards) {
        this(poolConfig, shards, Hashing.MURMUR_HASH);
    }

    public ShardedJedisPool(GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Hashing algo) {
        this(poolConfig, shards, algo, null);
    }

    public ShardedJedisPool(GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Pattern keyTagPattern) {
        this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern);
    }

    public ShardedJedisPool(GenericObjectPoolConfig poolConfig, List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
        super(poolConfig, new ShardedJedisFactory(shards, algo, keyTagPattern));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // redis.clients.util.Pool
    public ShardedJedis getResource() {
        ShardedJedis jedis = (ShardedJedis) super.getResource();
        jedis.setDataSource(this);
        return jedis;
    }

    @Override // redis.clients.util.Pool
    @Deprecated
    public void returnBrokenResource(ShardedJedis resource) {
        if (resource != null) {
            returnBrokenResourceObject(resource);
        }
    }

    @Override // redis.clients.util.Pool
    @Deprecated
    public void returnResource(ShardedJedis resource) {
        if (resource != null) {
            resource.resetState();
            returnResourceObject(resource);
        }
    }

    /* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ShardedJedisPool$ShardedJedisFactory.class */
    private static class ShardedJedisFactory implements PooledObjectFactory<ShardedJedis> {
        private List<JedisShardInfo> shards;
        private Hashing algo;
        private Pattern keyTagPattern;

        public ShardedJedisFactory(List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
            this.shards = shards;
            this.algo = algo;
            this.keyTagPattern = keyTagPattern;
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public PooledObject<ShardedJedis> makeObject() throws Exception {
            ShardedJedis jedis = new ShardedJedis(this.shards, this.algo, this.keyTagPattern);
            return new DefaultPooledObject(jedis);
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public void destroyObject(PooledObject<ShardedJedis> pooledShardedJedis) throws Exception {
            ShardedJedis shardedJedis = pooledShardedJedis.getObject();
            for (Jedis jedis : shardedJedis.getAllShards()) {
                if (jedis.isConnected()) {
                    try {
                        try {
                            jedis.quit();
                        } catch (Exception e) {
                        }
                    } catch (Exception e2) {
                    }
                    jedis.disconnect();
                }
            }
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public boolean validateObject(PooledObject<ShardedJedis> pooledShardedJedis) {
            try {
                ShardedJedis jedis = pooledShardedJedis.getObject();
                for (Jedis shard : jedis.getAllShards()) {
                    if (!shard.ping().equals(LettuceConnectionFactory.PING_REPLY)) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public void activateObject(PooledObject<ShardedJedis> p) throws Exception {
        }

        @Override // org.apache.commons.pool2.PooledObjectFactory
        public void passivateObject(PooledObject<ShardedJedis> p) throws Exception {
        }
    }
}
