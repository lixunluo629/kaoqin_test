package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryScriptingCommandsPipeline.class */
public interface BinaryScriptingCommandsPipeline {
    Response<Object> eval(byte[] bArr, byte[] bArr2, byte[]... bArr3);

    Response<Object> eval(byte[] bArr, int i, byte[]... bArr2);

    Response<Object> eval(byte[] bArr, List<byte[]> list, List<byte[]> list2);

    Response<Object> eval(byte[] bArr);

    Response<Object> evalsha(byte[] bArr);

    Response<Object> evalsha(byte[] bArr, List<byte[]> list, List<byte[]> list2);

    Response<Object> evalsha(byte[] bArr, int i, byte[]... bArr2);
}
