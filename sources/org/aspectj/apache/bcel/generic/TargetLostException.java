package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/TargetLostException.class */
public final class TargetLostException extends Exception {
    private InstructionHandle[] targets;

    TargetLostException(InstructionHandle[] instructionHandleArr, String str) {
        super(str);
        this.targets = instructionHandleArr;
    }

    public InstructionHandle[] getTargets() {
        return this.targets;
    }
}
