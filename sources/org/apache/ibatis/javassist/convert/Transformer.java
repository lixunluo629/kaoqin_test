package org.apache.ibatis.javassist.convert;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.bytecode.BadBytecode;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.CodeIterator;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.apache.ibatis.javassist.bytecode.Opcode;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/convert/Transformer.class */
public abstract class Transformer implements Opcode {
    private Transformer next;

    public abstract int transform(CtClass ctClass, int i, CodeIterator codeIterator, ConstPool constPool) throws BadBytecode, CannotCompileException;

    public Transformer(Transformer t) {
        this.next = t;
    }

    public Transformer getNext() {
        return this.next;
    }

    public void initialize(ConstPool cp, CodeAttribute attr) {
    }

    public void initialize(ConstPool cp, CtClass clazz, MethodInfo minfo) throws CannotCompileException {
        initialize(cp, minfo.getCodeAttribute());
    }

    public void clean() {
    }

    public int extraLocals() {
        return 0;
    }

    public int extraStack() {
        return 0;
    }
}
