package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.engines.ChaCha7539Engine;
import org.bouncycastle.crypto.macs.Poly1305;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/Chacha20Poly1305.class */
public class Chacha20Poly1305 implements TlsCipher {
    private static final byte[] ZEROES = new byte[15];
    protected TlsContext context;
    protected ChaCha7539Engine encryptCipher;
    protected ChaCha7539Engine decryptCipher;
    protected byte[] encryptIV;
    protected byte[] decryptIV;

    public Chacha20Poly1305(TlsContext tlsContext) throws IOException {
        KeyParameter keyParameter;
        KeyParameter keyParameter2;
        if (!TlsUtils.isTLSv12(tlsContext)) {
            throw new TlsFatalAlert((short) 80);
        }
        this.context = tlsContext;
        int i = (2 * 32) + (2 * 12);
        byte[] bArrCalculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, i);
        KeyParameter keyParameter3 = new KeyParameter(bArrCalculateKeyBlock, 0, 32);
        int i2 = 0 + 32;
        KeyParameter keyParameter4 = new KeyParameter(bArrCalculateKeyBlock, i2, 32);
        int i3 = i2 + 32;
        byte[] bArrCopyOfRange = Arrays.copyOfRange(bArrCalculateKeyBlock, i3, i3 + 12);
        int i4 = i3 + 12;
        byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArrCalculateKeyBlock, i4, i4 + 12);
        if (i4 + 12 != i) {
            throw new TlsFatalAlert((short) 80);
        }
        this.encryptCipher = new ChaCha7539Engine();
        this.decryptCipher = new ChaCha7539Engine();
        if (tlsContext.isServer()) {
            keyParameter = keyParameter4;
            keyParameter2 = keyParameter3;
            this.encryptIV = bArrCopyOfRange2;
            this.decryptIV = bArrCopyOfRange;
        } else {
            keyParameter = keyParameter3;
            keyParameter2 = keyParameter4;
            this.encryptIV = bArrCopyOfRange;
            this.decryptIV = bArrCopyOfRange2;
        }
        this.encryptCipher.init(true, new ParametersWithIV(keyParameter, this.encryptIV));
        this.decryptCipher.init(false, new ParametersWithIV(keyParameter2, this.decryptIV));
    }

    public int getPlaintextLimit(int i) {
        return i - 16;
    }

    public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException, IOException, IllegalArgumentException {
        KeyParameter keyParameterInitRecord = initRecord(this.encryptCipher, true, j, this.encryptIV);
        byte[] bArr2 = new byte[i2 + 16];
        this.encryptCipher.processBytes(bArr, i, i2, bArr2, 0);
        byte[] bArrCalculateRecordMAC = calculateRecordMAC(keyParameterInitRecord, getAdditionalData(j, s, i2), bArr2, 0, i2);
        System.arraycopy(bArrCalculateRecordMAC, 0, bArr2, i2, bArrCalculateRecordMAC.length);
        return bArr2;
    }

    public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) throws IOException {
        if (getPlaintextLimit(i2) < 0) {
            throw new TlsFatalAlert((short) 50);
        }
        int i3 = i2 - 16;
        if (!Arrays.constantTimeAreEqual(calculateRecordMAC(initRecord(this.decryptCipher, false, j, this.decryptIV), getAdditionalData(j, s, i3), bArr, i, i3), Arrays.copyOfRange(bArr, i + i3, i + i2))) {
            throw new TlsFatalAlert((short) 20);
        }
        byte[] bArr2 = new byte[i3];
        this.decryptCipher.processBytes(bArr, i, i3, bArr2, 0);
        return bArr2;
    }

    protected KeyParameter initRecord(StreamCipher streamCipher, boolean z, long j, byte[] bArr) throws IllegalArgumentException {
        streamCipher.init(z, new ParametersWithIV(null, calculateNonce(j, bArr)));
        return generateRecordMACKey(streamCipher);
    }

    protected byte[] calculateNonce(long j, byte[] bArr) {
        byte[] bArr2 = new byte[12];
        TlsUtils.writeUint64(j, bArr2, 4);
        for (int i = 0; i < 12; i++) {
            int i2 = i;
            bArr2[i2] = (byte) (bArr2[i2] ^ bArr[i]);
        }
        return bArr2;
    }

    protected KeyParameter generateRecordMACKey(StreamCipher streamCipher) {
        byte[] bArr = new byte[64];
        streamCipher.processBytes(bArr, 0, bArr.length, bArr, 0);
        KeyParameter keyParameter = new KeyParameter(bArr, 0, 32);
        Arrays.fill(bArr, (byte) 0);
        return keyParameter;
    }

    protected byte[] calculateRecordMAC(KeyParameter keyParameter, byte[] bArr, byte[] bArr2, int i, int i2) throws IllegalStateException, DataLengthException, IllegalArgumentException {
        Poly1305 poly1305 = new Poly1305();
        poly1305.init(keyParameter);
        updateRecordMACText(poly1305, bArr, 0, bArr.length);
        updateRecordMACText(poly1305, bArr2, i, i2);
        updateRecordMACLength(poly1305, bArr.length);
        updateRecordMACLength(poly1305, i2);
        byte[] bArr3 = new byte[poly1305.getMacSize()];
        poly1305.doFinal(bArr3, 0);
        return bArr3;
    }

    protected void updateRecordMACLength(Mac mac, int i) throws IllegalStateException, DataLengthException {
        byte[] bArrLongToLittleEndian = Pack.longToLittleEndian(i & 4294967295L);
        mac.update(bArrLongToLittleEndian, 0, bArrLongToLittleEndian.length);
    }

    protected void updateRecordMACText(Mac mac, byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException {
        mac.update(bArr, i, i2);
        int i3 = i2 % 16;
        if (i3 != 0) {
            mac.update(ZEROES, 0, 16 - i3);
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
