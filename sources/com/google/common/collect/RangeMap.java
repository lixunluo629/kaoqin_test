package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.lang.Comparable;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/collect/RangeMap.class */
public interface RangeMap<K extends Comparable, V> {
    @Nullable
    V get(K k);

    @Nullable
    Map.Entry<Range<K>, V> getEntry(K k);

    Range<K> span();

    void put(Range<K> range, V v);

    void putAll(RangeMap<K, V> rangeMap);

    void clear();

    void remove(Range<K> range);

    Map<Range<K>, V> asMapOfRanges();

    RangeMap<K, V> subRangeMap(Range<K> range);

    boolean equals(@Nullable Object obj);

    int hashCode();

    String toString();
}
