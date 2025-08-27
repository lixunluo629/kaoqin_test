package org.apache.ibatis.javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.bytecode.ConstPool;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/annotation/ByteMemberValue.class */
public class ByteMemberValue extends MemberValue {
    int valueIndex;

    public ByteMemberValue(int index, ConstPool cp) {
        super('B', cp);
        this.valueIndex = index;
    }

    public ByteMemberValue(byte b, ConstPool cp) {
        super('B', cp);
        setValue(b);
    }

    public ByteMemberValue(ConstPool cp) {
        super('B', cp);
        setValue((byte) 0);
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Object getValue(ClassLoader cl, ClassPool cp, Method m) {
        return Byte.valueOf(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Class getType(ClassLoader cl) {
        return Byte.TYPE;
    }

    public byte getValue() {
        return (byte) this.cp.getIntegerInfo(this.valueIndex);
    }

    public void setValue(byte newValue) {
        this.valueIndex = this.cp.addIntegerInfo(newValue);
    }

    public String toString() {
        return Byte.toString(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void write(AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void accept(MemberValueVisitor visitor) {
        visitor.visitByteMemberValue(this);
    }
}
