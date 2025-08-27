package org.apache.commons.collections4;

import java.util.Set;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/BidiMap.class */
public interface BidiMap<K, V> extends IterableMap<K, V> {
    @Override // java.util.Map, org.apache.commons.collections4.Put
    V put(K k, V v);

    K getKey(Object obj);

    K removeValue(Object obj);

    BidiMap<V, K> inverseBidiMap();

    @Override // java.util.Map, org.apache.commons.collections4.Get
    Set<V> values();
}
