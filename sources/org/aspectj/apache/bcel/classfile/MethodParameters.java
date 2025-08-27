package org.aspectj.apache.bcel.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/MethodParameters.class */
public class MethodParameters extends Attribute {
    public static final int[] NO_PARAMETER_NAME_INDEXES = new int[0];
    public static final int[] NO_PARAMETER_ACCESS_FLAGS = new int[0];
    public static final int ACCESS_FLAGS_FINAL = 16;
    public static final int ACCESS_FLAGS_SYNTHETIC = 4096;
    public static final int ACCESS_FLAGS_MANDATED = 32768;
    private boolean isInPackedState;
    private byte[] data;
    private int[] names;
    private int[] accessFlags;

    public MethodParameters(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        super((byte) 22, i, i2, constantPool);
        this.isInPackedState = false;
        this.data = new byte[i2];
        dataInputStream.readFully(this.data, 0, i2);
        this.isInPackedState = true;
    }

    private void ensureInflated() throws IOException {
        if (this.names != null) {
            return;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.data));
            int unsignedByte = dataInputStream.readUnsignedByte();
            if (unsignedByte == 0) {
                this.names = NO_PARAMETER_NAME_INDEXES;
                this.accessFlags = NO_PARAMETER_ACCESS_FLAGS;
            } else {
                this.names = new int[unsignedByte];
                this.accessFlags = new int[unsignedByte];
                for (int i = 0; i < unsignedByte; i++) {
                    this.names[i] = dataInputStream.readUnsignedShort();
                    this.accessFlags[i] = dataInputStream.readUnsignedShort();
                }
            }
            this.isInPackedState = false;
        } catch (IOException e) {
            throw new RuntimeException("Unabled to inflate type annotation data, badly formed?");
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        if (this.isInPackedState) {
            dataOutputStream.write(this.data);
            return;
        }
        dataOutputStream.writeByte(this.names.length);
        for (int i = 0; i < this.names.length; i++) {
            dataOutputStream.writeShort(this.names[i]);
            dataOutputStream.writeShort(this.accessFlags[i]);
        }
    }

    public int getParametersCount() throws IOException {
        ensureInflated();
        return this.names.length;
    }

    public String getParameterName(int i) throws IOException {
        ensureInflated();
        return ((ConstantUtf8) this.cpool.getConstant(this.names[i], (byte) 1)).getValue();
    }

    public int getAccessFlags(int i) throws IOException {
        ensureInflated();
        return this.accessFlags[i];
    }

    public boolean isFinal(int i) {
        return (getAccessFlags(i) & 16) != 0;
    }

    public boolean isSynthetic(int i) {
        return (getAccessFlags(i) & 4096) != 0;
    }

    public boolean isMandated(int i) {
        return (getAccessFlags(i) & 32768) != 0;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitMethodParameters(this);
    }
}
