package io.netty.util.collection;

import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ByteObjectMap.class */
public interface ByteObjectMap<V> extends Map<Byte, V> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ByteObjectMap$PrimitiveEntry.class */
    public interface PrimitiveEntry<V> {
        byte key();

        V value();

        void setValue(V v);
    }

    V get(byte b);

    V put(byte b, V v);

    V remove(byte b);

    Iterable<PrimitiveEntry<V>> entries();

    boolean containsKey(byte b);
}
