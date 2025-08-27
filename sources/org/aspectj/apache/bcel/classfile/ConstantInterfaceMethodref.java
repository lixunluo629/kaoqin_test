package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantInterfaceMethodref.class */
public final class ConstantInterfaceMethodref extends ConstantCP {
    ConstantInterfaceMethodref(DataInputStream dataInputStream) throws IOException {
        super((byte) 11, dataInputStream);
    }

    public ConstantInterfaceMethodref(int i, int i2) {
        super((byte) 11, i, i2);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantInterfaceMethodref(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return toString();
    }
}
