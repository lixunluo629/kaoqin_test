package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ExceptionTable.class */
public final class ExceptionTable extends Attribute {
    private int number_of_exceptions;
    private int[] exception_index_table;

    public ExceptionTable(ExceptionTable exceptionTable) {
        this(exceptionTable.getNameIndex(), exceptionTable.getLength(), exceptionTable.getExceptionIndexTable(), exceptionTable.getConstantPool());
    }

    public ExceptionTable(int i, int i2, int[] iArr, ConstantPool constantPool) {
        super((byte) 3, i, i2, constantPool);
        setExceptionIndexTable(iArr);
    }

    ExceptionTable(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (int[]) null, constantPool);
        this.number_of_exceptions = dataInputStream.readUnsignedShort();
        this.exception_index_table = new int[this.number_of_exceptions];
        for (int i3 = 0; i3 < this.number_of_exceptions; i3++) {
            this.exception_index_table[i3] = dataInputStream.readUnsignedShort();
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitExceptionTable(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.number_of_exceptions);
        for (int i = 0; i < this.number_of_exceptions; i++) {
            dataOutputStream.writeShort(this.exception_index_table[i]);
        }
    }

    public final int[] getExceptionIndexTable() {
        return this.exception_index_table;
    }

    public final int getNumberOfExceptions() {
        return this.number_of_exceptions;
    }

    public final String[] getExceptionNames() {
        String[] strArr = new String[this.number_of_exceptions];
        for (int i = 0; i < this.number_of_exceptions; i++) {
            strArr[i] = this.cpool.getConstantString(this.exception_index_table[i], (byte) 7).replace('/', '.');
        }
        return strArr;
    }

    public final void setExceptionIndexTable(int[] iArr) {
        this.exception_index_table = iArr;
        this.number_of_exceptions = iArr == null ? 0 : iArr.length;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < this.number_of_exceptions; i++) {
            stringBuffer.append(Utility.compactClassName(this.cpool.getConstantString(this.exception_index_table[i], (byte) 7), false));
            if (i < this.number_of_exceptions - 1) {
                stringBuffer.append(", ");
            }
        }
        return stringBuffer.toString();
    }
}
