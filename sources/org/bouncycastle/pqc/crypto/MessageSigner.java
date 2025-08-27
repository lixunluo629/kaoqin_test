package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.CipherParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/MessageSigner.class */
public interface MessageSigner {
    void init(boolean z, CipherParameters cipherParameters);

    byte[] generateSignature(byte[] bArr);

    boolean verifySignature(byte[] bArr, byte[] bArr2);
}
