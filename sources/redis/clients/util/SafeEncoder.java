package redis.clients.util;

import java.io.UnsupportedEncodingException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/SafeEncoder.class */
public final class SafeEncoder {
    private SafeEncoder() {
        throw new InstantiationError("Must not instantiate this class");
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [byte[], byte[][]] */
    public static byte[][] encodeMany(String... strs) {
        ?? r0 = new byte[strs.length];
        for (int i = 0; i < strs.length; i++) {
            r0[i] = encode(strs[i]);
        }
        return r0;
    }

    public static byte[] encode(String str) {
        try {
            if (str == null) {
                throw new JedisDataException("value sent to redis cannot be null");
            }
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new JedisException(e);
        }
    }

    public static String encode(byte[] data) {
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new JedisException(e);
        }
    }
}
