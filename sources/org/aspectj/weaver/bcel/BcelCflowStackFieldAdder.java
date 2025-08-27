package org.aspectj.weaver.bcel;

import org.aspectj.apache.bcel.generic.FieldGen;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelCflowStackFieldAdder.class */
public class BcelCflowStackFieldAdder extends BcelTypeMunger {
    private ResolvedMember cflowStackField;

    public BcelCflowStackFieldAdder(ResolvedMember cflowStackField) {
        super(null, (ResolvedType) cflowStackField.getDeclaringType());
        this.cflowStackField = cflowStackField;
    }

    @Override // org.aspectj.weaver.bcel.BcelTypeMunger
    public boolean munge(BcelClassWeaver weaver) {
        LazyClassGen gen = weaver.getLazyClassGen();
        if (!gen.getType().equals(this.cflowStackField.getDeclaringType())) {
            return false;
        }
        FieldGen f = new FieldGen(this.cflowStackField.getModifiers(), BcelWorld.makeBcelType(this.cflowStackField.getReturnType()), this.cflowStackField.getName(), gen.getConstantPool());
        gen.addField(f, getSourceLocation());
        LazyMethodGen clinit = gen.getAjcPreClinit();
        InstructionList setup = new InstructionList();
        InstructionFactory fact = gen.getFactory();
        setup.append(fact.createNew(NameMangler.CFLOW_STACK_TYPE));
        setup.append(InstructionFactory.createDup(1));
        setup.append(fact.createInvoke(NameMangler.CFLOW_STACK_TYPE, "<init>", Type.VOID, Type.NO_ARGS, (short) 183));
        setup.append(Utility.createSet(fact, this.cflowStackField));
        clinit.getBody().insert(setup);
        return true;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ResolvedMember getMatchingSyntheticMember(Member member) {
        return null;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ResolvedMember getSignature() {
        return this.cflowStackField;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public boolean matches(ResolvedType onType) {
        return onType.equals(this.cflowStackField.getDeclaringType());
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public boolean existsToSupportShadowMunging() {
        return true;
    }
}
