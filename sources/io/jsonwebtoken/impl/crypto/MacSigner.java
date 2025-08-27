package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.lang.Assert;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/MacSigner.class */
public class MacSigner extends MacProvider implements Signer {
    public MacSigner(SignatureAlgorithm alg, byte[] key) {
        this(alg, new SecretKeySpec(key, alg.getJcaName()));
    }

    public MacSigner(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(alg.isHmac(), "The MacSigner only supports HMAC signature algorithms.");
        if (!(key instanceof SecretKey)) {
            String msg = "MAC signatures must be computed and verified using a SecretKey.  The specified key of type " + key.getClass().getName() + " is not a SecretKey.";
            throw new IllegalArgumentException(msg);
        }
    }

    @Override // io.jsonwebtoken.impl.crypto.Signer
    public byte[] sign(byte[] data) throws SignatureException {
        Mac mac = getMacInstance();
        return mac.doFinal(data);
    }

    protected Mac getMacInstance() throws SignatureException {
        try {
            return doGetMacInstance();
        } catch (InvalidKeyException e) {
            String msg = "The specified signing key is not a valid " + this.alg.name() + " key: " + e.getMessage();
            throw new SignatureException(msg, e);
        } catch (NoSuchAlgorithmException e2) {
            String msg2 = "Unable to obtain JCA MAC algorithm '" + this.alg.getJcaName() + "': " + e2.getMessage();
            throw new SignatureException(msg2, e2);
        }
    }

    protected Mac doGetMacInstance() throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(this.alg.getJcaName());
        mac.init(this.key);
        return mac;
    }
}
