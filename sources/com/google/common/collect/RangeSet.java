package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.lang.Comparable;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/collect/RangeSet.class */
public interface RangeSet<C extends Comparable> {
    boolean contains(C c);

    Range<C> rangeContaining(C c);

    boolean encloses(Range<C> range);

    boolean enclosesAll(RangeSet<C> rangeSet);

    boolean isEmpty();

    Range<C> span();

    Set<Range<C>> asRanges();

    RangeSet<C> complement();

    RangeSet<C> subRangeSet(Range<C> range);

    void add(Range<C> range);

    void remove(Range<C> range);

    void clear();

    void addAll(RangeSet<C> rangeSet);

    void removeAll(RangeSet<C> rangeSet);

    boolean equals(@Nullable Object obj);

    int hashCode();

    String toString();
}
