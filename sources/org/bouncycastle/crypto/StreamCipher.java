package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/StreamCipher.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/StreamCipher.class */
public interface StreamCipher {
    void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException;

    String getAlgorithmName();

    byte returnByte(byte b);

    void processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws DataLengthException;

    void reset();
}
