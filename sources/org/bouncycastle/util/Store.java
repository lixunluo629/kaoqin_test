package org.bouncycastle.util;

import java.util.Collection;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/util/Store.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/util/Store.class */
public interface Store {
    Collection getMatches(Selector selector) throws StoreException;
}
