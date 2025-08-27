package org.bouncycastle.crypto.engines;

import java.math.BigInteger;
import org.bouncycastle.crypto.BasicAgreement;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.IESParameters;
import org.bouncycastle.crypto.params.IESWithCipherParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/IESEngine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/IESEngine.class */
public class IESEngine {
    BasicAgreement agree;
    DerivationFunction kdf;
    Mac mac;
    BufferedBlockCipher cipher;
    byte[] macBuf;
    boolean forEncryption;
    CipherParameters privParam;
    CipherParameters pubParam;
    IESParameters param;

    public IESEngine(BasicAgreement basicAgreement, DerivationFunction derivationFunction, Mac mac) {
        this.agree = basicAgreement;
        this.kdf = derivationFunction;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = null;
    }

    public IESEngine(BasicAgreement basicAgreement, DerivationFunction derivationFunction, Mac mac, BufferedBlockCipher bufferedBlockCipher) {
        this.agree = basicAgreement;
        this.kdf = derivationFunction;
        this.mac = mac;
        this.macBuf = new byte[mac.getMacSize()];
        this.cipher = bufferedBlockCipher;
    }

    public void init(boolean z, CipherParameters cipherParameters, CipherParameters cipherParameters2, CipherParameters cipherParameters3) {
        this.forEncryption = z;
        this.privParam = cipherParameters;
        this.pubParam = cipherParameters2;
        this.param = (IESParameters) cipherParameters3;
    }

    private byte[] decryptBlock(byte[] bArr, int i, int i2, byte[] bArr2) throws InvalidCipherTextException, IllegalStateException, DataLengthException, IllegalArgumentException {
        byte[] bArr3;
        KeyParameter keyParameter;
        KDFParameters kDFParameters = new KDFParameters(bArr2, this.param.getDerivationV());
        int macKeySize = this.param.getMacKeySize();
        this.kdf.init(kDFParameters);
        int macSize = i2 - this.mac.getMacSize();
        if (this.cipher == null) {
            byte[] bArrGenerateKdfBytes = generateKdfBytes(kDFParameters, macSize + (macKeySize / 8));
            bArr3 = new byte[macSize];
            for (int i3 = 0; i3 != macSize; i3++) {
                bArr3[i3] = (byte) (bArr[i + i3] ^ bArrGenerateKdfBytes[i3]);
            }
            keyParameter = new KeyParameter(bArrGenerateKdfBytes, macSize, macKeySize / 8);
        } else {
            int cipherKeySize = ((IESWithCipherParameters) this.param).getCipherKeySize();
            byte[] bArrGenerateKdfBytes2 = generateKdfBytes(kDFParameters, (cipherKeySize / 8) + (macKeySize / 8));
            this.cipher.init(false, new KeyParameter(bArrGenerateKdfBytes2, 0, cipherKeySize / 8));
            byte[] bArr4 = new byte[this.cipher.getOutputSize(macSize)];
            int iProcessBytes = this.cipher.processBytes(bArr, i, macSize, bArr4, 0);
            int iDoFinal = iProcessBytes + this.cipher.doFinal(bArr4, iProcessBytes);
            bArr3 = new byte[iDoFinal];
            System.arraycopy(bArr4, 0, bArr3, 0, iDoFinal);
            keyParameter = new KeyParameter(bArrGenerateKdfBytes2, cipherKeySize / 8, macKeySize / 8);
        }
        byte[] encodingV = this.param.getEncodingV();
        this.mac.init(keyParameter);
        this.mac.update(bArr, i, macSize);
        this.mac.update(encodingV, 0, encodingV.length);
        this.mac.doFinal(this.macBuf, 0);
        int i4 = i + macSize;
        for (int i5 = 0; i5 < this.macBuf.length; i5++) {
            if (this.macBuf[i5] != bArr[i4 + i5]) {
                throw new InvalidCipherTextException("Mac codes failed to equal.");
            }
        }
        return bArr3;
    }

    private byte[] encryptBlock(byte[] bArr, int i, int i2, byte[] bArr2) throws InvalidCipherTextException, IllegalStateException, DataLengthException, IllegalArgumentException {
        byte[] bArr3;
        int i3;
        KeyParameter keyParameter;
        KDFParameters kDFParameters = new KDFParameters(bArr2, this.param.getDerivationV());
        int macKeySize = this.param.getMacKeySize();
        if (this.cipher == null) {
            byte[] bArrGenerateKdfBytes = generateKdfBytes(kDFParameters, i2 + (macKeySize / 8));
            bArr3 = new byte[i2 + this.mac.getMacSize()];
            i3 = i2;
            for (int i4 = 0; i4 != i2; i4++) {
                bArr3[i4] = (byte) (bArr[i + i4] ^ bArrGenerateKdfBytes[i4]);
            }
            keyParameter = new KeyParameter(bArrGenerateKdfBytes, i2, macKeySize / 8);
        } else {
            int cipherKeySize = ((IESWithCipherParameters) this.param).getCipherKeySize();
            byte[] bArrGenerateKdfBytes2 = generateKdfBytes(kDFParameters, (cipherKeySize / 8) + (macKeySize / 8));
            this.cipher.init(true, new KeyParameter(bArrGenerateKdfBytes2, 0, cipherKeySize / 8));
            byte[] bArr4 = new byte[this.cipher.getOutputSize(i2)];
            int iProcessBytes = this.cipher.processBytes(bArr, i, i2, bArr4, 0);
            int iDoFinal = iProcessBytes + this.cipher.doFinal(bArr4, iProcessBytes);
            bArr3 = new byte[iDoFinal + this.mac.getMacSize()];
            i3 = iDoFinal;
            System.arraycopy(bArr4, 0, bArr3, 0, iDoFinal);
            keyParameter = new KeyParameter(bArrGenerateKdfBytes2, cipherKeySize / 8, macKeySize / 8);
        }
        byte[] encodingV = this.param.getEncodingV();
        this.mac.init(keyParameter);
        this.mac.update(bArr3, 0, i3);
        this.mac.update(encodingV, 0, encodingV.length);
        this.mac.doFinal(bArr3, i3);
        return bArr3;
    }

    private byte[] generateKdfBytes(KDFParameters kDFParameters, int i) throws DataLengthException, IllegalArgumentException {
        byte[] bArr = new byte[i];
        this.kdf.init(kDFParameters);
        this.kdf.generateBytes(bArr, 0, bArr.length);
        return bArr;
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) throws InvalidCipherTextException {
        this.agree.init(this.privParam);
        BigInteger bigIntegerCalculateAgreement = this.agree.calculateAgreement(this.pubParam);
        return this.forEncryption ? encryptBlock(bArr, i, i2, bigIntegerCalculateAgreement.toByteArray()) : decryptBlock(bArr, i, i2, bigIntegerCalculateAgreement.toByteArray());
    }
}
