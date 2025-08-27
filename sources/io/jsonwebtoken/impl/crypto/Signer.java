package io.jsonwebtoken.impl.crypto;

import io.jsonwebtoken.SignatureException;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/crypto/Signer.class */
public interface Signer {
    byte[] sign(byte[] bArr) throws SignatureException;
}
