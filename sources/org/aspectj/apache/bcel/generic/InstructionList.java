package org.aspectj.apache.bcel.generic;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.aspectj.apache.bcel.Constants;
import org.aspectj.apache.bcel.classfile.ConstantPool;
import org.aspectj.apache.bcel.util.ByteSequence;
import org.springframework.jdbc.datasource.init.ScriptUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/generic/InstructionList.class */
public class InstructionList implements Serializable {
    private InstructionHandle start = null;
    private InstructionHandle end = null;
    private int length = 0;
    private int[] positions;
    static final /* synthetic */ boolean $assertionsDisabled;

    public InstructionList() {
    }

    public InstructionList(Instruction instruction) {
        append(instruction);
    }

    public boolean isEmpty() {
        return this.start == null;
    }

    public static InstructionHandle findHandle(InstructionHandle[] instructionHandleArr, int[] iArr, int i, int i2) {
        return findHandle(instructionHandleArr, iArr, i, i2, false);
    }

    public static InstructionHandle findHandle(InstructionHandle[] instructionHandleArr, int[] iArr, int i, int i2, boolean z) {
        int i3 = 0;
        int i4 = i - 1;
        do {
            int i5 = (i3 + i4) / 2;
            int i6 = iArr[i5];
            if (i6 == i2) {
                return instructionHandleArr[i5];
            }
            if (i2 < i6) {
                i4 = i5 - 1;
            } else {
                i3 = i5 + 1;
            }
        } while (i3 <= i4);
        if (!z) {
            return null;
        }
        int i7 = (i3 + i4) / 2;
        if (i7 < 0) {
            i7 = 0;
        }
        return instructionHandleArr[i7];
    }

    public InstructionHandle findHandle(int i) {
        return findHandle(getInstructionHandles(), this.positions, this.length, i);
    }

    public InstructionHandle[] getInstructionsAsArray() {
        return getInstructionHandles();
    }

    public InstructionHandle findHandle(int i, InstructionHandle[] instructionHandleArr) {
        return findHandle(instructionHandleArr, this.positions, this.length, i);
    }

    public InstructionHandle findHandle(int i, InstructionHandle[] instructionHandleArr, boolean z) {
        return findHandle(instructionHandleArr, this.positions, this.length, i, z);
    }

    public InstructionList(byte[] bArr) {
        ByteSequence byteSequence = new ByteSequence(bArr);
        InstructionHandle[] instructionHandleArr = new InstructionHandle[bArr.length];
        int[] iArr = new int[bArr.length];
        int i = 0;
        while (byteSequence.available() > 0) {
            try {
                int index = byteSequence.getIndex();
                iArr[i] = index;
                Instruction instruction = Instruction.readInstruction(byteSequence);
                BranchHandle branchHandleAppend = instruction instanceof InstructionBranch ? append((InstructionBranch) instruction) : append(instruction);
                branchHandleAppend.setPosition(index);
                instructionHandleArr[i] = branchHandleAppend;
                i++;
            } catch (IOException e) {
                throw new ClassGenException(e.toString());
            }
        }
        this.positions = new int[i];
        System.arraycopy(iArr, 0, this.positions, 0, i);
        for (int i2 = 0; i2 < i; i2++) {
            if (instructionHandleArr[i2] instanceof BranchHandle) {
                InstructionBranch instructionBranch = (InstructionBranch) instructionHandleArr[i2].instruction;
                InstructionHandle instructionHandleFindHandle = findHandle(instructionHandleArr, iArr, i, instructionBranch.positionOfThisInstruction + instructionBranch.getIndex());
                if (instructionHandleFindHandle == null) {
                    throw new ClassGenException("Couldn't find target for branch: " + instructionBranch);
                }
                instructionBranch.setTarget(instructionHandleFindHandle);
                if (instructionBranch instanceof InstructionSelect) {
                    InstructionSelect instructionSelect = (InstructionSelect) instructionBranch;
                    int[] indices = instructionSelect.getIndices();
                    for (int i3 = 0; i3 < indices.length; i3++) {
                        InstructionHandle instructionHandleFindHandle2 = findHandle(instructionHandleArr, iArr, i, instructionBranch.positionOfThisInstruction + indices[i3]);
                        if (instructionHandleFindHandle2 == null) {
                            throw new ClassGenException("Couldn't find target for switch: " + instructionBranch);
                        }
                        instructionSelect.setTarget(i3, instructionHandleFindHandle2);
                    }
                } else {
                    continue;
                }
            }
        }
    }

