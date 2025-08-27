package redis.clients.jedis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryShardedJedis.class */
public class BinaryShardedJedis extends Sharded<Jedis, JedisShardInfo> implements BinaryJedisCommands {
    public BinaryShardedJedis(List<JedisShardInfo> shards) {
        super(shards);
    }

    public BinaryShardedJedis(List<JedisShardInfo> shards, Hashing algo) {
        super(shards, algo);
    }

    public BinaryShardedJedis(List<JedisShardInfo> shards, Pattern keyTagPattern) {
        super(shards, keyTagPattern);
    }

    public BinaryShardedJedis(List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
        super(shards, algo, keyTagPattern);
    }

    public void disconnect() {
        for (Jedis jedis : getAllShards()) {
            if (jedis.isConnected()) {
                try {
                    jedis.quit();
                } catch (JedisConnectionException e) {
                }
                try {
                    jedis.disconnect();
                } catch (JedisConnectionException e2) {
                }
            }
        }
    }

    protected Jedis create(JedisShardInfo shard) {
        return new Jedis(shard);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String set(byte[] key, byte[] value) {
        Jedis j = getShard(key);
        return j.set(key, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String set(byte[] key, byte[] value, byte[] nxxx) {
        Jedis j = getShard(key);
        return j.set(key, value, nxxx);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        Jedis j = getShard(key);
        return j.set(key, value, nxxx, expx, time);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] get(byte[] key) {
        Jedis j = getShard(key);
        return j.get(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean exists(byte[] key) {
        Jedis j = getShard(key);
        return j.exists(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String type(byte[] key) {
        Jedis j = getShard(key);
        return j.type(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long expire(byte[] key, int seconds) {
        Jedis j = getShard(key);
        return j.expire(key, seconds);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pexpire(byte[] key, long milliseconds) {
        Jedis j = getShard(key);
        return j.pexpire(key, milliseconds);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    @Deprecated
    public Long pexpire(String key, long milliseconds) {
        Jedis j = getShard(key);
        return j.pexpire(key, milliseconds);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long expireAt(byte[] key, long unixTime) {
        Jedis j = getShard(key);
        return j.expireAt(key, unixTime);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        Jedis j = getShard(key);
        return j.pexpireAt(key, millisecondsTimestamp);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long ttl(byte[] key) {
        Jedis j = getShard(key);
        return j.ttl(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pttl(byte[] key) {
        Jedis j = getShard(key);
        return j.pttl(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] getSet(byte[] key, byte[] value) {
        Jedis j = getShard(key);
        return j.getSet(key, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long setnx(byte[] key, byte[] value) {
        Jedis j = getShard(key);
        return j.setnx(key, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String setex(byte[] key, int seconds, byte[] value) {
        Jedis j = getShard(key);
        return j.setex(key, seconds, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long decrBy(byte[] key, long decrement) {
        Jedis j = getShard(key);
        return j.decrBy(key, decrement);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long decr(byte[] key) {
        Jedis j = getShard(key);
        return j.decr(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long del(byte[] key) {
        Jedis j = getShard(key);
        return j.del(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long incrBy(byte[] key, long increment) {
        Jedis j = getShard(key);
        return j.incrBy(key, increment);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double incrByFloat(byte[] key, double increment) {
        Jedis j = getShard(key);
        return j.incrByFloat(key, increment);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long incr(byte[] key) {
        Jedis j = getShard(key);
        return j.incr(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long append(byte[] key, byte[] value) {
        Jedis j = getShard(key);
        return j.append(key, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] substr(byte[] key, int start, int end) {
        Jedis j = getShard(key);
        return j.substr(key, start, end);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hset(byte[] key, byte[] field, byte[] value) {
        Jedis j = getShard(key);
        return j.hset(key, field, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] hget(byte[] key, byte[] field) {
        Jedis j = getShard(key);
        return j.hget(key, field);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        Jedis j = getShard(key);
        return j.hsetnx(key, field, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        Jedis j = getShard(key);
        return j.hmset(key, hash);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        Jedis j = getShard(key);
        return j.hmget(key, fields);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hincrBy(byte[] key, byte[] field, long value) {
        Jedis j = getShard(key);
        return j.hincrBy(key, field, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        Jedis j = getShard(key);
        return j.hincrByFloat(key, field, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean hexists(byte[] key, byte[] field) {
        Jedis j = getShard(key);
        return j.hexists(key, field);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hdel(byte[] key, byte[]... fields) {
        Jedis j = getShard(key);
        return j.hdel(key, fields);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hlen(byte[] key) {
        Jedis j = getShard(key);
        return j.hlen(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> hkeys(byte[] key) {
        Jedis j = getShard(key);
        return j.hkeys(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Collection<byte[]> hvals(byte[] key) {
        Jedis j = getShard(key);
        return j.hvals(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        Jedis j = getShard(key);
        return j.hgetAll(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long rpush(byte[] key, byte[]... strings) {
        Jedis j = getShard(key);
        return j.rpush(key, strings);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long lpush(byte[] key, byte[]... strings) {
        Jedis j = getShard(key);
        return j.lpush(key, strings);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long strlen(byte[] key) {
        Jedis j = getShard(key);
        return j.strlen(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long lpushx(byte[] key, byte[]... string) {
        Jedis j = getShard(key);
        return j.lpushx(key, string);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long persist(byte[] key) {
        Jedis j = getShard(key);
        return j.persist(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long rpushx(byte[] key, byte[]... string) {
        Jedis j = getShard(key);
        return j.rpushx(key, string);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long llen(byte[] key) {
        Jedis j = getShard(key);
        return j.llen(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> lrange(byte[] key, long start, long stop) {
        Jedis j = getShard(key);
        return j.lrange(key, start, stop);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String ltrim(byte[] key, long start, long stop) {
        Jedis j = getShard(key);
        return j.ltrim(key, start, stop);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] lindex(byte[] key, long index) {
        Jedis j = getShard(key);
        return j.lindex(key, index);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String lset(byte[] key, long index, byte[] value) {
        Jedis j = getShard(key);
        return j.lset(key, index, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long lrem(byte[] key, long count, byte[] value) {
        Jedis j = getShard(key);
        return j.lrem(key, count, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] lpop(byte[] key) {
        Jedis j = getShard(key);
        return j.lpop(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] rpop(byte[] key) {
        Jedis j = getShard(key);
        return j.rpop(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long sadd(byte[] key, byte[]... members) {
        Jedis j = getShard(key);
        return j.sadd(key, members);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> smembers(byte[] key) {
        Jedis j = getShard(key);
        return j.smembers(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long srem(byte[] key, byte[]... members) {
        Jedis j = getShard(key);
        return j.srem(key, members);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] spop(byte[] key) {
        Jedis j = getShard(key);
        return j.spop(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> spop(byte[] key, long count) {
        Jedis j = getShard(key);
        return j.spop(key, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long scard(byte[] key) {
        Jedis j = getShard(key);
        return j.scard(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean sismember(byte[] key, byte[] member) {
        Jedis j = getShard(key);
        return j.sismember(key, member);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] srandmember(byte[] key) {
        Jedis j = getShard(key);
        return j.srandmember(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List srandmember(byte[] key, int count) {
        Jedis j = getShard(key);
        return j.srandmember(key, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, double score, byte[] member) {
        Jedis j = getShard(key);
        return j.zadd(key, score, member);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        Jedis j = getShard(key);
        return j.zadd(key, score, member, params);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        Jedis j = getShard(key);
        return j.zadd(key, scoreMembers);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        Jedis j = getShard(key);
        return j.zadd(key, scoreMembers, params);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrange(byte[] key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrange(key, start, stop);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zrem(byte[] key, byte[]... members) {
        Jedis j = getShard(key);
        return j.zrem(key, members);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double zincrby(byte[] key, double increment, byte[] member) {
        Jedis j = getShard(key);
        return j.zincrby(key, increment, member);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
        Jedis j = getShard(key);
        return j.zincrby(key, increment, member, params);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zrank(byte[] key, byte[] member) {
        Jedis j = getShard(key);
        return j.zrank(key, member);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zrevrank(byte[] key, byte[] member) {
        Jedis j = getShard(key);
        return j.zrevrank(key, member);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrange(byte[] key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrevrange(key, start, stop);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrangeWithScores(key, start, stop);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrevrangeWithScores(key, start, stop);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zcard(byte[] key) {
        Jedis j = getShard(key);
        return j.zcard(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double zscore(byte[] key, byte[] member) {
        Jedis j = getShard(key);
        return j.zscore(key, member);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> sort(byte[] key) {
        Jedis j = getShard(key);
        return j.sort(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        Jedis j = getShard(key);
        return j.sort(key, sortingParameters);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zcount(byte[] key, double min, double max) {
        Jedis j = getShard(key);
        return j.zcount(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zcount(byte[] key, byte[] min, byte[] max) {
        Jedis j = getShard(key);
        return j.zcount(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByRank(byte[] key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zremrangeByRank(key, start, stop);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByScore(byte[] key, double min, double max) {
        Jedis j = getShard(key);
        return j.zremrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
        Jedis j = getShard(key);
        return j.zremrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        Jedis j = getShard(key);
        return j.zlexcount(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        Jedis j = getShard(key);
        return j.zrangeByLex(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByLex(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        Jedis j = getShard(key);
        return j.zrevrangeByLex(key, max, min);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByLex(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        Jedis j = getShard(key);
        return j.zremrangeByLex(key, min, max);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
        Jedis j = getShard(key);
        return j.linsert(key, where, pivot, value);
    }

    @Deprecated
    public List<Object> pipelined(ShardedJedisPipeline shardedJedisPipeline) {
        shardedJedisPipeline.setShardedJedis(this);
        shardedJedisPipeline.execute();
        return shardedJedisPipeline.getResults();
    }

    public ShardedJedisPipeline pipelined() {
        ShardedJedisPipeline pipeline = new ShardedJedisPipeline();
        pipeline.setShardedJedis(this);
        return pipeline;
    }

    public Long objectRefcount(byte[] key) {
        Jedis j = getShard(key);
        return j.objectRefcount(key);
    }

    public byte[] objectEncoding(byte[] key) {
        Jedis j = getShard(key);
        return j.objectEncoding(key);
    }

    public Long objectIdletime(byte[] key) {
        Jedis j = getShard(key);
        return j.objectIdletime(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean setbit(byte[] key, long offset, boolean value) {
        Jedis j = getShard(key);
        return j.setbit(key, offset, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        Jedis j = getShard(key);
        return j.setbit(key, offset, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean getbit(byte[] key, long offset) {
        Jedis j = getShard(key);
        return j.getbit(key, offset);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long setrange(byte[] key, long offset, byte[] value) {
        Jedis j = getShard(key);
        return j.setrange(key, offset, value);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        Jedis j = getShard(key);
        return j.getrange(key, startOffset, endOffset);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long move(byte[] key, int dbIndex) {
        Jedis j = getShard(key);
        return j.move(key, dbIndex);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] echo(byte[] arg) {
        Jedis j = getShard(arg);
        return j.echo(arg);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> brpop(byte[] arg) {
        Jedis j = getShard(arg);
        return j.brpop(arg);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> blpop(byte[] arg) {
        Jedis j = getShard(arg);
        return j.blpop(arg);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long bitcount(byte[] key) {
        Jedis j = getShard(key);
        return j.bitcount(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long bitcount(byte[] key, long start, long end) {
        Jedis j = getShard(key);
        return j.bitcount(key, start, end);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pfadd(byte[] key, byte[]... elements) {
        Jedis j = getShard(key);
        return j.pfadd(key, elements);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public long pfcount(byte[] key) {
        Jedis j = getShard(key);
        return j.pfcount(key);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        Jedis j = getShard(key);
        return j.geoadd(key, longitude, latitude, member);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        Jedis j = getShard(key);
        return j.geoadd(key, memberCoordinateMap);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        Jedis j = getShard(key);
        return j.geodist(key, member1, member2);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        Jedis j = getShard(key);
        return j.geodist(key, member1, member2, unit);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> geohash(byte[] key, byte[]... members) {
        Jedis j = getShard(key);
        return j.geohash(key, members);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        Jedis j = getShard(key);
        return j.geopos(key, members);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        Jedis j = getShard(key);
        return j.georadius(key, longitude, latitude, radius, unit);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        Jedis j = getShard(key);
        return j.georadius(key, longitude, latitude, radius, unit, param);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        Jedis j = getShard(key);
        return j.georadiusByMember(key, member, radius, unit);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        Jedis j = getShard(key);
        return j.georadiusByMember(key, member, radius, unit, param);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        Jedis j = getShard(key);
        return j.hscan(key, cursor);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        Jedis j = getShard(key);
        return j.hscan(key, cursor, params);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        Jedis j = getShard(key);
        return j.sscan(key, cursor);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        Jedis j = getShard(key);
        return j.sscan(key, cursor, params);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        Jedis j = getShard(key);
        return j.zscan(key, cursor);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        Jedis j = getShard(key);
        return j.zscan(key, cursor, params);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<Long> bitfield(byte[] key, byte[]... arguments) {
        Jedis j = getShard(key);
        return j.bitfield(key, arguments);
    }
}
