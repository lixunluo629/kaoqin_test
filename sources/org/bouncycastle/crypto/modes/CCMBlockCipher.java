package org.bouncycastle.crypto.modes;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/CCMBlockCipher.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/CCMBlockCipher.class */
public class CCMBlockCipher implements AEADBlockCipher {
    private BlockCipher cipher;
    private int blockSize;
    private boolean forEncryption;
    private byte[] nonce;
    private byte[] associatedText;
    private int macSize;
    private CipherParameters keyParam;
    private byte[] macBlock;
    private ByteArrayOutputStream data = new ByteArrayOutputStream();

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/CCMBlockCipher$ExposedByteArrayOutputStream.class */
    private class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
        public ExposedByteArrayOutputStream() {
        }

        public byte[] getBuffer() {
            return this.buf;
        }
    }

    public CCMBlockCipher(BlockCipher blockCipher) {
        this.cipher = blockCipher;
        this.blockSize = blockCipher.getBlockSize();
        this.macBlock = new byte[this.blockSize];
        if (this.blockSize != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
        this.forEncryption = z;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters) cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.associatedText = aEADParameters.getAssociatedText();
            this.macSize = aEADParameters.getMacSize() / 8;
            this.keyParam = aEADParameters.getKey();
            return;
        }
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("invalid parameters passed to CCM");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
        this.nonce = parametersWithIV.getIV();
        this.associatedText = null;
        this.macSize = this.macBlock.length / 2;
        this.keyParam = parametersWithIV.getParameters();
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/CCM";
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processByte(byte b, byte[] bArr, int i) throws IllegalStateException, DataLengthException {
        this.data.write(b);
        return 0;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws IllegalStateException, DataLengthException {
        this.data.write(bArr, i, i2);
        return 0;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int doFinal(byte[] bArr, int i) throws InvalidCipherTextException, IllegalStateException, DataLengthException, IllegalArgumentException {
        byte[] byteArray = this.data.toByteArray();
        byte[] bArrProcessPacket = processPacket(byteArray, 0, byteArray.length);
        System.arraycopy(bArrProcessPacket, 0, bArr, i, bArrProcessPacket.length);
        reset();
        return bArrProcessPacket.length;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void reset() {
        this.cipher.reset();
        this.data.reset();
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public byte[] getMac() {
        byte[] bArr = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, bArr, 0, bArr.length);
        return bArr;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getUpdateOutputSize(int i) {
        return 0;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getOutputSize(int i) {
        return this.forEncryption ? this.data.size() + i + this.macSize : (this.data.size() + i) - this.macSize;
    }

    public byte[] processPacket(byte[] bArr, int i, int i2) throws InvalidCipherTextException, IllegalStateException, DataLengthException, IllegalArgumentException {
        byte[] bArr2;
        if (this.keyParam == null) {
            throw new IllegalStateException("CCM cipher unitialized.");
        }
        SICBlockCipher sICBlockCipher = new SICBlockCipher(this.cipher);
        byte[] bArr3 = new byte[this.blockSize];
        bArr3[0] = (byte) (((15 - this.nonce.length) - 1) & 7);
        System.arraycopy(this.nonce, 0, bArr3, 1, this.nonce.length);
        sICBlockCipher.init(this.forEncryption, new ParametersWithIV(this.keyParam, bArr3));
        if (this.forEncryption) {
            int i3 = i;
            int i4 = 0;
            bArr2 = new byte[i2 + this.macSize];
            calculateMac(bArr, i, i2, this.macBlock);
            sICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
            while (i3 < i2 - this.blockSize) {
                sICBlockCipher.processBlock(bArr, i3, bArr2, i4);
                i4 += this.blockSize;
                i3 += this.blockSize;
            }
            byte[] bArr4 = new byte[this.blockSize];
            System.arraycopy(bArr, i3, bArr4, 0, i2 - i3);
            sICBlockCipher.processBlock(bArr4, 0, bArr4, 0);
            System.arraycopy(bArr4, 0, bArr2, i4, i2 - i3);
            int i5 = i4 + (i2 - i3);
            System.arraycopy(this.macBlock, 0, bArr2, i5, bArr2.length - i5);
        } else {
            int i6 = i;
            int i7 = 0;
            bArr2 = new byte[i2 - this.macSize];
            System.arraycopy(bArr, (i + i2) - this.macSize, this.macBlock, 0, this.macSize);
            sICBlockCipher.processBlock(this.macBlock, 0, this.macBlock, 0);
            for (int i8 = this.macSize; i8 != this.macBlock.length; i8++) {
                this.macBlock[i8] = 0;
            }
            while (i7 < bArr2.length - this.blockSize) {
                sICBlockCipher.processBlock(bArr, i6, bArr2, i7);
                i7 += this.blockSize;
                i6 += this.blockSize;
            }
            byte[] bArr5 = new byte[this.blockSize];
            System.arraycopy(bArr, i6, bArr5, 0, bArr2.length - i7);
            sICBlockCipher.processBlock(bArr5, 0, bArr5, 0);
            System.arraycopy(bArr5, 0, bArr2, i7, bArr2.length - i7);
            byte[] bArr6 = new byte[this.blockSize];
            calculateMac(bArr2, 0, bArr2.length, bArr6);
            if (!Arrays.constantTimeAreEqual(this.macBlock, bArr6)) {
                throw new InvalidCipherTextException("mac check in CCM failed");
            }
        }
        return bArr2;
    }

    private int calculateMac(byte[] bArr, int i, int i2, byte[] bArr2) throws IllegalStateException, DataLengthException, IllegalArgumentException {
        int i3;
        CBCBlockCipherMac cBCBlockCipherMac = new CBCBlockCipherMac(this.cipher, this.macSize * 8);
        cBCBlockCipherMac.init(this.keyParam);
        byte[] bArr3 = new byte[16];
        if (hasAssociatedText()) {
            bArr3[0] = (byte) (bArr3[0] | 64);
        }
        bArr3[0] = (byte) (bArr3[0] | ((((cBCBlockCipherMac.getMacSize() - 2) / 2) & 7) << 3));
        bArr3[0] = (byte) (bArr3[0] | (((15 - this.nonce.length) - 1) & 7));
        System.arraycopy(this.nonce, 0, bArr3, 1, this.nonce.length);
        int i4 = i2;
        int i5 = 1;
        while (i4 > 0) {
            bArr3[bArr3.length - i5] = (byte) (i4 & 255);
            i4 >>>= 8;
            i5++;
        }
        cBCBlockCipherMac.update(bArr3, 0, bArr3.length);
        if (hasAssociatedText()) {
            if (this.associatedText.length < 65280) {
                cBCBlockCipherMac.update((byte) (this.associatedText.length >> 8));
                cBCBlockCipherMac.update((byte) this.associatedText.length);
                i3 = 2;
            } else {
                cBCBlockCipherMac.update((byte) -1);
                cBCBlockCipherMac.update((byte) -2);
                cBCBlockCipherMac.update((byte) (this.associatedText.length >> 24));
                cBCBlockCipherMac.update((byte) (this.associatedText.length >> 16));
                cBCBlockCipherMac.update((byte) (this.associatedText.length >> 8));
                cBCBlockCipherMac.update((byte) this.associatedText.length);
                i3 = 6;
            }
            cBCBlockCipherMac.update(this.associatedText, 0, this.associatedText.length);
            int length = (i3 + this.associatedText.length) % 16;
            if (length != 0) {
                for (int i6 = 0; i6 != 16 - length; i6++) {
                    cBCBlockCipherMac.update((byte) 0);
                }
            }
        }
        cBCBlockCipherMac.update(bArr, i, i2);
        return cBCBlockCipherMac.doFinal(bArr2, 0);
    }

    private boolean hasAssociatedText() {
        return (this.associatedText == null || this.associatedText.length == 0) ? false : true;
    }
}
