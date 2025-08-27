package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionShort.class */
public class InstructionShort extends Instruction {
    private final short value;

    public InstructionShort(short s, short s2) {
        super(s);
        this.value = s2;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeShort(this.value);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        return super.toString(z) + SymbolConstants.SPACE_SYMBOL + ((int) this.value);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof InstructionShort)) {
            return false;
        }
        InstructionShort instructionShort = (InstructionShort) obj;
        return instructionShort.opcode == this.opcode && instructionShort.value == this.value;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + this.value;
    }
}
