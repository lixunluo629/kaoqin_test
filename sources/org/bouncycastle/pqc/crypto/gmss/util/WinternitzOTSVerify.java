package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/gmss/util/WinternitzOTSVerify.class */
public class WinternitzOTSVerify {
    private Digest messDigestOTS;
    private int w;

    public WinternitzOTSVerify(Digest digest, int i) {
        this.w = i;
        this.messDigestOTS = digest;
    }

    public int getSignatureLength() {
        int digestSize = this.messDigestOTS.getDigestSize();
        int i = ((digestSize << 3) + (this.w - 1)) / this.w;
        return digestSize * (i + (((getLog((i << this.w) + 1) + this.w) - 1) / this.w));
    }

    public byte[] Verify(byte[] bArr, byte[] bArr2) {
        int digestSize = this.messDigestOTS.getDigestSize();
        byte[] bArr3 = new byte[digestSize];
        this.messDigestOTS.update(bArr, 0, bArr.length);
        byte[] bArr4 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr4, 0);
        int i = ((digestSize << 3) + (this.w - 1)) / this.w;
        int log = getLog((i << this.w) + 1);
        int i2 = digestSize * (i + (((log + this.w) - 1) / this.w));
        if (i2 != bArr2.length) {
            return null;
        }
        byte[] bArr5 = new byte[i2];
        int i3 = 0;
        int i4 = 0;
        if (8 % this.w == 0) {
            int i5 = 8 / this.w;
            int i6 = (1 << this.w) - 1;
            byte[] bArr6 = new byte[digestSize];
            for (int i7 = 0; i7 < bArr4.length; i7++) {
                for (int i8 = 0; i8 < i5; i8++) {
                    int i9 = bArr4[i7] & i6;
                    i3 += i9;
                    System.arraycopy(bArr2, i4 * digestSize, bArr6, 0, digestSize);
                    while (i9 < i6) {
                        this.messDigestOTS.update(bArr6, 0, bArr6.length);
                        bArr6 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(bArr6, 0);
                        i9++;
                    }
                    System.arraycopy(bArr6, 0, bArr5, i4 * digestSize, digestSize);
                    bArr4[i7] = (byte) (bArr4[i7] >>> this.w);
                    i4++;
                }
            }
            int i10 = (i << this.w) - i3;
            int i11 = 0;
            while (true) {
                int i12 = i11;
                if (i12 >= log) {
                    break;
                }
                System.arraycopy(bArr2, i4 * digestSize, bArr6, 0, digestSize);
                for (int i13 = i10 & i6; i13 < i6; i13++) {
                    this.messDigestOTS.update(bArr6, 0, bArr6.length);
                    bArr6 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr6, 0);
                }
                System.arraycopy(bArr6, 0, bArr5, i4 * digestSize, digestSize);
                i10 >>>= this.w;
                i4++;
                i11 = i12 + this.w;
            }
        } else if (this.w < 8) {
            int i14 = digestSize / this.w;
            int i15 = (1 << this.w) - 1;
            byte[] bArr7 = new byte[digestSize];
            int i16 = 0;
            for (int i17 = 0; i17 < i14; i17++) {
                long j = 0;
                for (int i18 = 0; i18 < this.w; i18++) {
                    j ^= (bArr4[i16] & 255) << (i18 << 3);
                    i16++;
                }
                for (int i19 = 0; i19 < 8; i19++) {
                    int i20 = (int) (j & i15);
                    i3 += i20;
                    System.arraycopy(bArr2, i4 * digestSize, bArr7, 0, digestSize);
                    while (i20 < i15) {
                        this.messDigestOTS.update(bArr7, 0, bArr7.length);
                        bArr7 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(bArr7, 0);
                        i20++;
                    }
                    System.arraycopy(bArr7, 0, bArr5, i4 * digestSize, digestSize);
                    j >>>= this.w;
                    i4++;
                }
            }
            int i21 = digestSize % this.w;
            long j2 = 0;
            for (int i22 = 0; i22 < i21; i22++) {
                j2 ^= (bArr4[i16] & 255) << (i22 << 3);
                i16++;
            }
            int i23 = i21 << 3;
            int i24 = 0;
            while (true) {
                int i25 = i24;
                if (i25 >= i23) {
                    break;
                }
                int i26 = (int) (j2 & i15);
                i3 += i26;
                System.arraycopy(bArr2, i4 * digestSize, bArr7, 0, digestSize);
                while (i26 < i15) {
                    this.messDigestOTS.update(bArr7, 0, bArr7.length);
                    bArr7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr7, 0);
                    i26++;
                }
                System.arraycopy(bArr7, 0, bArr5, i4 * digestSize, digestSize);
                j2 >>>= this.w;
                i4++;
                i24 = i25 + this.w;
            }
            int i27 = (i << this.w) - i3;
            int i28 = 0;
            while (true) {
                int i29 = i28;
                if (i29 >= log) {
                    break;
                }
                System.arraycopy(bArr2, i4 * digestSize, bArr7, 0, digestSize);
                for (int i30 = i27 & i15; i30 < i15; i30++) {
                    this.messDigestOTS.update(bArr7, 0, bArr7.length);
                    bArr7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr7, 0);
                }
                System.arraycopy(bArr7, 0, bArr5, i4 * digestSize, digestSize);
                i27 >>>= this.w;
                i4++;
                i28 = i29 + this.w;
            }
        } else if (this.w < 57) {
            int i31 = (digestSize << 3) - this.w;
            int i32 = (1 << this.w) - 1;
            byte[] bArr8 = new byte[digestSize];
            int i33 = 0;
            while (i33 <= i31) {
                int i34 = i33 >>> 3;
                int i35 = i33 % 8;
                i33 += this.w;
                long j3 = 0;
                int i36 = 0;
                for (int i37 = i34; i37 < ((i33 + 7) >>> 3); i37++) {
                    j3 ^= (bArr4[i37] & 255) << (i36 << 3);
                    i36++;
                }
                long j4 = (j3 >>> i35) & i32;
                i3 = (int) (i3 + j4);
                System.arraycopy(bArr2, i4 * digestSize, bArr8, 0, digestSize);
                while (j4 < i32) {
                    this.messDigestOTS.update(bArr8, 0, bArr8.length);
                    bArr8 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr8, 0);
                    j4++;
                }
                System.arraycopy(bArr8, 0, bArr5, i4 * digestSize, digestSize);
                i4++;
            }
            int i38 = i33 >>> 3;
            if (i38 < digestSize) {
                int i39 = i33 % 8;
                long j5 = 0;
                int i40 = 0;
                for (int i41 = i38; i41 < digestSize; i41++) {
                    j5 ^= (bArr4[i41] & 255) << (i40 << 3);
                    i40++;
                }
                long j6 = (j5 >>> i39) & i32;
                i3 = (int) (i3 + j6);
                System.arraycopy(bArr2, i4 * digestSize, bArr8, 0, digestSize);
                while (j6 < i32) {
                    this.messDigestOTS.update(bArr8, 0, bArr8.length);
                    bArr8 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr8, 0);
                    j6++;
                }
                System.arraycopy(bArr8, 0, bArr5, i4 * digestSize, digestSize);
                i4++;
            }
            int i42 = (i << this.w) - i3;
            int i43 = 0;
            while (true) {
                int i44 = i43;
                if (i44 >= log) {
                    break;
                }
                System.arraycopy(bArr2, i4 * digestSize, bArr8, 0, digestSize);
                for (long j7 = i42 & i32; j7 < i32; j7++) {
                    this.messDigestOTS.update(bArr8, 0, bArr8.length);
                    bArr8 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr8, 0);
                }
                System.arraycopy(bArr8, 0, bArr5, i4 * digestSize, digestSize);
                i42 >>>= this.w;
                i4++;
                i43 = i44 + this.w;
            }
        }
        byte[] bArr9 = new byte[digestSize];
        this.messDigestOTS.update(bArr5, 0, bArr5.length);
        byte[] bArr10 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr10, 0);
        return bArr10;
    }

    public int getLog(int i) {
        int i2 = 1;
        int i3 = 2;
        while (i3 < i) {
            i3 <<= 1;
            i2++;
        }
        return i2;
    }
}
