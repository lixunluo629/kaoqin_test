package io.netty.util.collection;

import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/CharObjectMap.class */
public interface CharObjectMap<V> extends Map<Character, V> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/CharObjectMap$PrimitiveEntry.class */
    public interface PrimitiveEntry<V> {
        char key();

        V value();

        void setValue(V v);
    }

    V get(char c);

    V put(char c, V v);

    V remove(char c);

    Iterable<PrimitiveEntry<V>> entries();

    boolean containsKey(char c);
}
