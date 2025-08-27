package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantInvokeDynamic;
import org.aspectj.apache.bcel.classfile.ConstantNameAndType;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InvokeDynamic.class */
public final class InvokeDynamic extends InvokeInstruction {
    public InvokeDynamic(int i, int i2) {
        super((short) 186, i);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeShort(this.index);
        dataOutputStream.writeShort(0);
    }

    @Override // org.aspectj.apache.bcel.generic.InvokeInstruction, org.aspectj.apache.bcel.generic.InstructionCP
    public String toString(ConstantPool constantPool) {
        return super.toString(constantPool) + SymbolConstants.SPACE_SYMBOL + this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof InvokeDynamic)) {
            return false;
        }
        InvokeDynamic invokeDynamic = (InvokeDynamic) obj;
        return invokeDynamic.opcode == this.opcode && invokeDynamic.index == this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + this.index;
    }

    @Override // org.aspectj.apache.bcel.generic.InvokeInstruction
    public Type getReturnType(ConstantPool constantPool) {
        return Type.getReturnType(getSignature(constantPool));
    }

    @Override // org.aspectj.apache.bcel.generic.InvokeInstruction
    public Type[] getArgumentTypes(ConstantPool constantPool) {
        return Type.getArgumentTypes(getSignature(constantPool));
    }

    @Override // org.aspectj.apache.bcel.generic.FieldOrMethod
    public String getSignature(ConstantPool constantPool) {
        if (this.signature == null) {
            this.signature = constantPool.getConstantUtf8(((ConstantNameAndType) constantPool.getConstant(((ConstantInvokeDynamic) constantPool.getConstant(this.index)).getNameAndTypeIndex())).getSignatureIndex()).getValue();
        }
        return this.signature;
    }

    @Override // org.aspectj.apache.bcel.generic.FieldOrMethod
    public String getName(ConstantPool constantPool) {
        if (this.name == null) {
            this.name = constantPool.getConstantUtf8(((ConstantNameAndType) constantPool.getConstant(((ConstantInvokeDynamic) constantPool.getConstant(this.index)).getNameAndTypeIndex())).getNameIndex()).getValue();
        }
        return this.name;
    }

    @Override // org.aspectj.apache.bcel.generic.FieldOrMethod
    public String getClassName(ConstantPool constantPool) {
        throw new IllegalStateException("there is no classname for invokedynamic");
    }
}
