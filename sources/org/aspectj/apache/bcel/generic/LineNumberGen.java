package org.aspectj.apache.bcel.generic;

import java.io.Serializable;
import org.aspectj.apache.bcel.classfile.LineNumber;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/LineNumberGen.class */
public class LineNumberGen implements InstructionTargeter, Cloneable, Serializable {
    private InstructionHandle ih;
    private int src_line;

    public LineNumberGen(InstructionHandle instructionHandle, int i) {
        setInstruction(instructionHandle);
        setSourceLine(i);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle instructionHandle) {
        return this.ih == instructionHandle;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        if (instructionHandle != this.ih) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but " + this.ih + "}");
        }
        setInstruction(instructionHandle2);
    }

    public LineNumber getLineNumber() {
        return new LineNumber(this.ih.getPosition(), this.src_line);
    }

    public void setInstruction(InstructionHandle instructionHandle) {
        InstructionBranch.notifyTarget(this.ih, instructionHandle, this);
        this.ih = instructionHandle;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
            return null;
        }
    }

    public InstructionHandle getInstruction() {
        return this.ih;
    }

    public void setSourceLine(int i) {
        this.src_line = i;
    }

    public int getSourceLine() {
        return this.src_line;
    }
}
