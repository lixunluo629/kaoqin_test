package redis.clients.jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Client.class */
public class Client extends BinaryClient implements Commands {
    public Client() {
    }

    public Client(String host) {
        super(host);
    }

    public Client(String host, int port) {
        super(host, port);
    }

    public Client(String host, int port, boolean ssl) {
        super(host, port, ssl);
    }

    public Client(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    @Override // redis.clients.jedis.Commands
    public void set(String key, String value) {
        set(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void set(String key, String value, String nxxx, String expx, long time) {
        set(SafeEncoder.encode(key), SafeEncoder.encode(value), SafeEncoder.encode(nxxx), SafeEncoder.encode(expx), time);
    }

    @Override // redis.clients.jedis.Commands
    public void get(String key) {
        get(SafeEncoder.encode(key));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.Commands
    public void exists(String key) {
        exists((byte[][]) new byte[]{SafeEncoder.encode(key)});
    }

    @Override // redis.clients.jedis.Commands
    public void exists(String... keys) {
        exists(SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void del(String... keys) {
        del(SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void type(String key) {
        type(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void keys(String pattern) {
        keys(SafeEncoder.encode(pattern));
    }

    @Override // redis.clients.jedis.Commands
    public void rename(String oldkey, String newkey) {
        rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
    }

    @Override // redis.clients.jedis.Commands
    public void renamenx(String oldkey, String newkey) {
        renamenx(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
    }

    @Override // redis.clients.jedis.Commands
    public void expire(String key, int seconds) {
        expire(SafeEncoder.encode(key), seconds);
    }

    @Override // redis.clients.jedis.Commands
    public void expireAt(String key, long unixTime) {
        expireAt(SafeEncoder.encode(key), unixTime);
    }

    @Override // redis.clients.jedis.Commands
    public void ttl(String key) {
        ttl(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void move(String key, int dbIndex) {
        move(SafeEncoder.encode(key), dbIndex);
    }

    @Override // redis.clients.jedis.Commands
    public void getSet(String key, String value) {
        getSet(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void mget(String... keys) {
        mget(SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void setnx(String key, String value) {
        setnx(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void setex(String key, int seconds, String value) {
        setex(SafeEncoder.encode(key), seconds, SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void mset(String... keysvalues) {
        mset(SafeEncoder.encodeMany(keysvalues));
    }

    @Override // redis.clients.jedis.Commands
    public void msetnx(String... keysvalues) {
        msetnx(SafeEncoder.encodeMany(keysvalues));
    }

    @Override // redis.clients.jedis.Commands
    public void decrBy(String key, long decrement) {
        decrBy(SafeEncoder.encode(key), decrement);
    }

    @Override // redis.clients.jedis.Commands
    public void decr(String key) {
        decr(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void incrBy(String key, long increment) {
        incrBy(SafeEncoder.encode(key), increment);
    }

    @Override // redis.clients.jedis.Commands
    public void incr(String key) {
        incr(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void append(String key, String value) {
        append(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void substr(String key, int start, int end) {
        substr(SafeEncoder.encode(key), start, end);
    }

    @Override // redis.clients.jedis.Commands
    public void hset(String key, String field, String value) {
        hset(SafeEncoder.encode(key), SafeEncoder.encode(field), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void hget(String key, String field) {
        hget(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }

    @Override // redis.clients.jedis.Commands
    public void hsetnx(String key, String field, String value) {
        hsetnx(SafeEncoder.encode(key), SafeEncoder.encode(field), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void hmset(String key, Map<String, String> hash) {
        Map<byte[], byte[]> bhash = new HashMap<>(hash.size());
        for (Map.Entry<String, String> entry : hash.entrySet()) {
            bhash.put(SafeEncoder.encode(entry.getKey()), SafeEncoder.encode(entry.getValue()));
        }
        hmset(SafeEncoder.encode(key), bhash);
    }

    @Override // redis.clients.jedis.Commands
    public void hmget(String key, String... fields) {
        hmget(SafeEncoder.encode(key), SafeEncoder.encodeMany(fields));
    }

    @Override // redis.clients.jedis.Commands
    public void hincrBy(String key, String field, long value) {
        hincrBy(SafeEncoder.encode(key), SafeEncoder.encode(field), value);
    }

    @Override // redis.clients.jedis.Commands
    public void hexists(String key, String field) {
        hexists(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }

    @Override // redis.clients.jedis.Commands
    public void hdel(String key, String... fields) {
        hdel(SafeEncoder.encode(key), SafeEncoder.encodeMany(fields));
    }

    @Override // redis.clients.jedis.Commands
    public void hlen(String key) {
        hlen(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void hkeys(String key) {
        hkeys(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void hvals(String key) {
        hvals(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void hgetAll(String key) {
        hgetAll(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void rpush(String key, String... string) {
        rpush(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }

    @Override // redis.clients.jedis.Commands
    public void lpush(String key, String... string) {
        lpush(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }

    @Override // redis.clients.jedis.Commands
    public void llen(String key) {
        llen(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void lrange(String key, long start, long stop) {
        lrange(SafeEncoder.encode(key), start, stop);
    }

    @Override // redis.clients.jedis.Commands
    public void ltrim(String key, long start, long stop) {
        ltrim(SafeEncoder.encode(key), start, stop);
    }

    @Override // redis.clients.jedis.Commands
    public void lindex(String key, long index) {
        lindex(SafeEncoder.encode(key), index);
    }

    @Override // redis.clients.jedis.Commands
    public void lset(String key, long index, String value) {
        lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void lrem(String key, long count, String value) {
        lrem(SafeEncoder.encode(key), count, SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void lpop(String key) {
        lpop(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void rpop(String key) {
        rpop(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void rpoplpush(String srckey, String dstkey) {
        rpoplpush(SafeEncoder.encode(srckey), SafeEncoder.encode(dstkey));
    }

    @Override // redis.clients.jedis.Commands
    public void sadd(String key, String... members) {
        sadd(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    @Override // redis.clients.jedis.Commands
    public void smembers(String key) {
        smembers(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void srem(String key, String... members) {
        srem(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    @Override // redis.clients.jedis.Commands
    public void spop(String key) {
        spop(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void spop(String key, long count) {
        spop(SafeEncoder.encode(key), count);
    }

    @Override // redis.clients.jedis.Commands
    public void smove(String srckey, String dstkey, String member) {
        smove(SafeEncoder.encode(srckey), SafeEncoder.encode(dstkey), SafeEncoder.encode(member));
    }

    @Override // redis.clients.jedis.Commands
    public void scard(String key) {
        scard(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void sismember(String key, String member) {
        sismember(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    @Override // redis.clients.jedis.Commands
    public void sinter(String... keys) {
        sinter(SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void sinterstore(String dstkey, String... keys) {
        sinterstore(SafeEncoder.encode(dstkey), SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void sunion(String... keys) {
        sunion(SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void sunionstore(String dstkey, String... keys) {
        sunionstore(SafeEncoder.encode(dstkey), SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void sdiff(String... keys) {
        sdiff(SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void sdiffstore(String dstkey, String... keys) {
        sdiffstore(SafeEncoder.encode(dstkey), SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void srandmember(String key) {
        srandmember(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void zadd(String key, double score, String member) {
        zadd(SafeEncoder.encode(key), score, SafeEncoder.encode(member));
    }

    @Override // redis.clients.jedis.Commands
    public void zadd(String key, double score, String member, ZAddParams params) {
        zadd(SafeEncoder.encode(key), score, SafeEncoder.encode(member), params);
    }

    @Override // redis.clients.jedis.Commands
    public void zadd(String key, Map<String, Double> scoreMembers) {
        HashMap<byte[], Double> binaryScoreMembers = convertScoreMembersToBinary(scoreMembers);
        zaddBinary(SafeEncoder.encode(key), binaryScoreMembers);
    }

    @Override // redis.clients.jedis.Commands
    public void zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        HashMap<byte[], Double> binaryScoreMembers = convertScoreMembersToBinary(scoreMembers);
        zaddBinary(SafeEncoder.encode(key), binaryScoreMembers, params);
    }

    @Override // redis.clients.jedis.Commands
    public void zrange(String key, long start, long stop) {
        zrange(SafeEncoder.encode(key), start, stop);
    }

    @Override // redis.clients.jedis.Commands
    public void zrem(String key, String... members) {
        zrem(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    @Override // redis.clients.jedis.Commands
    public void zincrby(String key, double increment, String member) {
        zincrby(SafeEncoder.encode(key), increment, SafeEncoder.encode(member));
    }

    @Override // redis.clients.jedis.Commands
    public void zincrby(String key, double increment, String member, ZIncrByParams params) {
        zincrby(SafeEncoder.encode(key), increment, SafeEncoder.encode(member), params);
    }

    @Override // redis.clients.jedis.Commands
    public void zrank(String key, String member) {
        zrank(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrank(String key, String member) {
        zrevrank(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrange(String key, long start, long stop) {
        zrevrange(SafeEncoder.encode(key), start, stop);
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeWithScores(String key, long start, long stop) {
        zrangeWithScores(SafeEncoder.encode(key), start, stop);
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeWithScores(String key, long start, long stop) {
        zrevrangeWithScores(SafeEncoder.encode(key), start, stop);
    }

    @Override // redis.clients.jedis.Commands
    public void zcard(String key) {
        zcard(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void zscore(String key, String member) {
        zscore(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    @Override // redis.clients.jedis.Commands
    public void watch(String... keys) {
        watch(SafeEncoder.encodeMany(keys));
    }

    @Override // redis.clients.jedis.Commands
    public void sort(String key) {
        sort(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void sort(String key, SortingParams sortingParameters) {
        sort(SafeEncoder.encode(key), sortingParameters);
    }

    @Override // redis.clients.jedis.Commands
    public void blpop(String[] args) {
        blpop(SafeEncoder.encodeMany(args));
    }

    public void blpop(int timeout, String... keys) {
        int size = keys.length + 1;
        List<String> args = new ArrayList<>(size);
        for (String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));
        blpop((String[]) args.toArray(new String[size]));
    }

    @Override // redis.clients.jedis.Commands
    public void sort(String key, SortingParams sortingParameters, String dstkey) {
        sort(SafeEncoder.encode(key), sortingParameters, SafeEncoder.encode(dstkey));
    }

    @Override // redis.clients.jedis.Commands
    public void sort(String key, String dstkey) {
        sort(SafeEncoder.encode(key), SafeEncoder.encode(dstkey));
    }

    @Override // redis.clients.jedis.Commands
    public void brpop(String[] args) {
        brpop(SafeEncoder.encodeMany(args));
    }

    public void brpop(int timeout, String... keys) {
        int size = keys.length + 1;
        List<String> args = new ArrayList<>(size);
        for (String arg : keys) {
            args.add(arg);
        }
        args.add(String.valueOf(timeout));
        brpop((String[]) args.toArray(new String[size]));
    }

    @Override // redis.clients.jedis.Commands
    public void zcount(String key, double min, double max) {
        zcount(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zcount(String key, String min, String max) {
        zcount(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScore(String key, double min, double max) {
        zrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScore(String key, String min, String max) {
        zrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScore(String key, double min, double max, int offset, int count) {
        zrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScoreWithScores(String key, double min, double max) {
        zrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        zrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScore(String key, double max, double min) {
        zrevrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min));
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScore(String key, String min, String max, int offset, int count) {
        zrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScoreWithScores(String key, String min, String max) {
        zrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        zrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScore(String key, String max, String min) {
        zrevrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min));
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScore(String key, double max, double min, int offset, int count) {
        zrevrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScore(String key, String max, String min, int offset, int count) {
        zrevrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScoreWithScores(String key, double max, double min) {
        zrevrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min));
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScoreWithScores(String key, String max, String min) {
        zrevrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min));
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        zrevrangeByScoreWithScores(SafeEncoder.encode(key), Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        zrevrangeByScoreWithScores(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min), offset, count);
    }

    @Override // redis.clients.jedis.Commands
    public void zremrangeByRank(String key, long start, long stop) {
        zremrangeByRank(SafeEncoder.encode(key), start, stop);
    }

    @Override // redis.clients.jedis.Commands
    public void zremrangeByScore(String key, double min, double max) {
        zremrangeByScore(SafeEncoder.encode(key), Protocol.toByteArray(min), Protocol.toByteArray(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zremrangeByScore(String key, String min, String max) {
        zremrangeByScore(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    @Override // redis.clients.jedis.Commands
    public void zunionstore(String dstkey, String... sets) {
        zunionstore(SafeEncoder.encode(dstkey), SafeEncoder.encodeMany(sets));
    }

    @Override // redis.clients.jedis.Commands
    public void zunionstore(String dstkey, ZParams params, String... sets) {
        zunionstore(SafeEncoder.encode(dstkey), params, SafeEncoder.encodeMany(sets));
    }

    @Override // redis.clients.jedis.Commands
    public void zinterstore(String dstkey, String... sets) {
        zinterstore(SafeEncoder.encode(dstkey), SafeEncoder.encodeMany(sets));
    }

    @Override // redis.clients.jedis.Commands
    public void zinterstore(String dstkey, ZParams params, String... sets) {
        zinterstore(SafeEncoder.encode(dstkey), params, SafeEncoder.encodeMany(sets));
    }

    public void zlexcount(String key, String min, String max) {
        zlexcount(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    public void zrangeByLex(String key, String min, String max) {
        zrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    public void zrangeByLex(String key, String min, String max, int offset, int count) {
        zrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max), offset, count);
    }

    public void zrevrangeByLex(String key, String max, String min) {
        zrevrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min));
    }

    public void zrevrangeByLex(String key, String max, String min, int offset, int count) {
        zrevrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(max), SafeEncoder.encode(min), offset, count);
    }

    public void zremrangeByLex(String key, String min, String max) {
        zremrangeByLex(SafeEncoder.encode(key), SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    @Override // redis.clients.jedis.Commands
    public void strlen(String key) {
        strlen(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void lpushx(String key, String... string) {
        lpushx(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }

    @Override // redis.clients.jedis.Commands
    public void persist(String key) {
        persist(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void rpushx(String key, String... string) {
        rpushx(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }

    @Override // redis.clients.jedis.Commands
    public void echo(String string) {
        echo(SafeEncoder.encode(string));
    }

    @Override // redis.clients.jedis.Commands
    public void linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        linsert(SafeEncoder.encode(key), where, SafeEncoder.encode(pivot), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void brpoplpush(String source, String destination, int timeout) {
        brpoplpush(SafeEncoder.encode(source), SafeEncoder.encode(destination), timeout);
    }

    @Override // redis.clients.jedis.Commands
    public void setbit(String key, long offset, boolean value) {
        setbit(SafeEncoder.encode(key), offset, value);
    }

    @Override // redis.clients.jedis.Commands
    public void setbit(String key, long offset, String value) {
        setbit(SafeEncoder.encode(key), offset, SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void getbit(String key, long offset) {
        getbit(SafeEncoder.encode(key), offset);
    }

    public void bitpos(String key, boolean value, BitPosParams params) {
        bitpos(SafeEncoder.encode(key), value, params);
    }

    @Override // redis.clients.jedis.Commands
    public void setrange(String key, long offset, String value) {
        setrange(SafeEncoder.encode(key), offset, SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void getrange(String key, long startOffset, long endOffset) {
        getrange(SafeEncoder.encode(key), startOffset, endOffset);
    }

    public void publish(String channel, String message) {
        publish(SafeEncoder.encode(channel), SafeEncoder.encode(message));
    }

    public void unsubscribe(String... channels) {
        unsubscribe(SafeEncoder.encodeMany(channels));
    }

    public void psubscribe(String... patterns) {
        psubscribe(SafeEncoder.encodeMany(patterns));
    }

    public void punsubscribe(String... patterns) {
        punsubscribe(SafeEncoder.encodeMany(patterns));
    }

    public void subscribe(String... channels) {
        subscribe(SafeEncoder.encodeMany(channels));
    }

    public void pubsubChannels(String pattern) {
        pubsub(Protocol.PUBSUB_CHANNELS, pattern);
    }

    public void pubsubNumPat() {
        pubsub(Protocol.PUBSUB_NUM_PAT, new String[0]);
    }

    public void pubsubNumSub(String... channels) {
        pubsub(Protocol.PUBSUB_NUMSUB, channels);
    }

    @Override // redis.clients.jedis.Commands
    public void configSet(String parameter, String value) {
        configSet(SafeEncoder.encode(parameter), SafeEncoder.encode(value));
    }

    @Override // redis.clients.jedis.Commands
    public void configGet(String pattern) {
        configGet(SafeEncoder.encode(pattern));
    }

    public void eval(String script, int keyCount, String... params) {
        eval(SafeEncoder.encode(script), Protocol.toByteArray(keyCount), SafeEncoder.encodeMany(params));
    }

    public void evalsha(String sha1, int keyCount, String... params) {
        evalsha(SafeEncoder.encode(sha1), Protocol.toByteArray(keyCount), SafeEncoder.encodeMany(params));
    }

    public void scriptExists(String... sha1) {
        scriptExists(SafeEncoder.encodeMany(sha1));
    }

    public void scriptLoad(String script) {
        scriptLoad(SafeEncoder.encode(script));
    }

    @Override // redis.clients.jedis.Commands
    public void objectRefcount(String key) {
        objectRefcount(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void objectIdletime(String key) {
        objectIdletime(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void objectEncoding(String key) {
        objectEncoding(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void bitcount(String key) {
        bitcount(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void bitcount(String key, long start, long end) {
        bitcount(SafeEncoder.encode(key), start, end);
    }

    @Override // redis.clients.jedis.Commands
    public void bitop(BitOP op, String destKey, String... srcKeys) {
        bitop(op, SafeEncoder.encode(destKey), SafeEncoder.encodeMany(srcKeys));
    }

    public void sentinel(String... args) {
        sentinel(SafeEncoder.encodeMany(args));
    }

    public void dump(String key) {
        dump(SafeEncoder.encode(key));
    }

    public void restore(String key, int ttl, byte[] serializedValue) {
        restore(SafeEncoder.encode(key), ttl, serializedValue);
    }

    @Deprecated
    public void pexpire(String key, int milliseconds) {
        pexpire(key, milliseconds);
    }

    public void pexpire(String key, long milliseconds) {
        pexpire(SafeEncoder.encode(key), milliseconds);
    }

    public void pexpireAt(String key, long millisecondsTimestamp) {
        pexpireAt(SafeEncoder.encode(key), millisecondsTimestamp);
    }

    @Override // redis.clients.jedis.Commands
    public void pttl(String key) {
        pttl(SafeEncoder.encode(key));
    }

    @Override // redis.clients.jedis.Commands
    public void incrByFloat(String key, double increment) {
        incrByFloat(SafeEncoder.encode(key), increment);
    }

    @Deprecated
    public void psetex(String key, int milliseconds, String value) {
        psetex(key, milliseconds, value);
    }

    public void psetex(String key, long milliseconds, String value) {
        psetex(SafeEncoder.encode(key), milliseconds, SafeEncoder.encode(value));
    }

    public void set(String key, String value, String nxxx) {
        set(SafeEncoder.encode(key), SafeEncoder.encode(value), SafeEncoder.encode(nxxx));
    }

    public void set(String key, String value, String nxxx, String expx, int time) {
        set(SafeEncoder.encode(key), SafeEncoder.encode(value), SafeEncoder.encode(nxxx), SafeEncoder.encode(expx), time);
    }

    public void srandmember(String key, int count) {
        srandmember(SafeEncoder.encode(key), count);
    }

    public void clientKill(String client) {
        clientKill(SafeEncoder.encode(client));
    }

    public void clientSetname(String name) {
        clientSetname(SafeEncoder.encode(name));
    }

    public void migrate(String host, int port, String key, int destinationDb, int timeout) {
        migrate(SafeEncoder.encode(host), port, SafeEncoder.encode(key), destinationDb, timeout);
    }

    @Override // redis.clients.jedis.Commands
    public void hincrByFloat(String key, String field, double increment) {
        hincrByFloat(SafeEncoder.encode(key), SafeEncoder.encode(field), increment);
    }

    @Override // redis.clients.jedis.Commands
    @Deprecated
    public void hscan(String key, int cursor, ScanParams params) {
        hscan(SafeEncoder.encode(key), cursor, params);
    }

    @Override // redis.clients.jedis.Commands
    @Deprecated
    public void sscan(String key, int cursor, ScanParams params) {
        sscan(SafeEncoder.encode(key), cursor, params);
    }

    @Override // redis.clients.jedis.Commands
    @Deprecated
    public void zscan(String key, int cursor, ScanParams params) {
        zscan(SafeEncoder.encode(key), cursor, params);
    }

    @Override // redis.clients.jedis.Commands
    public void scan(String cursor, ScanParams params) {
        scan(SafeEncoder.encode(cursor), params);
    }

    @Override // redis.clients.jedis.Commands
    public void hscan(String key, String cursor, ScanParams params) {
        hscan(SafeEncoder.encode(key), SafeEncoder.encode(cursor), params);
    }

    @Override // redis.clients.jedis.Commands
    public void sscan(String key, String cursor, ScanParams params) {
        sscan(SafeEncoder.encode(key), SafeEncoder.encode(cursor), params);
    }

    @Override // redis.clients.jedis.Commands
    public void zscan(String key, String cursor, ScanParams params) {
        zscan(SafeEncoder.encode(key), SafeEncoder.encode(cursor), params);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    public void cluster(String subcommand, int... args) {
        ?? r0 = new byte[args.length + 1];
        for (int i = 1; i < r0.length; i++) {
            r0[i] = Protocol.toByteArray(args[i - 1]);
        }
        r0[0] = SafeEncoder.encode(subcommand);
        cluster((byte[][]) r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    public void pubsub(String subcommand, String... args) {
        ?? r0 = new byte[args.length + 1];
        for (int i = 1; i < r0.length; i++) {
            r0[i] = SafeEncoder.encode(args[i - 1]);
        }
        r0[0] = SafeEncoder.encode(subcommand);
        pubsub(r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    public void cluster(String subcommand, String... args) {
        ?? r0 = new byte[args.length + 1];
        for (int i = 1; i < r0.length; i++) {
            r0[i] = SafeEncoder.encode(args[i - 1]);
        }
        r0[0] = SafeEncoder.encode(subcommand);
        cluster((byte[][]) r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    public void cluster(String subcommand) {
        cluster((byte[][]) new byte[]{SafeEncoder.encode(subcommand)});
    }

    public void clusterNodes() {
        cluster(Protocol.CLUSTER_NODES);
    }

    public void clusterMeet(String ip, int port) {
        cluster(Protocol.CLUSTER_MEET, ip, String.valueOf(port));
    }

    public void clusterReset(JedisCluster.Reset resetType) {
        cluster("reset", resetType.toString());
    }

    public void clusterAddSlots(int... slots) {
        cluster(Protocol.CLUSTER_ADDSLOTS, slots);
    }

    public void clusterDelSlots(int... slots) {
        cluster(Protocol.CLUSTER_DELSLOTS, slots);
    }

    public void clusterInfo() {
        cluster(Protocol.CLUSTER_INFO);
    }

    public void clusterGetKeysInSlot(int slot, int count) {
        int[] args = {slot, count};
        cluster(Protocol.CLUSTER_GETKEYSINSLOT, args);
    }

    public void clusterSetSlotNode(int slot, String nodeId) {
        cluster(Protocol.CLUSTER_SETSLOT, String.valueOf(slot), Protocol.CLUSTER_SETSLOT_NODE, nodeId);
    }

    public void clusterSetSlotMigrating(int slot, String nodeId) {
        cluster(Protocol.CLUSTER_SETSLOT, String.valueOf(slot), Protocol.CLUSTER_SETSLOT_MIGRATING, nodeId);
    }

    public void clusterSetSlotImporting(int slot, String nodeId) {
        cluster(Protocol.CLUSTER_SETSLOT, String.valueOf(slot), Protocol.CLUSTER_SETSLOT_IMPORTING, nodeId);
    }

    public void pfadd(String key, String... elements) {
        pfadd(SafeEncoder.encode(key), SafeEncoder.encodeMany(elements));
    }

    public void pfcount(String key) {
        pfcount(SafeEncoder.encode(key));
    }

    public void pfcount(String... keys) {
        pfcount(SafeEncoder.encodeMany(keys));
    }

    public void pfmerge(String destkey, String... sourcekeys) {
        pfmerge(SafeEncoder.encode(destkey), SafeEncoder.encodeMany(sourcekeys));
    }

    public void clusterSetSlotStable(int slot) {
        cluster(Protocol.CLUSTER_SETSLOT, String.valueOf(slot), Protocol.CLUSTER_SETSLOT_STABLE);
    }

    public void clusterForget(String nodeId) {
        cluster(Protocol.CLUSTER_FORGET, nodeId);
    }

    public void clusterFlushSlots() {
        cluster(Protocol.CLUSTER_FLUSHSLOT);
    }

    public void clusterKeySlot(String key) {
        cluster(Protocol.CLUSTER_KEYSLOT, key);
    }

    public void clusterCountKeysInSlot(int slot) {
        cluster(Protocol.CLUSTER_COUNTKEYINSLOT, String.valueOf(slot));
    }

    public void clusterSaveConfig() {
        cluster(Protocol.CLUSTER_SAVECONFIG);
    }

    public void clusterReplicate(String nodeId) {
        cluster(Protocol.CLUSTER_REPLICATE, nodeId);
    }

    public void clusterSlaves(String nodeId) {
        cluster("slaves", nodeId);
    }

    public void clusterFailover() {
        cluster("failover");
    }

    public void clusterSlots() {
        cluster(Protocol.CLUSTER_SLOTS);
    }

    public void geoadd(String key, double longitude, double latitude, String member) {
        geoadd(SafeEncoder.encode(key), longitude, latitude, SafeEncoder.encode(member));
    }

    public void geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        geoadd(SafeEncoder.encode(key), convertMemberCoordinateMapToBinary(memberCoordinateMap));
    }

    public void geodist(String key, String member1, String member2) {
        geodist(SafeEncoder.encode(key), SafeEncoder.encode(member1), SafeEncoder.encode(member2));
    }

    public void geodist(String key, String member1, String member2, GeoUnit unit) {
        geodist(SafeEncoder.encode(key), SafeEncoder.encode(member1), SafeEncoder.encode(member2), unit);
    }

    public void geohash(String key, String... members) {
        geohash(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    public void geopos(String key, String[] members) {
        geopos(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    public void georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        georadius(SafeEncoder.encode(key), longitude, latitude, radius, unit);
    }

    public void georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        georadius(SafeEncoder.encode(key), longitude, latitude, radius, unit, param);
    }

    public void georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        georadiusByMember(SafeEncoder.encode(key), SafeEncoder.encode(member), radius, unit);
    }

    public void georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        georadiusByMember(SafeEncoder.encode(key), SafeEncoder.encode(member), radius, unit, param);
    }

    private HashMap<byte[], Double> convertScoreMembersToBinary(Map<String, Double> scoreMembers) {
        HashMap<byte[], Double> binaryScoreMembers = new HashMap<>();
        for (Map.Entry<String, Double> entry : scoreMembers.entrySet()) {
            binaryScoreMembers.put(SafeEncoder.encode(entry.getKey()), entry.getValue());
        }
        return binaryScoreMembers;
    }

    private HashMap<byte[], GeoCoordinate> convertMemberCoordinateMapToBinary(Map<String, GeoCoordinate> memberCoordinateMap) {
        HashMap<byte[], GeoCoordinate> binaryMemberCoordinateMap = new HashMap<>();
        for (Map.Entry<String, GeoCoordinate> entry : memberCoordinateMap.entrySet()) {
            binaryMemberCoordinateMap.put(SafeEncoder.encode(entry.getKey()), entry.getValue());
        }
        return binaryMemberCoordinateMap;
    }

    @Override // redis.clients.jedis.Commands
    public void bitfield(String key, String... arguments) {
        bitfield(SafeEncoder.encode(key), SafeEncoder.encodeMany(arguments));
    }
}
