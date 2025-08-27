package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantNameAndType.class */
public final class ConstantNameAndType extends Constant {
    private int name_index;
    private int signature_index;

    ConstantNameAndType(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort());
    }

    public ConstantNameAndType(int i, int i2) {
        super((byte) 12);
        this.name_index = i;
        this.signature_index = i2;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantNameAndType(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.name_index);
        dataOutputStream.writeShort(this.signature_index);
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final String getName(ConstantPool constantPool) {
        return constantPool.constantToString(getNameIndex(), (byte) 1);
    }

    public final int getSignatureIndex() {
        return this.signature_index;
    }

    public final String getSignature(ConstantPool constantPool) {
        return constantPool.constantToString(getSignatureIndex(), (byte) 1);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(name_index = " + this.name_index + ", signature_index = " + this.signature_index + ")";
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return toString();
    }
}
