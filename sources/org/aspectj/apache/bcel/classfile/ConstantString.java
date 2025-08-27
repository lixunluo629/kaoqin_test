package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantString.class */
public final class ConstantString extends Constant {
    private int stringIndex;

    ConstantString(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readUnsignedShort());
    }

    public ConstantString(int i) {
        super((byte) 8);
        this.stringIndex = i;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantString(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.stringIndex);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public Integer getValue() {
        return Integer.valueOf(this.stringIndex);
    }

    public final int getStringIndex() {
        return this.stringIndex;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(string_index = " + this.stringIndex + ")";
    }

    public String getString(ConstantPool constantPool) {
        return ((ConstantUtf8) constantPool.getConstant(this.stringIndex, (byte) 1)).getValue();
    }
}
