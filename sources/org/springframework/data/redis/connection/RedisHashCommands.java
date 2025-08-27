package org.springframework.data.redis.connection;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisHashCommands.class */
public interface RedisHashCommands {
    Boolean hSet(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Boolean hSetNX(byte[] bArr, byte[] bArr2, byte[] bArr3);

    byte[] hGet(byte[] bArr, byte[] bArr2);

    List<byte[]> hMGet(byte[] bArr, byte[]... bArr2);

    void hMSet(byte[] bArr, Map<byte[], byte[]> map);

    Long hIncrBy(byte[] bArr, byte[] bArr2, long j);

    Double hIncrBy(byte[] bArr, byte[] bArr2, double d);

    Boolean hExists(byte[] bArr, byte[] bArr2);

    Long hDel(byte[] bArr, byte[]... bArr2);

    Long hLen(byte[] bArr);

    Set<byte[]> hKeys(byte[] bArr);

    List<byte[]> hVals(byte[] bArr);

    Map<byte[], byte[]> hGetAll(byte[] bArr);

    Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] bArr, ScanOptions scanOptions);
}
