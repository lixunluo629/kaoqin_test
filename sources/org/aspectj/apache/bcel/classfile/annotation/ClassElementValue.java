package org.aspectj.apache.bcel.classfile.annotation;

import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;
import org.aspectj.apache.bcel.generic.ObjectType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/classfile/annotation/ClassElementValue.class */
public class ClassElementValue extends ElementValue {
    private int idx;

    protected ClassElementValue(int i, ConstantPool constantPool) {
        super(99, constantPool);
        this.idx = i;
    }

    public ClassElementValue(ObjectType objectType, ConstantPool constantPool) {
        super(99, constantPool);
        this.idx = constantPool.addUtf8(objectType.getSignature());
    }

    public ClassElementValue(ClassElementValue classElementValue, ConstantPool constantPool, boolean z) {
        super(99, constantPool);
        if (z) {
            this.idx = constantPool.addUtf8(classElementValue.getClassString());
        } else {
            this.idx = classElementValue.getIndex();
        }
    }

    public int getIndex() {
        return this.idx;
    }

    public String getClassString() {
        return ((ConstantUtf8) getConstantPool().getConstant(this.idx)).getValue();
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public String stringifyValue() {
        return getClassString();
    }

    @Override // org.aspectj.apache.bcel.classfile.annotation.ElementValue
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.type);
        dataOutputStream.writeShort(this.idx);
    }
}
