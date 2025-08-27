package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/modes/AEADBlockCipher.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/modes/AEADBlockCipher.class */
public interface AEADBlockCipher {
    void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException;

    String getAlgorithmName();

    BlockCipher getUnderlyingCipher();

    int processByte(byte b, byte[] bArr, int i) throws DataLengthException;

    int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) throws DataLengthException;

    int doFinal(byte[] bArr, int i) throws InvalidCipherTextException, IllegalStateException;

    byte[] getMac();

    int getUpdateOutputSize(int i);

    int getOutputSize(int i);

    void reset();
}
