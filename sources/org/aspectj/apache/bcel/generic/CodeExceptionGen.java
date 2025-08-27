package org.aspectj.apache.bcel.generic;

import java.io.Serializable;
import org.aspectj.apache.bcel.classfile.CodeException;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/CodeExceptionGen.class */
public final class CodeExceptionGen implements InstructionTargeter, Cloneable, Serializable {
    private InstructionHandle start_pc;
    private InstructionHandle end_pc;
    private InstructionHandle handler_pc;
    private ObjectType catch_type;

    public CodeExceptionGen(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, InstructionHandle instructionHandle3, ObjectType objectType) {
        setStartPC(instructionHandle);
        setEndPC(instructionHandle2);
        setHandlerPC(instructionHandle3);
        this.catch_type = objectType;
    }

    public CodeException getCodeException(ConstantPool constantPool) {
        return new CodeException(this.start_pc.getPosition(), this.end_pc.getPosition() + this.end_pc.getInstruction().getLength(), this.handler_pc.getPosition(), this.catch_type == null ? 0 : constantPool.addClass(this.catch_type));
    }

    public void setStartPC(InstructionHandle instructionHandle) {
        InstructionBranch.notifyTarget(this.start_pc, instructionHandle, this);
        this.start_pc = instructionHandle;
    }

    public void setEndPC(InstructionHandle instructionHandle) {
        InstructionBranch.notifyTarget(this.end_pc, instructionHandle, this);
        this.end_pc = instructionHandle;
    }

    public void setHandlerPC(InstructionHandle instructionHandle) {
        InstructionBranch.notifyTarget(this.handler_pc, instructionHandle, this);
        this.handler_pc = instructionHandle;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        boolean z = false;
        if (this.start_pc == instructionHandle) {
            z = true;
            setStartPC(instructionHandle2);
        }
        if (this.end_pc == instructionHandle) {
            z = true;
            setEndPC(instructionHandle2);
        }
        if (this.handler_pc == instructionHandle) {
            z = true;
            setHandlerPC(instructionHandle2);
        }
        if (!z) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but {" + this.start_pc + ", " + this.end_pc + ", " + this.handler_pc + "}");
        }
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle instructionHandle) {
        return this.start_pc == instructionHandle || this.end_pc == instructionHandle || this.handler_pc == instructionHandle;
    }

    public void setCatchType(ObjectType objectType) {
        this.catch_type = objectType;
    }

    public ObjectType getCatchType() {
        return this.catch_type;
    }

    public InstructionHandle getStartPC() {
        return this.start_pc;
    }

    public InstructionHandle getEndPC() {
        return this.end_pc;
    }

    public InstructionHandle getHandlerPC() {
        return this.handler_pc;
    }

    public String toString() {
        return "CodeExceptionGen(" + this.start_pc + ", " + this.end_pc + ", " + this.handler_pc + ")";
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println(e);
            return null;
        }
    }
}
