package org.bouncycastle.pqc.crypto.gmss.util;

import org.bouncycastle.crypto.Digest;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/gmss/util/WinternitzOTSignature.class */
public class WinternitzOTSignature {
    private Digest messDigestOTS;
    private int mdsize;
    private int keysize;
    private byte[][] privateKeyOTS;
    private int w;
    private GMSSRandom gmssRandom;
    private int messagesize;
    private int checksumsize;

    public WinternitzOTSignature(byte[] bArr, Digest digest, int i) {
        this.w = i;
        this.messDigestOTS = digest;
        this.gmssRandom = new GMSSRandom(this.messDigestOTS);
        this.mdsize = this.messDigestOTS.getDigestSize();
        this.messagesize = (int) Math.ceil((this.mdsize << 3) / i);
        this.checksumsize = getLog((this.messagesize << i) + 1);
        this.keysize = this.messagesize + ((int) Math.ceil(this.checksumsize / i));
        this.privateKeyOTS = new byte[this.keysize][this.mdsize];
        byte[] bArr2 = new byte[this.mdsize];
        System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        for (int i2 = 0; i2 < this.keysize; i2++) {
            this.privateKeyOTS[i2] = this.gmssRandom.nextSeed(bArr2);
        }
    }

    public byte[][] getPrivateKey() {
        return this.privateKeyOTS;
    }

