package org.bouncycastle.crypto;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/SkippingCipher.class */
public interface SkippingCipher {
    long skip(long j);

    long seekTo(long j);

    long getPosition();
}
