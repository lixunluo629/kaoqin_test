package org.bouncycastle.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/RFC3211WrapEngine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/RFC3211WrapEngine.class */
public class RFC3211WrapEngine implements Wrapper {
    private CBCBlockCipher engine;
    private ParametersWithIV param;
    private boolean forWrapping;
    private SecureRandom rand;

    public RFC3211WrapEngine(BlockCipher blockCipher) {
        this.engine = new CBCBlockCipher(blockCipher);
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public void init(boolean z, CipherParameters cipherParameters) {
        this.forWrapping = z;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.rand = parametersWithRandom.getRandom();
            this.param = (ParametersWithIV) parametersWithRandom.getParameters();
        } else {
            if (z) {
                this.rand = new SecureRandom();
            }
            this.param = (ParametersWithIV) cipherParameters;
        }
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public String getAlgorithmName() {
        return this.engine.getUnderlyingCipher().getAlgorithmName() + "/RFC3211Wrap";
    }

    @Override // org.bouncycastle.crypto.Wrapper
    public byte[] wrap(byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException, IllegalArgumentException {
        if (!this.forWrapping) {
            throw new IllegalStateException("not set for wrapping");
        }
        this.engine.init(true, this.param);
        int blockSize = this.engine.getBlockSize();
        byte[] bArr2 = i2 + 4 < blockSize * 2 ? new byte[blockSize * 2] : new byte[(i2 + 4) % blockSize == 0 ? i2 + 4 : (((i2 + 4) / blockSize) + 1) * blockSize];
        bArr2[0] = (byte) i2;
        bArr2[1] = (byte) (bArr[i] ^ (-1));
        bArr2[2] = (byte) (bArr[i + 1] ^ (-1));
        bArr2[3] = (byte) (bArr[i + 2] ^ (-1));
        System.arraycopy(bArr, i, bArr2, 4, i2);
        for (int i3 = i2 + 4; i3 < bArr2.length; i3++) {
            bArr2[i3] = (byte) this.rand.nextInt();
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= bArr2.length) {
                break;
            }
            this.engine.processBlock(bArr2, i5, bArr2, i5);
            i4 = i5 + blockSize;
        }
        int i6 = 0;
        while (true) {
            int i7 = i6;
            if (i7 >= bArr2.length) {
                return bArr2;
            }
            this.engine.processBlock(bArr2, i7, bArr2, i7);
            i6 = i7 + blockSize;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.bouncycastle.crypto.Wrapper
    public byte[] unwrap(byte[] bArr, int i, int i2) throws InvalidCipherTextException, IllegalStateException, DataLengthException, IllegalArgumentException {
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int blockSize = this.engine.getBlockSize();
        if (i2 < 2 * blockSize) {
            throw new InvalidCipherTextException("input too short");
        }
        byte[] bArr2 = new byte[i2];
        byte[] bArr3 = new byte[blockSize];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        System.arraycopy(bArr, i, bArr3, 0, bArr3.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), bArr3));
        int i3 = blockSize;
        while (true) {
            int i4 = i3;
            if (i4 >= bArr2.length) {
                break;
            }
            this.engine.processBlock(bArr2, i4, bArr2, i4);
            i3 = i4 + blockSize;
        }
        System.arraycopy(bArr2, bArr2.length - bArr3.length, bArr3, 0, bArr3.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), bArr3));
        this.engine.processBlock(bArr2, 0, bArr2, 0);
        this.engine.init(false, this.param);
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i6 >= bArr2.length) {
                break;
            }
            this.engine.processBlock(bArr2, i6, bArr2, i6);
            i5 = i6 + blockSize;
        }
        if ((bArr2[0] & 255) > bArr2.length - 4) {
            throw new InvalidCipherTextException("wrapped key corrupted");
        }
        byte[] bArr4 = new byte[bArr2[0] & 255];
        System.arraycopy(bArr2, 4, bArr4, 0, bArr2[0]);
        byte b = false;
        for (int i7 = 0; i7 != 3; i7++) {
            b = ((b == true ? 1 : 0) | (((byte) (bArr2[1 + i7] ^ (-1))) ^ bArr4[i7])) == true ? 1 : 0;
        }
        if (b == true) {
            throw new InvalidCipherTextException("wrapped key fails checksum");
        }
        return bArr4;
    }
}
