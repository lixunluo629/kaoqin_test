package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataInputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.Attribute;
import org.aspectj.apache.bcel.classfile.ClassVisitor;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/RuntimeInvisParamAnnos.class */
public class RuntimeInvisParamAnnos extends RuntimeParamAnnos {
    public RuntimeInvisParamAnnos(int i, int i2, ConstantPool constantPool) {
        super((byte) 15, false, i, i2, constantPool);
    }

    public RuntimeInvisParamAnnos(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, constantPool);
        readParameterAnnotations(dataInputStream, constantPool);
    }

    public RuntimeInvisParamAnnos(int i, int i2, byte[] bArr, ConstantPool constantPool) {
        super((byte) 15, false, i, i2, bArr, constantPool);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitRuntimeInvisibleParameterAnnotations(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.RuntimeParamAnnos
    public Attribute copy(ConstantPool constantPool) {
        throw new RuntimeException("Not implemented yet!");
    }
}
