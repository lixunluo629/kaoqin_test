package org.aspectj.weaver.bcel;

import org.aspectj.weaver.Advice;
import org.aspectj.weaver.AjAttribute;
import org.aspectj.weaver.ConcreteTypeMunger;
import org.aspectj.weaver.IWeavingSupport;
import org.aspectj.weaver.Member;
import org.aspectj.weaver.ResolvedMember;
import org.aspectj.weaver.ResolvedType;
import org.aspectj.weaver.ResolvedTypeMunger;
import org.aspectj.weaver.ast.Var;
import org.aspectj.weaver.patterns.PerClause;
import org.aspectj.weaver.patterns.Pointcut;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/weaver/bcel/BcelWeavingSupport.class */
public class BcelWeavingSupport implements IWeavingSupport {
    @Override // org.aspectj.weaver.IWeavingSupport
    public Advice createAdviceMunger(AjAttribute.AdviceAttribute attribute, Pointcut pointcut, Member signature, ResolvedType concreteAspect) {
        return new BcelAdvice(attribute, pointcut, signature, concreteAspect);
    }

    @Override // org.aspectj.weaver.IWeavingSupport
    public ConcreteTypeMunger makeCflowStackFieldAdder(ResolvedMember cflowField) {
        return new BcelCflowStackFieldAdder(cflowField);
    }

    @Override // org.aspectj.weaver.IWeavingSupport
    public ConcreteTypeMunger makeCflowCounterFieldAdder(ResolvedMember cflowField) {
        return new BcelCflowCounterFieldAdder(cflowField);
    }

    @Override // org.aspectj.weaver.IWeavingSupport
    public ConcreteTypeMunger makePerClauseAspect(ResolvedType aspect, PerClause.Kind kind) {
        return new BcelPerClauseAspectAdder(aspect, kind);
    }

    @Override // org.aspectj.weaver.IWeavingSupport
    public Var makeCflowAccessVar(ResolvedType formalType, Member cflowField, int arrayIndex) {
        return new BcelCflowAccessVar(formalType, cflowField, arrayIndex);
    }

    @Override // org.aspectj.weaver.IWeavingSupport
    public ConcreteTypeMunger concreteTypeMunger(ResolvedTypeMunger munger, ResolvedType aspectType) {
        return new BcelTypeMunger(munger, aspectType);
    }

    @Override // org.aspectj.weaver.IWeavingSupport
    public ConcreteTypeMunger createAccessForInlineMunger(ResolvedType aspect) {
        return new BcelAccessForInlineMunger(aspect);
    }
}
