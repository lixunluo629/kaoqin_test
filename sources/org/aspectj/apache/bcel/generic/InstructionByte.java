package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionByte.class */
public class InstructionByte extends Instruction {
    private final byte theByte;

    public InstructionByte(short s, byte b) {
        super(s);
        this.theByte = b;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        dataOutputStream.writeByte(this.theByte);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        return super.toString(z) + SymbolConstants.SPACE_SYMBOL + ((int) this.theByte);
    }

    public final byte getTypecode() {
        return this.theByte;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public final Type getType() {
        return new ArrayType(BasicType.getType(this.theByte), 1);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof InstructionByte)) {
            return false;
        }
        InstructionByte instructionByte = (InstructionByte) obj;
        return instructionByte.opcode == this.opcode && instructionByte.theByte == this.theByte;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + this.theByte;
    }
}
