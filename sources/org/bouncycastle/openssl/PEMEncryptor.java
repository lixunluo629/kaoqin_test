package org.bouncycastle.openssl;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/PEMEncryptor.class */
public interface PEMEncryptor {
    String getAlgorithm();

    byte[] getIV();

    byte[] encrypt(byte[] bArr) throws PEMException;
}