    public InstructionHandle append(InstructionHandle instructionHandle, InstructionList instructionList) {
        if (!$assertionsDisabled && instructionList == null) {
            throw new AssertionError();
        }
        if (instructionList.isEmpty()) {
            return instructionHandle;
        }
        InstructionHandle instructionHandle2 = instructionHandle.next;
        InstructionHandle instructionHandle3 = instructionList.start;
        instructionHandle.next = instructionList.start;
        instructionList.start.prev = instructionHandle;
        instructionList.end.next = instructionHandle2;
        if (instructionHandle2 != null) {
            instructionHandle2.prev = instructionList.end;
        } else {
            this.end = instructionList.end;
        }
        this.length += instructionList.length;
        instructionList.clear();
        return instructionHandle3;
    }

    public InstructionHandle append(Instruction instruction, InstructionList instructionList) {
        InstructionHandle instructionHandleFindInstruction2 = findInstruction2(instruction);
        if (instructionHandleFindInstruction2 == null) {
            throw new ClassGenException("Instruction " + instruction + " is not contained in this list.");
        }
        return append(instructionHandleFindInstruction2, instructionList);
    }

    public InstructionHandle append(InstructionList instructionList) {
        if (!$assertionsDisabled && instructionList == null) {
            throw new AssertionError();
        }
        if (instructionList.isEmpty()) {
            return null;
        }
        if (!isEmpty()) {
            return append(this.end, instructionList);
        }
        this.start = instructionList.start;
        this.end = instructionList.end;
        this.length = instructionList.length;
        instructionList.clear();
        return this.start;
    }

    private void append(InstructionHandle instructionHandle) {
        if (isEmpty()) {
            this.end = instructionHandle;
            this.start = instructionHandle;
            instructionHandle.prev = null;
            instructionHandle.next = null;
        } else {
            this.end.next = instructionHandle;
            instructionHandle.prev = this.end;
            instructionHandle.next = null;
            this.end = instructionHandle;
        }
        this.length++;
    }

    public InstructionHandle append(Instruction instruction) {
        InstructionHandle instructionHandle = InstructionHandle.getInstructionHandle(instruction);
        append(instructionHandle);
        return instructionHandle;
    }

    public InstructionHandle appendDUP() {
        InstructionHandle instructionHandle = InstructionHandle.getInstructionHandle(InstructionConstants.DUP);
        append(instructionHandle);
        return instructionHandle;
    }

    public InstructionHandle appendNOP() {
        InstructionHandle instructionHandle = InstructionHandle.getInstructionHandle(InstructionConstants.NOP);
        append(instructionHandle);
        return instructionHandle;
    }

    public InstructionHandle appendPOP() {
        InstructionHandle instructionHandle = InstructionHandle.getInstructionHandle(InstructionConstants.POP);
        append(instructionHandle);
        return instructionHandle;
    }

    public BranchHandle append(InstructionBranch instructionBranch) {
        BranchHandle branchHandle = BranchHandle.getBranchHandle(instructionBranch);
        append(branchHandle);
        return branchHandle;
    }

    public InstructionHandle append(Instruction instruction, Instruction instruction2) {
        return append(instruction, new InstructionList(instruction2));
    }

    public InstructionHandle append(InstructionHandle instructionHandle, Instruction instruction) {
        return append(instructionHandle, new InstructionList(instruction));
    }

    public BranchHandle append(InstructionHandle instructionHandle, InstructionBranch instructionBranch) {
        BranchHandle branchHandle = BranchHandle.getBranchHandle(instructionBranch);
        InstructionList instructionList = new InstructionList();
        instructionList.append(branchHandle);
        append(instructionHandle, instructionList);
        return branchHandle;
    }

