package org.aspectj.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.util.ByteSequence;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionSelect.class */
public abstract class InstructionSelect extends InstructionBranch {
    protected int[] match;
    protected int[] indices;
    protected InstructionHandle[] targets;
    protected int fixedLength;
    protected int matchLength;
    protected int padding;
    protected short length;

    InstructionSelect(short s, int[] iArr, InstructionHandle[] instructionHandleArr, InstructionHandle instructionHandle) {
        super(s, instructionHandle);
        this.padding = 0;
        this.targets = instructionHandleArr;
        for (InstructionHandle instructionHandle2 : instructionHandleArr) {
            notifyTarget(null, instructionHandle2, this);
        }
        this.match = iArr;
        int length = iArr.length;
        this.matchLength = length;
        if (length != instructionHandleArr.length) {
            throw new ClassGenException("Match and target array have not the same length");
        }
        this.indices = new int[this.matchLength];
    }

    protected int getTargetOffset(InstructionHandle instructionHandle) {
        if (instructionHandle == null) {
            throw new ClassGenException("Target of " + super.toString(true) + " is invalid null handle");
        }
        int position = instructionHandle.getPosition();
        if (position < 0) {
            throw new ClassGenException("Invalid branch target position offset for " + super.toString(true) + ":" + position + ":" + instructionHandle);
        }
        return position - this.positionOfThisInstruction;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch
    protected int updatePosition(int i, int i2) {
        this.positionOfThisInstruction += i;
        short s = this.length;
        this.padding = (4 - ((this.positionOfThisInstruction + 1) % 4)) % 4;
        this.length = (short) (this.fixedLength + this.padding);
        return this.length - s;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(this.opcode);
        for (int i = 0; i < this.padding; i++) {
            dataOutputStream.writeByte(0);
        }
        this.targetIndex = getTargetOffset();
        dataOutputStream.writeInt(this.targetIndex);
    }

    public InstructionSelect(short s, ByteSequence byteSequence) throws IOException {
        super(s);
        this.padding = 0;
        this.padding = (4 - (byteSequence.getIndex() % 4)) % 4;
        for (int i = 0; i < this.padding; i++) {
            byteSequence.readByte();
        }
        this.targetIndex = byteSequence.readInt();
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        StringBuffer stringBuffer = new StringBuffer(super.toString(z));
        if (z) {
            for (int i = 0; i < this.matchLength; i++) {
                String string = "null";
                if (this.targets[i] != null) {
                    string = this.targets[i].getInstruction().toString();
                }
                stringBuffer.append("(" + this.match[i] + ", " + string + " = {" + this.indices[i] + "})");
            }
        } else {
            stringBuffer.append(" ...");
        }
        return stringBuffer.toString();
    }

    public void setTarget(int i, InstructionHandle instructionHandle) {
        notifyTarget(this.targets[i], instructionHandle, this);
        this.targets[i] = instructionHandle;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.InstructionTargeter
    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        boolean z = false;
        if (this.targetInstruction == instructionHandle) {
            z = true;
            setTarget(instructionHandle2);
        }
        for (int i = 0; i < this.targets.length; i++) {
            if (this.targets[i] == instructionHandle) {
                z = true;
                setTarget(i, instructionHandle2);
            }
        }
        if (!z) {
            throw new ClassGenException("Not targeting " + instructionHandle);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle instructionHandle) {
        if (this.targetInstruction == instructionHandle) {
            return true;
        }
        for (int i = 0; i < this.targets.length; i++) {
            if (this.targets[i] == instructionHandle) {
                return true;
            }
        }
        return false;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.Instruction
    void dispose() {
        super.dispose();
        for (int i = 0; i < this.targets.length; i++) {
            this.targets[i].removeTargeter(this);
        }
    }

    public int[] getMatchs() {
        return this.match;
    }

    public int[] getIndices() {
        return this.indices;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return this.opcode * 37;
    }

    public InstructionHandle[] getTargets() {
        return this.targets;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int getLength() {
        return this.length;
    }
}
