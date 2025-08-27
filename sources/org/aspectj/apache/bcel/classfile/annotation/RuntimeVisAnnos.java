package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ClassVisitor;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/RuntimeVisAnnos.class */
public class RuntimeVisAnnos extends RuntimeAnnos {
    public RuntimeVisAnnos(int i, int i2, ConstantPool constantPool) {
        super((byte) 12, true, i, i2, constantPool);
    }

    public RuntimeVisAnnos(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, constantPool);
        readAnnotations(dataInputStream, constantPool);
    }

    public RuntimeVisAnnos(int i, int i2, byte[] bArr, ConstantPool constantPool) {
        super((byte) 12, true, i, i2, bArr, constantPool);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitRuntimeVisibleAnnotations(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        writeAnnotations(dataOutputStream);
    }

    public Attribute copy(ConstantPool constantPool) {
        throw new RuntimeException("Not implemented yet!");
    }
}
