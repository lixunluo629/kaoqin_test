package org.aspectj.apache.bcel.classfile.annotation;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/RuntimeTypeAnnos.class */
public abstract class RuntimeTypeAnnos extends Attribute {
    private boolean visible;
    private TypeAnnotationGen[] typeAnnotations;
    private byte[] annotation_data;

    public RuntimeTypeAnnos(byte b, boolean z, int i, int i2, ConstantPool constantPool) {
        super(b, i, i2, constantPool);
        this.visible = z;
    }

    protected void readTypeAnnotations(DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this.annotation_data = new byte[this.length];
        dataInputStream.readFully(this.annotation_data, 0, this.length);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        writeTypeAnnotations(dataOutputStream);
    }

    protected void writeTypeAnnotations(DataOutputStream dataOutputStream) throws IOException {
        if (this.typeAnnotations == null) {
            dataOutputStream.write(this.annotation_data, 0, this.length);
            return;
        }
        dataOutputStream.writeShort(this.typeAnnotations.length);
        for (int i = 0; i < this.typeAnnotations.length; i++) {
            this.typeAnnotations[i].dump(dataOutputStream);
        }
    }

    public Attribute copy(ConstantPool constantPool) {
        throw new RuntimeException("Not implemented yet!");
    }

    public TypeAnnotationGen[] getTypeAnnotations() throws IOException {
        ensureInflated();
        return this.typeAnnotations;
    }

    public boolean areVisible() {
        return this.visible;
    }

    private void ensureInflated() throws IOException {
        if (this.typeAnnotations != null) {
            return;
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(this.annotation_data));
            int unsignedShort = dataInputStream.readUnsignedShort();
            if (unsignedShort == 0) {
                this.typeAnnotations = TypeAnnotationGen.NO_TYPE_ANNOTATIONS;
            } else {
                this.typeAnnotations = new TypeAnnotationGen[unsignedShort];
                for (int i = 0; i < unsignedShort; i++) {
                    this.typeAnnotations[i] = TypeAnnotationGen.read(dataInputStream, getConstantPool(), this.visible);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unabled to inflate type annotation data, badly formed?");
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public String toString() {
        return "Runtime" + (this.visible ? "Visible" : "Invisible") + "TypeAnnotations [" + (isInflated() ? "inflated" : "not yet inflated") + "]";
    }

    public boolean isInflated() {
        return this.typeAnnotations != null;
    }
}
