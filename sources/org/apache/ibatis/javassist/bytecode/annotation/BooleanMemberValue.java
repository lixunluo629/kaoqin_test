package org.apache.ibatis.javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.bytecode.ConstPool;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/annotation/BooleanMemberValue.class */
public class BooleanMemberValue extends MemberValue {
    int valueIndex;

    public BooleanMemberValue(int index, ConstPool cp) {
        super('Z', cp);
        this.valueIndex = index;
    }

    public BooleanMemberValue(boolean b, ConstPool cp) {
        super('Z', cp);
        setValue(b);
    }

    public BooleanMemberValue(ConstPool cp) {
        super('Z', cp);
        setValue(false);
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Object getValue(ClassLoader cl, ClassPool cp, Method m) {
        return Boolean.valueOf(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Class getType(ClassLoader cl) {
        return Boolean.TYPE;
    }

    public boolean getValue() {
        return this.cp.getIntegerInfo(this.valueIndex) != 0;
    }

    public void setValue(boolean newValue) {
        this.valueIndex = this.cp.addIntegerInfo(newValue ? 1 : 0);
    }

    public String toString() {
        return getValue() ? "true" : "false";
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void write(AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void accept(MemberValueVisitor visitor) {
        visitor.visitBooleanMemberValue(this);
    }
}
