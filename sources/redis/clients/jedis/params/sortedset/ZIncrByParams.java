package redis.clients.jedis.params.sortedset;

import java.util.ArrayList;
import redis.clients.jedis.params.Params;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/params/sortedset/ZIncrByParams.class */
public class ZIncrByParams extends Params {
    private static final String XX = "xx";
    private static final String NX = "nx";
    private static final String INCR = "incr";

    private ZIncrByParams() {
    }

    public static ZIncrByParams zIncrByParams() {
        return new ZIncrByParams();
    }

    public ZIncrByParams nx() {
        addParam(NX);
        return this;
    }

    public ZIncrByParams xx() {
        addParam(XX);
        return this;
    }

    public byte[][] getByteParams(byte[] key, byte[]... args) {
        ArrayList<byte[]> byteParams = new ArrayList<>();
        byteParams.add(key);
        if (contains(NX)) {
            byteParams.add(SafeEncoder.encode(NX));
        }
        if (contains(XX)) {
            byteParams.add(SafeEncoder.encode(XX));
        }
        byteParams.add(SafeEncoder.encode(INCR));
        for (byte[] arg : args) {
            byteParams.add(arg);
        }
        return (byte[][]) byteParams.toArray((Object[]) new byte[byteParams.size()]);
    }
}
