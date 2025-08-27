package org.bouncycastle.pqc.jcajce.interfaces;

import java.security.PrivateKey;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/jcajce/interfaces/XMSSMTPrivateKey.class */
public interface XMSSMTPrivateKey extends XMSSMTKey, PrivateKey {
    long getUsagesRemaining();

    XMSSMTPrivateKey extractKeyShard(int i);
}
