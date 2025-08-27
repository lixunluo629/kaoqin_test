package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.lang.Assert;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/RsaSignatureValidator.class */
public class RsaSignatureValidator extends RsaProvider implements SignatureValidator {
    private final RsaSigner SIGNER;

    public RsaSignatureValidator(SignatureAlgorithm alg, Key key) {
        super(alg, key);
        Assert.isTrue((key instanceof RSAPrivateKey) || (key instanceof RSAPublicKey), "RSA Signature validation requires either a RSAPublicKey or RSAPrivateKey instance.");
        this.SIGNER = key instanceof RSAPrivateKey ? new RsaSigner(alg, key) : null;
    }

    @Override // io.jsonwebtoken.impl.crypto.SignatureValidator
    public boolean isValid(byte[] data, byte[] signature) {
        if (this.key instanceof PublicKey) {
            Signature sig = createSignatureInstance();
            PublicKey publicKey = (PublicKey) this.key;
            try {
                return doVerify(sig, publicKey, data, signature);
            } catch (Exception e) {
                String msg = "Unable to verify RSA signature using configured PublicKey. " + e.getMessage();
                throw new SignatureException(msg, e);
            }
        }
        Assert.notNull(this.SIGNER, "RSA Signer instance cannot be null.  This is a bug.  Please report it.");
        byte[] computed = this.SIGNER.sign(data);
        return Arrays.equals(computed, signature);
    }

    protected boolean doVerify(Signature sig, PublicKey publicKey, byte[] data, byte[] signature) throws java.security.SignatureException, InvalidKeyException {
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }
}
