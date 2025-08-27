package org.bouncycastle.pqc.jcajce.interfaces;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/jcajce/interfaces/XMSSMTKey.class */
public interface XMSSMTKey {
    int getHeight();

    int getLayers();

    String getTreeDigest();
}
