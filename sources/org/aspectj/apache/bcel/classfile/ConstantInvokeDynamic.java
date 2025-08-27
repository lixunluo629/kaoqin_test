package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantInvokeDynamic.class */
public final class ConstantInvokeDynamic extends Constant {
    private final int bootstrapMethodAttrIndex;
    private final int nameAndTypeIndex;

    ConstantInvokeDynamic(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort());
    }

    public ConstantInvokeDynamic(int i, int i2) {
        super((byte) 18);
        this.bootstrapMethodAttrIndex = i;
        this.nameAndTypeIndex = i2;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.bootstrapMethodAttrIndex);
        dataOutputStream.writeShort(this.nameAndTypeIndex);
    }

    public final int getNameAndTypeIndex() {
        return this.nameAndTypeIndex;
    }

    public final int getBootstrapMethodAttrIndex() {
        return this.bootstrapMethodAttrIndex;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(bootstrapMethodAttrIndex=" + this.bootstrapMethodAttrIndex + ",nameAndTypeIndex=" + this.nameAndTypeIndex + ")";
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return toString();
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantInvokeDynamic(this);
    }
}
