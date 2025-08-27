package org.springframework.data.redis.connection;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisKeyCommands.class */
public interface RedisKeyCommands {
    Boolean exists(byte[] bArr);

    Long del(byte[]... bArr);

    DataType type(byte[] bArr);

    Set<byte[]> keys(byte[] bArr);

    Cursor<byte[]> scan(ScanOptions scanOptions);

    byte[] randomKey();

    void rename(byte[] bArr, byte[] bArr2);

    Boolean renameNX(byte[] bArr, byte[] bArr2);

    Boolean expire(byte[] bArr, long j);

    Boolean pExpire(byte[] bArr, long j);

    Boolean expireAt(byte[] bArr, long j);

    Boolean pExpireAt(byte[] bArr, long j);

    Boolean persist(byte[] bArr);

    Boolean move(byte[] bArr, int i);

    Long ttl(byte[] bArr);

    Long ttl(byte[] bArr, TimeUnit timeUnit);

    Long pTtl(byte[] bArr);

    Long pTtl(byte[] bArr, TimeUnit timeUnit);

    List<byte[]> sort(byte[] bArr, SortParameters sortParameters);

    Long sort(byte[] bArr, SortParameters sortParameters, byte[] bArr2);

    byte[] dump(byte[] bArr);

    void restore(byte[] bArr, long j, byte[] bArr2);
}
