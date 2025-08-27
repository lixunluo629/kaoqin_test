package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/IINC.class */
public class IINC extends InstructionLV {
    private int c;

    public IINC(int i, int i2, boolean z) {
        super((short) 132, i);
        this.c = i2;
    }

    private boolean wide() {
        return this.lvar > 255 || Math.abs(this.c) > 127;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionLV, org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (!wide()) {
            dataOutputStream.writeByte(this.opcode);
            dataOutputStream.writeByte(this.lvar);
            dataOutputStream.writeByte(this.c);
        } else {
            dataOutputStream.writeByte(196);
            dataOutputStream.writeByte(this.opcode);
            dataOutputStream.writeShort(this.lvar);
            dataOutputStream.writeShort(this.c);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionLV, org.aspectj.apache.bcel.generic.Instruction
    public int getLength() {
        return wide() ? 6 : 3;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionLV, org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        return super.toString(z) + SymbolConstants.SPACE_SYMBOL + this.c;
    }

    public final int getIncrement() {
        return this.c;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionLV, org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof IINC)) {
            return false;
        }
        IINC iinc = (IINC) obj;
        return iinc.lvar == this.lvar && iinc.c == this.c;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionLV, org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + (this.lvar * (this.c + 17));
    }
}
