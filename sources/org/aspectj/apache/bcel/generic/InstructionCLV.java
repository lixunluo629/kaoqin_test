package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionCLV.class */
public class InstructionCLV extends InstructionLV {
    public InstructionCLV(short s) {
        super(s);
    }

    public InstructionCLV(short s, int i) {
        super(s, i);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionLV, org.aspectj.apache.bcel.generic.Instruction
    public void setIndex(int i) {
        if (i != getIndex()) {
            throw new ClassGenException("Do not attempt to modify the index to '" + i + "' for this constant instruction: " + this);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionLV
    public boolean canSetIndex() {
        return false;
    }
}
