package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionTargeter.class */
public interface InstructionTargeter {
    boolean containsTarget(InstructionHandle instructionHandle);

    void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2);
}
