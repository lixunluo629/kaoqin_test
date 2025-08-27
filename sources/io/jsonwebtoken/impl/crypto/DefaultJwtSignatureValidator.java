package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Assert;
import java.nio.charset.Charset;
import java.security.Key;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/DefaultJwtSignatureValidator.class */
public class DefaultJwtSignatureValidator implements JwtSignatureValidator {
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private final SignatureValidator signatureValidator;

    public DefaultJwtSignatureValidator(SignatureAlgorithm alg, Key key) {
        this(DefaultSignatureValidatorFactory.INSTANCE, alg, key);
    }

    public DefaultJwtSignatureValidator(SignatureValidatorFactory factory, SignatureAlgorithm alg, Key key) {
        Assert.notNull(factory, "SignerFactory argument cannot be null.");
        this.signatureValidator = factory.createSignatureValidator(alg, key);
    }

    @Override // io.jsonwebtoken.impl.crypto.JwtSignatureValidator
    public boolean isValid(String jwtWithoutSignature, String base64UrlEncodedSignature) {
        byte[] data = jwtWithoutSignature.getBytes(US_ASCII);
        byte[] signature = TextCodec.BASE64URL.decode(base64UrlEncodedSignature);
        return this.signatureValidator.isValid(data, signature);
    }
}
