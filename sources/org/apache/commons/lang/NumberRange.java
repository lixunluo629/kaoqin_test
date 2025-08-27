package org.apache.commons.lang;

import org.apache.commons.lang.text.StrBuilder;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/NumberRange.class */
public final class NumberRange {
    private final Number min;
    private final Number max;

    public NumberRange(Number num) {
        if (num == null) {
            throw new NullPointerException("The number must not be null");
        }
        this.min = num;
        this.max = num;
    }

    public NumberRange(Number min, Number max) {
        if (min == null) {
            throw new NullPointerException("The minimum value must not be null");
        }
        if (max == null) {
            throw new NullPointerException("The maximum value must not be null");
        }
        if (max.doubleValue() < min.doubleValue()) {
            this.max = min;
            this.min = min;
        } else {
            this.min = min;
            this.max = max;
        }
    }

    public Number getMinimum() {
        return this.min;
    }

    public Number getMaximum() {
        return this.max;
    }

    public boolean includesNumber(Number number) {
        return number != null && this.min.doubleValue() <= number.doubleValue() && this.max.doubleValue() >= number.doubleValue();
    }

    public boolean includesRange(NumberRange range) {
        return range != null && includesNumber(range.min) && includesNumber(range.max);
    }

    public boolean overlaps(NumberRange range) {
        if (range == null) {
            return false;
        }
        return range.includesNumber(this.min) || range.includesNumber(this.max) || includesRange(range);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof NumberRange)) {
            return false;
        }
        NumberRange range = (NumberRange) obj;
        return this.min.equals(range.min) && this.max.equals(range.max);
    }

    public int hashCode() {
        int result = (37 * 17) + this.min.hashCode();
        return (37 * result) + this.max.hashCode();
    }

    public String toString() {
        StrBuilder sb = new StrBuilder();
        if (this.min.doubleValue() < 0.0d) {
            sb.append('(').append(this.min).append(')');
        } else {
            sb.append(this.min);
        }
        sb.append('-');
        if (this.max.doubleValue() < 0.0d) {
            sb.append('(').append(this.max).append(')');
        } else {
            sb.append(this.max);
        }
        return sb.toString();
    }
}
