package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.StringTokenizer;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InvokeInstruction.class */
public class InvokeInstruction extends FieldOrMethod {
    public InvokeInstruction(short s, int i) {
        super(s, i);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP
    public String toString(ConstantPool constantPool) {
        StringTokenizer stringTokenizer = new StringTokenizer(constantPool.constantToString(constantPool.getConstant(this.index)));
        return Constants.OPCODE_NAMES[this.opcode] + SymbolConstants.SPACE_SYMBOL + stringTokenizer.nextToken().replace('.', '/') + stringTokenizer.nextToken();
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int consumeStack(ConstantPool constantPool) throws ClassFormatException {
        int argumentSizes = Type.getArgumentSizes(getSignature(constantPool));
        if (this.opcode != 184) {
            argumentSizes++;
        }
        return argumentSizes;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int produceStack(ConstantPool constantPool) {
        return getReturnType(constantPool).getSize();
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public Type getType(ConstantPool constantPool) {
        return getReturnType(constantPool);
    }

    public String getMethodName(ConstantPool constantPool) {
        return getName(constantPool);
    }

    public Type getReturnType(ConstantPool constantPool) {
        return Type.getReturnType(getSignature(constantPool));
    }

    public Type[] getArgumentTypes(ConstantPool constantPool) {
        return Type.getArgumentTypes(getSignature(constantPool));
    }
}
