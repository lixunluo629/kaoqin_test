package io.netty.util.collection;

import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/LongObjectMap.class */
public interface LongObjectMap<V> extends Map<Long, V> {

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/LongObjectMap$PrimitiveEntry.class */
    public interface PrimitiveEntry<V> {
        long key();

        V value();

        void setValue(V v);
    }

    V get(long j);

    V put(long j, V v);

    V remove(long j);

    Iterable<PrimitiveEntry<V>> entries();

    boolean containsKey(long j);
}
