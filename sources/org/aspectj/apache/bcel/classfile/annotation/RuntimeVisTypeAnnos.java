package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataInputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ClassVisitor;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/RuntimeVisTypeAnnos.class */
public class RuntimeVisTypeAnnos extends RuntimeTypeAnnos {
    public RuntimeVisTypeAnnos(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, constantPool);
        readTypeAnnotations(dataInputStream, constantPool);
    }

    public RuntimeVisTypeAnnos(int i, int i2, ConstantPool constantPool) {
        super((byte) 20, true, i, i2, constantPool);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitRuntimeVisibleTypeAnnotations(this);
    }
}
