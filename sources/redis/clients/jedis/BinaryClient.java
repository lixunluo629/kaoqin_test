package redis.clients.jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryClient.class */
public class BinaryClient extends Connection {
    private boolean isInMulti;
    private String password;
    private long db;
    private boolean isInWatch;

    /* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryClient$LIST_POSITION.class */
    public enum LIST_POSITION {
        BEFORE,
        AFTER;

        public final byte[] raw = SafeEncoder.encode(name());

        LIST_POSITION() {
        }
    }

    public BinaryClient() {
    }

    public BinaryClient(String host) {
        super(host);
    }

    public BinaryClient(String host, int port) {
        super(host, port);
    }

    public BinaryClient(String host, int port, boolean ssl) {
        super(host, port, ssl);
    }

    public BinaryClient(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        super(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public boolean isInMulti() {
        return this.isInMulti;
    }

    public boolean isInWatch() {
        return this.isInWatch;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][], java.lang.Object] */
    private byte[][] joinParameters(byte[] first, byte[][] rest) {
        ?? r0 = new byte[rest.length + 1];
        r0[0] = first;
        System.arraycopy(rest, 0, r0, 1, rest.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][], java.lang.Object] */
    private byte[][] joinParameters(byte[] first, byte[] second, byte[][] rest) {
        ?? r0 = new byte[rest.length + 2];
        r0[0] = first;
        r0[1] = second;
        System.arraycopy(rest, 0, r0, 2, rest.length);
        return r0;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDb(long db) {
        this.db = db;
    }

    @Override // redis.clients.jedis.Connection
    public void connect() {
        if (!isConnected()) {
            super.connect();
            if (this.password != null) {
                auth(this.password);
                getStatusCodeReply();
            }
            if (this.db > 0) {
                select((int) this.db);
                getStatusCodeReply();
            }
        }
    }

    public void ping() {
        sendCommand(Protocol.Command.PING);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void set(byte[] key, byte[] value) {
        sendCommand(Protocol.Command.SET, (byte[][]) new byte[]{key, value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        sendCommand(Protocol.Command.SET, (byte[][]) new byte[]{key, value, nxxx, expx, Protocol.toByteArray(time)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void get(byte[] key) {
        sendCommand(Protocol.Command.GET, (byte[][]) new byte[]{key});
    }

    public void quit() {
        this.db = 0L;
        sendCommand(Protocol.Command.QUIT);
    }

    public void exists(byte[]... key) {
        sendCommand(Protocol.Command.EXISTS, key);
    }

    public void del(byte[]... keys) {
        sendCommand(Protocol.Command.DEL, keys);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void type(byte[] key) {
        sendCommand(Protocol.Command.TYPE, (byte[][]) new byte[]{key});
    }

    public void flushDB() {
        sendCommand(Protocol.Command.FLUSHDB);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void keys(byte[] pattern) {
        sendCommand(Protocol.Command.KEYS, (byte[][]) new byte[]{pattern});
    }

    public void randomKey() {
        sendCommand(Protocol.Command.RANDOMKEY);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void rename(byte[] oldkey, byte[] newkey) {
        sendCommand(Protocol.Command.RENAME, (byte[][]) new byte[]{oldkey, newkey});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void renamenx(byte[] oldkey, byte[] newkey) {
        sendCommand(Protocol.Command.RENAMENX, (byte[][]) new byte[]{oldkey, newkey});
    }

    public void dbSize() {
        sendCommand(Protocol.Command.DBSIZE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void expire(byte[] key, int seconds) {
        sendCommand(Protocol.Command.EXPIRE, (byte[][]) new byte[]{key, Protocol.toByteArray(seconds)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void expireAt(byte[] key, long unixTime) {
        sendCommand(Protocol.Command.EXPIREAT, (byte[][]) new byte[]{key, Protocol.toByteArray(unixTime)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void ttl(byte[] key) {
        sendCommand(Protocol.Command.TTL, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void select(int index) {
        sendCommand(Protocol.Command.SELECT, (byte[][]) new byte[]{Protocol.toByteArray(index)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void move(byte[] key, int dbIndex) {
        sendCommand(Protocol.Command.MOVE, (byte[][]) new byte[]{key, Protocol.toByteArray(dbIndex)});
    }

    public void flushAll() {
        sendCommand(Protocol.Command.FLUSHALL);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void getSet(byte[] key, byte[] value) {
        sendCommand(Protocol.Command.GETSET, (byte[][]) new byte[]{key, value});
    }

    public void mget(byte[]... keys) {
        sendCommand(Protocol.Command.MGET, keys);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void setnx(byte[] key, byte[] value) {
        sendCommand(Protocol.Command.SETNX, (byte[][]) new byte[]{key, value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void setex(byte[] key, int seconds, byte[] value) {
        sendCommand(Protocol.Command.SETEX, (byte[][]) new byte[]{key, Protocol.toByteArray(seconds), value});
    }

    public void mset(byte[]... keysvalues) {
        sendCommand(Protocol.Command.MSET, keysvalues);
    }

    public void msetnx(byte[]... keysvalues) {
        sendCommand(Protocol.Command.MSETNX, keysvalues);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void decrBy(byte[] key, long decrement) {
        sendCommand(Protocol.Command.DECRBY, (byte[][]) new byte[]{key, Protocol.toByteArray(decrement)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void decr(byte[] key) {
        sendCommand(Protocol.Command.DECR, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void incrBy(byte[] key, long increment) {
        sendCommand(Protocol.Command.INCRBY, (byte[][]) new byte[]{key, Protocol.toByteArray(increment)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void incrByFloat(byte[] key, double increment) {
        sendCommand(Protocol.Command.INCRBYFLOAT, (byte[][]) new byte[]{key, Protocol.toByteArray(increment)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void incr(byte[] key) {
        sendCommand(Protocol.Command.INCR, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void append(byte[] key, byte[] value) {
        sendCommand(Protocol.Command.APPEND, (byte[][]) new byte[]{key, value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void substr(byte[] key, int start, int end) {
        sendCommand(Protocol.Command.SUBSTR, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(end)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hset(byte[] key, byte[] field, byte[] value) {
        sendCommand(Protocol.Command.HSET, (byte[][]) new byte[]{key, field, value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hget(byte[] key, byte[] field) {
        sendCommand(Protocol.Command.HGET, (byte[][]) new byte[]{key, field});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hsetnx(byte[] key, byte[] field, byte[] value) {
        sendCommand(Protocol.Command.HSETNX, (byte[][]) new byte[]{key, field, value});
    }

    public void hmset(byte[] key, Map<byte[], byte[]> hash) {
        List<byte[]> params = new ArrayList<>();
        params.add(key);
        for (Map.Entry<byte[], byte[]> entry : hash.entrySet()) {
            params.add(entry.getKey());
            params.add(entry.getValue());
        }
        sendCommand(Protocol.Command.HMSET, (byte[][]) params.toArray((Object[]) new byte[params.size()]));
    }

    public void hmget(byte[] key, byte[]... fields) {
        sendCommand(Protocol.Command.HMGET, joinParameters(key, fields));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hincrBy(byte[] key, byte[] field, long value) {
        sendCommand(Protocol.Command.HINCRBY, (byte[][]) new byte[]{key, field, Protocol.toByteArray(value)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hexists(byte[] key, byte[] field) {
        sendCommand(Protocol.Command.HEXISTS, (byte[][]) new byte[]{key, field});
    }

    public void hdel(byte[] key, byte[]... fields) {
        sendCommand(Protocol.Command.HDEL, joinParameters(key, fields));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hlen(byte[] key) {
        sendCommand(Protocol.Command.HLEN, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hkeys(byte[] key) {
        sendCommand(Protocol.Command.HKEYS, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hvals(byte[] key) {
        sendCommand(Protocol.Command.HVALS, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hgetAll(byte[] key) {
        sendCommand(Protocol.Command.HGETALL, (byte[][]) new byte[]{key});
    }

    public void rpush(byte[] key, byte[]... strings) {
        sendCommand(Protocol.Command.RPUSH, joinParameters(key, strings));
    }

    public void lpush(byte[] key, byte[]... strings) {
        sendCommand(Protocol.Command.LPUSH, joinParameters(key, strings));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void llen(byte[] key) {
        sendCommand(Protocol.Command.LLEN, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void lrange(byte[] key, long start, long stop) {
        sendCommand(Protocol.Command.LRANGE, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(stop)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void ltrim(byte[] key, long start, long stop) {
        sendCommand(Protocol.Command.LTRIM, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(stop)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void lindex(byte[] key, long index) {
        sendCommand(Protocol.Command.LINDEX, (byte[][]) new byte[]{key, Protocol.toByteArray(index)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void lset(byte[] key, long index, byte[] value) {
        sendCommand(Protocol.Command.LSET, (byte[][]) new byte[]{key, Protocol.toByteArray(index), value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void lrem(byte[] key, long count, byte[] value) {
        sendCommand(Protocol.Command.LREM, (byte[][]) new byte[]{key, Protocol.toByteArray(count), value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void lpop(byte[] key) {
        sendCommand(Protocol.Command.LPOP, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void rpop(byte[] key) {
        sendCommand(Protocol.Command.RPOP, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void rpoplpush(byte[] srckey, byte[] dstkey) {
        sendCommand(Protocol.Command.RPOPLPUSH, (byte[][]) new byte[]{srckey, dstkey});
    }

    public void sadd(byte[] key, byte[]... members) {
        sendCommand(Protocol.Command.SADD, joinParameters(key, members));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void smembers(byte[] key) {
        sendCommand(Protocol.Command.SMEMBERS, (byte[][]) new byte[]{key});
    }

    public void srem(byte[] key, byte[]... members) {
        sendCommand(Protocol.Command.SREM, joinParameters(key, members));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void spop(byte[] key) {
        sendCommand(Protocol.Command.SPOP, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void spop(byte[] key, long count) {
        sendCommand(Protocol.Command.SPOP, (byte[][]) new byte[]{key, Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void smove(byte[] srckey, byte[] dstkey, byte[] member) {
        sendCommand(Protocol.Command.SMOVE, (byte[][]) new byte[]{srckey, dstkey, member});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void scard(byte[] key) {
        sendCommand(Protocol.Command.SCARD, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void sismember(byte[] key, byte[] member) {
        sendCommand(Protocol.Command.SISMEMBER, (byte[][]) new byte[]{key, member});
    }

    public void sinter(byte[]... keys) {
        sendCommand(Protocol.Command.SINTER, keys);
    }

    public void sinterstore(byte[] dstkey, byte[]... keys) {
        sendCommand(Protocol.Command.SINTERSTORE, joinParameters(dstkey, keys));
    }

    public void sunion(byte[]... keys) {
        sendCommand(Protocol.Command.SUNION, keys);
    }

    public void sunionstore(byte[] dstkey, byte[]... keys) {
        sendCommand(Protocol.Command.SUNIONSTORE, joinParameters(dstkey, keys));
    }

    public void sdiff(byte[]... keys) {
        sendCommand(Protocol.Command.SDIFF, keys);
    }

    public void sdiffstore(byte[] dstkey, byte[]... keys) {
        sendCommand(Protocol.Command.SDIFFSTORE, joinParameters(dstkey, keys));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void srandmember(byte[] key) {
        sendCommand(Protocol.Command.SRANDMEMBER, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zadd(byte[] key, double score, byte[] member) {
        sendCommand(Protocol.Command.ZADD, (byte[][]) new byte[]{key, Protocol.toByteArray(score), member});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [byte[], byte[][]] */
    public void zadd(byte[] key, double score, byte[] member, ZAddParams zAddParams) {
        sendCommand(Protocol.Command.ZADD, zAddParams.getByteParams(key, new byte[]{Protocol.toByteArray(score), member}));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [byte[], byte[][], java.lang.Object[]] */
    public void zaddBinary(byte[] key, Map<byte[], Double> scoreMembers) {
        ArrayList<byte[]> args = new ArrayList<>((scoreMembers.size() * 2) + 1);
        args.add(key);
        args.addAll(convertScoreMembersToByteArrays(scoreMembers));
        ?? r0 = new byte[args.size()];
        args.toArray((Object[]) r0);
        sendCommand(Protocol.Command.ZADD, (byte[][]) r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [byte[], byte[][], java.lang.Object[]] */
    public void zaddBinary(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams zAddParams) {
        ArrayList<byte[]> args = convertScoreMembersToByteArrays(scoreMembers);
        ?? r0 = new byte[args.size()];
        args.toArray((Object[]) r0);
        sendCommand(Protocol.Command.ZADD, zAddParams.getByteParams(key, r0));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrange(byte[] key, long start, long stop) {
        sendCommand(Protocol.Command.ZRANGE, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(stop)});
    }

    public void zrem(byte[] key, byte[]... members) {
        sendCommand(Protocol.Command.ZREM, joinParameters(key, members));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zincrby(byte[] key, double increment, byte[] member) {
        sendCommand(Protocol.Command.ZINCRBY, (byte[][]) new byte[]{key, Protocol.toByteArray(increment), member});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [byte[], byte[][]] */
    public void zincrby(byte[] key, double increment, byte[] member, ZIncrByParams zIncrByParams) {
        sendCommand(Protocol.Command.ZADD, zIncrByParams.getByteParams(key, new byte[]{Protocol.toByteArray(increment), member}));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrank(byte[] key, byte[] member) {
        sendCommand(Protocol.Command.ZRANK, (byte[][]) new byte[]{key, member});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrank(byte[] key, byte[] member) {
        sendCommand(Protocol.Command.ZREVRANK, (byte[][]) new byte[]{key, member});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrange(byte[] key, long start, long stop) {
        sendCommand(Protocol.Command.ZREVRANGE, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(stop)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeWithScores(byte[] key, long start, long stop) {
        sendCommand(Protocol.Command.ZRANGE, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(stop), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeWithScores(byte[] key, long start, long stop) {
        sendCommand(Protocol.Command.ZREVRANGE, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(stop), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zcard(byte[] key) {
        sendCommand(Protocol.Command.ZCARD, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zscore(byte[] key, byte[] member) {
        sendCommand(Protocol.Command.ZSCORE, (byte[][]) new byte[]{key, member});
    }

    public void multi() {
        sendCommand(Protocol.Command.MULTI);
        this.isInMulti = true;
    }

    public void discard() {
        sendCommand(Protocol.Command.DISCARD);
        this.isInMulti = false;
        this.isInWatch = false;
    }

    public void exec() {
        sendCommand(Protocol.Command.EXEC);
        this.isInMulti = false;
        this.isInWatch = false;
    }

    public void watch(byte[]... keys) {
        sendCommand(Protocol.Command.WATCH, keys);
        this.isInWatch = true;
    }

    public void unwatch() {
        sendCommand(Protocol.Command.UNWATCH);
        this.isInWatch = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void sort(byte[] key) {
        sendCommand(Protocol.Command.SORT, (byte[][]) new byte[]{key});
    }

    public void sort(byte[] key, SortingParams sortingParameters) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        sendCommand(Protocol.Command.SORT, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void blpop(byte[][] args) {
        sendCommand(Protocol.Command.BLPOP, args);
    }

    public void blpop(int timeout, byte[]... keys) {
        List<byte[]> args = new ArrayList<>();
        for (byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));
        blpop((byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.addAll(sortingParameters.getParams());
        args.add(Protocol.Keyword.STORE.raw);
        args.add(dstkey);
        sendCommand(Protocol.Command.SORT, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void sort(byte[] key, byte[] dstkey) {
        sendCommand(Protocol.Command.SORT, (byte[][]) new byte[]{key, Protocol.Keyword.STORE.raw, dstkey});
    }

    public void brpop(byte[][] args) {
        sendCommand(Protocol.Command.BRPOP, args);
    }

    public void brpop(int timeout, byte[]... keys) {
        List<byte[]> args = new ArrayList<>();
        for (byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));
        brpop((byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void auth(String password) {
        setPassword(password);
        sendCommand(Protocol.Command.AUTH, password);
    }

    public void subscribe(byte[]... channels) {
        sendCommand(Protocol.Command.SUBSCRIBE, channels);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void publish(byte[] channel, byte[] message) {
        sendCommand(Protocol.Command.PUBLISH, (byte[][]) new byte[]{channel, message});
    }

    public void unsubscribe() {
        sendCommand(Protocol.Command.UNSUBSCRIBE);
    }

    public void unsubscribe(byte[]... channels) {
        sendCommand(Protocol.Command.UNSUBSCRIBE, channels);
    }

    public void psubscribe(byte[]... patterns) {
        sendCommand(Protocol.Command.PSUBSCRIBE, patterns);
    }

    public void punsubscribe() {
        sendCommand(Protocol.Command.PUNSUBSCRIBE);
    }

    public void punsubscribe(byte[]... patterns) {
        sendCommand(Protocol.Command.PUNSUBSCRIBE, patterns);
    }

    public void pubsub(byte[]... args) {
        sendCommand(Protocol.Command.PUBSUB, args);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zcount(byte[] key, double min, double max) {
        sendCommand(Protocol.Command.ZCOUNT, (byte[][]) new byte[]{key, Protocol.toByteArray(min), Protocol.toByteArray(max)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zcount(byte[] key, byte[] min, byte[] max) {
        sendCommand(Protocol.Command.ZCOUNT, (byte[][]) new byte[]{key, min, max});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zcount(byte[] key, String min, String max) {
        sendCommand(Protocol.Command.ZCOUNT, (byte[][]) new byte[]{key, min.getBytes(), max.getBytes()});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScore(byte[] key, double min, double max) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(min), Protocol.toByteArray(max)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScore(byte[] key, byte[] min, byte[] max) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min, max});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScore(byte[] key, String min, String max) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min.getBytes(), max.getBytes()});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScore(byte[] key, double max, double min) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(max), Protocol.toByteArray(min)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max, min});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScore(byte[] key, String max, String min) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max.getBytes(), min.getBytes()});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(min), Protocol.toByteArray(max), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScore(byte[] key, String min, String max, int offset, int count) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min.getBytes(), max.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(max), Protocol.toByteArray(min), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScore(byte[] key, String max, String min, int offset, int count) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max.getBytes(), min.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScoreWithScores(byte[] key, double min, double max) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(min), Protocol.toByteArray(max), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScoreWithScores(byte[] key, String min, String max) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min.getBytes(), max.getBytes(), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(max), Protocol.toByteArray(min), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScoreWithScores(byte[] key, String max, String min) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max.getBytes(), min.getBytes(), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(min), Protocol.toByteArray(max), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScoreWithScores(byte[] key, String min, String max, int offset, int count) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min.getBytes(), max.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, Protocol.toByteArray(max), Protocol.toByteArray(min), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScoreWithScores(byte[] key, String max, String min, int offset, int count) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max.getBytes(), min.getBytes(), Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min, max, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max, min, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min, max, Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max, min, Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        sendCommand(Protocol.Command.ZRANGEBYSCORE, (byte[][]) new byte[]{key, min, max, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        sendCommand(Protocol.Command.ZREVRANGEBYSCORE, (byte[][]) new byte[]{key, max, min, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count), Protocol.Keyword.WITHSCORES.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zremrangeByRank(byte[] key, long start, long stop) {
        sendCommand(Protocol.Command.ZREMRANGEBYRANK, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(stop)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zremrangeByScore(byte[] key, byte[] min, byte[] max) {
        sendCommand(Protocol.Command.ZREMRANGEBYSCORE, (byte[][]) new byte[]{key, min, max});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zremrangeByScore(byte[] key, String min, String max) {
        sendCommand(Protocol.Command.ZREMRANGEBYSCORE, (byte[][]) new byte[]{key, min.getBytes(), max.getBytes()});
    }

    public void zunionstore(byte[] dstkey, byte[]... sets) {
        sendCommand(Protocol.Command.ZUNIONSTORE, joinParameters(dstkey, Protocol.toByteArray(sets.length), sets));
    }

    public void zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        List<byte[]> args = new ArrayList<>();
        args.add(dstkey);
        args.add(Protocol.toByteArray(sets.length));
        for (byte[] set : sets) {
            args.add(set);
        }
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.ZUNIONSTORE, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void zinterstore(byte[] dstkey, byte[]... sets) {
        sendCommand(Protocol.Command.ZINTERSTORE, joinParameters(dstkey, Protocol.toByteArray(sets.length), sets));
    }

    public void zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        List<byte[]> args = new ArrayList<>();
        args.add(dstkey);
        args.add(Protocol.toByteArray(sets.length));
        for (byte[] set : sets) {
            args.add(set);
        }
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.ZINTERSTORE, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zlexcount(byte[] key, byte[] min, byte[] max) {
        sendCommand(Protocol.Command.ZLEXCOUNT, (byte[][]) new byte[]{key, min, max});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByLex(byte[] key, byte[] min, byte[] max) {
        sendCommand(Protocol.Command.ZRANGEBYLEX, (byte[][]) new byte[]{key, min, max});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        sendCommand(Protocol.Command.ZRANGEBYLEX, (byte[][]) new byte[]{key, min, max, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        sendCommand(Protocol.Command.ZREVRANGEBYLEX, (byte[][]) new byte[]{key, max, min});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        sendCommand(Protocol.Command.ZREVRANGEBYLEX, (byte[][]) new byte[]{key, max, min, Protocol.Keyword.LIMIT.raw, Protocol.toByteArray(offset), Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        sendCommand(Protocol.Command.ZREMRANGEBYLEX, (byte[][]) new byte[]{key, min, max});
    }

    public void save() {
        sendCommand(Protocol.Command.SAVE);
    }

    public void bgsave() {
        sendCommand(Protocol.Command.BGSAVE);
    }

    public void bgrewriteaof() {
        sendCommand(Protocol.Command.BGREWRITEAOF);
    }

    public void lastsave() {
        sendCommand(Protocol.Command.LASTSAVE);
    }

    public void shutdown() {
        sendCommand(Protocol.Command.SHUTDOWN);
    }

    public void info() {
        sendCommand(Protocol.Command.INFO);
    }

    public void info(String section) {
        sendCommand(Protocol.Command.INFO, section);
    }

    public void monitor() {
        sendCommand(Protocol.Command.MONITOR);
    }

    public void slaveof(String host, int port) {
        sendCommand(Protocol.Command.SLAVEOF, host, String.valueOf(port));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void slaveofNoOne() {
        sendCommand(Protocol.Command.SLAVEOF, (byte[][]) new byte[]{Protocol.Keyword.NO.raw, Protocol.Keyword.ONE.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void configGet(byte[] pattern) {
        sendCommand(Protocol.Command.CONFIG, (byte[][]) new byte[]{Protocol.Keyword.GET.raw, pattern});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void configSet(byte[] parameter, byte[] value) {
        sendCommand(Protocol.Command.CONFIG, (byte[][]) new byte[]{Protocol.Keyword.SET.raw, parameter, value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void strlen(byte[] key) {
        sendCommand(Protocol.Command.STRLEN, (byte[][]) new byte[]{key});
    }

    public void sync() {
        sendCommand(Protocol.Command.SYNC);
    }

    public void lpushx(byte[] key, byte[]... string) {
        sendCommand(Protocol.Command.LPUSHX, joinParameters(key, string));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void persist(byte[] key) {
        sendCommand(Protocol.Command.PERSIST, (byte[][]) new byte[]{key});
    }

    public void rpushx(byte[] key, byte[]... string) {
        sendCommand(Protocol.Command.RPUSHX, joinParameters(key, string));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void echo(byte[] string) {
        sendCommand(Protocol.Command.ECHO, (byte[][]) new byte[]{string});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
        sendCommand(Protocol.Command.LINSERT, (byte[][]) new byte[]{key, where.raw, pivot, value});
    }

    public void debug(DebugParams params) {
        sendCommand(Protocol.Command.DEBUG, params.getCommand());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void brpoplpush(byte[] source, byte[] destination, int timeout) {
        sendCommand(Protocol.Command.BRPOPLPUSH, (byte[][]) new byte[]{source, destination, Protocol.toByteArray(timeout)});
    }

    public void configResetStat() {
        sendCommand(Protocol.Command.CONFIG, Protocol.Keyword.RESETSTAT.name());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void setbit(byte[] key, long offset, byte[] value) {
        sendCommand(Protocol.Command.SETBIT, (byte[][]) new byte[]{key, Protocol.toByteArray(offset), value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void setbit(byte[] key, long offset, boolean value) {
        sendCommand(Protocol.Command.SETBIT, (byte[][]) new byte[]{key, Protocol.toByteArray(offset), Protocol.toByteArray(value)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void getbit(byte[] key, long offset) {
        sendCommand(Protocol.Command.GETBIT, (byte[][]) new byte[]{key, Protocol.toByteArray(offset)});
    }

    public void bitpos(byte[] key, boolean value, BitPosParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.add(Protocol.toByteArray(value));
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.BITPOS, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void setrange(byte[] key, long offset, byte[] value) {
        sendCommand(Protocol.Command.SETRANGE, (byte[][]) new byte[]{key, Protocol.toByteArray(offset), value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void getrange(byte[] key, long startOffset, long endOffset) {
        sendCommand(Protocol.Command.GETRANGE, (byte[][]) new byte[]{key, Protocol.toByteArray(startOffset), Protocol.toByteArray(endOffset)});
    }

    public Long getDB() {
        return Long.valueOf(this.db);
    }

    @Override // redis.clients.jedis.Connection
    public void disconnect() {
        this.db = 0L;
        super.disconnect();
    }

    @Override // redis.clients.jedis.Connection, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.db = 0L;
        super.close();
    }

    public void resetState() {
        if (isInMulti()) {
            discard();
        }
        if (isInWatch()) {
            unwatch();
            getStatusCodeReply();
        }
    }

    public void eval(byte[] script, byte[] keyCount, byte[][] params) {
        sendCommand(Protocol.Command.EVAL, joinParameters(script, keyCount, params));
    }

    public void eval(byte[] script, int keyCount, byte[]... params) {
        sendCommand(Protocol.Command.EVAL, joinParameters(script, Protocol.toByteArray(keyCount), params));
    }

    public void evalsha(byte[] sha1, byte[] keyCount, byte[]... params) {
        sendCommand(Protocol.Command.EVALSHA, joinParameters(sha1, keyCount, params));
    }

    public void evalsha(byte[] sha1, int keyCount, byte[]... params) {
        sendCommand(Protocol.Command.EVALSHA, joinParameters(sha1, Protocol.toByteArray(keyCount), params));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void scriptFlush() {
        sendCommand(Protocol.Command.SCRIPT, (byte[][]) new byte[]{Protocol.Keyword.FLUSH.raw});
    }

    public void scriptExists(byte[]... sha1) {
        sendCommand(Protocol.Command.SCRIPT, joinParameters(Protocol.Keyword.EXISTS.raw, sha1));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void scriptLoad(byte[] script) {
        sendCommand(Protocol.Command.SCRIPT, (byte[][]) new byte[]{Protocol.Keyword.LOAD.raw, script});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void scriptKill() {
        sendCommand(Protocol.Command.SCRIPT, (byte[][]) new byte[]{Protocol.Keyword.KILL.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void slowlogGet() {
        sendCommand(Protocol.Command.SLOWLOG, (byte[][]) new byte[]{Protocol.Keyword.GET.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void slowlogGet(long entries) {
        sendCommand(Protocol.Command.SLOWLOG, (byte[][]) new byte[]{Protocol.Keyword.GET.raw, Protocol.toByteArray(entries)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void slowlogReset() {
        sendCommand(Protocol.Command.SLOWLOG, (byte[][]) new byte[]{Protocol.Keyword.RESET.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void slowlogLen() {
        sendCommand(Protocol.Command.SLOWLOG, (byte[][]) new byte[]{Protocol.Keyword.LEN.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void objectRefcount(byte[] key) {
        sendCommand(Protocol.Command.OBJECT, (byte[][]) new byte[]{Protocol.Keyword.REFCOUNT.raw, key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void objectIdletime(byte[] key) {
        sendCommand(Protocol.Command.OBJECT, (byte[][]) new byte[]{Protocol.Keyword.IDLETIME.raw, key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void objectEncoding(byte[] key) {
        sendCommand(Protocol.Command.OBJECT, (byte[][]) new byte[]{Protocol.Keyword.ENCODING.raw, key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void bitcount(byte[] key) {
        sendCommand(Protocol.Command.BITCOUNT, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void bitcount(byte[] key, long start, long end) {
        sendCommand(Protocol.Command.BITCOUNT, (byte[][]) new byte[]{key, Protocol.toByteArray(start), Protocol.toByteArray(end)});
    }

    public void bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        Protocol.Keyword kw = Protocol.Keyword.AND;
        switch (op) {
            case AND:
                kw = Protocol.Keyword.AND;
                break;
            case OR:
                kw = Protocol.Keyword.OR;
                break;
            case XOR:
                kw = Protocol.Keyword.XOR;
                break;
            case NOT:
                kw = Protocol.Keyword.NOT;
                break;
        }
        sendCommand(Protocol.Command.BITOP, joinParameters(kw.raw, destKey, srcKeys));
    }

    public void sentinel(byte[]... args) {
        sendCommand(Protocol.Command.SENTINEL, args);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void dump(byte[] key) {
        sendCommand(Protocol.Command.DUMP, (byte[][]) new byte[]{key});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void restore(byte[] key, int ttl, byte[] serializedValue) {
        sendCommand(Protocol.Command.RESTORE, (byte[][]) new byte[]{key, Protocol.toByteArray(ttl), serializedValue});
    }

    @Deprecated
    public void pexpire(byte[] key, int milliseconds) {
        pexpire(key, milliseconds);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void pexpire(byte[] key, long milliseconds) {
        sendCommand(Protocol.Command.PEXPIRE, (byte[][]) new byte[]{key, Protocol.toByteArray(milliseconds)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void pexpireAt(byte[] key, long millisecondsTimestamp) {
        sendCommand(Protocol.Command.PEXPIREAT, (byte[][]) new byte[]{key, Protocol.toByteArray(millisecondsTimestamp)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void pttl(byte[] key) {
        sendCommand(Protocol.Command.PTTL, (byte[][]) new byte[]{key});
    }

    @Deprecated
    public void psetex(byte[] key, int milliseconds, byte[] value) {
        psetex(key, milliseconds, value);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void psetex(byte[] key, long milliseconds, byte[] value) {
        sendCommand(Protocol.Command.PSETEX, (byte[][]) new byte[]{key, Protocol.toByteArray(milliseconds), value});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void set(byte[] key, byte[] value, byte[] nxxx) {
        sendCommand(Protocol.Command.SET, (byte[][]) new byte[]{key, value, nxxx});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        sendCommand(Protocol.Command.SET, (byte[][]) new byte[]{key, value, nxxx, expx, Protocol.toByteArray(time)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void srandmember(byte[] key, int count) {
        sendCommand(Protocol.Command.SRANDMEMBER, (byte[][]) new byte[]{key, Protocol.toByteArray(count)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void clientKill(byte[] client) {
        sendCommand(Protocol.Command.CLIENT, (byte[][]) new byte[]{Protocol.Keyword.KILL.raw, client});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void clientGetname() {
        sendCommand(Protocol.Command.CLIENT, (byte[][]) new byte[]{Protocol.Keyword.GETNAME.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void clientList() {
        sendCommand(Protocol.Command.CLIENT, (byte[][]) new byte[]{Protocol.Keyword.LIST.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void clientSetname(byte[] name) {
        sendCommand(Protocol.Command.CLIENT, (byte[][]) new byte[]{Protocol.Keyword.SETNAME.raw, name});
    }

    public void time() {
        sendCommand(Protocol.Command.TIME);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void migrate(byte[] host, int port, byte[] key, int destinationDb, int timeout) {
        sendCommand(Protocol.Command.MIGRATE, (byte[][]) new byte[]{host, Protocol.toByteArray(port), key, Protocol.toByteArray(destinationDb), Protocol.toByteArray(timeout)});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void hincrByFloat(byte[] key, byte[] field, double increment) {
        sendCommand(Protocol.Command.HINCRBYFLOAT, (byte[][]) new byte[]{key, field, Protocol.toByteArray(increment)});
    }

    @Deprecated
    public void scan(int cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.SCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    @Deprecated
    public void hscan(byte[] key, int cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.HSCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    @Deprecated
    public void sscan(byte[] key, int cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.SSCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    @Deprecated
    public void zscan(byte[] key, int cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.add(Protocol.toByteArray(cursor));
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.ZSCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void scan(byte[] cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(cursor);
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.SCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void hscan(byte[] key, byte[] cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.add(cursor);
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.HSCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void sscan(byte[] key, byte[] cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.add(cursor);
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.SSCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    public void zscan(byte[] key, byte[] cursor, ScanParams params) {
        List<byte[]> args = new ArrayList<>();
        args.add(key);
        args.add(cursor);
        args.addAll(params.getParams());
        sendCommand(Protocol.Command.ZSCAN, (byte[][]) args.toArray((Object[]) new byte[args.size()]));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void waitReplicas(int replicas, long timeout) {
        sendCommand(Protocol.Command.WAIT, (byte[][]) new byte[]{Protocol.toByteArray(replicas), Protocol.toByteArray(timeout)});
    }

    public void cluster(byte[]... args) {
        sendCommand(Protocol.Command.CLUSTER, args);
    }

    public void asking() {
        sendCommand(Protocol.Command.ASKING);
    }

    public void pfadd(byte[] key, byte[]... elements) {
        sendCommand(Protocol.Command.PFADD, joinParameters(key, elements));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void pfcount(byte[] key) {
        sendCommand(Protocol.Command.PFCOUNT, (byte[][]) new byte[]{key});
    }

    public void pfcount(byte[]... keys) {
        sendCommand(Protocol.Command.PFCOUNT, keys);
    }

    public void pfmerge(byte[] destkey, byte[]... sourcekeys) {
        sendCommand(Protocol.Command.PFMERGE, joinParameters(destkey, sourcekeys));
    }

    public void readonly() {
        sendCommand(Protocol.Command.READONLY);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        sendCommand(Protocol.Command.GEOADD, (byte[][]) new byte[]{key, Protocol.toByteArray(longitude), Protocol.toByteArray(latitude), member});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [byte[], byte[][], java.lang.Object[]] */
    public void geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        List<byte[]> args = new ArrayList<>((memberCoordinateMap.size() * 3) + 1);
        args.add(key);
        args.addAll(convertGeoCoordinateMapToByteArrays(memberCoordinateMap));
        ?? r0 = new byte[args.size()];
        args.toArray((Object[]) r0);
        sendCommand(Protocol.Command.GEOADD, (byte[][]) r0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void geodist(byte[] key, byte[] member1, byte[] member2) {
        sendCommand(Protocol.Command.GEODIST, (byte[][]) new byte[]{key, member1, member2});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        sendCommand(Protocol.Command.GEODIST, (byte[][]) new byte[]{key, member1, member2, unit.raw});
    }

    public void geohash(byte[] key, byte[]... members) {
        sendCommand(Protocol.Command.GEOHASH, joinParameters(key, members));
    }

    public void geopos(byte[] key, byte[][] members) {
        sendCommand(Protocol.Command.GEOPOS, joinParameters(key, members));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        sendCommand(Protocol.Command.GEORADIUS, (byte[][]) new byte[]{key, Protocol.toByteArray(longitude), Protocol.toByteArray(latitude), Protocol.toByteArray(radius), unit.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    public void georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam geoRadiusParam) {
        sendCommand(Protocol.Command.GEORADIUS, geoRadiusParam.getByteParams(new byte[]{key, Protocol.toByteArray(longitude), Protocol.toByteArray(latitude), Protocol.toByteArray(radius), unit.raw}));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [byte[], byte[][]] */
    public void georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        sendCommand(Protocol.Command.GEORADIUSBYMEMBER, (byte[][]) new byte[]{key, member, Protocol.toByteArray(radius), unit.raw});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    public void georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam geoRadiusParam) {
        sendCommand(Protocol.Command.GEORADIUSBYMEMBER, geoRadiusParam.getByteParams(new byte[]{key, member, Protocol.toByteArray(radius), unit.raw}));
    }

    private ArrayList<byte[]> convertScoreMembersToByteArrays(Map<byte[], Double> scoreMembers) {
        ArrayList<byte[]> args = new ArrayList<>(scoreMembers.size() * 2);
        for (Map.Entry<byte[], Double> entry : scoreMembers.entrySet()) {
            args.add(Protocol.toByteArray(entry.getValue().doubleValue()));
            args.add(entry.getKey());
        }
        return args;
    }

    private List<byte[]> convertGeoCoordinateMapToByteArrays(Map<byte[], GeoCoordinate> memberCoordinateMap) {
        List<byte[]> args = new ArrayList<>(memberCoordinateMap.size() * 3);
        for (Map.Entry<byte[], GeoCoordinate> entry : memberCoordinateMap.entrySet()) {
            GeoCoordinate coordinate = entry.getValue();
            args.add(Protocol.toByteArray(coordinate.getLongitude()));
            args.add(Protocol.toByteArray(coordinate.getLatitude()));
            args.add(entry.getKey());
        }
        return args;
    }

    public void bitfield(byte[] key, byte[]... value) {
        sendCommand(Protocol.Command.BITFIELD, joinParameters(key, value));
    }
}
