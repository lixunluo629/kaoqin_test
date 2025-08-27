package org.springframework.data.util;

import lombok.Generated;
import lombok.NonNull;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/Pair.class */
public final class Pair<S, T> {

    @NonNull
    private final S first;

    @NonNull
    private final T second;

    @Generated
    public String toString() {
        return "Pair(first=" + getFirst() + ", second=" + getSecond() + ")";
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> other = (Pair) o;
        Object this$first = getFirst();
        Object other$first = other.getFirst();
        if (this$first == null) {
            if (other$first != null) {
                return false;
            }
        } else if (!this$first.equals(other$first)) {
            return false;
        }
        Object this$second = getSecond();
        Object other$second = other.getSecond();
        return this$second == null ? other$second == null : this$second.equals(other$second);
    }

    @Generated
    public int hashCode() {
        Object $first = getFirst();
        int result = (1 * 59) + ($first == null ? 43 : $first.hashCode());
        Object $second = getSecond();
        return (result * 59) + ($second == null ? 43 : $second.hashCode());
    }

    @Generated
    private Pair(@NonNull S first, @NonNull T second) {
        if (first == null) {
            throw new IllegalArgumentException("first is marked @NonNull but is null");
        }
        if (second == null) {
            throw new IllegalArgumentException("second is marked @NonNull but is null");
        }
        this.first = first;
        this.second = second;
    }

    public static <S, T> Pair<S, T> of(S first, T second) {
        return new Pair<>(first, second);
    }

    public S getFirst() {
        return this.first;
    }

    public T getSecond() {
        return this.second;
    }
}
