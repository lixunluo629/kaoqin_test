package org.bouncycastle.crypto.modes.kgcm;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/kgcm/KGCMMultiplier.class */
public interface KGCMMultiplier {
    void init(long[] jArr);

    void multiplyH(long[] jArr);
}
