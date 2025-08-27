package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/MapDifference.class */
public interface MapDifference<K, V> {

    /* loaded from: guava-18.0.jar:com/google/common/collect/MapDifference$ValueDifference.class */
    public interface ValueDifference<V> {
        V leftValue();

        V rightValue();

        boolean equals(@Nullable Object obj);

        int hashCode();
    }

    boolean areEqual();

    Map<K, V> entriesOnlyOnLeft();

    Map<K, V> entriesOnlyOnRight();

    Map<K, V> entriesInCommon();

    Map<K, ValueDifference<V>> entriesDiffering();

    boolean equals(@Nullable Object obj);

    int hashCode();
}
