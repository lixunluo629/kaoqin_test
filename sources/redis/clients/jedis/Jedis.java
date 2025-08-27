package redis.clients.jedis;

import java.net.URI;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.Pool;
import redis.clients.util.SafeEncoder;
import redis.clients.util.Slowlog;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Jedis.class */
public class Jedis extends BinaryJedis implements JedisCommands, MultiKeyCommands, AdvancedJedisCommands, ScriptingCommands, BasicCommands, ClusterCommands, SentinelCommands {
    protected Pool<Jedis> dataSource;

    public Jedis() {
        this.dataSource = null;
    }

    public Jedis(String host) {
        super(host);
        this.dataSource = null;
    }

    public Jedis(String host, int port) {
        super(host, port);
        this.dataSource = null;
    }

    public Jedis(String host, int port, boolean ssl) {
        super(host, port, ssl);
        this.dataSource = null;
    }

    public Jedis(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
        this.dataSource = null;
    }

    public Jedis(String host, int port, int timeout) {
        super(host, port, timeout);
        this.dataSource = null;
    }

    public Jedis(String host, int port, int timeout, boolean ssl) {
        super(host, port, timeout, ssl);
        this.dataSource = null;
    }

    public Jedis(String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, timeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
        this.dataSource = null;
    }

    public Jedis(String host, int port, int connectionTimeout, int soTimeout) {
        super(host, port, connectionTimeout, soTimeout);
        this.dataSource = null;
    }

