package redis.clients.jedis;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Hashing;
import redis.clients.util.Pool;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/ShardedJedis.class */
public class ShardedJedis extends BinaryShardedJedis implements JedisCommands, Closeable {
    protected Pool<ShardedJedis> dataSource;

    public ShardedJedis(List<JedisShardInfo> shards) {
        super(shards);
        this.dataSource = null;
    }

    public ShardedJedis(List<JedisShardInfo> shards, Hashing algo) {
        super(shards, algo);
        this.dataSource = null;
    }

    public ShardedJedis(List<JedisShardInfo> shards, Pattern keyTagPattern) {
        super(shards, keyTagPattern);
        this.dataSource = null;
    }

    public ShardedJedis(List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
        super(shards, algo, keyTagPattern);
        this.dataSource = null;
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(String key, String value) {
        Jedis j = getShard(key);
        return j.set(key, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(String key, String value, String nxxx, String expx, long time) {
        Jedis j = getShard(key);
        return j.set(key, value, nxxx, expx, time);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(String key, String value, String nxxx) {
        Jedis j = getShard(key);
        return j.set(key, value, nxxx);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String get(String key) {
        Jedis j = getShard(key);
        return j.get(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String echo(String string) {
        Jedis j = getShard(string);
        return j.echo(string);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean exists(String key) {
        Jedis j = getShard(key);
        return j.exists(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String type(String key) {
        Jedis j = getShard(key);
        return j.type(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long expire(String key, int seconds) {
        Jedis j = getShard(key);
        return j.expire(key, seconds);
    }

    @Override // redis.clients.jedis.BinaryShardedJedis, redis.clients.jedis.BinaryJedisCommands
    public Long pexpire(String key, long milliseconds) {
        Jedis j = getShard(key);
        return j.pexpire(key, milliseconds);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long expireAt(String key, long unixTime) {
        Jedis j = getShard(key);
        return j.expireAt(key, unixTime);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        Jedis j = getShard(key);
        return j.pexpireAt(key, millisecondsTimestamp);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long ttl(String key) {
        Jedis j = getShard(key);
        return j.ttl(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pttl(String key) {
        Jedis j = getShard(key);
        return j.pttl(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean setbit(String key, long offset, boolean value) {
        Jedis j = getShard(key);
        return j.setbit(key, offset, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean setbit(String key, long offset, String value) {
        Jedis j = getShard(key);
        return j.setbit(key, offset, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean getbit(String key, long offset) {
        Jedis j = getShard(key);
        return j.getbit(key, offset);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long setrange(String key, long offset, String value) {
        Jedis j = getShard(key);
        return j.setrange(key, offset, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String getrange(String key, long startOffset, long endOffset) {
        Jedis j = getShard(key);
        return j.getrange(key, startOffset, endOffset);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String getSet(String key, String value) {
        Jedis j = getShard(key);
        return j.getSet(key, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long setnx(String key, String value) {
        Jedis j = getShard(key);
        return j.setnx(key, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String setex(String key, int seconds, String value) {
        Jedis j = getShard(key);
        return j.setex(key, seconds, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String psetex(String key, long milliseconds, String value) {
        Jedis j = getShard(key);
        return j.psetex(key, milliseconds, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> blpop(String arg) {
        Jedis j = getShard(arg);
        return j.blpop(arg);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> blpop(int timeout, String key) {
        Jedis j = getShard(key);
        return j.blpop(timeout, key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> brpop(String arg) {
        Jedis j = getShard(arg);
        return j.brpop(arg);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> brpop(int timeout, String key) {
        Jedis j = getShard(key);
        return j.brpop(timeout, key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long decrBy(String key, long decrement) {
        Jedis j = getShard(key);
        return j.decrBy(key, decrement);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long decr(String key) {
        Jedis j = getShard(key);
        return j.decr(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long incrBy(String key, long increment) {
        Jedis j = getShard(key);
        return j.incrBy(key, increment);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double incrByFloat(String key, double increment) {
        Jedis j = getShard(key);
        return j.incrByFloat(key, increment);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long incr(String key) {
        Jedis j = getShard(key);
        return j.incr(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long append(String key, String value) {
        Jedis j = getShard(key);
        return j.append(key, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String substr(String key, int start, int end) {
        Jedis j = getShard(key);
        return j.substr(key, start, end);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hset(String key, String field, String value) {
        Jedis j = getShard(key);
        return j.hset(key, field, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String hget(String key, String field) {
        Jedis j = getShard(key);
        return j.hget(key, field);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hsetnx(String key, String field, String value) {
        Jedis j = getShard(key);
        return j.hsetnx(key, field, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String hmset(String key, Map<String, String> hash) {
        Jedis j = getShard(key);
        return j.hmset(key, hash);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> hmget(String key, String... fields) {
        Jedis j = getShard(key);
        return j.hmget(key, fields);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hincrBy(String key, String field, long value) {
        Jedis j = getShard(key);
        return j.hincrBy(key, field, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double hincrByFloat(String key, String field, double value) {
        Jedis j = getShard(key);
        return j.hincrByFloat(key, field, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean hexists(String key, String field) {
        Jedis j = getShard(key);
        return j.hexists(key, field);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long del(String key) {
        Jedis j = getShard(key);
        return j.del(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hdel(String key, String... fields) {
        Jedis j = getShard(key);
        return j.hdel(key, fields);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hlen(String key) {
        Jedis j = getShard(key);
        return j.hlen(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> hkeys(String key) {
        Jedis j = getShard(key);
        return j.hkeys(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> hvals(String key) {
        Jedis j = getShard(key);
        return j.hvals(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Map<String, String> hgetAll(String key) {
        Jedis j = getShard(key);
        return j.hgetAll(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long rpush(String key, String... strings) {
        Jedis j = getShard(key);
        return j.rpush(key, strings);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lpush(String key, String... strings) {
        Jedis j = getShard(key);
        return j.lpush(key, strings);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lpushx(String key, String... string) {
        Jedis j = getShard(key);
        return j.lpushx(key, string);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long strlen(String key) {
        Jedis j = getShard(key);
        return j.strlen(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long move(String key, int dbIndex) {
        Jedis j = getShard(key);
        return j.move(key, dbIndex);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long rpushx(String key, String... string) {
        Jedis j = getShard(key);
        return j.rpushx(key, string);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long persist(String key) {
        Jedis j = getShard(key);
        return j.persist(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long llen(String key) {
        Jedis j = getShard(key);
        return j.llen(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> lrange(String key, long start, long stop) {
        Jedis j = getShard(key);
        return j.lrange(key, start, stop);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String ltrim(String key, long start, long stop) {
        Jedis j = getShard(key);
        return j.ltrim(key, start, stop);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lindex(String key, long index) {
        Jedis j = getShard(key);
        return j.lindex(key, index);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lset(String key, long index, String value) {
        Jedis j = getShard(key);
        return j.lset(key, index, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lrem(String key, long count, String value) {
        Jedis j = getShard(key);
        return j.lrem(key, count, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lpop(String key) {
        Jedis j = getShard(key);
        return j.lpop(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String rpop(String key) {
        Jedis j = getShard(key);
        return j.rpop(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long sadd(String key, String... members) {
        Jedis j = getShard(key);
        return j.sadd(key, members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> smembers(String key) {
        Jedis j = getShard(key);
        return j.smembers(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long srem(String key, String... members) {
        Jedis j = getShard(key);
        return j.srem(key, members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String spop(String key) {
        Jedis j = getShard(key);
        return j.spop(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> spop(String key, long count) {
        Jedis j = getShard(key);
        return j.spop(key, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long scard(String key) {
        Jedis j = getShard(key);
        return j.scard(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean sismember(String key, String member) {
        Jedis j = getShard(key);
        return j.sismember(key, member);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String srandmember(String key) {
        Jedis j = getShard(key);
        return j.srandmember(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> srandmember(String key, int count) {
        Jedis j = getShard(key);
        return j.srandmember(key, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, double score, String member) {
        Jedis j = getShard(key);
        return j.zadd(key, score, member);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, double score, String member, ZAddParams params) {
        Jedis j = getShard(key);
        return j.zadd(key, score, member, params);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis j = getShard(key);
        return j.zadd(key, scoreMembers);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        Jedis j = getShard(key);
        return j.zadd(key, scoreMembers, params);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrange(String key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrange(key, start, stop);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrem(String key, String... members) {
        Jedis j = getShard(key);
        return j.zrem(key, members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zincrby(String key, double increment, String member) {
        Jedis j = getShard(key);
        return j.zincrby(key, increment, member);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zincrby(String key, double increment, String member, ZIncrByParams params) {
        Jedis j = getShard(key);
        return j.zincrby(key, increment, member, params);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrank(String key, String member) {
        Jedis j = getShard(key);
        return j.zrank(key, member);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrevrank(String key, String member) {
        Jedis j = getShard(key);
        return j.zrevrank(key, member);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrange(String key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrevrange(key, start, stop);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeWithScores(String key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrangeWithScores(key, start, stop);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeWithScores(String key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zrevrangeWithScores(key, start, stop);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcard(String key) {
        Jedis j = getShard(key);
        return j.zcard(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zscore(String key, String member) {
        Jedis j = getShard(key);
        return j.zscore(key, member);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> sort(String key) {
        Jedis j = getShard(key);
        return j.sort(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> sort(String key, SortingParams sortingParameters) {
        Jedis j = getShard(key);
        return j.sort(key, sortingParameters);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcount(String key, double min, double max) {
        Jedis j = getShard(key);
        return j.zcount(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcount(String key, String min, String max) {
        Jedis j = getShard(key);
        return j.zcount(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, double min, double max) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScore(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScore(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        Jedis j = getShard(key);
        return j.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByRank(String key, long start, long stop) {
        Jedis j = getShard(key);
        return j.zremrangeByRank(key, start, stop);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByScore(String key, double min, double max) {
        Jedis j = getShard(key);
        return j.zremrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByScore(String key, String min, String max) {
        Jedis j = getShard(key);
        return j.zremrangeByScore(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zlexcount(String key, String min, String max) {
        return getShard(key).zlexcount(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByLex(String key, String min, String max) {
        return getShard(key).zrangeByLex(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return getShard(key).zrangeByLex(key, min, max, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        return getShard(key).zrevrangeByLex(key, max, min);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return getShard(key).zrevrangeByLex(key, max, min, offset, count);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByLex(String key, String min, String max) {
        return getShard(key).zremrangeByLex(key, min, max);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        Jedis j = getShard(key);
        return j.linsert(key, where, pivot, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitcount(String key) {
        Jedis j = getShard(key);
        return j.bitcount(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitcount(String key, long start, long end) {
        Jedis j = getShard(key);
        return j.bitcount(key, start, end);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitpos(String key, boolean value) {
        Jedis j = getShard(key);
        return j.bitpos(key, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitpos(String key, boolean value, BitPosParams params) {
        Jedis j = getShard(key);
        return j.bitpos(key, value, params);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
        Jedis j = getShard(key);
        return j.hscan(key, cursor);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<String> sscan(String key, int cursor) {
        Jedis j = getShard(key);
        return j.sscan(key, cursor);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<Tuple> zscan(String key, int cursor) {
        Jedis j = getShard(key);
        return j.zscan(key, cursor);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        Jedis j = getShard(key);
        return j.hscan(key, cursor);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        Jedis j = getShard(key);
        return j.hscan(key, cursor, params);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<String> sscan(String key, String cursor) {
        Jedis j = getShard(key);
        return j.sscan(key, cursor);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        Jedis j = getShard(key);
        return j.sscan(key, cursor, params);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Tuple> zscan(String key, String cursor) {
        Jedis j = getShard(key);
        return j.zscan(key, cursor);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        Jedis j = getShard(key);
        return j.zscan(key, cursor, params);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.dataSource != null) {
            boolean broken = false;
            Iterator<Jedis> it = getAllShards().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Jedis jedis = it.next();
                if (jedis.getClient().isBroken()) {
                    broken = true;
                    break;
                }
            }
            if (broken) {
                this.dataSource.returnBrokenResource(this);
            } else {
                this.dataSource.returnResource(this);
            }
            this.dataSource = null;
            return;
        }
        disconnect();
    }

    public void setDataSource(Pool<ShardedJedis> shardedJedisPool) {
        this.dataSource = shardedJedisPool;
    }

    public void resetState() {
        for (Jedis jedis : getAllShards()) {
            jedis.resetState();
        }
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pfadd(String key, String... elements) {
        Jedis j = getShard(key);
        return j.pfadd(key, elements);
    }

    @Override // redis.clients.jedis.JedisCommands
    public long pfcount(String key) {
        Jedis j = getShard(key);
        return j.pfcount(key);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long geoadd(String key, double longitude, double latitude, String member) {
        Jedis j = getShard(key);
        return j.geoadd(key, longitude, latitude, member);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        Jedis j = getShard(key);
        return j.geoadd(key, memberCoordinateMap);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double geodist(String key, String member1, String member2) {
        Jedis j = getShard(key);
        return j.geodist(key, member1, member2);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        Jedis j = getShard(key);
        return j.geodist(key, member1, member2, unit);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> geohash(String key, String... members) {
        Jedis j = getShard(key);
        return j.geohash(key, members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoCoordinate> geopos(String key, String... members) {
        Jedis j = getShard(key);
        return j.geopos(key, members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        Jedis j = getShard(key);
        return j.georadius(key, longitude, latitude, radius, unit);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        Jedis j = getShard(key);
        return j.georadius(key, longitude, latitude, radius, unit, param);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        Jedis j = getShard(key);
        return j.georadiusByMember(key, member, radius, unit);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        Jedis j = getShard(key);
        return j.georadiusByMember(key, member, radius, unit, param);
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<Long> bitfield(String key, String... arguments) {
        Jedis j = getShard(key);
        return j.bitfield(key, arguments);
    }
}
