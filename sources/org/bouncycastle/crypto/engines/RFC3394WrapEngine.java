package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/RFC3394WrapEngine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/RFC3394WrapEngine.class */
public class RFC3394WrapEngine implements Wrapper {
    private BlockCipher engine;
    private KeyParameter param;
    private boolean forWrapping;
    private byte[] iv = {-90, -90, -90, -90, -90, -90, -90, -90};

    public RFC3394WrapEngine(BlockCipher blockCipher) {
        this.engine = blockCipher;
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public void init(boolean z, CipherParameters cipherParameters) {
        this.forWrapping = z;
        if (cipherParameters instanceof ParametersWithRandom) {
            cipherParameters = ((ParametersWithRandom) cipherParameters).getParameters();
        }
        if (cipherParameters instanceof KeyParameter) {
            this.param = (KeyParameter) cipherParameters;
        } else if (cipherParameters instanceof ParametersWithIV) {
            this.iv = ((ParametersWithIV) cipherParameters).getIV();
            this.param = (KeyParameter) ((ParametersWithIV) cipherParameters).getParameters();
            if (this.iv.length != 8) {
                throw new IllegalArgumentException("IV not equal to 8");
            }
        }
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public String getAlgorithmName() {
        return this.engine.getAlgorithmName();
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public byte[] wrap(byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException, IllegalArgumentException {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        int i3 = i2 / 8;
        if (i3 * 8 != i2) {
            throw new DataLengthException("wrap data must be a multiple of 8 bytes");
        }
        byte[] bArr2 = new byte[i2 + this.iv.length];
        byte[] bArr3 = new byte[8 + this.iv.length];
        System.arraycopy(this.iv, 0, bArr2, 0, this.iv.length);
        System.arraycopy(bArr, 0, bArr2, this.iv.length, i2);
        this.engine.init(true, this.param);
        for (int i4 = 0; i4 != 6; i4++) {
            for (int i5 = 1; i5 <= i3; i5++) {
                System.arraycopy(bArr2, 0, bArr3, 0, this.iv.length);
                System.arraycopy(bArr2, 8 * i5, bArr3, this.iv.length, 8);
                this.engine.processBlock(bArr3, 0, bArr3, 0);
                int i6 = (i3 * i4) + i5;
                int i7 = 1;
                while (i6 != 0) {
                    int length = this.iv.length - i7;
                    bArr3[length] = (byte) (bArr3[length] ^ ((byte) i6));
                    i6 >>>= 8;
                    i7++;
                }
                System.arraycopy(bArr3, 0, bArr2, 0, 8);
                System.arraycopy(bArr3, 8, bArr2, 8 * i5, 8);
            }
        }
        return bArr2;
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public byte[] unwrap(byte[] bArr, int i, int i2) throws InvalidCipherTextException, IllegalStateException, DataLengthException, IllegalArgumentException {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int i3 = i2 / 8;
        if (i3 * 8 != i2) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        }
        byte[] bArr2 = new byte[i2 - this.iv.length];
        byte[] bArr3 = new byte[this.iv.length];
        byte[] bArr4 = new byte[8 + this.iv.length];
        System.arraycopy(bArr, 0, bArr3, 0, this.iv.length);
        System.arraycopy(bArr, this.iv.length, bArr2, 0, i2 - this.iv.length);
        this.engine.init(false, this.param);
        int i4 = i3 - 1;
        for (int i5 = 5; i5 >= 0; i5--) {
            for (int i6 = i4; i6 >= 1; i6--) {
                System.arraycopy(bArr3, 0, bArr4, 0, this.iv.length);
                System.arraycopy(bArr2, 8 * (i6 - 1), bArr4, this.iv.length, 8);
                int i7 = (i4 * i5) + i6;
                int i8 = 1;
                while (i7 != 0) {
                    int length = this.iv.length - i8;
                    bArr4[length] = (byte) (bArr4[length] ^ ((byte) i7));
                    i7 >>>= 8;
                    i8++;
                }
                this.engine.processBlock(bArr4, 0, bArr4, 0);
                System.arraycopy(bArr4, 0, bArr3, 0, 8);
                System.arraycopy(bArr4, 8, bArr2, 8 * (i6 - 1), 8);
            }
        }
        if (Arrays.constantTimeAreEqual(bArr3, this.iv)) {
            return bArr2;
        }
        throw new InvalidCipherTextException("checksum failed");
    }
}
