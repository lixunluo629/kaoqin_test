package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.weaver.ConstantPoolReader;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelConstantPoolReader.class */
public class BcelConstantPoolReader implements ConstantPoolReader {
    private ConstantPool constantPool;

    public BcelConstantPoolReader(ConstantPool constantPool) {
        this.constantPool = constantPool;
    }

    @Override // org.aspectj.weaver.ConstantPoolReader
    public String readUtf8(int cpIndex) {
        return this.constantPool.getConstantUtf8(cpIndex).getValue();
    }
}
