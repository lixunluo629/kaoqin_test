package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/gcm/Tables1kGCMExponentiator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/gcm/Tables1kGCMExponentiator.class */
public class Tables1kGCMExponentiator implements GCMExponentiator {
    byte[][] lookupPowX2 = new byte[64];

    @Override // org.bouncycastle.crypto.modes.gcm.GCMExponentiator
    public void init(byte[] bArr) {
        this.lookupPowX2[0] = new byte[16];
        this.lookupPowX2[0][0] = Byte.MIN_VALUE;
        this.lookupPowX2[1] = Arrays.clone(bArr);
        for (int i = 2; i != 64; i++) {
            byte[] bArrClone = Arrays.clone(this.lookupPowX2[i - 1]);
            GCMUtil.multiply(bArrClone, bArrClone);
            this.lookupPowX2[i] = bArrClone;
        }
    }

    @Override // org.bouncycastle.crypto.modes.gcm.GCMExponentiator
    public void exponentiateX(long j, byte[] bArr) {
        byte[] bArrOneAsBytes = GCMUtil.oneAsBytes();
        int i = 1;
        while (j > 0) {
            if ((j & 1) != 0) {
                GCMUtil.multiply(bArrOneAsBytes, this.lookupPowX2[i]);
            }
            i++;
            j >>>= 1;
        }
        System.arraycopy(bArrOneAsBytes, 0, bArr, 0, 16);
    }
}
