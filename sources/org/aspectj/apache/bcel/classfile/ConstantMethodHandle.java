package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/ConstantMethodHandle.class */
public final class ConstantMethodHandle extends Constant {
    private byte referenceKind;
    private int referenceIndex;

    ConstantMethodHandle(DataInputStream dataInputStream) throws IOException {
        this(dataInputStream.readByte(), dataInputStream.readUnsignedShort());
    }

    public ConstantMethodHandle(byte b, int i) {
        super((byte) 15);
        this.referenceKind = b;
        this.referenceIndex = i;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.tag);
        dataOutputStream.writeByte(this.referenceKind);
        dataOutputStream.writeShort(this.referenceIndex);
    }

    public final byte getReferenceKind() {
        return this.referenceKind;
    }

    public final int getReferenceIndex() {
        return this.referenceIndex;
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public final String toString() {
        return super.toString() + "(referenceKind=" + ((int) this.referenceKind) + ",referenceIndex=" + this.referenceIndex + ")";
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant
    public String getValue() {
        return toString();
    }

    @Override // org.aspectj.apache.bcel.classfile.Constant, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitConstantMethodHandle(this);
    }

    public static String kindToString(byte b) {
        switch (b) {
            case 1:
                return "getfield";
            case 2:
                return "getstatic";
            case 3:
                return "putfield";
            case 4:
                return "putstatic";
            case 5:
                return "invokevirtual";
            case 6:
                return "invokestatic";
            case 7:
                return "invokespecial";
            case 8:
                return "newinvokespecial";
            case 9:
                return "invokeinterface";
            default:
                return "nyi";
        }
    }
}
