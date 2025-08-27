package org.aspectj.weaver.bcel;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelFieldRef.class */
public class BcelFieldRef extends BcelVar {
    private String className;
    private String fieldName;

    public BcelFieldRef(ResolvedType type, String className, String fieldName) {
        super(type, 0);
        this.className = className;
        this.fieldName = fieldName;
    }

    @Override // org.aspectj.weaver.bcel.BcelVar, org.aspectj.weaver.ast.Var
    public String toString() {
        return "BcelFieldRef(" + getType() + SymbolConstants.SPACE_SYMBOL + this.className + "." + this.fieldName + ")";
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public Instruction createLoad(InstructionFactory fact) {
        return fact.createFieldAccess(this.className, this.fieldName, BcelWorld.makeBcelType(getType()), (short) 178);
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public Instruction createStore(InstructionFactory fact) {
        return fact.createFieldAccess(this.className, this.fieldName, BcelWorld.makeBcelType(getType()), (short) 179);
    }

    @Override // org.aspectj.weaver.bcel.BcelVar
    public InstructionList createCopyFrom(InstructionFactory fact, int oldSlot) {
        throw new RuntimeException("unimplemented");
    }
}
