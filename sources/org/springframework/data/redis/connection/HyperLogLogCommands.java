package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/HyperLogLogCommands.class */
public interface HyperLogLogCommands {
    Long pfAdd(byte[] bArr, byte[]... bArr2);

    Long pfCount(byte[]... bArr);

    void pfMerge(byte[] bArr, byte[]... bArr2);
}
