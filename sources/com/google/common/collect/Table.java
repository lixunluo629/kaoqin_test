package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/Table.class */
public interface Table<R, C, V> {

    /* loaded from: guava-18.0.jar:com/google/common/collect/Table$Cell.class */
    public interface Cell<R, C, V> {
        R getRowKey();

        C getColumnKey();

        V getValue();

        boolean equals(@Nullable Object obj);

        int hashCode();
    }

    boolean contains(@Nullable Object obj, @Nullable Object obj2);

    boolean containsRow(@Nullable Object obj);

    boolean containsColumn(@Nullable Object obj);

    boolean containsValue(@Nullable Object obj);

    V get(@Nullable Object obj, @Nullable Object obj2);

    boolean isEmpty();

    int size();

    boolean equals(@Nullable Object obj);

    int hashCode();

    void clear();

    V put(R r, C c, V v);

    void putAll(Table<? extends R, ? extends C, ? extends V> table);

    V remove(@Nullable Object obj, @Nullable Object obj2);

    Map<C, V> row(R r);

    Map<R, V> column(C c);

    Set<Cell<R, C, V>> cellSet();

    Set<R> rowKeySet();

    Set<C> columnKeySet();

    Collection<V> values();

    Map<R, Map<C, V>> rowMap();

    Map<C, Map<R, V>> columnMap();
}
