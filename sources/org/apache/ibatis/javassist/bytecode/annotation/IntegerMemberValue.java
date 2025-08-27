package org.apache.ibatis.javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.bytecode.ConstPool;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/annotation/IntegerMemberValue.class */
public class IntegerMemberValue extends MemberValue {
    int valueIndex;

    public IntegerMemberValue(int index, ConstPool cp) {
        super('I', cp);
        this.valueIndex = index;
    }

    public IntegerMemberValue(ConstPool cp, int value) {
        super('I', cp);
        setValue(value);
    }

    public IntegerMemberValue(ConstPool cp) {
        super('I', cp);
        setValue(0);
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Object getValue(ClassLoader cl, ClassPool cp, Method m) {
        return Integer.valueOf(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Class getType(ClassLoader cl) {
        return Integer.TYPE;
    }

    public int getValue() {
        return this.cp.getIntegerInfo(this.valueIndex);
    }

    public void setValue(int newValue) {
        this.valueIndex = this.cp.addIntegerInfo(newValue);
    }

    public String toString() {
        return Integer.toString(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void write(AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void accept(MemberValueVisitor visitor) {
        visitor.visitIntegerMemberValue(this);
    }
}
