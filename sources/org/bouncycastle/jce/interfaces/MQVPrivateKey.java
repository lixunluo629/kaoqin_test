package org.bouncycastle.jce.interfaces;

import java.security.PrivateKey;
import java.security.PublicKey;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/interfaces/MQVPrivateKey.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/interfaces/MQVPrivateKey.class */
public interface MQVPrivateKey extends PrivateKey {
    PrivateKey getStaticPrivateKey();

    PrivateKey getEphemeralPrivateKey();

    PublicKey getEphemeralPublicKey();
}
