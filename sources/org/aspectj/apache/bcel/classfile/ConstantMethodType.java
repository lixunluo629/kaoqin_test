package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantMethodType.class */
public final class ConstantMethodType extends Constant {
    private int descriptorIndex;

    ConstantMethodType(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readUnsignedShort());
    }

    public ConstantMethodType(int i) {
        super((byte) 16);
        this.descriptorIndex = i;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.descriptorIndex);
    }

    public final int getDescriptorIndex() {
        return this.descriptorIndex;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(descriptorIndex=" + this.descriptorIndex + ")";
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return toString();
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantMethodType(this);
    }
}