    public byte[] getPublicKey() {
        byte[] bArr = new byte[this.keysize * this.mdsize];
        byte[] bArr2 = new byte[this.mdsize];
        int i = 1 << this.w;
        for (int i2 = 0; i2 < this.keysize; i2++) {
            this.messDigestOTS.update(this.privateKeyOTS[i2], 0, this.privateKeyOTS[i2].length);
            byte[] bArr3 = new byte[this.messDigestOTS.getDigestSize()];
            this.messDigestOTS.doFinal(bArr3, 0);
            for (int i3 = 2; i3 < i; i3++) {
                this.messDigestOTS.update(bArr3, 0, bArr3.length);
                bArr3 = new byte[this.messDigestOTS.getDigestSize()];
                this.messDigestOTS.doFinal(bArr3, 0);
            }
            System.arraycopy(bArr3, 0, bArr, this.mdsize * i2, this.mdsize);
        }
        this.messDigestOTS.update(bArr, 0, bArr.length);
        byte[] bArr4 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr4, 0);
        return bArr4;
    }

    public byte[] getSignature(byte[] bArr) {
        byte[] bArr2 = new byte[this.keysize * this.mdsize];
        byte[] bArr3 = new byte[this.mdsize];
        int i = 0;
        int i2 = 0;
        this.messDigestOTS.update(bArr, 0, bArr.length);
        byte[] bArr4 = new byte[this.messDigestOTS.getDigestSize()];
        this.messDigestOTS.doFinal(bArr4, 0);
        if (8 % this.w == 0) {
            int i3 = 8 / this.w;
            int i4 = (1 << this.w) - 1;
            byte[] bArr5 = new byte[this.mdsize];
            for (int i5 = 0; i5 < bArr4.length; i5++) {
                for (int i6 = 0; i6 < i3; i6++) {
                    int i7 = bArr4[i5] & i4;
                    i2 += i7;
                    System.arraycopy(this.privateKeyOTS[i], 0, bArr5, 0, this.mdsize);
                    while (i7 > 0) {
                        this.messDigestOTS.update(bArr5, 0, bArr5.length);
                        bArr5 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(bArr5, 0);
                        i7--;
                    }
                    System.arraycopy(bArr5, 0, bArr2, i * this.mdsize, this.mdsize);
                    bArr4[i5] = (byte) (bArr4[i5] >>> this.w);
                    i++;
                }
            }
            int i8 = (this.messagesize << this.w) - i2;
            int i9 = 0;
            while (true) {
                int i10 = i9;
                if (i10 >= this.checksumsize) {
                    break;
                }
                System.arraycopy(this.privateKeyOTS[i], 0, bArr5, 0, this.mdsize);
                for (int i11 = i8 & i4; i11 > 0; i11--) {
                    this.messDigestOTS.update(bArr5, 0, bArr5.length);
                    bArr5 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr5, 0);
                }
                System.arraycopy(bArr5, 0, bArr2, i * this.mdsize, this.mdsize);
                i8 >>>= this.w;
                i++;
                i9 = i10 + this.w;
            }
        } else if (this.w < 8) {
            int i12 = this.mdsize / this.w;
            int i13 = (1 << this.w) - 1;
            byte[] bArr6 = new byte[this.mdsize];
            int i14 = 0;
            for (int i15 = 0; i15 < i12; i15++) {
                long j = 0;
                for (int i16 = 0; i16 < this.w; i16++) {
                    j ^= (bArr4[i14] & 255) << (i16 << 3);
                    i14++;
                }
                for (int i17 = 0; i17 < 8; i17++) {
                    int i18 = (int) (j & i13);
                    i2 += i18;
                    System.arraycopy(this.privateKeyOTS[i], 0, bArr6, 0, this.mdsize);
                    while (i18 > 0) {
                        this.messDigestOTS.update(bArr6, 0, bArr6.length);
                        bArr6 = new byte[this.messDigestOTS.getDigestSize()];
                        this.messDigestOTS.doFinal(bArr6, 0);
                        i18--;
                    }
                    System.arraycopy(bArr6, 0, bArr2, i * this.mdsize, this.mdsize);
                    j >>>= this.w;
                    i++;
                }
            }
            int i19 = this.mdsize % this.w;
            long j2 = 0;
            for (int i20 = 0; i20 < i19; i20++) {
                j2 ^= (bArr4[i14] & 255) << (i20 << 3);
                i14++;
            }
            int i21 = i19 << 3;
            int i22 = 0;
            while (true) {
                int i23 = i22;
                if (i23 >= i21) {
                    break;
                }
                int i24 = (int) (j2 & i13);
                i2 += i24;
                System.arraycopy(this.privateKeyOTS[i], 0, bArr6, 0, this.mdsize);
                while (i24 > 0) {
                    this.messDigestOTS.update(bArr6, 0, bArr6.length);
                    bArr6 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr6, 0);
                    i24--;
                }
                System.arraycopy(bArr6, 0, bArr2, i * this.mdsize, this.mdsize);
                j2 >>>= this.w;
                i++;
                i22 = i23 + this.w;
            }
            int i25 = (this.messagesize << this.w) - i2;
            int i26 = 0;
            while (true) {
                int i27 = i26;
                if (i27 >= this.checksumsize) {
                    break;
                }
                System.arraycopy(this.privateKeyOTS[i], 0, bArr6, 0, this.mdsize);
                for (int i28 = i25 & i13; i28 > 0; i28--) {
                    this.messDigestOTS.update(bArr6, 0, bArr6.length);
                    bArr6 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr6, 0);
                }
                System.arraycopy(bArr6, 0, bArr2, i * this.mdsize, this.mdsize);
                i25 >>>= this.w;
                i++;
                i26 = i27 + this.w;
            }
        } else if (this.w < 57) {
            int i29 = (this.mdsize << 3) - this.w;
            int i30 = (1 << this.w) - 1;
            byte[] bArr7 = new byte[this.mdsize];
            int i31 = 0;
            while (i31 <= i29) {
                int i32 = i31 >>> 3;
                int i33 = i31 % 8;
                i31 += this.w;
                long j3 = 0;
                int i34 = 0;
                for (int i35 = i32; i35 < ((i31 + 7) >>> 3); i35++) {
                    j3 ^= (bArr4[i35] & 255) << (i34 << 3);
                    i34++;
                }
                long j4 = (j3 >>> i33) & i30;
                i2 = (int) (i2 + j4);
                System.arraycopy(this.privateKeyOTS[i], 0, bArr7, 0, this.mdsize);
                while (j4 > 0) {
                    this.messDigestOTS.update(bArr7, 0, bArr7.length);
                    bArr7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr7, 0);
                    j4--;
                }
                System.arraycopy(bArr7, 0, bArr2, i * this.mdsize, this.mdsize);
                i++;
            }
            int i36 = i31 >>> 3;
            if (i36 < this.mdsize) {
                int i37 = i31 % 8;
                long j5 = 0;
                int i38 = 0;
                for (int i39 = i36; i39 < this.mdsize; i39++) {
                    j5 ^= (bArr4[i39] & 255) << (i38 << 3);
                    i38++;
                }
                long j6 = (j5 >>> i37) & i30;
                i2 = (int) (i2 + j6);
                System.arraycopy(this.privateKeyOTS[i], 0, bArr7, 0, this.mdsize);
                while (j6 > 0) {
                    this.messDigestOTS.update(bArr7, 0, bArr7.length);
                    bArr7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr7, 0);
                    j6--;
                }
                System.arraycopy(bArr7, 0, bArr2, i * this.mdsize, this.mdsize);
                i++;
            }
            int i40 = (this.messagesize << this.w) - i2;
            int i41 = 0;
            while (true) {
                int i42 = i41;
                if (i42 >= this.checksumsize) {
                    break;
                }
                System.arraycopy(this.privateKeyOTS[i], 0, bArr7, 0, this.mdsize);
                for (long j7 = i40 & i30; j7 > 0; j7--) {
                    this.messDigestOTS.update(bArr7, 0, bArr7.length);
                    bArr7 = new byte[this.messDigestOTS.getDigestSize()];
                    this.messDigestOTS.doFinal(bArr7, 0);
                }
                System.arraycopy(bArr7, 0, bArr2, i * this.mdsize, this.mdsize);
                i40 >>>= this.w;
                i++;
                i41 = i42 + this.w;
            }
        }
        return bArr2;
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
