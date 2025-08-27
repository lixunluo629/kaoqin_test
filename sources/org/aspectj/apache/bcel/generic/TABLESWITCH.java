package org.aspectj.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.aspectj.apache.bcel.util.ByteSequence;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/TABLESWITCH.class */
public class TABLESWITCH extends InstructionSelect {
    public TABLESWITCH(int[] iArr, InstructionHandle[] instructionHandleArr, InstructionHandle instructionHandle) {
        super((short) 170, iArr, instructionHandleArr, instructionHandle);
        this.length = (short) (13 + (this.matchLength * 4));
        this.fixedLength = this.length;
    }

    @Override // org.aspectj.apache.bcel.generic.InstructionSelect, org.aspectj.apache.bcel.generic.InstructionBranch, org.aspectj.apache.bcel.generic.Instruction
    public void dump(DataOutputStream dataOutputStream) throws IOException {
        super.dump(dataOutputStream);
        dataOutputStream.writeInt(this.matchLength > 0 ? this.match[0] : 0);
        dataOutputStream.writeInt(this.matchLength > 0 ? this.match[this.matchLength - 1] : 0);
        for (int i = 0; i < this.matchLength; i++) {
            int targetOffset = getTargetOffset(this.targets[i]);
            this.indices[i] = targetOffset;
            dataOutputStream.writeInt(targetOffset);
        }
    }

    public TABLESWITCH(ByteSequence byteSequence) throws IOException {
        super((short) 170, byteSequence);
        int i = byteSequence.readInt();
        int i2 = byteSequence.readInt();
        this.matchLength = (i2 - i) + 1;
        this.fixedLength = (short) (13 + (this.matchLength * 4));
        this.length = (short) (this.fixedLength + this.padding);
        this.match = new int[this.matchLength];
        this.indices = new int[this.matchLength];
        this.targets = new InstructionHandle[this.matchLength];
        for (int i3 = i; i3 <= i2; i3++) {
            this.match[i3 - i] = i3;
        }
        for (int i4 = 0; i4 < this.matchLength; i4++) {
            this.indices[i4] = byteSequence.readInt();
        }
    }
}
