package org.apache.commons.collections4;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/OrderedMapIterator.class */
public interface OrderedMapIterator<K, V> extends MapIterator<K, V>, OrderedIterator<K> {
    @Override // org.apache.commons.collections4.OrderedIterator
    boolean hasPrevious();

    @Override // org.apache.commons.collections4.OrderedIterator
    K previous();
}
