package org.aspectj.weaver.bcel;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ast.Var;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelVar.class */
public class BcelVar extends Var {
    private int positionInAroundState;
    private int slot;
    public static final BcelVar[] NONE = new BcelVar[0];

    public BcelVar(ResolvedType type, int slot) {
        super(type);
        this.positionInAroundState = -1;
        this.slot = slot;
    }

    @Override // org.aspectj.weaver.ast.Var
    public String toString() {
        return "BcelVar(" + getType() + SymbolConstants.SPACE_SYMBOL + this.slot + (this.positionInAroundState != -1 ? SymbolConstants.SPACE_SYMBOL + this.positionInAroundState : "") + ")";
    }

    public int getSlot() {
        return this.slot;
    }

    public Instruction createLoad(InstructionFactory fact) {
        return InstructionFactory.createLoad(BcelWorld.makeBcelType(getType()), this.slot);
    }

    public Instruction createStore(InstructionFactory fact) {
        return InstructionFactory.createStore(BcelWorld.makeBcelType(getType()), this.slot);
    }

    public void appendStore(InstructionList il, InstructionFactory fact) {
        il.append(createStore(fact));
    }

    public void appendLoad(InstructionList il, InstructionFactory fact) {
        il.append(createLoad(fact));
    }

    public void appendLoadAndConvert(InstructionList il, InstructionFactory fact, ResolvedType toType) {
        il.append(createLoad(fact));
        Utility.appendConversion(il, fact, getType(), toType);
    }

    public void insertLoad(InstructionList il, InstructionFactory fact) {
        il.insert(createLoad(fact));
    }

    public InstructionList createCopyFrom(InstructionFactory fact, int oldSlot) {
        InstructionList il = new InstructionList();
        il.append(InstructionFactory.createLoad(BcelWorld.makeBcelType(getType()), oldSlot));
        il.append(createStore(fact));
        return il;
    }

    void appendConvertableArrayLoad(InstructionList il, InstructionFactory fact, int index, ResolvedType convertTo) {
        ResolvedType convertFromType = getType().getResolvedComponentType();
        appendLoad(il, fact);
        il.append(Utility.createConstant(fact, index));
        il.append(InstructionFactory.createArrayLoad(BcelWorld.makeBcelType(convertFromType)));
        Utility.appendConversion(il, fact, convertFromType, convertTo);
    }

    void appendConvertableArrayStore(InstructionList il, InstructionFactory fact, int index, BcelVar storee) {
        ResolvedType convertToType = getType().getResolvedComponentType();
        appendLoad(il, fact);
        il.append(Utility.createConstant(fact, index));
        storee.appendLoad(il, fact);
        Utility.appendConversion(il, fact, storee.getType(), convertToType);
        il.append(InstructionFactory.createArrayStore(BcelWorld.makeBcelType(convertToType)));
    }

    InstructionList createConvertableArrayStore(InstructionFactory fact, int index, BcelVar storee) {
        InstructionList il = new InstructionList();
        appendConvertableArrayStore(il, fact, index, storee);
        return il;
    }

    InstructionList createConvertableArrayLoad(InstructionFactory fact, int index, ResolvedType convertTo) {
        InstructionList il = new InstructionList();
        appendConvertableArrayLoad(il, fact, index, convertTo);
        return il;
    }

    public int getPositionInAroundState() {
        return this.positionInAroundState;
    }

    public void setPositionInAroundState(int positionInAroundState) {
        this.positionInAroundState = positionInAroundState;
    }
}
