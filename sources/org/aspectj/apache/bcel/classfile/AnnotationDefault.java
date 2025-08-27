package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.annotation.ElementValue;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/AnnotationDefault.class */
public class AnnotationDefault extends Attribute {
    private ElementValue value;

    public AnnotationDefault(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, ElementValue.readElementValue(dataInputStream, constantPool), constantPool);
    }

    private AnnotationDefault(int i, int i2, ElementValue elementValue, ConstantPool constantPool) {
        super((byte) 18, i, i2, constantPool);
        this.value = elementValue;
    }

    public Attribute copy(ConstantPool constantPool) {
        throw new RuntimeException("Not implemented yet!");
    }

    public final ElementValue getElementValue() {
        return this.value;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        this.value.dump(dataOutputStream);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitAnnotationDefault(this);
    }
}
