package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/AsymmetricCipherKeyPair.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/AsymmetricCipherKeyPair.class */
public class AsymmetricCipherKeyPair {
    private CipherParameters publicParam;
    private CipherParameters privateParam;

    public AsymmetricCipherKeyPair(CipherParameters cipherParameters, CipherParameters cipherParameters2) {
        this.publicParam = cipherParameters;
        this.privateParam = cipherParameters2;
    }

    public CipherParameters getPublic() {
        return this.publicParam;
    }

    public CipherParameters getPrivate() {
        return this.privateParam;
    }
}
