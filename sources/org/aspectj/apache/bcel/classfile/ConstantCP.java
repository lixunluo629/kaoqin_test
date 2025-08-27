package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantCP.class */
public abstract class ConstantCP extends Constant {
    protected int classIndex;
    protected int nameAndTypeIndex;

    ConstantCP(byte b, DataInputStream dataInputStream) throws IOException {
        this(b, dataInputStream.readUnsignedShort(), dataInputStream.readUnsignedShort());
    }

    protected ConstantCP(byte b, int i, int i2) {
        super(b);
        this.classIndex = i;
        this.nameAndTypeIndex = i2;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeShort(this.classIndex);
        dataOutputStream.writeShort(this.nameAndTypeIndex);
    }

    public final int getClassIndex() {
        return this.classIndex;
    }

    public final int getNameAndTypeIndex() {
        return this.nameAndTypeIndex;
    }

    public String getClass(ConstantPool constantPool) {
        return constantPool.constantToString(this.classIndex, (byte) 7);
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(classIndex = " + this.classIndex + ", nameAndTypeIndex = " + this.nameAndTypeIndex + ")";
    }
}
