package io.jsonwebtoken;

import io.jsonwebtoken.lang.Assert;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/SigningKeyResolverAdapter.class */
public class SigningKeyResolverAdapter implements SigningKeyResolver {
    @Override // io.jsonwebtoken.SigningKeyResolver
    public Key resolveSigningKey(JwsHeader header, Claims claims) throws SignatureException {
        SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
        Assert.isTrue(alg.isHmac(), "The default resolveSigningKey(JwsHeader, Claims) implementation cannot be used for asymmetric key algorithms (RSA, Elliptic Curve).  Override the resolveSigningKey(JwsHeader, Claims) method instead and return a Key instance appropriate for the " + alg.name() + " algorithm.");
        byte[] keyBytes = resolveSigningKeyBytes(header, claims);
        return new SecretKeySpec(keyBytes, alg.getJcaName());
    }

    @Override // io.jsonwebtoken.SigningKeyResolver
    public Key resolveSigningKey(JwsHeader header, String plaintext) throws SignatureException {
        SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
        Assert.isTrue(alg.isHmac(), "The default resolveSigningKey(JwsHeader, String) implementation cannot be used for asymmetric key algorithms (RSA, Elliptic Curve).  Override the resolveSigningKey(JwsHeader, String) method instead and return a Key instance appropriate for the " + alg.name() + " algorithm.");
        byte[] keyBytes = resolveSigningKeyBytes(header, plaintext);
        return new SecretKeySpec(keyBytes, alg.getJcaName());
    }

    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
        throw new UnsupportedJwtException("The specified SigningKeyResolver implementation does not support Claims JWS signing key resolution.  Consider overriding either the resolveSigningKey(JwsHeader, Claims) method or, for HMAC algorithms, the resolveSigningKeyBytes(JwsHeader, Claims) method.");
    }

    public byte[] resolveSigningKeyBytes(JwsHeader header, String payload) {
        throw new UnsupportedJwtException("The specified SigningKeyResolver implementation does not support plaintext JWS signing key resolution.  Consider overriding either the resolveSigningKey(JwsHeader, String) method or, for HMAC algorithms, the resolveSigningKeyBytes(JwsHeader, String) method.");
    }
}
