package org.apache.commons.lang.mutable;

import org.apache.commons.lang.math.NumberUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/mutable/MutableFloat.class */
public class MutableFloat extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = 5787169186L;
    private float value;

    public MutableFloat() {
    }

    public MutableFloat(float value) {
        this.value = value;
    }

    public MutableFloat(Number value) {
        this.value = value.floatValue();
    }

    public MutableFloat(String value) throws NumberFormatException {
        this.value = Float.parseFloat(value);
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public Object getValue() {
        return new Float(this.value);
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public void setValue(Object value) {
        setValue(((Number) value).floatValue());
    }

    public boolean isNaN() {
        return Float.isNaN(this.value);
    }

    public boolean isInfinite() {
        return Float.isInfinite(this.value);
    }

    public void increment() {
        this.value += 1.0f;
    }

    public void decrement() {
        this.value -= 1.0f;
    }

    public void add(float operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.floatValue();
    }

    public void subtract(float operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.floatValue();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) this.value;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public Float toFloat() {
        return new Float(floatValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableFloat) && Float.floatToIntBits(((MutableFloat) obj).value) == Float.floatToIntBits(this.value);
    }

    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        MutableFloat other = (MutableFloat) obj;
        float anotherVal = other.value;
        return NumberUtils.compare(this.value, anotherVal);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
