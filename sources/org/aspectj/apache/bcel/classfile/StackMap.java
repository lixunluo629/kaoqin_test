package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/StackMap.class */
public final class StackMap extends Attribute {
    private int map_length;
    private StackMapEntry[] map;

    public StackMap(int i, int i2, StackMapEntry[] stackMapEntryArr, ConstantPool constantPool) {
        super((byte) 11, i, i2, constantPool);
        setStackMap(stackMapEntryArr);
    }

    StackMap(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (StackMapEntry[]) null, constantPool);
        this.map_length = dataInputStream.readUnsignedShort();
        this.map = new StackMapEntry[this.map_length];
        for (int i3 = 0; i3 < this.map_length; i3++) {
            this.map[i3] = new StackMapEntry(dataInputStream, constantPool);
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.map_length);
        for (int i = 0; i < this.map_length; i++) {
            this.map[i].dump(dataOutputStream);
        }
    }

    public final StackMapEntry[] getStackMap() {
        return this.map;
    }

    public final void setStackMap(StackMapEntry[] stackMapEntryArr) {
        this.map = stackMapEntryArr;
        this.map_length = stackMapEntryArr == null ? 0 : stackMapEntryArr.length;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("StackMap(");
        for (int i = 0; i < this.map_length; i++) {
            stringBuffer.append(this.map[i].toString());
            if (i < this.map_length - 1) {
                stringBuffer.append(", ");
            }
        }
        stringBuffer.append(')');
        return stringBuffer.toString();
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitStackMap(this);
    }

    public final int getMapLength() {
        return this.map_length;
    }
}
