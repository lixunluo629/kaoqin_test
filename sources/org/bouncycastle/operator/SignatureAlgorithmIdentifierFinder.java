package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/operator/SignatureAlgorithmIdentifierFinder.class */
public interface SignatureAlgorithmIdentifierFinder {
    AlgorithmIdentifier find(String str);
}
