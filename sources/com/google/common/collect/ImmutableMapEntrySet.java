package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/ImmutableMapEntrySet.class */
abstract class ImmutableMapEntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> {
    abstract ImmutableMap<K, V> map();

    ImmutableMapEntrySet() {
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return map().size();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@Nullable Object object) {
        if (object instanceof Map.Entry) {
            Map.Entry<?, ?> entry = (Map.Entry) object;
            V value = map().get(entry.getKey());
            return value != null && value.equals(entry.getValue());
        }
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return map().isPartialView();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    @GwtIncompatible("serialization")
    Object writeReplace() {
        return new EntrySetSerializedForm(map());
    }

    @GwtIncompatible("serialization")
    /* loaded from: guava-18.0.jar:com/google/common/collect/ImmutableMapEntrySet$EntrySetSerializedForm.class */
    private static class EntrySetSerializedForm<K, V> implements Serializable {
        final ImmutableMap<K, V> map;
        private static final long serialVersionUID = 0;

        EntrySetSerializedForm(ImmutableMap<K, V> map) {
            this.map = map;
        }

        Object readResolve() {
            return this.map.entrySet();
        }
    }
}
