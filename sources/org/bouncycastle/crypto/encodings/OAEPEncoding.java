package org.bouncycastle.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/encodings/OAEPEncoding.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/encodings/OAEPEncoding.class */
public class OAEPEncoding implements AsymmetricBlockCipher {
    private byte[] defHash;
    private Digest hash;
    private Digest mgf1Hash;
    private AsymmetricBlockCipher engine;
    private SecureRandom random;
    private boolean forEncryption;

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this(asymmetricBlockCipher, new SHA1Digest(), null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, byte[] bArr) {
        this(asymmetricBlockCipher, digest, digest, bArr);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, byte[] bArr) {
        this.engine = asymmetricBlockCipher;
        this.hash = digest;
        this.mgf1Hash = digest2;
        this.defHash = new byte[digest.getDigestSize()];
        if (bArr != null) {
            digest.update(bArr, 0, bArr.length);
        }
        digest.doFinal(this.defHash, 0);
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    @Override // org.bouncycastle.crypto.AsymmetricBlockCipher
    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            this.random = ((ParametersWithRandom) cipherParameters).getRandom();
        } else {
            this.random = new SecureRandom();
        }
        this.engine.init(z, cipherParameters);
        this.forEncryption = z;
    }

    @Override // org.bouncycastle.crypto.AsymmetricBlockCipher
    public int getInputBlockSize() {
        int inputBlockSize = this.engine.getInputBlockSize();
        return this.forEncryption ? (inputBlockSize - 1) - (2 * this.defHash.length) : inputBlockSize;
    }

    @Override // org.bouncycastle.crypto.AsymmetricBlockCipher
    public int getOutputBlockSize() {
        int outputBlockSize = this.engine.getOutputBlockSize();
        return this.forEncryption ? outputBlockSize : (outputBlockSize - 1) - (2 * this.defHash.length);
    }

    @Override // org.bouncycastle.crypto.AsymmetricBlockCipher
    public byte[] processBlock(byte[] bArr, int i, int i2) throws InvalidCipherTextException {
        return this.forEncryption ? encodeBlock(bArr, i, i2) : decodeBlock(bArr, i, i2);
    }

    public byte[] encodeBlock(byte[] bArr, int i, int i2) throws InvalidCipherTextException {
        byte[] bArr2 = new byte[getInputBlockSize() + 1 + (2 * this.defHash.length)];
        System.arraycopy(bArr, i, bArr2, bArr2.length - i2, i2);
        bArr2[(bArr2.length - i2) - 1] = 1;
        System.arraycopy(this.defHash, 0, bArr2, this.defHash.length, this.defHash.length);
        byte[] bArr3 = new byte[this.defHash.length];
        this.random.nextBytes(bArr3);
        byte[] bArrMaskGeneratorFunction1 = maskGeneratorFunction1(bArr3, 0, bArr3.length, bArr2.length - this.defHash.length);
        for (int length = this.defHash.length; length != bArr2.length; length++) {
            int i3 = length;
            bArr2[i3] = (byte) (bArr2[i3] ^ bArrMaskGeneratorFunction1[length - this.defHash.length]);
        }
        System.arraycopy(bArr3, 0, bArr2, 0, this.defHash.length);
        byte[] bArrMaskGeneratorFunction12 = maskGeneratorFunction1(bArr2, this.defHash.length, bArr2.length - this.defHash.length, this.defHash.length);
        for (int i4 = 0; i4 != this.defHash.length; i4++) {
            int i5 = i4;
            bArr2[i5] = (byte) (bArr2[i5] ^ bArrMaskGeneratorFunction12[i4]);
        }
        return this.engine.processBlock(bArr2, 0, bArr2.length);
    }

    public byte[] decodeBlock(byte[] bArr, int i, int i2) throws InvalidCipherTextException {
        byte[] bArr2;
        byte[] bArrProcessBlock = this.engine.processBlock(bArr, i, i2);
        if (bArrProcessBlock.length < this.engine.getOutputBlockSize()) {
            bArr2 = new byte[this.engine.getOutputBlockSize()];
            System.arraycopy(bArrProcessBlock, 0, bArr2, bArr2.length - bArrProcessBlock.length, bArrProcessBlock.length);
        } else {
            bArr2 = bArrProcessBlock;
        }
        if (bArr2.length < (2 * this.defHash.length) + 1) {
            throw new InvalidCipherTextException("data too short");
        }
        byte[] bArrMaskGeneratorFunction1 = maskGeneratorFunction1(bArr2, this.defHash.length, bArr2.length - this.defHash.length, this.defHash.length);
        for (int i3 = 0; i3 != this.defHash.length; i3++) {
            byte[] bArr3 = bArr2;
            int i4 = i3;
            bArr3[i4] = (byte) (bArr3[i4] ^ bArrMaskGeneratorFunction1[i3]);
        }
        byte[] bArrMaskGeneratorFunction12 = maskGeneratorFunction1(bArr2, 0, this.defHash.length, bArr2.length - this.defHash.length);
        for (int length = this.defHash.length; length != bArr2.length; length++) {
            byte[] bArr4 = bArr2;
            int i5 = length;
            bArr4[i5] = (byte) (bArr4[i5] ^ bArrMaskGeneratorFunction12[length - this.defHash.length]);
        }
        for (int i6 = 0; i6 != this.defHash.length; i6++) {
            if (this.defHash[i6] != bArr2[this.defHash.length + i6]) {
                throw new InvalidCipherTextException("data hash wrong");
            }
        }
        int length2 = 2 * this.defHash.length;
        while (length2 != bArr2.length && bArr2[length2] == 0) {
            length2++;
        }
        if (length2 >= bArr2.length - 1 || bArr2[length2] != 1) {
            throw new InvalidCipherTextException("data start wrong " + length2);
        }
        int i7 = length2 + 1;
        byte[] bArr5 = new byte[bArr2.length - i7];
        System.arraycopy(bArr2, i7, bArr5, 0, bArr5.length);
        return bArr5;
    }

    private void ItoOSP(int i, byte[] bArr) {
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) (i >>> 0);
    }

    private byte[] maskGeneratorFunction1(byte[] bArr, int i, int i2, int i3) {
        byte[] bArr2 = new byte[i3];
        byte[] bArr3 = new byte[this.mgf1Hash.getDigestSize()];
        byte[] bArr4 = new byte[4];
        int i4 = 0;
        this.hash.reset();
        do {
            ItoOSP(i4, bArr4);
            this.mgf1Hash.update(bArr, i, i2);
            this.mgf1Hash.update(bArr4, 0, bArr4.length);
            this.mgf1Hash.doFinal(bArr3, 0);
            System.arraycopy(bArr3, 0, bArr2, i4 * bArr3.length, bArr3.length);
            i4++;
        } while (i4 < i3 / bArr3.length);
        if (i4 * bArr3.length < i3) {
            ItoOSP(i4, bArr4);
            this.mgf1Hash.update(bArr, i, i2);
            this.mgf1Hash.update(bArr4, 0, bArr4.length);
            this.mgf1Hash.doFinal(bArr3, 0);
            System.arraycopy(bArr3, 0, bArr2, i4 * bArr3.length, bArr2.length - (i4 * bArr3.length));
        }
        return bArr2;
    }
}
