package org.aspectj.apache.bcel.generic;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.aspectj.apache.bcel.classfile.Utility;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionHandle.class */
public class InstructionHandle implements Serializable {
    InstructionHandle next;
    InstructionHandle prev;
    Instruction instruction;
    protected int pos = -1;
    private Set<InstructionTargeter> targeters = Collections.emptySet();

    protected InstructionHandle(Instruction instruction) {
        setInstruction(instruction);
    }

    static final InstructionHandle getInstructionHandle(Instruction instruction) {
        return new InstructionHandle(instruction);
    }

    public final InstructionHandle getNext() {
        return this.next;
    }

    public final InstructionHandle getPrev() {
        return this.prev;
    }

    public final Instruction getInstruction() {
        return this.instruction;
    }

    public void setInstruction(Instruction instruction) {
        if (this.instruction != null) {
            this.instruction.dispose();
        }
        this.instruction = instruction;
    }

    public int getPosition() {
        return this.pos;
    }

    void setPosition(int i) {
        this.pos = i;
    }

    void dispose() {
        this.prev = null;
        this.next = null;
        this.instruction.dispose();
        this.instruction = null;
        this.pos = -1;
        removeAllTargeters();
    }

    public void removeAllTargeters() {
        this.targeters.clear();
    }

    public void removeTargeter(InstructionTargeter instructionTargeter) {
        this.targeters.remove(instructionTargeter);
    }

    public void addTargeter(InstructionTargeter instructionTargeter) {
        if (this.targeters == Collections.EMPTY_SET) {
            this.targeters = new HashSet();
        }
        this.targeters.add(instructionTargeter);
    }

    public boolean hasTargeters() {
        return !this.targeters.isEmpty();
    }

    public Set<InstructionTargeter> getTargeters() {
        return this.targeters;
    }

    public Set<InstructionTargeter> getTargetersCopy() {
        HashSet hashSet = new HashSet();
        hashSet.addAll(this.targeters);
        return hashSet;
    }

    public String toString(boolean z) {
        return Utility.format(this.pos, 4, false, ' ') + ": " + this.instruction.toString(z);
    }

    public String toString() {
        return toString(true);
    }
}
