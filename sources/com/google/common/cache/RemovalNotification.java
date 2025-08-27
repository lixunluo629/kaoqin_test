package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/cache/RemovalNotification.class */
public final class RemovalNotification<K, V> implements Map.Entry<K, V> {

    @Nullable
    private final K key;

    @Nullable
    private final V value;
    private final RemovalCause cause;
    private static final long serialVersionUID = 0;

    RemovalNotification(@Nullable K key, @Nullable V value, RemovalCause cause) {
        this.key = key;
        this.value = value;
        this.cause = (RemovalCause) Preconditions.checkNotNull(cause);
    }

    public RemovalCause getCause() {
        return this.cause;
    }

    public boolean wasEvicted() {
        return this.cause.wasEvicted();
    }

    @Override // java.util.Map.Entry
    @Nullable
    public K getKey() {
        return this.key;
    }

    @Override // java.util.Map.Entry
    @Nullable
    public V getValue() {
        return this.value;
    }

    @Override // java.util.Map.Entry
    public final V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map.Entry
    public boolean equals(@Nullable Object object) {
        if (object instanceof Map.Entry) {
            Map.Entry<?, ?> that = (Map.Entry) object;
            return Objects.equal(getKey(), that.getKey()) && Objects.equal(getValue(), that.getValue());
        }
        return false;
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        K k = getKey();
        V v = getValue();
        return (k == null ? 0 : k.hashCode()) ^ (v == null ? 0 : v.hashCode());
    }

    public String toString() {
        String strValueOf = String.valueOf(String.valueOf(getKey()));
        String strValueOf2 = String.valueOf(String.valueOf(getValue()));
        return new StringBuilder(1 + strValueOf.length() + strValueOf2.length()).append(strValueOf).append(SymbolConstants.EQUAL_SYMBOL).append(strValueOf2).toString();
    }
}
