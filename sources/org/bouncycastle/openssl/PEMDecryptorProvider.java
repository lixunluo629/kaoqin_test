package org.bouncycastle.openssl;

import org.bouncycastle.operator.OperatorCreationException;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/openssl/PEMDecryptorProvider.class */
public interface PEMDecryptorProvider {
    PEMDecryptor get(String str) throws OperatorCreationException;
}
