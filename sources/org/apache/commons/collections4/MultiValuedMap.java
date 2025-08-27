package org.apache.commons.collections4;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/MultiValuedMap.class */
public interface MultiValuedMap<K, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(Object obj);

    boolean containsValue(Object obj);

    boolean containsMapping(Object obj, Object obj2);

    Collection<V> get(K k);

    boolean put(K k, V v);

    boolean putAll(K k, Iterable<? extends V> iterable);

    boolean putAll(Map<? extends K, ? extends V> map);

    boolean putAll(MultiValuedMap<? extends K, ? extends V> multiValuedMap);

    Collection<V> remove(Object obj);

    boolean removeMapping(Object obj, Object obj2);

    void clear();

    Collection<Map.Entry<K, V>> entries();

    MultiSet<K> keys();

    Set<K> keySet();

    Collection<V> values();

    Map<K, Collection<V>> asMap();

    MapIterator<K, V> mapIterator();
}
