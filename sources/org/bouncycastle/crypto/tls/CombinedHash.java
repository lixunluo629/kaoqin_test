package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/CombinedHash.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/CombinedHash.class */
class CombinedHash implements Digest {
    private MD5Digest md5;
    private SHA1Digest sha1;

    CombinedHash() {
        this.md5 = new MD5Digest();
        this.sha1 = new SHA1Digest();
    }

    CombinedHash(CombinedHash combinedHash) {
        this.md5 = new MD5Digest(combinedHash.md5);
        this.sha1 = new SHA1Digest(combinedHash.sha1);
    }

    @Override // org.bouncycastle.crypto.Digest
    public String getAlgorithmName() {
        return this.md5.getAlgorithmName() + " and " + this.sha1.getAlgorithmName() + " for TLS 1.0";
    }

    @Override // org.bouncycastle.crypto.Digest
    public int getDigestSize() {
        return 36;
    }

    @Override // org.bouncycastle.crypto.Digest
    public void update(byte b) {
        this.md5.update(b);
        this.sha1.update(b);
    }

    @Override // org.bouncycastle.crypto.Digest
    public void update(byte[] bArr, int i, int i2) {
        this.md5.update(bArr, i, i2);
        this.sha1.update(bArr, i, i2);
    }

    @Override // org.bouncycastle.crypto.Digest
    public int doFinal(byte[] bArr, int i) {
        return this.md5.doFinal(bArr, i) + this.sha1.doFinal(bArr, i + 16);
    }

    @Override // org.bouncycastle.crypto.Digest
    public void reset() {
        this.md5.reset();
        this.sha1.reset();
    }
}
