package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/EAXBlockCipher.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/EAXBlockCipher.class */
public class EAXBlockCipher implements AEADBlockCipher {
    private static final byte nTAG = 0;
    private static final byte hTAG = 1;
    private static final byte cTAG = 2;
    private SICBlockCipher cipher;
    private boolean forEncryption;
    private int blockSize;
    private Mac mac;
    private byte[] nonceMac;
    private byte[] associatedTextMac;
    private byte[] macBlock;
    private int macSize;
    private byte[] bufBlock;
    private int bufOff;

    public EAXBlockCipher(BlockCipher blockCipher) {
        this.blockSize = blockCipher.getBlockSize();
        this.mac = new CMac(blockCipher);
        this.macBlock = new byte[this.blockSize];
        this.bufBlock = new byte[this.blockSize * 2];
        this.associatedTextMac = new byte[this.mac.getMacSize()];
        this.nonceMac = new byte[this.mac.getMacSize()];
        this.cipher = new SICBlockCipher(blockCipher);
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public String getAlgorithmName() {
        return this.cipher.getUnderlyingCipher().getAlgorithmName() + "/EAX";
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public BlockCipher getUnderlyingCipher() {
        return this.cipher.getUnderlyingCipher();
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void init(boolean z, CipherParameters cipherParameters) throws IllegalStateException, DataLengthException, IllegalArgumentException {
        byte[] iv;
        byte[] associatedText;
        CipherParameters parameters;
        this.forEncryption = z;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters) cipherParameters;
            iv = aEADParameters.getNonce();
            associatedText = aEADParameters.getAssociatedText();
            this.macSize = aEADParameters.getMacSize() / 8;
            parameters = aEADParameters.getKey();
        } else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to EAX");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            iv = parametersWithIV.getIV();
            associatedText = new byte[0];
            this.macSize = this.mac.getMacSize() / 2;
            parameters = parametersWithIV.getParameters();
        }
        byte[] bArr = new byte[this.blockSize];
        this.mac.init(parameters);
        bArr[this.blockSize - 1] = 1;
        this.mac.update(bArr, 0, this.blockSize);
        this.mac.update(associatedText, 0, associatedText.length);
        this.mac.doFinal(this.associatedTextMac, 0);
        bArr[this.blockSize - 1] = 0;
        this.mac.update(bArr, 0, this.blockSize);
        this.mac.update(iv, 0, iv.length);
        this.mac.doFinal(this.nonceMac, 0);
        bArr[this.blockSize - 1] = 2;
        this.mac.update(bArr, 0, this.blockSize);
        this.cipher.init(true, new ParametersWithIV(parameters, this.nonceMac));
    }

    private void calculateMac() throws IllegalStateException, DataLengthException {
        byte[] bArr = new byte[this.blockSize];
        this.mac.doFinal(bArr, 0);
        for (int i = 0; i < this.macBlock.length; i++) {
            this.macBlock[i] = (byte) ((this.nonceMac[i] ^ this.associatedTextMac[i]) ^ bArr[i]);
        }
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void reset() throws IllegalStateException, DataLengthException {
        reset(true);
    }

    private void reset(boolean z) throws IllegalStateException, DataLengthException {
        this.cipher.reset();
        this.mac.reset();
        this.bufOff = 0;
        Arrays.fill(this.bufBlock, (byte) 0);
        if (z) {
            Arrays.fill(this.macBlock, (byte) 0);
        }
        byte[] bArr = new byte[this.blockSize];
        bArr[this.blockSize - 1] = 2;
        this.mac.update(bArr, 0, this.blockSize);
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processByte(byte b, byte[] bArr, int i) throws DataLengthException {
        return process(b, bArr, i);
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws DataLengthException {
        int iProcess = 0;
        for (int i4 = 0; i4 != i2; i4++) {
            iProcess += process(bArr[i + i4], bArr2, i3 + iProcess);
        }
        return iProcess;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int doFinal(byte[] bArr, int i) throws InvalidCipherTextException, IllegalStateException, DataLengthException {
        int i2 = this.bufOff;
        byte[] bArr2 = new byte[this.bufBlock.length];
        this.bufOff = 0;
        if (this.forEncryption) {
            this.cipher.processBlock(this.bufBlock, 0, bArr2, 0);
            this.cipher.processBlock(this.bufBlock, this.blockSize, bArr2, this.blockSize);
            System.arraycopy(bArr2, 0, bArr, i, i2);
            this.mac.update(bArr2, 0, i2);
            calculateMac();
            System.arraycopy(this.macBlock, 0, bArr, i + i2, this.macSize);
            reset(false);
            return i2 + this.macSize;
        }
        if (i2 > this.macSize) {
            this.mac.update(this.bufBlock, 0, i2 - this.macSize);
            this.cipher.processBlock(this.bufBlock, 0, bArr2, 0);
            this.cipher.processBlock(this.bufBlock, this.blockSize, bArr2, this.blockSize);
            System.arraycopy(bArr2, 0, bArr, i, i2 - this.macSize);
        }
        calculateMac();
        if (!verifyMac(this.bufBlock, i2 - this.macSize)) {
            throw new InvalidCipherTextException("mac check in EAX failed");
        }
        reset(false);
        return i2 - this.macSize;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public byte[] getMac() {
        byte[] bArr = new byte[this.macSize];
        System.arraycopy(this.macBlock, 0, bArr, 0, this.macSize);
        return bArr;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getUpdateOutputSize(int i) {
        return ((i + this.bufOff) / this.blockSize) * this.blockSize;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getOutputSize(int i) {
        return this.forEncryption ? i + this.bufOff + this.macSize : (i + this.bufOff) - this.macSize;
    }

    private int process(byte b, byte[] bArr, int i) throws IllegalStateException, DataLengthException {
        int iProcessBlock;
        byte[] bArr2 = this.bufBlock;
        int i2 = this.bufOff;
        this.bufOff = i2 + 1;
        bArr2[i2] = b;
        if (this.bufOff != this.bufBlock.length) {
            return 0;
        }
        if (this.forEncryption) {
            iProcessBlock = this.cipher.processBlock(this.bufBlock, 0, bArr, i);
            this.mac.update(bArr, i, this.blockSize);
        } else {
            this.mac.update(this.bufBlock, 0, this.blockSize);
            iProcessBlock = this.cipher.processBlock(this.bufBlock, 0, bArr, i);
        }
        this.bufOff = this.blockSize;
        System.arraycopy(this.bufBlock, this.blockSize, this.bufBlock, 0, this.blockSize);
        return iProcessBlock;
    }

    private boolean verifyMac(byte[] bArr, int i) {
        for (int i2 = 0; i2 < this.macSize; i2++) {
            if (this.macBlock[i2] != bArr[i + i2]) {
                return false;
            }
        }
        return true;
    }
}
