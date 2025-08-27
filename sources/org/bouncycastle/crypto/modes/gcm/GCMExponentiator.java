package org.bouncycastle.crypto.modes.gcm;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/gcm/GCMExponentiator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/gcm/GCMExponentiator.class */
public interface GCMExponentiator {
    void init(byte[] bArr);

    void exponentiateX(long j, byte[] bArr);
}
