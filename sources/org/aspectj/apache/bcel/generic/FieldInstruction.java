package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/FieldInstruction.class */
public class FieldInstruction extends FieldOrMethod {
    public FieldInstruction(short s, int i) {
        super(s, i);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP
    public String toString(ConstantPool constantPool) {
        return Constants.OPCODE_NAMES[this.opcode] + SymbolConstants.SPACE_SYMBOL + constantPool.constantToString(this.index, (byte) 9);
    }

    protected int getFieldSize(ConstantPool constantPool) {
        return Type.getTypeSize(getSignature(constantPool));
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public Type getType(ConstantPool constantPool) {
        return getFieldType(constantPool);
    }

    public Type getFieldType(ConstantPool constantPool) {
        return Type.getType(getSignature(constantPool));
    }

    public String getFieldName(ConstantPool constantPool) {
        return getName(constantPool);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int produceStack(ConstantPool constantPool) {
        if (isStackProducer()) {
            return getFieldSize(constantPool);
        }
        return 0;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int consumeStack(ConstantPool constantPool) {
        if (!isStackConsumer()) {
            return 0;
        }
        if (this.opcode == 180) {
            return 1;
        }
        return getFieldSize(constantPool) + (this.opcode == 181 ? 1 : 0);
    }
}
