package redis.clients.jedis.params.sortedset;

import java.util.ArrayList;
import redis.clients.jedis.params.Params;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/params/sortedset/ZAddParams.class */
public class ZAddParams extends Params {
    private static final String XX = "xx";
    private static final String NX = "nx";
    private static final String CH = "ch";

    private ZAddParams() {
    }

    public static ZAddParams zAddParams() {
        return new ZAddParams();
    }

    public ZAddParams nx() {
        addParam(NX);
        return this;
    }

    public ZAddParams xx() {
        addParam(XX);
        return this;
    }

    public ZAddParams ch() {
        addParam(CH);
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
        if (contains(CH)) {
            byteParams.add(SafeEncoder.encode(CH));
        }
        for (byte[] arg : args) {
            byteParams.add(arg);
        }
        return (byte[][]) byteParams.toArray((Object[]) new byte[byteParams.size()]);
    }
}
