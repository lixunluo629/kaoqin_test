package org.bouncycastle.crypto.tls;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/NameType.class */
public class NameType {
    public static final short host_name = 0;

    public static boolean isValid(short s) {
        return s == 0;
    }
}
