package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantFieldref.class */
public final class ConstantFieldref extends ConstantCP {
    ConstantFieldref(DataInputStream dataInputStream) throws IOException {
        super((byte) 9, dataInputStream);
    }

    public ConstantFieldref(int i, int i2) {
        super((byte) 9, i, i2);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantFieldref(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return toString();
    }
}
