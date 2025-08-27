package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantInteger.class */
public final class ConstantInteger extends Constant implements SimpleConstant {
    private int intValue;

    public ConstantInteger(int i) {
        super((byte) 3);
        this.intValue = i;
    }

    ConstantInteger(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readInt());
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantInteger(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeInt(this.intValue);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(bytes = " + this.intValue + ")";
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public Integer getValue() {
        return Integer.valueOf(this.intValue);
    }

    public int getIntValue() {
        return this.intValue;
    }

    @Override // org.aspectj.apache.bcel.classfile.SimpleConstant
    public String getStringValue() {
        return Integer.toString(this.intValue);
    }
}
