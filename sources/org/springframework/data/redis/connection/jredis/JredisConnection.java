package org.springframework.data.redis.connection.jredis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.jredis.ClientRuntimeException;
import org.jredis.JRedis;
import org.jredis.Query;
import org.jredis.RedisException;
import org.jredis.Sort;
import org.jredis.connector.ConnectionException;
import org.jredis.connector.NotConnectedException;
import org.jredis.protocol.Command;
import org.jredis.ri.alphazero.JRedisSupport;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.AbstractRedisConnection;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.Pool;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.connection.Subscription;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jredis/JredisConnection.class */
public class JredisConnection extends AbstractRedisConnection {
    private static final Method SERVICE_REQUEST = ReflectionUtils.findMethod(JRedisSupport.class, "serviceRequest", Command.class, byte[][].class);
    private final JRedis jredis;
    private final Pool<JRedis> pool;
    private boolean isClosed;
    private boolean broken;

    static {
        ReflectionUtils.makeAccessible(SERVICE_REQUEST);
    }

    public JredisConnection(JRedis jredis) {
        this(jredis, null);
    }

    public JredisConnection(JRedis jredis, Pool<JRedis> pool) {
        this.isClosed = false;
        this.broken = false;
        Assert.notNull(jredis, "a not-null instance required");
        this.jredis = jredis;
        this.pool = pool;
    }

    protected DataAccessException convertJredisAccessException(Exception ex) {
        if (ex instanceof RedisException) {
            return JredisUtils.convertJredisAccessException((RedisException) ex);
        }
        if (ex instanceof ClientRuntimeException) {
            if ((ex instanceof NotConnectedException) || (ex instanceof ConnectionException)) {
                this.broken = true;
            }
            return JredisUtils.convertJredisAccessException((ClientRuntimeException) ex);
        }
        return new RedisSystemException("Unknown JRedis exception", ex);
    }

