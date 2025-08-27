package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.Multiset;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/RegularImmutableMultiset.class */
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
    private final transient ImmutableMap<E, Integer> map;
    private final transient int size;

    RegularImmutableMultiset(ImmutableMap<E, Integer> map, int size) {
        this.map = map;
        this.size = size;
    }

    @Override // com.google.common.collect.ImmutableCollection
    boolean isPartialView() {
        return this.map.isPartialView();
    }

    @Override // com.google.common.collect.Multiset
    public int count(@Nullable Object element) {
        Integer value = this.map.get(element);
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(@Nullable Object element) {
        return this.map.containsKey(element);
    }

    @Override // com.google.common.collect.Multiset
    public ImmutableSet<E> elementSet() {
        return this.map.keySet();
    }

    @Override // com.google.common.collect.ImmutableMultiset
    Multiset.Entry<E> getEntry(int index) {
        Map.Entry<E, Integer> mapEntry = this.map.entrySet().asList().get(index);
        return Multisets.immutableEntry(mapEntry.getKey(), mapEntry.getValue().intValue());
    }

    @Override // com.google.common.collect.ImmutableMultiset, java.util.Collection, com.google.common.collect.Multiset
    public int hashCode() {
        return this.map.hashCode();
    }
}
