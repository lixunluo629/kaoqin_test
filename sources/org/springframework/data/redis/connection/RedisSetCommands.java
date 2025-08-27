package org.springframework.data.redis.connection;

import java.util.List;
import java.util.Set;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisSetCommands.class */
public interface RedisSetCommands {
    Long sAdd(byte[] bArr, byte[]... bArr2);

    Long sRem(byte[] bArr, byte[]... bArr2);

    byte[] sPop(byte[] bArr);

    Boolean sMove(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Long sCard(byte[] bArr);

    Boolean sIsMember(byte[] bArr, byte[] bArr2);

    Set<byte[]> sInter(byte[]... bArr);

    Long sInterStore(byte[] bArr, byte[]... bArr2);

    Set<byte[]> sUnion(byte[]... bArr);

    Long sUnionStore(byte[] bArr, byte[]... bArr2);

    Set<byte[]> sDiff(byte[]... bArr);

    Long sDiffStore(byte[] bArr, byte[]... bArr2);

    Set<byte[]> sMembers(byte[] bArr);

    byte[] sRandMember(byte[] bArr);

    List<byte[]> sRandMember(byte[] bArr, long j);

    Cursor<byte[]> sScan(byte[] bArr, ScanOptions scanOptions);
}
