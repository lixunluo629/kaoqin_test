package org.apache.commons.lang.mutable;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/mutable/MutableByte.class */
public class MutableByte extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = -1585823265;
    private byte value;

    public MutableByte() {
    }

    public MutableByte(byte value) {
        this.value = value;
    }

    public MutableByte(Number value) {
        this.value = value.byteValue();
    }

    public MutableByte(String value) throws NumberFormatException {
        this.value = Byte.parseByte(value);
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public Object getValue() {
        return new Byte(this.value);
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override // org.apache.commons.lang.mutable.Mutable
    public void setValue(Object value) {
        setValue(((Number) value).byteValue());
    }

    public void increment() {
        this.value = (byte) (this.value + 1);
    }

    public void decrement() {
        this.value = (byte) (this.value - 1);
    }

    public void add(byte operand) {
        this.value = (byte) (this.value + operand);
    }

    public void add(Number operand) {
        this.value = (byte) (this.value + operand.byteValue());
    }

    public void subtract(byte operand) {
        this.value = (byte) (this.value - operand);
    }

    public void subtract(Number operand) {
        this.value = (byte) (this.value - operand.byteValue());
    }

    @Override // java.lang.Number
    public byte byteValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.value;
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.value;
    }

    public Byte toByte() {
        return new Byte(byteValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableByte) && this.value == ((MutableByte) obj).byteValue();
    }

    public int hashCode() {
        return this.value;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        MutableByte other = (MutableByte) obj;
        byte anotherVal = other.value;
        if (this.value < anotherVal) {
            return -1;
        }
        return this.value == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf((int) this.value);
    }
}