    public Jedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl) {
        super(host, port, connectionTimeout, soTimeout, ssl);
        this.dataSource = null;
    }

    public Jedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, connectionTimeout, soTimeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
        this.dataSource = null;
    }

    public Jedis(JedisShardInfo shardInfo) {
        super(shardInfo);
        this.dataSource = null;
    }

    public Jedis(URI uri) {
        super(uri);
        this.dataSource = null;
    }

    public Jedis(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, sslSocketFactory, sslParameters, hostnameVerifier);
        this.dataSource = null;
    }

    public Jedis(URI uri, int timeout) {
        super(uri, timeout);
        this.dataSource = null;
    }

    public Jedis(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
        this.dataSource = null;
    }

    public Jedis(URI uri, int connectionTimeout, int soTimeout) {
        super(uri, connectionTimeout, soTimeout);
        this.dataSource = null;
    }

    public Jedis(URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(uri, connectionTimeout, soTimeout, sslSocketFactory, sslParameters, hostnameVerifier);
        this.dataSource = null;
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(String key, String value) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(String key, String value, String nxxx, String expx, long time) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value, nxxx, expx, time);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String get(String key) {
        checkIsInMultiOrPipeline();
        this.client.sendCommand(Protocol.Command.GET, key);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long exists(String... keys) {
        checkIsInMultiOrPipeline();
        this.client.exists(keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean exists(String key) {
        checkIsInMultiOrPipeline();
        this.client.exists(key);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long del(String... keys) {
        checkIsInMultiOrPipeline();
        this.client.del(keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long del(String key) {
        checkIsInMultiOrPipeline();
        this.client.del(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String type(String key) {
        checkIsInMultiOrPipeline();
        this.client.type(key);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Set<String> keys(String pattern) {
        checkIsInMultiOrPipeline();
        this.client.keys(pattern);
        return BuilderFactory.STRING_SET.build(this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public String randomKey() {
        checkIsInMultiOrPipeline();
        this.client.randomKey();
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public String rename(String oldkey, String newkey) {
        checkIsInMultiOrPipeline();
        this.client.rename(oldkey, newkey);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long renamenx(String oldkey, String newkey) {
        checkIsInMultiOrPipeline();
        this.client.renamenx(oldkey, newkey);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long expire(String key, int seconds) {
        checkIsInMultiOrPipeline();
        this.client.expire(key, seconds);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long expireAt(String key, long unixTime) {
        checkIsInMultiOrPipeline();
        this.client.expireAt(key, unixTime);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long ttl(String key) {
        checkIsInMultiOrPipeline();
        this.client.ttl(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long move(String key, int dbIndex) {
        checkIsInMultiOrPipeline();
        this.client.move(key, dbIndex);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String getSet(String key, String value) {
        checkIsInMultiOrPipeline();
        this.client.getSet(key, value);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public List<String> mget(String... keys) {
        checkIsInMultiOrPipeline();
        this.client.mget(keys);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long setnx(String key, String value) {
        checkIsInMultiOrPipeline();
        this.client.setnx(key, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String setex(String key, int seconds, String value) {
        checkIsInMultiOrPipeline();
        this.client.setex(key, seconds, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public String mset(String... keysvalues) {
        checkIsInMultiOrPipeline();
        this.client.mset(keysvalues);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long msetnx(String... keysvalues) {
        checkIsInMultiOrPipeline();
        this.client.msetnx(keysvalues);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long decrBy(String key, long decrement) {
        checkIsInMultiOrPipeline();
        this.client.decrBy(key, decrement);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long decr(String key) {
        checkIsInMultiOrPipeline();
        this.client.decr(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long incrBy(String key, long increment) {
        checkIsInMultiOrPipeline();
        this.client.incrBy(key, increment);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double incrByFloat(String key, double increment) {
        checkIsInMultiOrPipeline();
        this.client.incrByFloat(key, increment);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long incr(String key) {
        checkIsInMultiOrPipeline();
        this.client.incr(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long append(String key, String value) {
        checkIsInMultiOrPipeline();
        this.client.append(key, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String substr(String key, int start, int end) {
        checkIsInMultiOrPipeline();
        this.client.substr(key, start, end);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hset(String key, String field, String value) {
        checkIsInMultiOrPipeline();
        this.client.hset(key, field, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String hget(String key, String field) {
        checkIsInMultiOrPipeline();
        this.client.hget(key, field);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hsetnx(String key, String field, String value) {
        checkIsInMultiOrPipeline();
        this.client.hsetnx(key, field, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String hmset(String key, Map<String, String> hash) {
        checkIsInMultiOrPipeline();
        this.client.hmset(key, hash);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> hmget(String key, String... fields) {
        checkIsInMultiOrPipeline();
        this.client.hmget(key, fields);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hincrBy(String key, String field, long value) {
        checkIsInMultiOrPipeline();
        this.client.hincrBy(key, field, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double hincrByFloat(String key, String field, double value) {
        checkIsInMultiOrPipeline();
        this.client.hincrByFloat(key, field, value);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean hexists(String key, String field) {
        checkIsInMultiOrPipeline();
        this.client.hexists(key, field);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hdel(String key, String... fields) {
        checkIsInMultiOrPipeline();
        this.client.hdel(key, fields);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long hlen(String key) {
        checkIsInMultiOrPipeline();
        this.client.hlen(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> hkeys(String key) {
        checkIsInMultiOrPipeline();
        this.client.hkeys(key);
        return BuilderFactory.STRING_SET.build(this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> hvals(String key) {
        checkIsInMultiOrPipeline();
        this.client.hvals(key);
        List<String> lresult = this.client.getMultiBulkReply();
        return lresult;
    }

    @Override // redis.clients.jedis.JedisCommands
    public Map<String, String> hgetAll(String key) {
        checkIsInMultiOrPipeline();
        this.client.hgetAll(key);
        return BuilderFactory.STRING_MAP.build(this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long rpush(String key, String... strings) {
        checkIsInMultiOrPipeline();
        this.client.rpush(key, strings);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lpush(String key, String... strings) {
        checkIsInMultiOrPipeline();
        this.client.lpush(key, strings);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long llen(String key) {
        checkIsInMultiOrPipeline();
        this.client.llen(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> lrange(String key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.lrange(key, start, stop);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String ltrim(String key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.ltrim(key, start, stop);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lindex(String key, long index) {
        checkIsInMultiOrPipeline();
        this.client.lindex(key, index);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lset(String key, long index, String value) {
        checkIsInMultiOrPipeline();
        this.client.lset(key, index, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lrem(String key, long count, String value) {
        checkIsInMultiOrPipeline();
        this.client.lrem(key, count, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String lpop(String key) {
        checkIsInMultiOrPipeline();
        this.client.lpop(key);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String rpop(String key) {
        checkIsInMultiOrPipeline();
        this.client.rpop(key);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public String rpoplpush(String srckey, String dstkey) {
        checkIsInMultiOrPipeline();
        this.client.rpoplpush(srckey, dstkey);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long sadd(String key, String... members) {
        checkIsInMultiOrPipeline();
        this.client.sadd(key, members);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> smembers(String key) {
        checkIsInMultiOrPipeline();
        this.client.smembers(key);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long srem(String key, String... members) {
        checkIsInMultiOrPipeline();
        this.client.srem(key, members);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String spop(String key) {
        checkIsInMultiOrPipeline();
        this.client.spop(key);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> spop(String key, long count) {
        checkIsInMultiOrPipeline();
        this.client.spop(key, count);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long smove(String srckey, String dstkey, String member) {
        checkIsInMultiOrPipeline();
        this.client.smove(srckey, dstkey, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long scard(String key) {
        checkIsInMultiOrPipeline();
        this.client.scard(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean sismember(String key, String member) {
        checkIsInMultiOrPipeline();
        this.client.sismember(key, member);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Set<String> sinter(String... keys) {
        checkIsInMultiOrPipeline();
        this.client.sinter(keys);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long sinterstore(String dstkey, String... keys) {
        checkIsInMultiOrPipeline();
        this.client.sinterstore(dstkey, keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Set<String> sunion(String... keys) {
        checkIsInMultiOrPipeline();
        this.client.sunion(keys);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long sunionstore(String dstkey, String... keys) {
        checkIsInMultiOrPipeline();
        this.client.sunionstore(dstkey, keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Set<String> sdiff(String... keys) {
        checkIsInMultiOrPipeline();
        this.client.sdiff(keys);
        return BuilderFactory.STRING_SET.build(this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long sdiffstore(String dstkey, String... keys) {
        checkIsInMultiOrPipeline();
        this.client.sdiffstore(dstkey, keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String srandmember(String key) {
        checkIsInMultiOrPipeline();
        this.client.srandmember(key);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> srandmember(String key, int count) {
        checkIsInMultiOrPipeline();
        this.client.srandmember(key, count);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, double score, String member) {
        checkIsInMultiOrPipeline();
        this.client.zadd(key, score, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, double score, String member, ZAddParams params) {
        checkIsInMultiOrPipeline();
        this.client.zadd(key, score, member, params);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        checkIsInMultiOrPipeline();
        this.client.zadd(key, scoreMembers);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        checkIsInMultiOrPipeline();
        this.client.zadd(key, scoreMembers, params);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrange(String key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrange(key, start, stop);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrem(String key, String... members) {
        checkIsInMultiOrPipeline();
        this.client.zrem(key, members);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zincrby(String key, double increment, String member) {
        checkIsInMultiOrPipeline();
        this.client.zincrby(key, increment, member);
        return BuilderFactory.DOUBLE.build(this.client.getOne());
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zincrby(String key, double increment, String member, ZIncrByParams params) {
        checkIsInMultiOrPipeline();
        this.client.zincrby(key, increment, member, params);
        return BuilderFactory.DOUBLE.build(this.client.getOne());
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrank(String key, String member) {
        checkIsInMultiOrPipeline();
        this.client.zrank(key, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zrevrank(String key, String member) {
        checkIsInMultiOrPipeline();
        this.client.zrevrank(key, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrange(String key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrevrange(key, start, stop);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeWithScores(String key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrangeWithScores(key, start, stop);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeWithScores(String key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeWithScores(key, start, stop);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcard(String key) {
        checkIsInMultiOrPipeline();
        this.client.zcard(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double zscore(String key, String member) {
        checkIsInMultiOrPipeline();
        this.client.zscore(key, member);
        return BuilderFactory.DOUBLE.build(this.client.getOne());
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public String watch(String... keys) {
        this.client.watch(keys);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> sort(String key) {
        checkIsInMultiOrPipeline();
        this.client.sort(key);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> sort(String key, SortingParams sortingParameters) {
        checkIsInMultiOrPipeline();
        this.client.sort(key, sortingParameters);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public List<String> blpop(int timeout, String... keys) {
        return blpop(getArgsAddTimeout(timeout, keys));
    }

    private String[] getArgsAddTimeout(int timeout, String[] keys) {
        int keyCount = keys.length;
        String[] args = new String[keyCount + 1];
        for (int at = 0; at != keyCount; at++) {
            args[at] = keys[at];
        }
        args[keyCount] = String.valueOf(timeout);
        return args;
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public List<String> blpop(String... args) {
        checkIsInMultiOrPipeline();
        this.client.blpop(args);
        this.client.setTimeoutInfinite();
        try {
            return this.client.getMultiBulkReply();
        } finally {
            this.client.rollbackTimeout();
        }
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public List<String> brpop(String... args) {
        checkIsInMultiOrPipeline();
        this.client.brpop(args);
        this.client.setTimeoutInfinite();
        try {
            return this.client.getMultiBulkReply();
        } finally {
            this.client.rollbackTimeout();
        }
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public List<String> blpop(String arg) {
        return blpop(arg);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public List<String> brpop(String arg) {
        return brpop(arg);
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        checkIsInMultiOrPipeline();
        this.client.sort(key, sortingParameters, dstkey);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long sort(String key, String dstkey) {
        checkIsInMultiOrPipeline();
        this.client.sort(key, dstkey);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public List<String> brpop(int timeout, String... keys) {
        return brpop(getArgsAddTimeout(timeout, keys));
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcount(String key, double min, double max) {
        checkIsInMultiOrPipeline();
        this.client.zcount(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zcount(String key, String min, String max) {
        checkIsInMultiOrPipeline();
        this.client.zcount(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, double min, double max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScore(key, min, max);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, String min, String max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScore(key, min, max);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScore(key, min, max, offset, count);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScore(key, min, max, offset, count);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScoreWithScores(key, min, max);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScoreWithScores(key, min, max);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScoreWithScores(key, min, max, offset, count);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScoreWithScores(key, min, max, offset, count);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScore(key, max, min);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScore(key, max, min);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScore(key, max, min, offset, count);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScoreWithScores(key, max, min);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScore(key, max, min, offset, count);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScoreWithScores(key, max, min);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByRank(String key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zremrangeByRank(key, start, stop);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByScore(String key, double min, double max) {
        checkIsInMultiOrPipeline();
        this.client.zremrangeByScore(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByScore(String key, String min, String max) {
        checkIsInMultiOrPipeline();
        this.client.zremrangeByScore(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long zunionstore(String dstkey, String... sets) {
        checkIsInMultiOrPipeline();
        this.client.zunionstore(dstkey, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        checkIsInMultiOrPipeline();
        this.client.zunionstore(dstkey, params, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long zinterstore(String dstkey, String... sets) {
        checkIsInMultiOrPipeline();
        this.client.zinterstore(dstkey, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        checkIsInMultiOrPipeline();
        this.client.zinterstore(dstkey, params, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zlexcount(String key, String min, String max) {
        checkIsInMultiOrPipeline();
        this.client.zlexcount(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByLex(String key, String min, String max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByLex(key, min, max);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByLex(key, min, max, offset, count);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByLex(key, max, min);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByLex(key, max, min, offset, count);
        List<String> members = this.client.getMultiBulkReply();
        return BinaryJedis.SetFromList.of((List) members);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long zremrangeByLex(String key, String min, String max) {
        checkIsInMultiOrPipeline();
        this.client.zremrangeByLex(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long strlen(String key) {
        checkIsInMultiOrPipeline();
        this.client.strlen(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long lpushx(String key, String... string) {
        checkIsInMultiOrPipeline();
        this.client.lpushx(key, string);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long persist(String key) {
        this.client.persist(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long rpushx(String key, String... string) {
        checkIsInMultiOrPipeline();
        this.client.rpushx(key, string);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String echo(String string) {
        checkIsInMultiOrPipeline();
        this.client.echo(string);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        checkIsInMultiOrPipeline();
        this.client.linsert(key, where, pivot, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public String brpoplpush(String source, String destination, int timeout) {
        this.client.brpoplpush(source, destination, timeout);
        this.client.setTimeoutInfinite();
        try {
            String bulkReply = this.client.getBulkReply();
            this.client.rollbackTimeout();
            return bulkReply;
        } catch (Throwable th) {
            this.client.rollbackTimeout();
            throw th;
        }
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean setbit(String key, long offset, boolean value) {
        checkIsInMultiOrPipeline();
        this.client.setbit(key, offset, value);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean setbit(String key, long offset, String value) {
        checkIsInMultiOrPipeline();
        this.client.setbit(key, offset, value);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Boolean getbit(String key, long offset) {
        checkIsInMultiOrPipeline();
        this.client.getbit(key, offset);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long setrange(String key, long offset, String value) {
        checkIsInMultiOrPipeline();
        this.client.setrange(key, offset, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String getrange(String key, long startOffset, long endOffset) {
        checkIsInMultiOrPipeline();
        this.client.getrange(key, startOffset, endOffset);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitpos(String key, boolean value) {
        return bitpos(key, value, new BitPosParams());
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitpos(String key, boolean value, BitPosParams params) {
        checkIsInMultiOrPipeline();
        this.client.bitpos(key, value, params);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.AdvancedJedisCommands
    public List<String> configGet(String pattern) {
        this.client.configGet(pattern);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.AdvancedJedisCommands
    public String configSet(String parameter, String value) {
        this.client.configSet(parameter, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public Object eval(String script, int keyCount, String... params) {
        this.client.setTimeoutInfinite();
        try {
            this.client.eval(script, keyCount, params);
            Object evalResult = getEvalResult();
            this.client.rollbackTimeout();
            return evalResult;
        } catch (Throwable th) {
            this.client.rollbackTimeout();
            throw th;
        }
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        this.client.setTimeoutInfinite();
        try {
            jedisPubSub.proceed(this.client, channels);
        } finally {
            this.client.rollbackTimeout();
        }
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long publish(String channel, String message) {
        checkIsInMultiOrPipeline();
        connect();
        this.client.publish(channel, message);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        checkIsInMultiOrPipeline();
        this.client.setTimeoutInfinite();
        try {
            jedisPubSub.proceedWithPatterns(this.client, patterns);
        } finally {
            this.client.rollbackTimeout();
        }
    }

    protected static String[] getParams(List<String> keys, List<String> args) {
        int keyCount = keys.size();
        int argCount = args.size();
        String[] params = new String[keyCount + args.size()];
        for (int i = 0; i < keyCount; i++) {
            params[i] = keys.get(i);
        }
        for (int i2 = 0; i2 < argCount; i2++) {
            params[keyCount + i2] = args.get(i2);
        }
        return params;
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public Object eval(String script, List<String> keys, List<String> args) {
        return eval(script, keys.size(), getParams(keys, args));
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public Object eval(String script) {
        return eval(script, 0, new String[0]);
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public Object evalsha(String sha1) {
        return evalsha(sha1, 0, new String[0]);
    }

    private Object getEvalResult() {
        return evalResult(this.client.getOne());
    }

    private Object evalResult(Object result) {
        if (result instanceof byte[]) {
            return SafeEncoder.encode((byte[]) result);
        }
        if (result instanceof List) {
            List<?> list = (List) result;
            List<Object> listResult = new ArrayList<>(list.size());
            for (Object bin : list) {
                listResult.add(evalResult(bin));
            }
            return listResult;
        }
        return result;
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        return evalsha(sha1, keys.size(), getParams(keys, args));
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public Object evalsha(String sha1, int keyCount, String... params) {
        checkIsInMultiOrPipeline();
        this.client.evalsha(sha1, keyCount, params);
        return getEvalResult();
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public Boolean scriptExists(String sha1) {
        String[] a = {sha1};
        return scriptExists(a).get(0);
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public List<Boolean> scriptExists(String... sha1) {
        this.client.scriptExists(sha1);
        List<Long> result = this.client.getIntegerMultiBulkReply();
        List<Boolean> exists = new ArrayList<>();
        for (Long value : result) {
            exists.add(Boolean.valueOf(value.longValue() == 1));
        }
        return exists;
    }

    @Override // redis.clients.jedis.ScriptingCommands
    public String scriptLoad(String script) {
        this.client.scriptLoad(script);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.AdvancedJedisCommands
    public List<Slowlog> slowlogGet() {
        this.client.slowlogGet();
        return Slowlog.from(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.AdvancedJedisCommands
    public List<Slowlog> slowlogGet(long entries) {
        this.client.slowlogGet(entries);
        return Slowlog.from(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.AdvancedJedisCommands
    public Long objectRefcount(String key) {
        this.client.objectRefcount(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.AdvancedJedisCommands
    public String objectEncoding(String key) {
        this.client.objectEncoding(key);
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.AdvancedJedisCommands
    public Long objectIdletime(String key) {
        this.client.objectIdletime(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitcount(String key) {
        checkIsInMultiOrPipeline();
        this.client.bitcount(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long bitcount(String key, long start, long end) {
        checkIsInMultiOrPipeline();
        this.client.bitcount(key, start, end);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        checkIsInMultiOrPipeline();
        this.client.bitop(op, destKey, srcKeys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.SentinelCommands
    public List<Map<String, String>> sentinelMasters() {
        this.client.sentinel(Protocol.SENTINEL_MASTERS);
        List<Object> reply = this.client.getObjectMultiBulkReply();
        List<Map<String, String>> masters = new ArrayList<>();
        for (Object obj : reply) {
            masters.add(BuilderFactory.STRING_MAP.build((List) obj));
        }
        return masters;
    }

    @Override // redis.clients.jedis.SentinelCommands
    public List<String> sentinelGetMasterAddrByName(String masterName) {
        this.client.sentinel(Protocol.SENTINEL_GET_MASTER_ADDR_BY_NAME, masterName);
        List<Object> reply = this.client.getObjectMultiBulkReply();
        return BuilderFactory.STRING_LIST.build(reply);
    }

    @Override // redis.clients.jedis.SentinelCommands
    public Long sentinelReset(String pattern) {
        this.client.sentinel("reset", pattern);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.SentinelCommands
    public List<Map<String, String>> sentinelSlaves(String masterName) {
        this.client.sentinel("slaves", masterName);
        List<Object> reply = this.client.getObjectMultiBulkReply();
        List<Map<String, String>> slaves = new ArrayList<>();
        for (Object obj : reply) {
            slaves.add(BuilderFactory.STRING_MAP.build((List) obj));
        }
        return slaves;
    }

    @Override // redis.clients.jedis.SentinelCommands
    public String sentinelFailover(String masterName) {
        this.client.sentinel("failover", masterName);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.SentinelCommands
    public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
        this.client.sentinel(Protocol.SENTINEL_MONITOR, masterName, ip, String.valueOf(port), String.valueOf(quorum));
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.SentinelCommands
    public String sentinelRemove(String masterName) {
        this.client.sentinel(Protocol.SENTINEL_REMOVE, masterName);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.SentinelCommands
    public String sentinelSet(String masterName, Map<String, String> parameterMap) {
        int paramsLength = (parameterMap.size() * 2) + 2;
        String[] params = new String[paramsLength];
        int index = 0 + 1;
        params[0] = "set";
        int index2 = index + 1;
        params[index] = masterName;
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            int i = index2;
            int index3 = index2 + 1;
            params[i] = entry.getKey();
            index2 = index3 + 1;
            params[index3] = entry.getValue();
        }
        this.client.sentinel(params);
        return this.client.getStatusCodeReply();
    }

    public byte[] dump(String key) {
        checkIsInMultiOrPipeline();
        this.client.dump(key);
        return this.client.getBinaryBulkReply();
    }

    public String restore(String key, int ttl, byte[] serializedValue) {
        checkIsInMultiOrPipeline();
        this.client.restore(key, ttl, serializedValue);
        return this.client.getStatusCodeReply();
    }

    @Deprecated
    public Long pexpire(String key, int milliseconds) {
        return pexpire(key, milliseconds);
    }

    @Override // redis.clients.jedis.BinaryJedis, redis.clients.jedis.BinaryJedisCommands
    public Long pexpire(String key, long milliseconds) {
        checkIsInMultiOrPipeline();
        this.client.pexpire(key, milliseconds);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        checkIsInMultiOrPipeline();
        this.client.pexpireAt(key, millisecondsTimestamp);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pttl(String key) {
        checkIsInMultiOrPipeline();
        this.client.pttl(key);
        return this.client.getIntegerReply();
    }

    @Deprecated
    public String psetex(String key, int milliseconds, String value) {
        return psetex(key, milliseconds, value);
    }

    @Override // redis.clients.jedis.JedisCommands
    public String psetex(String key, long milliseconds, String value) {
        checkIsInMultiOrPipeline();
        this.client.psetex(key, milliseconds, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public String set(String key, String value, String nxxx) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value, nxxx);
        return this.client.getStatusCodeReply();
    }

    public String set(String key, String value, String nxxx, String expx, int time) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value, nxxx, expx, time);
        return this.client.getStatusCodeReply();
    }

    public String clientKill(String client) {
        checkIsInMultiOrPipeline();
        this.client.clientKill(client);
        return this.client.getStatusCodeReply();
    }

    public String clientSetname(String name) {
        checkIsInMultiOrPipeline();
        this.client.clientSetname(name);
        return this.client.getStatusCodeReply();
    }

    public String migrate(String host, int port, String key, int destinationDb, int timeout) {
        checkIsInMultiOrPipeline();
        this.client.migrate(host, port, key, destinationDb, timeout);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    @Deprecated
    public ScanResult<String> scan(int cursor) {
        return scan(cursor, new ScanParams());
    }

    @Deprecated
    public ScanResult<String> scan(int cursor, ScanParams params) throws NumberFormatException {
        checkIsInMultiOrPipeline();
        this.client.scan(cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        int newcursor = Integer.parseInt(new String((byte[]) result.get(0)));
        List<String> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        for (byte[] bs : rawResults) {
            results.add(SafeEncoder.encode(bs));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
        return hscan(key, cursor, new ScanParams());
    }

    @Deprecated
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor, ScanParams params) throws NumberFormatException {
        checkIsInMultiOrPipeline();
        this.client.hscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        int newcursor = Integer.parseInt(new String((byte[]) result.get(0)));
        List<Map.Entry<String, String>> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new AbstractMap.SimpleEntry<>(SafeEncoder.encode(iterator.next()), SafeEncoder.encode(iterator.next())));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<String> sscan(String key, int cursor) {
        return sscan(key, cursor, new ScanParams());
    }

    @Deprecated
    public ScanResult<String> sscan(String key, int cursor, ScanParams params) throws NumberFormatException {
        checkIsInMultiOrPipeline();
        this.client.sscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        int newcursor = Integer.parseInt(new String((byte[]) result.get(0)));
        List<String> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        for (byte[] bs : rawResults) {
            results.add(SafeEncoder.encode(bs));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.JedisCommands
    @Deprecated
    public ScanResult<Tuple> zscan(String key, int cursor) {
        return zscan(key, cursor, new ScanParams());
    }

    @Deprecated
    public ScanResult<Tuple> zscan(String key, int cursor, ScanParams params) throws NumberFormatException {
        checkIsInMultiOrPipeline();
        this.client.zscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        int newcursor = Integer.parseInt(new String((byte[]) result.get(0)));
        List<Tuple> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new Tuple(SafeEncoder.encode(iterator.next()), Double.valueOf(SafeEncoder.encode(iterator.next()))));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public ScanResult<String> scan(String cursor) {
        return scan(cursor, new ScanParams());
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public ScanResult<String> scan(String cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.scan(cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<String> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        for (byte[] bs : rawResults) {
            results.add(SafeEncoder.encode(bs));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        return hscan(key, cursor, new ScanParams());
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.hscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<Map.Entry<String, String>> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new AbstractMap.SimpleEntry<>(SafeEncoder.encode(iterator.next()), SafeEncoder.encode(iterator.next())));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<String> sscan(String key, String cursor) {
        return sscan(key, cursor, new ScanParams());
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.sscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<String> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        for (byte[] bs : rawResults) {
            results.add(SafeEncoder.encode(bs));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Tuple> zscan(String key, String cursor) {
        return zscan(key, cursor, new ScanParams());
    }

    @Override // redis.clients.jedis.JedisCommands
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.zscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        String newcursor = new String((byte[]) result.get(0));
        List<Tuple> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterNodes() {
        checkIsInMultiOrPipeline();
        this.client.clusterNodes();
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String readonly() {
        this.client.readonly();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterMeet(String ip, int port) {
        checkIsInMultiOrPipeline();
        this.client.clusterMeet(ip, port);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterReset(JedisCluster.Reset resetType) {
        checkIsInMultiOrPipeline();
        this.client.clusterReset(resetType);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterAddSlots(int... slots) {
        checkIsInMultiOrPipeline();
        this.client.clusterAddSlots(slots);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterDelSlots(int... slots) {
        checkIsInMultiOrPipeline();
        this.client.clusterDelSlots(slots);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterInfo() {
        checkIsInMultiOrPipeline();
        this.client.clusterInfo();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public List<String> clusterGetKeysInSlot(int slot, int count) {
        checkIsInMultiOrPipeline();
        this.client.clusterGetKeysInSlot(slot, count);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterSetSlotNode(int slot, String nodeId) {
        checkIsInMultiOrPipeline();
        this.client.clusterSetSlotNode(slot, nodeId);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterSetSlotMigrating(int slot, String nodeId) {
        checkIsInMultiOrPipeline();
        this.client.clusterSetSlotMigrating(slot, nodeId);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterSetSlotImporting(int slot, String nodeId) {
        checkIsInMultiOrPipeline();
        this.client.clusterSetSlotImporting(slot, nodeId);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterSetSlotStable(int slot) {
        checkIsInMultiOrPipeline();
        this.client.clusterSetSlotStable(slot);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterForget(String nodeId) {
        checkIsInMultiOrPipeline();
        this.client.clusterForget(nodeId);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterFlushSlots() {
        checkIsInMultiOrPipeline();
        this.client.clusterFlushSlots();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public Long clusterKeySlot(String key) {
        checkIsInMultiOrPipeline();
        this.client.clusterKeySlot(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public Long clusterCountKeysInSlot(int slot) {
        checkIsInMultiOrPipeline();
        this.client.clusterCountKeysInSlot(slot);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterSaveConfig() {
        checkIsInMultiOrPipeline();
        this.client.clusterSaveConfig();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterReplicate(String nodeId) {
        checkIsInMultiOrPipeline();
        this.client.clusterReplicate(nodeId);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public List<String> clusterSlaves(String nodeId) {
        checkIsInMultiOrPipeline();
        this.client.clusterSlaves(nodeId);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public String clusterFailover() {
        checkIsInMultiOrPipeline();
        this.client.clusterFailover();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.ClusterCommands
    public List<Object> clusterSlots() {
        checkIsInMultiOrPipeline();
        this.client.clusterSlots();
        return this.client.getObjectMultiBulkReply();
    }

    public String asking() {
        checkIsInMultiOrPipeline();
        this.client.asking();
        return this.client.getStatusCodeReply();
    }

    public List<String> pubsubChannels(String pattern) {
        checkIsInMultiOrPipeline();
        this.client.pubsubChannels(pattern);
        return this.client.getMultiBulkReply();
    }

    public Long pubsubNumPat() {
        checkIsInMultiOrPipeline();
        this.client.pubsubNumPat();
        return this.client.getIntegerReply();
    }

    public Map<String, String> pubsubNumSub(String... channels) {
        checkIsInMultiOrPipeline();
        this.client.pubsubNumSub(channels);
        return BuilderFactory.PUBSUB_NUMSUB_MAP.build(this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedis, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.dataSource != null) {
            Pool<Jedis> pool = this.dataSource;
            this.dataSource = null;
            if (this.client.isBroken()) {
                pool.returnBrokenResource(this);
                return;
            } else {
                pool.returnResource(this);
                return;
            }
        }
        super.close();
    }

    public void setDataSource(Pool<Jedis> jedisPool) {
        this.dataSource = jedisPool;
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long pfadd(String key, String... elements) {
        checkIsInMultiOrPipeline();
        this.client.pfadd(key, elements);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public long pfcount(String key) {
        checkIsInMultiOrPipeline();
        this.client.pfcount(key);
        return this.client.getIntegerReply().longValue();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public long pfcount(String... keys) {
        checkIsInMultiOrPipeline();
        this.client.pfcount(keys);
        return this.client.getIntegerReply().longValue();
    }

    @Override // redis.clients.jedis.MultiKeyCommands
    public String pfmerge(String destkey, String... sourcekeys) {
        checkIsInMultiOrPipeline();
        this.client.pfmerge(destkey, sourcekeys);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> blpop(int timeout, String key) {
        return blpop(key, String.valueOf(timeout));
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> brpop(int timeout, String key) {
        return brpop(key, String.valueOf(timeout));
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long geoadd(String key, double longitude, double latitude, String member) {
        checkIsInMultiOrPipeline();
        this.client.geoadd(key, longitude, latitude, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        checkIsInMultiOrPipeline();
        this.client.geoadd(key, memberCoordinateMap);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double geodist(String key, String member1, String member2) {
        checkIsInMultiOrPipeline();
        this.client.geodist(key, member1, member2);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.JedisCommands
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        checkIsInMultiOrPipeline();
        this.client.geodist(key, member1, member2, unit);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<String> geohash(String key, String... members) {
        checkIsInMultiOrPipeline();
        this.client.geohash(key, members);
        return this.client.getMultiBulkReply();
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoCoordinate> geopos(String key, String... members) {
        checkIsInMultiOrPipeline();
        this.client.geopos(key, members);
        return BuilderFactory.GEO_COORDINATE_LIST.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        checkIsInMultiOrPipeline();
        this.client.georadius(key, longitude, latitude, radius, unit);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        checkIsInMultiOrPipeline();
        this.client.georadius(key, longitude, latitude, radius, unit, param);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        checkIsInMultiOrPipeline();
        this.client.georadiusByMember(key, member, radius, unit);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        checkIsInMultiOrPipeline();
        this.client.georadiusByMember(key, member, radius, unit, param);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.JedisCommands
    public List<Long> bitfield(String key, String... arguments) {
        checkIsInMultiOrPipeline();
        this.client.bitfield(key, arguments);
        return this.client.getIntegerMultiBulkReply();
    }
}
