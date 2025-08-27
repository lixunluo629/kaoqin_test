package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/BranchHandle.class */
public final class BranchHandle extends InstructionHandle {
    private InstructionBranch bi;

    private BranchHandle(InstructionBranch instructionBranch) {
        super(instructionBranch);
        this.bi = instructionBranch;
    }

    static final BranchHandle getBranchHandle(InstructionBranch instructionBranch) {
        return new BranchHandle(instructionBranch);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionHandle
    public int getPosition() {
        return this.bi.positionOfThisInstruction;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionHandle
    void setPosition(int i) {
        this.bi.positionOfThisInstruction = i;
        this.pos = i;
    }

    protected int updatePosition(int i, int i2) {
        int iUpdatePosition = this.bi.updatePosition(i, i2);
        this.pos = this.bi.positionOfThisInstruction;
        return iUpdatePosition;
    }

    public void setTarget(InstructionHandle instructionHandle) {
        this.bi.setTarget(instructionHandle);
    }

    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        this.bi.updateTarget(instructionHandle, instructionHandle2);
    }

    public InstructionHandle getTarget() {
        return this.bi.getTarget();
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionHandle
    public void setInstruction(Instruction instruction) {
        super.setInstruction(instruction);
        this.bi = (InstructionBranch) instruction;
    }
}
