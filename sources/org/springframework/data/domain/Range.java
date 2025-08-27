package org.springframework.data.domain;

import java.lang.Comparable;
import lombok.Generated;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/domain/Range.class */
public final class Range<T extends Comparable<T>> {
    private final T lowerBound;
    private final T upperBound;
    private final boolean lowerInclusive;
    private final boolean upperInclusive;

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Range)) {
            return false;
        }
        Range<?> other = (Range) o;
        Object this$lowerBound = getLowerBound();
        Object other$lowerBound = other.getLowerBound();
        if (this$lowerBound == null) {
            if (other$lowerBound != null) {
                return false;
            }
        } else if (!this$lowerBound.equals(other$lowerBound)) {
            return false;
        }
        Object this$upperBound = getUpperBound();
        Object other$upperBound = other.getUpperBound();
        if (this$upperBound == null) {
            if (other$upperBound != null) {
                return false;
            }
        } else if (!this$upperBound.equals(other$upperBound)) {
            return false;
        }
        return isLowerInclusive() == other.isLowerInclusive() && isUpperInclusive() == other.isUpperInclusive();
    }

    @Generated
    public int hashCode() {
        Object $lowerBound = getLowerBound();
        int result = (1 * 59) + ($lowerBound == null ? 43 : $lowerBound.hashCode());
        Object $upperBound = getUpperBound();
        return (((((result * 59) + ($upperBound == null ? 43 : $upperBound.hashCode())) * 59) + (isLowerInclusive() ? 79 : 97)) * 59) + (isUpperInclusive() ? 79 : 97);
    }

    @Generated
    public String toString() {
        return "Range(lowerBound=" + getLowerBound() + ", upperBound=" + getUpperBound() + ", lowerInclusive=" + isLowerInclusive() + ", upperInclusive=" + isUpperInclusive() + ")";
    }

    @Generated
    public T getLowerBound() {
        return this.lowerBound;
    }

    @Generated
    public T getUpperBound() {
        return this.upperBound;
    }

    @Generated
    public boolean isLowerInclusive() {
        return this.lowerInclusive;
    }

    @Generated
    public boolean isUpperInclusive() {
        return this.upperInclusive;
    }

    public Range(T lowerBound, T upperBound) {
        this(lowerBound, upperBound, true, true);
    }

    public Range(T lowerBound, T upperBound, boolean lowerInclusive, boolean upperInclusive) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
    }

    public boolean contains(T value) {
        boolean z;
        boolean z2;
        Assert.notNull(value, "Reference value must not be null!");
        if (this.lowerBound == null) {
            z = true;
        } else if (this.lowerInclusive) {
            z = this.lowerBound.compareTo(value) <= 0;
        } else {
            z = this.lowerBound.compareTo(value) < 0;
        }
        boolean greaterThanLowerBound = z;
        if (this.upperBound == null) {
            z2 = true;
        } else if (this.upperInclusive) {
            z2 = this.upperBound.compareTo(value) >= 0;
        } else {
            z2 = this.upperBound.compareTo(value) > 0;
        }
        boolean lessThanUpperBound = z2;
        return greaterThanLowerBound && lessThanUpperBound;
    }
}
