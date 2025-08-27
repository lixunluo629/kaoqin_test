package org.aspectj.apache.bcel.generic;

import java.io.Serializable;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.classfile.LocalVariable;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/LocalVariableGen.class */
public class LocalVariableGen implements InstructionTargeter, Cloneable, Serializable {
    private int index;
    private String name;
    private Type type;
    private InstructionHandle start;
    private InstructionHandle end;

    public LocalVariableGen(int i, String str, Type type, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        if (i < 0 || i > 65535) {
            throw new ClassGenException("Invalid index index: " + i);
        }
        this.name = str;
        this.type = type;
        this.index = i;
        setStart(instructionHandle);
        setEnd(instructionHandle2);
    }

    public LocalVariable getLocalVariable(ConstantPool constantPool) {
        int position = this.start.getPosition();
        int position2 = this.end.getPosition() - position;
        if (position2 > 0) {
            position2 += this.end.getInstruction().getLength();
        }
        return new LocalVariable(position, position2, constantPool.addUtf8(this.name), constantPool.addUtf8(this.type.getSignature()), this.index, constantPool);
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

    public InstructionHandle getStart() {
        return this.start;
    }

    public InstructionHandle getEnd() {
        return this.end;
    }

    public void setStart(InstructionHandle instructionHandle) {
        InstructionBranch.notifyTarget(this.start, instructionHandle, this);
        this.start = instructionHandle;
    }

    public void setEnd(InstructionHandle instructionHandle) {
        InstructionBranch.notifyTarget(this.end, instructionHandle, this);
        this.end = instructionHandle;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        boolean z = false;
        if (this.start == instructionHandle) {
            z = true;
            setStart(instructionHandle2);
        }
        if (this.end == instructionHandle) {
            z = true;
            setEnd(instructionHandle2);
        }
        if (!z) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but {" + this.start + ", " + this.end + "}");
        }
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle instructionHandle) {
        return this.start == instructionHandle || this.end == instructionHandle;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LocalVariableGen)) {
            return false;
        }
        LocalVariableGen localVariableGen = (LocalVariableGen) obj;
        return localVariableGen.index == this.index && localVariableGen.start == this.start && localVariableGen.end == this.end;
    }

    public String toString() {
        return "LocalVariableGen(" + this.name + ", " + this.type + ", " + this.start + ", " + this.end + ")";
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
