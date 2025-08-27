package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantDouble.class */
public final class ConstantDouble extends Constant implements SimpleConstant {
    private double value;

    public ConstantDouble(double d) {
        super((byte) 6);
        this.value = d;
    }

    ConstantDouble(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readDouble());
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantDouble(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeDouble(this.value);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(bytes = " + this.value + ")";
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public Double getValue() {
        return Double.valueOf(this.value);
    }

    @Override // org.aspectj.apache.bcel.classfile.SimpleConstant
    public String getStringValue() {
        return Double.toString(this.value);
    }
}
