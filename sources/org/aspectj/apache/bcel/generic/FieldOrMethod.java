package org.aspectj.apache.bcel.generic;

import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ConstantCP;
import org.aspectj.apache.bcel.classfile.ConstantNameAndType;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.ConstantUtf8;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/FieldOrMethod.class */
public abstract class FieldOrMethod extends InstructionCP {
    protected String signature;
    protected String name;
    private String classname;

    protected FieldOrMethod(short s, int i) {
        super(s, i);
    }

    public String getSignature(ConstantPool constantPool) {
        if (this.signature == null) {
            this.signature = ((ConstantUtf8) constantPool.getConstant(((ConstantNameAndType) constantPool.getConstant(((ConstantCP) constantPool.getConstant(this.index)).getNameAndTypeIndex())).getSignatureIndex())).getValue();
        }
        return this.signature;
    }

    public String getName(ConstantPool constantPool) {
        if (this.name == null) {
            this.name = ((ConstantUtf8) constantPool.getConstant(((ConstantNameAndType) constantPool.getConstant(((ConstantCP) constantPool.getConstant(this.index)).getNameAndTypeIndex())).getNameIndex())).getValue();
        }
        return this.name;
    }

    public String getClassName(ConstantPool constantPool) throws ClassFormatException {
        if (this.classname == null) {
            String constantString = constantPool.getConstantString(((ConstantCP) constantPool.getConstant(this.index)).getClassIndex(), (byte) 7);
            if (constantString.charAt(0) == '[') {
                this.classname = constantString;
            } else {
                this.classname = constantString.replace('/', '.');
            }
        }
        return this.classname;
    }

    public ObjectType getClassType(ConstantPool constantPool) {
        return new ObjectType(getClassName(constantPool));
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public ObjectType getLoadClassType(ConstantPool constantPool) {
        return getClassType(constantPool);
    }
}
