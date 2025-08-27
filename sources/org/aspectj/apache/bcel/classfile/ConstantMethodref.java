package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantMethodref.class */
public final class ConstantMethodref extends ConstantCP {
    ConstantMethodref(DataInputStream dataInputStream) throws IOException {
        super((byte) 10, dataInputStream);
    }

    public ConstantMethodref(int i, int i2) {
        super((byte) 10, i, i2);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantMethodref(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return toString();
    }
}
