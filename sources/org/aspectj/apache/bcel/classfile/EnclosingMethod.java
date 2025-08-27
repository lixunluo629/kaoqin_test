package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/EnclosingMethod.class */
public class EnclosingMethod extends Attribute {
    private int classIndex;
    private int methodIndex;

    public EnclosingMethod(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort(), constantPool);
    }

    private EnclosingMethod(int i, int i2, int i3, int i4, ConstantPool constantPool) {
        super((byte) 17, i, i2, constantPool);
        this.classIndex = i3;
        this.methodIndex = i4;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitEnclosingMethod(this);
    }

    public Attribute copy(ConstantPool constantPool) {
        throw new RuntimeException("Not implemented yet!");
    }

    public final int getEnclosingClassIndex() {
        return this.classIndex;
    }

    public final int getEnclosingMethodIndex() {
        return this.methodIndex;
    }

    public final void setEnclosingClassIndex(int i) {
        this.classIndex = i;
    }

    public final void setEnclosingMethodIndex(int i) {
        this.methodIndex = i;
    }

    public final ConstantClass getEnclosingClass() {
        return (ConstantClass) this.cpool.getConstant(this.classIndex, (byte) 7);
    }

    public final ConstantNameAndType getEnclosingMethod() {
        if (this.methodIndex == 0) {
            return null;
        }
        return (ConstantNameAndType) this.cpool.getConstant(this.methodIndex, (byte) 12);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.classIndex);
        dataOutputStream.writeShort(this.methodIndex);
    }
}
