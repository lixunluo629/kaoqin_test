package org.apache.commons.collections4;

import java.util.List;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/ListValuedMap.class */
public interface ListValuedMap<K, V> extends MultiValuedMap<K, V> {
    @Override // org.apache.commons.collections4.MultiValuedMap
    List<V> get(K k);

    @Override // org.apache.commons.collections4.MultiValuedMap
    List<V> remove(Object obj);
}
