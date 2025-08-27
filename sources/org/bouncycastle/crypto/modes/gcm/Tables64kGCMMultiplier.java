package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.util.Pack;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/gcm/Tables64kGCMMultiplier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/gcm/Tables64kGCMMultiplier.class */
public class Tables64kGCMMultiplier implements GCMMultiplier {
    private final int[][][] M = new int[16][256][];

    @Override // org.bouncycastle.crypto.modes.gcm.GCMMultiplier
    public void init(byte[] bArr) {
        this.M[0][0] = new int[4];
        this.M[0][128] = GCMUtil.asInts(bArr);
        int i = 64;
        while (true) {
            int i2 = i;
            if (i2 < 1) {
                break;
            }
            int[] iArr = new int[4];
            System.arraycopy(this.M[0][i2 + i2], 0, iArr, 0, 4);
            GCMUtil.multiplyP(iArr);
            this.M[0][i2] = iArr;
            i = i2 >> 1;
        }
        int i3 = 0;
        while (true) {
            int i4 = 2;
            while (true) {
                int i5 = i4;
                if (i5 >= 256) {
                    break;
                }
                for (int i6 = 1; i6 < i5; i6++) {
                    int[] iArr2 = new int[4];
                    System.arraycopy(this.M[i3][i5], 0, iArr2, 0, 4);
                    GCMUtil.xor(iArr2, this.M[i3][i6]);
                    this.M[i3][i5 + i6] = iArr2;
                }
                i4 = i5 + i5;
            }
            i3++;
            if (i3 == 16) {
                return;
            }
            this.M[i3][0] = new int[4];
            int i7 = 128;
            while (true) {
                int i8 = i7;
                if (i8 > 0) {
                    int[] iArr3 = new int[4];
                    System.arraycopy(this.M[i3 - 1][i8], 0, iArr3, 0, 4);
                    GCMUtil.multiplyP8(iArr3);
                    this.M[i3][i8] = iArr3;
                    i7 = i8 >> 1;
                }
            }
        }
    }

    @Override // org.bouncycastle.crypto.modes.gcm.GCMMultiplier
    public void multiplyH(byte[] bArr) {
        int[] iArr = new int[4];
        for (int i = 15; i >= 0; i--) {
            int[] iArr2 = this.M[i][bArr[i] & 255];
            iArr[0] = iArr[0] ^ iArr2[0];
            iArr[1] = iArr[1] ^ iArr2[1];
            iArr[2] = iArr[2] ^ iArr2[2];
            iArr[3] = iArr[3] ^ iArr2[3];
        }
        Pack.intToBigEndian(iArr[0], bArr, 0);
        Pack.intToBigEndian(iArr[1], bArr, 4);
        Pack.intToBigEndian(iArr[2], bArr, 8);
        Pack.intToBigEndian(iArr[3], bArr, 12);
    }
}
