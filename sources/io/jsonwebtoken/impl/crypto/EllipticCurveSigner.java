package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/EllipticCurveSigner.class */
public class EllipticCurveSigner extends EllipticCurveProvider implements Signer {
    public EllipticCurveSigner(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        if (!(key instanceof ECPrivateKey)) {
            String msg = "Elliptic Curve signatures must be computed using an ECPrivateKey.  The specified key of type " + key.getClass().getName() + " is not an ECPrivateKey.";
            throw new IllegalArgumentException(msg);
        }
    }

    @Override // io.jsonwebtoken.impl.crypto.Signer
    public byte[] sign(byte[] data) {
        try {
            return doSign(data);
        } catch (InvalidKeyException e) {
            throw new SignatureException("Invalid Elliptic Curve PrivateKey. " + e.getMessage(), e);
        } catch (java.security.SignatureException e2) {
            throw new SignatureException("Unable to calculate signature using Elliptic Curve PrivateKey. " + e2.getMessage(), e2);
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
