package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.params.DHParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsDHVerifier.class */
public interface TlsDHVerifier {
    boolean accept(DHParameters dHParameters);
}
