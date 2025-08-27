package org.aspectj.weaver.bcel;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import org.aspectj.apache.bcel.generic.FieldGen;
import org.aspectj.apache.bcel.generic.InstructionFactory;
import org.aspectj.apache.bcel.generic.InstructionList;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.aspectj.apache.bcel.generic.Type;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.NameMangler;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelCflowCounterFieldAdder.class */
public class BcelCflowCounterFieldAdder extends BcelTypeMunger {
    private ResolvedMember cflowCounterField;

    public BcelCflowCounterFieldAdder(ResolvedMember cflowCounterField) {
        super(null, (ResolvedType) cflowCounterField.getDeclaringType());
        this.cflowCounterField = cflowCounterField;
    }

    @Override // org.aspectj.weaver.bcel.BcelTypeMunger
    public boolean munge(BcelClassWeaver weaver) {
        LazyClassGen gen = weaver.getLazyClassGen();
        if (!gen.getType().equals(this.cflowCounterField.getDeclaringType())) {
            return false;
        }
        FieldGen f = new FieldGen(this.cflowCounterField.getModifiers(), BcelWorld.makeBcelType(this.cflowCounterField.getReturnType()), this.cflowCounterField.getName(), gen.getConstantPool());
        gen.addField(f, getSourceLocation());
        LazyMethodGen clinit = gen.getAjcPreClinit();
        InstructionList setup = new InstructionList();
        InstructionFactory fact = gen.getFactory();
        setup.append(fact.createNew(new ObjectType(NameMangler.CFLOW_COUNTER_TYPE)));
        setup.append(InstructionFactory.createDup(1));
        setup.append(fact.createInvoke(NameMangler.CFLOW_COUNTER_TYPE, "<init>", Type.VOID, new Type[0], (short) 183));
        setup.append(Utility.createSet(fact, this.cflowCounterField));
        clinit.getBody().insert(setup);
        return true;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ResolvedMember getMatchingSyntheticMember(Member member) {
        return null;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public ResolvedMember getSignature() {
        return this.cflowCounterField;
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public boolean matches(ResolvedType onType) {
        return onType.equals(this.cflowCounterField.getDeclaringType());
    }

    @Override // org.aspectj.weaver.ConcreteTypeMunger
    public boolean existsToSupportShadowMunging() {
        return true;
    }

    @Override // org.aspectj.weaver.bcel.BcelTypeMunger
    public String toString() {
        return "(BcelTypeMunger: CflowField " + this.cflowCounterField.getDeclaringType().getName() + SymbolConstants.SPACE_SYMBOL + this.cflowCounterField.getName() + ")";
    }
}
