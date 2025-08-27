package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/MacProvider.class */
public abstract class MacProvider extends SignatureProvider {
    protected MacProvider(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(alg.isHmac(), "SignatureAlgorithm must be a HMAC SHA algorithm.");
    }

    public static SecretKey generateKey() {
        return generateKey(SignatureAlgorithm.HS512);
    }

    public static SecretKey generateKey(SignatureAlgorithm alg) {
        return generateKey(alg, SignatureProvider.DEFAULT_SECURE_RANDOM);
    }

    public static SecretKey generateKey(SignatureAlgorithm alg, SecureRandom random) {
        byte[] bytes;
        Assert.isTrue(alg.isHmac(), "SignatureAlgorithm argument must represent an HMAC algorithm.");
        switch (alg) {
            case HS256:
                bytes = new byte[32];
                break;
            case HS384:
                bytes = new byte[48];
                break;
            default:
                bytes = new byte[64];
                break;
        }
        random.nextBytes(bytes);
        return new SecretKeySpec(bytes, alg.getJcaName());
    }
}
