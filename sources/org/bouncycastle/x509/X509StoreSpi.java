package org.bouncycastle.x509;

import java.util.Collection;
import org.bouncycastle.util.Selector;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/x509/X509StoreSpi.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/x509/X509StoreSpi.class */
public abstract class X509StoreSpi {
    public abstract void engineInit(X509StoreParameters x509StoreParameters);

    public abstract Collection engineGetMatches(Selector selector);
}
