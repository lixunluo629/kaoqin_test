package org.bouncycastle.pqc.jcajce.interfaces;

import java.security.PrivateKey;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/jcajce/interfaces/NHPrivateKey.class */
public interface NHPrivateKey extends NHKey, PrivateKey {
    short[] getSecretData();
}
