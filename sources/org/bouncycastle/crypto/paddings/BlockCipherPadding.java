package org.bouncycastle.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.InvalidCipherTextException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/paddings/BlockCipherPadding.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/paddings/BlockCipherPadding.class */
public interface BlockCipherPadding {
    void init(SecureRandom secureRandom) throws IllegalArgumentException;

    String getPaddingName();

    int addPadding(byte[] bArr, int i);

    int padCount(byte[] bArr) throws InvalidCipherTextException;
}
