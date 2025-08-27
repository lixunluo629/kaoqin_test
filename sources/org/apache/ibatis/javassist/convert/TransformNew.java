package org.apache.ibatis.javassist.convert;

import org.apache.ibatis.javassist.CannotCompileException;
import org.apache.ibatis.javassist.CtClass;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.CodeIterator;
import org.apache.ibatis.javassist.bytecode.ConstPool;
import org.apache.ibatis.javassist.bytecode.Descriptor;
import org.apache.ibatis.javassist.bytecode.StackMap;
import org.apache.ibatis.javassist.bytecode.StackMapTable;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/convert/TransformNew.class */
public final class TransformNew extends Transformer {
    private int nested;
    private String classname;
    private String trapClass;
    private String trapMethod;

    public TransformNew(Transformer next, String classname, String trapClass, String trapMethod) {
        super(next);
        this.classname = classname;
        this.trapClass = trapClass;
        this.trapMethod = trapMethod;
    }

    @Override // org.apache.ibatis.javassist.convert.Transformer
    public void initialize(ConstPool cp, CodeAttribute attr) {
        this.nested = 0;
    }

    @Override // org.apache.ibatis.javassist.convert.Transformer
    public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws CannotCompileException {
        int c = iterator.byteAt(pos);
        if (c == 187) {
            int index = iterator.u16bitAt(pos + 1);
            if (cp.getClassInfo(index).equals(this.classname)) {
                if (iterator.byteAt(pos + 3) != 89) {
                    throw new CannotCompileException("NEW followed by no DUP was found");
                }
                iterator.writeByte(0, pos);
                iterator.writeByte(0, pos + 1);
                iterator.writeByte(0, pos + 2);
                iterator.writeByte(0, pos + 3);
                this.nested++;
                StackMapTable smt = (StackMapTable) iterator.get().getAttribute(StackMapTable.tag);
                if (smt != null) {
                    smt.removeNew(pos);
                }
                StackMap sm = (StackMap) iterator.get().getAttribute(StackMap.tag);
                if (sm != null) {
                    sm.removeNew(pos);
                }
            }
        } else if (c == 183) {
            int index2 = iterator.u16bitAt(pos + 1);
            int typedesc = cp.isConstructor(this.classname, index2);
            if (typedesc != 0 && this.nested > 0) {
                int methodref = computeMethodref(typedesc, cp);
                iterator.writeByte(184, pos);
                iterator.write16bit(methodref, pos + 1);
                this.nested--;
            }
        }
        return pos;
    }

    private int computeMethodref(int typedesc, ConstPool cp) {
        int classIndex = cp.addClassInfo(this.trapClass);
        int mnameIndex = cp.addUtf8Info(this.trapMethod);
        return cp.addMethodrefInfo(classIndex, cp.addNameAndTypeInfo(mnameIndex, cp.addUtf8Info(Descriptor.changeReturnType(this.classname, cp.getUtf8Info(typedesc)))));
    }
}
