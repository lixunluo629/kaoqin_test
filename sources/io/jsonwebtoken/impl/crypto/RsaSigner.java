package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/RsaSigner.class */
public class RsaSigner extends RsaProvider implements Signer {
    public RsaSigner(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        if (!(key instanceof RSAPrivateKey)) {
            String msg = "RSA signatures must be computed using an RSAPrivateKey.  The specified key of type " + key.getClass().getName() + " is not an RSAPrivateKey.";
            throw new IllegalArgumentException(msg);
        }
    }

    @Override // io.jsonwebtoken.impl.crypto.Signer
    public byte[] sign(byte[] data) {
        try {
            return doSign(data);
        } catch (InvalidKeyException e) {
            throw new SignatureException("Invalid RSA PrivateKey. " + e.getMessage(), e);
        } catch (java.security.SignatureException e2) {
            throw new SignatureException("Unable to calculate signature using RSA PrivateKey. " + e2.getMessage(), e2);
        }
    }

    protected byte[] doSign(byte[] data) throws java.security.SignatureException, InvalidKeyException {
        PrivateKey privateKey = (PrivateKey) this.key;
        Signature sig = createSignatureInstance();
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }
}