    public InstructionHandle insert(InstructionHandle instructionHandle, InstructionList instructionList) {
        if (instructionList == null) {
            throw new ClassGenException("Inserting null InstructionList");
        }
        if (instructionList.isEmpty()) {
            return instructionHandle;
        }
        InstructionHandle instructionHandle2 = instructionHandle.prev;
        InstructionHandle instructionHandle3 = instructionList.start;
        instructionHandle.prev = instructionList.end;
        instructionList.end.next = instructionHandle;
        instructionList.start.prev = instructionHandle2;
        if (instructionHandle2 != null) {
            instructionHandle2.next = instructionList.start;
        } else {
            this.start = instructionList.start;
        }
        this.length += instructionList.length;
        instructionList.clear();
        return instructionHandle3;
    }

    public InstructionHandle insert(InstructionList instructionList) {
        if (!isEmpty()) {
            return insert(this.start, instructionList);
        }
        append(instructionList);
        return this.start;
    }

    private void insert(InstructionHandle instructionHandle) {
        if (isEmpty()) {
            this.end = instructionHandle;
            this.start = instructionHandle;
            instructionHandle.prev = null;
            instructionHandle.next = null;
        } else {
            this.start.prev = instructionHandle;
            instructionHandle.next = this.start;
            instructionHandle.prev = null;
            this.start = instructionHandle;
        }
        this.length++;
    }

    public InstructionHandle insert(Instruction instruction, InstructionList instructionList) {
        InstructionHandle instructionHandleFindInstruction1 = findInstruction1(instruction);
        if (instructionHandleFindInstruction1 == null) {
            throw new ClassGenException("Instruction " + instruction + " is not contained in this list.");
        }
        return insert(instructionHandleFindInstruction1, instructionList);
    }

    public InstructionHandle insert(Instruction instruction) {
        InstructionHandle instructionHandle = InstructionHandle.getInstructionHandle(instruction);
        insert(instructionHandle);
        return instructionHandle;
    }

    public BranchHandle insert(InstructionBranch instructionBranch) {
        BranchHandle branchHandle = BranchHandle.getBranchHandle(instructionBranch);
        insert(branchHandle);
        return branchHandle;
    }

    public InstructionHandle insert(Instruction instruction, Instruction instruction2) {
        return insert(instruction, new InstructionList(instruction2));
    }

    public InstructionHandle insert(InstructionHandle instructionHandle, Instruction instruction) {
        return insert(instructionHandle, new InstructionList(instruction));
    }

    public BranchHandle insert(InstructionHandle instructionHandle, InstructionBranch instructionBranch) {
        BranchHandle branchHandle = BranchHandle.getBranchHandle(instructionBranch);
        InstructionList instructionList = new InstructionList();
        instructionList.append(branchHandle);
        insert(instructionHandle, instructionList);
        return branchHandle;
    }

    public void move(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, InstructionHandle instructionHandle3) {
        if (instructionHandle == null || instructionHandle2 == null) {
            throw new ClassGenException("Invalid null handle: From " + instructionHandle + " to " + instructionHandle2);
        }
        if (instructionHandle3 == instructionHandle || instructionHandle3 == instructionHandle2) {
            throw new ClassGenException("Invalid range: From " + instructionHandle + " to " + instructionHandle2 + " contains target " + instructionHandle3);
        }
        InstructionHandle instructionHandle4 = instructionHandle;
        while (true) {
            InstructionHandle instructionHandle5 = instructionHandle4;
            if (instructionHandle5 == instructionHandle2.next) {
                InstructionHandle instructionHandle6 = instructionHandle.prev;
                InstructionHandle instructionHandle7 = instructionHandle2.next;
                if (instructionHandle6 != null) {
                    instructionHandle6.next = instructionHandle7;
                } else {
                    this.start = instructionHandle7;
                }
                if (instructionHandle7 != null) {
                    instructionHandle7.prev = instructionHandle6;
                } else {
                    this.end = instructionHandle6;
                }
                instructionHandle2.next = null;
                instructionHandle.prev = null;
                if (instructionHandle3 == null) {
                    instructionHandle2.next = this.start;
                    this.start = instructionHandle;
                    return;
                }
                InstructionHandle instructionHandle8 = instructionHandle3.next;
                instructionHandle3.next = instructionHandle;
                instructionHandle.prev = instructionHandle3;
                instructionHandle2.next = instructionHandle8;
                if (instructionHandle8 != null) {
                    instructionHandle8.prev = instructionHandle2;
                    return;
                }
                return;
            }
            if (instructionHandle5 == null) {
                throw new ClassGenException("Invalid range: From " + instructionHandle + " to " + instructionHandle2);
            }
            if (instructionHandle5 == instructionHandle3) {
                throw new ClassGenException("Invalid range: From " + instructionHandle + " to " + instructionHandle2 + " contains target " + instructionHandle3);
            }
            instructionHandle4 = instructionHandle5.next;
        }
    }

