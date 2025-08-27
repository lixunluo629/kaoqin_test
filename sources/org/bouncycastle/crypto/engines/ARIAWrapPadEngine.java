package org.bouncycastle.crypto.engines;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/ARIAWrapPadEngine.class */
public class ARIAWrapPadEngine extends RFC5649WrapEngine {
    public ARIAWrapPadEngine() {
        super(new ARIAEngine());
    }
}
