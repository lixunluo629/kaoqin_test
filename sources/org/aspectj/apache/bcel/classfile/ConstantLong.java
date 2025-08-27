package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantLong.class */
public final class ConstantLong extends Constant implements SimpleConstant {
    private long longValue;

    public ConstantLong(long j) {
        super((byte) 5);
        this.longValue = j;
    }

    ConstantLong(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readLong());
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantLong(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeLong(this.longValue);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final Long getValue() {
        return Long.valueOf(this.longValue);
    }

    @Override // org.aspectj.apache.bcel.classfile.SimpleConstant
    public final String getStringValue() {
        return Long.toString(this.longValue);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(longValue = " + this.longValue + ")";
    }
}
