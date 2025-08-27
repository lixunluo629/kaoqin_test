package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import java.security.Key;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/DefaultSignatureValidatorFactory.class */
public class DefaultSignatureValidatorFactory implements SignatureValidatorFactory {
    public static final SignatureValidatorFactory INSTANCE = new DefaultSignatureValidatorFactory();

    @Override // io.jsonwebtoken.impl.crypto.SignatureValidatorFactory
    public SignatureValidator createSignatureValidator(SignatureAlgorithm alg, Key key) {
        Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
        Assert.notNull(key, "Signing Key cannot be null.");
        switch (alg) {
            case HS256:
            case HS384:
            case HS512:
                return new MacValidator(alg, key);
            case RS256:
            case RS384:
            case RS512:
            case PS256:
            case PS384:
            case PS512:
                return new RsaSignatureValidator(alg, key);
            case ES256:
            case ES384:
            case ES512:
                return new EllipticCurveSignatureValidator(alg, key);
            default:
                throw new IllegalArgumentException("The '" + alg.name() + "' algorithm cannot be used for signing.");
        }
    }
}
