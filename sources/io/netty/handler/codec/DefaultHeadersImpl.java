package io.netty.handler.codec;

import io.netty.handler.codec.DefaultHeaders;
import io.netty.util.HashingStrategy;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/DefaultHeadersImpl.class */
public final class DefaultHeadersImpl<K, V> extends DefaultHeaders<K, V, DefaultHeadersImpl<K, V>> {
    public DefaultHeadersImpl(HashingStrategy<K> nameHashingStrategy, ValueConverter<V> valueConverter, DefaultHeaders.NameValidator<K> nameValidator) {
        super(nameHashingStrategy, valueConverter, nameValidator);
    }
}
