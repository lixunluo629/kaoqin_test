package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/INVOKEINTERFACE.class */
public final class INVOKEINTERFACE extends InvokeInstruction {
    private int nargs;

    public INVOKEINTERFACE(int i, int i2, int i3) {
        super((short) 185, i);
        if (i2 < 1) {
            throw new ClassGenException("Number of arguments must be > 0 " + i2);
        }
        this.nargs = i2;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeShort(this.index);
        dataOutputStream.writeByte(this.nargs);
        dataOutputStream.writeByte(0);
    }

    public int getCount() {
        return this.nargs;
    }

    @Override // org.aspectj.apache.bcel.generic.InvokeInstruction, org.aspectj.apache.bcel.generic.InstructionCP
    public String toString(ConstantPool constantPool) {
        return super.toString(constantPool) + SymbolConstants.SPACE_SYMBOL + this.nargs;
    }

    @Override // org.aspectj.apache.bcel.generic.InvokeInstruction, org.aspectj.apache.bcel.generic.Instruction
    public int consumeStack(ConstantPool constantPool) {
        return this.nargs;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof INVOKEINTERFACE)) {
            return false;
        }
        INVOKEINTERFACE invokeinterface = (INVOKEINTERFACE) obj;
        return invokeinterface.opcode == this.opcode && invokeinterface.index == this.index && invokeinterface.nargs == this.nargs;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionCP, org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + (this.index * (this.nargs + 17));
    }
}
