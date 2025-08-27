package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.crypto.util.Pack;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/gcm/Tables8kGCMMultiplier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/gcm/Tables8kGCMMultiplier.class */
public class Tables8kGCMMultiplier implements GCMMultiplier {
    private final int[][][] M = new int[32][16][];

    @Override // org.bouncycastle.crypto.modes.gcm.GCMMultiplier
    public void init(byte[] bArr) {
        this.M[0][0] = new int[4];
        this.M[1][0] = new int[4];
        this.M[1][8] = GCMUtil.asInts(bArr);
        int i = 4;
        while (true) {
            int i2 = i;
            if (i2 < 1) {
                break;
            }
            int[] iArr = new int[4];
            System.arraycopy(this.M[1][i2 + i2], 0, iArr, 0, 4);
            GCMUtil.multiplyP(iArr);
            this.M[1][i2] = iArr;
            i = i2 >> 1;
        }
        int[] iArr2 = new int[4];
        System.arraycopy(this.M[1][1], 0, iArr2, 0, 4);
        GCMUtil.multiplyP(iArr2);
        this.M[0][8] = iArr2;
        int i3 = 4;
        while (true) {
            int i4 = i3;
            if (i4 < 1) {
                break;
            }
            int[] iArr3 = new int[4];
            System.arraycopy(this.M[0][i4 + i4], 0, iArr3, 0, 4);
            GCMUtil.multiplyP(iArr3);
            this.M[0][i4] = iArr3;
            i3 = i4 >> 1;
        }
        int i5 = 0;
        while (true) {
            int i6 = 2;
            while (true) {
                int i7 = i6;
                if (i7 >= 16) {
                    break;
                }
                for (int i8 = 1; i8 < i7; i8++) {
                    int[] iArr4 = new int[4];
                    System.arraycopy(this.M[i5][i7], 0, iArr4, 0, 4);
                    GCMUtil.xor(iArr4, this.M[i5][i8]);
                    this.M[i5][i7 + i8] = iArr4;
                }
                i6 = i7 + i7;
            }
            i5++;
            if (i5 == 32) {
                return;
            }
            if (i5 > 1) {
                this.M[i5][0] = new int[4];
                int i9 = 8;
                while (true) {
                    int i10 = i9;
                    if (i10 > 0) {
                        int[] iArr5 = new int[4];
                        System.arraycopy(this.M[i5 - 2][i10], 0, iArr5, 0, 4);
                        GCMUtil.multiplyP8(iArr5);
                        this.M[i5][i10] = iArr5;
                        i9 = i10 >> 1;
                    }
                }
            }
        }
    }

    @Override // org.bouncycastle.crypto.modes.gcm.GCMMultiplier
    public void multiplyH(byte[] bArr) {
        int[] iArr = new int[4];
        for (int i = 15; i >= 0; i--) {
            int[] iArr2 = this.M[i + i][bArr[i] & 15];
            iArr[0] = iArr[0] ^ iArr2[0];
            iArr[1] = iArr[1] ^ iArr2[1];
            iArr[2] = iArr[2] ^ iArr2[2];
            iArr[3] = iArr[3] ^ iArr2[3];
            int[] iArr3 = this.M[i + i + 1][(bArr[i] & 240) >>> 4];
            iArr[0] = iArr[0] ^ iArr3[0];
            iArr[1] = iArr[1] ^ iArr3[1];
            iArr[2] = iArr[2] ^ iArr3[2];
            iArr[3] = iArr[3] ^ iArr3[3];
        }
        Pack.intToBigEndian(iArr[0], bArr, 0);
        Pack.intToBigEndian(iArr[1], bArr, 4);
        Pack.intToBigEndian(iArr[2], bArr, 8);
        Pack.intToBigEndian(iArr[3], bArr, 12);
    }
}
