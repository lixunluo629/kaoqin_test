package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantClass.class */
public final class ConstantClass extends Constant {
    private int nameIndex;

    ConstantClass(DataInputStream dataInputStream) throws IOException {
        super((byte) 7);
        this.nameIndex = dataInputStream.readUnsignedShort();
    }

    public ConstantClass(int i) {
        super((byte) 7);
        this.nameIndex = i;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantClass(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.nameIndex);
    }

    public final int getNameIndex() {
        return this.nameIndex;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public Integer getValue() {
        return Integer.valueOf(this.nameIndex);
    }

    public String getClassname(ConstantPool constantPool) {
        return constantPool.getConstantUtf8(this.nameIndex).getValue();
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(name_index = " + this.nameIndex + ")";
    }
}
