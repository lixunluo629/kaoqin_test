package org.bouncycastle.crypto.engines;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/ARIAWrapEngine.class */
public class ARIAWrapEngine extends RFC3394WrapEngine {
    public ARIAWrapEngine() {
        super(new ARIAEngine());
    }

    public ARIAWrapEngine(boolean z) {
        super(new ARIAEngine(), z);
    }
}
