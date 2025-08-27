package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ArrayType.class */
public final class ArrayType extends ReferenceType {
    private int dimensions;
    private Type basic_type;

    public ArrayType(byte b, int i) {
        this(BasicType.getType(b), i);
    }

    public ArrayType(String str, int i) {
        this(new ObjectType(str), i);
    }

    public ArrayType(Type type, int i) {
        super((byte) 13, "<dummy>");
        if (i < 1 || i > 255) {
            throw new ClassGenException("Invalid number of dimensions: " + i);
        }
        switch (type.getType()) {
            case 12:
                throw new ClassGenException("Invalid type: void[]");
            case 13:
                ArrayType arrayType = (ArrayType) type;
                this.dimensions = i + arrayType.dimensions;
                this.basic_type = arrayType.basic_type;
                break;
            default:
                this.dimensions = i;
                this.basic_type = type;
                break;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < this.dimensions; i2++) {
            stringBuffer.append('[');
        }
        stringBuffer.append(this.basic_type.getSignature());
        this.signature = stringBuffer.toString();
    }

    public Type getBasicType() {
        return this.basic_type;
    }

    public Type getElementType() {
        return this.dimensions == 1 ? this.basic_type : new ArrayType(this.basic_type, this.dimensions - 1);
    }

    public int getDimensions() {
        return this.dimensions;
    }

    public int hashCode() {
        return this.basic_type.hashCode() ^ this.dimensions;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ArrayType)) {
            return false;
        }
        ArrayType arrayType = (ArrayType) obj;
        return arrayType.dimensions == this.dimensions && arrayType.basic_type.equals(this.basic_type);
    }
}
