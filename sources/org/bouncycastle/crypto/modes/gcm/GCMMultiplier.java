package org.bouncycastle.crypto.modes.gcm;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/gcm/GCMMultiplier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/gcm/GCMMultiplier.class */
public interface GCMMultiplier {
    void init(byte[] bArr);

    void multiplyH(byte[] bArr);
}
