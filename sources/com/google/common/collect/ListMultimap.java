package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/ListMultimap.class */
public interface ListMultimap<K, V> extends Multimap<K, V> {
    List<V> get(@Nullable K k);

    List<V> removeAll(@Nullable Object obj);

    List<V> replaceValues(K k, Iterable<? extends V> iterable);

    Map<K, Collection<V>> asMap();

    boolean equals(@Nullable Object obj);
}
