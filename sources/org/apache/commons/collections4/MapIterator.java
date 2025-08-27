package org.apache.commons.collections4;

import java.util.Iterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/MapIterator.class */
public interface MapIterator<K, V> extends Iterator<K> {
    @Override // java.util.Iterator
    boolean hasNext();

    @Override // java.util.Iterator
    K next();

    K getKey();

    V getValue();

    @Override // java.util.Iterator
    void remove();

    V setValue(V v);
}
