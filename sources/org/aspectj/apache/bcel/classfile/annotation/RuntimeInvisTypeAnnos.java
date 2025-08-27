package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataInputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ClassVisitor;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/RuntimeInvisTypeAnnos.class */
public class RuntimeInvisTypeAnnos extends RuntimeTypeAnnos {
    public RuntimeInvisTypeAnnos(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, constantPool);
        readTypeAnnotations(dataInputStream, constantPool);
    }

    public RuntimeInvisTypeAnnos(int i, int i2, ConstantPool constantPool) {
        super((byte) 21, false, i, i2, constantPool);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitRuntimeInvisibleTypeAnnotations(this);
    }
}
