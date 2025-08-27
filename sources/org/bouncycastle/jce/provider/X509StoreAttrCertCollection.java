package org.bouncycastle.jce.provider;

import java.util.Collection;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Selector;
import org.bouncycastle.x509.X509CollectionStoreParameters;
import org.bouncycastle.x509.X509StoreParameters;
import org.bouncycastle.x509.X509StoreSpi;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/provider/X509StoreAttrCertCollection.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/provider/X509StoreAttrCertCollection.class */
public class X509StoreAttrCertCollection extends X509StoreSpi {
    private CollectionStore _store;

    @Override // org.bouncycastle.x509.X509StoreSpi
    public void engineInit(X509StoreParameters x509StoreParameters) {
        if (!(x509StoreParameters instanceof X509CollectionStoreParameters)) {
            throw new IllegalArgumentException(x509StoreParameters.toString());
        }
        this._store = new CollectionStore(((X509CollectionStoreParameters) x509StoreParameters).getCollection());
    }

    @Override // org.bouncycastle.x509.X509StoreSpi
    public Collection engineGetMatches(Selector selector) {
        return this._store.getMatches(selector);
    }
}
