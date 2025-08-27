package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/AdvancedBinaryJedisCommands.class */
public interface AdvancedBinaryJedisCommands {
    List<byte[]> configGet(byte[] bArr);

    byte[] configSet(byte[] bArr, byte[] bArr2);

    String slowlogReset();

    Long slowlogLen();

    List<byte[]> slowlogGetBinary();

    List<byte[]> slowlogGetBinary(long j);

    Long objectRefcount(byte[] bArr);

    byte[] objectEncoding(byte[] bArr);

    Long objectIdletime(byte[] bArr);
}
