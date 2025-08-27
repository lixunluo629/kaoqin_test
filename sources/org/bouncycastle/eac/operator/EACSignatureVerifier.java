package org.bouncycastle.eac.operator;

import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/eac/operator/EACSignatureVerifier.class */
public interface EACSignatureVerifier {
    ASN1ObjectIdentifier getUsageIdentifier();

    OutputStream getOutputStream();

    boolean verify(byte[] bArr);
}
