package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.weaver.ConstantPoolWriter;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelConstantPoolWriter.class */
class BcelConstantPoolWriter implements ConstantPoolWriter {
    ConstantPool pool;

    public BcelConstantPoolWriter(ConstantPool pool) {
        this.pool = pool;
    }

    @Override // org.aspectj.weaver.ConstantPoolWriter
    public int writeUtf8(String name) {
        return this.pool.addUtf8(name);
    }
}
