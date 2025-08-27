package org.bouncycastle.jce.interfaces;

import java.security.PublicKey;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/interfaces/MQVPublicKey.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/interfaces/MQVPublicKey.class */
public interface MQVPublicKey extends PublicKey {
    PublicKey getStaticKey();

    PublicKey getEphemeralKey();
}
