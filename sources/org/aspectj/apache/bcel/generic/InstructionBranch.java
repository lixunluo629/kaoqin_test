package org.aspectj.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Font;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.ConstantPool;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionBranch.class */
public class InstructionBranch extends Instruction implements InstructionTargeter {
    private static final int UNSET = -1;
    protected int targetIndex;
    protected InstructionHandle targetInstruction;
    protected int positionOfThisInstruction;

    public InstructionBranch(short s, InstructionHandle instructionHandle) {
        super(s);
        this.targetIndex = -1;
        setTarget(instructionHandle);
    }

    public InstructionBranch(short s, int i) {
        super(s);
        this.targetIndex = -1;
        this.targetIndex = i;
    }

    public InstructionBranch(short s) {
        super(s);
        this.targetIndex = -1;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        int targetOffset = getTargetOffset();
        if (Math.abs(targetOffset) >= 32767 && this.opcode != 200 && this.opcode != 201) {
            throw new ClassGenException("Branch target offset too large for short.  Instruction: " + getName().toUpperCase() + "(" + ((int) this.opcode) + ")");
        }
        dataOutputStream.writeByte(this.opcode);
        switch (this.opcode) {
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 198:
            case 199:
                dataOutputStream.writeShort(targetOffset);
                return;
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 175:
            case 176:
            case 177:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            default:
                throw new IllegalStateException("Don't know how to write out " + getName().toUpperCase());
            case 200:
            case 201:
                dataOutputStream.writeInt(targetOffset);
                return;
        }
    }

    protected int getTargetOffset() {
        if (this.targetInstruction == null && this.targetIndex == -1) {
            throw new ClassGenException("Target of " + super.toString(true) + " is unknown");
        }
        return this.targetInstruction == null ? this.targetIndex : this.targetInstruction.getPosition() - this.positionOfThisInstruction;
    }

    protected int updatePosition(int i, int i2) {
        int targetOffset = getTargetOffset();
        this.positionOfThisInstruction += i;
        if (Math.abs(targetOffset) < Font.COLOR_NORMAL - i2 || this.opcode == 201 || this.opcode == 200) {
            return 0;
        }
        if (this.opcode != 168 && this.opcode != 167) {
            throw new IllegalStateException("Unable to pack method, jump (with opcode=" + ((int) this.opcode) + ") is too far: " + Math.abs(targetOffset));
        }
        if (this.opcode == 168) {
            this.opcode = (short) 201;
            return 2;
        }
        this.opcode = (short) 200;
        return 2;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public String toString(boolean z) {
        String string = super.toString(z);
        String string2 = "null";
        if (z) {
            if (this.targetInstruction != null) {
                string2 = this.targetInstruction.getInstruction() == this ? "<points to itself>" : this.targetInstruction.getInstruction() == null ? "<null destination>" : this.targetInstruction.getInstruction().toString(false);
            }
        } else if (this.targetInstruction != null) {
            this.targetIndex = getTargetOffset();
            string2 = "" + (this.targetIndex + this.positionOfThisInstruction);
        }
        return string + " -> " + string2;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public final int getIndex() {
        return this.targetIndex;
    }

    public InstructionHandle getTarget() {
        return this.targetInstruction;
    }

    public void setTarget(InstructionHandle instructionHandle) {
        notifyTarget(this.targetInstruction, instructionHandle, this);
        this.targetInstruction = instructionHandle;
    }

    static final void notifyTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, InstructionTargeter instructionTargeter) {
        if (instructionHandle != null) {
            instructionHandle.removeTargeter(instructionTargeter);
        }
        if (instructionHandle2 != null) {
            instructionHandle2.addTargeter(instructionTargeter);
        }
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        if (this.targetInstruction != instructionHandle) {
            throw new ClassGenException("Not targeting " + instructionHandle + ", but " + this.targetInstruction);
        }
        setTarget(instructionHandle2);
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle instructionHandle) {
        return this.targetInstruction == instructionHandle;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    void dispose() {
        setTarget(null);
        this.targetIndex = -1;
        this.positionOfThisInstruction = -1;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public Type getType(ConstantPool constantPool) {
        return (Constants.instFlags[this.opcode] & 16384) != 0 ? new ReturnaddressType(physicalSuccessor()) : super.getType(constantPool);
    }

    public InstructionHandle physicalSuccessor() {
        InstructionHandle next;
        InstructionHandle prev = this.targetInstruction;
        while (true) {
            next = prev;
            if (next.getPrev() == null) {
                break;
            }
            prev = next.getPrev();
        }
        while (next.getInstruction() != this) {
            next = next.getNext();
        }
        InstructionHandle instructionHandle = next;
        while (next != null) {
            next = next.getNext();
            if (next != null && next.getInstruction() == this) {
                throw new RuntimeException("physicalSuccessor() called on a shared JsrInstruction.");
            }
        }
        return instructionHandle.getNext();
    }

    public boolean isIfInstruction() {
        return (Constants.instFlags[this.opcode] & 8192) != 0;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override // org.aspectj.apache.bcel.generic.Instruction
    public int hashCode() {
        return (this.opcode * 37) + 17;
    }
}
