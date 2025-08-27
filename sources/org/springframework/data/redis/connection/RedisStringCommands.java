package org.springframework.data.redis.connection;

import java.util.List;
import java.util.Map;
import org.springframework.data.redis.core.types.Expiration;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisStringCommands.class */
public interface RedisStringCommands {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisStringCommands$BitOperation.class */
    public enum BitOperation {
        AND,
        OR,
        XOR,
        NOT
    }

    byte[] get(byte[] bArr);

    byte[] getSet(byte[] bArr, byte[] bArr2);

    List<byte[]> mGet(byte[]... bArr);

    void set(byte[] bArr, byte[] bArr2);

    void set(byte[] bArr, byte[] bArr2, Expiration expiration, SetOption setOption);

    Boolean setNX(byte[] bArr, byte[] bArr2);

    void setEx(byte[] bArr, long j, byte[] bArr2);

    void pSetEx(byte[] bArr, long j, byte[] bArr2);

    void mSet(Map<byte[], byte[]> map);

    Boolean mSetNX(Map<byte[], byte[]> map);

    Long incr(byte[] bArr);

    Long incrBy(byte[] bArr, long j);

    Double incrBy(byte[] bArr, double d);

    Long decr(byte[] bArr);

    Long decrBy(byte[] bArr, long j);

    Long append(byte[] bArr, byte[] bArr2);

    byte[] getRange(byte[] bArr, long j, long j2);

    void setRange(byte[] bArr, byte[] bArr2, long j);

    Boolean getBit(byte[] bArr, long j);

    Boolean setBit(byte[] bArr, long j, boolean z);

    Long bitCount(byte[] bArr);

    Long bitCount(byte[] bArr, long j, long j2);

    Long bitOp(BitOperation bitOperation, byte[] bArr, byte[]... bArr2);

    Long strLen(byte[] bArr);

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisStringCommands$SetOption.class */
    public enum SetOption {
        UPSERT,
        SET_IF_ABSENT,
        SET_IF_PRESENT;

        public static SetOption upsert() {
            return UPSERT;
        }

        public static SetOption ifPresent() {
            return SET_IF_PRESENT;
        }

        public static SetOption ifAbsent() {
            return SET_IF_ABSENT;
        }
    }
}
