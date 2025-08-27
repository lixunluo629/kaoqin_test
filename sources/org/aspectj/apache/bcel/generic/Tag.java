package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/Tag.class */
public abstract class Tag implements InstructionTargeter, Cloneable {
    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle instructionHandle) {
        return false;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        instructionHandle.removeTargeter(this);
        if (instructionHandle2 != null) {
            instructionHandle2.addTargeter(this);
        }
    }

    public Tag copy() {
        try {
            return (Tag) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Sanity check, can't clone me");
        }
    }
}
