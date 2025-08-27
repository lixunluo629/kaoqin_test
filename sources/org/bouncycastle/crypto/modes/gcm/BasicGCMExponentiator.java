package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/gcm/BasicGCMExponentiator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/gcm/BasicGCMExponentiator.class */
public class BasicGCMExponentiator implements GCMExponentiator {
    private byte[] x;

    @Override // org.bouncycastle.crypto.modes.gcm.GCMExponentiator
    public void init(byte[] bArr) {
        this.x = Arrays.clone(bArr);
    }

    @Override // org.bouncycastle.crypto.modes.gcm.GCMExponentiator
    public void exponentiateX(long j, byte[] bArr) {
        byte[] bArrOneAsBytes = GCMUtil.oneAsBytes();
        if (j > 0) {
            byte[] bArrClone = Arrays.clone(this.x);
            do {
                if ((j & 1) != 0) {
                    GCMUtil.multiply(bArrOneAsBytes, bArrClone);
                }
                GCMUtil.multiply(bArrClone, bArrClone);
                j >>>= 1;
            } while (j > 0);
        }
        System.arraycopy(bArrOneAsBytes, 0, bArr, 0, 16);
    }
}
