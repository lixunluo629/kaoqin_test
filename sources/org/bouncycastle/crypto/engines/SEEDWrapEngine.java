package org.bouncycastle.crypto.engines;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/SEEDWrapEngine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/SEEDWrapEngine.class */
public class SEEDWrapEngine extends RFC3394WrapEngine {
    public SEEDWrapEngine() {
        super(new SEEDEngine());
    }
}