    public void move(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        move(instructionHandle, instructionHandle, instructionHandle2);
    }

    private void remove(InstructionHandle instructionHandle, InstructionHandle instructionHandle2, boolean z) throws TargetLostException {
        InstructionHandle instructionHandle3;
        InstructionHandle instructionHandle4;
        if (instructionHandle == null && instructionHandle2 == null) {
            InstructionHandle instructionHandle5 = this.start;
            instructionHandle4 = instructionHandle5;
            instructionHandle3 = instructionHandle5;
            this.end = null;
            this.start = null;
        } else {
            if (instructionHandle == null) {
                instructionHandle3 = this.start;
                this.start = instructionHandle2;
            } else {
                instructionHandle3 = instructionHandle.next;
                instructionHandle.next = instructionHandle2;
            }
            if (instructionHandle2 == null) {
                instructionHandle4 = this.end;
                this.end = instructionHandle;
            } else {
                instructionHandle4 = instructionHandle2.prev;
                instructionHandle2.prev = instructionHandle;
            }
        }
        instructionHandle3.prev = null;
        instructionHandle4.next = null;
        if (z) {
            ArrayList arrayList = new ArrayList();
            InstructionHandle instructionHandle6 = instructionHandle3;
            while (true) {
                InstructionHandle instructionHandle7 = instructionHandle6;
                if (instructionHandle7 == null) {
                    break;
                }
                instructionHandle7.getInstruction().dispose();
                instructionHandle6 = instructionHandle7.next;
            }
            StringBuffer stringBuffer = new StringBuffer("{ ");
            InstructionHandle instructionHandle8 = instructionHandle3;
            while (true) {
                InstructionHandle instructionHandle9 = instructionHandle8;
                if (instructionHandle9 == null) {
                    break;
                }
                InstructionHandle instructionHandle10 = instructionHandle9.next;
                this.length--;
                boolean z2 = false;
                for (InstructionTargeter instructionTargeter : instructionHandle9.getTargeters()) {
                    if (instructionTargeter.getClass().getName().endsWith("ShadowRange") || instructionTargeter.getClass().getName().endsWith("ExceptionRange") || instructionTargeter.getClass().getName().endsWith("LineNumberTag")) {
                        z2 = true;
                    } else {
                        System.out.println(instructionTargeter.getClass());
                    }
                }
                if (z2) {
                    instructionHandle9.dispose();
                } else {
                    arrayList.add(instructionHandle9);
                    stringBuffer.append(instructionHandle9.toString(true) + SymbolConstants.SPACE_SYMBOL);
                    instructionHandle9.prev = null;
                    instructionHandle9.next = null;
                }
                instructionHandle8 = instructionHandle10;
            }
            stringBuffer.append("}");
            if (arrayList.isEmpty()) {
                return;
            }
            InstructionHandle[] instructionHandleArr = new InstructionHandle[arrayList.size()];
            arrayList.toArray(instructionHandleArr);
            throw new TargetLostException(instructionHandleArr, stringBuffer.toString());
        }
    }

    public void delete(InstructionHandle instructionHandle) throws TargetLostException {
        remove(instructionHandle.prev, instructionHandle.next, false);
    }

    public void delete(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) throws TargetLostException {
        remove(instructionHandle.prev, instructionHandle2.next, false);
    }

