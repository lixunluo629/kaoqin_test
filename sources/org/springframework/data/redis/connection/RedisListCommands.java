package org.springframework.data.redis.connection;

import java.util.List;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisListCommands.class */
public interface RedisListCommands {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisListCommands$Position.class */
    public enum Position {
        BEFORE,
        AFTER
    }

    Long rPush(byte[] bArr, byte[]... bArr2);

    Long lPush(byte[] bArr, byte[]... bArr2);

    Long rPushX(byte[] bArr, byte[] bArr2);

    Long lPushX(byte[] bArr, byte[] bArr2);

    Long lLen(byte[] bArr);

    List<byte[]> lRange(byte[] bArr, long j, long j2);

    void lTrim(byte[] bArr, long j, long j2);

    byte[] lIndex(byte[] bArr, long j);

    Long lInsert(byte[] bArr, Position position, byte[] bArr2, byte[] bArr3);

    void lSet(byte[] bArr, long j, byte[] bArr2);

    Long lRem(byte[] bArr, long j, byte[] bArr2);

    byte[] lPop(byte[] bArr);

    byte[] rPop(byte[] bArr);

    List<byte[]> bLPop(int i, byte[]... bArr);

    List<byte[]> bRPop(int i, byte[]... bArr);

    byte[] rPopLPush(byte[] bArr, byte[] bArr2);

    byte[] bRPopLPush(int i, byte[] bArr, byte[] bArr2);
}
