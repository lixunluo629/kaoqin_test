package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.MaxBytesExceededException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Strings;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/Salsa20Engine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/Salsa20Engine.class */
public class Salsa20Engine implements StreamCipher {
    private static final int stateSize = 16;
    private static final byte[] sigma = Strings.toByteArray("expand 32-byte k");
    private static final byte[] tau = Strings.toByteArray("expand 16-byte k");
    private int index = 0;
    private int[] engineState = new int[16];
    private int[] x = new int[16];
    private byte[] keyStream = new byte[64];
    private byte[] workingKey = null;
    private byte[] workingIV = null;
    private boolean initialised = false;
    private int cW0;
    private int cW1;
    private int cW2;

    @Override // org.bouncycastle.crypto.StreamCipher
    public void init(boolean z, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Salsa20 Init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
        byte[] iv = parametersWithIV.getIV();
        if (iv == null || iv.length != 8) {
            throw new IllegalArgumentException("Salsa20 requires exactly 8 bytes of IV");
        }
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("Salsa20 Init parameters must include a key");
        }
        this.workingKey = ((KeyParameter) parametersWithIV.getParameters()).getKey();
        this.workingIV = iv;
        setKey(this.workingKey, this.workingIV);
    }

    @Override // org.bouncycastle.crypto.StreamCipher
    public String getAlgorithmName() {
        return "Salsa20";
    }

    @Override // org.bouncycastle.crypto.StreamCipher
    public byte returnByte(byte b) {
        if (limitExceeded()) {
            throw new MaxBytesExceededException("2^70 byte limit per IV; Change IV");
        }
        if (this.index == 0) {
            salsa20WordToByte(this.engineState, this.keyStream);
            int[] iArr = this.engineState;
            iArr[8] = iArr[8] + 1;
            if (this.engineState[8] == 0) {
                int[] iArr2 = this.engineState;
                iArr2[9] = iArr2[9] + 1;
            }
        }
        byte b2 = (byte) (this.keyStream[this.index] ^ b);
        this.index = (this.index + 1) & 63;
        return b2;
    }

    @Override // org.bouncycastle.crypto.StreamCipher
    public void processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (!this.initialised) {
            throw new IllegalStateException(getAlgorithmName() + " not initialised");
        }
        if (i + i2 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (i3 + i2 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (limitExceeded(i2)) {
            throw new MaxBytesExceededException("2^70 byte limit per IV would be exceeded; Change IV");
        }
        for (int i4 = 0; i4 < i2; i4++) {
            if (this.index == 0) {
                salsa20WordToByte(this.engineState, this.keyStream);
                int[] iArr = this.engineState;
                iArr[8] = iArr[8] + 1;
                if (this.engineState[8] == 0) {
                    int[] iArr2 = this.engineState;
                    iArr2[9] = iArr2[9] + 1;
                }
            }
            bArr2[i4 + i3] = (byte) (this.keyStream[this.index] ^ bArr[i4 + i]);
            this.index = (this.index + 1) & 63;
        }
    }

    @Override // org.bouncycastle.crypto.StreamCipher
    public void reset() {
        setKey(this.workingKey, this.workingIV);
    }

    /* JADX INFO: Access modifiers changed from: private */
    void setKey(byte[] bArr, byte[] bArr2) {
        byte[] bArr3;
        this.workingKey = bArr;
        this.workingIV = bArr2;
        this.index = 0;
        resetCounter();
        int i = 0;
        this.engineState[1] = byteToIntLittle(this.workingKey, 0);
        this.engineState[2] = byteToIntLittle(this.workingKey, 4);
        this.engineState[3] = byteToIntLittle(this.workingKey, 8);
        this.engineState[4] = byteToIntLittle(this.workingKey, 12);
        if (this.workingKey.length == 32) {
            bArr3 = sigma;
            i = 16;
        } else {
            bArr3 = tau;
        }
        this.engineState[11] = byteToIntLittle(this.workingKey, i);
        this.engineState[12] = byteToIntLittle(this.workingKey, i + 4);
        this.engineState[13] = byteToIntLittle(this.workingKey, i + 8);
        this.engineState[14] = byteToIntLittle(this.workingKey, i + 12);
        this.engineState[0] = byteToIntLittle(bArr3, 0);
        this.engineState[5] = byteToIntLittle(bArr3, 4);
        this.engineState[10] = byteToIntLittle(bArr3, 8);
        this.engineState[15] = byteToIntLittle(bArr3, 12);
        this.engineState[6] = byteToIntLittle(this.workingIV, 0);
        this.engineState[7] = byteToIntLittle(this.workingIV, 4);
        int[] iArr = this.engineState;
        this.engineState[9] = 0;
        iArr[8] = 0;
        this.initialised = true;
    }

    private void salsa20WordToByte(int[] iArr, byte[] bArr) {
        System.arraycopy(iArr, 0, this.x, 0, iArr.length);
        for (int i = 0; i < 10; i++) {
            int[] iArr2 = this.x;
            iArr2[4] = iArr2[4] ^ rotl(this.x[0] + this.x[12], 7);
            int[] iArr3 = this.x;
            iArr3[8] = iArr3[8] ^ rotl(this.x[4] + this.x[0], 9);
            int[] iArr4 = this.x;
            iArr4[12] = iArr4[12] ^ rotl(this.x[8] + this.x[4], 13);
            int[] iArr5 = this.x;
            iArr5[0] = iArr5[0] ^ rotl(this.x[12] + this.x[8], 18);
            int[] iArr6 = this.x;
            iArr6[9] = iArr6[9] ^ rotl(this.x[5] + this.x[1], 7);
            int[] iArr7 = this.x;
            iArr7[13] = iArr7[13] ^ rotl(this.x[9] + this.x[5], 9);
            int[] iArr8 = this.x;
            iArr8[1] = iArr8[1] ^ rotl(this.x[13] + this.x[9], 13);
            int[] iArr9 = this.x;
            iArr9[5] = iArr9[5] ^ rotl(this.x[1] + this.x[13], 18);
            int[] iArr10 = this.x;
            iArr10[14] = iArr10[14] ^ rotl(this.x[10] + this.x[6], 7);
            int[] iArr11 = this.x;
            iArr11[2] = iArr11[2] ^ rotl(this.x[14] + this.x[10], 9);
            int[] iArr12 = this.x;
            iArr12[6] = iArr12[6] ^ rotl(this.x[2] + this.x[14], 13);
            int[] iArr13 = this.x;
            iArr13[10] = iArr13[10] ^ rotl(this.x[6] + this.x[2], 18);
            int[] iArr14 = this.x;
            iArr14[3] = iArr14[3] ^ rotl(this.x[15] + this.x[11], 7);
            int[] iArr15 = this.x;
            iArr15[7] = iArr15[7] ^ rotl(this.x[3] + this.x[15], 9);
            int[] iArr16 = this.x;
            iArr16[11] = iArr16[11] ^ rotl(this.x[7] + this.x[3], 13);
            int[] iArr17 = this.x;
            iArr17[15] = iArr17[15] ^ rotl(this.x[11] + this.x[7], 18);
            int[] iArr18 = this.x;
            iArr18[1] = iArr18[1] ^ rotl(this.x[0] + this.x[3], 7);
            int[] iArr19 = this.x;
            iArr19[2] = iArr19[2] ^ rotl(this.x[1] + this.x[0], 9);
            int[] iArr20 = this.x;
            iArr20[3] = iArr20[3] ^ rotl(this.x[2] + this.x[1], 13);
            int[] iArr21 = this.x;
            iArr21[0] = iArr21[0] ^ rotl(this.x[3] + this.x[2], 18);
            int[] iArr22 = this.x;
            iArr22[6] = iArr22[6] ^ rotl(this.x[5] + this.x[4], 7);
            int[] iArr23 = this.x;
            iArr23[7] = iArr23[7] ^ rotl(this.x[6] + this.x[5], 9);
            int[] iArr24 = this.x;
            iArr24[4] = iArr24[4] ^ rotl(this.x[7] + this.x[6], 13);
            int[] iArr25 = this.x;
            iArr25[5] = iArr25[5] ^ rotl(this.x[4] + this.x[7], 18);
            int[] iArr26 = this.x;
            iArr26[11] = iArr26[11] ^ rotl(this.x[10] + this.x[9], 7);
            int[] iArr27 = this.x;
            iArr27[8] = iArr27[8] ^ rotl(this.x[11] + this.x[10], 9);
            int[] iArr28 = this.x;
            iArr28[9] = iArr28[9] ^ rotl(this.x[8] + this.x[11], 13);
            int[] iArr29 = this.x;
            iArr29[10] = iArr29[10] ^ rotl(this.x[9] + this.x[8], 18);
            int[] iArr30 = this.x;
            iArr30[12] = iArr30[12] ^ rotl(this.x[15] + this.x[14], 7);
            int[] iArr31 = this.x;
            iArr31[13] = iArr31[13] ^ rotl(this.x[12] + this.x[15], 9);
            int[] iArr32 = this.x;
            iArr32[14] = iArr32[14] ^ rotl(this.x[13] + this.x[12], 13);
            int[] iArr33 = this.x;
            iArr33[15] = iArr33[15] ^ rotl(this.x[14] + this.x[13], 18);
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 16; i3++) {
            intToByteLittle(this.x[i3] + iArr[i3], bArr, i2);
            i2 += 4;
        }
        for (int i4 = 16; i4 < this.x.length; i4++) {
            intToByteLittle(this.x[i4], bArr, i2);
            i2 += 4;
        }
    }

    private byte[] intToByteLittle(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        bArr[i2 + 1] = (byte) (i >>> 8);
        bArr[i2 + 2] = (byte) (i >>> 16);
        bArr[i2 + 3] = (byte) (i >>> 24);
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    int rotl(int i, int i2) {
        return (i << i2) | (i >>> (-i2));
    }

    private int byteToIntLittle(byte[] bArr, int i) {
        return (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << 16) | (bArr[i + 3] << 24);
    }

    private void resetCounter() {
        this.cW0 = 0;
        this.cW1 = 0;
        this.cW2 = 0;
    }

    private boolean limitExceeded() {
        this.cW0++;
        if (this.cW0 != 0) {
            return false;
        }
        this.cW1++;
        if (this.cW1 != 0) {
            return false;
        }
        this.cW2++;
        return (this.cW2 & 32) != 0;
    }

    private boolean limitExceeded(int i) {
        if (this.cW0 >= 0) {
            this.cW0 += i;
            return false;
        }
        this.cW0 += i;
        if (this.cW0 < 0) {
            return false;
        }
        this.cW1++;
        if (this.cW1 != 0) {
            return false;
        }
        this.cW2++;
        return (this.cW2 & 32) != 0;
    }
}
