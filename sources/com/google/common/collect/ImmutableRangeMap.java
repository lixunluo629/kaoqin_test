package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.SortedLists;
import java.lang.Comparable;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("NavigableMap")
/* loaded from: guava-18.0.jar:com/google/common/collect/ImmutableRangeMap.class */
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V> {
    private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap<>(ImmutableList.of(), ImmutableList.of());
    private final ImmutableList<Range<K>> ranges;
    private final ImmutableList<V> values;

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
        return (ImmutableRangeMap<K, V>) EMPTY;
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
        return new ImmutableRangeMap<>(ImmutableList.of(range), ImmutableList.of(value));
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
        if (rangeMap instanceof ImmutableRangeMap) {
            return (ImmutableRangeMap) rangeMap;
        }
        Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
        ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(map.size());
        ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(map.size());
        for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
            rangesBuilder.add((ImmutableList.Builder<Range<K>>) entry.getKey());
            valuesBuilder.add((ImmutableList.Builder<V>) entry.getValue());
        }
        return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
    }

    public static <K extends Comparable<?>, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/ImmutableRangeMap$Builder.class */
    public static final class Builder<K extends Comparable<?>, V> {
        private final RangeSet<K> keyRanges = TreeRangeSet.create();
        private final RangeMap<K, V> rangeMap = TreeRangeMap.create();

        public Builder<K, V> put(Range<K> range, V value) {
            Preconditions.checkNotNull(range);
            Preconditions.checkNotNull(value);
            Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
            if (!this.keyRanges.complement().encloses(range)) {
                for (Map.Entry<Range<K>, V> entry : this.rangeMap.asMapOfRanges().entrySet()) {
                    Range<K> key = entry.getKey();
                    if (key.isConnected(range) && !key.intersection(range).isEmpty()) {
                        String strValueOf = String.valueOf(String.valueOf(range));
                        String strValueOf2 = String.valueOf(String.valueOf(entry));
                        throw new IllegalArgumentException(new StringBuilder(47 + strValueOf.length() + strValueOf2.length()).append("Overlapping ranges: range ").append(strValueOf).append(" overlaps with entry ").append(strValueOf2).toString());
                    }
                }
            }
            this.keyRanges.add(range);
            this.rangeMap.put(range, value);
            return this;
        }

        public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
            for (Map.Entry<Range<K>, ? extends V> entry : rangeMap.asMapOfRanges().entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableRangeMap<K, V> build() {
            Map<Range<K>, V> map = this.rangeMap.asMapOfRanges();
            ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(map.size());
            ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(map.size());
            for (Map.Entry<Range<K>, V> entry : map.entrySet()) {
                rangesBuilder.add((ImmutableList.Builder<Range<K>>) entry.getKey());
                valuesBuilder.add((ImmutableList.Builder<V>) entry.getValue());
            }
            return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
        }
    }

    ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
        this.ranges = ranges;
        this.values = values;
    }

    @Override // com.google.common.collect.RangeMap
    @Nullable
    public V get(K key) {
        int index = SortedLists.binarySearch(this.ranges, (Function<? super E, Cut>) Range.lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        Range<K> range = this.ranges.get(index);
        if (range.contains(key)) {
            return this.values.get(index);
        }
        return null;
    }

    @Override // com.google.common.collect.RangeMap
    @Nullable
    public Map.Entry<Range<K>, V> getEntry(K key) {
        int index = SortedLists.binarySearch(this.ranges, (Function<? super E, Cut>) Range.lowerBoundFn(), Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
        if (index == -1) {
            return null;
        }
        Range<K> range = this.ranges.get(index);
        if (range.contains(key)) {
            return Maps.immutableEntry(range, this.values.get(index));
        }
        return null;
    }

    @Override // com.google.common.collect.RangeMap
    public Range<K> span() {
        if (this.ranges.isEmpty()) {
            throw new NoSuchElementException();
        }
        Range<K> firstRange = this.ranges.get(0);
        Range<K> lastRange = this.ranges.get(this.ranges.size() - 1);
        return Range.create(firstRange.lowerBound, lastRange.upperBound);
    }

    @Override // com.google.common.collect.RangeMap
    public void put(Range<K> range, V value) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    public void putAll(RangeMap<K, V> rangeMap) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    public void remove(Range<K> range) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.RangeMap
    public ImmutableMap<Range<K>, V> asMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.of();
        }
        RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges, Range.RANGE_LEX_ORDERING);
        return new RegularImmutableSortedMap(rangeSet, this.values);
    }

    @Override // com.google.common.collect.RangeMap
    public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
        if (((Range) Preconditions.checkNotNull(range)).isEmpty()) {
            return of();
        }
        if (this.ranges.isEmpty() || range.encloses(span())) {
            return this;
        }
        final int iBinarySearch = SortedLists.binarySearch(this.ranges, (Function<? super E, Comparable>) Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        int iBinarySearch2 = SortedLists.binarySearch(this.ranges, (Function<? super E, Comparable>) Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
        if (iBinarySearch >= iBinarySearch2) {
            return of();
        }
        final int i = iBinarySearch2 - iBinarySearch;
        return (ImmutableRangeMap<K, V>) new ImmutableRangeMap<K, V>(new ImmutableList<Range<K>>() { // from class: com.google.common.collect.ImmutableRangeMap.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return i;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.List
            public Range<K> get(int index) {
                Preconditions.checkElementIndex(index, i);
                return (index == 0 || index == i - 1) ? ((Range) ImmutableRangeMap.this.ranges.get(index + iBinarySearch)).intersection(range) : (Range) ImmutableRangeMap.this.ranges.get(index + iBinarySearch);
            }

            @Override // com.google.common.collect.ImmutableCollection
            boolean isPartialView() {
                return true;
            }
        }, this.values.subList(iBinarySearch, iBinarySearch2)) { // from class: com.google.common.collect.ImmutableRangeMap.2
            @Override // com.google.common.collect.ImmutableRangeMap, com.google.common.collect.RangeMap
            public /* bridge */ /* synthetic */ Map asMapOfRanges() {
                return super.asMapOfRanges();
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.ImmutableRangeMap, com.google.common.collect.RangeMap
            public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
                if (range.isConnected(subRange)) {
                    return this.subRangeMap((Range) subRange.intersection(range));
                }
                return ImmutableRangeMap.of();
            }
        };
    }

    @Override // com.google.common.collect.RangeMap
    public int hashCode() {
        return asMapOfRanges().hashCode();
    }

    @Override // com.google.common.collect.RangeMap
    public boolean equals(@Nullable Object o) {
        if (o instanceof RangeMap) {
            RangeMap<?, ?> rangeMap = (RangeMap) o;
            return asMapOfRanges().equals(rangeMap.asMapOfRanges());
        }
        return false;
    }

    @Override // com.google.common.collect.RangeMap
    public String toString() {
        return asMapOfRanges().toString();
    }
}
