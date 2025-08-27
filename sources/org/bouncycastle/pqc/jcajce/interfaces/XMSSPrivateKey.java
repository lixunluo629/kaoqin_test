package org.bouncycastle.pqc.jcajce.interfaces;

import java.security.PrivateKey;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/jcajce/interfaces/XMSSPrivateKey.class */
public interface XMSSPrivateKey extends XMSSKey, PrivateKey {
    long getUsagesRemaining();

    XMSSPrivateKey extractKeyShard(int i);
}
