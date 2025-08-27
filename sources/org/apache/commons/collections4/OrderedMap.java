package org.apache.commons.collections4;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/OrderedMap.class */
public interface OrderedMap<K, V> extends IterableMap<K, V> {
    @Override // org.apache.commons.collections4.IterableGet
    OrderedMapIterator<K, V> mapIterator();

    K firstKey();

    K lastKey();

    K nextKey(K k);

    K previousKey(K k);
}
