package redis.clients.jedis;

import java.io.Closeable;
import java.io.Serializable;
import java.net.URI;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.exceptions.InvalidURIException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import redis.clients.util.JedisByteHashMap;
import redis.clients.util.JedisURIHelper;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryJedis.class */
public class BinaryJedis implements BasicCommands, BinaryJedisCommands, MultiKeyBinaryCommands, AdvancedBinaryJedisCommands, BinaryScriptingCommands, Closeable {
    protected Client client;
    protected Transaction transaction;
    protected Pipeline pipeline;

    public BinaryJedis() {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client();
    }

    public BinaryJedis(String host) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        URI uri = URI.create(host);
        if (uri.getScheme() != null && (uri.getScheme().equals("redis") || uri.getScheme().equals("rediss"))) {
            initializeClientFromURI(uri);
        } else {
            this.client = new Client(host);
        }
    }

    public BinaryJedis(String host, int port) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port);
    }

    public BinaryJedis(String host, int port, boolean ssl) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port, ssl);
    }

    public BinaryJedis(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public BinaryJedis(String host, int port, int timeout) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port);
        this.client.setConnectionTimeout(timeout);
        this.client.setSoTimeout(timeout);
    }

    public BinaryJedis(String host, int port, int timeout, boolean ssl) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port, ssl);
        this.client.setConnectionTimeout(timeout);
        this.client.setSoTimeout(timeout);
    }

    public BinaryJedis(String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
        this.client.setConnectionTimeout(timeout);
        this.client.setSoTimeout(timeout);
    }

    public BinaryJedis(String host, int port, int connectionTimeout, int soTimeout) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port);
        this.client.setConnectionTimeout(connectionTimeout);
        this.client.setSoTimeout(soTimeout);
    }

    public BinaryJedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port, ssl);
        this.client.setConnectionTimeout(connectionTimeout);
        this.client.setSoTimeout(soTimeout);
    }

    public BinaryJedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(host, port, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
        this.client.setConnectionTimeout(connectionTimeout);
        this.client.setSoTimeout(soTimeout);
    }

    public BinaryJedis(JedisShardInfo shardInfo) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        this.client = new Client(shardInfo.getHost(), shardInfo.getPort(), shardInfo.getSsl(), shardInfo.getSslSocketFactory(), shardInfo.getSslParameters(), shardInfo.getHostnameVerifier());
        this.client.setConnectionTimeout(shardInfo.getConnectionTimeout());
        this.client.setSoTimeout(shardInfo.getSoTimeout());
        this.client.setPassword(shardInfo.getPassword());
        this.client.setDb(shardInfo.getDb());
    }

    public BinaryJedis(URI uri) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        initializeClientFromURI(uri);
    }

    public BinaryJedis(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        initializeClientFromURI(uri, sslSocketFactory, sslParameters, hostnameVerifier);
    }

    public BinaryJedis(URI uri, int timeout) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        initializeClientFromURI(uri);
        this.client.setConnectionTimeout(timeout);
        this.client.setSoTimeout(timeout);
    }

    public BinaryJedis(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        initializeClientFromURI(uri, sslSocketFactory, sslParameters, hostnameVerifier);
        this.client.setConnectionTimeout(timeout);
        this.client.setSoTimeout(timeout);
    }

    public BinaryJedis(URI uri, int connectionTimeout, int soTimeout) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        initializeClientFromURI(uri);
        this.client.setConnectionTimeout(connectionTimeout);
        this.client.setSoTimeout(soTimeout);
    }

    public BinaryJedis(URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        this.client = null;
        this.transaction = null;
        this.pipeline = null;
        initializeClientFromURI(uri, sslSocketFactory, sslParameters, hostnameVerifier);
        this.client.setConnectionTimeout(connectionTimeout);
        this.client.setSoTimeout(soTimeout);
    }

    private void initializeClientFromURI(URI uri) {
        if (!JedisURIHelper.isValid(uri)) {
            throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI. %s", uri.toString()));
        }
        this.client = new Client(uri.getHost(), uri.getPort(), uri.getScheme().equals("rediss"));
        String password = JedisURIHelper.getPassword(uri);
        if (password != null) {
            this.client.auth(password);
            this.client.getStatusCodeReply();
        }
        int dbIndex = JedisURIHelper.getDBIndex(uri);
        if (dbIndex > 0) {
            this.client.select(dbIndex);
            this.client.getStatusCodeReply();
            this.client.setDb(dbIndex);
        }
    }

    private void initializeClientFromURI(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
        if (!JedisURIHelper.isValid(uri)) {
            throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI. %s", uri.toString()));
        }
        this.client = new Client(uri.getHost(), uri.getPort(), uri.getScheme().equals("rediss"), sslSocketFactory, sslParameters, hostnameVerifier);
        String password = JedisURIHelper.getPassword(uri);
        if (password != null) {
            this.client.auth(password);
            this.client.getStatusCodeReply();
        }
        int dbIndex = JedisURIHelper.getDBIndex(uri);
        if (dbIndex > 0) {
            this.client.select(dbIndex);
            this.client.getStatusCodeReply();
            this.client.setDb(dbIndex);
        }
    }

    @Override // redis.clients.jedis.BasicCommands
    public String ping() {
        checkIsInMultiOrPipeline();
        this.client.ping();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String set(byte[] key, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value, nxxx, expx, time);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] get(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.get(key);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String quit() {
        checkIsInMultiOrPipeline();
        this.client.quit();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long exists(byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.exists(keys);
        return this.client.getIntegerReply();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean exists(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.exists((byte[][]) new byte[]{key});
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long del(byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.del(keys);
        return this.client.getIntegerReply();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long del(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.del((byte[][]) new byte[]{key});
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String type(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.type(key);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String flushDB() {
        checkIsInMultiOrPipeline();
        this.client.flushDB();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Set<byte[]> keys(byte[] pattern) {
        checkIsInMultiOrPipeline();
        this.client.keys(pattern);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public byte[] randomBinaryKey() {
        checkIsInMultiOrPipeline();
        this.client.randomKey();
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public String rename(byte[] oldkey, byte[] newkey) {
        checkIsInMultiOrPipeline();
        this.client.rename(oldkey, newkey);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long renamenx(byte[] oldkey, byte[] newkey) {
        checkIsInMultiOrPipeline();
        this.client.renamenx(oldkey, newkey);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public Long dbSize() {
        checkIsInMultiOrPipeline();
        this.client.dbSize();
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long expire(byte[] key, int seconds) {
        checkIsInMultiOrPipeline();
        this.client.expire(key, seconds);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    @Deprecated
    public Long pexpire(String key, long milliseconds) {
        checkIsInMultiOrPipeline();
        this.client.pexpire(key, milliseconds);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long expireAt(byte[] key, long unixTime) {
        checkIsInMultiOrPipeline();
        this.client.expireAt(key, unixTime);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long ttl(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.ttl(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String select(int index) {
        checkIsInMultiOrPipeline();
        this.client.select(index);
        String statusCodeReply = this.client.getStatusCodeReply();
        this.client.setDb(index);
        return statusCodeReply;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long move(byte[] key, int dbIndex) {
        checkIsInMultiOrPipeline();
        this.client.move(key, dbIndex);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String flushAll() {
        checkIsInMultiOrPipeline();
        this.client.flushAll();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] getSet(byte[] key, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.getSet(key, value);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public List<byte[]> mget(byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.mget(keys);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long setnx(byte[] key, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.setnx(key, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String setex(byte[] key, int seconds, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.setex(key, seconds, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public String mset(byte[]... keysvalues) {
        checkIsInMultiOrPipeline();
        this.client.mset(keysvalues);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long msetnx(byte[]... keysvalues) {
        checkIsInMultiOrPipeline();
        this.client.msetnx(keysvalues);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long decrBy(byte[] key, long decrement) {
        checkIsInMultiOrPipeline();
        this.client.decrBy(key, decrement);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long decr(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.decr(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long incrBy(byte[] key, long increment) {
        checkIsInMultiOrPipeline();
        this.client.incrBy(key, increment);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double incrByFloat(byte[] key, double increment) {
        checkIsInMultiOrPipeline();
        this.client.incrByFloat(key, increment);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long incr(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.incr(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long append(byte[] key, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.append(key, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] substr(byte[] key, int start, int end) {
        checkIsInMultiOrPipeline();
        this.client.substr(key, start, end);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hset(byte[] key, byte[] field, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.hset(key, field, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] hget(byte[] key, byte[] field) {
        checkIsInMultiOrPipeline();
        this.client.hget(key, field);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.hsetnx(key, field, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        checkIsInMultiOrPipeline();
        this.client.hmset(key, hash);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        checkIsInMultiOrPipeline();
        this.client.hmget(key, fields);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hincrBy(byte[] key, byte[] field, long value) {
        checkIsInMultiOrPipeline();
        this.client.hincrBy(key, field, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        checkIsInMultiOrPipeline();
        this.client.hincrByFloat(key, field, value);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean hexists(byte[] key, byte[] field) {
        checkIsInMultiOrPipeline();
        this.client.hexists(key, field);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hdel(byte[] key, byte[]... fields) {
        checkIsInMultiOrPipeline();
        this.client.hdel(key, fields);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long hlen(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.hlen(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> hkeys(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.hkeys(key);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> hvals(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.hvals(key);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Map<byte[], byte[]> hgetAll(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.hgetAll(key);
        List<byte[]> flatHash = this.client.getBinaryMultiBulkReply();
        Map<byte[], byte[]> hash = new JedisByteHashMap();
        Iterator<byte[]> iterator = flatHash.iterator();
        while (iterator.hasNext()) {
            hash.put(iterator.next(), iterator.next());
        }
        return hash;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long rpush(byte[] key, byte[]... strings) {
        checkIsInMultiOrPipeline();
        this.client.rpush(key, strings);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long lpush(byte[] key, byte[]... strings) {
        checkIsInMultiOrPipeline();
        this.client.lpush(key, strings);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long llen(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.llen(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> lrange(byte[] key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.lrange(key, start, stop);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String ltrim(byte[] key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.ltrim(key, start, stop);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] lindex(byte[] key, long index) {
        checkIsInMultiOrPipeline();
        this.client.lindex(key, index);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String lset(byte[] key, long index, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.lset(key, index, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long lrem(byte[] key, long count, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.lrem(key, count, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] lpop(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.lpop(key);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] rpop(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.rpop(key);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
        checkIsInMultiOrPipeline();
        this.client.rpoplpush(srckey, dstkey);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long sadd(byte[] key, byte[]... members) {
        checkIsInMultiOrPipeline();
        this.client.sadd(key, members);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> smembers(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.smembers(key);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long srem(byte[] key, byte[]... member) {
        checkIsInMultiOrPipeline();
        this.client.srem(key, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] spop(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.spop(key);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> spop(byte[] key, long count) {
        checkIsInMultiOrPipeline();
        this.client.spop(key, count);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.smove(srckey, dstkey, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long scard(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.scard(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean sismember(byte[] key, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.sismember(key, member);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Set<byte[]> sinter(byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.sinter(keys);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long sinterstore(byte[] dstkey, byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.sinterstore(dstkey, keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Set<byte[]> sunion(byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.sunion(keys);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long sunionstore(byte[] dstkey, byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.sunionstore(dstkey, keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Set<byte[]> sdiff(byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.sdiff(keys);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long sdiffstore(byte[] dstkey, byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.sdiffstore(dstkey, keys);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] srandmember(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.srandmember(key);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> srandmember(byte[] key, int count) {
        checkIsInMultiOrPipeline();
        this.client.srandmember(key, count);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, double score, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.zadd(key, score, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        checkIsInMultiOrPipeline();
        this.client.zadd(key, score, member, params);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        checkIsInMultiOrPipeline();
        this.client.zaddBinary(key, scoreMembers);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        checkIsInMultiOrPipeline();
        this.client.zaddBinary(key, scoreMembers, params);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrange(byte[] key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrange(key, start, stop);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zrem(byte[] key, byte[]... members) {
        checkIsInMultiOrPipeline();
        this.client.zrem(key, members);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double zincrby(byte[] key, double increment, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.zincrby(key, increment, member);
        return BuilderFactory.DOUBLE.build(this.client.getOne());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
        checkIsInMultiOrPipeline();
        this.client.zincrby(key, increment, member, params);
        return BuilderFactory.DOUBLE.build(this.client.getOne());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zrank(byte[] key, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.zrank(key, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zrevrank(byte[] key, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.zrevrank(key, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrange(byte[] key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrevrange(key, start, stop);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeWithScores(byte[] key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrangeWithScores(key, start, stop);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeWithScores(key, start, stop);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zcard(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.zcard(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double zscore(byte[] key, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.zscore(key, member);
        String score = this.client.getBulkReply();
        if (score != null) {
            return new Double(score);
        }
        return null;
    }

    public Transaction multi() {
        this.client.multi();
        this.transaction = new Transaction(this.client);
        return this.transaction;
    }

    @Deprecated
    public List<Object> multi(TransactionBlock jedisTransaction) throws JedisException {
        jedisTransaction.setClient(this.client);
        this.client.multi();
        jedisTransaction.execute();
        return jedisTransaction.exec();
    }

    protected void checkIsInMultiOrPipeline() {
        if (this.client.isInMulti()) {
            throw new JedisDataException("Cannot use Jedis when in Multi. Please use Transaction or reset jedis state.");
        }
        if (this.pipeline != null && this.pipeline.hasPipelinedResponse()) {
            throw new JedisDataException("Cannot use Jedis when in Pipeline. Please use Pipeline or reset jedis state .");
        }
    }

    public void connect() {
        this.client.connect();
    }

    public void disconnect() {
        this.client.disconnect();
    }

    public void resetState() {
        if (this.client.isConnected()) {
            if (this.transaction != null) {
                this.transaction.clear();
            }
            if (this.pipeline != null) {
                this.pipeline.clear();
            }
            this.client.resetState();
        }
        this.transaction = null;
        this.pipeline = null;
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public String watch(byte[]... keys) {
        this.client.watch(keys);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public String unwatch() {
        this.client.unwatch();
        return this.client.getStatusCodeReply();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.client.close();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> sort(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.sort(key);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        checkIsInMultiOrPipeline();
        this.client.sort(key, sortingParameters);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public List<byte[]> blpop(int timeout, byte[]... keys) {
        return blpop(getArgsAddTimeout(timeout, keys));
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [byte[], byte[][]] */
    private byte[][] getArgsAddTimeout(int timeout, byte[][] keys) {
        int size = keys.length;
        ?? r0 = new byte[size + 1];
        for (int at = 0; at != size; at++) {
            r0[at] = keys[at];
        }
        r0[size] = Protocol.toByteArray(timeout);
        return r0;
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        checkIsInMultiOrPipeline();
        this.client.sort(key, sortingParameters, dstkey);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long sort(byte[] key, byte[] dstkey) {
        checkIsInMultiOrPipeline();
        this.client.sort(key, dstkey);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public List<byte[]> brpop(int timeout, byte[]... keys) {
        return brpop(getArgsAddTimeout(timeout, keys));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryJedisCommands
    @Deprecated
    public List<byte[]> blpop(byte[] arg) {
        return blpop((byte[][]) new byte[]{arg});
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryJedisCommands
    @Deprecated
    public List<byte[]> brpop(byte[] arg) {
        return brpop((byte[][]) new byte[]{arg});
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public List<byte[]> blpop(byte[]... args) {
        checkIsInMultiOrPipeline();
        this.client.blpop(args);
        this.client.setTimeoutInfinite();
        try {
            return this.client.getBinaryMultiBulkReply();
        } finally {
            this.client.rollbackTimeout();
        }
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public List<byte[]> brpop(byte[]... args) {
        checkIsInMultiOrPipeline();
        this.client.brpop(args);
        this.client.setTimeoutInfinite();
        try {
            return this.client.getBinaryMultiBulkReply();
        } finally {
            this.client.rollbackTimeout();
        }
    }

    @Override // redis.clients.jedis.BasicCommands
    public String auth(String password) {
        checkIsInMultiOrPipeline();
        this.client.auth(password);
        return this.client.getStatusCodeReply();
    }

    @Deprecated
    public List<Object> pipelined(PipelineBlock jedisPipeline) {
        jedisPipeline.setClient(this.client);
        jedisPipeline.execute();
        return jedisPipeline.syncAndReturnAll();
    }

    public Pipeline pipelined() {
        this.pipeline = new Pipeline();
        this.pipeline.setClient(this.client);
        return this.pipeline;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zcount(byte[] key, double min, double max) {
        checkIsInMultiOrPipeline();
        this.client.zcount(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zcount(byte[] key, byte[] min, byte[] max) {
        checkIsInMultiOrPipeline();
        this.client.zcount(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return zrangeByScore(key, Protocol.toByteArray(min), Protocol.toByteArray(max));
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScore(key, min, max);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return zrangeByScore(key, Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScore(key, min, max, offset, count);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return zrangeByScoreWithScores(key, Protocol.toByteArray(min), Protocol.toByteArray(max));
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScoreWithScores(key, min, max);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        return zrangeByScoreWithScores(key, Protocol.toByteArray(min), Protocol.toByteArray(max), offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByScoreWithScores(key, min, max, offset, count);
        return getTupledSet();
    }

    protected Set<Tuple> getTupledSet() {
        List<byte[]> membersWithScores = this.client.getBinaryMultiBulkReply();
        if (membersWithScores.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Tuple> set = new LinkedHashSet<>(membersWithScores.size() / 2, 1.0f);
        Iterator<byte[]> iterator = membersWithScores.iterator();
        while (iterator.hasNext()) {
            set.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
        }
        return set;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return zrevrangeByScore(key, Protocol.toByteArray(max), Protocol.toByteArray(min));
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScore(key, max, min);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return zrevrangeByScore(key, Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScore(key, max, min, offset, count);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return zrevrangeByScoreWithScores(key, Protocol.toByteArray(max), Protocol.toByteArray(min));
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        return zrevrangeByScoreWithScores(key, Protocol.toByteArray(max), Protocol.toByteArray(min), offset, count);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScoreWithScores(key, max, min);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByScoreWithScores(key, max, min, offset, count);
        return getTupledSet();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByRank(byte[] key, long start, long stop) {
        checkIsInMultiOrPipeline();
        this.client.zremrangeByRank(key, start, stop);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByScore(byte[] key, double min, double max) {
        return zremrangeByScore(key, Protocol.toByteArray(min), Protocol.toByteArray(max));
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
        checkIsInMultiOrPipeline();
        this.client.zremrangeByScore(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long zunionstore(byte[] dstkey, byte[]... sets) {
        checkIsInMultiOrPipeline();
        this.client.zunionstore(dstkey, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        checkIsInMultiOrPipeline();
        this.client.zunionstore(dstkey, params, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long zinterstore(byte[] dstkey, byte[]... sets) {
        checkIsInMultiOrPipeline();
        this.client.zinterstore(dstkey, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        checkIsInMultiOrPipeline();
        this.client.zinterstore(dstkey, params, sets);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        checkIsInMultiOrPipeline();
        this.client.zlexcount(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByLex(key, min, max);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrangeByLex(key, min, max, offset, count);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByLex(key, max, min);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        checkIsInMultiOrPipeline();
        this.client.zrevrangeByLex(key, max, min, offset, count);
        return SetFromList.of((List) this.client.getBinaryMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        checkIsInMultiOrPipeline();
        this.client.zremrangeByLex(key, min, max);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String save() {
        this.client.save();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String bgsave() {
        this.client.bgsave();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String bgrewriteaof() {
        this.client.bgrewriteaof();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public Long lastsave() {
        this.client.lastsave();
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String shutdown() {
        String status;
        this.client.shutdown();
        try {
            status = this.client.getStatusCodeReply();
        } catch (JedisException e) {
            status = null;
        }
        return status;
    }

    @Override // redis.clients.jedis.BasicCommands
    public String info() {
        this.client.info();
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String info(String section) {
        this.client.info(section);
        return this.client.getBulkReply();
    }

    public void monitor(JedisMonitor jedisMonitor) {
        this.client.monitor();
        this.client.getStatusCodeReply();
        jedisMonitor.proceed(this.client);
    }

    @Override // redis.clients.jedis.BasicCommands
    public String slaveof(String host, int port) {
        this.client.slaveof(host, port);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String slaveofNoOne() {
        this.client.slaveofNoOne();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public List<byte[]> configGet(byte[] pattern) {
        this.client.configGet(pattern);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String configResetStat() {
        this.client.configResetStat();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public byte[] configSet(byte[] parameter, byte[] value) {
        this.client.configSet(parameter, value);
        return this.client.getBinaryBulkReply();
    }

    public boolean isConnected() {
        return this.client.isConnected();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long strlen(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.strlen(key);
        return this.client.getIntegerReply();
    }

    public void sync() {
        this.client.sync();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long lpushx(byte[] key, byte[]... string) {
        checkIsInMultiOrPipeline();
        this.client.lpushx(key, string);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long persist(byte[] key) {
        this.client.persist(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long rpushx(byte[] key, byte[]... string) {
        checkIsInMultiOrPipeline();
        this.client.rpushx(key, string);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] echo(byte[] string) {
        checkIsInMultiOrPipeline();
        this.client.echo(string);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.linsert(key, where, pivot, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public String debug(DebugParams params) {
        this.client.debug(params);
        return this.client.getStatusCodeReply();
    }

    public Client getClient() {
        return this.client;
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
        this.client.brpoplpush(source, destination, timeout);
        this.client.setTimeoutInfinite();
        try {
            byte[] binaryBulkReply = this.client.getBinaryBulkReply();
            this.client.rollbackTimeout();
            return binaryBulkReply;
        } catch (Throwable th) {
            this.client.rollbackTimeout();
            throw th;
        }
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean setbit(byte[] key, long offset, boolean value) {
        checkIsInMultiOrPipeline();
        this.client.setbit(key, offset, value);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.setbit(key, offset, value);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Boolean getbit(byte[] key, long offset) {
        checkIsInMultiOrPipeline();
        this.client.getbit(key, offset);
        return Boolean.valueOf(this.client.getIntegerReply().longValue() == 1);
    }

    public Long bitpos(byte[] key, boolean value) {
        return bitpos(key, value, new BitPosParams());
    }

    public Long bitpos(byte[] key, boolean value, BitPosParams params) {
        checkIsInMultiOrPipeline();
        this.client.bitpos(key, value, params);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long setrange(byte[] key, long offset, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.setrange(key, offset, value);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        checkIsInMultiOrPipeline();
        this.client.getrange(key, startOffset, endOffset);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long publish(byte[] channel, byte[] message) {
        checkIsInMultiOrPipeline();
        this.client.publish(channel, message);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        this.client.setTimeoutInfinite();
        try {
            jedisPubSub.proceed(this.client, channels);
        } finally {
            this.client.rollbackTimeout();
        }
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
        this.client.setTimeoutInfinite();
        try {
            jedisPubSub.proceedWithPatterns(this.client, patterns);
        } finally {
            this.client.rollbackTimeout();
        }
    }

    @Override // redis.clients.jedis.BasicCommands
    public Long getDB() {
        return this.client.getDB();
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        return eval(script, Protocol.toByteArray(keys.size()), getParamsWithBinary(keys, args));
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [byte[], byte[][]] */
    protected static byte[][] getParamsWithBinary(List<byte[]> keys, List<byte[]> args) {
        int keyCount = keys.size();
        int argCount = args.size();
        ?? r0 = new byte[keyCount + argCount];
        for (int i = 0; i < keyCount; i++) {
            r0[i] = keys.get(i);
        }
        for (int i2 = 0; i2 < argCount; i2++) {
            r0[keyCount + i2] = args.get(i2);
        }
        return r0;
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
        this.client.setTimeoutInfinite();
        try {
            this.client.eval(script, keyCount, params);
            Object one = this.client.getOne();
            this.client.rollbackTimeout();
            return one;
        } catch (Throwable th) {
            this.client.rollbackTimeout();
            throw th;
        }
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public Object eval(byte[] script, int keyCount, byte[]... params) {
        return eval(script, Protocol.toByteArray(keyCount), params);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryScriptingCommands
    public Object eval(byte[] script) {
        return eval(script, 0, (byte[][]) new byte[0]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [byte[], byte[][]] */
    @Override // redis.clients.jedis.BinaryScriptingCommands
    public Object evalsha(byte[] sha1) {
        return evalsha(sha1, 0, (byte[][]) new byte[0]);
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        return evalsha(sha1, keys.size(), getParamsWithBinary(keys, args));
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
        this.client.setTimeoutInfinite();
        try {
            this.client.evalsha(sha1, keyCount, params);
            Object one = this.client.getOne();
            this.client.rollbackTimeout();
            return one;
        } catch (Throwable th) {
            this.client.rollbackTimeout();
            throw th;
        }
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public String scriptFlush() {
        this.client.scriptFlush();
        return this.client.getStatusCodeReply();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][]] */
    public Long scriptExists(byte[] sha1) {
        return scriptExists((byte[][]) new byte[]{sha1}).get(0);
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public List<Long> scriptExists(byte[]... sha1) {
        this.client.scriptExists(sha1);
        return this.client.getIntegerMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public byte[] scriptLoad(byte[] script) {
        this.client.scriptLoad(script);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.BinaryScriptingCommands
    public String scriptKill() {
        this.client.scriptKill();
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public String slowlogReset() {
        this.client.slowlogReset();
        return this.client.getBulkReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public Long slowlogLen() {
        this.client.slowlogLen();
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public List<byte[]> slowlogGetBinary() {
        this.client.slowlogGet();
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public List<byte[]> slowlogGetBinary(long entries) {
        this.client.slowlogGet(entries);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public Long objectRefcount(byte[] key) {
        this.client.objectRefcount(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public byte[] objectEncoding(byte[] key) {
        this.client.objectEncoding(key);
        return this.client.getBinaryBulkReply();
    }

    @Override // redis.clients.jedis.AdvancedBinaryJedisCommands
    public Long objectIdletime(byte[] key) {
        this.client.objectIdletime(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long bitcount(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.bitcount(key);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long bitcount(byte[] key, long start, long end) {
        checkIsInMultiOrPipeline();
        this.client.bitcount(key, start, end);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        checkIsInMultiOrPipeline();
        this.client.bitop(op, destKey, srcKeys);
        return this.client.getIntegerReply();
    }

    public byte[] dump(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.dump(key);
        return this.client.getBinaryBulkReply();
    }

    public String restore(byte[] key, int ttl, byte[] serializedValue) {
        checkIsInMultiOrPipeline();
        this.client.restore(key, ttl, serializedValue);
        return this.client.getStatusCodeReply();
    }

    @Deprecated
    public Long pexpire(byte[] key, int milliseconds) {
        return pexpire(key, milliseconds);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pexpire(byte[] key, long milliseconds) {
        checkIsInMultiOrPipeline();
        this.client.pexpire(key, milliseconds);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        checkIsInMultiOrPipeline();
        this.client.pexpireAt(key, millisecondsTimestamp);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pttl(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.pttl(key);
        return this.client.getIntegerReply();
    }

    @Deprecated
    public String psetex(byte[] key, int milliseconds, byte[] value) {
        return psetex(key, milliseconds, value);
    }

    public String psetex(byte[] key, long milliseconds, byte[] value) {
        checkIsInMultiOrPipeline();
        this.client.psetex(key, milliseconds, value);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public String set(byte[] key, byte[] value, byte[] nxxx) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value, nxxx);
        return this.client.getStatusCodeReply();
    }

    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        checkIsInMultiOrPipeline();
        this.client.set(key, value, nxxx, expx, time);
        return this.client.getStatusCodeReply();
    }

    public String clientKill(byte[] client) {
        checkIsInMultiOrPipeline();
        this.client.clientKill(client);
        return this.client.getStatusCodeReply();
    }

    public String clientGetname() {
        checkIsInMultiOrPipeline();
        this.client.clientGetname();
        return this.client.getBulkReply();
    }

    public String clientList() {
        checkIsInMultiOrPipeline();
        this.client.clientList();
        return this.client.getBulkReply();
    }

    public String clientSetname(byte[] name) {
        checkIsInMultiOrPipeline();
        this.client.clientSetname(name);
        return this.client.getBulkReply();
    }

    public List<String> time() {
        checkIsInMultiOrPipeline();
        this.client.time();
        return this.client.getMultiBulkReply();
    }

    public String migrate(byte[] host, int port, byte[] key, int destinationDb, int timeout) {
        checkIsInMultiOrPipeline();
        this.client.migrate(host, port, key, destinationDb, timeout);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.BasicCommands
    public Long waitReplicas(int replicas, long timeout) {
        checkIsInMultiOrPipeline();
        this.client.waitReplicas(replicas, timeout);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long pfadd(byte[] key, byte[]... elements) {
        checkIsInMultiOrPipeline();
        this.client.pfadd(key, elements);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public long pfcount(byte[] key) {
        checkIsInMultiOrPipeline();
        this.client.pfcount(key);
        return this.client.getIntegerReply().longValue();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
        checkIsInMultiOrPipeline();
        this.client.pfmerge(destkey, sourcekeys);
        return this.client.getStatusCodeReply();
    }

    @Override // redis.clients.jedis.MultiKeyBinaryCommands
    public Long pfcount(byte[]... keys) {
        checkIsInMultiOrPipeline();
        this.client.pfcount(keys);
        return this.client.getIntegerReply();
    }

    public ScanResult<byte[]> scan(byte[] cursor) {
        return scan(cursor, new ScanParams());
    }

    public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.scan(cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        byte[] newcursor = (byte[]) result.get(0);
        List<byte[]> rawResults = (List) result.get(1);
        return new ScanResult<>(newcursor, rawResults);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        return hscan(key, cursor, new ScanParams());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.hscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        byte[] newcursor = (byte[]) result.get(0);
        List<Map.Entry<byte[], byte[]>> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new AbstractMap.SimpleEntry<>(iterator.next(), iterator.next()));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        return sscan(key, cursor, new ScanParams());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.sscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        byte[] newcursor = (byte[]) result.get(0);
        List<byte[]> rawResults = (List) result.get(1);
        return new ScanResult<>(newcursor, rawResults);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        return zscan(key, cursor, new ScanParams());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        checkIsInMultiOrPipeline();
        this.client.zscan(key, cursor, params);
        List<Object> result = this.client.getObjectMultiBulkReply();
        byte[] newcursor = (byte[]) result.get(0);
        List<Tuple> results = new ArrayList<>();
        List<byte[]> rawResults = (List) result.get(1);
        Iterator<byte[]> iterator = rawResults.iterator();
        while (iterator.hasNext()) {
            results.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
        }
        return new ScanResult<>(newcursor, results);
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        checkIsInMultiOrPipeline();
        this.client.geoadd(key, longitude, latitude, member);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        checkIsInMultiOrPipeline();
        this.client.geoadd(key, memberCoordinateMap);
        return this.client.getIntegerReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        checkIsInMultiOrPipeline();
        this.client.geodist(key, member1, member2);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        checkIsInMultiOrPipeline();
        this.client.geodist(key, member1, member2, unit);
        String dval = this.client.getBulkReply();
        if (dval != null) {
            return new Double(dval);
        }
        return null;
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<byte[]> geohash(byte[] key, byte[]... members) {
        checkIsInMultiOrPipeline();
        this.client.geohash(key, members);
        return this.client.getBinaryMultiBulkReply();
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        checkIsInMultiOrPipeline();
        this.client.geopos(key, members);
        return BuilderFactory.GEO_COORDINATE_LIST.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        checkIsInMultiOrPipeline();
        this.client.georadius(key, longitude, latitude, radius, unit);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        checkIsInMultiOrPipeline();
        this.client.georadius(key, longitude, latitude, radius, unit, param);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        checkIsInMultiOrPipeline();
        this.client.georadiusByMember(key, member, radius, unit);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        checkIsInMultiOrPipeline();
        this.client.georadiusByMember(key, member, radius, unit, param);
        return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(this.client.getObjectMultiBulkReply());
    }

    /* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryJedis$SetFromList.class */
    protected static class SetFromList<E> extends AbstractSet<E> implements Serializable {
        private static final long serialVersionUID = -2850347066962734052L;
        private final transient List<E> list;

        private SetFromList(List<E> list) {
            if (list == null) {
                throw new NullPointerException("list");
            }
            this.list = list;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.list.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.list.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return this.list.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return this.list.contains(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return this.list.remove(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(E e) {
            return !contains(e) && this.list.add(e);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return this.list.iterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return this.list.toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.list.toArray(tArr);
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            return this.list.toString();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return this.list.hashCode();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Set)) {
                return false;
            }
            Collection<?> c = (Collection) o;
            if (c.size() != size()) {
                return false;
            }
            return containsAll(c);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> c) {
            return this.list.containsAll(c);
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            return this.list.removeAll(c);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            return this.list.retainAll(c);
        }

        protected static <E> SetFromList<E> of(List<E> list) {
            return new SetFromList<>(list);
        }
    }

    @Override // redis.clients.jedis.BinaryJedisCommands
    public List<Long> bitfield(byte[] key, byte[]... arguments) {
        checkIsInMultiOrPipeline();
        this.client.bitfield(key, arguments);
        return this.client.getIntegerMultiBulkReply();
    }
}
