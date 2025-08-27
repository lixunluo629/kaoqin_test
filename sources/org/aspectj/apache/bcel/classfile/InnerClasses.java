package org.aspectj.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/InnerClasses.class */
public final class InnerClasses extends Attribute {
    private InnerClass[] inner_classes;
    private int number_of_classes;

    public InnerClasses(InnerClasses innerClasses) {
        this(innerClasses.getNameIndex(), innerClasses.getLength(), innerClasses.getInnerClasses(), innerClasses.getConstantPool());
    }

    public InnerClasses(int i, int i2, InnerClass[] innerClassArr, ConstantPool constantPool) {
        super((byte) 6, i, i2, constantPool);
        setInnerClasses(innerClassArr);
    }

    InnerClasses(int i, int i2, DataInputStream dataInputStream, ConstantPool constantPool) throws IOException {
        this(i, i2, (InnerClass[]) null, constantPool);
        this.number_of_classes = dataInputStream.readUnsignedShort();
        this.inner_classes = new InnerClass[this.number_of_classes];
        for (int i3 = 0; i3 < this.number_of_classes; i3++) {
            this.inner_classes[i3] = new InnerClass(dataInputStream);
        }
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute, org.aspectj.apache.bcel.classfile.Node
    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitInnerClasses(this);
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeShort(this.number_of_classes);
        for (int i = 0; i < this.number_of_classes; i++) {
            this.inner_classes[i].dump(dataOutputStream);
        }
    }

    public final InnerClass[] getInnerClasses() {
        return this.inner_classes;
    }

    public final void setInnerClasses(InnerClass[] innerClassArr) {
        this.inner_classes = innerClassArr;
        this.number_of_classes = innerClassArr == null ? 0 : innerClassArr.length;
    }

    @Override // org.aspectj.apache.bcel.classfile.Attribute
    public final String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.number_of_classes; i++) {
            stringBuffer.append(this.inner_classes[i].toString(this.cpool) + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
        }
        return stringBuffer.toString();
    }
}
