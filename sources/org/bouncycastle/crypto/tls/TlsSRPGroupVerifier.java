package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.params.SRP6GroupParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsSRPGroupVerifier.class */
public interface TlsSRPGroupVerifier {
    boolean accept(SRP6GroupParameters sRP6GroupParameters);
}