    @Override // org.springframework.data.redis.connection.RedisCommands
    public Object execute(String command, byte[]... args) {
        Assert.hasText(command, "a valid command needs to be specified");
        try {
            List<byte[]> mArgs = new ArrayList<>();
            if (!ObjectUtils.isEmpty((Object[]) args)) {
                Collections.addAll(mArgs, args);
            }
            return ReflectionUtils.invokeMethod(SERVICE_REQUEST, this.jredis, Command.valueOf(command.trim().toUpperCase()), mArgs.toArray((Object[]) new byte[mArgs.size()]));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.AbstractRedisConnection, org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        super.close();
        if (isClosed()) {
            return;
        }
        this.isClosed = true;
        if (this.pool != null) {
            if (!this.broken) {
                this.pool.returnResource(this.jredis);
                return;
            } else {
                this.pool.returnBrokenResource(this.jredis);
                return;
            }
        }
        try {
            this.jredis.quit();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public JRedis getNativeConnection() {
        return this.jredis;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isClosed() {
        return this.isClosed;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isQueueing() {
        return false;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isPipelined() {
        return false;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void openPipeline() {
        throw new UnsupportedOperationException("Pipelining not supported by JRedis");
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public List<Object> closePipeline() {
        return Collections.emptyList();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public List<byte[]> sort(byte[] key, SortParameters params) {
        Sort sort = this.jredis.sort(key);
        JredisUtils.applySortingParams(sort, params, null);
        try {
            return sort.exec();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        Sort sort = this.jredis.sort(key);
        JredisUtils.applySortingParams(sort, params, storeKey);
        try {
            return Long.valueOf(Query.Support.unpackValue(sort.exec()));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long dbSize() {
        try {
            return Long.valueOf(this.jredis.dbsize());
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushDb() {
        try {
            this.jredis.flushdb();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushAll() {
        try {
            this.jredis.flushall();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public byte[] echo(byte[] message) {
        try {
            return this.jredis.echo(message);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public String ping() {
        try {
            this.jredis.ping();
            return LettuceConnectionFactory.PING_REPLY;
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgSave() {
        try {
            this.jredis.bgsave();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgReWriteAof() {
        try {
            this.jredis.bgrewriteaof();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    @Deprecated
    public void bgWriteAof() {
        bgReWriteAof();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void save() {
        try {
            this.jredis.save();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<String> getConfig(String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info() {
        try {
            return JredisUtils.info(this.jredis.info());
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info(String section) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long lastSave() {
        try {
            return Long.valueOf(this.jredis.lastsave());
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setConfig(String param, String value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void resetConfigStats() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown(RedisServerCommands.ShutdownOption option) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long del(byte[]... keys) {
        try {
            return Long.valueOf(this.jredis.del(keys));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void discard() {
        try {
            this.jredis.discard();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public List<Object> exec() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean exists(byte[] key) {
        try {
            return Boolean.valueOf(this.jredis.exists(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expire(byte[] key, long seconds) {
        try {
            return Boolean.valueOf(this.jredis.expire(key, (int) seconds));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expireAt(byte[] key, long unixTime) {
        try {
            return Boolean.valueOf(this.jredis.expireat(key, unixTime));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpire(byte[] key, long millis) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] dump(byte[] key) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Set<byte[]> keys(byte[] pattern) {
        try {
            return new LinkedHashSet(this.jredis.keys(pattern));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void multi() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean persist(byte[] key) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean move(byte[] key, int dbIndex) {
        try {
            return Boolean.valueOf(this.jredis.move(key, dbIndex));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] randomKey() {
        try {
            return this.jredis.randomkey();
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void rename(byte[] oldName, byte[] newName) {
        try {
            this.jredis.rename(oldName, newName);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        try {
            return Boolean.valueOf(this.jredis.renamenx(oldName, newName));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public void select(int dbIndex) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key) {
        Assert.notNull(key, "Key must not be null!");
        try {
            return Long.valueOf(this.jredis.ttl(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        Assert.notNull(key, "Key must not be null!");
        try {
            return Long.valueOf(Converters.secondsToTimeUnit(this.jredis.ttl(key), timeUnit));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public DataType type(byte[] key) {
        try {
            return JredisUtils.convertDataType(this.jredis.type(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void unwatch() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void watch(byte[]... keys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] get(byte[] key) {
        try {
            return this.jredis.get(key);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value) {
        try {
            this.jredis.set(key, value);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value, Expiration expiration, RedisStringCommands.SetOption option) {
        throw new UnsupportedOperationException("SET with options is not supported for JRedis. Please use SETNX, SETEX, PSETEX.");
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getSet(byte[] key, byte[] value) {
        try {
            return this.jredis.getset(key, value);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long append(byte[] key, byte[] value) {
        try {
            return Long.valueOf(this.jredis.append(key, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public List<byte[]> mGet(byte[]... keys) {
        try {
            return this.jredis.mget(keys);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void mSet(Map<byte[], byte[]> tuple) {
        try {
            this.jredis.mset(tuple);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean mSetNX(Map<byte[], byte[]> tuple) {
        try {
            return Boolean.valueOf(this.jredis.msetnx(tuple));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setEx(byte[] key, long seconds, byte[] value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void pSetEx(byte[] key, long milliseconds, byte[] value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setNX(byte[] key, byte[] value) {
        try {
            return Boolean.valueOf(this.jredis.setnx(key, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getRange(byte[] key, long start, long end) {
        try {
            return this.jredis.substr(key, start, end);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decr(byte[] key) {
        try {
            return Long.valueOf(this.jredis.decr(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decrBy(byte[] key, long value) {
        try {
            return Long.valueOf(this.jredis.decrby(key, (int) value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incr(byte[] key) {
        try {
            return Long.valueOf(this.jredis.incr(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incrBy(byte[] key, long value) {
        try {
            return Long.valueOf(this.jredis.incrby(key, (int) value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Double incrBy(byte[] key, double value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean getBit(byte[] key, long offset) {
        try {
            return Boolean.valueOf(this.jredis.getbit(key, (int) offset));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setBit(byte[] key, long offset, boolean value) {
        try {
            return Boolean.valueOf(this.jredis.setbit(key, (int) offset, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setRange(byte[] key, byte[] value, long start) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long strLen(byte[] key) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key, long begin, long end) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lIndex(byte[] key, long index) {
        try {
            return this.jredis.lindex(key, index);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lLen(byte[] key) {
        try {
            return Long.valueOf(this.jredis.llen(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lPop(byte[] key) {
        try {
            return this.jredis.lpop(key);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPush(byte[] key, byte[]... values) {
        if (values.length > 1) {
            throw new UnsupportedOperationException("lPush of multiple fields not supported");
        }
        try {
            this.jredis.lpush(key, values[0]);
            return null;
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> lRange(byte[] key, long start, long end) {
        try {
            List<byte[]> lrange = this.jredis.lrange(key, start, end);
            return lrange;
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lRem(byte[] key, long count, byte[] value) {
        try {
            return Long.valueOf(this.jredis.lrem(key, value, (int) count));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lSet(byte[] key, long index, byte[] value) {
        try {
            this.jredis.lset(key, index, value);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lTrim(byte[] key, long start, long end) {
        try {
            this.jredis.ltrim(key, start, end);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPop(byte[] key) {
        try {
            return this.jredis.rpop(key);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        try {
            return this.jredis.rpoplpush(srcKey, dstKey);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPush(byte[] key, byte[]... values) {
        if (values.length > 1) {
            throw new UnsupportedOperationException("rPush of multiple fields not supported");
        }
        try {
            this.jredis.rpush(key, values[0]);
            return null;
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lInsert(byte[] key, RedisListCommands.Position where, byte[] pivot, byte[] value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPushX(byte[] key, byte[] value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPushX(byte[] key, byte[] value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sAdd(byte[] key, byte[]... values) {
        if (values.length > 1) {
            throw new UnsupportedOperationException("sAdd of multiple fields not supported");
        }
        try {
            return JredisUtils.toLong(Boolean.valueOf(this.jredis.sadd(key, values[0])));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sCard(byte[] key) {
        try {
            return Long.valueOf(this.jredis.scard(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sDiff(byte[]... keys) {
        byte[] destKey = keys[0];
        byte[][] sets = (byte[][]) Arrays.copyOfRange(keys, 1, keys.length);
        try {
            List<byte[]> result = this.jredis.sdiff(destKey, sets);
            return new LinkedHashSet(result);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        try {
            this.jredis.sdiffstore(destKey, keys);
            return -1L;
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sInter(byte[]... keys) {
        byte[] set1 = keys[0];
        byte[][] sets = (byte[][]) Arrays.copyOfRange(keys, 1, keys.length);
        try {
            List<byte[]> result = this.jredis.sinter(set1, sets);
            return new LinkedHashSet(result);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        try {
            this.jredis.sinterstore(destKey, keys);
            return -1L;
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sIsMember(byte[] key, byte[] value) {
        try {
            return Boolean.valueOf(this.jredis.sismember(key, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sMembers(byte[] key) {
        try {
            return new LinkedHashSet(this.jredis.smembers(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        try {
            return Boolean.valueOf(this.jredis.smove(srcKey, destKey, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sPop(byte[] key) {
        try {
            return this.jredis.spop(key);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sRandMember(byte[] key) {
        try {
            return this.jredis.srandmember(key);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public List<byte[]> sRandMember(byte[] key, long count) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sRem(byte[] key, byte[]... values) {
        if (values.length > 1) {
            throw new UnsupportedOperationException("sRem of multiple fields not supported");
        }
        try {
            return JredisUtils.toLong(Boolean.valueOf(this.jredis.srem(key, values[0])));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sUnion(byte[]... keys) {
        byte[] set1 = keys[0];
        byte[][] sets = (byte[][]) Arrays.copyOfRange(keys, 1, keys.length);
        try {
            return new LinkedHashSet(this.jredis.sunion(set1, sets));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        try {
            this.jredis.sunionstore(destKey, keys);
            return -1L;
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        try {
            return Boolean.valueOf(this.jredis.zadd(key, score, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zAdd(byte[] key, Set<RedisZSetCommands.Tuple> tuples) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCard(byte[] key) {
        try {
            return Long.valueOf(this.jredis.zcard(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, double min, double max) {
        return zCount(key, new RedisZSetCommands.Range().gte(Double.valueOf(min)).lte(Double.valueOf(max)));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, RedisZSetCommands.Range range) {
        Assert.notNull(range, "Range for ZCOUNT must not be null!");
        double min = ((Double) range.getMin().getValue()).doubleValue();
        double max = ((Double) range.getMax().getValue()).doubleValue();
        try {
            return Long.valueOf(this.jredis.zcount(key, min, max));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        try {
            return this.jredis.zincrby(key, increment, value);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        try {
            return new LinkedHashSet(this.jredis.zrange(key, start, end));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(byte[] key, long start, long end) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        try {
            return new LinkedHashSet(this.jredis.zrangebyscore(key, min, max));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRank(byte[] key, byte[] value) {
        try {
            return Long.valueOf(this.jredis.zrank(key, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRem(byte[] key, byte[]... values) {
        if (values.length > 1) {
            throw new UnsupportedOperationException("zRem of multiple fields not supported");
        }
        try {
            return JredisUtils.toLong(Boolean.valueOf(this.jredis.zrem(key, values[0])));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRange(byte[] key, long start, long end) {
        try {
            return Long.valueOf(this.jredis.zremrangebyrank(key, start, end));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        try {
            return Long.valueOf(this.jredis.zremrangebyscore(key, min, max));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        try {
            return new LinkedHashSet(this.jredis.zrevrange(key, start, end));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRevRank(byte[] key, byte[] value) {
        try {
            return Long.valueOf(this.jredis.zrevrank(key, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zScore(byte[] key, byte[] value) {
        try {
            return this.jredis.zscore(key, value);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hDel(byte[] key, byte[]... fields) {
        if (fields.length > 1) {
            throw new UnsupportedOperationException("hDel of multiple fields not supported");
        }
        try {
            return JredisUtils.toLong(Boolean.valueOf(this.jredis.hdel(key, fields[0])));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hExists(byte[] key, byte[] field) {
        try {
            return Boolean.valueOf(this.jredis.hexists(key, field));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public byte[] hGet(byte[] key, byte[] field) {
        try {
            return this.jredis.hget(key, field);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        try {
            return this.jredis.hgetall(key);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Set<byte[]> hKeys(byte[] key) {
        try {
            return new LinkedHashSet(this.jredis.hkeys(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hLen(byte[] key) {
        try {
            return Long.valueOf(this.jredis.hlen(key));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public void hMSet(byte[] key, Map<byte[], byte[]> values) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        try {
            return Boolean.valueOf(this.jredis.hset(key, field, value));
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hVals(byte[] key) {
        try {
            return this.jredis.hvals(key);
        } catch (Exception ex) {
            throw convertJredisAccessException(ex);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Subscription getSubscription() {
        return null;
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public boolean isSubscribed() {
        return false;
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Long publish(byte[] channel, byte[] message) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void subscribe(MessageListener listener, byte[]... channels) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, RedisGeoCommands.GeoLocation<byte[]> location) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Iterable<RedisGeoCommands.GeoLocation<byte[]>> locations) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<String> geoHash(byte[] key, byte[]... members) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<Point> geoPos(byte[] key, byte[]... members) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, RedisGeoCommands.GeoRadiusCommandArgs args) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoRemove(byte[] key, byte[]... members) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptFlush() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptKill() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public String scriptLoad(byte[] script) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public List<Boolean> scriptExists(String... scriptSha1) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(String scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(byte[] scriptSha1, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long time() {
        throw new UnsupportedOperationException("The 'TIME' command is not supported by the JRedis driver.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void killClient(String host, int port) {
        throw new UnsupportedOperationException("The 'CLIENT KILL' command is not supported by the JRedis driver.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setClientName(byte[] name) {
        throw new UnsupportedOperationException("'CLIENT SETNAME' is not supported by the JRedis driver.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOf(String host, int port) {
        try {
            this.jredis.slaveof(host, port);
        } catch (Exception e) {
            throw convertJredisAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public String getClientName() {
        throw new UnsupportedOperationException("The 'CLIENT GETNAME' command is not supported by the JRedis driver.");
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<RedisClientInfo> getClientList() {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOfNoOne() {
        try {
            this.jredis.slaveofnone();
        } catch (Exception e) {
            throw convertJredisAccessException(e);
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Cursor<byte[]> scan(ScanOptions options) {
        throw new UnsupportedOperationException("'SCAN' command is not supported for jredis.");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Cursor<RedisZSetCommands.Tuple> zScan(byte[] key, ScanOptions options) {
        throw new UnsupportedOperationException("'ZSCAN' command is not supported for jredis.");
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        throw new UnsupportedOperationException("'SSCAN' command is not uspported for jredis");
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        throw new UnsupportedOperationException("'HSCAN' command is not uspported for jredis");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        throw new UnsupportedOperationException("'zRangeByScore' command is not uspported for jredis");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        throw new UnsupportedOperationException("'zRangeByScore' command is not uspported for jredis");
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfAdd(byte[] key, byte[]... values) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfCount(byte[]... keys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key) {
        throw new UnsupportedOperationException("ZRANGEBYLEX is no supported for jredis.");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range) {
        throw new UnsupportedOperationException("ZRANGEBYLEX is no supported for jredis.");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        throw new UnsupportedOperationException("ZRANGEBYLEX is no supported for jredis.");
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option) {
        throw new UnsupportedOperationException();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option, long timeout) {
        throw new UnsupportedOperationException();
    }
}
