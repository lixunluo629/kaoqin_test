package io.netty.handler.codec.serialization;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/serialization/SoftReferenceMap.class */
final class SoftReferenceMap<K, V> extends ReferenceMap<K, V> {
    SoftReferenceMap(Map<K, Reference<V>> delegate) {
        super(delegate);
    }

    @Override // io.netty.handler.codec.serialization.ReferenceMap
    Reference<V> fold(V value) {
        return new SoftReference(value);
    }
}
