package org.apache.commons.lang.math;

import java.io.Serializable;
import org.apache.commons.lang.text.StrBuilder;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/math/NumberRange.class */
public final class NumberRange extends Range implements Serializable {
    private static final long serialVersionUID = 71849363892710L;
    private final Number min;
    private final Number max;
    private transient int hashCode = 0;
    private transient String toString = null;

    public NumberRange(Number num) {
        if (num == null) {
            throw new IllegalArgumentException("The number must not be null");
        }
        if (!(num instanceof Comparable)) {
            throw new IllegalArgumentException("The number must implement Comparable");
        }
        if ((num instanceof Double) && ((Double) num).isNaN()) {
            throw new IllegalArgumentException("The number must not be NaN");
        }
        if ((num instanceof Float) && ((Float) num).isNaN()) {
            throw new IllegalArgumentException("The number must not be NaN");
        }
        this.min = num;
        this.max = num;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public NumberRange(Number number, Number num2) {
        if (number == 0 || num2 == null) {
            throw new IllegalArgumentException("The numbers must not be null");
        }
        if (number.getClass() != num2.getClass()) {
            throw new IllegalArgumentException("The numbers must be of the same type");
        }
        if (!(number instanceof Comparable)) {
            throw new IllegalArgumentException("The numbers must implement Comparable");
        }
        if (number instanceof Double) {
            if (((Double) number).isNaN() || ((Double) num2).isNaN()) {
                throw new IllegalArgumentException("The number must not be NaN");
            }
        } else if ((number instanceof Float) && (((Float) number).isNaN() || ((Float) num2).isNaN())) {
            throw new IllegalArgumentException("The number must not be NaN");
        }
        int compare = ((Comparable) number).compareTo(num2);
        if (compare == 0) {
            this.min = number;
            this.max = number;
        } else if (compare > 0) {
            this.min = num2;
            this.max = number;
        } else {
            this.min = number;
            this.max = num2;
        }
    }

    @Override // org.apache.commons.lang.math.Range
    public Number getMinimumNumber() {
        return this.min;
    }

    @Override // org.apache.commons.lang.math.Range
    public Number getMaximumNumber() {
        return this.max;
    }

    @Override // org.apache.commons.lang.math.Range
    public boolean containsNumber(Number number) {
        if (number == null) {
            return false;
        }
        if (number.getClass() != this.min.getClass()) {
            throw new IllegalArgumentException("The number must be of the same type as the range numbers");
        }
        int compareMin = ((Comparable) this.min).compareTo(number);
        int compareMax = ((Comparable) this.max).compareTo(number);
        return compareMin <= 0 && compareMax >= 0;
    }

    @Override // org.apache.commons.lang.math.Range
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

    @Override // org.apache.commons.lang.math.Range
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 17;
            this.hashCode = (37 * this.hashCode) + getClass().hashCode();
            this.hashCode = (37 * this.hashCode) + this.min.hashCode();
            this.hashCode = (37 * this.hashCode) + this.max.hashCode();
        }
        return this.hashCode;
    }

    @Override // org.apache.commons.lang.math.Range
    public String toString() {
        if (this.toString == null) {
            StrBuilder buf = new StrBuilder(32);
            buf.append("Range[");
            buf.append(this.min);
            buf.append(',');
            buf.append(this.max);
            buf.append(']');
            this.toString = buf.toString();
        }
        return this.toString;
    }
}
