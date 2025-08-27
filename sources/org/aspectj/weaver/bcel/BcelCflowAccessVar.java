package org.aspectj.weaver.bcel;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelCflowAccessVar.class */
public class BcelCflowAccessVar extends BcelVar {
    private Member stackField;
    private int index;

    public BcelCflowAccessVar(ResolvedType type, Member stackField, int index) {
        super(type, 0);
        this.stackField = stackField;
        this.index = index;
    }

    @Override // org.aspectj.weaver.bcel.BcelVar, org.aspectj.weaver.ast.Var
    public String toString() {
        return "BcelCflowAccessVar(" + getType() + SymbolConstants.SPACE_SYMBOL + this.stackField + "." + this.index + ")";
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public Instruction createLoad(InstructionFactory fact) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public Instruction createStore(InstructionFactory fact) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public InstructionList createCopyFrom(InstructionFactory fact, int oldSlot) {
        throw new RuntimeException("unimplemented");
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void appendLoad(InstructionList il, InstructionFactory fact) {
        il.append(createLoadInstructions(getType(), fact));
    }

    public InstructionList createLoadInstructions(ResolvedType toType, InstructionFactory fact) {
        InstructionList il = new InstructionList();
        il.append(Utility.createGet(fact, this.stackField));
        il.append(Utility.createConstant(fact, this.index));
        il.append(fact.createInvoke(NameMangler.CFLOW_STACK_TYPE, BeanUtil.PREFIX_GETTER_GET, Type.OBJECT, new Type[]{Type.INT}, (short) 182));
        il.append(Utility.createConversion(fact, Type.OBJECT, BcelWorld.makeBcelType(toType)));
        return il;
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void appendLoadAndConvert(InstructionList il, InstructionFactory fact, ResolvedType toType) {
        il.append(createLoadInstructions(toType, fact));
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public void insertLoad(InstructionList il, InstructionFactory fact) {
        il.insert(createLoadInstructions(getType(), fact));
    }
}
