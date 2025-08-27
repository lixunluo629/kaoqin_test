package org.bouncycastle.crypto.prng;

import org.bouncycastle.crypto.prng.drbg.SP80090DRBG;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/prng/DRBGProvider.class */
interface DRBGProvider {
    SP80090DRBG get(EntropySource entropySource);
}
