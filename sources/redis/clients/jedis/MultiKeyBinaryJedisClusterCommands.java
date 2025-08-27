package redis.clients.jedis;

import java.util.List;
import java.util.Set;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/MultiKeyBinaryJedisClusterCommands.class */
public interface MultiKeyBinaryJedisClusterCommands {
    Long exists(byte[]... bArr);

    Long del(byte[]... bArr);

    List<byte[]> blpop(int i, byte[]... bArr);

    List<byte[]> brpop(int i, byte[]... bArr);

    List<byte[]> mget(byte[]... bArr);

    String mset(byte[]... bArr);

    Long msetnx(byte[]... bArr);

    String rename(byte[] bArr, byte[] bArr2);

    Long renamenx(byte[] bArr, byte[] bArr2);

    byte[] rpoplpush(byte[] bArr, byte[] bArr2);

    Set<byte[]> sdiff(byte[]... bArr);

    Long sdiffstore(byte[] bArr, byte[]... bArr2);

    Set<byte[]> sinter(byte[]... bArr);

    Long sinterstore(byte[] bArr, byte[]... bArr2);

    Long smove(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Long sort(byte[] bArr, SortingParams sortingParams, byte[] bArr2);

    Long sort(byte[] bArr, byte[] bArr2);

    Set<byte[]> sunion(byte[]... bArr);

    Long sunionstore(byte[] bArr, byte[]... bArr2);

    Long zinterstore(byte[] bArr, byte[]... bArr2);

    Long zinterstore(byte[] bArr, ZParams zParams, byte[]... bArr2);

    Long zunionstore(byte[] bArr, byte[]... bArr2);

    Long zunionstore(byte[] bArr, ZParams zParams, byte[]... bArr2);

    byte[] brpoplpush(byte[] bArr, byte[] bArr2, int i);

    Long publish(byte[] bArr, byte[] bArr2);

    void subscribe(BinaryJedisPubSub binaryJedisPubSub, byte[]... bArr);

    void psubscribe(BinaryJedisPubSub binaryJedisPubSub, byte[]... bArr);

    Long bitop(BitOP bitOP, byte[] bArr, byte[]... bArr2);

    String pfmerge(byte[] bArr, byte[]... bArr2);

    Long pfcount(byte[]... bArr);
}
