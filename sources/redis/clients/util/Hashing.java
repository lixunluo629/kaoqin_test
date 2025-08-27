package redis.clients.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* loaded from: jedis-2.9.3.jar:redis/clients/util/Hashing.class */
public interface Hashing {
    public static final Hashing MURMUR_HASH = new MurmurHash();
    public static final ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<>();
    public static final Hashing MD5 = new Hashing() { // from class: redis.clients.util.Hashing.1
        @Override // redis.clients.util.Hashing
        public long hash(String key) {
            return hash(SafeEncoder.encode(key));
        }

        @Override // redis.clients.util.Hashing
        public long hash(byte[] key) {
            try {
                if (md5Holder.get() == null) {
                    md5Holder.set(MessageDigest.getInstance(MessageDigestAlgorithms.MD5));
                }
                MessageDigest md5 = md5Holder.get();
                md5.reset();
                md5.update(key);
                byte[] bKey = md5.digest();
                long res = ((bKey[3] & 255) << 24) | ((bKey[2] & 255) << 16) | ((bKey[1] & 255) << 8) | (bKey[0] & 255);
                return res;
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("++++ no md5 algorythm found");
            }
        }
    };

    long hash(String str);

    long hash(byte[] bArr);
}
