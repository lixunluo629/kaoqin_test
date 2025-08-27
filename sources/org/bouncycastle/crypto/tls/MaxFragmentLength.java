package org.bouncycastle.crypto.tls;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/MaxFragmentLength.class */
public class MaxFragmentLength {
    public static final short pow2_9 = 1;
    public static final short pow2_10 = 2;
    public static final short pow2_11 = 3;
    public static final short pow2_12 = 4;

    public static boolean isValid(short s) {
        return s >= 1 && s <= 4;
    }
}
