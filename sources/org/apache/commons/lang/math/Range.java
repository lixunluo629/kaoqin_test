package org.apache.commons.lang.math;

import org.apache.commons.lang.text.StrBuilder;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/math/Range.class */
public abstract class Range {
    public abstract Number getMinimumNumber();

    public abstract Number getMaximumNumber();

    public abstract boolean containsNumber(Number number);

    public long getMinimumLong() {
        return getMinimumNumber().longValue();
    }

    public int getMinimumInteger() {
        return getMinimumNumber().intValue();
    }

    public double getMinimumDouble() {
        return getMinimumNumber().doubleValue();
    }

    public float getMinimumFloat() {
        return getMinimumNumber().floatValue();
    }

    public long getMaximumLong() {
        return getMaximumNumber().longValue();
    }

    public int getMaximumInteger() {
        return getMaximumNumber().intValue();
    }

    public double getMaximumDouble() {
        return getMaximumNumber().doubleValue();
    }

    public float getMaximumFloat() {
        return getMaximumNumber().floatValue();
    }

    public boolean containsLong(Number value) {
        if (value == null) {
            return false;
        }
        return containsLong(value.longValue());
    }

    public boolean containsLong(long value) {
        return value >= getMinimumLong() && value <= getMaximumLong();
    }

    public boolean containsInteger(Number value) {
        if (value == null) {
            return false;
        }
        return containsInteger(value.intValue());
    }

    public boolean containsInteger(int value) {
        return value >= getMinimumInteger() && value <= getMaximumInteger();
    }

    public boolean containsDouble(Number value) {
        if (value == null) {
            return false;
        }
        return containsDouble(value.doubleValue());
    }

    public boolean containsDouble(double value) {
        int compareMin = NumberUtils.compare(getMinimumDouble(), value);
        int compareMax = NumberUtils.compare(getMaximumDouble(), value);
        return compareMin <= 0 && compareMax >= 0;
    }

    public boolean containsFloat(Number value) {
        if (value == null) {
            return false;
        }
        return containsFloat(value.floatValue());
    }

    public boolean containsFloat(float value) {
        int compareMin = NumberUtils.compare(getMinimumFloat(), value);
        int compareMax = NumberUtils.compare(getMaximumFloat(), value);
        return compareMin <= 0 && compareMax >= 0;
    }

    public boolean containsRange(Range range) {
        return range != null && containsNumber(range.getMinimumNumber()) && containsNumber(range.getMaximumNumber());
    }

    public boolean overlapsRange(Range range) {
        if (range == null) {
            return false;
        }
        return range.containsNumber(getMinimumNumber()) || range.containsNumber(getMaximumNumber()) || containsNumber(range.getMinimumNumber());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Range range = (Range) obj;
        return getMinimumNumber().equals(range.getMinimumNumber()) && getMaximumNumber().equals(range.getMaximumNumber());
    }

    public int hashCode() {
        int result = (37 * 17) + getClass().hashCode();
        return (37 * ((37 * result) + getMinimumNumber().hashCode())) + getMaximumNumber().hashCode();
    }

    public String toString() {
        StrBuilder buf = new StrBuilder(32);
        buf.append("Range[");
        buf.append(getMinimumNumber());
        buf.append(',');
        buf.append(getMaximumNumber());
        buf.append(']');
        return buf.toString();
    }
}
