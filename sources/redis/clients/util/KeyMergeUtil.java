package redis.clients.util;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/KeyMergeUtil.class */
public final class KeyMergeUtil {
    private KeyMergeUtil() {
        throw new InstantiationError("Must not instantiate this class");
    }

    public static String[] merge(String destKey, String[] keys) {
        String[] mergedKeys = new String[keys.length + 1];
        mergedKeys[0] = destKey;
        System.arraycopy(keys, 0, mergedKeys, 1, keys.length);
        return mergedKeys;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][], java.lang.Object] */
    public static byte[][] merge(byte[] destKey, byte[][] keys) {
        ?? r0 = new byte[keys.length + 1];
        r0[0] = destKey;
        System.arraycopy(keys, 0, r0, 1, keys.length);
        return r0;
    }
}
