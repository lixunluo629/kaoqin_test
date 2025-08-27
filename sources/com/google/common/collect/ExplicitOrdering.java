package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/ExplicitOrdering.class */
final class ExplicitOrdering<T> extends Ordering<T> implements Serializable {
    final ImmutableMap<T, Integer> rankMap;
    private static final long serialVersionUID = 0;

    ExplicitOrdering(List<T> valuesInOrder) {
        this(buildRankMap(valuesInOrder));
    }

    ExplicitOrdering(ImmutableMap<T, Integer> rankMap) {
        this.rankMap = rankMap;
    }

    @Override // com.google.common.collect.Ordering, java.util.Comparator
    public int compare(T left, T right) {
        return rank(left) - rank(right);
    }

    private int rank(T value) {
        Integer rank = this.rankMap.get(value);
        if (rank == null) {
            throw new Ordering.IncomparableValueException(value);
        }
        return rank.intValue();
    }

    private static <T> ImmutableMap<T, Integer> buildRankMap(List<T> valuesInOrder) {
        ImmutableMap.Builder<T, Integer> builder = ImmutableMap.builder();
        int rank = 0;
        for (T value : valuesInOrder) {
            int i = rank;
            rank++;
            builder.put(value, Integer.valueOf(i));
        }
        return builder.build();
    }

    @Override // java.util.Comparator
    public boolean equals(@Nullable Object object) {
        if (object instanceof ExplicitOrdering) {
            ExplicitOrdering<?> that = (ExplicitOrdering) object;
            return this.rankMap.equals(that.rankMap);
        }
        return false;
    }

    public int hashCode() {
        return this.rankMap.hashCode();
    }

    public String toString() {
        String strValueOf = String.valueOf(String.valueOf(this.rankMap.keySet()));
        return new StringBuilder(19 + strValueOf.length()).append("Ordering.explicit(").append(strValueOf).append(")").toString();
    }
}
