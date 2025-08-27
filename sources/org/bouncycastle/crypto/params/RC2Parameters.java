package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/params/RC2Parameters.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/params/RC2Parameters.class */
public class RC2Parameters implements CipherParameters {
    private byte[] key;
    private int bits;

    public RC2Parameters(byte[] bArr) {
        this(bArr, bArr.length > 128 ? 1024 : bArr.length * 8);
    }

    public RC2Parameters(byte[] bArr, int i) {
        this.key = new byte[bArr.length];
        this.bits = i;
        System.arraycopy(bArr, 0, this.key, 0, bArr.length);
    }

    public byte[] getKey() {
        return this.key;
    }

    public int getEffectiveKeyBits() {
        return this.bits;
    }
}
