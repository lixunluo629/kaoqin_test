package org.bouncycastle.crypto.tls;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/CertChainType.class */
public class CertChainType {
    public static final short individual_certs = 0;
    public static final short pkipath = 1;

    public static boolean isValid(short s) {
        return s >= 0 && s <= 1;
    }
}
