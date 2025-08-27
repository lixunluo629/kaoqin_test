package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/AspectInstanceVar.class */
public class AspectInstanceVar extends BcelVar {
    public AspectInstanceVar(ResolvedType type) {
        super(type, -1);
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public Instruction createLoad(InstructionFactory fact) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public Instruction createStore(InstructionFactory fact) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void appendStore(InstructionList il, InstructionFactory fact) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void appendLoad(InstructionList il, InstructionFactory fact) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void appendLoadAndConvert(InstructionList il, InstructionFactory fact, ResolvedType toType) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void insertLoad(InstructionList il, InstructionFactory fact) {
        InstructionList loadInstructions = new InstructionList();
        loadInstructions.append(fact.createInvoke(getType().getName(), "aspectOf", "()" + getType().getSignature(), (short) 184));
        il.insert(loadInstructions);
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public InstructionList createCopyFrom(InstructionFactory fact, int oldSlot) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    void appendConvertableArrayLoad(InstructionList il, InstructionFactory fact, int index, ResolvedType convertTo) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    void appendConvertableArrayStore(InstructionList il, InstructionFactory fact, int index, BcelVar storee) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    InstructionList createConvertableArrayStore(InstructionFactory fact, int index, BcelVar storee) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    InstructionList createConvertableArrayLoad(InstructionFactory fact, int index, ResolvedType convertTo) {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public int getPositionInAroundState() {
        throw new IllegalStateException();
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void setPositionInAroundState(int positionInAroundState) {
        throw new IllegalStateException();
    }
}
