package io.netty.util.collection;

import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/IntObjectMap.class */
public interface IntObjectMap<V> extends Map<Integer, V> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/IntObjectMap$PrimitiveEntry.class */
    public interface PrimitiveEntry<V> {
        int key();

        V value();

        void setValue(V v);
    }

    V get(int i);

    V put(int i, V v);

    V remove(int i);

    Iterable<PrimitiveEntry<V>> entries();

    boolean containsKey(int i);
}
