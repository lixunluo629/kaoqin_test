package org.apache.commons.collections4;

import java.util.SortedMap;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/Trie.class */
public interface Trie<K, V> extends IterableSortedMap<K, V> {
    SortedMap<K, V> prefixMap(K k);
}
