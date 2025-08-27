package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.MacDerivationFunction;
import org.bouncycastle.crypto.params.KDFFeedbackParameters;
import org.bouncycastle.crypto.params.KeyParameter;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/KDFFeedbackBytesGenerator.class */
public class KDFFeedbackBytesGenerator implements MacDerivationFunction {
    private static final BigInteger INTEGER_MAX = BigInteger.valueOf(2147483647L);
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private final Mac prf;
    private final int h;
    private byte[] fixedInputData;
    private int maxSizeExcl;
    private byte[] ios;
    private byte[] iv;
    private boolean useCounter;
    private int generatedBytes;
    private byte[] k;

    public KDFFeedbackBytesGenerator(Mac mac) {
        this.prf = mac;
        this.h = mac.getMacSize();
        this.k = new byte[this.h];
    }

    @Override // org.bouncycastle.crypto.DerivationFunction
    public void init(DerivationParameters derivationParameters) throws IllegalArgumentException {
        if (!(derivationParameters instanceof KDFFeedbackParameters)) {
            throw new IllegalArgumentException("Wrong type of arguments given");
        }
        KDFFeedbackParameters kDFFeedbackParameters = (KDFFeedbackParameters) derivationParameters;
        this.prf.init(new KeyParameter(kDFFeedbackParameters.getKI()));
        this.fixedInputData = kDFFeedbackParameters.getFixedInputData();
        int r = kDFFeedbackParameters.getR();
        this.ios = new byte[r / 8];
        if (kDFFeedbackParameters.useCounter()) {
            BigInteger bigIntegerMultiply = TWO.pow(r).multiply(BigInteger.valueOf(this.h));
            this.maxSizeExcl = bigIntegerMultiply.compareTo(INTEGER_MAX) == 1 ? Integer.MAX_VALUE : bigIntegerMultiply.intValue();
        } else {
            this.maxSizeExcl = Integer.MAX_VALUE;
        }
        this.iv = kDFFeedbackParameters.getIV();
        this.useCounter = kDFFeedbackParameters.useCounter();
        this.generatedBytes = 0;
    }

    @Override // org.bouncycastle.crypto.MacDerivationFunction
    public Mac getMac() {
        return this.prf;
    }

    @Override // org.bouncycastle.crypto.DerivationFunction
    public int generateBytes(byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException, IllegalArgumentException {
        int i3 = this.generatedBytes + i2;
        if (i3 < 0 || i3 >= this.maxSizeExcl) {
            throw new DataLengthException("Current KDFCTR may only be used for " + this.maxSizeExcl + " bytes");
        }
        if (this.generatedBytes % this.h == 0) {
            generateNext();
        }
        int i4 = i2;
        int i5 = this.generatedBytes % this.h;
        int iMin = Math.min(this.h - (this.generatedBytes % this.h), i4);
        System.arraycopy(this.k, i5, bArr, i, iMin);
        this.generatedBytes += iMin;
        while (true) {
            i4 -= iMin;
            i += iMin;
            if (i4 <= 0) {
                return i2;
            }
            generateNext();
            iMin = Math.min(this.h, i4);
            System.arraycopy(this.k, 0, bArr, i, iMin);
            this.generatedBytes += iMin;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private void generateNext() throws IllegalStateException, DataLengthException {
        if (this.generatedBytes == 0) {
            this.prf.update(this.iv, 0, this.iv.length);
        } else {
            this.prf.update(this.k, 0, this.k.length);
        }
        if (this.useCounter) {
            int i = (this.generatedBytes / this.h) + 1;
            switch (this.ios.length) {
                case 1:
                    this.ios[this.ios.length - 1] = (byte) i;
                    this.prf.update(this.ios, 0, this.ios.length);
                    break;
                case 2:
                    this.ios[this.ios.length - 2] = (byte) (i >>> 8);
                    this.ios[this.ios.length - 1] = (byte) i;
                    this.prf.update(this.ios, 0, this.ios.length);
                    break;
                case 3:
                    this.ios[this.ios.length - 3] = (byte) (i >>> 16);
                    this.ios[this.ios.length - 2] = (byte) (i >>> 8);
                    this.ios[this.ios.length - 1] = (byte) i;
                    this.prf.update(this.ios, 0, this.ios.length);
                    break;
                case 4:
                    this.ios[0] = (byte) (i >>> 24);
                    this.ios[this.ios.length - 3] = (byte) (i >>> 16);
                    this.ios[this.ios.length - 2] = (byte) (i >>> 8);
                    this.ios[this.ios.length - 1] = (byte) i;
                    this.prf.update(this.ios, 0, this.ios.length);
                    break;
                default:
                    throw new IllegalStateException("Unsupported size of counter i");
            }
        }
        this.prf.update(this.fixedInputData, 0, this.fixedInputData.length);
        this.prf.doFinal(this.k, 0);
    }
}
