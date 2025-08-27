package org.bouncycastle.crypto.digests;

import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/digests/CSHAKEDigest.class */
public class CSHAKEDigest extends SHAKEDigest {
    private static final byte[] padding = new byte[100];
    private final byte[] diff;

    public CSHAKEDigest(int i, byte[] bArr, byte[] bArr2) {
        super(i);
        if ((bArr == null || bArr.length == 0) && (bArr2 == null || bArr2.length == 0)) {
            this.diff = null;
        } else {
            this.diff = Arrays.concatenate(leftEncode(this.rate / 8), encodeString(bArr), encodeString(bArr2));
            diffPadAndAbsorb();
        }
    }

    private void diffPadAndAbsorb() {
        int i = this.rate / 8;
        absorb(this.diff, 0, this.diff.length);
        int i2 = i;
        int length = this.diff.length % i;
        while (true) {
            int i3 = i2 - length;
            if (i3 <= padding.length) {
                absorb(padding, 0, i3);
                return;
            } else {
                absorb(padding, 0, padding.length);
                i2 = i3;
                length = padding.length;
            }
        }
    }

    private byte[] encodeString(byte[] bArr) {
        return (bArr == null || bArr.length == 0) ? leftEncode(0L) : Arrays.concatenate(leftEncode(bArr.length * 8), bArr);
    }

    private static byte[] leftEncode(long j) {
        byte b = 1;
        long j2 = j;
        while (true) {
            long j3 = j2 >> 8;
            j2 = j3;
            if (j3 == 0) {
                break;
            }
            b = (byte) (b + 1);
        }
        byte[] bArr = new byte[b + 1];
        bArr[0] = b;
        for (int i = 1; i <= b; i++) {
            bArr[i] = (byte) (j >> (8 * (b - i)));
        }
        return bArr;
    }

    @Override // org.bouncycastle.crypto.digests.SHAKEDigest, org.bouncycastle.crypto.Xof
    public int doOutput(byte[] bArr, int i, int i2) {
        if (this.diff == null) {
            return super.doOutput(bArr, i, i2);
        }
        if (!this.squeezing) {
            absorbBits(0, 2);
        }
        squeeze(bArr, i, i2 * 8);
        return i2;
    }

    @Override // org.bouncycastle.crypto.digests.KeccakDigest, org.bouncycastle.crypto.Digest
    public void reset() {
        super.reset();
        if (this.diff != null) {
            diffPadAndAbsorb();
        }
    }
}
