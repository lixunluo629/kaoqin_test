package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.Constants;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionLV.class */
public class InstructionLV extends Instruction {
    protected int lvar;

    public InstructionLV(short s, int i) {
        super(s);
        this.lvar = -1;
        this.lvar = i;
    }

    public InstructionLV(short s) {
        super(s);
        this.lvar = -1;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        if (this.lvar == -1) {
            dataOutputStream.writeByte(this.opcode);
            return;
        }
        if (this.lvar >= 4) {
            if (wide()) {
                dataOutputStream.writeByte(196);
            }
            dataOutputStream.writeByte(this.opcode);
            if (wide()) {
                dataOutputStream.writeShort(this.lvar);
                return;
            } else {
                dataOutputStream.writeByte(this.lvar);
                return;
            }
        }
        if (this.opcode == 25) {
            dataOutputStream.writeByte(42 + this.lvar);
            return;
        }
        if (this.opcode == 58) {
            dataOutputStream.writeByte(75 + this.lvar);
            return;
        }
        if (this.opcode == 21) {
            dataOutputStream.writeByte(26 + this.lvar);
            return;
        }
        if (this.opcode == 54) {
            dataOutputStream.writeByte(59 + this.lvar);
            return;
        }
        if (this.opcode == 24) {
            dataOutputStream.writeByte(38 + this.lvar);
            return;
        }
        if (this.opcode == 57) {
            dataOutputStream.writeByte(71 + this.lvar);
            return;
        }
        if (this.opcode == 23) {
            dataOutputStream.writeByte(34 + this.lvar);
            return;
        }
        if (this.opcode == 56) {
            dataOutputStream.writeByte(67 + this.lvar);
            return;
        }
        if (this.opcode == 22) {
            dataOutputStream.writeByte(30 + this.lvar);
            return;
        }
        if (this.opcode == 55) {
            dataOutputStream.writeByte(63 + this.lvar);
            return;
        }
        if (wide()) {
            dataOutputStream.writeByte(196);
        }
        dataOutputStream.writeByte(this.opcode);
        if (wide()) {
            dataOutputStream.writeShort(this.lvar);
        } else {
            dataOutputStream.writeByte(this.lvar);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        if ((this.opcode < 26 || this.opcode > 45) && (this.opcode < 59 || this.opcode > 78)) {
            return super.toString(z) + ((this.lvar == -1 || this.lvar >= 4) ? SymbolConstants.SPACE_SYMBOL : "_") + this.lvar;
        }
        return super.toString(z);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean isALOAD() {
        return this.opcode == 25 || (this.opcode >= 42 && this.opcode <= 45);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean isASTORE() {
        return this.opcode == 58 || (this.opcode >= 75 && this.opcode <= 78);
    }

    public int getBaseOpcode() {
        if ((this.opcode >= 21 && this.opcode <= 25) || (this.opcode >= 54 && this.opcode <= 58)) {
            return this.opcode;
        }
        if (this.opcode < 26 || this.opcode > 45) {
            int i = this.opcode - 59;
            return ((i - (i % 4)) / 4) + 54;
        }
        int i2 = this.opcode - 26;
        return ((i2 - (i2 % 4)) / 4) + 21;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public final int getIndex() {
        if (this.lvar != -1) {
            return this.lvar;
        }
        if (this.opcode >= 26 && this.opcode <= 45) {
            return (this.opcode - 26) % 4;
        }
        if (this.opcode < 59 || this.opcode > 78) {
            return -1;
        }
        return (this.opcode - 59) % 4;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void setIndex(int i) {
        if (getIndex() != i) {
            if (this.opcode >= 26 && this.opcode <= 45) {
                this.opcode = (short) (21 + ((this.opcode - 26) / 4));
            } else if (this.opcode >= 59 && this.opcode <= 78) {
                this.opcode = (short) (54 + ((this.opcode - 59) / 4));
            }
            this.lvar = i;
        }
    }

    public boolean canSetIndex() {
        return true;
    }

    public InstructionLV setIndexAndCopyIfNecessary(int i) {
        if (canSetIndex()) {
            setIndex(i);
            return this;
        }
        if (getIndex() == i) {
            return this;
        }
        int baseOpcode = getBaseOpcode();
        return i < 4 ? isStoreInstruction() ? (InstructionLV) InstructionConstants.INSTRUCTIONS[((baseOpcode - 54) * 4) + 59 + i] : (InstructionLV) InstructionConstants.INSTRUCTIONS[((baseOpcode - 21) * 4) + 26 + i] : new InstructionLV((short) baseOpcode, i);
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int getLength() {
        byte b = Constants.iLen[this.opcode];
        if (this.lvar == -1) {
            return b;
        }
        if (this.lvar >= 4) {
            return wide() ? b + 2 : b;
        }
        if (this.opcode == 25 || this.opcode == 58 || this.opcode == 21 || this.opcode == 54 || this.opcode == 24 || this.opcode == 57 || this.opcode == 23 || this.opcode == 56 || this.opcode == 22 || this.opcode == 55) {
            return 1;
        }
        return wide() ? b + 2 : b;
    }

    private final boolean wide() {
        return this.lvar > 255;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        if (!(obj instanceof InstructionLV)) {
            return false;
        }
        InstructionLV instructionLV = (InstructionLV) obj;
        return instructionLV.opcode == this.opcode && instructionLV.lvar == this.lvar;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + this.lvar;
    }
}
