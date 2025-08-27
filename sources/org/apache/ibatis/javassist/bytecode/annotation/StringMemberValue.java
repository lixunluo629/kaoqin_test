package org.apache.ibatis.javassist.bytecode.annotation;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.ibatis.javassist.ClassPool;
import org.apache.ibatis.javassist.bytecode.ConstPool;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/bytecode/annotation/StringMemberValue.class */
public class StringMemberValue extends MemberValue {
    int valueIndex;

    public StringMemberValue(int index, ConstPool cp) {
        super('s', cp);
        this.valueIndex = index;
    }

    public StringMemberValue(String str, ConstPool cp) {
        super('s', cp);
        setValue(str);
    }

    public StringMemberValue(ConstPool cp) {
        super('s', cp);
        setValue("");
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Object getValue(ClassLoader cl, ClassPool cp, Method m) {
        return getValue();
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    Class getType(ClassLoader cl) {
        return String.class;
    }

    public String getValue() {
        return this.cp.getUtf8Info(this.valueIndex);
    }

    public void setValue(String newValue) {
        this.valueIndex = this.cp.addUtf8Info(newValue);
    }

    public String toString() {
        return SymbolConstants.QUOTES_SYMBOL + getValue() + SymbolConstants.QUOTES_SYMBOL;
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void write(AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(getValue());
    }

    @Override // org.apache.ibatis.javassist.bytecode.annotation.MemberValue
    public void accept(MemberValueVisitor visitor) {
        visitor.visitStringMemberValue(this);
    }
}
