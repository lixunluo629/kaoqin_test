package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.generic.Instruction;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.UnresolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/TypeAnnotationAccessVar.class */
public class TypeAnnotationAccessVar extends BcelVar {
    private BcelVar target;

    public TypeAnnotationAccessVar(ResolvedType type, BcelVar theAnnotatedTargetIsStoredHere) {
        super(type, 0);
        this.target = theAnnotatedTargetIsStoredHere;
    }

    @Override // org.aspectj.weaver.bcel.BcelVar, org.aspectj.weaver.ast.Var
    public String toString() {
        return "TypeAnnotationAccessVar(" + getType() + ")";
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
        Type jlClass = BcelWorld.makeBcelType(UnresolvedType.JL_CLASS);
        Type jlaAnnotation = BcelWorld.makeBcelType(UnresolvedType.ANNOTATION);
        il.append(this.target.createLoad(fact));
        il.append(fact.createInvoke("java/lang/Object", "getClass", jlClass, new Type[0], (short) 182));
        il.append(fact.createConstant(new ObjectType(toType.getName())));
        il.append(fact.createInvoke("java/lang/Class", "getAnnotation", jlaAnnotation, new Type[]{jlClass}, (short) 182));
        il.append(Utility.createConversion(fact, jlaAnnotation, BcelWorld.makeBcelType(toType)));
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
