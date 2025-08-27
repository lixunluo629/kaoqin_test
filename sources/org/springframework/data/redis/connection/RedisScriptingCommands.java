package org.springframework.data.redis.connection;

import java.util.List;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisScriptingCommands.class */
public interface RedisScriptingCommands {
    void scriptFlush();

    void scriptKill();

    String scriptLoad(byte[] bArr);

    List<Boolean> scriptExists(String... strArr);

    <T> T eval(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2);

    <T> T evalSha(String str, ReturnType returnType, int i, byte[]... bArr);

    <T> T evalSha(byte[] bArr, ReturnType returnType, int i, byte[]... bArr2);
}
