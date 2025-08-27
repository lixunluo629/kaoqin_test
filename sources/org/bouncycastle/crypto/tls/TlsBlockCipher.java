package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsBlockCipher.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsBlockCipher.class */
public class TlsBlockCipher implements TlsCipher {
    protected TlsClientContext context;
    protected BlockCipher encryptCipher;
    protected BlockCipher decryptCipher;
    protected TlsMac writeMac;
    protected TlsMac readMac;

    public TlsBlockCipher(TlsClientContext tlsClientContext, BlockCipher blockCipher, BlockCipher blockCipher2, Digest digest, Digest digest2, int i) throws IllegalArgumentException {
        this.context = tlsClientContext;
        this.encryptCipher = blockCipher;
        this.decryptCipher = blockCipher2;
        int digestSize = (2 * i) + digest.getDigestSize() + digest2.getDigestSize() + blockCipher.getBlockSize() + blockCipher2.getBlockSize();
        SecurityParameters securityParameters = tlsClientContext.getSecurityParameters();
        byte[] bArrPRF = TlsUtils.PRF(securityParameters.masterSecret, "key expansion", TlsUtils.concat(securityParameters.serverRandom, securityParameters.clientRandom), digestSize);
        this.writeMac = new TlsMac(digest, bArrPRF, 0, digest.getDigestSize());
        int digestSize2 = 0 + digest.getDigestSize();
        this.readMac = new TlsMac(digest2, bArrPRF, digestSize2, digest2.getDigestSize());
        int digestSize3 = digestSize2 + digest2.getDigestSize();
        initCipher(true, blockCipher, bArrPRF, i, digestSize3, digestSize3 + (i * 2));
        int i2 = digestSize3 + i;
        initCipher(false, blockCipher2, bArrPRF, i, i2, i2 + i + blockCipher.getBlockSize());
    }

    protected void initCipher(boolean z, BlockCipher blockCipher, byte[] bArr, int i, int i2, int i3) throws IllegalArgumentException {
        blockCipher.init(z, new ParametersWithIV(new KeyParameter(bArr, i2, i), bArr, i3, blockCipher.getBlockSize()));
    }

    @Override // org.bouncycastle.crypto.tls.TlsCipher
    public byte[] encodePlaintext(short s, byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException {
        int blockSize = this.encryptCipher.getBlockSize();
        int size = blockSize - (((i2 + this.writeMac.getSize()) + 1) % blockSize);
        int iChooseExtraPadBlocks = size + (chooseExtraPadBlocks(this.context.getSecureRandom(), (255 - size) / blockSize) * blockSize);
        int size2 = i2 + this.writeMac.getSize() + iChooseExtraPadBlocks + 1;
        byte[] bArr2 = new byte[size2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        byte[] bArrCalculateMac = this.writeMac.calculateMac(s, bArr, i, i2);
        System.arraycopy(bArrCalculateMac, 0, bArr2, i2, bArrCalculateMac.length);
        int length = i2 + bArrCalculateMac.length;
        for (int i3 = 0; i3 <= iChooseExtraPadBlocks; i3++) {
            bArr2[i3 + length] = (byte) iChooseExtraPadBlocks;
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 >= size2) {
                return bArr2;
            }
            this.encryptCipher.processBlock(bArr2, i5, bArr2, i5);
            i4 = i5 + blockSize;
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsCipher
    public byte[] decodeCiphertext(short s, byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException, IOException {
        int size = this.readMac.getSize() + 1;
        int blockSize = this.decryptCipher.getBlockSize();
        boolean z = false;
        if (i2 < size) {
            throw new TlsFatalAlert((short) 50);
        }
        if (i2 % blockSize != 0) {
            throw new TlsFatalAlert((short) 21);
        }
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= i2) {
                break;
            }
            this.decryptCipher.processBlock(bArr, i4 + i, bArr, i4 + i);
            i3 = i4 + blockSize;
        }
        int i5 = (i + i2) - 1;
        byte b = bArr[i5];
        int i6 = b & 255;
        if (i6 > i2 - size) {
            z = true;
            i6 = 0;
        } else {
            byte b2 = 0;
            for (int i7 = i5 - i6; i7 < i5; i7++) {
                b2 = (byte) (b2 | (bArr[i7] ^ b));
            }
            if (b2 != 0) {
                z = true;
                i6 = 0;
            }
        }
        int i8 = (i2 - size) - i6;
        byte[] bArrCalculateMac = this.readMac.calculateMac(s, bArr, i, i8);
        byte[] bArr2 = new byte[bArrCalculateMac.length];
        System.arraycopy(bArr, i + i8, bArr2, 0, bArrCalculateMac.length);
        if (!Arrays.constantTimeAreEqual(bArrCalculateMac, bArr2)) {
            z = true;
        }
        if (z) {
            throw new TlsFatalAlert((short) 20);
        }
        byte[] bArr3 = new byte[i8];
        System.arraycopy(bArr, i, bArr3, 0, i8);
        return bArr3;
    }

    protected int chooseExtraPadBlocks(SecureRandom secureRandom, int i) {
        return Math.min(lowestBitSet(secureRandom.nextInt()), i);
    }

    protected int lowestBitSet(int i) {
        if (i == 0) {
            return 32;
        }
        int i2 = 0;
        while ((i & 1) == 0) {
            i2++;
            i >>= 1;
        }
        return i2;
    }
}
