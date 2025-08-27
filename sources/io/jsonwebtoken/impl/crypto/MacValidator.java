package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.security.Key;
import java.security.MessageDigest;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/MacValidator.class */
public class MacValidator implements SignatureValidator {
    private final MacSigner signer;

    public MacValidator(SignatureAlgorithm alg, Key key) {
        this.signer = new MacSigner(alg, key);
    }

    @Override // io.jsonwebtoken.impl.crypto.SignatureValidator
    public boolean isValid(byte[] data, byte[] signature) throws SignatureException {
        byte[] computed = this.signer.sign(data);
        return MessageDigest.isEqual(computed, signature);
    }
}
