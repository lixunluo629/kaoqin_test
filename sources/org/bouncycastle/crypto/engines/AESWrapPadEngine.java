package org.bouncycastle.crypto.engines;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/AESWrapPadEngine.class */
public class AESWrapPadEngine extends RFC5649WrapEngine {
    public AESWrapPadEngine() {
        super(new AESEngine());
    }
}
