package org.bouncycastle.jce.interfaces;

import java.security.SecureRandom;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/interfaces/BCKeyStore.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/interfaces/BCKeyStore.class */
public interface BCKeyStore {
    void setRandom(SecureRandom secureRandom);
}
