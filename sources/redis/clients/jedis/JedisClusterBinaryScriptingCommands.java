package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisClusterBinaryScriptingCommands.class */
public interface JedisClusterBinaryScriptingCommands {
    Object eval(byte[] bArr, byte[] bArr2, byte[]... bArr3);

    Object eval(byte[] bArr, int i, byte[]... bArr2);

    Object eval(byte[] bArr, List<byte[]> list, List<byte[]> list2);

    Object eval(byte[] bArr, byte[] bArr2);

    Object evalsha(byte[] bArr, byte[] bArr2);

    Object evalsha(byte[] bArr, List<byte[]> list, List<byte[]> list2);

    Object evalsha(byte[] bArr, int i, byte[]... bArr2);

    List<Long> scriptExists(byte[] bArr, byte[][] bArr2);

    byte[] scriptLoad(byte[] bArr, byte[] bArr2);

    String scriptFlush(byte[] bArr);

    String scriptKill(byte[] bArr);
}
