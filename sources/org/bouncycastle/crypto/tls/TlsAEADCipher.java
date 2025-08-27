package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsAEADCipher.class */
public class TlsAEADCipher implements TlsCipher {
    public static final int NONCE_RFC5288 = 1;
    static final int NONCE_DRAFT_CHACHA20_POLY1305 = 2;
    protected TlsContext context;
    protected int macSize;
    protected int record_iv_length;
    protected AEADBlockCipher encryptCipher;
    protected AEADBlockCipher decryptCipher;
    protected byte[] encryptImplicitNonce;
    protected byte[] decryptImplicitNonce;
    protected int nonceMode;

    public TlsAEADCipher(TlsContext tlsContext, AEADBlockCipher aEADBlockCipher, AEADBlockCipher aEADBlockCipher2, int i, int i2) throws IOException {
        this(tlsContext, aEADBlockCipher, aEADBlockCipher2, i, i2, 1);
    }

    TlsAEADCipher(TlsContext tlsContext, AEADBlockCipher aEADBlockCipher, AEADBlockCipher aEADBlockCipher2, int i, int i2, int i3) throws IOException, IllegalArgumentException {
        int i4;
        KeyParameter keyParameter;
        KeyParameter keyParameter2;
        if (!TlsUtils.isTLSv12(tlsContext)) {
            throw new TlsFatalAlert((short) 80);
        }
        this.nonceMode = i3;
        switch (i3) {
            case 1:
                i4 = 4;
                this.record_iv_length = 8;
                break;
            case 2:
                i4 = 12;
                this.record_iv_length = 0;
                break;
            default:
                throw new TlsFatalAlert((short) 80);
        }
        this.context = tlsContext;
        this.macSize = i2;
        int i5 = (2 * i) + (2 * i4);
        byte[] bArrCalculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, i5);
        KeyParameter keyParameter3 = new KeyParameter(bArrCalculateKeyBlock, 0, i);
        int i6 = 0 + i;
        KeyParameter keyParameter4 = new KeyParameter(bArrCalculateKeyBlock, i6, i);
        int i7 = i6 + i;
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArrCalculateKeyBlock, i7, i7 + i4);
        int i8 = i7 + i4;
        byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArrCalculateKeyBlock, i8, i8 + i4);
        if (i8 + i4 != i5) {
            throw new TlsFatalAlert((short) 80);
        }
        if (tlsContext.isServer()) {
            this.encryptCipher = aEADBlockCipher2;
            this.decryptCipher = aEADBlockCipher;
            this.encryptImplicitNonce = bArrCopyOfRange2;
            this.decryptImplicitNonce = bArrCopyOfRange;
            keyParameter = keyParameter4;
            keyParameter2 = keyParameter3;
        } else {
            this.encryptCipher = aEADBlockCipher;
            this.decryptCipher = aEADBlockCipher2;
            this.encryptImplicitNonce = bArrCopyOfRange;
            this.decryptImplicitNonce = bArrCopyOfRange2;
            keyParameter = keyParameter3;
            keyParameter2 = keyParameter4;
        }
        byte[] bArr = new byte[i4 + this.record_iv_length];
        this.encryptCipher.init(true, new AEADParameters(keyParameter, 8 * i2, bArr));
        this.decryptCipher.init(false, new AEADParameters(keyParameter2, 8 * i2, bArr));
    }

    public int getPlaintextLimit(int i) {
        return (i - this.macSize) - this.record_iv_length;
    }

    public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) throws IOException {
        byte[] bArr2 = new byte[this.encryptImplicitNonce.length + this.record_iv_length];
        switch (this.nonceMode) {
            case 1:
                System.arraycopy(this.encryptImplicitNonce, 0, bArr2, 0, this.encryptImplicitNonce.length);
                TlsUtils.writeUint64(j, bArr2, this.encryptImplicitNonce.length);
                break;
            case 2:
                TlsUtils.writeUint64(j, bArr2, bArr2.length - 8);
                for (int i3 = 0; i3 < this.encryptImplicitNonce.length; i3++) {
                    int i4 = i3;
                    bArr2[i4] = (byte) (bArr2[i4] ^ this.encryptImplicitNonce[i3]);
                }
                break;
            default:
                throw new TlsFatalAlert((short) 80);
        }
        byte[] bArr3 = new byte[this.record_iv_length + this.encryptCipher.getOutputSize(i2)];
        if (this.record_iv_length != 0) {
            System.arraycopy(bArr2, bArr2.length - this.record_iv_length, bArr3, 0, this.record_iv_length);
        }
        int i5 = this.record_iv_length;
        try {
            this.encryptCipher.init(true, new AEADParameters(null, 8 * this.macSize, bArr2, getAdditionalData(j, s, i2)));
            int iProcessBytes = i5 + this.encryptCipher.processBytes(bArr, i, i2, bArr3, i5);
            if (iProcessBytes + this.encryptCipher.doFinal(bArr3, iProcessBytes) != bArr3.length) {
                throw new TlsFatalAlert((short) 80);
            }
            return bArr3;
        } catch (Exception e) {
            throw new TlsFatalAlert((short) 80, e);
        }
    }

    public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) throws IOException {
        if (getPlaintextLimit(i2) < 0) {
            throw new TlsFatalAlert((short) 50);
        }
        byte[] bArr2 = new byte[this.decryptImplicitNonce.length + this.record_iv_length];
        switch (this.nonceMode) {
            case 1:
                System.arraycopy(this.decryptImplicitNonce, 0, bArr2, 0, this.decryptImplicitNonce.length);
                System.arraycopy(bArr, i, bArr2, bArr2.length - this.record_iv_length, this.record_iv_length);
                break;
            case 2:
                TlsUtils.writeUint64(j, bArr2, bArr2.length - 8);
                for (int i3 = 0; i3 < this.decryptImplicitNonce.length; i3++) {
                    int i4 = i3;
                    bArr2[i4] = (byte) (bArr2[i4] ^ this.decryptImplicitNonce[i3]);
                }
                break;
            default:
                throw new TlsFatalAlert((short) 80);
        }
        int i5 = i + this.record_iv_length;
        int i6 = i2 - this.record_iv_length;
        int outputSize = this.decryptCipher.getOutputSize(i6);
        byte[] bArr3 = new byte[outputSize];
        try {
            this.decryptCipher.init(false, new AEADParameters(null, 8 * this.macSize, bArr2, getAdditionalData(j, s, outputSize)));
            int iProcessBytes = 0 + this.decryptCipher.processBytes(bArr, i5, i6, bArr3, 0);
            if (iProcessBytes + this.decryptCipher.doFinal(bArr3, iProcessBytes) != bArr3.length) {
                throw new TlsFatalAlert((short) 80);
            }
            return bArr3;
        } catch (Exception e) {
            throw new TlsFatalAlert((short) 20, e);
        }
    }

    protected byte[] getAdditionalData(long j, short s, int i) throws IOException {
        byte[] bArr = new byte[13];
        TlsUtils.writeUint64(j, bArr, 0);
        TlsUtils.writeUint8(s, bArr, 8);
        TlsUtils.writeVersion(this.context.getServerVersion(), bArr, 9);
        TlsUtils.writeUint16(i, bArr, 11);
        return bArr;
    }
}
