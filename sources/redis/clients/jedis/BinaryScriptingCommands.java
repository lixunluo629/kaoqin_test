package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryScriptingCommands.class */
public interface BinaryScriptingCommands {
    Object eval(byte[] bArr, byte[] bArr2, byte[]... bArr3);

    Object eval(byte[] bArr, int i, byte[]... bArr2);

    Object eval(byte[] bArr, List<byte[]> list, List<byte[]> list2);

    Object eval(byte[] bArr);

    Object evalsha(byte[] bArr);

    Object evalsha(byte[] bArr, List<byte[]> list, List<byte[]> list2);

    Object evalsha(byte[] bArr, int i, byte[]... bArr2);

    List<Long> scriptExists(byte[]... bArr);

    byte[] scriptLoad(byte[] bArr);

    String scriptFlush();

    String scriptKill();
}
