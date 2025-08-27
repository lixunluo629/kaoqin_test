package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantFloat.class */
public final class ConstantFloat extends Constant implements SimpleConstant {
    private float floatValue;

    public ConstantFloat(float f) {
        super((byte) 4);
        this.floatValue = f;
    }

    ConstantFloat(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readFloat());
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantFloat(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeFloat(this.floatValue);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final Float getValue() {
        return Float.valueOf(this.floatValue);
    }

    @Override // org.aspectj.apache.bcel.classfile.SimpleConstant
    public final String getStringValue() {
        return Float.toString(this.floatValue);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(bytes = " + this.floatValue + ")";
    }
}
