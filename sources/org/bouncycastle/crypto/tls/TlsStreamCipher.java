package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsStreamCipher.class */
public class TlsStreamCipher implements TlsCipher {
    protected TlsContext context;
    protected StreamCipher encryptCipher;
    protected StreamCipher decryptCipher;
    protected TlsMac writeMac;
    protected TlsMac readMac;
    protected boolean usesNonce;

    public TlsStreamCipher(TlsContext tlsContext, StreamCipher streamCipher, StreamCipher streamCipher2, Digest digest, Digest digest2, int i, boolean z) throws IOException, IllegalArgumentException {
        CipherParameters parametersWithIV;
        CipherParameters parametersWithIV2;
        boolean zIsServer = tlsContext.isServer();
        this.context = tlsContext;
        this.usesNonce = z;
        this.encryptCipher = streamCipher;
        this.decryptCipher = streamCipher2;
        int digestSize = (2 * i) + digest.getDigestSize() + digest2.getDigestSize();
        byte[] bArrCalculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, digestSize);
        TlsMac tlsMac = new TlsMac(tlsContext, digest, bArrCalculateKeyBlock, 0, digest.getDigestSize());
        int digestSize2 = 0 + digest.getDigestSize();
        TlsMac tlsMac2 = new TlsMac(tlsContext, digest2, bArrCalculateKeyBlock, digestSize2, digest2.getDigestSize());
        int digestSize3 = digestSize2 + digest2.getDigestSize();
        CipherParameters keyParameter = new KeyParameter(bArrCalculateKeyBlock, digestSize3, i);
        int i2 = digestSize3 + i;
        CipherParameters keyParameter2 = new KeyParameter(bArrCalculateKeyBlock, i2, i);
        if (i2 + i != digestSize) {
            throw new TlsFatalAlert((short) 80);
        }
        if (zIsServer) {
            this.writeMac = tlsMac2;
            this.readMac = tlsMac;
            this.encryptCipher = streamCipher2;
            this.decryptCipher = streamCipher;
            parametersWithIV = keyParameter2;
            parametersWithIV2 = keyParameter;
        } else {
            this.writeMac = tlsMac;
            this.readMac = tlsMac2;
            this.encryptCipher = streamCipher;
            this.decryptCipher = streamCipher2;
            parametersWithIV = keyParameter;
            parametersWithIV2 = keyParameter2;
        }
        if (z) {
            byte[] bArr = new byte[8];
            parametersWithIV = new ParametersWithIV(parametersWithIV, bArr);
            parametersWithIV2 = new ParametersWithIV(parametersWithIV2, bArr);
        }
        this.encryptCipher.init(true, parametersWithIV);
        this.decryptCipher.init(false, parametersWithIV2);
    }

    public int getPlaintextLimit(int i) {
        return i - this.writeMac.getSize();
    }

    public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) throws IllegalArgumentException {
        if (this.usesNonce) {
            updateIV(this.encryptCipher, true, j);
        }
        byte[] bArr2 = new byte[i2 + this.writeMac.getSize()];
        this.encryptCipher.processBytes(bArr, i, i2, bArr2, 0);
        byte[] bArrCalculateMac = this.writeMac.calculateMac(j, s, bArr, i, i2);
        this.encryptCipher.processBytes(bArrCalculateMac, 0, bArrCalculateMac.length, bArr2, i2);
        return bArr2;
    }

    public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) throws IOException, IllegalArgumentException {
        if (this.usesNonce) {
            updateIV(this.decryptCipher, false, j);
        }
        int size = this.readMac.getSize();
        if (i2 < size) {
            throw new TlsFatalAlert((short) 50);
        }
        int i3 = i2 - size;
        byte[] bArr2 = new byte[i2];
        this.decryptCipher.processBytes(bArr, i, i2, bArr2, 0);
        checkMAC(j, s, bArr2, i3, i2, bArr2, 0, i3);
        return Arrays.copyOfRange(bArr2, 0, i3);
    }

    protected void checkMAC(long j, short s, byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) throws IOException {
        if (!Arrays.constantTimeAreEqual(Arrays.copyOfRange(bArr, i, i2), this.readMac.calculateMac(j, s, bArr2, i3, i4))) {
            throw new TlsFatalAlert((short) 20);
        }
    }

    protected void updateIV(StreamCipher streamCipher, boolean z, long j) throws IllegalArgumentException {
        byte[] bArr = new byte[8];
        TlsUtils.writeUint64(j, bArr, 0);
        streamCipher.init(z, new ParametersWithIV(null, bArr));
    }
}
