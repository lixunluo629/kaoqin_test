package org.bouncycastle.pqc.jcajce.interfaces;

import java.security.PublicKey;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/jcajce/interfaces/NHPublicKey.class */
public interface NHPublicKey extends NHKey, PublicKey {
    byte[] getPublicData();
}
