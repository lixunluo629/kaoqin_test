package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.lang.Assert;
import java.nio.charset.Charset;
import java.security.Key;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/DefaultJwtSigner.class */
public class DefaultJwtSigner implements JwtSigner {
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private final Signer signer;

    public DefaultJwtSigner(SignatureAlgorithm alg, Key key) {
        this(DefaultSignerFactory.INSTANCE, alg, key);
    }

    public DefaultJwtSigner(SignerFactory factory, SignatureAlgorithm alg, Key key) {
        Assert.notNull(factory, "SignerFactory argument cannot be null.");
        this.signer = factory.createSigner(alg, key);
    }

    @Override // io.jsonwebtoken.impl.crypto.JwtSigner
    public String sign(String jwtWithoutSignature) throws SignatureException {
        byte[] bytesToSign = jwtWithoutSignature.getBytes(US_ASCII);
        byte[] signature = this.signer.sign(bytesToSign);
        return TextCodec.BASE64URL.encode(signature);
    }
}
