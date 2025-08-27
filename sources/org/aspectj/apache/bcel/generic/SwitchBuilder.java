package org.aspectj.apache.bcel.generic;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/SwitchBuilder.class */
public final class SwitchBuilder {
    private int[] match;
    private InstructionHandle[] targets;
    private InstructionSelect instruction;
    private int match_length;

    public SwitchBuilder(int[] iArr, InstructionHandle[] instructionHandleArr, InstructionHandle instructionHandle, int i) {
        this.match = (int[]) iArr.clone();
        this.targets = (InstructionHandle[]) instructionHandleArr.clone();
        int length = iArr.length;
        this.match_length = length;
        if (length < 2) {
            if (iArr.length == 0) {
                this.instruction = new LOOKUPSWITCH(iArr, instructionHandleArr, instructionHandle);
                return;
            } else {
                this.instruction = new TABLESWITCH(iArr, instructionHandleArr, instructionHandle);
                return;
            }
        }
        sort(0, this.match_length - 1);
        if (!matchIsOrdered(i)) {
            this.instruction = new LOOKUPSWITCH(this.match, this.targets, instructionHandle);
        } else {
            fillup(i, instructionHandle);
            this.instruction = new TABLESWITCH(this.match, this.targets, instructionHandle);
        }
    }

    public SwitchBuilder(int[] iArr, InstructionHandle[] instructionHandleArr, InstructionHandle instructionHandle) {
        this(iArr, instructionHandleArr, instructionHandle, 1);
    }

    private final void fillup(int i, InstructionHandle instructionHandle) {
        int i2 = this.match_length + (this.match_length * i);
        int[] iArr = new int[i2];
        InstructionHandle[] instructionHandleArr = new InstructionHandle[i2];
        int i3 = 1;
        iArr[0] = this.match[0];
        instructionHandleArr[0] = this.targets[0];
        for (int i4 = 1; i4 < this.match_length; i4++) {
            int i5 = this.match[i4 - 1];
            int i6 = this.match[i4] - i5;
            for (int i7 = 1; i7 < i6; i7++) {
                iArr[i3] = i5 + i7;
                instructionHandleArr[i3] = instructionHandle;
                i3++;
            }
            iArr[i3] = this.match[i4];
            instructionHandleArr[i3] = this.targets[i4];
            i3++;
        }
        this.match = new int[i3];
        this.targets = new InstructionHandle[i3];
        System.arraycopy(iArr, 0, this.match, 0, i3);
        System.arraycopy(instructionHandleArr, 0, this.targets, 0, i3);
    }

    private final void sort(int i, int i2) {
        int i3 = i;
        int i4 = i2;
        int i5 = this.match[(i + i2) / 2];
        while (true) {
            if (this.match[i3] < i5) {
                i3++;
            } else {
                while (i5 < this.match[i4]) {
                    i4--;
                }
                if (i3 <= i4) {
                    int i6 = this.match[i3];
                    this.match[i3] = this.match[i4];
                    this.match[i4] = i6;
                    InstructionHandle instructionHandle = this.targets[i3];
                    this.targets[i3] = this.targets[i4];
                    this.targets[i4] = instructionHandle;
                    i3++;
                    i4--;
                }
                if (i3 > i4) {
                    break;
                }
            }
        }
        if (i < i4) {
            sort(i, i4);
        }
        if (i3 < i2) {
            sort(i3, i2);
        }
    }

    private final boolean matchIsOrdered(int i) {
        for (int i2 = 1; i2 < this.match_length; i2++) {
            int i3 = this.match[i2] - this.match[i2 - 1];
            if (i3 > i || i3 < 0) {
                return false;
            }
        }
        return true;
    }

    public final InstructionSelect getInstruction() {
        return this.instruction;
    }
}
