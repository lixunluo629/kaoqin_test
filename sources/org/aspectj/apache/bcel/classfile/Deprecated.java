package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.Constants;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Deprecated.class */
public final class Deprecated extends Attribute {
    private byte[] bytes;

    public Deprecated(Deprecated deprecated) {
        this(deprecated.getNameIndex(), deprecated.getLength(), deprecated.getBytes(), deprecated.getConstantPool());
    }

    public Deprecated(int i, int i2, byte[] bArr, ConstantPool constantPool) {
        super((byte) 8, i, i2, constantPool);
        this.bytes = bArr;
    }

    Deprecated(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (byte[]) null, constantPool);
        if (i2 > 0) {
            this.bytes = new byte[i2];
            dataInputStream.readFully(this.bytes);
            System.err.println("Deprecated attribute with length > 0");
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitDeprecated(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        if (this.length > 0) {
            dataOutputStream.write(this.bytes, 0, this.length);
        }
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public final void setBytes(byte[] bArr) {
        this.bytes = bArr;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        return Constants.ATTRIBUTE_NAMES[8];
    }
}
