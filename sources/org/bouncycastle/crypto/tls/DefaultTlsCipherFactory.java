package org.bouncycastle.crypto.tls;

import java.io.IOException;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/DefaultTlsCipherFactory.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/DefaultTlsCipherFactory.class */
public class DefaultTlsCipherFactory implements TlsCipherFactory {
    @Override // org.bouncycastle.crypto.tls.TlsCipherFactory
    public TlsCipher createCipher(TlsClientContext tlsClientContext, int i, int i2) throws IOException {
        switch (i) {
            case 7:
                return createDESedeCipher(tlsClientContext, 24, i2);
            case 8:
                return createAESCipher(tlsClientContext, 16, i2);
            case 9:
                return createAESCipher(tlsClientContext, 32, i2);
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }

    protected TlsCipher createAESCipher(TlsClientContext tlsClientContext, int i, int i2) throws IOException {
        return new TlsBlockCipher(tlsClientContext, createAESBlockCipher(), createAESBlockCipher(), createDigest(i2), createDigest(i2), i);
    }

    protected TlsCipher createDESedeCipher(TlsClientContext tlsClientContext, int i, int i2) throws IOException {
        return new TlsBlockCipher(tlsClientContext, createDESedeBlockCipher(), createDESedeBlockCipher(), createDigest(i2), createDigest(i2), i);
    }

    protected BlockCipher createAESBlockCipher() {
        return new CBCBlockCipher(new AESFastEngine());
    }

    protected BlockCipher createDESedeBlockCipher() {
        return new CBCBlockCipher(new DESedeEngine());
    }

    protected Digest createDigest(int i) throws IOException {
        switch (i) {
            case 1:
                return new MD5Digest();
            case 2:
                return new SHA1Digest();
            case 3:
                return new SHA256Digest();
            case 4:
                return new SHA384Digest();
            default:
                throw new TlsFatalAlert((short) 80);
        }
    }
}
