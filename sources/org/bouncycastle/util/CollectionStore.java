package org.bouncycastle.util;

import java.util.ArrayList;
import java.util.Collection;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/CollectionStore.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/CollectionStore.class */
public class CollectionStore implements Store {
    private Collection _local;

    public CollectionStore(Collection collection) {
        this._local = new ArrayList(collection);
    }

    @Override // org.bouncycastle.util.Store
    public Collection getMatches(Selector selector) {
        if (selector == null) {
            return new ArrayList(this._local);
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : this._local) {
            if (selector.match(obj)) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }
}
