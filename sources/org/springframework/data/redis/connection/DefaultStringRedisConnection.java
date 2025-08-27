package org.springframework.data.redis.connection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.convert.ListConverter;
import org.springframework.data.redis.connection.convert.MapConverter;
import org.springframework.data.redis.connection.convert.SetConverter;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultStringRedisConnection.class */
public class DefaultStringRedisConnection implements StringRedisConnection, DecoratedRedisConnection {
    private static final byte[][] EMPTY_2D_BYTE_ARRAY = new byte[0];
    private final Log log;
    private final RedisConnection delegate;
    private final RedisSerializer<String> serializer;
    private Converter<byte[], String> bytesToString;
    private SetConverter<RedisZSetCommands.Tuple, StringRedisConnection.StringTuple> tupleToStringTuple;
    private SetConverter<StringRedisConnection.StringTuple, RedisZSetCommands.Tuple> stringTupleToTuple;
    private ListConverter<byte[], String> byteListToStringList;
    private MapConverter<byte[], String> byteMapToStringMap;
    private SetConverter<byte[], String> byteSetToStringSet;
    private Converter<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<String>>> byteGeoResultsToStringGeoResults;
    private Queue<Converter> pipelineConverters;
    private Queue<Converter> txConverters;
    private boolean deserializePipelineAndTxResults;
    private IdentityConverter identityConverter;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultStringRedisConnection$DeserializingConverter.class */
    private class DeserializingConverter implements Converter<byte[], String> {
        private DeserializingConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public String convert2(byte[] source) {
            return (String) DefaultStringRedisConnection.this.serializer.deserialize(source);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultStringRedisConnection$TupleConverter.class */
    private class TupleConverter implements Converter<RedisZSetCommands.Tuple, StringRedisConnection.StringTuple> {
        private TupleConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public StringRedisConnection.StringTuple convert2(RedisZSetCommands.Tuple source) {
            return new DefaultStringTuple(source, (String) DefaultStringRedisConnection.this.serializer.deserialize(source.getValue()));
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultStringRedisConnection$StringTupleConverter.class */
    private class StringTupleConverter implements Converter<StringRedisConnection.StringTuple, RedisZSetCommands.Tuple> {
        private StringTupleConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public RedisZSetCommands.Tuple convert2(StringRedisConnection.StringTuple source) {
            return new DefaultTuple(source.getValue(), source.getScore());
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultStringRedisConnection$IdentityConverter.class */
    private class IdentityConverter implements Converter<Object, Object> {
        private IdentityConverter() {
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public Object convert2(Object source) {
            return source;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DefaultStringRedisConnection$TransactionResultConverter.class */
    private class TransactionResultConverter implements Converter<List<Object>, List<Object>> {
        private Queue<Converter> txConverters;

        public TransactionResultConverter(Queue<Converter> txConverters) {
            this.txConverters = txConverters;
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<Object> convert2(List<Object> execResults) {
            return DefaultStringRedisConnection.this.convertResults(execResults, this.txConverters);
        }
    }

    public DefaultStringRedisConnection(RedisConnection connection) {
        this(connection, new StringRedisSerializer());
    }

    public DefaultStringRedisConnection(RedisConnection connection, RedisSerializer<String> serializer) {
        this.log = LogFactory.getLog(DefaultStringRedisConnection.class);
        this.bytesToString = new DeserializingConverter();
        this.tupleToStringTuple = new SetConverter<>(new TupleConverter());
        this.stringTupleToTuple = new SetConverter<>(new StringTupleConverter());
        this.byteListToStringList = new ListConverter<>(this.bytesToString);
        this.byteMapToStringMap = new MapConverter<>(this.bytesToString);
        this.byteSetToStringSet = new SetConverter<>(this.bytesToString);
        this.pipelineConverters = new LinkedList();
        this.txConverters = new LinkedList();
        this.deserializePipelineAndTxResults = false;
        this.identityConverter = new IdentityConverter();
        Assert.notNull(connection, "connection is required");
        Assert.notNull(serializer, "serializer is required");
        this.delegate = connection;
        this.serializer = serializer;
        this.byteGeoResultsToStringGeoResults = Converters.deserializingGeoResultsConverter(serializer);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long append(byte[] key, byte[] value) {
        Long result = this.delegate.append(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgSave() {
        this.delegate.bgSave();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void bgReWriteAof() {
        this.delegate.bgReWriteAof();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    @Deprecated
    public void bgWriteAof() {
        bgReWriteAof();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bLPop(int timeout, byte[]... keys) {
        List<byte[]> results = this.delegate.bLPop(timeout, keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> bRPop(int timeout, byte[]... keys) {
        List<byte[]> results = this.delegate.bRPop(timeout, keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
        byte[] result = this.delegate.bRPopLPush(timeout, srcKey, dstKey);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void close() throws DataAccessException {
        this.delegate.close();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long dbSize() {
        Long result = this.delegate.dbSize();
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decr(byte[] key) {
        Long result = this.delegate.decr(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long decrBy(byte[] key, long value) {
        Long result = this.delegate.decrBy(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long del(byte[]... keys) {
        Long result = this.delegate.del(keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void discard() {
        try {
            this.delegate.discard();
        } finally {
            this.txConverters.clear();
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public byte[] echo(byte[] message) {
        byte[] result = this.delegate.echo(message);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public List<Object> exec() {
        try {
            List<Object> results = this.delegate.exec();
            if (isPipelined()) {
                this.pipelineConverters.add(new TransactionResultConverter(new LinkedList(this.txConverters)));
                return results;
            }
            return convertResults(results, this.txConverters);
        } finally {
            this.txConverters.clear();
        }
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean exists(byte[] key) {
        Boolean result = this.delegate.exists(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expire(byte[] key, long seconds) {
        Boolean result = this.delegate.expire(key, seconds);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean expireAt(byte[] key, long unixTime) {
        Boolean result = this.delegate.expireAt(key, unixTime);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushAll() {
        this.delegate.flushAll();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void flushDb() {
        this.delegate.flushDb();
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] get(byte[] key) {
        byte[] result = this.delegate.get(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean getBit(byte[] key, long offset) {
        Boolean result = this.delegate.getBit(key, offset);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public List<String> getConfig(String pattern) {
        List<String> results = this.delegate.getConfig(pattern);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public Object getNativeConnection() {
        Object result = this.delegate.getNativeConnection();
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getRange(byte[] key, long start, long end) {
        byte[] result = this.delegate.getRange(key, start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public byte[] getSet(byte[] key, byte[] value) {
        byte[] result = this.delegate.getSet(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Subscription getSubscription() {
        return this.delegate.getSubscription();
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hDel(byte[] key, byte[]... fields) {
        Long result = this.delegate.hDel(key, fields);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hExists(byte[] key, byte[] field) {
        Boolean result = this.delegate.hExists(key, field);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public byte[] hGet(byte[] key, byte[] field) {
        byte[] result = this.delegate.hGet(key, field);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Map<byte[], byte[]> hGetAll(byte[] key) {
        Map<byte[], byte[]> results = this.delegate.hGetAll(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hIncrBy(byte[] key, byte[] field, long delta) {
        Long result = this.delegate.hIncrBy(key, field, delta);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Double hIncrBy(byte[] key, byte[] field, double delta) {
        Double result = this.delegate.hIncrBy(key, field, delta);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Set<byte[]> hKeys(byte[] key) {
        Set<byte[]> results = this.delegate.hKeys(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Long hLen(byte[] key) {
        Long result = this.delegate.hLen(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hMGet(byte[] key, byte[]... fields) {
        List<byte[]> results = this.delegate.hMGet(key, fields);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
        this.delegate.hMSet(key, hashes);
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSet(byte[] key, byte[] field, byte[] value) {
        Boolean result = this.delegate.hSet(key, field, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
        Boolean result = this.delegate.hSetNX(key, field, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public List<byte[]> hVals(byte[] key) {
        List<byte[]> results = this.delegate.hVals(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incr(byte[] key) {
        Long result = this.delegate.incr(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long incrBy(byte[] key, long value) {
        Long result = this.delegate.incrBy(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Double incrBy(byte[] key, double value) {
        Double result = this.delegate.incrBy(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info() {
        Properties result = this.delegate.info();
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Properties info(String section) {
        Properties result = this.delegate.info(section);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isClosed() {
        return this.delegate.isClosed();
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isQueueing() {
        return this.delegate.isQueueing();
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public boolean isSubscribed() {
        return this.delegate.isSubscribed();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Set<byte[]> keys(byte[] pattern) {
        Set<byte[]> results = this.delegate.keys(pattern);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long lastSave() {
        Long result = this.delegate.lastSave();
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lIndex(byte[] key, long index) {
        byte[] result = this.delegate.lIndex(key, index);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lInsert(byte[] key, RedisListCommands.Position where, byte[] pivot, byte[] value) {
        Long result = this.delegate.lInsert(key, where, pivot, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lLen(byte[] key) {
        Long result = this.delegate.lLen(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] lPop(byte[] key) {
        byte[] result = this.delegate.lPop(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPush(byte[] key, byte[]... values) {
        Long result = this.delegate.lPush(key, values);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lPushX(byte[] key, byte[] value) {
        Long result = this.delegate.lPushX(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public List<byte[]> lRange(byte[] key, long start, long end) {
        List<byte[]> results = this.delegate.lRange(key, start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long lRem(byte[] key, long count, byte[] value) {
        Long result = this.delegate.lRem(key, count, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lSet(byte[] key, long index, byte[] value) {
        this.delegate.lSet(key, index, value);
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public void lTrim(byte[] key, long start, long end) {
        this.delegate.lTrim(key, start, end);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public List<byte[]> mGet(byte[]... keys) {
        List<byte[]> results = this.delegate.mGet(keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void mSet(Map<byte[], byte[]> tuple) {
        this.delegate.mSet(tuple);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean mSetNX(Map<byte[], byte[]> tuple) {
        Boolean result = this.delegate.mSetNX(tuple);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void multi() {
        this.delegate.multi();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean persist(byte[] key) {
        Boolean result = this.delegate.persist(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean move(byte[] key, int dbIndex) {
        Boolean result = this.delegate.move(key, dbIndex);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public String ping() {
        String result = this.delegate.ping();
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void pSubscribe(MessageListener listener, byte[]... patterns) {
        this.delegate.pSubscribe(listener, patterns);
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public Long publish(byte[] channel, byte[] message) {
        Long result = this.delegate.publish(channel, message);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] randomKey() {
        byte[] result = this.delegate.randomKey();
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void rename(byte[] oldName, byte[] newName) {
        this.delegate.rename(oldName, newName);
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean renameNX(byte[] oldName, byte[] newName) {
        Boolean result = this.delegate.renameNX(oldName, newName);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void resetConfigStats() {
        this.delegate.resetConfigStats();
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPop(byte[] key) {
        byte[] result = this.delegate.rPop(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
        byte[] result = this.delegate.rPopLPush(srcKey, dstKey);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPush(byte[] key, byte[]... values) {
        Long result = this.delegate.rPush(key, values);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisListCommands
    public Long rPushX(byte[] key, byte[] value) {
        Long result = this.delegate.rPushX(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sAdd(byte[] key, byte[]... values) {
        Long result = this.delegate.sAdd(key, values);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void save() {
        this.delegate.save();
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sCard(byte[] key) {
        Long result = this.delegate.sCard(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sDiff(byte[]... keys) {
        Set<byte[]> results = this.delegate.sDiff(keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sDiffStore(byte[] destKey, byte[]... keys) {
        Long result = this.delegate.sDiffStore(destKey, keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisConnectionCommands
    public void select(int dbIndex) {
        this.delegate.select(dbIndex);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value) {
        this.delegate.set(key, value);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void set(byte[] key, byte[] value, Expiration expiration, RedisStringCommands.SetOption option) {
        this.delegate.set(key, value, expiration, option);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setBit(byte[] key, long offset, boolean value) {
        return this.delegate.setBit(key, offset, value);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setConfig(String param, String value) {
        this.delegate.setConfig(param, value);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setEx(byte[] key, long seconds, byte[] value) {
        this.delegate.setEx(key, seconds, value);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void pSetEx(byte[] key, long milliseconds, byte[] value) {
        this.delegate.pSetEx(key, milliseconds, value);
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Boolean setNX(byte[] key, byte[] value) {
        Boolean result = this.delegate.setNX(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public void setRange(byte[] key, byte[] value, long start) {
        this.delegate.setRange(key, value, start);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown() {
        this.delegate.shutdown();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void shutdown(RedisServerCommands.ShutdownOption option) {
        this.delegate.shutdown(option);
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sInter(byte[]... keys) {
        Set<byte[]> results = this.delegate.sInter(keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sInterStore(byte[] destKey, byte[]... keys) {
        Long result = this.delegate.sInterStore(destKey, keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sIsMember(byte[] key, byte[] value) {
        Boolean result = this.delegate.sIsMember(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sMembers(byte[] key) {
        Set<byte[]> results = this.delegate.sMembers(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
        Boolean result = this.delegate.sMove(srcKey, destKey, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
        Long result = this.delegate.sort(key, params, storeKey);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public List<byte[]> sort(byte[] key, SortParameters params) {
        List<byte[]> results = this.delegate.sort(key, params);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sPop(byte[] key) {
        byte[] result = this.delegate.sPop(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public byte[] sRandMember(byte[] key) {
        byte[] result = this.delegate.sRandMember(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public List<byte[]> sRandMember(byte[] key, long count) {
        List<byte[]> results = this.delegate.sRandMember(key, count);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sRem(byte[] key, byte[]... values) {
        Long result = this.delegate.sRem(key, values);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long strLen(byte[] key) {
        Long result = this.delegate.strLen(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key) {
        Long result = this.delegate.bitCount(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitCount(byte[] key, long begin, long end) {
        Long result = this.delegate.bitCount(key, begin, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisStringCommands
    public Long bitOp(RedisStringCommands.BitOperation op, byte[] destination, byte[]... keys) {
        Long result = this.delegate.bitOp(op, destination, keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisPubSubCommands
    public void subscribe(MessageListener listener, byte[]... channels) {
        this.delegate.subscribe(listener, channels);
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Set<byte[]> sUnion(byte[]... keys) {
        Set<byte[]> results = this.delegate.sUnion(keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Long sUnionStore(byte[] destKey, byte[]... keys) {
        Long result = this.delegate.sUnionStore(destKey, keys);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key) {
        Long result = this.delegate.ttl(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long ttl(byte[] key, TimeUnit timeUnit) {
        Long result = this.delegate.ttl(key, timeUnit);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public DataType type(byte[] key) {
        DataType result = this.delegate.type(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void unwatch() {
        this.delegate.unwatch();
    }

    @Override // org.springframework.data.redis.connection.RedisTxCommands
    public void watch(byte[]... keys) {
        this.delegate.watch(keys);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Boolean zAdd(byte[] key, double score, byte[] value) {
        Boolean result = this.delegate.zAdd(key, score, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zAdd(byte[] key, Set<RedisZSetCommands.Tuple> tuples) {
        Long result = this.delegate.zAdd(key, tuples);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCard(byte[] key) {
        Long result = this.delegate.zCard(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, double min, double max) {
        Long result = this.delegate.zCount(key, min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zCount(byte[] key, RedisZSetCommands.Range range) {
        Long result = this.delegate.zCount(key, range);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zIncrBy(byte[] key, double increment, byte[] value) {
        Double result = this.delegate.zIncrBy(key, increment, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        Long result = this.delegate.zInterStore(destKey, aggregate, weights, sets);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zInterStore(byte[] destKey, byte[]... sets) {
        Long result = this.delegate.zInterStore(destKey, sets);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRange(byte[] key, long start, long end) {
        Set<byte[]> results = this.delegate.zRange(key, start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
        Set<byte[]> results = this.delegate.zRangeByScore(key, min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        Set<byte[]> results = this.delegate.zRangeByScore(key, range);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Set<byte[]> results = this.delegate.zRangeByScore(key, range, limit);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeByScoreWithScores(key, range);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
        Set<byte[]> results = this.delegate.zRangeByScore(key, min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeByScoreWithScores(key, min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeByScoreWithScores(key, range, limit);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeByScoreWithScores(key, min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRangeWithScores(byte[] key, long start, long end) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeWithScores(key, start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
        Set<byte[]> results = this.delegate.zRevRangeByScore(key, min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        Set<byte[]> results = this.delegate.zRevRangeByScore(key, range);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
        Set<byte[]> results = this.delegate.zRevRangeByScore(key, min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRangeByScore(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Set<byte[]> results = this.delegate.zRevRangeByScore(key, range, limit);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeByScoreWithScores(key, min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeByScoreWithScores(key, range);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeByScoreWithScores(key, range, limit);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeByScoreWithScores(key, min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRank(byte[] key, byte[] value) {
        Long result = this.delegate.zRank(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRem(byte[] key, byte[]... values) {
        Long result = this.delegate.zRem(key, values);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRange(byte[] key, long start, long end) {
        Long result = this.delegate.zRemRange(key, start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, double min, double max) {
        Long result = this.delegate.zRemRangeByScore(key, min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRemRangeByScore(byte[] key, RedisZSetCommands.Range range) {
        Long result = this.delegate.zRemRangeByScore(key, range);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRevRange(byte[] key, long start, long end) {
        Set<byte[]> results = this.delegate.zRevRange(key, start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<RedisZSetCommands.Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeWithScores(key, start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zRevRank(byte[] key, byte[] value) {
        Long result = this.delegate.zRevRank(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Double zScore(byte[] key, byte[] value) {
        Double result = this.delegate.zScore(key, value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, byte[]... sets) {
        Long result = this.delegate.zUnionStore(destKey, aggregate, weights, sets);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Long zUnionStore(byte[] destKey, byte[]... sets) {
        Long result = this.delegate.zUnionStore(destKey, sets);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpire(byte[] key, long millis) {
        Boolean result = this.delegate.pExpire(key, millis);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
        Boolean result = this.delegate.pExpireAt(key, unixTimeInMillis);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key) {
        Long result = this.delegate.pTtl(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Long pTtl(byte[] key, TimeUnit timeUnit) {
        Long result = this.delegate.pTtl(key, timeUnit);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public byte[] dump(byte[] key) {
        byte[] result = this.delegate.dump(key);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
        this.delegate.restore(key, ttlInMillis, serializedValue);
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptFlush() {
        this.delegate.scriptFlush();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public void scriptKill() {
        this.delegate.scriptKill();
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public String scriptLoad(byte[] script) {
        String result = this.delegate.scriptLoad(script);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public List<Boolean> scriptExists(String... scriptSha1) {
        List<Boolean> results = this.delegate.scriptExists(scriptSha1);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T eval(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        T t = (T) this.delegate.eval(bArr, returnType, i, bArr2);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return t;
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(String str, ReturnType returnType, int i, byte[]... bArr) {
        T t = (T) this.delegate.evalSha(str, returnType, i, bArr);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return t;
    }

    @Override // org.springframework.data.redis.connection.RedisScriptingCommands
    public <T> T evalSha(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2) {
        T t = (T) this.delegate.evalSha(bArr, returnType, i, bArr2);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return t;
    }

    private byte[] serialize(String data) {
        return this.serializer.serialize(data);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    private byte[][] serializeMulti(String... keys) {
        if (keys == null) {
            return EMPTY_2D_BYTE_ARRAY;
        }
        ?? r0 = new byte[keys.length];
        for (int i = 0; i < r0.length; i++) {
            r0[i] = this.serializer.serialize(keys[i]);
        }
        return r0;
    }

    private Map<byte[], byte[]> serialize(Map<String, String> hashes) {
        Map<byte[], byte[]> ret = new LinkedHashMap<>(hashes.size());
        for (Map.Entry<String, String> entry : hashes.entrySet()) {
            ret.put(this.serializer.serialize(entry.getKey()), this.serializer.serialize(entry.getValue()));
        }
        return ret;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long append(String key, String value) {
        Long result = this.delegate.append(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> bLPop(int timeout, String... keys) {
        List<byte[]> results = this.delegate.bLPop(timeout, serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> bRPop(int timeout, String... keys) {
        List<byte[]> results = this.delegate.bRPop(timeout, serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String bRPopLPush(int timeout, String srcKey, String dstKey) {
        byte[] result = this.delegate.bRPopLPush(timeout, serialize(srcKey), serialize(dstKey));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long decr(String key) {
        Long result = this.delegate.decr(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long decrBy(String key, long value) {
        Long result = this.delegate.decrBy(serialize(key), value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long del(String... keys) {
        Long result = this.delegate.del(serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String echo(String message) {
        byte[] result = this.delegate.echo(serialize(message));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean exists(String key) {
        Boolean result = this.delegate.exists(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean expire(String key, long seconds) {
        Boolean result = this.delegate.expire(serialize(key), seconds);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean expireAt(String key, long unixTime) {
        Boolean result = this.delegate.expireAt(serialize(key), unixTime);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String get(String key) {
        byte[] result = this.delegate.get(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean getBit(String key, long offset) {
        Boolean result = this.delegate.getBit(serialize(key), offset);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String getRange(String key, long start, long end) {
        byte[] result = this.delegate.getRange(serialize(key), start, end);
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String getSet(String key, String value) {
        byte[] result = this.delegate.getSet(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long hDel(String key, String... fields) {
        Long result = this.delegate.hDel(serialize(key), serializeMulti(fields));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean hExists(String key, String field) {
        Boolean result = this.delegate.hExists(serialize(key), serialize(field));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String hGet(String key, String field) {
        byte[] result = this.delegate.hGet(serialize(key), serialize(field));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Map<String, String> hGetAll(String key) {
        Map<byte[], byte[]> results = this.delegate.hGetAll(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.byteMapToStringMap);
        }
        return this.byteMapToStringMap.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long hIncrBy(String key, String field, long delta) {
        Long result = this.delegate.hIncrBy(serialize(key), serialize(field), delta);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Double hIncrBy(String key, String field, double delta) {
        Double result = this.delegate.hIncrBy(serialize(key), serialize(field), delta);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> hKeys(String key) {
        Set<byte[]> results = this.delegate.hKeys(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long hLen(String key) {
        Long result = this.delegate.hLen(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> hMGet(String key, String... fields) {
        List<byte[]> results = this.delegate.hMGet(serialize(key), serializeMulti(fields));
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void hMSet(String key, Map<String, String> hashes) {
        this.delegate.hMSet(serialize(key), serialize(hashes));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean hSet(String key, String field, String value) {
        Boolean result = this.delegate.hSet(serialize(key), serialize(field), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean hSetNX(String key, String field, String value) {
        Boolean result = this.delegate.hSetNX(serialize(key), serialize(field), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> hVals(String key) {
        List<byte[]> results = this.delegate.hVals(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long incr(String key) {
        Long result = this.delegate.incr(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long incrBy(String key, long value) {
        Long result = this.delegate.incrBy(serialize(key), value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Double incrBy(String key, double value) {
        Double result = this.delegate.incrBy(serialize(key), value);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Collection<String> keys(String pattern) {
        Set<byte[]> results = this.delegate.keys(serialize(pattern));
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String lIndex(String key, long index) {
        byte[] result = this.delegate.lIndex(serialize(key), index);
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long lInsert(String key, RedisListCommands.Position where, String pivot, String value) {
        Long result = this.delegate.lInsert(serialize(key), where, serialize(pivot), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long lLen(String key) {
        Long result = this.delegate.lLen(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String lPop(String key) {
        byte[] result = this.delegate.lPop(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long lPush(String key, String... values) {
        Long result = this.delegate.lPush(serialize(key), serializeMulti(values));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long lPushX(String key, String value) {
        Long result = this.delegate.lPushX(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> lRange(String key, long start, long end) {
        List<byte[]> results = this.delegate.lRange(serialize(key), start, end);
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long lRem(String key, long count, String value) {
        Long result = this.delegate.lRem(serialize(key), count, serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void lSet(String key, long index, String value) {
        this.delegate.lSet(serialize(key), index, serialize(value));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void lTrim(String key, long start, long end) {
        this.delegate.lTrim(serialize(key), start, end);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> mGet(String... keys) {
        List<byte[]> results = this.delegate.mGet(serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean mSetNXString(Map<String, String> tuple) {
        Boolean result = this.delegate.mSetNX(serialize(tuple));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void mSetString(Map<String, String> tuple) {
        this.delegate.mSet(serialize(tuple));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean persist(String key) {
        Boolean result = this.delegate.persist(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean move(String key, int dbIndex) {
        Boolean result = this.delegate.move(serialize(key), dbIndex);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void pSubscribe(MessageListener listener, String... patterns) {
        this.delegate.pSubscribe(listener, serializeMulti(patterns));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long publish(String channel, String message) {
        Long result = this.delegate.publish(serialize(channel), serialize(message));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void rename(String oldName, String newName) {
        this.delegate.rename(serialize(oldName), serialize(newName));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean renameNX(String oldName, String newName) {
        Boolean result = this.delegate.renameNX(serialize(oldName), serialize(newName));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String rPop(String key) {
        byte[] result = this.delegate.rPop(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String rPopLPush(String srcKey, String dstKey) {
        byte[] result = this.delegate.rPopLPush(serialize(srcKey), serialize(dstKey));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long rPush(String key, String... values) {
        Long result = this.delegate.rPush(serialize(key), serializeMulti(values));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long rPushX(String key, String value) {
        Long result = this.delegate.rPushX(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long sAdd(String key, String... values) {
        Long result = this.delegate.sAdd(serialize(key), serializeMulti(values));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long sCard(String key) {
        Long result = this.delegate.sCard(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> sDiff(String... keys) {
        Set<byte[]> results = this.delegate.sDiff(serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long sDiffStore(String destKey, String... keys) {
        Long result = this.delegate.sDiffStore(serialize(destKey), serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void set(String key, String value) {
        this.delegate.set(serialize(key), serialize(value));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void set(String key, String value, Expiration expiration, RedisStringCommands.SetOption option) {
        set(serialize(key), serialize(value), expiration, option);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean setBit(String key, long offset, boolean value) {
        return this.delegate.setBit(serialize(key), offset, value);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void setEx(String key, long seconds, String value) {
        this.delegate.setEx(serialize(key), seconds, serialize(value));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void pSetEx(String key, long seconds, String value) {
        pSetEx(serialize(key), seconds, serialize(value));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean setNX(String key, String value) {
        Boolean result = this.delegate.setNX(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void setRange(String key, String value, long start) {
        this.delegate.setRange(serialize(key), serialize(value), start);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> sInter(String... keys) {
        Set<byte[]> results = this.delegate.sInter(serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long sInterStore(String destKey, String... keys) {
        Long result = this.delegate.sInterStore(serialize(destKey), serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean sIsMember(String key, String value) {
        Boolean result = this.delegate.sIsMember(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> sMembers(String key) {
        Set<byte[]> results = this.delegate.sMembers(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean sMove(String srcKey, String destKey, String value) {
        Boolean result = this.delegate.sMove(serialize(srcKey), serialize(destKey), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long sort(String key, SortParameters params, String storeKey) {
        Long result = this.delegate.sort(serialize(key), params, serialize(storeKey));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> sort(String key, SortParameters params) {
        List<byte[]> results = this.delegate.sort(serialize(key), params);
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String sPop(String key) {
        byte[] result = this.delegate.sPop(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String sRandMember(String key) {
        byte[] result = this.delegate.sRandMember(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.bytesToString);
        }
        return this.bytesToString.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> sRandMember(String key, long count) {
        List<byte[]> results = this.delegate.sRandMember(serialize(key), count);
        if (isFutureConversion()) {
            addResultConverter(this.byteListToStringList);
        }
        return this.byteListToStringList.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long sRem(String key, String... values) {
        Long result = this.delegate.sRem(serialize(key), serializeMulti(values));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long strLen(String key) {
        Long result = this.delegate.strLen(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long bitCount(String key) {
        Long result = this.delegate.bitCount(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long bitCount(String key, long begin, long end) {
        Long result = this.delegate.bitCount(serialize(key), begin, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long bitOp(RedisStringCommands.BitOperation op, String destination, String... keys) {
        Long result = this.delegate.bitOp(op, serialize(destination), serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void subscribe(MessageListener listener, String... channels) {
        this.delegate.subscribe(listener, serializeMulti(channels));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> sUnion(String... keys) {
        Set<byte[]> results = this.delegate.sUnion(serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long sUnionStore(String destKey, String... keys) {
        Long result = this.delegate.sUnionStore(serialize(destKey), serializeMulti(keys));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long ttl(String key) {
        return ttl(serialize(key));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long ttl(String key, TimeUnit timeUnit) {
        return ttl(serialize(key), timeUnit);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public DataType type(String key) {
        DataType result = this.delegate.type(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean zAdd(String key, double score, String value) {
        Boolean result = this.delegate.zAdd(serialize(key), score, serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zAdd(String key, Set<StringRedisConnection.StringTuple> tuples) {
        Long result = this.delegate.zAdd(serialize(key), this.stringTupleToTuple.convert(tuples));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zCard(String key) {
        Long result = this.delegate.zCard(serialize(key));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zCount(String key, double min, double max) {
        Long result = this.delegate.zCount(serialize(key), min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Double zIncrBy(String key, double increment, String value) {
        Double result = this.delegate.zIncrBy(serialize(key), increment, serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zInterStore(String destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, String... sets) {
        Long result = this.delegate.zInterStore(serialize(destKey), aggregate, weights, serializeMulti(sets));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zInterStore(String destKey, String... sets) {
        Long result = this.delegate.zInterStore(serialize(destKey), serializeMulti(sets));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRange(String key, long start, long end) {
        Set<byte[]> results = this.delegate.zRange(serialize(key), start, end);
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRangeByScore(String key, double min, double max, long offset, long count) {
        Set<byte[]> results = this.delegate.zRangeByScore(serialize(key), min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRangeByScore(String key, double min, double max) {
        Set<byte[]> results = this.delegate.zRangeByScore(serialize(key), min, max);
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<StringRedisConnection.StringTuple> zRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeByScoreWithScores(serialize(key), min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.tupleToStringTuple);
        }
        return this.tupleToStringTuple.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<StringRedisConnection.StringTuple> zRangeByScoreWithScores(String key, double min, double max) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeByScoreWithScores(serialize(key), min, max);
        if (isFutureConversion()) {
            addResultConverter(this.tupleToStringTuple);
        }
        return this.tupleToStringTuple.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<StringRedisConnection.StringTuple> zRangeWithScores(String key, long start, long end) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRangeWithScores(serialize(key), start, end);
        if (isFutureConversion()) {
            addResultConverter(this.tupleToStringTuple);
        }
        return this.tupleToStringTuple.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zRank(String key, String value) {
        Long result = this.delegate.zRank(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zRem(String key, String... values) {
        Long result = this.delegate.zRem(serialize(key), serializeMulti(values));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zRemRange(String key, long start, long end) {
        Long result = this.delegate.zRemRange(serialize(key), start, end);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zRemRangeByScore(String key, double min, double max) {
        Long result = this.delegate.zRemRangeByScore(serialize(key), min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRevRange(String key, long start, long end) {
        Set<byte[]> results = this.delegate.zRevRange(serialize(key), start, end);
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<StringRedisConnection.StringTuple> zRevRangeWithScores(String key, long start, long end) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeWithScores(serialize(key), start, end);
        if (isFutureConversion()) {
            addResultConverter(this.tupleToStringTuple);
        }
        return this.tupleToStringTuple.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRevRangeByScore(String key, double min, double max) {
        Set<byte[]> results = this.delegate.zRevRangeByScore(serialize(key), min, max);
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<StringRedisConnection.StringTuple> zRevRangeByScoreWithScores(String key, double min, double max) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeByScoreWithScores(serialize(key), min, max);
        if (isFutureConversion()) {
            addResultConverter(this.tupleToStringTuple);
        }
        return this.tupleToStringTuple.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRevRangeByScore(String key, double min, double max, long offset, long count) {
        Set<byte[]> results = this.delegate.zRevRangeByScore(serialize(key), min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<StringRedisConnection.StringTuple> zRevRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        Set<RedisZSetCommands.Tuple> results = this.delegate.zRevRangeByScoreWithScores(serialize(key), min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.tupleToStringTuple);
        }
        return this.tupleToStringTuple.convert(results);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zRevRank(String key, String value) {
        Long result = this.delegate.zRevRank(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Double zScore(String key, String value) {
        Double result = this.delegate.zScore(serialize(key), serialize(value));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zUnionStore(String destKey, RedisZSetCommands.Aggregate aggregate, int[] weights, String... sets) {
        Long result = this.delegate.zUnionStore(serialize(destKey), aggregate, weights, serializeMulti(sets));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long zUnionStore(String destKey, String... sets) {
        Long result = this.delegate.zUnionStore(serialize(destKey), serializeMulti(sets));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Point point, byte[] member) {
        Long result = this.delegate.geoAdd(key, point, member);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, RedisGeoCommands.GeoLocation<byte[]> location) {
        Long result = this.delegate.geoAdd(key, location);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long geoAdd(String key, Point point, String member) {
        return geoAdd(serialize(key), point, serialize(member));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long geoAdd(String key, RedisGeoCommands.GeoLocation<String> location) {
        Assert.notNull(location, "Location must not be null!");
        return geoAdd(key, location.getPoint(), location.getName());
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
        Long result = this.delegate.geoAdd(key, memberCoordinateMap);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoAdd(byte[] key, Iterable<RedisGeoCommands.GeoLocation<byte[]>> locations) {
        Long result = this.delegate.geoAdd(key, locations);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long geoAdd(String key, Map<String, Point> memberCoordinateMap) {
        Assert.notNull(memberCoordinateMap, "MemberCoordinateMap must not be null!");
        Map<byte[], Point> byteMap = new HashMap<>();
        for (Map.Entry<String, Point> entry : memberCoordinateMap.entrySet()) {
            byteMap.put(serialize(entry.getKey()), entry.getValue());
        }
        return geoAdd(serialize(key), byteMap);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long geoAdd(String key, Iterable<RedisGeoCommands.GeoLocation<String>> locations) {
        Assert.notNull(locations, "Locations must not be null!");
        Map<byte[], Point> byteMap = new HashMap<>();
        for (RedisGeoCommands.GeoLocation<String> location : locations) {
            byteMap.put(serialize(location.getName()), location.getPoint());
        }
        return geoAdd(serialize(key), byteMap);
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
        Distance result = this.delegate.geoDist(key, member1, member2);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Distance geoDist(String key, String member1, String member2) {
        return geoDist(serialize(key), serialize(member1), serialize(member2));
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
        Distance result = this.delegate.geoDist(key, member1, member2, metric);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Distance geoDist(String key, String member1, String member2, Metric metric) {
        return geoDist(serialize(key), serialize(member1), serialize(member2), metric);
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<String> geoHash(byte[] key, byte[]... members) {
        List<String> result = this.delegate.geoHash(key, members);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<String> geoHash(String key, String... members) {
        List<String> result = this.delegate.geoHash(serialize(key), serializeMulti(members));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public List<Point> geoPos(byte[] key, byte[]... members) {
        List<Point> result = this.delegate.geoPos(key, members);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public List<Point> geoPos(String key, String... members) {
        return geoPos(serialize(key), serializeMulti(members));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(String key, Circle within) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadius(serialize(key), within);
        if (isFutureConversion()) {
            addResultConverter(this.byteGeoResultsToStringGeoResults);
        }
        return this.byteGeoResultsToStringGeoResults.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(String key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadius(serialize(key), within, args);
        if (isFutureConversion()) {
            addResultConverter(this.byteGeoResultsToStringGeoResults);
        }
        return this.byteGeoResultsToStringGeoResults.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadiusByMember(String key, String member, double radius) {
        return geoRadiusByMember(key, member, new Distance(radius, RedisGeoCommands.DistanceUnit.METERS));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadiusByMember(String key, String member, Distance radius) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadiusByMember(serialize(key), serialize(member), radius);
        if (isFutureConversion()) {
            addResultConverter(this.byteGeoResultsToStringGeoResults);
        }
        return this.byteGeoResultsToStringGeoResults.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadiusByMember(String key, String member, Distance radius, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadiusByMember(serialize(key), serialize(member), radius, args);
        if (isFutureConversion()) {
            addResultConverter(this.byteGeoResultsToStringGeoResults);
        }
        return this.byteGeoResultsToStringGeoResults.convert2(result);
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadius(key, within);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadius(key, within, args);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
        return geoRadiusByMember(key, member, new Distance(radius, RedisGeoCommands.DistanceUnit.METERS));
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadiusByMember(key, member, radius);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoResults<RedisGeoCommands.GeoLocation<byte[]>> result = this.delegate.geoRadiusByMember(key, member, radius, args);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.RedisGeoCommands
    public Long geoRemove(byte[] key, byte[]... members) {
        return zRem(key, members);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long geoRemove(String key, String... members) {
        return geoRemove(serialize(key), serializeMulti(members));
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public List<Object> closePipeline() {
        try {
            return convertResults(this.delegate.closePipeline(), this.pipelineConverters);
        } finally {
            this.pipelineConverters.clear();
        }
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public boolean isPipelined() {
        return this.delegate.isPipelined();
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public void openPipeline() {
        this.delegate.openPipeline();
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Object execute(String command) {
        return execute(command, (byte[][]) null);
    }

    @Override // org.springframework.data.redis.connection.RedisCommands
    public Object execute(String command, byte[]... args) {
        Object result = this.delegate.execute(command, args);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Object execute(String command, String... args) {
        return execute(command, serializeMulti(args));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean pExpire(String key, long millis) {
        return pExpire(serialize(key), millis);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Boolean pExpireAt(String key, long unixTimeInMillis) {
        return pExpireAt(serialize(key), unixTimeInMillis);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long pTtl(String key) {
        return pTtl(serialize(key));
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long pTtl(String key, TimeUnit timeUnit) {
        return pTtl(serialize(key), timeUnit);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public String scriptLoad(String script) {
        String result = this.delegate.scriptLoad(serialize(script));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return result;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public <T> T eval(String str, ReturnType returnType, int i, String... strArr) {
        T t = (T) this.delegate.eval(serialize(str), returnType, i, serializeMulti(strArr));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return t;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public <T> T evalSha(String str, ReturnType returnType, int i, String... strArr) {
        T t = (T) this.delegate.evalSha(str, returnType, i, serializeMulti(strArr));
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return t;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public Long time() {
        return this.delegate.time();
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection, org.springframework.data.redis.connection.RedisServerCommands
    public List<RedisClientInfo> getClientList() {
        return this.delegate.getClientList();
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOf(String host, int port) {
        this.delegate.slaveOf(host, port);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void slaveOfNoOne() {
        this.delegate.slaveOfNoOne();
    }

    @Override // org.springframework.data.redis.connection.RedisKeyCommands
    public Cursor<byte[]> scan(ScanOptions options) {
        return this.delegate.scan(options);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Cursor<RedisZSetCommands.Tuple> zScan(byte[] key, ScanOptions options) {
        return this.delegate.zScan(key, options);
    }

    @Override // org.springframework.data.redis.connection.RedisSetCommands
    public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
        return this.delegate.sScan(key, options);
    }

    @Override // org.springframework.data.redis.connection.RedisHashCommands
    public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
        return this.delegate.hScan(key, options);
    }

    public void setDeserializePipelineAndTxResults(boolean deserializePipelineAndTxResults) {
        this.deserializePipelineAndTxResults = deserializePipelineAndTxResults;
    }

    private void addResultConverter(Converter<?, ?> converter) {
        if (isQueueing()) {
            this.txConverters.add(converter);
        } else {
            this.pipelineConverters.add(converter);
        }
    }

    private boolean isFutureConversion() {
        return isPipelined() || isQueueing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<Object> convertResults(List<Object> results, Queue<Converter> converters) {
        if (!this.deserializePipelineAndTxResults || results == null) {
            return results;
        }
        if (results.size() != converters.size()) {
            this.log.warn("Delegate returned an unexpected number of results. Abandoning type conversion.");
            return results;
        }
        List<Object> convertedResults = new ArrayList<>();
        for (Object result : results) {
            convertedResults.add(converters.remove().convert2(result));
        }
        return convertedResults;
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void setClientName(byte[] name) {
        this.delegate.setClientName(name);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void setClientName(String name) {
        setClientName(this.serializer.serialize(name));
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void killClient(String host, int port) {
        this.delegate.killClient(host, port);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public String getClientName() {
        return this.delegate.getClientName();
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Cursor<Map.Entry<String, String>> hScan(String key, ScanOptions options) {
        return new ConvertingCursor(this.delegate.hScan(serialize(key), options), new Converter<Map.Entry<byte[], byte[]>, Map.Entry<String, String>>() { // from class: org.springframework.data.redis.connection.DefaultStringRedisConnection.1
            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public Map.Entry<String, String> convert2(final Map.Entry<byte[], byte[]> source) {
                return new Map.Entry<String, String>() { // from class: org.springframework.data.redis.connection.DefaultStringRedisConnection.1.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Map.Entry
                    public String getKey() {
                        return (String) DefaultStringRedisConnection.this.bytesToString.convert2(source.getKey());
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Map.Entry
                    public String getValue() {
                        return (String) DefaultStringRedisConnection.this.bytesToString.convert2(source.getValue());
                    }

                    @Override // java.util.Map.Entry
                    public String setValue(String value) {
                        throw new UnsupportedOperationException("Cannot set value for entry in cursor");
                    }
                };
            }
        });
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Cursor<String> sScan(String key, ScanOptions options) {
        return new ConvertingCursor(this.delegate.sScan(serialize(key), options), this.bytesToString);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Cursor<StringRedisConnection.StringTuple> zScan(String key, ScanOptions options) {
        return new ConvertingCursor(this.delegate.zScan(serialize(key), options), new TupleConverter());
    }

    @Override // org.springframework.data.redis.connection.RedisConnection
    public RedisSentinelConnection getSentinelConnection() {
        return this.delegate.getSentinelConnection();
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<byte[]> zRangeByScore(String key, String min, String max) {
        Set<byte[]> results = this.delegate.zRangeByScore(serialize(key), min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<byte[]> zRangeByScore(String key, String min, String max, long offset, long count) {
        Set<byte[]> results = this.delegate.zRangeByScore(serialize(key), min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
        Set<byte[]> results = this.delegate.zRangeByScore(key, min, max);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
        Set<byte[]> results = this.delegate.zRangeByScore(key, min, max, offset, count);
        if (isFutureConversion()) {
            addResultConverter(this.identityConverter);
        }
        return results;
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfAdd(byte[] key, byte[]... values) {
        return this.delegate.pfAdd(key, values);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long pfAdd(String key, String... values) {
        return pfAdd(serialize(key), serializeMulti(values));
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public Long pfCount(byte[]... keys) {
        return this.delegate.pfCount(keys);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Long pfCount(String... keys) {
        return pfCount(serializeMulti(keys));
    }

    @Override // org.springframework.data.redis.connection.HyperLogLogCommands
    public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
        this.delegate.pfMerge(destinationKey, sourceKeys);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public void pfMerge(String destinationKey, String... sourceKeys) {
        pfMerge(serialize(destinationKey), serializeMulti(sourceKeys));
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key) {
        return this.delegate.zRangeByLex(key);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range) {
        return this.delegate.zRangeByLex(key, range);
    }

    @Override // org.springframework.data.redis.connection.RedisZSetCommands
    public Set<byte[]> zRangeByLex(byte[] key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        return this.delegate.zRangeByLex(key, range, limit);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRangeByLex(String key) {
        return zRangeByLex(key, RedisZSetCommands.Range.unbounded());
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRangeByLex(String key, RedisZSetCommands.Range range) {
        return zRangeByLex(key, range, (RedisZSetCommands.Limit) null);
    }

    @Override // org.springframework.data.redis.connection.StringRedisConnection
    public Set<String> zRangeByLex(String key, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit) {
        Set<byte[]> results = this.delegate.zRangeByLex(serialize(key), range);
        if (isFutureConversion()) {
            addResultConverter(this.byteSetToStringSet);
        }
        return this.byteSetToStringSet.convert(results);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option) {
        this.delegate.migrate(key, target, dbIndex, option);
    }

    @Override // org.springframework.data.redis.connection.RedisServerCommands
    public void migrate(byte[] key, RedisNode target, int dbIndex, RedisServerCommands.MigrateOption option, long timeout) {
        this.delegate.migrate(key, target, dbIndex, option, timeout);
    }

    @Override // org.springframework.data.redis.connection.DecoratedRedisConnection
    public RedisConnection getDelegate() {
        return this.delegate;
    }
}
