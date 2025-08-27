package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/redis/FixedRedisMessagePool.class */
public final class FixedRedisMessagePool implements RedisMessagePool {
    private static final long MIN_CACHED_INTEGER_NUMBER = -1;
    private static final long MAX_CACHED_INTEGER_NUMBER = 128;
    private static final int SIZE_CACHED_INTEGER_NUMBER = 129;
    private final Map<ByteBuf, SimpleStringRedisMessage> byteBufToSimpleStrings = new HashMap(DEFAULT_SIMPLE_STRINGS.length, 1.0f);
    private final Map<String, SimpleStringRedisMessage> stringToSimpleStrings = new HashMap(DEFAULT_SIMPLE_STRINGS.length, 1.0f);
    private final Map<ByteBuf, ErrorRedisMessage> byteBufToErrors;
    private final Map<String, ErrorRedisMessage> stringToErrors;
    private final Map<ByteBuf, IntegerRedisMessage> byteBufToIntegers;
    private final LongObjectMap<IntegerRedisMessage> longToIntegers;
    private final LongObjectMap<byte[]> longToByteBufs;
    private static final String[] DEFAULT_SIMPLE_STRINGS = {"OK", LettuceConnectionFactory.PING_REPLY, "QUEUED"};
    private static final String[] DEFAULT_ERRORS = {"ERR", "ERR index out of range", "ERR no such key", "ERR source and destination objects are the same", "ERR syntax error", "BUSY Redis is busy running a script. You can only call SCRIPT KILL or SHUTDOWN NOSAVE.", "BUSYKEY Target key name already exists.", "EXECABORT Transaction discarded because of previous errors.", "LOADING Redis is loading the dataset in memory", "MASTERDOWN Link with MASTER is down and slave-serve-stale-data is set to 'no'.", "MISCONF Redis is configured to save RDB snapshots, but is currently not able to persist on disk. Commands that may modify the data set are disabled. Please check Redis logs for details about the error.", "NOAUTH Authentication required.", "NOREPLICAS Not enough good slaves to write.", "NOSCRIPT No matching script. Please use EVAL.", "OOM command not allowed when used memory > 'maxmemory'.", "READONLY You can't write against a read only slave.", "WRONGTYPE Operation against a key holding the wrong kind of value"};
    public static final FixedRedisMessagePool INSTANCE = new FixedRedisMessagePool();

    private FixedRedisMessagePool() {
        for (String message : DEFAULT_SIMPLE_STRINGS) {
            ByteBuf key = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(message.getBytes(CharsetUtil.UTF_8))));
            SimpleStringRedisMessage cached = new SimpleStringRedisMessage(message);
            this.byteBufToSimpleStrings.put(key, cached);
            this.stringToSimpleStrings.put(message, cached);
        }
        this.byteBufToErrors = new HashMap(DEFAULT_ERRORS.length, 1.0f);
        this.stringToErrors = new HashMap(DEFAULT_ERRORS.length, 1.0f);
        for (String message2 : DEFAULT_ERRORS) {
            ByteBuf key2 = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(message2.getBytes(CharsetUtil.UTF_8))));
            ErrorRedisMessage cached2 = new ErrorRedisMessage(message2);
            this.byteBufToErrors.put(key2, cached2);
            this.stringToErrors.put(message2, cached2);
        }
        this.byteBufToIntegers = new HashMap(129, 1.0f);
        this.longToIntegers = new LongObjectHashMap(129, 1.0f);
        this.longToByteBufs = new LongObjectHashMap(129, 1.0f);
        long j = -1;
        while (true) {
            long value = j;
            if (value < MAX_CACHED_INTEGER_NUMBER) {
                byte[] keyBytes = RedisCodecUtil.longToAsciiBytes(value);
                ByteBuf keyByteBuf = Unpooled.unmodifiableBuffer(Unpooled.unreleasableBuffer(Unpooled.wrappedBuffer(keyBytes)));
                IntegerRedisMessage cached3 = new IntegerRedisMessage(value);
                this.byteBufToIntegers.put(keyByteBuf, cached3);
                this.longToIntegers.put(value, (long) cached3);
                this.longToByteBufs.put(value, (long) keyBytes);
                j = value + 1;
            } else {
                return;
            }
        }
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public SimpleStringRedisMessage getSimpleString(String content) {
        return this.stringToSimpleStrings.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public SimpleStringRedisMessage getSimpleString(ByteBuf content) {
        return this.byteBufToSimpleStrings.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public ErrorRedisMessage getError(String content) {
        return this.stringToErrors.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public ErrorRedisMessage getError(ByteBuf content) {
        return this.byteBufToErrors.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public IntegerRedisMessage getInteger(long value) {
        return this.longToIntegers.get(value);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public IntegerRedisMessage getInteger(ByteBuf content) {
        return this.byteBufToIntegers.get(content);
    }

    @Override // io.netty.handler.codec.redis.RedisMessagePool
    public byte[] getByteBufOfInteger(long value) {
        return this.longToByteBufs.get(value);
    }
}
