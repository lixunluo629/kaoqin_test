package org.aspectj.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.util.ByteSequence;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/LOOKUPSWITCH.class */
public class LOOKUPSWITCH extends InstructionSelect {
    public LOOKUPSWITCH(int[] iArr, InstructionHandle[] instructionHandleArr, InstructionHandle instructionHandle) {
        super((short) 171, iArr, instructionHandleArr, instructionHandle);
        this.length = (short) (9 + (this.matchLength * 8));
        this.fixedLength = this.length;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionSelect, org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeInt(this.matchLength);
        for (int i = 0; i < this.matchLength; i++) {
            dataOutputStream.writeInt(this.match[i]);
            int targetOffset = getTargetOffset(this.targets[i]);
            this.indices[i] = targetOffset;
            dataOutputStream.writeInt(targetOffset);
        }
    }

    public LOOKUPSWITCH(ByteSequence byteSequence) throws IOException {
        super((short) 171, byteSequence);
        this.matchLength = byteSequence.readInt();
        this.fixedLength = (short) (9 + (this.matchLength * 8));
        this.length = (short) (this.fixedLength + this.padding);
        this.match = new int[this.matchLength];
        this.indices = new int[this.matchLength];
        this.targets = new InstructionHandle[this.matchLength];
        for (int i = 0; i < this.matchLength; i++) {
            this.match[i] = byteSequence.readInt();
            this.indices[i] = byteSequence.readInt();
        }
    }
}
