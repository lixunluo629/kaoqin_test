package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/ReturnaddressType.class */
public class ReturnaddressType extends Type {
    public static final ReturnaddressType NO_TARGET = new ReturnaddressType();
    private InstructionHandle returnTarget;

    private ReturnaddressType() {
        super((byte) 16, "<return address>");
    }

    public ReturnaddressType(InstructionHandle instructionHandle) {
        super((byte) 16, "<return address targeting " + instructionHandle + ">");
        this.returnTarget = instructionHandle;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ReturnaddressType) {
            return ((ReturnaddressType) obj).returnTarget.equals(this.returnTarget);
        }
        return false;
    }

    public InstructionHandle getTarget() {
        return this.returnTarget;
    }
}
