package redis.clients.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/MurmurHash.class */
public class MurmurHash implements Hashing {
    public static int hash(byte[] data, int seed) {
        return hash(ByteBuffer.wrap(data), seed);
    }

    public static int hash(byte[] data, int offset, int length, int seed) {
        return hash(ByteBuffer.wrap(data, offset, length), seed);
    }

    public static int hash(ByteBuffer buf, int seed) {
        int h;
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);
        int i = seed;
        int iRemaining = buf.remaining();
        while (true) {
            h = i ^ iRemaining;
            if (buf.remaining() < 4) {
                break;
            }
            int k = buf.getInt() * 1540483477;
            int k2 = (k ^ (k >>> 24)) * 1540483477;
            i = h * 1540483477;
            iRemaining = k2;
        }
        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h = (h ^ finish.getInt()) * 1540483477;
        }
        int h2 = (h ^ (h >>> 13)) * 1540483477;
        int h3 = h2 ^ (h2 >>> 15);
        buf.order(byteOrder);
        return h3;
    }

    public static long hash64A(byte[] data, int seed) {
        return hash64A(ByteBuffer.wrap(data), seed);
    }

    public static long hash64A(byte[] data, int offset, int length, int seed) {
        return hash64A(ByteBuffer.wrap(data, offset, length), seed);
    }

    public static long hash64A(ByteBuffer buf, int seed) {
        long h;
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);
        long jRemaining = seed ^ (buf.remaining() * (-4132994306676758123L));
        while (true) {
            h = jRemaining;
            if (buf.remaining() < 8) {
                break;
            }
            long k = buf.getLong() * (-4132994306676758123L);
            jRemaining = (h ^ ((k ^ (k >>> 47)) * (-4132994306676758123L))) * (-4132994306676758123L);
        }
        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h = (h ^ finish.getLong()) * (-4132994306676758123L);
        }
        long h2 = (h ^ (h >>> 47)) * (-4132994306676758123L);
        long h3 = h2 ^ (h2 >>> 47);
        buf.order(byteOrder);
        return h3;
    }

    @Override // redis.clients.util.Hashing
    public long hash(byte[] key) {
        return hash64A(key, 305441741);
    }

    @Override // redis.clients.util.Hashing
    public long hash(String key) {
        return hash(SafeEncoder.encode(key));
    }
}
