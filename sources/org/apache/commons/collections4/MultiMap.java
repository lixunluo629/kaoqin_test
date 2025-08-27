package org.apache.commons.collections4;

import java.util.Collection;

@Deprecated
/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/MultiMap.class */
public interface MultiMap<K, V> extends IterableMap<K, Object> {
    boolean removeMapping(K k, V v);

    @Override // java.util.Map, org.apache.commons.collections4.Get
    int size();

    @Override // java.util.Map, org.apache.commons.collections4.Get
    Object get(Object obj);

    @Override // java.util.Map, org.apache.commons.collections4.Get
    boolean containsValue(Object obj);

    @Override // java.util.Map, org.apache.commons.collections4.Put
    Object put(K k, Object obj);

    @Override // java.util.Map, org.apache.commons.collections4.Get
    Object remove(Object obj);

    @Override // java.util.Map, org.apache.commons.collections4.Get
    Collection<Object> values();
}