    public void delete(Instruction instruction, Instruction instruction2) throws TargetLostException {
        InstructionHandle instructionHandleFindInstruction1 = findInstruction1(instruction);
        if (instructionHandleFindInstruction1 == null) {
            throw new ClassGenException("Instruction " + instruction + " is not contained in this list.");
        }
        InstructionHandle instructionHandleFindInstruction2 = findInstruction2(instruction2);
        if (instructionHandleFindInstruction2 == null) {
            throw new ClassGenException("Instruction " + instruction2 + " is not contained in this list.");
        }
        delete(instructionHandleFindInstruction1, instructionHandleFindInstruction2);
    }

    private InstructionHandle findInstruction1(Instruction instruction) {
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 == null) {
                return null;
            }
            if (instructionHandle2.instruction == instruction) {
                return instructionHandle2;
            }
            instructionHandle = instructionHandle2.next;
        }
    }

    private InstructionHandle findInstruction2(Instruction instruction) {
        InstructionHandle instructionHandle = this.end;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 == null) {
                return null;
            }
            if (instructionHandle2.instruction == instruction) {
                return instructionHandle2;
            }
            instructionHandle = instructionHandle2.prev;
        }
    }

    public boolean contains(InstructionHandle instructionHandle) {
        if (instructionHandle == null) {
            return false;
        }
        InstructionHandle instructionHandle2 = this.start;
        while (true) {
            InstructionHandle instructionHandle3 = instructionHandle2;
            if (instructionHandle3 == null) {
                return false;
            }
            if (instructionHandle3 == instructionHandle) {
                return true;
            }
            instructionHandle2 = instructionHandle3.next;
        }
    }

    public boolean contains(Instruction instruction) {
        return findInstruction1(instruction) != null;
    }

    public void setPositions() {
        setPositions(false);
    }

    public void setPositions(boolean z) {
        int i = 0;
        int length = 0;
        int i2 = 0;
        int[] iArr = new int[this.length];
        if (z) {
            checkInstructionList();
        }
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 != null) {
                Instruction instruction = instructionHandle2.instruction;
                instructionHandle2.setPosition(length);
                int i3 = i2;
                i2++;
                iArr[i3] = length;
                switch (instruction.opcode) {
                    case 167:
                    case 168:
                        i += 2;
                        break;
                    case 170:
                    case 171:
                        i += 3;
                        break;
                }
                length += instruction.getLength();
                instructionHandle = instructionHandle2.next;
            } else {
                boolean z2 = false;
                int iUpdatePosition = 0;
                InstructionHandle instructionHandle3 = this.start;
                while (true) {
                    InstructionHandle instructionHandle4 = instructionHandle3;
                    if (instructionHandle4 == null) {
                        if (z2) {
                            i2 = 0;
                            int length2 = 0;
                            InstructionHandle instructionHandle5 = this.start;
                            while (true) {
                                InstructionHandle instructionHandle6 = instructionHandle5;
                                if (instructionHandle6 != null) {
                                    Instruction instruction2 = instructionHandle6.instruction;
                                    instructionHandle6.setPosition(length2);
                                    int i4 = i2;
                                    i2++;
                                    iArr[i4] = length2;
                                    length2 += instruction2.getLength();
                                    instructionHandle5 = instructionHandle6.next;
                                }
                            }
                        }
                        this.positions = new int[i2];
                        System.arraycopy(iArr, 0, this.positions, 0, i2);
                        return;
                    }
                    if (instructionHandle4 instanceof BranchHandle) {
                        iUpdatePosition += ((BranchHandle) instructionHandle4).updatePosition(iUpdatePosition, i);
                        if (iUpdatePosition != 0) {
                            z2 = true;
                        }
                    }
                    instructionHandle3 = instructionHandle4.next;
                }
            }
        }
    }

    private void checkInstructionList() {
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 == null) {
                return;
            }
            Instruction instruction = instructionHandle2.instruction;
            if (instruction instanceof InstructionBranch) {
                Instruction instruction2 = ((InstructionBranch) instruction).getTarget().instruction;
                if (!contains(instruction2)) {
                    throw new ClassGenException("Branch target of " + Constants.OPCODE_NAMES[instruction.opcode] + ":" + instruction2 + " not in instruction list");
                }
                if (instruction instanceof InstructionSelect) {
                    for (InstructionHandle instructionHandle3 : ((InstructionSelect) instruction).getTargets()) {
                        instruction2 = instructionHandle3.instruction;
                        if (!contains(instruction2)) {
                            throw new ClassGenException("Branch target of " + Constants.OPCODE_NAMES[instruction.opcode] + ":" + instruction2 + " not in instruction list");
                        }
                    }
                }
                if (!(instructionHandle2 instanceof BranchHandle)) {
                    throw new ClassGenException("Branch instruction " + Constants.OPCODE_NAMES[instruction.opcode] + ":" + instruction2 + " not contained in BranchHandle.");
                }
            }
            instructionHandle = instructionHandle2.next;
        }
    }

    public byte[] getByteCode() {
        setPositions();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            for (InstructionHandle instructionHandle = this.start; instructionHandle != null; instructionHandle = instructionHandle.next) {
                instructionHandle.instruction.dump(dataOutputStream);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (byteArray.length > 65536) {
                throw new ClassGenException("Code size too big: " + byteArray.length);
            }
            return byteArray;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }

    public Instruction[] getInstructions() {
        ByteSequence byteSequence = new ByteSequence(getByteCode());
        ArrayList arrayList = new ArrayList();
        while (byteSequence.available() > 0) {
            try {
                arrayList.add(Instruction.readInstruction(byteSequence));
            } catch (IOException e) {
                throw new ClassGenException(e.toString());
            }
        }
        Instruction[] instructionArr = new Instruction[arrayList.size()];
        arrayList.toArray(instructionArr);
        return instructionArr;
    }

    public String toString() {
        return toString(true);
    }

    public String toString(boolean z) {
        StringBuffer stringBuffer = new StringBuffer();
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 == null) {
                return stringBuffer.toString();
            }
            stringBuffer.append(instructionHandle2.toString(z) + ScriptUtils.FALLBACK_STATEMENT_SEPARATOR);
            instructionHandle = instructionHandle2.next;
        }
    }

    public Iterator iterator() {
        return new Iterator() { // from class: org.aspectj.apache.bcel.generic.InstructionList.1
            private InstructionHandle ih;

            {
                this.ih = InstructionList.this.start;
            }

            @Override // java.util.Iterator
            public Object next() {
                InstructionHandle instructionHandle = this.ih;
                this.ih = this.ih.next;
                return instructionHandle;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.ih != null;
            }
        };
    }

    public InstructionHandle[] getInstructionHandles() {
        InstructionHandle[] instructionHandleArr = new InstructionHandle[this.length];
        InstructionHandle instructionHandle = this.start;
        for (int i = 0; i < this.length; i++) {
            instructionHandleArr[i] = instructionHandle;
            instructionHandle = instructionHandle.next;
        }
        return instructionHandleArr;
    }

    public int[] getInstructionPositions() {
        return this.positions;
    }

    public InstructionList copy() {
        HashMap map = new HashMap();
        InstructionList instructionList = new InstructionList();
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 == null) {
                break;
            }
            Instruction instructionCopy = instructionHandle2.instruction.copy();
            if (instructionCopy instanceof InstructionBranch) {
                map.put(instructionHandle2, instructionList.append((InstructionBranch) instructionCopy));
            } else {
                map.put(instructionHandle2, instructionList.append(instructionCopy));
            }
            instructionHandle = instructionHandle2.next;
        }
        InstructionHandle instructionHandle3 = this.start;
        InstructionHandle instructionHandle4 = instructionList.start;
        while (true) {
            InstructionHandle instructionHandle5 = instructionHandle4;
            if (instructionHandle3 == null) {
                return instructionList;
            }
            Instruction instruction = instructionHandle3.instruction;
            Instruction instruction2 = instructionHandle5.instruction;
            if (instruction instanceof InstructionBranch) {
                InstructionBranch instructionBranch = (InstructionBranch) instruction;
                InstructionBranch instructionBranch2 = (InstructionBranch) instruction2;
                instructionBranch2.setTarget((InstructionHandle) map.get(instructionBranch.getTarget()));
                if (instructionBranch instanceof InstructionSelect) {
                    InstructionHandle[] targets = ((InstructionSelect) instructionBranch).getTargets();
                    InstructionHandle[] targets2 = ((InstructionSelect) instructionBranch2).getTargets();
                    for (int i = 0; i < targets.length; i++) {
                        targets2[i] = (InstructionHandle) map.get(targets[i]);
                    }
                }
            }
            instructionHandle3 = instructionHandle3.next;
            instructionHandle4 = instructionHandle5.next;
        }
    }

    public void replaceConstantPool(ConstantPool constantPool, ConstantPool constantPool2) {
        InstructionHandle instructionHandle = this.start;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 == null) {
                return;
            }
            Instruction instruction = instructionHandle2.instruction;
            if (instruction.isConstantPoolInstruction()) {
                InstructionCP instructionCP = (InstructionCP) instruction;
                instructionCP.setIndex(constantPool2.addConstant(constantPool.getConstant(instructionCP.getIndex()), constantPool));
            }
            instructionHandle = instructionHandle2.next;
        }
    }

    private void clear() {
        this.end = null;
        this.start = null;
        this.length = 0;
    }

    public void dispose() {
        InstructionHandle instructionHandle = this.end;
        while (true) {
            InstructionHandle instructionHandle2 = instructionHandle;
            if (instructionHandle2 == null) {
                clear();
                return;
            } else {
                instructionHandle2.dispose();
                instructionHandle = instructionHandle2.prev;
            }
        }
    }

    public InstructionHandle getStart() {
        return this.start;
    }

    public InstructionHandle getEnd() {
        return this.end;
    }

    public int getLength() {
        return this.length;
    }

    public int size() {
        return this.length;
    }

    public void redirectBranches(InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        InstructionHandle instructionHandle3 = this.start;
        while (true) {
            InstructionHandle instructionHandle4 = instructionHandle3;
            if (instructionHandle4 == null) {
                return;
            }
            Instruction instruction = instructionHandle4.getInstruction();
            if (instruction instanceof InstructionBranch) {
                InstructionBranch instructionBranch = (InstructionBranch) instruction;
                if (instructionBranch.getTarget() == instructionHandle) {
                    instructionBranch.setTarget(instructionHandle2);
                }
                if (instructionBranch instanceof InstructionSelect) {
                    InstructionHandle[] targets = ((InstructionSelect) instructionBranch).getTargets();
                    for (int i = 0; i < targets.length; i++) {
                        if (targets[i] == instructionHandle) {
                            ((InstructionSelect) instructionBranch).setTarget(i, instructionHandle2);
                        }
                    }
                }
            }
            instructionHandle3 = instructionHandle4.next;
        }
    }

    public void redirectLocalVariables(LocalVariableGen[] localVariableGenArr, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        for (int i = 0; i < localVariableGenArr.length; i++) {
            InstructionHandle start = localVariableGenArr[i].getStart();
            InstructionHandle end = localVariableGenArr[i].getEnd();
            if (start == instructionHandle) {
                localVariableGenArr[i].setStart(instructionHandle2);
            }
            if (end == instructionHandle) {
                localVariableGenArr[i].setEnd(instructionHandle2);
            }
        }
    }

    public void redirectExceptionHandlers(CodeExceptionGen[] codeExceptionGenArr, InstructionHandle instructionHandle, InstructionHandle instructionHandle2) {
        for (int i = 0; i < codeExceptionGenArr.length; i++) {
            if (codeExceptionGenArr[i].getStartPC() == instructionHandle) {
                codeExceptionGenArr[i].setStartPC(instructionHandle2);
            }
            if (codeExceptionGenArr[i].getEndPC() == instructionHandle) {
                codeExceptionGenArr[i].setEndPC(instructionHandle2);
            }
            if (codeExceptionGenArr[i].getHandlerPC() == instructionHandle) {
                codeExceptionGenArr[i].setHandlerPC(instructionHandle2);
            }
        }
    }

    static {
        $assertionsDisabled = !InstructionList.class.desiredAssertionStatus();
    }
}
