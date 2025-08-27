package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/AbstractMapEntry.class */
abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
    @Override // java.util.Map.Entry
    public abstract K getKey();

    @Override // java.util.Map.Entry
    public abstract V getValue();

    AbstractMapEntry() {
    }

    @Override // java.util.Map.Entry
    public V setValue(V value) {
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
