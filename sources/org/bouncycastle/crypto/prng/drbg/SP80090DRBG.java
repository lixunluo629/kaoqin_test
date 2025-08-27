package org.bouncycastle.crypto.prng.drbg;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/prng/drbg/SP80090DRBG.class */
public interface SP80090DRBG {
    int getBlockSize();

    int generate(byte[] bArr, byte[] bArr2, boolean z);

    void reseed(byte[] bArr);
}
