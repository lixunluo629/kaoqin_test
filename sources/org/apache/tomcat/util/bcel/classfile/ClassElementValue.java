package org.apache.tomcat.util.bcel.classfile;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/tomcat/util/bcel/classfile/ClassElementValue.class */
public class ClassElementValue extends ElementValue {
    private final int idx;

    ClassElementValue(int type, int idx, ConstantPool cpool) {
        super(type, cpool);
        this.idx = idx;
    }

    @Override // org.apache.tomcat.util.bcel.classfile.ElementValue
    public String stringifyValue() {
        ConstantUtf8 cu8 = (ConstantUtf8) super.getConstantPool().getConstant(this.idx, (byte) 1);
        return cu8.getBytes();
    }
}
