package org.springframework.data.redis.core.script;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/script/DigestUtils.class */
public abstract class DigestUtils {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final Charset UTF8_CHARSET = Charset.forName("UTF8");

    public static String sha1DigestAsHex(String data) {
        byte[] dataBytes = getDigest("SHA").digest(data.getBytes(UTF8_CHARSET));
        return new String(encodeHex(dataBytes));
    }

    private static char[] encodeHex(byte[] data) {
        int l = data.length;
        char[] out = new char[l << 1];
        int j = 0;
        for (int i = 0; i < l; i++) {
            int i2 = j;
            int j2 = j + 1;
            out[i2] = HEX_CHARS[(240 & data[i]) >>> 4];
            j = j2 + 1;
            out[j2] = HEX_CHARS[15 & data[i]];
        }
        return out;
    }

    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + SymbolConstants.QUOTES_SYMBOL, ex);
        }
    }
}
