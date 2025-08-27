package org.apache.commons.collections4;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/OrderedBidiMap.class */
public interface OrderedBidiMap<K, V> extends BidiMap<K, V>, OrderedMap<K, V> {
    @Override // org.apache.commons.collections4.BidiMap
    OrderedBidiMap<V, K> inverseBidiMap();
}
