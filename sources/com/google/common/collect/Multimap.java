package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/Multimap.class */
public interface Multimap<K, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(@Nullable Object obj);

    boolean containsValue(@Nullable Object obj);

    boolean containsEntry(@Nullable Object obj, @Nullable Object obj2);

    boolean put(@Nullable K k, @Nullable V v);

    boolean remove(@Nullable Object obj, @Nullable Object obj2);

    boolean putAll(@Nullable K k, Iterable<? extends V> iterable);

    boolean putAll(Multimap<? extends K, ? extends V> multimap);

    Collection<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable);

    Collection<V> removeAll(@Nullable Object obj);

    void clear();

    Collection<V> get(@Nullable K k);

    Set<K> keySet();

    Multiset<K> keys();

    Collection<V> values();

    Collection<Map.Entry<K, V>> entries();

    Map<K, Collection<V>> asMap();

    boolean equals(@Nullable Object obj);

    int hashCode();
}
