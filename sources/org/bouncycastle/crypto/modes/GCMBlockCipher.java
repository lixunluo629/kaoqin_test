package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.modes.gcm.GCMMultiplier;
import org.bouncycastle.crypto.modes.gcm.Tables8kGCMMultiplier;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.util.Pack;
import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/GCMBlockCipher.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/GCMBlockCipher.class */
public class GCMBlockCipher implements AEADBlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final byte[] ZEROES = new byte[16];
    private BlockCipher cipher;
    private GCMMultiplier multiplier;
    private boolean forEncryption;
    private int macSize;
    private byte[] nonce;
    private byte[] A;
    private KeyParameter keyParam;
    private byte[] H;
    private byte[] initS;
    private byte[] J0;
    private byte[] bufBlock;
    private byte[] macBlock;
    private byte[] S;
    private byte[] counter;
    private int bufOff;
    private long totalLength;

    public GCMBlockCipher(BlockCipher blockCipher) {
        this(blockCipher, null);
    }

    public GCMBlockCipher(BlockCipher blockCipher, GCMMultiplier gCMMultiplier) {
        if (blockCipher.getBlockSize() != 16) {
            throw new IllegalArgumentException("cipher required with a block size of 16.");
        }
        gCMMultiplier = gCMMultiplier == null ? new Tables8kGCMMultiplier() : gCMMultiplier;
        this.cipher = blockCipher;
        this.multiplier = gCMMultiplier;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public BlockCipher getUnderlyingCipher() {
        return this.cipher;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/GCM";
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void init(boolean z, CipherParameters cipherParameters) throws IllegalStateException, DataLengthException, IllegalArgumentException {
        this.forEncryption = z;
        this.macBlock = null;
        if (cipherParameters instanceof AEADParameters) {
            AEADParameters aEADParameters = (AEADParameters) cipherParameters;
            this.nonce = aEADParameters.getNonce();
            this.A = aEADParameters.getAssociatedText();
            int macSize = aEADParameters.getMacSize();
            if (macSize < 96 || macSize > 128 || macSize % 8 != 0) {
                throw new IllegalArgumentException("Invalid value for MAC size: " + macSize);
            }
            this.macSize = macSize / 8;
            this.keyParam = aEADParameters.getKey();
        } else {
            if (!(cipherParameters instanceof ParametersWithIV)) {
                throw new IllegalArgumentException("invalid parameters passed to GCM");
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            this.nonce = parametersWithIV.getIV();
            this.A = null;
            this.macSize = 16;
            this.keyParam = (KeyParameter) parametersWithIV.getParameters();
        }
        this.bufBlock = new byte[z ? 16 : 16 + this.macSize];
        if (this.nonce == null || this.nonce.length < 1) {
            throw new IllegalArgumentException("IV must be at least 1 byte");
        }
        if (this.A == null) {
            this.A = new byte[0];
        }
        this.cipher.init(true, this.keyParam);
        this.H = new byte[16];
        this.cipher.processBlock(ZEROES, 0, this.H, 0);
        this.multiplier.init(this.H);
        this.initS = gHASH(this.A);
        if (this.nonce.length == 12) {
            this.J0 = new byte[16];
            System.arraycopy(this.nonce, 0, this.J0, 0, this.nonce.length);
            this.J0[15] = 1;
        } else {
            this.J0 = gHASH(this.nonce);
            byte[] bArr = new byte[16];
            packLength(this.nonce.length * 8, bArr, 8);
            xor(this.J0, bArr);
            this.multiplier.multiplyH(this.J0);
        }
        this.S = Arrays.clone(this.initS);
        this.counter = Arrays.clone(this.J0);
        this.bufOff = 0;
        this.totalLength = 0L;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public byte[] getMac() {
        return Arrays.clone(this.macBlock);
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getOutputSize(int i) {
        return this.forEncryption ? i + this.bufOff + this.macSize : (i + this.bufOff) - this.macSize;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int getUpdateOutputSize(int i) {
        return ((i + this.bufOff) / 16) * 16;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processByte(byte b, byte[] bArr, int i) throws DataLengthException {
        return process(b, bArr, i);
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws IllegalStateException, DataLengthException {
        int i4 = 0;
        for (int i5 = 0; i5 != i2; i5++) {
            byte[] bArr3 = this.bufBlock;
            int i6 = this.bufOff;
            this.bufOff = i6 + 1;
            bArr3[i6] = bArr[i + i5];
            if (this.bufOff == this.bufBlock.length) {
                gCTRBlock(this.bufBlock, 16, bArr2, i3 + i4);
                if (!this.forEncryption) {
                    System.arraycopy(this.bufBlock, 16, this.bufBlock, 0, this.macSize);
                }
                this.bufOff = this.bufBlock.length - 16;
                i4 += 16;
            }
        }
        return i4;
    }

    private int process(byte b, byte[] bArr, int i) throws IllegalStateException, DataLengthException {
        byte[] bArr2 = this.bufBlock;
        int i2 = this.bufOff;
        this.bufOff = i2 + 1;
        bArr2[i2] = b;
        if (this.bufOff != this.bufBlock.length) {
            return 0;
        }
        gCTRBlock(this.bufBlock, 16, bArr, i);
        if (!this.forEncryption) {
            System.arraycopy(this.bufBlock, 16, this.bufBlock, 0, this.macSize);
        }
        this.bufOff = this.bufBlock.length - 16;
        return 16;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public int doFinal(byte[] bArr, int i) throws InvalidCipherTextException, IllegalStateException, DataLengthException {
        int i2 = this.bufOff;
        if (!this.forEncryption) {
            if (i2 < this.macSize) {
                throw new InvalidCipherTextException("data too short");
            }
            i2 -= this.macSize;
        }
        if (i2 > 0) {
            byte[] bArr2 = new byte[16];
            System.arraycopy(this.bufBlock, 0, bArr2, 0, i2);
            gCTRBlock(bArr2, i2, bArr, i);
        }
        byte[] bArr3 = new byte[16];
        packLength(this.A.length * 8, bArr3, 0);
        packLength(this.totalLength * 8, bArr3, 8);
        xor(this.S, bArr3);
        this.multiplier.multiplyH(this.S);
        byte[] bArr4 = new byte[16];
        this.cipher.processBlock(this.J0, 0, bArr4, 0);
        xor(bArr4, this.S);
        int i3 = i2;
        this.macBlock = new byte[this.macSize];
        System.arraycopy(bArr4, 0, this.macBlock, 0, this.macSize);
        if (this.forEncryption) {
            System.arraycopy(this.macBlock, 0, bArr, i + this.bufOff, this.macSize);
            i3 += this.macSize;
        } else {
            byte[] bArr5 = new byte[this.macSize];
            System.arraycopy(this.bufBlock, i2, bArr5, 0, this.macSize);
            if (!Arrays.constantTimeAreEqual(this.macBlock, bArr5)) {
                throw new InvalidCipherTextException("mac check in GCM failed");
            }
        }
        reset(false);
        return i3;
    }

    @Override // org.bouncycastle.crypto.modes.AEADBlockCipher
    public void reset() {
        reset(true);
    }

    private void reset(boolean z) {
        this.S = Arrays.clone(this.initS);
        this.counter = Arrays.clone(this.J0);
        this.bufOff = 0;
        this.totalLength = 0L;
        if (this.bufBlock != null) {
            Arrays.fill(this.bufBlock, (byte) 0);
        }
        if (z) {
            this.macBlock = null;
        }
        this.cipher.reset();
    }

    private void gCTRBlock(byte[] bArr, int i, byte[] bArr2, int i2) throws IllegalStateException, DataLengthException {
        byte[] bArr3;
        for (int i3 = 15; i3 >= 12; i3--) {
            byte b = (byte) ((this.counter[i3] + 1) & 255);
            this.counter[i3] = b;
            if (b != 0) {
                break;
            }
        }
        byte[] bArr4 = new byte[16];
        this.cipher.processBlock(this.counter, 0, bArr4, 0);
        if (this.forEncryption) {
            System.arraycopy(ZEROES, i, bArr4, i, 16 - i);
            bArr3 = bArr4;
        } else {
            bArr3 = bArr;
        }
        for (int i4 = i - 1; i4 >= 0; i4--) {
            int i5 = i4;
            bArr4[i5] = (byte) (bArr4[i5] ^ bArr[i4]);
            bArr2[i2 + i4] = bArr4[i4];
        }
        xor(this.S, bArr3);
        this.multiplier.multiplyH(this.S);
        this.totalLength += i;
    }

    private byte[] gHASH(byte[] bArr) {
        byte[] bArr2 = new byte[16];
        for (int i = 0; i < bArr.length; i += 16) {
            byte[] bArr3 = new byte[16];
            System.arraycopy(bArr, i, bArr3, 0, Math.min(bArr.length - i, 16));
            xor(bArr2, bArr3);
            this.multiplier.multiplyH(bArr2);
        }
        return bArr2;
    }

    private static void xor(byte[] bArr, byte[] bArr2) {
        for (int i = 15; i >= 0; i--) {
            int i2 = i;
            bArr[i2] = (byte) (bArr[i2] ^ bArr2[i]);
        }
    }

    private static void packLength(long j, byte[] bArr, int i) {
        Pack.intToBigEndian((int) (j >>> 32), bArr, i);
        Pack.intToBigEndian((int) j, bArr, i + 4);
    }
}
