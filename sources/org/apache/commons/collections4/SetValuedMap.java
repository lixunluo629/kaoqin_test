package org.apache.commons.collections4;

import java.util.Set;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/SetValuedMap.class */
public interface SetValuedMap<K, V> extends MultiValuedMap<K, V> {
    @Override // org.apache.commons.collections4.MultiValuedMap
    Set<V> get(K k);

    @Override // org.apache.commons.collections4.MultiValuedMap
    Set<V> remove(Object obj);
}
