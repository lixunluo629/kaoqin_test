package org.bouncycastle.openssl;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/PEMDecryptor.class */
public interface PEMDecryptor {
    byte[] decrypt(byte[] bArr, byte[] bArr2) throws PEMException;
}
