package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.lang.Assert;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/EllipticCurveSignatureValidator.class */
public class EllipticCurveSignatureValidator extends EllipticCurveProvider implements SignatureValidator {
    private static final String EC_PUBLIC_KEY_REQD_MSG = "Elliptic Curve signature validation requires an ECPublicKey instance.";

    public EllipticCurveSignatureValidator(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue(key instanceof ECPublicKey, EC_PUBLIC_KEY_REQD_MSG);
    }

    @Override // io.jsonwebtoken.impl.crypto.SignatureValidator
    public boolean isValid(byte[] data, byte[] signature) {
        Signature sig = createSignatureInstance();
        PublicKey publicKey = (PublicKey) this.key;
        try {
            return doVerify(sig, publicKey, data, signature);
        } catch (Exception e) {
            String msg = "Unable to verify Elliptic Curve signature using configured ECPublicKey. " + e.getMessage();
            throw new SignatureException(msg, e);
        }
    }

    protected boolean doVerify(Signature sig, PublicKey publicKey, byte[] data, byte[] signature) throws java.security.SignatureException, InvalidKeyException {
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }
}
