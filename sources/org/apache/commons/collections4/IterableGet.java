package org.apache.commons.collections4;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/IterableGet.class */
public interface IterableGet<K, V> extends Get<K, V> {
    MapIterator<K, V> mapIterator();
}
