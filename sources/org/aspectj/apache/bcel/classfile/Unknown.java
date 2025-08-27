package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/Unknown.class */
public final class Unknown extends Attribute {
    private byte[] bytes;
    private String name;

    public Unknown(Unknown unknown) {
        this(unknown.getNameIndex(), unknown.getLength(), unknown.getBytes(), unknown.getConstantPool());
    }

    public Unknown(int i, int i2, byte[] bArr, ConstantPool constantPool) {
        super((byte) -1, i, i2, constantPool);
        this.bytes = bArr;
        this.name = ((ConstantUtf8) constantPool.getConstant(i, (byte) 1)).getValue();
    }

    Unknown(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (byte[]) null, constantPool);
        if (i2 > 0) {
            this.bytes = new byte[i2];
            dataInputStream.readFully(this.bytes);
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitUnknown(this);
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

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public String getName() {
        return this.name;
    }

    public final void setBytes(byte[] bArr) {
        this.bytes = bArr;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        String hexString;
        if (this.length == 0 || this.bytes == null) {
            return "(Unknown attribute " + this.name + ")";
        }
        if (this.length > 10) {
            byte[] bArr = new byte[10];
            System.arraycopy(this.bytes, 0, bArr, 0, 10);
            hexString = Utility.toHexString(bArr) + "... (truncated)";
        } else {
            hexString = Utility.toHexString(this.bytes);
        }
        return "(Unknown attribute " + this.name + ": " + hexString + ")";
    }
}
